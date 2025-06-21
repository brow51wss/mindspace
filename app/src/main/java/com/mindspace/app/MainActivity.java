package com.mindspace.app;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView dateDisplay;
    private Button moodHappy, moodCalm, moodStressed, moodAnxious;
    private Button moodSad, moodExcited, moodTired, moodAngry;
    private Button viewHistory, resourcesButton, meditationButton, cbtButton, progressButton, achievementsButton, personalPlanButton;
    private TextView streakCounter;
    private StreakTracker streakTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Initialize streak tracker
        streakTracker = new StreakTracker(this);
        
        initializeViews();
        setupDateDisplay();
        setupMoodButtons();
        updateStreakDisplay();
    }
    
    private void initializeViews() {
        dateDisplay = findViewById(R.id.date_display);
        
        // Mood buttons
        moodHappy = findViewById(R.id.mood_happy);
        moodCalm = findViewById(R.id.mood_calm);
        moodStressed = findViewById(R.id.mood_stressed);
        moodAnxious = findViewById(R.id.mood_anxious);
        moodSad = findViewById(R.id.mood_sad);
        moodExcited = findViewById(R.id.mood_excited);
        moodTired = findViewById(R.id.mood_tired);
        moodAngry = findViewById(R.id.mood_angry);
        
        // Other elements
        viewHistory = findViewById(R.id.view_history);
        resourcesButton = findViewById(R.id.resources_button);
        meditationButton = findViewById(R.id.meditation_button);
        cbtButton = findViewById(R.id.cbt_button);
        progressButton = findViewById(R.id.progress_button);
        achievementsButton = findViewById(R.id.achievements_button);
        personalPlanButton = findViewById(R.id.personal_plan_button);
        streakCounter = findViewById(R.id.streak_counter);
        
        // Make streak counter clickable
        streakCounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StreakDetailsActivity.class);
                startActivity(intent);
            }
        });
    }
    
    private void setupDateDisplay() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM d", Locale.getDefault());
        String currentDate = sdf.format(new Date());
        dateDisplay.setText(currentDate);
    }
    
    private void setupMoodButtons() {
        View.OnClickListener moodClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mood = "";
                
                if (v == moodHappy) mood = "Happy";
                else if (v == moodCalm) mood = "Calm";
                else if (v == moodStressed) mood = "Stressed";
                else if (v == moodAnxious) mood = "Anxious";
                else if (v == moodSad) mood = "Sad";
                else if (v == moodExcited) mood = "Excited";
                else if (v == moodTired) mood = "Tired";
                else if (v == moodAngry) mood = "Angry";
                
                logMood(mood);
            }
        };
        
        // Set click listeners for all mood buttons
        moodHappy.setOnClickListener(moodClickListener);
        moodCalm.setOnClickListener(moodClickListener);
        moodStressed.setOnClickListener(moodClickListener);
        moodAnxious.setOnClickListener(moodClickListener);
        moodSad.setOnClickListener(moodClickListener);
        moodExcited.setOnClickListener(moodClickListener);
        moodTired.setOnClickListener(moodClickListener);
        moodAngry.setOnClickListener(moodClickListener);
        
        // History button click listener
        viewHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MoodHistoryActivity.class);
                startActivity(intent);
            }
        });
        
        // Resources button click listener
        resourcesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ResourceHubActivity.class);
                startActivity(intent);
            }
        });
        
        // Meditation button click listener
        meditationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MeditationLibraryActivity.class);
                startActivity(intent);
            }
        });
        
        // CBT button click listener
        cbtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CBTExercisesActivity.class);
                startActivity(intent);
            }
        });
        
        // Progress button click listener
        progressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProgressDashboardActivity.class);
                startActivity(intent);
            }
        });
        
        // Achievements button click listener
        achievementsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AchievementSystemActivity.class);
                startActivity(intent);
            }
        });
        
        // Personal Plan button click listener
        personalPlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PersonalizedPlanActivity.class);
                startActivity(intent);
            }
        });
    }
    
    private void logMood(String mood) {
        // Save mood data for achievements
        SharedPreferences moodPrefs = getSharedPreferences("MindSpaceMoods", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = moodPrefs.edit();
        
        // Update mood tracking stats
        int totalEntries = moodPrefs.getInt("total_entries", 0) + 1;
        editor.putInt("total_entries", totalEntries);
        editor.putLong("last_entry_time", System.currentTimeMillis());
        
        // Store time for early bird achievement
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        editor.putString("last_entry_time_formatted", timeFormat.format(new Date()));
        
        // Simple streak tracking (would be more sophisticated in real app)
        String lastEntryDate = moodPrefs.getString("last_entry_date", "");
        String todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        
        if (!todayDate.equals(lastEntryDate)) {
            int currentStreak = moodPrefs.getInt("current_streak", 0) + 1;
            editor.putInt("current_streak", currentStreak);
            editor.putString("last_entry_date", todayDate);
        }
        
        editor.apply();
        
        Log.d("MainActivity", "Mood logged: " + mood + " (Total entries: " + totalEntries + ")");
        
        // Update streak tracking
        StreakTracker.StreakResult streakResult = streakTracker.updateStreak(StreakTracker.StreakType.MOOD);
        updateStreakDisplay();
        
        // Show success message with streak info
        String message = "Feeling " + mood.toLowerCase() + " today! 🌟";
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        
        // Show streak milestone message if reached
        if (streakResult.milestoneReached > 0 || streakResult.isNewBest) {
            String streakMessage = streakTracker.getMotivationalMessage(streakResult);
            showDelayedToast(streakMessage, 2500);
        }
        
        // Show mood-based suggestion
        showMoodSuggestion(mood);
    }
    
    private void showMoodSuggestion(String mood) {
        final String suggestion;
        
        switch (mood) {
            case "Happy":
                suggestion = "Great! Share your positivity with others today! 😊";
                break;
            case "Calm":
                suggestion = "Perfect mood for meditation or journaling 🧘‍♀️";
                break;
            case "Stressed":
                suggestion = "Try some deep breathing exercises or a quick walk 🌬️";
                break;
            case "Anxious":
                suggestion = "Ground yourself: Name 5 things you can see around you 👀";
                break;
            case "Sad":
                suggestion = "It's okay to feel sad. Maybe listen to uplifting music? 🎵";
                break;
            case "Excited":
                suggestion = "Channel that energy into something creative! ⚡";
                break;
            case "Tired":
                suggestion = "Rest is important. Consider a power nap or early bedtime 😴";
                break;
            case "Angry":
                suggestion = "Take 10 deep breaths. Physical exercise can help too 💪";
                break;
            default:
                suggestion = "Take a moment to reflect on your feelings 🌟";
                break;
        }
        
        // Show suggestion after a brief delay
        android.os.Handler handler = new android.os.Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, suggestion, Toast.LENGTH_LONG).show();
            }
        }, 2000);
    }
    
    private void updateStreakDisplay() {
        String streakText = streakTracker.getStreakDisplayText();
        streakCounter.setText(streakText);
        Log.d("MainActivity", "Streak display updated: " + streakText);
    }
    
    private void showDelayedToast(String message, int delayMillis) {
        android.os.Handler handler = new android.os.Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
            }
        }, delayMillis);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Update streak display when returning to main screen
        updateStreakDisplay();
    }
} 