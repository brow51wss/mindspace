import 'dart:async';
import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:intl/intl.dart';
import 'package:firebase_core/firebase_core.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_analytics/firebase_analytics.dart';
import 'package:package_info_plus/package_info_plus.dart';
import 'models/auth_models.dart';
import 'services/auth_service.dart';
import 'screens/auth/login_screen.dart';
import 'screens/resource_hub_screen.dart';
import 'screens/meditation_library_screen.dart';
import 'screens/cbt_exercises_screen.dart';
import 'screens/progress_dashboard_screen.dart';
import 'screens/personalized_plan_screen.dart';
import 'screens/achievement_system_screen.dart';


void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await Firebase.initializeApp();
  
  // Initialize authentication service
  await AuthService().initialize();
  
  runApp(const MindSpaceApp());
}

class MindSpaceApp extends StatelessWidget {
  const MindSpaceApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'MindSpace',
      theme: ThemeData(
        primarySwatch: Colors.blue,
        useMaterial3: true,
      ),
      home: const AuthWrapper(),
      debugShowCheckedModeBanner: false,
    );
  }
}

class AuthWrapper extends StatefulWidget {
  const AuthWrapper({super.key});

  @override
  State<AuthWrapper> createState() => _AuthWrapperState();
}

class _AuthWrapperState extends State<AuthWrapper> {
  final AuthService _authService = AuthService();
  AuthState _currentState = AuthState.unauthenticated;

  @override
  void initState() {
    super.initState();
    
    // Listen to authentication state changes
    _authService.authStateStream.listen((state) {
      if (mounted) {
        setState(() {
          _currentState = state;
        });
      }
    });

    // Initialize the auth service
    _authService.initialize();
  }

  @override
  Widget build(BuildContext context) {
    switch (_currentState) {
      case AuthState.loading:
        return const LoadingScreen();
      case AuthState.authenticated:
        return const DailyCheckInScreen();
      case AuthState.unauthenticated:
      default:
        return const LoginScreen();
    }
  }
}

class LoadingScreen extends StatelessWidget {
  const LoadingScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xFF1E1E2E),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            // MindSpace Logo
            Container(
              width: 100,
              height: 100,
              decoration: BoxDecoration(
                gradient: const LinearGradient(
                  colors: [Color(0xFF6C5CE7), Color(0xFFA855F7)],
                  begin: Alignment.topLeft,
                  end: Alignment.bottomRight,
                ),
                borderRadius: BorderRadius.circular(25),
                boxShadow: [
                  BoxShadow(
                    color: const Color(0xFF6C5CE7).withOpacity(0.4),
                    blurRadius: 25,
                    offset: const Offset(0, 10),
                  ),
                ],
              ),
              child: const Icon(
                Icons.psychology_outlined,
                color: Colors.white,
                size: 50,
              ),
            ),
            
            const SizedBox(height: 30),
            
            const Text(
              'MindSpace',
              style: TextStyle(
                color: Colors.white,
                fontSize: 36,
                fontWeight: FontWeight.bold,
                letterSpacing: 1.5,
              ),
            ),
            
            const SizedBox(height: 40),
            
            const CircularProgressIndicator(
              valueColor: AlwaysStoppedAnimation<Color>(Color(0xFF6C5CE7)),
              strokeWidth: 3,
            ),
            
            const SizedBox(height: 20),
            
            Text(
              'Loading your wellness journey...',
              style: TextStyle(
                color: Colors.white.withOpacity(0.7),
                fontSize: 16,
              ),
            ),
          ],
        ),
      ),
    );
  }
}

class DailyCheckInScreen extends StatefulWidget {
  const DailyCheckInScreen({super.key});

  @override
  State<DailyCheckInScreen> createState() => _DailyCheckInScreenState();
}

class _DailyCheckInScreenState extends State<DailyCheckInScreen> {
  String? selectedMood;
  int currentStreak = 0;
  bool hasCheckedInToday = false;
  String versionInfo = 'Loading...';
  
  final List<Map<String, dynamic>> moods = [
    {'name': 'Happy', 'emoji': '😊', 'color': Colors.orange},
    {'name': 'Calm', 'emoji': '😌', 'color': Colors.green},
    {'name': 'Stressed', 'emoji': '😰', 'color': Colors.red},
    {'name': 'Anxious', 'emoji': '😟', 'color': Colors.purple},
    {'name': 'Sad', 'emoji': '😢', 'color': Colors.blue},
    {'name': 'Excited', 'emoji': '🤩', 'color': Colors.yellow},
    {'name': 'Tired', 'emoji': '😴', 'color': Colors.grey},
    {'name': 'Angry', 'emoji': '😠', 'color': Colors.redAccent},
  ];

