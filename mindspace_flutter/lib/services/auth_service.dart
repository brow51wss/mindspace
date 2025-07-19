// Authentication Service for MindSpace Flutter App
// Handles all Firebase Auth operations and user state management

import 'dart:async';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:cloud_firestore/cloud_firestore.dart';
import '../models/auth_models.dart';

class AuthService {
  static final AuthService _instance = AuthService._internal();
  factory AuthService() => _instance;
  AuthService._internal();

  // Firebase instances
  final FirebaseAuth _auth = FirebaseAuth.instance;
  final FirebaseFirestore _firestore = FirebaseFirestore.instance;

  // Stream controllers for authentication state
  final StreamController<AuthState> _authStateController = 
      StreamController<AuthState>.broadcast();
  final StreamController<MindSpaceUser?> _userController = 
      StreamController<MindSpaceUser?>.broadcast();

  // Current user and state
  MindSpaceUser? _currentUser;
  AuthState _currentState = AuthState.loading;

  // Public getters
  MindSpaceUser? get currentUser => _currentUser;
  AuthState get currentState => _currentState;
  Stream<AuthState> get authStateStream => _authStateController.stream;
  Stream<MindSpaceUser?> get userStream => _userController.stream;
  bool get isAuthenticated => _currentUser != null;

  /// Initialize the authentication service
  Future<void> initialize() async {
    try {
      // Listen to Firebase Auth state changes
      _auth.authStateChanges().listen(_handleAuthStateChange);
      
      // Check if user is already signed in
      final firebaseUser = _auth.currentUser;
      if (firebaseUser != null) {
        await _loadUserProfile(firebaseUser);
      } else {
        _updateAuthState(AuthState.unauthenticated);
      }
    } catch (e) {
      print('AuthService initialization error: $e');
      _updateAuthState(AuthState.unauthenticated);
    }
  }

  /// Handle Firebase Auth state changes
  Future<void> _handleAuthStateChange(User? firebaseUser) async {
    if (firebaseUser != null) {
      await _loadUserProfile(firebaseUser);
    } else {
      _currentUser = null;
      _updateAuthState(AuthState.unauthenticated);
      _userController.add(null);
    }
  }

  /// Load user profile from Firestore
  Future<void> _loadUserProfile(User firebaseUser) async {
    try {
      _updateAuthState(AuthState.loading);
      
      final userDoc = await _firestore
          .collection('users')
          .doc(firebaseUser.uid)
          .get();

      if (userDoc.exists && userDoc.data() != null) {
        _currentUser = MindSpaceUser.fromFirestore(userDoc.data()!);
      } else {
        // Create new user profile if doesn't exist
        _currentUser = MindSpaceUser.fromFirebaseUser(firebaseUser);
        await _saveUserProfile(_currentUser!);
      }

      _updateAuthState(AuthState.authenticated);
      _userController.add(_currentUser);
    } catch (e) {
      print('Error loading user profile: $e');
      _updateAuthState(AuthState.unauthenticated);
    }
  }

  /// Save user profile to Firestore
  Future<void> _saveUserProfile(MindSpaceUser user) async {
    try {
      await _firestore
          .collection('users')
          .doc(user.uid)
          .set(user.toFirestore(), SetOptions(merge: true));
    } catch (e) {
      print('Error saving user profile: $e');
    }
  }

  /// Update authentication state
  void _updateAuthState(AuthState newState) {
    _currentState = newState;
    _authStateController.add(newState);
  }

  /// Sign up with email and password
  Future<AuthResult> signUpWithEmail(String email, String password) async {
    try {
      _updateAuthState(AuthState.loading);

      // Validate input
      final emailError = EmailValidator.validate(email);
      if (emailError != null) {
        _updateAuthState(AuthState.unauthenticated);
        return AuthResult.error(emailError, AuthErrorType.invalidEmail);
      }

      final passwordError = PasswordValidator.validate(password);
      if (passwordError != null) {
        _updateAuthState(AuthState.unauthenticated);
        return AuthResult.error(passwordError, AuthErrorType.weakPassword);
      }

      // Create Firebase user
      final credential = await _auth.createUserWithEmailAndPassword(
        email: email,
        password: password,
      );

      if (credential.user != null) {
        // User profile will be created automatically by auth state listener
        await Future.delayed(const Duration(milliseconds: 500)); // Wait for state update
        return AuthResult.success(_currentUser!);
      } else {
        _updateAuthState(AuthState.unauthenticated);
        return AuthResult.error('Failed to create account', AuthErrorType.unknown);
      }
    } on FirebaseAuthException catch (e) {
      _updateAuthState(AuthState.unauthenticated);
      final errorType = AuthErrorParser.parseErrorCode(e.code);
      final errorMessage = AuthErrorParser.getErrorMessage(e.code);
      return AuthResult.error(errorMessage, errorType);
    } catch (e) {
      _updateAuthState(AuthState.unauthenticated);
      return AuthResult.error('An unexpected error occurred', AuthErrorType.unknown);
    }
  }

