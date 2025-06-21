package com.mindspace.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * PlanGenerator - The AI brain of MindSpace
 * Analyzes user patterns and generates personalized mental health plans
 */
public class PlanGenerator {
    private static final String TAG = "PlanGenerator";
    private static final String PREFS_NAME = "MindSpacePrefs";
    
    private Context context;
    private SharedPreferences prefs;
    
    // Plan recommendation types
    public static class DailyRecommendation {
        public String day;
        public String primaryActivity;
        public String activityType; // "meditation", "cbt", "mixed"
        public String activityId;
        public String reason;
        public String motivationalMessage;
        public int estimatedMinutes;
        
        public DailyRecommendation(String day, String primaryActivity, String activityType, 
                                 String activityId, String reason, String motivationalMessage, int estimatedMinutes) {
            this.day = day;
            this.primaryActivity = primaryActivity;
            this.activityType = activityType;
            this.activityId = activityId;
            this.reason = reason;
            this.motivationalMessage = motivationalMessage;
            this.estimatedMinutes = estimatedMinutes;
        }
    }
    
    public static class WeeklyInsight {
        public String insightType; // "mood_pattern", "activity_preference", "streak_motivation"
        public String title;
        public String description;
        public String actionSuggestion;
        
        public WeeklyInsight(String insightType, String title, String description, String actionSuggestion) {
            this.insightType = insightType;
            this.title = title;
            this.description = description;
            this.actionSuggestion = actionSuggestion;
        }
    }
    
    public PlanGenerator(Context context) {
        this.context = context;
        this.prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Log.d(TAG, "PlanGenerator initialized");
    }
    
    /**
     * Generates a personalized weekly plan based on user data
     */
    public List<DailyRecommendation> generateWeeklyPlan() {
        Log.d(TAG, "Generating weekly personalized plan");
        
        List<DailyRecommendation> weeklyPlan = new ArrayList<>();
        
        // Analyze user patterns
        Map<String, Object> userAnalysis = analyzeUserPatterns();
        
        // Get days of the week
        String[] daysOfWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        
        for (String day : daysOfWeek) {
            DailyRecommendation recommendation = generateDayRecommendation(day, userAnalysis);
            weeklyPlan.add(recommendation);
        }
        
        Log.d(TAG, "Generated " + weeklyPlan.size() + " daily recommendations");
        return weeklyPlan;
    }
    
    /**
     * Analyzes user patterns from stored data
     */
    private Map<String, Object> analyzeUserPatterns() {
        Map<String, Object> analysis = new HashMap<>();
        
        // Analyze mood patterns
        Map<String, Integer> moodFrequency = analyzeMoodPatterns();
        String dominantMood = getDominantMood(moodFrequency);
        
        // Analyze activity preferences
        Map<String, Integer> activityPreferences = analyzeActivityPreferences();
        
        // Get streak and engagement data
        StreakTracker streakTracker = new StreakTracker(context);
        int currentStreak = streakTracker.getHighestCurrentStreak();
        int totalXP = prefs.getInt("total_xp", 0);
        int moodEntries = prefs.getInt("mood_entry_count", 0);
        
        // Store analysis results
        analysis.put("dominantMood", dominantMood);
        analysis.put("moodFrequency", moodFrequency);
        analysis.put("activityPreferences", activityPreferences);
        analysis.put("currentStreak", currentStreak);
        analysis.put("totalXP", totalXP);
        analysis.put("moodEntries", moodEntries);
        analysis.put("engagementLevel", calculateEngagementLevel(currentStreak, totalXP, moodEntries));
        
        Log.d(TAG, "User analysis - Dominant mood: " + dominantMood + ", Streak: " + currentStreak + ", XP: " + totalXP);
        
        return analysis;
    }
    