  @override
  void initState() {
    super.initState();
    _loadStreakData();
    _checkTodayStatus();
    _loadVersionInfo();
  }

  Future<void> _loadVersionInfo() async {
    try {
      PackageInfo packageInfo = await PackageInfo.fromPlatform();
      setState(() {
        versionInfo = 'v${packageInfo.version} (Build ${packageInfo.buildNumber})';
      });
    } catch (e) {
      setState(() {
        versionInfo = 'v1.3.0 (Build 1)'; // Fallback version
      });
    }
  }

  Future<void> _loadStreakData() async {
    final prefs = await SharedPreferences.getInstance();
    setState(() {
      currentStreak = prefs.getInt('current_streak') ?? 0;
    });
  }

  Future<void> _checkTodayStatus() async {
    final today = DateFormat('yyyy-MM-dd').format(DateTime.now());
    final prefs = await SharedPreferences.getInstance();
    final lastCheckIn = prefs.getString('last_check_in');
    
    setState(() {
      hasCheckedInToday = lastCheckIn == today;
    });
  }

  Future<void> _logMood(String mood) async {
    final today = DateFormat('yyyy-MM-dd').format(DateTime.now());
    final prefs = await SharedPreferences.getInstance();
    
    try {
      // Save to local storage (Firebase will be added back later)
      await prefs.setString('last_check_in', today);
      await prefs.setString('last_mood', mood);
      
      // Update streak
      if (!hasCheckedInToday) {
        final newStreak = currentStreak + 1;
        await prefs.setInt('current_streak', newStreak);
        setState(() {
          currentStreak = newStreak;
        });
      }
      
      setState(() {
        selectedMood = mood;
        hasCheckedInToday = true;
      });
      
      _showMoodSuggestion(mood);
    } catch (e) {
      print('Error logging mood: $e');
    }
  }