  /// Sign in with email and password
  Future<AuthResult> signInWithEmail(String email, String password) async {
    try {
      _updateAuthState(AuthState.loading);

      // Validate input
      final emailError = EmailValidator.validate(email);
      if (emailError != null) {
        _updateAuthState(AuthState.unauthenticated);
        return AuthResult.error(emailError, AuthErrorType.invalidEmail);
      }

      if (password.isEmpty) {
        _updateAuthState(AuthState.unauthenticated);
        return AuthResult.error('Password is required', AuthErrorType.wrongPassword);
      }

      // Sign in with Firebase
      final credential = await _auth.signInWithEmailAndPassword(
        email: email,
        password: password,
      );

      if (credential.user != null) {
        // Update last login time
        if (_currentUser != null) {
          final updatedUser = _currentUser!.copyWith(
            lastLoginAt: DateTime.now(),
          );
          await _saveUserProfile(updatedUser);
        }
        
        await Future.delayed(const Duration(milliseconds: 500)); // Wait for state update
        return AuthResult.success(_currentUser!);
      } else {
        _updateAuthState(AuthState.unauthenticated);
        return AuthResult.error('Failed to sign in', AuthErrorType.unknown);
      }
    } on FirebaseAuthException catch (e) {
      _updateAuthState(AuthState.unauthenticated);
      final errorType = AuthErrorParser.parseErrorCode(e.code);
      final errorMessage = AuthErrorParser.getErrorMessage(e.code);
      return AuthResult.error(errorMessage, errorType);
    } catch (e) {
      _updateAuthState(AuthState.unauthenticated);
      return AuthResult.error('An unexpected error occurred', AuthErrorType.unknown);
    }
  }

  /// Sign out
  Future<void> signOut() async {
    try {
      await _auth.signOut();
      _currentUser = null;
      _updateAuthState(AuthState.unauthenticated);
      _userController.add(null);
    } catch (e) {
      print('Error signing out: $e');
    }
  }

  /// Send password reset email
  Future<AuthResult> sendPasswordResetEmail(String email) async {
    try {
      // Validate email
      final emailError = EmailValidator.validate(email);
      if (emailError != null) {
        return AuthResult.error(emailError, AuthErrorType.invalidEmail);
      }

      await _auth.sendPasswordResetEmail(email: email);
      return AuthResult.success(_currentUser!); // Success with no user data needed
    } on FirebaseAuthException catch (e) {
      final errorType = AuthErrorParser.parseErrorCode(e.code);
      final errorMessage = AuthErrorParser.getErrorMessage(e.code);
      return AuthResult.error(errorMessage, errorType);
    } catch (e) {
      return AuthResult.error('Failed to send reset email', AuthErrorType.unknown);
    }
  }

  /// Update user profile
  Future<bool> updateProfile({String? displayName, String? photoUrl}) async {
    try {
      final firebaseUser = _auth.currentUser;
      if (firebaseUser == null || _currentUser == null) return false;

      // Update Firebase user profile
      await firebaseUser.updateProfile(
        displayName: displayName ?? firebaseUser.displayName,
        photoURL: photoUrl ?? firebaseUser.photoURL,
      );

      // Update local user model
      final updatedUser = _currentUser!.copyWith(
        displayName: displayName ?? _currentUser!.displayName,
        photoUrl: photoUrl ?? _currentUser!.photoUrl,
      );

      // Save to Firestore
      await _saveUserProfile(updatedUser);

      // Update current user and notify listeners
      _currentUser = updatedUser;
      _userController.add(_currentUser);

      return true;
    } catch (e) {
      print('Error updating profile: $e');
      return false;
    }
  }

  /// Delete user account
  Future<AuthResult> deleteAccount() async {
    try {
      final firebaseUser = _auth.currentUser;
      if (firebaseUser == null || _currentUser == null) {
        return AuthResult.error('No user to delete', AuthErrorType.userNotFound);
      }

      // Delete user document from Firestore
      await _firestore.collection('users').doc(_currentUser!.uid).delete();

      // Delete Firebase user
      await firebaseUser.delete();

      _currentUser = null;
      _updateAuthState(AuthState.unauthenticated);
      _userController.add(null);

      return AuthResult.success(_currentUser!); // Success with no user data
    } on FirebaseAuthException catch (e) {
      final errorType = AuthErrorParser.parseErrorCode(e.code);
      final errorMessage = AuthErrorParser.getErrorMessage(e.code);
      return AuthResult.error(errorMessage, errorType);
    } catch (e) {
      return AuthResult.error('Failed to delete account', AuthErrorType.unknown);
    }
  }

  /// Check if email is already in use
  Future<bool> isEmailInUse(String email) async {
    try {
      final methods = await _auth.fetchSignInMethodsForEmail(email);
      return methods.isNotEmpty;
    } catch (e) {
      return false;
    }
  }

  /// Get user statistics
  Future<Map<String, dynamic>> getUserStats() async {
    if (_currentUser == null) return {};

    try {
      // This would integrate with other collections for user stats
      // For now, return basic info
      return {
        'accountCreated': _currentUser!.createdAt.toIso8601String(),
        'lastLogin': _currentUser!.lastLoginAt.toIso8601String(),
        'daysSinceJoined': DateTime.now().difference(_currentUser!.createdAt).inDays,
      };
    } catch (e) {
      print('Error getting user stats: $e');
      return {};
    }
  }

  /// Dispose streams
  void dispose() {
    _authStateController.close();
    _userController.close();
  }
} 