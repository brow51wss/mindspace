import 'package:shared_preferences/shared_preferences.dart';
import 'dart:math';

// Achievement Model
class Achievement {
  final String id;
  final String title;
  final String description;
  final int xpReward;
  final String category; // "mood", "meditation", "cbt", "special"
  bool isUnlocked;
  DateTime? unlockedDate;

  Achievement({
    required this.id,
    required this.title,
    required this.description,
    required this.xpReward,
    required this.category,
    this.isUnlocked = false,
    this.unlockedDate,
  });
}

// Streak Result Model
class StreakResult {
  final StreakType streakType;
  final int previousStreak;
  final int newStreak;
  final bool isNewActivity;
  final bool isStreakIncreased;
  final bool isStreakBroken;
  final bool isNewBest;
  final int milestoneReached;
  final int milestoneXP;

  StreakResult({
    required this.streakType,
    this.previousStreak = 0,
    this.newStreak = 0,
    this.isNewActivity = false,
    this.isStreakIncreased = false,
    this.isStreakBroken = false,
    this.isNewBest = false,
    this.milestoneReached = 0,
    this.milestoneXP = 0,
  });
}

// Streak Type Enum
enum StreakType {
  mood('MindSpaceMoods', 'mood_streak'),
  meditation('MindSpaceMeditation', 'meditation_streak'),
  cbt('MindSpaceCBT', 'cbt_streak'),
  overall('MindSpaceStreaks', 'overall_streak');

  const StreakType(this.prefsName, this.streakKey);
  final String prefsName;
  final String streakKey;
}

// Progress Statistics Model
class ProgressStats {
  final int totalActiveDays;
  final int currentStreak;
  final int totalXP;
  final int currentLevel;
  final int moodEntries;
  final int meditationMinutes;
  final int cbtExercises;
  final List<String> weeklyMoods;
  final String weeklyInsight;
  final String motivationalMessage;

  ProgressStats({
    this.totalActiveDays = 0,
    this.currentStreak = 0,
    this.totalXP = 0,
    this.currentLevel = 1,
    this.moodEntries = 0,
    this.meditationMinutes = 0,
    this.cbtExercises = 0,
    this.weeklyMoods = const [],
    this.weeklyInsight = '',
    this.motivationalMessage = '',
  });
}

// Gamification Data Manager
class GamificationData {
  // Streak milestones with XP rewards
  static const List<int> streakMilestones = [3, 7, 14, 21, 30, 50, 100];
  static const List<int> milestoneXP = [25, 50, 100, 150, 250, 500, 1000];

  // Get all achievements
  static List<Achievement> getAchievements() {
    return [
      // Mood Tracking Achievements
      Achievement(
        id: 'first_mood',
        title: '🌟 First Steps',
        description: 'Log your first mood',
        xpReward: 10,
        category: 'mood',
      ),
      Achievement(
        id: 'mood_3_days',
        title: '📅 Consistent Tracker',
        description: 'Log mood for 3 consecutive days',
        xpReward: 25,
        category: 'mood',
      ),
      Achievement(
        id: 'mood_week',
        title: '🗓️ Weekly Warrior',
        description: 'Log mood for 7 consecutive days',
        xpReward: 50,
        category: 'mood',
      ),
      Achievement(
        id: 'mood_month',
        title: '📊 Monthly Master',
        description: 'Log mood for 30 days',
        xpReward: 100,
        category: 'mood',
      ),

      // Meditation Achievements
      Achievement(
        id: 'first_meditation',
        title: '🧘 Mindful Beginner',
        description: 'Complete your first meditation',
        xpReward: 15,
        category: 'meditation',
      ),
      Achievement(
        id: 'meditation_5',
        title: '🕯️ Peaceful Mind',
        description: 'Complete 5 meditation sessions',
        xpReward: 35,
        category: 'meditation',
      ),
      Achievement(
        id: 'meditation_20',
        title: '☯️ Zen Master',
        description: 'Complete 20 meditation sessions',
        xpReward: 75,
        category: 'meditation',
      ),
      Achievement(
        id: 'meditation_streak_7',
        title: '🔥 Meditation Streak',
        description: 'Meditate for 7 days in a row',
        xpReward: 60,
        category: 'meditation',
      ),

      // CBT Exercise Achievements
      Achievement(
        id: 'first_cbt',
        title: '🧠 Thought Explorer',
        description: 'Complete your first CBT exercise',
        xpReward: 20,
        category: 'cbt',
      ),
      Achievement(
        id: 'cbt_all_types',
        title: '🔄 CBT Champion',
        description: 'Try all 4 types of CBT exercises',
        xpReward: 40,
        category: 'cbt',
      ),
      Achievement(
        id: 'cbt_10',
        title: '💭 Mind Shaper',
        description: 'Complete 10 CBT exercises',
        xpReward: 80,
        category: 'cbt',
      ),

      // Special Achievements
      Achievement(
        id: 'balanced_week',
        title: '⚖️ Balanced Life',
        description: 'Use all features in one week',
        xpReward: 100,
        category: 'special',
      ),
      Achievement(
        id: 'stress_warrior',
        title: '💪 Stress Warrior',
        description: 'Improve mood rating by 3+ points',
        xpReward: 50,
        category: 'special',
      ),
      Achievement(
        id: 'early_bird',
        title: '🌅 Early Bird',
        description: 'Log mood before 9 AM',
        xpReward: 30,
        category: 'special',
      ),
    ];
  }

