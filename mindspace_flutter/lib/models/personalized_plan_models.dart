import 'package:shared_preferences/shared_preferences.dart';
import 'dart:math';

// Daily Recommendation Model
class DailyRecommendation {
  final String day;
  final String primaryActivity;
  final String activityType; // "meditation", "cbt", "mixed"
  final String activityId;
  final String reason;
  final String motivationalMessage;
  final int estimatedMinutes;

  DailyRecommendation({
    required this.day,
    required this.primaryActivity,
    required this.activityType,
    required this.activityId,
    required this.reason,
    required this.motivationalMessage,
    required this.estimatedMinutes,
  });
}

// Weekly Insight Model
class WeeklyInsight {
  final String insightType; // "mood_pattern", "activity_preference", "streak_motivation"
  final String title;
  final String description;
  final String actionSuggestion;

  WeeklyInsight({
    required this.insightType,
    required this.title,
    required this.description,
    required this.actionSuggestion,
  });
}

// User Analysis Data
class UserAnalysis {
  final String dominantMood;
  final Map<String, int> moodFrequency;
  final Map<String, int> activityPreferences;
  final int currentStreak;
  final int totalXP;
  final int moodEntries;
  final String engagementLevel;

  UserAnalysis({
    required this.dominantMood,
    required this.moodFrequency,
    required this.activityPreferences,
    required this.currentStreak,
    required this.totalXP,
    required this.moodEntries,
    required this.engagementLevel,
  });
}

// Plan Generator - The AI Brain of MindSpace
class PlanGenerator {
  static const String tag = "PlanGenerator";

  /// Generates a personalized weekly plan based on user data
  static Future<List<DailyRecommendation>> generateWeeklyPlan() async {
    print('[$tag] Generating weekly personalized plan');
    
    List<DailyRecommendation> weeklyPlan = [];
    
    // Analyze user patterns
    UserAnalysis userAnalysis = await analyzeUserPatterns();
    
    // Get days of the week
    List<String> daysOfWeek = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"];
    
    for (String day in daysOfWeek) {
      DailyRecommendation recommendation = generateDayRecommendation(day, userAnalysis);
      weeklyPlan.add(recommendation);
    }
    
    print('[$tag] Generated ${weeklyPlan.length} daily recommendations');
    return weeklyPlan;
  }

  /// Analyzes user patterns from stored data
  static Future<UserAnalysis> analyzeUserPatterns() async {
    final prefs = await SharedPreferences.getInstance();
    
    // Analyze mood patterns
    Map<String, int> moodFrequency = await _analyzeMoodPatterns(prefs);
    String dominantMood = _getDominantMood(moodFrequency);
    
    // Analyze activity preferences
    Map<String, int> activityPreferences = _analyzeActivityPreferences(prefs);
    
    // Get streak and engagement data
    int currentStreak = prefs.getInt('mood_streak') ?? 0;
    int totalXP = prefs.getInt('total_xp') ?? 0;
    int moodEntries = prefs.getInt('total_mood_entries') ?? 0;
    String engagementLevel = _calculateEngagementLevel(currentStreak, totalXP, moodEntries);
    
    UserAnalysis analysis = UserAnalysis(
      dominantMood: dominantMood,
      moodFrequency: moodFrequency,
      activityPreferences: activityPreferences,
      currentStreak: currentStreak,
      totalXP: totalXP,
      moodEntries: moodEntries,
      engagementLevel: engagementLevel,
    );
    
    print('[$tag] User analysis - Dominant mood: $dominantMood, Streak: $currentStreak, XP: $totalXP');
    
    return analysis;
  }

  /// Analyzes mood patterns from recent entries
  static Future<Map<String, int>> _analyzeMoodPatterns(SharedPreferences prefs) async {
    Map<String, int> moodCount = {};
    
    // Get recent mood entries (last 14 days)
    DateTime now = DateTime.now();
    for (int i = 0; i < 14; i++) {
      DateTime date = now.subtract(Duration(days: i));
      String dateKey = 'mood_${date.year}-${date.month.toString().padLeft(2, '0')}-${date.day.toString().padLeft(2, '0')}';
      String? mood = prefs.getString(dateKey);
      
      if (mood != null && mood.isNotEmpty) {
        moodCount[mood] = (moodCount[mood] ?? 0) + 1;
      }
    }
    
    return moodCount;
  }

