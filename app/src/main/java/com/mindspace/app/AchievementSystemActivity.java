package com.mindspace.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.*;

public class AchievementSystemActivity extends AppCompatActivity {
    private static final String TAG = "AchievementSystem";
    private static final String PREFS_NAME = "MindSpaceAchievements";
    
    private RecyclerView achievementsRecyclerView;
    private AchievementAdapter achievementAdapter;
    private List<Achievement> achievements;
    private TextView totalBadgesText, levelText, xpText;
    private SharedPreferences prefs;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement_system);
        
        Log.d(TAG, "Achievement System Activity started");
        
        prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        
        initializeViews();
        initializeAchievements();
        checkAndUpdateAchievements();
        setupRecyclerView();
        updateStatsDisplay();
    }
    
    private void initializeViews() {
        achievementsRecyclerView = findViewById(R.id.achievementsRecyclerView);
        totalBadgesText = findViewById(R.id.totalBadgesText);
        levelText = findViewById(R.id.levelText);
        xpText = findViewById(R.id.xpText);
        
        // Back button
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            Log.d(TAG, "Back button clicked");
            finish();
        });
    }
    
    private void initializeAchievements() {
        achievements = new ArrayList<>();
        
        // Mood Tracking Achievements
        achievements.add(new Achievement("first_mood", "🌟 First Steps", 
            "Log your first mood", 10, false, "mood"));
        achievements.add(new Achievement("mood_3_days", "📅 Consistent Tracker", 
            "Log mood for 3 consecutive days", 25, false, "mood"));
        achievements.add(new Achievement("mood_week", "🗓️ Weekly Warrior", 
            "Log mood for 7 consecutive days", 50, false, "mood"));
        achievements.add(new Achievement("mood_month", "📊 Monthly Master", 
            "Log mood for 30 days", 100, false, "mood"));
        
        // Meditation Achievements
        achievements.add(new Achievement("first_meditation", "🧘 Mindful Beginner", 
            "Complete your first meditation", 15, false, "meditation"));
        achievements.add(new Achievement("meditation_5", "🕯️ Peaceful Mind", 
            "Complete 5 meditation sessions", 35, false, "meditation"));
        achievements.add(new Achievement("meditation_20", "☯️ Zen Master", 
            "Complete 20 meditation sessions", 75, false, "meditation"));
        achievements.add(new Achievement("meditation_streak_7", "🔥 Meditation Streak", 
            "Meditate for 7 days in a row", 60, false, "meditation"));
        
        // CBT Exercise Achievements
        achievements.add(new Achievement("first_cbt", "🧠 Thought Explorer", 
            "Complete your first CBT exercise", 20, false, "cbt"));
        achievements.add(new Achievement("cbt_all_types", "🔄 CBT Champion", 
            "Try all 4 types of CBT exercises", 40, false, "cbt"));
        achievements.add(new Achievement("cbt_10", "💭 Mind Shaper", 
            "Complete 10 CBT exercises", 80, false, "cbt"));
        
        // Special Achievements
        achievements.add(new Achievement("balanced_week", "⚖️ Balanced Life", 
            "Use all features in one week", 100, false, "special"));
        achievements.add(new Achievement("stress_warrior", "💪 Stress Warrior", 
            "Improve mood rating by 3+ points", 50, false, "special"));
        achievements.add(new Achievement("early_bird", "🌅 Early Bird", 
            "Log mood before 9 AM", 30, false, "special"));
        
        Log.d(TAG, "Initialized " + achievements.size() + " achievements");
    }
    
    private void checkAndUpdateAchievements() {
        SharedPreferences moodPrefs = getSharedPreferences("MindSpaceMoods", Context.MODE_PRIVATE);
        SharedPreferences meditationPrefs = getSharedPreferences("MindSpaceMeditation", Context.MODE_PRIVATE);
        SharedPreferences cbtPrefs = getSharedPreferences("MindSpaceCBT", Context.MODE_PRIVATE);
        
        // Check mood achievements
        checkMoodAchievements(moodPrefs);
        
        // Check meditation achievements
        checkMeditationAchievements(meditationPrefs);
        
        // Check CBT achievements
        checkCBTAchievements(cbtPrefs);
        
        // Check special achievements
        checkSpecialAchievements(moodPrefs, meditationPrefs, cbtPrefs);
        
        // Load achievement states from preferences
        loadAchievementStates();
    }
    
    private void checkMoodAchievements(SharedPreferences moodPrefs) {
        int totalMoodEntries = moodPrefs.getInt("total_entries", 0);
        int currentStreak = calculateMoodStreak(moodPrefs);
        
        // First mood
        if (totalMoodEntries > 0) {
            unlockAchievement("first_mood");
        }
        
        // Streak achievements
        if (currentStreak >= 3) {
            unlockAchievement("mood_3_days");
        }
        if (currentStreak >= 7) {
            unlockAchievement("mood_week");
        }
        if (totalMoodEntries >= 30) {
            unlockAchievement("mood_month");
        }
        
        Log.d(TAG, "Mood achievements checked - Total entries: " + totalMoodEntries + ", Streak: " + currentStreak);
    }
    
    private void checkMeditationAchievements(SharedPreferences meditationPrefs) {
        int totalSessions = meditationPrefs.getInt("total_sessions", 0);
        int meditationStreak = meditationPrefs.getInt("current_streak", 0);
        
        if (totalSessions > 0) {
            unlockAchievement("first_meditation");
        }
        if (totalSessions >= 5) {
            unlockAchievement("meditation_5");
        }
        if (totalSessions >= 20) {
            unlockAchievement("meditation_20");
        }
        if (meditationStreak >= 7) {
            unlockAchievement("meditation_streak_7");
        }
        
        Log.d(TAG, "Meditation achievements checked - Total sessions: " + totalSessions);
    }
    
    private void checkCBTAchievements(SharedPreferences cbtPrefs) {
        int totalExercises = cbtPrefs.getInt("total_exercises", 0);
        Set<String> completedTypes = cbtPrefs.getStringSet("completed_types", new HashSet<>());
        
        if (totalExercises > 0) {
            unlockAchievement("first_cbt");
        }
        if (completedTypes.size() >= 4) {
            unlockAchievement("cbt_all_types");
        }
        if (totalExercises >= 10) {
            unlockAchievement("cbt_10");
        }
        
        Log.d(TAG, "CBT achievements checked - Total exercises: " + totalExercises);
    }
    
    private void checkSpecialAchievements(SharedPreferences moodPrefs, 
                                        SharedPreferences meditationPrefs, 
                                        SharedPreferences cbtPrefs) {
        // Check if used all features in past week
        long weekAgo = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000);
        boolean usedMoodThisWeek = moodPrefs.getLong("last_entry_time", 0) > weekAgo;
        boolean usedMeditationThisWeek = meditationPrefs.getLong("last_session_time", 0) > weekAgo;
        boolean usedCBTThisWeek = cbtPrefs.getLong("last_exercise_time", 0) > weekAgo;
        
        if (usedMoodThisWeek && usedMeditationThisWeek && usedCBTThisWeek) {
            unlockAchievement("balanced_week");
        }
        
        // Check early bird (mood logged before 9 AM)
        String lastMoodTime = moodPrefs.getString("last_entry_time_formatted", "");
        if (!lastMoodTime.isEmpty() && isEarlyMorning(lastMoodTime)) {
            unlockAchievement("early_bird");
        }
    }
    
    private boolean isEarlyMorning(String timeString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
            Date time = sdf.parse(timeString);
            Calendar cal = Calendar.getInstance();
            cal.setTime(time);
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            return hour < 9;
        } catch (Exception e) {
            return false;
        }
    }
    
    private int calculateMoodStreak(SharedPreferences moodPrefs) {
        // Simple streak calculation - in real app would be more sophisticated
        return moodPrefs.getInt("current_streak", 0);
    }
    
    private void unlockAchievement(String achievementId) {
        boolean wasUnlocked = prefs.getBoolean(achievementId, false);
        if (!wasUnlocked) {
            prefs.edit().putBoolean(achievementId, true).apply();
            
            // Add XP
            Achievement achievement = findAchievementById(achievementId);
            if (achievement != null) {
                int currentXP = prefs.getInt("total_xp", 0);
                prefs.edit().putInt("total_xp", currentXP + achievement.xpReward).apply();
                
                Log.d(TAG, "Achievement unlocked: " + achievement.title + " (+" + achievement.xpReward + " XP)");
            }
        }
    }
    
    private Achievement findAchievementById(String id) {
        for (Achievement achievement : achievements) {
            if (achievement.id.equals(id)) {
                return achievement;
            }
        }
        return null;
    }
    
    private void loadAchievementStates() {
        for (Achievement achievement : achievements) {
            achievement.isUnlocked = prefs.getBoolean(achievement.id, false);
        }
    }
    
    private void setupRecyclerView() {
        achievementAdapter = new AchievementAdapter(achievements);
        achievementsRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        achievementsRecyclerView.setAdapter(achievementAdapter);
        
        Log.d(TAG, "RecyclerView setup complete");
    }
    
    private void updateStatsDisplay() {
        int unlockedCount = 0;
        for (Achievement achievement : achievements) {
            if (achievement.isUnlocked) {
                unlockedCount++;
            }
        }
        
        int totalXP = prefs.getInt("total_xp", 0);
        int level = calculateLevel(totalXP);
        
        totalBadgesText.setText(unlockedCount + "/" + achievements.size() + " Badges");
        levelText.setText("Level " + level);
        xpText.setText(totalXP + " XP");
        
        Log.d(TAG, "Stats updated - Badges: " + unlockedCount + "/" + achievements.size() + 
              ", Level: " + level + ", XP: " + totalXP);
    }
    
    private int calculateLevel(int xp) {
        // Level system: Level 1 = 0-99 XP, Level 2 = 100-249 XP, etc.
        return (xp / 100) + 1;
    }
    
    // Achievement data class
    public static class Achievement {
        public String id;
        public String title;
        public String description;
        public int xpReward;
        public boolean isUnlocked;
        public String category;
        
        public Achievement(String id, String title, String description, 
                         int xpReward, boolean isUnlocked, String category) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.xpReward = xpReward;
            this.isUnlocked = isUnlocked;
            this.category = category;
        }
    }
} 