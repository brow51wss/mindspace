package com.mindspace.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MeditationLibraryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MeditationCategoryAdapter adapter;
    private List<MeditationCategory> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meditation_library);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        setupMeditationCategories();
        adapter = new MeditationCategoryAdapter(categories);
        recyclerView.setAdapter(adapter);
        
        // Debug: Test if RecyclerView is receiving touches
        recyclerView.setOnTouchListener((v, event) -> {
            Log.d("MeditationLibrary", "RecyclerView touched at: " + event.getX() + ", " + event.getY());
            return false;
        });
    }

    private void setupMeditationCategories() {
        categories = new ArrayList<>();

        // Stress Relief Category
        List<MeditationSession> stressReliefSessions = new ArrayList<>();
        stressReliefSessions.add(new MeditationSession("Quick Calm", "5 minutes", "Beginner", 
            "A rapid stress-relief technique perfect for busy students. Uses progressive muscle relaxation and breathing to quickly reduce tension and anxiety."));
        stressReliefSessions.add(new MeditationSession("Deep Relaxation", "15 minutes", "Intermediate", 
            "Comprehensive body scan and breathing meditation designed to release deep-seated stress and promote full-body relaxation."));
        stressReliefSessions.add(new MeditationSession("Tension Release", "10 minutes", "Beginner", 
            "Targeted meditation focusing on common stress points like shoulders, jaw, and mind. Perfect after long study sessions."));
        categories.add(new MeditationCategory("😌 Stress Relief", "#FF6B6B", stressReliefSessions));

        // Focus & Concentration Category
        List<MeditationSession> focusSessions = new ArrayList<>();
        focusSessions.add(new MeditationSession("Study Focus", "12 minutes", "Intermediate", 
            "Enhance concentration and mental clarity before studying. Uses mindfulness techniques to improve attention span and reduce distractions."));
        focusSessions.add(new MeditationSession("Mind Clarity", "8 minutes", "Beginner", 
            "Clear mental fog and improve decision-making. Perfect before exams or important presentations to sharpen cognitive function."));
        focusSessions.add(new MeditationSession("Attention Training", "20 minutes", "Advanced", 
            "Advanced concentration practice using single-point focus techniques. Builds sustained attention and mental discipline."));
        categories.add(new MeditationCategory("🧠 Focus & Concentration", "#4ECDC4", focusSessions));

        // Sleep & Rest Category
        List<MeditationSession> sleepSessions = new ArrayList<>();
        sleepSessions.add(new MeditationSession("Bedtime Calm", "15 minutes", "Beginner", 
            "Gentle guided meditation designed to quiet racing thoughts and prepare your mind and body for restful sleep."));
        sleepSessions.add(new MeditationSession("Sleep Preparation", "10 minutes", "Beginner", 
            "Progressive relaxation technique that helps transition from daily stress to peaceful sleep. Perfect for students with busy minds."));
        categories.add(new MeditationCategory("😴 Sleep & Rest", "#9B59B6", sleepSessions));

        // Confidence Building Category
        List<MeditationSession> confidenceSessions = new ArrayList<>();
        confidenceSessions.add(new MeditationSession("Self-Esteem Boost", "12 minutes", "Intermediate", 
            "Positive affirmation meditation combined with visualization techniques to build self-confidence and reduce self-doubt."));
        confidenceSessions.add(new MeditationSession("Inner Strength", "18 minutes", "Intermediate", 
            "Develop resilience and inner confidence through mindfulness and self-compassion practices. Perfect for overcoming challenges."));
        categories.add(new MeditationCategory("💪 Confidence Building", "#F39C12", confidenceSessions));

        // Quick Energy Category
        List<MeditationSession> energySessions = new ArrayList<>();
        energySessions.add(new MeditationSession("5-Min Reset", "5 minutes", "Beginner", 
            "Quick energy boost using breathing techniques and gentle movement. Perfect between classes or during study breaks."));
        energySessions.add(new MeditationSession("Morning Boost", "8 minutes", "Beginner", 
            "Energizing morning meditation to start your day with clarity, positivity, and focused intention."));
        categories.add(new MeditationCategory("⚡ Quick Energy", "#2ECC71", energySessions));
    }

    // Meditation Category Class
    public static class MeditationCategory {
        public String name;
        public String color;
        public List<MeditationSession> sessions;
        public boolean isExpanded;

        public MeditationCategory(String name, String color, List<MeditationSession> sessions) {
            this.name = name;
            this.color = color;
            this.sessions = sessions;
            this.isExpanded = false;
        }
    }

    // Meditation Session Class
    public static class MeditationSession {
        public String title;
        public String duration;
        public String level;
        public String description;

        public MeditationSession(String title, String duration, String level, String description) {
            this.title = title;
            this.duration = duration;
            this.level = level;
            this.description = description;
        }
    }

    // Adapter for Categories
    public class MeditationCategoryAdapter extends RecyclerView.Adapter<MeditationCategoryAdapter.CategoryViewHolder> {
        private List<MeditationCategory> categories;

        public MeditationCategoryAdapter(List<MeditationCategory> categories) {
            this.categories = categories;
        }

        @Override
        public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_meditation_category, parent, false);
            return new CategoryViewHolder(view);
        }

        @Override
        public void onBindViewHolder(CategoryViewHolder holder, int position) {
            MeditationCategory category = categories.get(position);
            holder.bind(category);
        }

        @Override
        public int getItemCount() {
            return categories.size();
        }

        public class CategoryViewHolder extends RecyclerView.ViewHolder {
            TextView categoryName;
            ImageView expandIcon;
            LinearLayout sessionsContainer;
            LinearLayout categoryHeader;

            public CategoryViewHolder(View itemView) {
                super(itemView);
                categoryName = itemView.findViewById(R.id.categoryName);
                expandIcon = itemView.findViewById(R.id.expandIcon);
                sessionsContainer = itemView.findViewById(R.id.sessionsContainer);
                categoryHeader = itemView.findViewById(R.id.categoryHeader);
            }

            public void bind(MeditationCategory category) {
                Log.d("MeditationLibrary", "Binding category: " + category.name + ", expanded: " + category.isExpanded);
                categoryName.setText(category.name);
                
                // Set expand/collapse icon
                expandIcon.setImageResource(category.isExpanded ? 
                    android.R.drawable.arrow_up_float : android.R.drawable.arrow_down_float);

                // Handle category click - use the header instead of itemView
                categoryHeader.setOnClickListener(v -> {
                    Log.d("MeditationLibrary", "Category header clicked: " + category.name);
                    category.isExpanded = !category.isExpanded;
                    Log.d("MeditationLibrary", "New expanded state: " + category.isExpanded);
                    notifyItemChanged(getAdapterPosition());
                });

                // Always update sessions container based on expanded state
                if (category.isExpanded) {
                    Log.d("MeditationLibrary", "Showing sessions for: " + category.name);
                    showSessions(category);
                } else {
                    Log.d("MeditationLibrary", "Hiding sessions for: " + category.name);
                    sessionsContainer.setVisibility(View.GONE);
                    sessionsContainer.removeAllViews();
                }
            }

            private void showSessions(MeditationCategory category) {
                sessionsContainer.removeAllViews();
                sessionsContainer.setVisibility(View.VISIBLE);

                for (MeditationSession session : category.sessions) {
                    View sessionView = LayoutInflater.from(itemView.getContext())
                            .inflate(R.layout.item_meditation_session, sessionsContainer, false);
                    
                    TextView sessionTitle = sessionView.findViewById(R.id.sessionTitle);
                    TextView sessionDuration = sessionView.findViewById(R.id.sessionDuration);
                    TextView sessionLevel = sessionView.findViewById(R.id.sessionLevel);
                    TextView sessionDescription = sessionView.findViewById(R.id.sessionDescription);

                    sessionTitle.setText(session.title);
                    sessionDuration.setText(session.duration);
                    sessionLevel.setText(session.level);
                    sessionDescription.setText(session.description);

                    // Handle session click
                    sessionView.setOnClickListener(v -> {
                        Intent intent = new Intent(MeditationLibraryActivity.this, MeditationPlayerActivity.class);
                        intent.putExtra("session_title", session.title);
                        intent.putExtra("session_duration", session.duration);
                        intent.putExtra("session_level", session.level);
                        intent.putExtra("session_description", session.description);
                        startActivity(intent);
                    });

                    sessionsContainer.addView(sessionView);
                }
            }


        }
    }
} 