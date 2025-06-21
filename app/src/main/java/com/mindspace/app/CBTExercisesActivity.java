package com.mindspace.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.ImageButton;
import java.util.ArrayList;
import java.util.List;

public class CBTExercisesActivity extends AppCompatActivity {
    private static final String TAG = "CBTExercises";
    private RecyclerView exercisesRecyclerView;
    private CBTExerciseAdapter adapter;
    private List<CBTExercise> exercises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cbt_exercises);

        Log.d(TAG, "CBT Exercises Activity started");

        initializeViews();
        setupExercises();
        setupRecyclerView();
    }

    private void initializeViews() {
        exercisesRecyclerView = findViewById(R.id.exercisesRecyclerView);
        
        // Back button
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            Log.d(TAG, "Back button clicked");
            finish();
        });
    }

    private void setupExercises() {
        exercises = new ArrayList<>();
        
        // Exercise 1: Thought Challenging
        exercises.add(new CBTExercise(
            "thought_challenging",
            "🔄 Thought Challenging",
            "Challenge negative thoughts with evidence-based questions",
            "Learn to identify and question automatic negative thoughts that may be unrealistic or unhelpful.",
            "#4A90E2", // Blue
            "10-15 min"
        ));

        // Exercise 2: Mood-Thought Connection
        exercises.add(new CBTExercise(
            "mood_thought_connection",
            "📝 Mood-Thought Connection",
            "Explore the link between your feelings and thoughts",
            "Understand how your thoughts influence your emotions and discover patterns in your thinking.",
            "#7B68EE", // Medium Slate Blue
            "8-12 min"
        ));

        // Exercise 3: Evidence Examination
        exercises.add(new CBTExercise(
            "evidence_examination",
            "⚖️ Evidence Examination",
            "Objectively evaluate your thoughts with facts",
            "Learn to separate facts from opinions and examine evidence for and against your thoughts.",
            "#20B2AA", // Light Sea Green
            "12-18 min"
        ));

        // Exercise 4: Positive Reframing
        exercises.add(new CBTExercise(
            "positive_reframing",
            "🌟 Positive Reframing",
            "Transform negative self-talk into balanced thinking",
            "Practice reframing negative thoughts into more balanced, realistic, and helpful perspectives.",
            "#FF6B6B", // Coral
            "15-20 min"
        ));

        Log.d(TAG, "Loaded " + exercises.size() + " CBT exercises");
    }

    private void setupRecyclerView() {
        adapter = new CBTExerciseAdapter(exercises, this::onExerciseClick);
        exercisesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        exercisesRecyclerView.setAdapter(adapter);
    }

    private void onExerciseClick(CBTExercise exercise) {
        Log.d(TAG, "Exercise clicked: " + exercise.getTitle());
        
        Intent intent = new Intent(this, CBTExerciseActivity.class);
        intent.putExtra("exercise_id", exercise.getId());
        intent.putExtra("exercise_title", exercise.getTitle());
        intent.putExtra("exercise_description", exercise.getDescription());
        intent.putExtra("exercise_details", exercise.getDetails());
        intent.putExtra("exercise_color", exercise.getColor());
        intent.putExtra("exercise_duration", exercise.getDuration());
        startActivity(intent);
    }

    // CBT Exercise data class
    public static class CBTExercise {
        private String id;
        private String title;
        private String description;
        private String details;
        private String color;
        private String duration;

        public CBTExercise(String id, String title, String description, String details, String color, String duration) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.details = details;
            this.color = color;
            this.duration = duration;
        }

        // Getters
        public String getId() { return id; }
        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public String getDetails() { return details; }
        public String getColor() { return color; }
        public String getDuration() { return duration; }
    }
} 