// Authentication Models for MindSpace Flutter App
// Handles user data, authentication states, and validation

import 'package:firebase_auth/firebase_auth.dart';

/// User profile data model
class MindSpaceUser {
  final String uid;
  final String email;
  final String? displayName;
  final String? photoUrl;
  final DateTime createdAt;
  final DateTime lastLoginAt;
  final Map<String, dynamic>? preferences;

  MindSpaceUser({
    required this.uid,
    required this.email,
    this.displayName,
    this.photoUrl,
    required this.createdAt,
    required this.lastLoginAt,
    this.preferences,
  });

  /// Create MindSpaceUser from Firebase User
  factory MindSpaceUser.fromFirebaseUser(User firebaseUser) {
    return MindSpaceUser(
      uid: firebaseUser.uid,
      email: firebaseUser.email ?? '',
      displayName: firebaseUser.displayName,
      photoUrl: firebaseUser.photoURL,
      createdAt: firebaseUser.metadata.creationTime ?? DateTime.now(),
      lastLoginAt: firebaseUser.metadata.lastSignInTime ?? DateTime.now(),
    );
  }

  /// Convert to Firestore document
  Map<String, dynamic> toFirestore() {
    return {
      'uid': uid,
      'email': email,
      'displayName': displayName,
      'photoUrl': photoUrl,
      'createdAt': createdAt.toIso8601String(),
      'lastLoginAt': lastLoginAt.toIso8601String(),
      'preferences': preferences ?? {},
    };
  }

  /// Create from Firestore document
  factory MindSpaceUser.fromFirestore(Map<String, dynamic> data) {
    return MindSpaceUser(
      uid: data['uid'] ?? '',
      email: data['email'] ?? '',
      displayName: data['displayName'],
      photoUrl: data['photoUrl'],
      createdAt: DateTime.parse(data['createdAt'] ?? DateTime.now().toIso8601String()),
      lastLoginAt: DateTime.parse(data['lastLoginAt'] ?? DateTime.now().toIso8601String()),
      preferences: data['preferences'],
    );
  }

  /// Copy with updated values
  MindSpaceUser copyWith({
    String? uid,
    String? email,
    String? displayName,
    String? photoUrl,
    DateTime? createdAt,
    DateTime? lastLoginAt,
    Map<String, dynamic>? preferences,
  }) {
    return MindSpaceUser(
      uid: uid ?? this.uid,
      email: email ?? this.email,
      displayName: displayName ?? this.displayName,
      photoUrl: photoUrl ?? this.photoUrl,
      createdAt: createdAt ?? this.createdAt,
      lastLoginAt: lastLoginAt ?? this.lastLoginAt,
      preferences: preferences ?? this.preferences,
    );
  }
}

/// Authentication result model
class AuthResult {
  final bool success;
  final MindSpaceUser? user;
  final String? errorMessage;
  final AuthErrorType? errorType;

  AuthResult({
    required this.success,
    this.user,
    this.errorMessage,
    this.errorType,
  });

  /// Success result
  factory AuthResult.success(MindSpaceUser user) {
    return AuthResult(
      success: true,
      user: user,
    );
  }

  /// Error result
  factory AuthResult.error(String message, [AuthErrorType? type]) {
    return AuthResult(
      success: false,
      errorMessage: message,
      errorType: type,
    );
  }
}

/// Authentication error types
enum AuthErrorType {
  weakPassword,
  emailAlreadyInUse,
  userNotFound,
  wrongPassword,
  invalidEmail,
  userDisabled,
  networkError,
  unknown,
}

/// Authentication state enum
enum AuthState {
  authenticated,
  unauthenticated,
  loading,
}

/// Email validation helper
class EmailValidator {
  static bool isValid(String email) {
    return RegExp(r'^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$').hasMatch(email);
  }

  static String? validate(String? email) {
    if (email == null || email.isEmpty) {
      return 'Email is required';
    }
    if (!isValid(email)) {
      return 'Please enter a valid email address';
    }
    return null;
  }
}

/// Password validation helper
class PasswordValidator {
  static bool isValid(String password) {
    // At least 6 characters with one letter and one number
    return password.length >= 6 && 
           RegExp(r'^(?=.*[A-Za-z])(?=.*\d)').hasMatch(password);
  }

  static String? validate(String? password) {
    if (password == null || password.isEmpty) {
      return 'Password is required';
    }
    if (password.length < 6) {
      return 'Password must be at least 6 characters';
    }
    if (!RegExp(r'^(?=.*[A-Za-z])(?=.*\d)').hasMatch(password)) {
      return 'Password must contain at least one letter and one number';
    }
    return null;
  }

  static String? validateConfirmPassword(String? password, String? confirmPassword) {
    if (confirmPassword == null || confirmPassword.isEmpty) {
      return 'Please confirm your password';
    }
    if (password != confirmPassword) {
      return 'Passwords do not match';
    }
    return null;
  }
}

/// Firebase Auth error parser
class AuthErrorParser {
  static AuthErrorType parseErrorCode(String code) {
    switch (code) {
      case 'weak-password':
        return AuthErrorType.weakPassword;
      case 'email-already-in-use':
        return AuthErrorType.emailAlreadyInUse;
      case 'user-not-found':
        return AuthErrorType.userNotFound;
      case 'wrong-password':
        return AuthErrorType.wrongPassword;
      case 'invalid-email':
        return AuthErrorType.invalidEmail;
      case 'user-disabled':
        return AuthErrorType.userDisabled;
      case 'network-request-failed':
        return AuthErrorType.networkError;
      default:
        return AuthErrorType.unknown;
    }
  }

  static String getErrorMessage(String code) {
    switch (code) {
      case 'weak-password':
        return 'Password is too weak. Please choose a stronger password.';
      case 'email-already-in-use':
        return 'An account already exists with this email address.';
      case 'user-not-found':
        return 'No account found with this email address.';
      case 'wrong-password':
        return 'Incorrect password. Please try again.';
      case 'invalid-email':
        return 'Please enter a valid email address.';
      case 'user-disabled':
        return 'This account has been disabled. Please contact support.';
      case 'network-request-failed':
        return 'Network error. Please check your internet connection.';
      case 'too-many-requests':
        return 'Too many failed attempts. Please try again later.';
      default:
        return 'An unexpected error occurred. Please try again.';
    }
  }
} 