  void _showMoodSuggestion(String mood) {
    String suggestion = _getMoodSuggestion(mood);
    
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: Text('Thanks for checking in!'),
          content: Text(suggestion),
          actions: [
            TextButton(
              onPressed: () => Navigator.of(context).pop(),
              child: const Text('OK'),
            ),
          ],
        );
      },
    );
  }

  String _getMoodSuggestion(String mood) {
    switch (mood.toLowerCase()) {
      case 'happy':
        return 'Great to see you feeling happy! Keep up the positive energy.';
      case 'calm':
        return 'Wonderful that you feel calm today. Consider some meditation to maintain this peace.';
      case 'stressed':
        return 'Take some deep breaths. Try our breathing exercises to help reduce stress.';
      case 'anxious':
        return 'Remember that anxiety is temporary. Consider trying our guided meditation.';
      case 'sad':
        return 'It\'s okay to feel sad sometimes. Try journaling or talking to someone you trust.';
      case 'excited':
        return 'Your excitement is contagious! Channel this energy into something positive.';
      case 'tired':
        return 'Rest is important. Make sure you\'re getting enough sleep and staying hydrated.';
      case 'angry':
        return 'Take a moment to cool down. Deep breathing can help manage anger.';
      default:
        return 'Thanks for sharing how you feel today!';
    }
  }

  Future<void> _handleLogout() async {
    final authService = AuthService();
    await authService.signOut();
    
    if (mounted) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          content: Text('Logged out successfully'),
          backgroundColor: Color(0xFF6C5CE7),
          behavior: SnackBarBehavior.floating,
        ),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    final today = DateFormat('EEEE, MMMM d').format(DateTime.now());
    final authService = AuthService();
    final user = authService.currentUser;
    
    return Scaffold(
      backgroundColor: const Color(0xFFF5F5F5),
      appBar: AppBar(
        backgroundColor: Colors.transparent,
        elevation: 0,
        title: Text(
          'Welcome, ${user?.email?.split('@')[0] ?? 'User'}',
          style: const TextStyle(
            color: Color(0xFF1E1E2E),
            fontWeight: FontWeight.w600,
          ),
        ),
        actions: [
          IconButton(
            icon: const Icon(
              Icons.logout,
              color: Color(0xFF6C5CE7),
            ),
            onPressed: () {
              showDialog(
                context: context,
                builder: (BuildContext context) {
                  return AlertDialog(
                    title: const Text('Logout'),
                    content: const Text('Are you sure you want to logout?'),
                    actions: [
                      TextButton(
                        onPressed: () => Navigator.of(context).pop(),
                        child: const Text('Cancel'),
                      ),
                      TextButton(
                        onPressed: () {
                          Navigator.of(context).pop();
                          _handleLogout();
                        },
                        child: const Text(
                          'Logout',
                          style: TextStyle(color: Colors.red),
                        ),
                      ),
                    ],
                  );
                },
              );
            },
          ),
        ],
      ),
      body: SafeArea(
        child: Stack(
          children: [
            // Main content
            SingleChildScrollView(
              padding: const EdgeInsets.all(16.0),
              child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              // Header Section
              Container(
                width: double.infinity,
                padding: const EdgeInsets.all(20),
                decoration: BoxDecoration(
                  color: Colors.white,
                  borderRadius: BorderRadius.circular(12),
                  boxShadow: [
                    BoxShadow(
                      color: Colors.grey.withOpacity(0.1),
                      blurRadius: 8,
                      offset: const Offset(0, 2),
                    ),
                  ],
                ),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      'Daily Check-In',
                      style: TextStyle(
                        fontSize: 28,
                        fontWeight: FontWeight.bold,
                        color: Colors.blue[800],
                      ),
                    ),
                    const SizedBox(height: 8),
                    Text(
                      today,
                      style: const TextStyle(
                        fontSize: 16,
                        color: Colors.grey,
                      ),
                    ),
                    const SizedBox(height: 16),
                    Text(
                      'How are you feeling today?',
                      style: TextStyle(
                        fontSize: 18,
                        fontWeight: FontWeight.w500,
                        color: Colors.grey[800],
                      ),
                    ),
                  ],
                ),
              ),
              
              const SizedBox(height: 24),
              
              // Mood Buttons Grid
              GridView.builder(
                shrinkWrap: true,
                physics: const NeverScrollableScrollPhysics(),
                gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
                  crossAxisCount: 2,
                  childAspectRatio: 1.5,
                  crossAxisSpacing: 12,
                  mainAxisSpacing: 12,
                ),
                itemCount: moods.length,
                itemBuilder: (context, index) {
                  final mood = moods[index];
                  final isSelected = selectedMood == mood['name'];
                  
                  return GestureDetector(
                    onTap: hasCheckedInToday 
                        ? null 
                        : () => _logMood(mood['name']),
                    child: Container(
                      decoration: BoxDecoration(
                        color: isSelected 
                            ? mood['color'].withOpacity(0.8)
                            : Colors.white,
                        borderRadius: BorderRadius.circular(12),
                        border: Border.all(
                          color: isSelected 
                              ? mood['color']
                              : Colors.grey.withOpacity(0.3),
                          width: 2,
                        ),
                        boxShadow: [
                          BoxShadow(
                            color: Colors.grey.withOpacity(0.1),
                            blurRadius: 4,
                            offset: const Offset(0, 2),
                          ),
                        ],
                      ),
                      child: Column(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          Text(
                            mood['emoji'],
                            style: const TextStyle(fontSize: 32),
                          ),
                          const SizedBox(height: 8),
                          Text(
                            mood['name'],
                            style: TextStyle(
                              fontSize: 16,
                              fontWeight: FontWeight.w600,
                              color: isSelected ? Colors.white : Colors.black87,
                            ),
                          ),
                        ],
                      ),
                    ),
                  );
                },
              ),
              
              const SizedBox(height: 24),
              
              // Navigation Buttons
              Container(
                width: double.infinity,
                padding: const EdgeInsets.all(20),
                decoration: BoxDecoration(
                  color: Colors.white,
                  borderRadius: BorderRadius.circular(12),
                  boxShadow: [
                    BoxShadow(
                      color: Colors.grey.withOpacity(0.1),
                      blurRadius: 8,
                      offset: const Offset(0, 2),
                    ),
                  ],
                ),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      'Explore MindSpace',
                      style: TextStyle(
                        fontSize: 20,
                        fontWeight: FontWeight.bold,
                        color: Colors.blue[800],
                      ),
                    ),
                    const SizedBox(height: 16),
                    
                    // Navigation grid
                    GridView.count(
                      shrinkWrap: true,
                      physics: const NeverScrollableScrollPhysics(),
                      crossAxisCount: 2,
                      childAspectRatio: 2.5,
                      crossAxisSpacing: 8,
                      mainAxisSpacing: 8,
                      children: [
                        _buildNavButton('🧘', 'Meditation', Colors.green),
                        _buildNavButton('📚', 'Resources', Colors.blue),
                        _buildNavButton('🎯', 'CBT Tools', Colors.purple),
                        _buildNavButton('📊', 'Progress', Colors.orange),
                        _buildNavButton('🏆', 'Achievements', Colors.amber),
                        _buildNavButton('💡', 'Plans', Colors.teal),
                        _buildNavButton('👥', 'Community', Colors.pink),
                        _buildNavButton('📱', 'Settings', Colors.grey),
                      ],
                    ),
                  ],
                ),
              ),
              
              const SizedBox(height: 24),
              
              // Streak Counter
              Container(
                width: double.infinity,
                padding: const EdgeInsets.all(20),
                decoration: BoxDecoration(
                  color: Colors.blue[50],
                  borderRadius: BorderRadius.circular(12),
                  border: Border.all(color: Colors.blue[200]!),
                ),
                child: Column(
                  children: [
                    Text(
                      '🔥 Current Streak',
                      style: TextStyle(
                        fontSize: 18,
                        fontWeight: FontWeight.w600,
                        color: Colors.blue[800],
                      ),
                    ),
                    const SizedBox(height: 8),
                    Text(
                      '$currentStreak days',
                      style: TextStyle(
                        fontSize: 32,
                        fontWeight: FontWeight.bold,
                        color: Colors.blue[800],
                      ),
                    ),
                    const SizedBox(height: 8),
                    Text(
                      hasCheckedInToday 
                          ? 'Great job checking in today!'
                          : 'Check in today to continue your streak!',
                      style: TextStyle(
                        fontSize: 14,
                        color: Colors.blue[600],
                      ),
                      textAlign: TextAlign.center,
                    ),
                  ],
                ),
              ),
            ],
          ),
        ),
            
            // Version display in upper right corner
            Positioned(
              top: 8,
              right: 8,
              child: Container(
                padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
                decoration: BoxDecoration(
                  color: Colors.black54,
                  borderRadius: BorderRadius.circular(12),
                ),
                child: Text(
                  versionInfo,
                  style: const TextStyle(
                    color: Colors.white,
                    fontSize: 10,
                    fontWeight: FontWeight.bold,
                  ),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildNavButton(String emoji, String label, Color color) {
    return Container(
      decoration: BoxDecoration(
        color: color.withOpacity(0.1),
        borderRadius: BorderRadius.circular(8),
        border: Border.all(color: color.withOpacity(0.3)),
      ),
      child: Material(
        color: Colors.transparent,
        child: InkWell(
          borderRadius: BorderRadius.circular(8),
          onTap: () {
            if (label == 'Resources') {
              Navigator.push(
                context,
                MaterialPageRoute(
                  builder: (context) => const ResourceHubScreen(),
                ),
              );
            } else if (label == 'Meditation') {
              Navigator.push(
                context,
                MaterialPageRoute(
                  builder: (context) => MeditationLibraryScreen(),
                ),
              );
            } else if (label == 'CBT Tools') {
              Navigator.push(
                context,
                MaterialPageRoute(
                  builder: (context) => CBTExercisesScreen(),
                ),
              );
            } else if (label == 'Progress') {
              Navigator.push(
                context,
                MaterialPageRoute(
                  builder: (context) => ProgressDashboardScreen(),
                ),
              );
            } else if (label == 'Plans') {
              Navigator.push(
                context,
                MaterialPageRoute(
                  builder: (context) => PersonalizedPlanScreen(),
                ),
              );
            } else if (label == 'Achievements') {
              Navigator.push(
                context,
                MaterialPageRoute(
                  builder: (context) => AchievementSystemScreen(),
                ),
              );
            } else if (label == 'Settings') {
              ScaffoldMessenger.of(context).showSnackBar(
                SnackBar(content: Text('Settings coming soon!')),
              );
            } else {
              ScaffoldMessenger.of(context).showSnackBar(
                SnackBar(content: Text('$label coming soon!')),
              );
            }
          },          child: Center(
            child: Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Text(emoji, style: const TextStyle(fontSize: 16)),
                const SizedBox(width: 8),
                Text(
                  label,
                  style: TextStyle(
                    fontSize: 12,
                    fontWeight: FontWeight.w600,
                    color: color.withOpacity(0.8),
                  ),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