    /**
     * Analyzes mood patterns from recent entries
     */
    private Map<String, Integer> analyzeMoodPatterns() {
        Map<String, Integer> moodCount = new HashMap<>();
        
        // Get recent mood entries (last 14 days)
        for (int i = 0; i < 14; i++) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_YEAR, -i);
            String dateKey = "mood_" + new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.getTime());
            String mood = prefs.getString(dateKey, "");
            
            if (!mood.isEmpty()) {
                moodCount.put(mood, moodCount.getOrDefault(mood, 0) + 1);
            }
        }
        
        return moodCount;
    }
    
    /**
     * Gets the most frequent mood
     */
    private String getDominantMood(Map<String, Integer> moodFrequency) {
        if (moodFrequency.isEmpty()) return "neutral";
        
        return moodFrequency.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("neutral");
    }
    
    /**
     * Analyzes user's activity preferences
     */
    private Map<String, Integer> analyzeActivityPreferences() {
        Map<String, Integer> preferences = new HashMap<>();
        
        // Count meditation completions
        int meditationCount = prefs.getInt("meditation_completed_count", 0);
        preferences.put("meditation", meditationCount);
        
        // Count CBT exercise completions
        int cbtCount = prefs.getInt("cbt_completed_count", 0);
        preferences.put("cbt", cbtCount);
        
        // Add resource reading (if we track it)
        int resourcesViewed = prefs.getInt("resources_viewed_count", 0);
        preferences.put("resources", resourcesViewed);
        
        return preferences;
    }
    
    /**
     * Calculates user engagement level
     */
    private String calculateEngagementLevel(int streak, int xp, int moodEntries) {
        int score = streak * 10 + (xp / 10) + moodEntries * 5;
        
        if (score >= 100) return "high";
        if (score >= 50) return "medium";
        return "low";
    }
    
    /**
     * Generates a personalized recommendation for a specific day
     */
    private DailyRecommendation generateDayRecommendation(String day, Map<String, Object> analysis) {
        String dominantMood = (String) analysis.get("dominantMood");
        @SuppressWarnings("unchecked")
        Map<String, Integer> activityPrefs = (Map<String, Integer>) analysis.get("activityPreferences");
        String engagementLevel = (String) analysis.get("engagementLevel");
        int currentStreak = (Integer) analysis.get("currentStreak");
        
        // Determine preferred activity type
        String preferredActivityType = getPreferredActivityType(activityPrefs);
        
        // Generate recommendation based on day, mood, and preferences
        return generateSmartRecommendation(day, dominantMood, preferredActivityType, engagementLevel, currentStreak);
    }
    
    /**
     * Determines user's preferred activity type
     */
    private String getPreferredActivityType(Map<String, Integer> activityPrefs) {
        int meditationCount = activityPrefs.getOrDefault("meditation", 0);
        int cbtCount = activityPrefs.getOrDefault("cbt", 0);
        
        if (meditationCount > cbtCount * 1.5) return "meditation";
        if (cbtCount > meditationCount * 1.5) return "cbt";
        return "mixed";
    }
    
    /**
     * Generates smart recommendations based on analysis
     */
    private DailyRecommendation generateSmartRecommendation(String day, String dominantMood, 
                                                          String preferredActivity, String engagementLevel, int streak) {
        
        // Monday - Fresh start recommendations
        if (day.equals("Monday")) {
            if (dominantMood.equals("Stressed") || dominantMood.equals("Anxious")) {
                return new DailyRecommendation(day, "5-Minute Breathing Exercise", "meditation", "breathing_basics",
                    "Start your week calm and focused", "New week, fresh mindset! 🌟", 5);
            } else {
                return new DailyRecommendation(day, "Thought Challenging Exercise", "cbt", "thought_challenging",
                    "Set positive intentions for the week", "You've got this week! 💪", 10);
            }
        }
        
        // Tuesday - Build momentum
        if (day.equals("Tuesday")) {
            if (preferredActivity.equals("meditation")) {
                return new DailyRecommendation(day, "Focus & Concentration Session", "meditation", "focus_concentration",
                    "Enhance your focus for the week ahead", "Building momentum! 🚀", 10);
            } else {
                return new DailyRecommendation(day, "Mood-Thought Connection", "cbt", "mood_thought_connection",
                    "Understand your emotional patterns", "Self-awareness is power! 🧠", 12);
            }
        }
        
        // Wednesday - Mid-week support
        if (day.equals("Wednesday")) {
            if (dominantMood.equals("Sad") || dominantMood.equals("Lonely")) {
                return new DailyRecommendation(day, "Confidence Building Session", "meditation", "confidence_building",
                    "Boost your mid-week confidence", "You're stronger than you know! ✨", 15);
            } else {
                return new DailyRecommendation(day, "Evidence Examination", "cbt", "evidence_examination",
                    "Challenge negative thoughts mid-week", "Facts over fears! 🔍", 15);
            }
        }
        
        // Thursday - Energy boost
        if (day.equals("Thursday")) {
            if (engagementLevel.equals("high") || streak >= 3) {
                return new DailyRecommendation(day, "Advanced Mindfulness", "meditation", "stress_relief_advanced",
                    "You're doing great! Try something new", "Level up your practice! 🎯", 20);
            } else {
                return new DailyRecommendation(day, "Quick Energy Boost", "meditation", "quick_energy",
                    "Energize yourself for the day", "Almost to the weekend! ⚡", 7);
            }
        }
        
        // Friday - Week completion
        if (day.equals("Friday")) {
            return new DailyRecommendation(day, "Positive Reframing", "cbt", "positive_reframing",
                "End your week on a positive note", "You made it through the week! 🎉", 10);
        }
        
        // Saturday - Self-care
        if (day.equals("Saturday")) {
            if (dominantMood.equals("Happy") || dominantMood.equals("Excited")) {
                return new DailyRecommendation(day, "Gratitude Meditation", "meditation", "confidence_building",
                    "Celebrate your positive energy", "Weekend vibes! Enjoy this moment 🌞", 15);
            } else {
                return new DailyRecommendation(day, "Self-Care Reflection", "meditation", "stress_relief_basics",
                    "Take time for yourself today", "You deserve this self-care time 💚", 12);
            }
        }
        
        // Sunday - Week preparation
        if (day.equals("Sunday")) {
            return new DailyRecommendation(day, "Weekly Reflection & Planning", "cbt", "thought_challenging",
                "Reflect on your week and prepare for the next", "Ready for another great week! 🌟", 15);
        }
        
        // Default fallback
        return new DailyRecommendation(day, "Mindful Breathing", "meditation", "breathing_basics",
            "A perfect activity for any day", "Take a moment for yourself 🌸", 5);
    }
    
    /**
     * Generates weekly insights based on user patterns
     */
    public List<WeeklyInsight> generateWeeklyInsights() {
        List<WeeklyInsight> insights = new ArrayList<>();
        Map<String, Object> analysis = analyzeUserPatterns();
        
        // Mood pattern insight
        String dominantMood = (String) analysis.get("dominantMood");
        if (!dominantMood.equals("neutral")) {
            insights.add(generateMoodPatternInsight(dominantMood, analysis));
        }
        
        // Streak motivation insight
        int currentStreak = (Integer) analysis.get("currentStreak");
        insights.add(generateStreakInsight(currentStreak));
        
        // Activity preference insight
        @SuppressWarnings("unchecked")
        Map<String, Integer> activityPrefs = (Map<String, Integer>) analysis.get("activityPreferences");
        insights.add(generateActivityInsight(activityPrefs));
        
        Log.d(TAG, "Generated " + insights.size() + " weekly insights");
        return insights;
    }
    
    /**
     * Generates mood pattern insights
     */
    private WeeklyInsight generateMoodPatternInsight(String dominantMood, Map<String, Object> analysis) {
        switch (dominantMood) {
            case "Stressed":
                return new WeeklyInsight("mood_pattern", "Stress Pattern Detected",
                    "You've been feeling stressed lately. This is completely normal!",
                    "Try our breathing exercises and stress-relief meditations");
            case "Anxious":
                return new WeeklyInsight("mood_pattern", "Managing Anxiety",
                    "Anxiety has been present in your recent check-ins.",
                    "CBT exercises can help challenge anxious thoughts");
            case "Sad":
                return new WeeklyInsight("mood_pattern", "Supporting Your Mood",
                    "You've had some tough days recently. You're not alone.",
                    "Confidence-building activities might help lift your spirits");
            case "Happy":
                return new WeeklyInsight("mood_pattern", "Positive Energy Detected!",
                    "You've been feeling great lately - that's wonderful!",
                    "Keep up the good work with your mental health practices");
            default:
                return new WeeklyInsight("mood_pattern", "Balanced Emotions",
                    "Your mood has been relatively stable recently.",
                    "Continue your current mental health practices");
        }
    }
    
    /**
     * Generates streak motivation insights
     */
    private WeeklyInsight generateStreakInsight(int streak) {
        if (streak >= 7) {
            return new WeeklyInsight("streak_motivation", "Amazing Streak! 🔥",
                "You've maintained a " + streak + "-day streak! That's incredible dedication.",
                "Keep it up - you're building a powerful habit");
        } else if (streak >= 3) {
            return new WeeklyInsight("streak_motivation", "Building Momentum 🚀",
                "You're on a " + streak + "-day streak! You're doing great.",
                "Just a few more days to reach a full week!");
        } else if (streak >= 1) {
            return new WeeklyInsight("streak_motivation", "Great Start! ⭐",
                "You've started your mental health journey. Every day counts!",
                "Try to check in daily to build your streak");
        } else {
            return new WeeklyInsight("streak_motivation", "Ready to Begin? 🌟",
                "Starting your mental health journey is the hardest part.",
                "Log your mood today to begin building healthy habits");
        }
    }
    
    /**
     * Generates activity preference insights
     */
    private WeeklyInsight generateActivityInsight(Map<String, Integer> activityPrefs) {
        int meditationCount = activityPrefs.getOrDefault("meditation", 0);
        int cbtCount = activityPrefs.getOrDefault("cbt", 0);
        
        if (meditationCount > cbtCount * 2) {
            return new WeeklyInsight("activity_preference", "Meditation Lover 🧘",
                "You really enjoy meditation sessions! That's fantastic.",
                "Try mixing in some CBT exercises for a well-rounded approach");
        } else if (cbtCount > meditationCount * 2) {
            return new WeeklyInsight("activity_preference", "CBT Champion 🧠",
                "You're great at completing CBT exercises! Keep it up.",
                "Consider adding meditation for relaxation and mindfulness");
        } else if (meditationCount + cbtCount >= 5) {
            return new WeeklyInsight("activity_preference", "Well-Rounded Practice ⚖️",
                "You're doing great with both meditation and CBT exercises!",
                "Your balanced approach is excellent for mental wellness");
        } else {
            return new WeeklyInsight("activity_preference", "Explore More Activities 🔍",
                "There are many activities to discover in MindSpace.",
                "Try both meditation and CBT exercises to find what works best");
        }
    }
} 