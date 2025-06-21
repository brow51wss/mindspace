package com.mindspace.app;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
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
    private Button viewHistory, resourcesButton, meditationButton;
    private TextView streakCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initializeViews();
        setupDateDisplay();
        setupMoodButtons();
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
        streakCounter = findViewById(R.id.streak_counter);
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
    }
    
    private void logMood(String mood) {
        // TODO: Save mood to database
        
        // For now, show a success message
        String message = "Feeling " + mood.toLowerCase() + " today! 🌟";
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        
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
} 