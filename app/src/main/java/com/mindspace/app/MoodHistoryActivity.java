package com.mindspace.app;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.ArrayAdapter;
import android.content.SharedPreferences;
import android.content.Context;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class MoodHistoryActivity extends AppCompatActivity {

    private ListView historyListView;
    private LinearLayout emptyStateText;
    private ArrayAdapter<String> historyAdapter;
    private List<String> moodHistory;
    
    // Summary statistics TextViews
    private TextView totalEntriesCount;
    private TextView dayStreakCount;
    private TextView mostCommonMood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_history);
        
        setupActionBar();
        initializeViews();
        loadMoodHistory();
    }
    
    private void setupActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Mood History");
        }
    }
    
    private void initializeViews() {
        historyListView = findViewById(R.id.history_list_view);
        emptyStateText = findViewById(R.id.empty_state_text);
        
        // Initialize summary stats TextViews
        totalEntriesCount = findViewById(R.id.total_entries_count);
        dayStreakCount = findViewById(R.id.day_streak_count);
        mostCommonMood = findViewById(R.id.most_common_mood);
        
        moodHistory = new ArrayList<>();
        historyAdapter = new ArrayAdapter<>(this, 
            android.R.layout.simple_list_item_1, moodHistory);
        historyListView.setAdapter(historyAdapter);
    }
    
    private void loadMoodHistory() {
        // Load REAL mood data from SharedPreferences
        SharedPreferences moodPrefs = getSharedPreferences("MindSpaceMoods", Context.MODE_PRIVATE);
        Map<String, ?> allEntries = moodPrefs.getAll();
        
        // Create list of mood entries with timestamps
        List<MoodEntry> entries = new ArrayList<>();
        
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String key = entry.getKey();
            if (key.startsWith("mood_entry_")) {
                String value = (String) entry.getValue();
                if (value != null && value.contains("|")) {
                    String[] parts = value.split("\\|");
                    if (parts.length == 2) {
                        String mood = parts[0];
                        String dateTime = parts[1];
                        String timestamp = key.replace("mood_entry_", "");
                        entries.add(new MoodEntry(mood, dateTime, Long.parseLong(timestamp)));
                    }
                }
            }
        }
        
        // Sort by timestamp (newest first)
        Collections.sort(entries, new Comparator<MoodEntry>() {
            @Override
            public int compare(MoodEntry a, MoodEntry b) {
                return Long.compare(b.timestamp, a.timestamp);
            }
        });
        
        // Convert to display strings
        moodHistory.clear();
        for (MoodEntry entry : entries) {
            String emoji = getMoodEmoji(entry.mood);
            String displayText = "📅 " + entry.dateTime + " - " + emoji + " " + entry.mood;
            moodHistory.add(displayText);
        }
        
        historyAdapter.notifyDataSetChanged();
        
        // Update summary statistics
        updateSummaryStats(entries);
        
        // Show/hide empty state based on real data
        if (moodHistory.size() > 0) {
            emptyStateText.setVisibility(android.view.View.GONE);
            historyListView.setVisibility(android.view.View.VISIBLE);
        } else {
            emptyStateText.setVisibility(android.view.View.VISIBLE);
            historyListView.setVisibility(android.view.View.GONE);
        }
    }
    
    private String getMoodEmoji(String mood) {
        switch (mood) {
            case "Happy": return "😊";
            case "Calm": return "😌";
            case "Stressed": return "😰";
            case "Anxious": return "😟";
            case "Sad": return "😢";
            case "Excited": return "🤩";
            case "Tired": return "😴";
            case "Angry": return "😠";
            default: return "😐";
        }
    }
    
    private void updateSummaryStats(List<MoodEntry> entries) {
        // 1. Total Entries
        totalEntriesCount.setText(String.valueOf(entries.size()));
        
        // 2. Current Streak (from StreakTracker)
        StreakTracker streakTracker = new StreakTracker(this);
        int currentStreak = streakTracker.getCurrentStreak(StreakTracker.StreakType.MOOD);
        dayStreakCount.setText(String.valueOf(currentStreak));
        
        // 3. Most Common Mood
        if (entries.isEmpty()) {
            mostCommonMood.setText("—");
        } else {
            HashMap<String, Integer> moodCounts = new HashMap<>();
            
            // Count occurrences of each mood
            for (MoodEntry entry : entries) {
                String mood = entry.mood;
                moodCounts.put(mood, moodCounts.getOrDefault(mood, 0) + 1);
            }
            
            // Find most common mood
            String mostCommon = "";
            int maxCount = 0;
            for (Map.Entry<String, Integer> moodCount : moodCounts.entrySet()) {
                if (moodCount.getValue() > maxCount) {
                    maxCount = moodCount.getValue();
                    mostCommon = moodCount.getKey();
                }
            }
            
            // Display emoji for most common mood
            mostCommonMood.setText(getMoodEmoji(mostCommon));
        }
    }
    
    // Helper class for mood entries
    private static class MoodEntry {
        String mood;
        String dateTime;
        long timestamp;
        
        MoodEntry(String mood, String dateTime, long timestamp) {
            this.mood = mood;
            this.dateTime = dateTime;
            this.timestamp = timestamp;
        }
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Handle back button press
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
} 