  /// Gets the most frequent mood
  static String _getDominantMood(Map<String, int> moodFrequency) {
    if (moodFrequency.isEmpty) return "neutral";
    
    return moodFrequency.entries
        .reduce((a, b) => a.value > b.value ? a : b)
        .key;
  }

  /// Analyzes user's activity preferences
  static Map<String, int> _analyzeActivityPreferences(SharedPreferences prefs) {
    Map<String, int> preferences = {};
    
    // Count meditation completions
    int meditationCount = prefs.getInt('total_meditation_sessions') ?? 0;
    preferences['meditation'] = meditationCount;
    
    // Count CBT exercise completions
    int cbtCount = prefs.getInt('total_cbt_exercises') ?? 0;
    preferences['cbt'] = cbtCount;
    
    // Add resource reading (if we track it)
    int resourcesViewed = prefs.getInt('resources_viewed_count') ?? 0;
    preferences['resources'] = resourcesViewed;
    
    return preferences;
  }

  /// Calculates user engagement level
  static String _calculateEngagementLevel(int streak, int xp, int moodEntries) {
    int score = (streak * 10) + (xp ~/ 10) + (moodEntries * 5);
    
    if (score >= 100) return "high";
    if (score >= 50) return "medium";
    return "low";
  }

  /// Determines user's preferred activity type
  static String _getPreferredActivityType(Map<String, int> activityPrefs) {
    int meditationCount = activityPrefs['meditation'] ?? 0;
    int cbtCount = activityPrefs['cbt'] ?? 0;
    
    if (meditationCount > cbtCount * 1.5) return "meditation";
    if (cbtCount > meditationCount * 1.5) return "cbt";
    return "mixed";
  }

  /// Generates a personalized recommendation for a specific day
  static DailyRecommendation generateDayRecommendation(String day, UserAnalysis analysis) {
    String preferredActivityType = _getPreferredActivityType(analysis.activityPreferences);
    
    // Generate recommendation based on day, mood, and preferences
    return _generateSmartRecommendation(
      day, 
      analysis.dominantMood, 
      preferredActivityType, 
      analysis.engagementLevel, 
      analysis.currentStreak
    );
  }

