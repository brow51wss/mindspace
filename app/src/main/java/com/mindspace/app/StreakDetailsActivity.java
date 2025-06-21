package com.mindspace.app;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class StreakDetailsActivity extends AppCompatActivity {
    private static final String TAG = "StreakDetails";
    
    private TextView overallStreakEmoji, overallStreakText, streakMotivation;
    private TextView moodStreakText, moodStreakEmoji;
    private TextView meditationStreakText, meditationStreakEmoji;
    private TextView cbtStreakText, cbtStreakEmoji;
    private LinearLayout milestone3, milestone7, milestone14, milestone21, milestone30;
    
    private StreakTracker streakTracker;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_streak_details);
        
        Log.d(TAG, "Streak Details Activity started");
        
        // Initialize streak tracker
        streakTracker = new StreakTracker(this);
        
        initializeViews();
        loadStreakData();
        updateMilestoneDisplay();
    }
    
    private void initializeViews() {
        // Header
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            Log.d(TAG, "Back button clicked");
            finish();
        });
        
        // Overall streak
        overallStreakEmoji = findViewById(R.id.overallStreakEmoji);
        overallStreakText = findViewById(R.id.overallStreakText);
        streakMotivation = findViewById(R.id.streakMotivation);
        
        // Individual streaks
        moodStreakText = findViewById(R.id.moodStreakText);
        moodStreakEmoji = findViewById(R.id.moodStreakEmoji);
        meditationStreakText = findViewById(R.id.meditationStreakText);
        meditationStreakEmoji = findViewById(R.id.meditationStreakEmoji);
        cbtStreakText = findViewById(R.id.cbtStreakText);
        cbtStreakEmoji = findViewById(R.id.cbtStreakEmoji);
        
        // Milestones
        milestone3 = findViewById(R.id.milestone3);
        milestone7 = findViewById(R.id.milestone7);
        milestone14 = findViewById(R.id.milestone14);
        milestone21 = findViewById(R.id.milestone21);
        milestone30 = findViewById(R.id.milestone30);
    }
    
    private void loadStreakData() {
        // Get all streak statistics
        StreakTracker.StreakStats stats = streakTracker.getAllStreakStats();
        int highestStreak = stats.overallCurrent;
        
        // Update overall streak display
        String overallEmoji = streakTracker.getStreakEmoji(highestStreak);
        overallStreakEmoji.setText(overallEmoji);
        
        if (highestStreak > 0) {
            overallStreakText.setText(highestStreak + " Day Streak!");
            streakMotivation.setText(getStreakMotivation(highestStreak));
        } else {
            overallStreakText.setText("Start Your Streak!");
            streakMotivation.setText("Complete any activity today to begin your journey! 🌟");
        }
        
        // Update individual streak displays
        updateIndividualStreak(moodStreakText, moodStreakEmoji, stats.moodCurrent, stats.moodBest, "Mood");
        updateIndividualStreak(meditationStreakText, meditationStreakEmoji, stats.meditationCurrent, stats.meditationBest, "Meditation");
        updateIndividualStreak(cbtStreakText, cbtStreakEmoji, stats.cbtCurrent, stats.cbtBest, "CBT");
        
        Log.d(TAG, "Streak data loaded - Overall: " + highestStreak + 
              ", Mood: " + stats.moodCurrent + ", Meditation: " + stats.meditationCurrent + 
              ", CBT: " + stats.cbtCurrent);
    }
    
    private void updateIndividualStreak(TextView streakText, TextView streakEmoji, int current, int best, String type) {
        if (current > 0 || best > 0) {
            streakText.setText("Current: " + current + " days • Best: " + best + " days");
            streakEmoji.setText(streakTracker.getStreakEmoji(current));
        } else {
            streakText.setText("No streak yet • Start today!");
            streakEmoji.setText("🌟");
        }
    }
    
    private String getStreakMotivation(int streak) {
        if (streak == 0) {
            return "Every journey begins with a single step! 🚀";
        } else if (streak < 3) {
            return "Great start! You're building momentum! 💪";
        } else if (streak < 7) {
            return "You're on fire! Keep the streak alive! 🔥";
        } else if (streak < 14) {
            return "Amazing consistency! You're forming great habits! ⭐";
        } else if (streak < 21) {
            return "Incredible dedication! You're a wellness champion! 🏆";
        } else if (streak < 30) {
            return "Outstanding commitment! You're an inspiration! 🌟";
        } else {
            return "Legendary streak! You're a true wellness master! 👑";
        }
    }
    
    private void updateMilestoneDisplay() {
        int highestStreak = streakTracker.getHighestCurrentStreak();
        
        // Update milestone indicators based on achieved streaks
        updateMilestoneStatus(milestone3, 3, highestStreak);
        updateMilestoneStatus(milestone7, 7, highestStreak);
        updateMilestoneStatus(milestone14, 14, highestStreak);
        updateMilestoneStatus(milestone21, 21, highestStreak);
        updateMilestoneStatus(milestone30, 30, highestStreak);
    }
    
    private void updateMilestoneStatus(LinearLayout milestone, int requiredStreak, int currentStreak) {
        TextView icon = (TextView) milestone.getChildAt(0);
        TextView title = (TextView) milestone.getChildAt(1);
        TextView xp = (TextView) milestone.getChildAt(2);
        
        if (currentStreak >= requiredStreak) {
            // Milestone achieved
            icon.setText("✅");
            title.setTextColor(Color.parseColor("#27AE60"));
            xp.setTextColor(Color.parseColor("#27AE60"));
        } else {
            // Milestone not yet achieved
            icon.setText("🎯");
            title.setTextColor(Color.parseColor("#7F8C8D"));
            xp.setTextColor(Color.parseColor("#7F8C8D"));
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Refresh streak data when returning to activity
        loadStreakData();
        updateMilestoneDisplay();
    }
} 