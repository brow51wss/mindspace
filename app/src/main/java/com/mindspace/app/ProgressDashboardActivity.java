package com.mindspace.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import java.text.SimpleDateFormat;
import java.util.*;

public class ProgressDashboardActivity extends AppCompatActivity {
    private static final String TAG = "ProgressDashboard";
    
    private TextView totalDaysText, currentStreakText, totalXPText, currentLevelText;
    private TextView moodEntriesText, meditationMinutesText, cbtExercisesText;
    private TextView weeklyInsightText, motivationalText;
    private LinearLayout chartContainer, statsContainer;
    private CardView moodCard, meditationCard, cbtCard, achievementCard;
    private StreakTracker streakTracker;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_dashboard);
        
        Log.d(TAG, "Progress Dashboard Activity started");
        
        // Initialize streak tracker
        streakTracker = new StreakTracker(this);
        
        initializeViews();
        loadProgressData();
        createMoodChart();
        updateStatsCards();
        generateInsights();
    }
    
    private void initializeViews() {
        // Header stats
        totalDaysText = findViewById(R.id.totalDaysText);
        currentStreakText = findViewById(R.id.currentStreakText);
        totalXPText = findViewById(R.id.totalXPText);
        currentLevelText = findViewById(R.id.currentLevelText);
        
        // Activity stats
        moodEntriesText = findViewById(R.id.moodEntriesText);
        meditationMinutesText = findViewById(R.id.meditationMinutesText);
        cbtExercisesText = findViewById(R.id.cbtExercisesText);
        
        // Insights
        weeklyInsightText = findViewById(R.id.weeklyInsightText);
        motivationalText = findViewById(R.id.motivationalText);
        
        // Containers
        chartContainer = findViewById(R.id.chartContainer);
        statsContainer = findViewById(R.id.statsContainer);
        
        // Cards
        moodCard = findViewById(R.id.moodCard);
        meditationCard = findViewById(R.id.meditationCard);
        cbtCard = findViewById(R.id.cbtCard);
        achievementCard = findViewById(R.id.achievementCard);
        
        // Back button
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            Log.d(TAG, "Back button clicked");
            finish();
        });
    }
    
    private void loadProgressData() {
        // Load data from all SharedPreferences
        SharedPreferences moodPrefs = getSharedPreferences("MindSpaceMoods", Context.MODE_PRIVATE);
        SharedPreferences meditationPrefs = getSharedPreferences("MindSpaceMeditation", Context.MODE_PRIVATE);
        SharedPreferences cbtPrefs = getSharedPreferences("MindSpaceCBT", Context.MODE_PRIVATE);
        SharedPreferences achievementPrefs = getSharedPreferences("MindSpaceAchievements", Context.MODE_PRIVATE);
        
        // Calculate total active days
        Set<String> activeDays = new HashSet<>();
        
        // Add mood tracking days
        int moodEntries = moodPrefs.getInt("total_entries", 0);
        if (moodEntries > 0) {
            activeDays.add("mood_" + moodPrefs.getString("last_entry_date", ""));
        }
        
        // Add meditation days
        int meditationSessions = meditationPrefs.getInt("total_sessions", 0);
        if (meditationSessions > 0) {
            activeDays.add("meditation_" + meditationPrefs.getString("last_session_date", ""));
        }
        
        // Add CBT days
        int cbtExercises = cbtPrefs.getInt("total_exercises", 0);
        if (cbtExercises > 0) {
            activeDays.add("cbt_" + getCurrentDate());
        }
        
        // Update header stats using new StreakTracker
        totalDaysText.setText(String.valueOf(Math.max(activeDays.size(), 1)));
        
        // Get highest current streak from StreakTracker
        int highestStreak = streakTracker.getHighestCurrentStreak();
        String streakDisplay = streakTracker.getStreakEmoji(highestStreak) + " " + Math.max(highestStreak, 1);
        currentStreakText.setText(streakDisplay);
        
        int totalXP = achievementPrefs.getInt("total_xp", 0);
        totalXPText.setText(totalXP + " XP");
        currentLevelText.setText("Level " + calculateLevel(totalXP));
        
        // Update activity stats
        moodEntriesText.setText(String.valueOf(moodEntries));
        
        // Calculate meditation minutes (estimate 10 minutes per session)
        int estimatedMinutes = meditationSessions * 10;
        meditationMinutesText.setText(estimatedMinutes + " min");
        
        cbtExercisesText.setText(String.valueOf(cbtExercises));
        
        Log.d(TAG, "Progress data loaded - Days: " + activeDays.size() + 
              ", XP: " + totalXP + ", Level: " + calculateLevel(totalXP));
    }
    
    private void createMoodChart() {
        // Create a simple visual mood chart representation
        SharedPreferences moodPrefs = getSharedPreferences("MindSpaceMoods", Context.MODE_PRIVATE);
        
        // Clear existing chart
        chartContainer.removeAllViews();
        
        // Create mock mood data for the last 7 days
        String[] moodLabels = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        String[] moodEmojis = {"😊", "😌", "😰", "😊", "😢", "😊", "😌"};
        
        // Create horizontal layout for chart
        LinearLayout chartRow = new LinearLayout(this);
        chartRow.setOrientation(LinearLayout.HORIZONTAL);
        chartRow.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        
        for (int i = 0; i < moodLabels.length; i++) {
            LinearLayout dayColumn = new LinearLayout(this);
            dayColumn.setOrientation(LinearLayout.VERTICAL);
            dayColumn.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            ));
            dayColumn.setGravity(android.view.Gravity.CENTER);
            dayColumn.setPadding(8, 16, 8, 16);
            
            // Mood emoji
            TextView moodEmoji = new TextView(this);
            moodEmoji.setText(moodEmojis[i]);
            moodEmoji.setTextSize(24);
            moodEmoji.setGravity(android.view.Gravity.CENTER);
            
            // Day label
            TextView dayLabel = new TextView(this);
            dayLabel.setText(moodLabels[i]);
            dayLabel.setTextSize(12);
            dayLabel.setTextColor(Color.parseColor("#666666"));
            dayLabel.setGravity(android.view.Gravity.CENTER);
            dayLabel.setPadding(0, 8, 0, 0);
            
            dayColumn.addView(moodEmoji);
            dayColumn.addView(dayLabel);
            chartRow.addView(dayColumn);
        }
        
        chartContainer.addView(chartRow);
        
        Log.d(TAG, "Mood chart created with 7 days of data");
    }
    
    private void updateStatsCards() {
        // Set card click listeners and styling
        moodCard.setOnClickListener(v -> {
            // Could navigate to detailed mood history
            Log.d(TAG, "Mood card clicked");
        });
        
        meditationCard.setOnClickListener(v -> {
            // Could navigate to meditation stats
            Log.d(TAG, "Meditation card clicked");
        });
        
        cbtCard.setOnClickListener(v -> {
            // Could navigate to CBT progress
            Log.d(TAG, "CBT card clicked");
        });
        
        achievementCard.setOnClickListener(v -> {
            // Navigate to achievements
            Log.d(TAG, "Achievement card clicked");
            startActivity(new android.content.Intent(this, AchievementSystemActivity.class));
        });
        
        // Add subtle animations
        addCardHoverEffect(moodCard);
        addCardHoverEffect(meditationCard);
        addCardHoverEffect(cbtCard);
        addCardHoverEffect(achievementCard);
    }
    
    private void addCardHoverEffect(CardView card) {
        card.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case android.view.MotionEvent.ACTION_DOWN:
                    v.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100);
                    break;
                case android.view.MotionEvent.ACTION_UP:
                case android.view.MotionEvent.ACTION_CANCEL:
                    v.animate().scaleX(1f).scaleY(1f).setDuration(100);
                    v.performClick();
                    break;
            }
            return true;
        });
    }
    
    private void generateInsights() {
        SharedPreferences moodPrefs = getSharedPreferences("MindSpaceMoods", Context.MODE_PRIVATE);
        SharedPreferences meditationPrefs = getSharedPreferences("MindSpaceMeditation", Context.MODE_PRIVATE);
        SharedPreferences cbtPrefs = getSharedPreferences("MindSpaceCBT", Context.MODE_PRIVATE);
        
        // Generate weekly insight
        int moodStreak = moodPrefs.getInt("current_streak", 0);
        int meditationSessions = meditationPrefs.getInt("total_sessions", 0);
        int cbtExercises = cbtPrefs.getInt("total_exercises", 0);
        
        String weeklyInsight;
        if (moodStreak >= 7) {
            weeklyInsight = "🔥 Amazing! You've maintained a " + moodStreak + "-day mood tracking streak!";
        } else if (meditationSessions >= 5) {
            weeklyInsight = "🧘‍♀️ Great progress! You've completed " + meditationSessions + " meditation sessions.";
        } else if (cbtExercises >= 3) {
            weeklyInsight = "🧠 Excellent work! You've practiced " + cbtExercises + " CBT exercises.";
        } else {
            weeklyInsight = "🌟 You're building healthy mental wellness habits. Keep going!";
        }
        
        weeklyInsightText.setText(weeklyInsight);
        
        // Generate motivational message
        String[] motivationalMessages = {
            "💪 Every small step counts toward better mental health.",
            "🌱 You're growing stronger with each mindful moment.",
            "✨ Your commitment to self-care is inspiring.",
            "🎯 Focus on progress, not perfection.",
            "🌈 You have the power to shape your mental wellness journey.",
            "💙 Be kind to yourself - you're doing great!",
            "🚀 Your future self will thank you for the work you're doing today."
        };
        
        Random random = new Random();
        String motivationalMessage = motivationalMessages[random.nextInt(motivationalMessages.length)];
        motivationalText.setText(motivationalMessage);
        
        Log.d(TAG, "Insights generated - Weekly: " + weeklyInsight);
    }
    
    private int calculateLevel(int xp) {
        return (xp / 100) + 1;
    }
    
    private String getCurrentDate() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }
} 