  // Calculate level from XP
  static int calculateLevel(int totalXP) {
    // Level formula: Level = sqrt(XP / 100) + 1
    return sqrt(totalXP / 100).floor() + 1;
  }

  // Calculate XP needed for next level
  static int getXPForNextLevel(int currentLevel) {
    return ((currentLevel * currentLevel) - (2 * currentLevel) + 1) * 100;
  }

  // Get streak emoji
  static String getStreakEmoji(int streak) {
    if (streak >= 30) return '🌟';
    if (streak >= 14) return '🔥';
    if (streak >= 7) return '⚡';
    if (streak >= 3) return '💪';
    return '🌱';
  }

  // Check if milestone reached
  static int checkMilestone(int streak) {
    for (int i = streakMilestones.length - 1; i >= 0; i--) {
      if (streak == streakMilestones[i]) {
        return streakMilestones[i];
      }
    }
    return 0;
  }

  // Get XP for milestone
  static int getMilestoneXP(int milestone) {
    int index = streakMilestones.indexOf(milestone);
    return index >= 0 ? milestoneXP[index] : 0;
  }

  // Generate motivational messages
  static List<String> getMotivationalMessages() {
    return [
      "You're making great progress! 🌟",
      "Every small step counts! 💪",
      "Your mental health journey is inspiring! 🌈",
      "Consistency is the key to growth! 🗝️",
      "You're building healthy habits! 🌱",
      "Keep up the amazing work! 🎯",
      "Your dedication is paying off! 💎",
      "You're stronger than you think! 🦋",
      "Progress, not perfection! ✨",
      "You've got this! 🚀",
    ];
  }

  // Generate weekly insights
  static List<String> getWeeklyInsights() {
    return [
      "You're showing great consistency in tracking your mood!",
      "Your meditation practice is helping build mindfulness!",
      "CBT exercises are helping you develop better thought patterns!",
      "You're building a balanced mental health routine!",
      "Your progress shows real commitment to self-care!",
      "You're developing healthy coping strategies!",
      "Your streak shows dedication to mental wellness!",
    ];
  }

  // Sample mood data for chart (in real app, this would come from actual data)
  static List<String> getSampleMoodData() {
    return ['😊', '😌', '😰', '😊', '😢', '😊', '😌'];
  }

  // Day labels for mood chart
  static List<String> getMoodChartLabels() {
    return ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'];
  }
}

// Achievement Manager Class
class AchievementManager {
  static Future<void> checkAndUpdateAchievements() async {
    final prefs = await SharedPreferences.getInstance();
    final achievements = GamificationData.getAchievements();
    
    // Check mood achievements
    await _checkMoodAchievements(prefs, achievements);
    
    // Check meditation achievements  
    await _checkMeditationAchievements(prefs, achievements);
    
    // Check CBT achievements
    await _checkCBTAchievements(prefs, achievements);
    
    // Check special achievements
    await _checkSpecialAchievements(prefs, achievements);
  }