  /// Generates smart recommendations based on analysis
  static DailyRecommendation _generateSmartRecommendation(
    String day, 
    String dominantMood, 
    String preferredActivity, 
    String engagementLevel, 
    int streak
  ) {
    
    // Monday - Fresh start recommendations
    if (day == "Monday") {
      if (dominantMood == "Stressed" || dominantMood == "Anxious") {
        return DailyRecommendation(
          day: day,
          primaryActivity: "5-Minute Breathing Exercise",
          activityType: "meditation",
          activityId: "breathing_basics",
          reason: "Start your week calm and focused",
          motivationalMessage: "New week, fresh mindset! 🌟",
          estimatedMinutes: 5,
        );
      } else {
        return DailyRecommendation(
          day: day,
          primaryActivity: "Thought Challenging Exercise",
          activityType: "cbt",
          activityId: "thought_challenging",
          reason: "Set positive intentions for the week",
          motivationalMessage: "You've got this week! 💪",
          estimatedMinutes: 10,
        );
      }
    }
    
    // Tuesday - Build momentum
    if (day == "Tuesday") {
      if (preferredActivity == "meditation") {
        return DailyRecommendation(
          day: day,
          primaryActivity: "Focus & Concentration Session",
          activityType: "meditation",
          activityId: "focus_concentration",
          reason: "Enhance your focus for the week ahead",
          motivationalMessage: "Building momentum! 🚀",
          estimatedMinutes: 10,
        );
      } else {
        return DailyRecommendation(
          day: day,
          primaryActivity: "Mood-Thought Connection",
          activityType: "cbt",
          activityId: "mood_thought_connection",
          reason: "Understand your emotional patterns",
          motivationalMessage: "Self-awareness is power! 🧠",
          estimatedMinutes: 12,
        );
      }
    }
    
    // Wednesday - Mid-week support
    if (day == "Wednesday") {
      if (dominantMood == "Sad" || dominantMood == "Lonely") {
        return DailyRecommendation(
          day: day,
          primaryActivity: "Confidence Building Session",
          activityType: "meditation",
          activityId: "confidence_building",
          reason: "Boost your mid-week confidence",
          motivationalMessage: "You're stronger than you know! ✨",
          estimatedMinutes: 15,
        );
      } else {
        return DailyRecommendation(
          day: day,
          primaryActivity: "Evidence Examination",
          activityType: "cbt",
          activityId: "evidence_examination",
          reason: "Challenge negative thoughts mid-week",
          motivationalMessage: "Facts over fears! 🔍",
          estimatedMinutes: 15,
        );
      }
    }
    
    // Thursday - Energy boost
    if (day == "Thursday") {
      if (engagementLevel == "high" || streak >= 3) {
        return DailyRecommendation(
          day: day,
          primaryActivity: "Advanced Mindfulness",
          activityType: "meditation",
          activityId: "stress_relief_advanced",
          reason: "You're doing great! Try something new",
          motivationalMessage: "Level up your practice! 🎯",
          estimatedMinutes: 20,
        );
      } else {
        return DailyRecommendation(
          day: day,
          primaryActivity: "Quick Energy Boost",
          activityType: "meditation",
          activityId: "quick_energy",
          reason: "Energize yourself for the day",
          motivationalMessage: "Almost to the weekend! ⚡",
          estimatedMinutes: 7,
        );
      }
    }
    
    // Friday - Week completion
    if (day == "Friday") {
      return DailyRecommendation(
        day: day,
        primaryActivity: "Positive Reframing",
        activityType: "cbt",
        activityId: "positive_reframing",
        reason: "End your week on a positive note",
        motivationalMessage: "You made it through the week! 🎉",
        estimatedMinutes: 10,
      );
    }
    
    // Saturday - Self-care
    if (day == "Saturday") {
      if (dominantMood == "Happy" || dominantMood == "Excited") {
        return DailyRecommendation(
          day: day,
          primaryActivity: "Gratitude Meditation",
          activityType: "meditation",
          activityId: "confidence_building",
          reason: "Celebrate your positive energy",
          motivationalMessage: "Weekend vibes! Enjoy this moment 🌞",
          estimatedMinutes: 15,
        );
      } else {
        return DailyRecommendation(
          day: day,
          primaryActivity: "Self-Care Reflection",
          activityType: "meditation",
          activityId: "stress_relief_basics",
          reason: "Take time for yourself today",
          motivationalMessage: "You deserve this self-care time 💚",
          estimatedMinutes: 12,
        );
      }
    }
    
    // Sunday - Week preparation
    if (day == "Sunday") {
      return DailyRecommendation(
        day: day,
        primaryActivity: "Weekly Reflection & Planning",
        activityType: "cbt",
        activityId: "thought_challenging",
        reason: "Reflect on your week and prepare for the next",
        motivationalMessage: "Ready for another great week! 🌟",
        estimatedMinutes: 15,
      );
    }
    
    // Default fallback
    return DailyRecommendation(
      day: day,
      primaryActivity: "Mindful Breathing",
      activityType: "meditation",
      activityId: "breathing_basics",
      reason: "A perfect activity for any day",
      motivationalMessage: "Take a moment for yourself 🌸",
      estimatedMinutes: 5,
    );
  }

  /// Generates weekly insights based on user patterns
  static Future<List<WeeklyInsight>> generateWeeklyInsights() async {
    List<WeeklyInsight> insights = [];
    UserAnalysis analysis = await analyzeUserPatterns();
    
    // Mood pattern insight
    if (analysis.dominantMood != "neutral") {
      insights.add(_generateMoodPatternInsight(analysis.dominantMood, analysis));
    }
    
    // Streak motivation insight
    insights.add(_generateStreakInsight(analysis.currentStreak));
    
    // Activity preference insight
    insights.add(_generateActivityInsight(analysis.activityPreferences));
    
    print('[$tag] Generated ${insights.length} weekly insights');
    return insights;
  }

