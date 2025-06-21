package com.mindspace.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.*;

public class StreakTracker {
    private static final String TAG = "StreakTracker";
    
    // Streak types
    public enum StreakType {
        MOOD("MindSpaceMoods", "mood_streak"),
        MEDITATION("MindSpaceMeditation", "meditation_streak"),
        CBT("MindSpaceCBT", "cbt_streak"),
        OVERALL("MindSpaceStreaks", "overall_streak");
        
        public final String prefsName;
        public final String streakKey;
        
        StreakType(String prefsName, String streakKey) {
            this.prefsName = prefsName;
            this.streakKey = streakKey;
        }
    }
    
    // Streak milestones with rewards
    private static final int[] STREAK_MILESTONES = {3, 7, 14, 21, 30, 50, 100};
    private static final int[] MILESTONE_XP = {25, 50, 100, 150, 250, 500, 1000};
    
    private Context context;
    
    public StreakTracker(Context context) {
        this.context = context;
    }
    
    /**
     * Update streak for a specific activity type
     */
    public StreakResult updateStreak(StreakType type) {
        SharedPreferences prefs = context.getSharedPreferences(type.prefsName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        
        String todayDate = getCurrentDate();
        String lastActivityDate = prefs.getString("last_activity_date", "");
        int currentStreak = prefs.getInt(type.streakKey, 0);
        int bestStreak = prefs.getInt(type.streakKey + "_best", 0);
        
        StreakResult result = new StreakResult();
        result.streakType = type;
        result.previousStreak = currentStreak;
        
        if (todayDate.equals(lastActivityDate)) {
            // Already completed today, no change
            result.newStreak = currentStreak;
            result.isNewActivity = false;
            Log.d(TAG, type.name() + " already completed today, streak: " + currentStreak);
        } else if (isConsecutiveDay(lastActivityDate, todayDate)) {
            // Consecutive day - increase streak
            result.newStreak = currentStreak + 1;
            result.isNewActivity = true;
            result.isStreakIncreased = true;
            
            // Check for milestone
            result.milestoneReached = checkMilestone(result.newStreak);
            if (result.milestoneReached > 0) {
                result.milestoneXP = getMilestoneXP(result.milestoneReached);
                awardMilestoneXP(result.milestoneXP);
            }
            
            // Update best streak
            if (result.newStreak > bestStreak) {
                editor.putInt(type.streakKey + "_best", result.newStreak);
                result.isNewBest = true;
            }
            
            Log.d(TAG, type.name() + " streak increased: " + currentStreak + " → " + result.newStreak);
        } else {
            // Streak broken - reset to 1
            result.newStreak = 1;
            result.isNewActivity = true;
            result.isStreakBroken = currentStreak > 0;
            
            Log.d(TAG, type.name() + " streak reset: " + currentStreak + " → 1");
        }
        
        // Save updated data
        editor.putInt(type.streakKey, result.newStreak);
        editor.putString("last_activity_date", todayDate);
        editor.putLong("last_activity_timestamp", System.currentTimeMillis());
        editor.apply();
        
        // Update overall streak
        if (result.isNewActivity) {
            updateOverallStreak();
        }
        
        return result;
    }
    
    /**
     * Get current streak for a specific type
     */
    public int getCurrentStreak(StreakType type) {
        SharedPreferences prefs = context.getSharedPreferences(type.prefsName, Context.MODE_PRIVATE);
        
        // Check if streak is still valid (not broken by missing yesterday)
        String lastActivityDate = prefs.getString("last_activity_date", "");
        String todayDate = getCurrentDate();
        String yesterdayDate = getYesterdayDate();
        
        if (lastActivityDate.equals(todayDate) || lastActivityDate.equals(yesterdayDate)) {
            return prefs.getInt(type.streakKey, 0);
        } else {
            // Streak is broken, reset it
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(type.streakKey, 0);
            editor.apply();
            return 0;
        }
    }
    
    /**
     * Get the highest current streak across all activities
     */
    public int getHighestCurrentStreak() {
        int maxStreak = 0;
        for (StreakType type : StreakType.values()) {
            if (type != StreakType.OVERALL) {
                maxStreak = Math.max(maxStreak, getCurrentStreak(type));
            }
        }
        return maxStreak;
    }
    
    /**
     * Get fire emoji representation of streak
     */
    public String getStreakEmoji(int streak) {
        if (streak == 0) return "";
        if (streak < 3) return "🔥";
        if (streak < 7) return "🔥🔥";
        if (streak < 14) return "🔥🔥🔥";
        if (streak < 21) return "🔥🔥🔥🔥";
        if (streak < 30) return "🔥🔥🔥🔥🔥";
        return "🔥🔥🔥🔥🔥🔥"; // Max display
    }
    
    /**
     * Get motivational message based on streak status
     */
    public String getMotivationalMessage(StreakResult result) {
        if (result.milestoneReached > 0) {
            return "🎉 Amazing! " + result.milestoneReached + " day milestone reached! +" + result.milestoneXP + " XP!";
        }
        
        if (result.isNewBest) {
            return "🌟 New personal best! " + result.newStreak + " days in a row!";
        }
        
        if (result.isStreakBroken && result.previousStreak >= 3) {
            return "💪 Don't give up! Every expert was once a beginner. Start fresh!";
        }
        
        if (result.isStreakIncreased) {
            switch (result.newStreak) {
                case 2: return "🔥 Great start! Keep the momentum going!";
                case 3: return "🔥🔥 You're on fire! 3 days strong!";
                case 7: return "🔥🔥🔥 One week completed! You're building a great habit!";
                case 14: return "🔥🔥🔥🔥 Two weeks! Your dedication is inspiring!";
                case 21: return "🔥🔥🔥🔥🔥 21 days! You're officially building a lasting habit!";
                case 30: return "🔥🔥🔥🔥🔥🔥 One month! You're a wellness champion!";
                default:
                    if (result.newStreak % 10 == 0) {
                        return "🔥 " + result.newStreak + " days! Your consistency is remarkable!";
                    }
                    return "🔥 Day " + result.newStreak + "! Keep up the excellent work!";
            }
        }
        
        return "🌟 Great job staying consistent with your mental wellness!";
    }
    
    /**
     * Get formatted streak display text
     */
    public String getStreakDisplayText() {
        int highestStreak = getHighestCurrentStreak();
        if (highestStreak == 0) {
            return "Start your streak today! 🌟";
        }
        
        String emoji = getStreakEmoji(highestStreak);
        return emoji + " " + highestStreak + " day streak!";
    }
    
    /**
     * Get all streak statistics
     */
    public StreakStats getAllStreakStats() {
        StreakStats stats = new StreakStats();
        
        for (StreakType type : StreakType.values()) {
            if (type != StreakType.OVERALL) {
                SharedPreferences prefs = context.getSharedPreferences(type.prefsName, Context.MODE_PRIVATE);
                int current = getCurrentStreak(type);
                int best = prefs.getInt(type.streakKey + "_best", 0);
                
                switch (type) {
                    case MOOD:
                        stats.moodCurrent = current;
                        stats.moodBest = best;
                        break;
                    case MEDITATION:
                        stats.meditationCurrent = current;
                        stats.meditationBest = best;
                        break;
                    case CBT:
                        stats.cbtCurrent = current;
                        stats.cbtBest = best;
                        break;
                }
            }
        }
        
        stats.overallCurrent = getHighestCurrentStreak();
        return stats;
    }
    
    // Private helper methods
    private void updateOverallStreak() {
        // Overall streak is the highest individual streak
        int highestStreak = getHighestCurrentStreak();
        SharedPreferences prefs = context.getSharedPreferences(StreakType.OVERALL.prefsName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(StreakType.OVERALL.streakKey, highestStreak);
        editor.apply();
    }
    
    private boolean isConsecutiveDay(String lastDate, String currentDate) {
        if (lastDate.isEmpty()) return false;
        
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date last = sdf.parse(lastDate);
            Date current = sdf.parse(currentDate);
            
            Calendar cal = Calendar.getInstance();
            cal.setTime(last);
            cal.add(Calendar.DAY_OF_MONTH, 1);
            
            return sdf.format(cal.getTime()).equals(currentDate);
        } catch (Exception e) {
            Log.e(TAG, "Error checking consecutive days", e);
            return false;
        }
    }
    
    private int checkMilestone(int streak) {
        for (int milestone : STREAK_MILESTONES) {
            if (streak == milestone) {
                return milestone;
            }
        }
        return 0;
    }
    
    private int getMilestoneXP(int milestone) {
        for (int i = 0; i < STREAK_MILESTONES.length; i++) {
            if (STREAK_MILESTONES[i] == milestone) {
                return MILESTONE_XP[i];
            }
        }
        return 0;
    }
    
    private void awardMilestoneXP(int xp) {
        SharedPreferences achievementPrefs = context.getSharedPreferences("MindSpaceAchievements", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = achievementPrefs.edit();
        int currentXP = achievementPrefs.getInt("total_xp", 0);
        editor.putInt("total_xp", currentXP + xp);
        editor.apply();
        
        Log.d(TAG, "Milestone XP awarded: " + xp + " (Total: " + (currentXP + xp) + ")");
    }
    
    private String getCurrentDate() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }
    
    private String getYesterdayDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.getTime());
    }
    
    // Result classes
    public static class StreakResult {
        public StreakType streakType;
        public int previousStreak;
        public int newStreak;
        public boolean isNewActivity;
        public boolean isStreakIncreased;
        public boolean isStreakBroken;
        public boolean isNewBest;
        public int milestoneReached;
        public int milestoneXP;
    }
    
    public static class StreakStats {
        public int moodCurrent;
        public int moodBest;
        public int meditationCurrent;
        public int meditationBest;
        public int cbtCurrent;
        public int cbtBest;
        public int overallCurrent;
    }
} 