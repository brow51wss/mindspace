package com.mindspace.app;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.ArrayAdapter;
import java.util.ArrayList;
import java.util.List;

public class MoodHistoryActivity extends AppCompatActivity {

    private ListView historyListView;
    private LinearLayout emptyStateText;
    private ArrayAdapter<String> historyAdapter;
    private List<String> moodHistory;

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
        
        moodHistory = new ArrayList<>();
        historyAdapter = new ArrayAdapter<>(this, 
            android.R.layout.simple_list_item_1, moodHistory);
        historyListView.setAdapter(historyAdapter);
    }
    
    private void loadMoodHistory() {
        // TODO: Load from database
        // For now, add some sample data to demonstrate
        moodHistory.add("📅 Today - 😊 Happy");
        moodHistory.add("📅 Yesterday - 😌 Calm");
        moodHistory.add("📅 June 19 - 😰 Stressed");
        moodHistory.add("📅 June 18 - 😊 Happy");
        moodHistory.add("📅 June 17 - 😴 Tired");
        
        historyAdapter.notifyDataSetChanged();
        
        // Hide empty state since we have data
        if (moodHistory.size() > 0) {
            emptyStateText.setVisibility(android.view.View.GONE);
            historyListView.setVisibility(android.view.View.VISIBLE);
        } else {
            emptyStateText.setVisibility(android.view.View.VISIBLE);
            historyListView.setVisibility(android.view.View.GONE);
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