  /// Generates mood pattern insights
  static WeeklyInsight _generateMoodPatternInsight(String dominantMood, UserAnalysis analysis) {
    switch (dominantMood) {
      case "Stressed":
        return WeeklyInsight(
          insightType: "mood_pattern",
          title: "Stress Pattern Detected",
          description: "You've been feeling stressed lately. This is completely normal!",
          actionSuggestion: "Try our breathing exercises and stress-relief meditations",
        );
      case "Anxious":
        return WeeklyInsight(
          insightType: "mood_pattern",
          title: "Managing Anxiety",
          description: "Anxiety has been present in your recent check-ins.",
          actionSuggestion: "CBT exercises can help challenge anxious thoughts",
        );
      case "Sad":
        return WeeklyInsight(
          insightType: "mood_pattern",
          title: "Supporting Your Mood",
          description: "You've had some tough days recently. You're not alone.",
          actionSuggestion: "Confidence-building activities might help lift your spirits",
        );
      case "Happy":
        return WeeklyInsight(
          insightType: "mood_pattern",
          title: "Positive Energy Detected!",
          description: "You've been feeling great lately - that's wonderful!",
          actionSuggestion: "Keep up the good work with your mental health practices",
        );
      default:
        return WeeklyInsight(
          insightType: "mood_pattern",
          title: "Balanced Emotions",
          description: "Your mood has been relatively stable recently.",
          actionSuggestion: "Continue your current mental health practices",
        );
    }
  }

  /// Generates streak motivation insights
  static WeeklyInsight _generateStreakInsight(int streak) {
    if (streak >= 7) {
      return WeeklyInsight(
        insightType: "streak_motivation",
        title: "Amazing Streak! 🔥",
        description: "You've maintained a $streak-day streak! That's incredible dedication.",
        actionSuggestion: "Keep it up - you're building a powerful habit",
      );
    } else if (streak >= 3) {
      return WeeklyInsight(
        insightType: "streak_motivation",
        title: "Building Momentum 🚀",
        description: "You're on a $streak-day streak! You're doing great.",
        actionSuggestion: "Just a few more days to reach a full week!",
      );
    } else if (streak >= 1) {
      return WeeklyInsight(
        insightType: "streak_motivation",
        title: "Great Start! ⭐",
        description: "You've started your mental health journey. Every day counts!",
        actionSuggestion: "Try to check in daily to build your streak",
      );
    } else {
      return WeeklyInsight(
        insightType: "streak_motivation",
        title: "Ready to Begin? 🌟",
        description: "Starting your mental health journey is the hardest part.",
        actionSuggestion: "Log your mood today to begin building healthy habits",
      );
    }
  }

  /// Generates activity preference insights
  static WeeklyInsight _generateActivityInsight(Map<String, int> activityPrefs) {
    int meditationCount = activityPrefs['meditation'] ?? 0;
    int cbtCount = activityPrefs['cbt'] ?? 0;
    
    if (meditationCount > cbtCount * 2) {
      return WeeklyInsight(
        insightType: "activity_preference",
        title: "Meditation Lover 🧘",
        description: "You really enjoy meditation sessions! That's fantastic.",
        actionSuggestion: "Try mixing in some CBT exercises for a well-rounded approach",
      );
    } else if (cbtCount > meditationCount * 2) {
      return WeeklyInsight(
        insightType: "activity_preference",
        title: "CBT Champion 🧠",
        description: "You're great at completing CBT exercises! Keep it up.",
        actionSuggestion: "Consider adding meditation for relaxation and mindfulness",
      );
    } else if (meditationCount + cbtCount >= 5) {
      return WeeklyInsight(
        insightType: "activity_preference",
        title: "Well-Rounded Practice ⚖️",
        description: "You're doing great with both meditation and CBT exercises!",
        actionSuggestion: "Your balanced approach is excellent for mental wellness",
      );
    } else {
      return WeeklyInsight(
        insightType: "activity_preference",
        title: "Explore More Activities 🔍",
        description: "There are many activities to discover in MindSpace.",
        actionSuggestion: "Try both meditation and CBT exercises to find what works best",
      );
    }
  }
} 