  static Future<void> _checkMoodAchievements(SharedPreferences prefs, List<Achievement> achievements) async {
    int totalMoodEntries = prefs.getInt('total_mood_entries') ?? 0;
    int currentStreak = prefs.getInt('mood_streak') ?? 0;
    
    if (totalMoodEntries > 0) {
      await _unlockAchievement(prefs, 'first_mood');
    }
    if (currentStreak >= 3) {
      await _unlockAchievement(prefs, 'mood_3_days');
    }
    if (currentStreak >= 7) {
      await _unlockAchievement(prefs, 'mood_week');
    }
    if (totalMoodEntries >= 30) {
      await _unlockAchievement(prefs, 'mood_month');
    }
  }

  static Future<void> _checkMeditationAchievements(SharedPreferences prefs, List<Achievement> achievements) async {
    int totalSessions = prefs.getInt('total_meditation_sessions') ?? 0;
    int meditationStreak = prefs.getInt('meditation_streak') ?? 0;
    
    if (totalSessions > 0) {
      await _unlockAchievement(prefs, 'first_meditation');
    }
    if (totalSessions >= 5) {
      await _unlockAchievement(prefs, 'meditation_5');
    }
    if (totalSessions >= 20) {
      await _unlockAchievement(prefs, 'meditation_20');
    }
    if (meditationStreak >= 7) {
      await _unlockAchievement(prefs, 'meditation_streak_7');
    }
  }

  static Future<void> _checkCBTAchievements(SharedPreferences prefs, List<Achievement> achievements) async {
    int totalExercises = prefs.getInt('total_cbt_exercises') ?? 0;
    List<String> completedTypes = prefs.getStringList('completed_cbt_types') ?? [];
    
    if (totalExercises > 0) {
      await _unlockAchievement(prefs, 'first_cbt');
    }
    if (completedTypes.length >= 4) {
      await _unlockAchievement(prefs, 'cbt_all_types');
    }
    if (totalExercises >= 10) {
      await _unlockAchievement(prefs, 'cbt_10');
    }
  }

  static Future<void> _checkSpecialAchievements(SharedPreferences prefs, List<Achievement> achievements) async {
    // Check if used all features in past week
    final weekAgo = DateTime.now().subtract(Duration(days: 7)).millisecondsSinceEpoch;
    final lastMoodTime = prefs.getInt('last_mood_time') ?? 0;
    final lastMeditationTime = prefs.getInt('last_meditation_time') ?? 0;
    final lastCBTTime = prefs.getInt('last_cbt_time') ?? 0;
    
    if (lastMoodTime > weekAgo && lastMeditationTime > weekAgo && lastCBTTime > weekAgo) {
      await _unlockAchievement(prefs, 'balanced_week');
    }
    
    // Early bird check would need time-of-day tracking (simplified for now)
    final lastMoodHour = prefs.getInt('last_mood_hour') ?? 12;
    if (lastMoodHour < 9) {
      await _unlockAchievement(prefs, 'early_bird');
    }
  }

  static Future<void> _unlockAchievement(SharedPreferences prefs, String achievementId) async {
    final isUnlocked = prefs.getBool('achievement_$achievementId') ?? false;
    if (!isUnlocked) {
      await prefs.setBool('achievement_$achievementId', true);
      await prefs.setInt('achievement_${achievementId}_date', DateTime.now().millisecondsSinceEpoch);
      
      // Award XP
      final achievement = GamificationData.getAchievements().firstWhere((a) => a.id == achievementId);
      final currentXP = prefs.getInt('total_xp') ?? 0;
      await prefs.setInt('total_xp', currentXP + achievement.xpReward);
    }
  }

  static Future<List<Achievement>> getUnlockedAchievements() async {
    final prefs = await SharedPreferences.getInstance();
    final achievements = GamificationData.getAchievements();
    
    for (var achievement in achievements) {
      achievement.isUnlocked = prefs.getBool('achievement_${achievement.id}') ?? false;
      if (achievement.isUnlocked) {
        final unlockedTimestamp = prefs.getInt('achievement_${achievement.id}_date') ?? 0;
        achievement.unlockedDate = DateTime.fromMillisecondsSinceEpoch(unlockedTimestamp);
      }
    }
    
    return achievements;
  }
} 