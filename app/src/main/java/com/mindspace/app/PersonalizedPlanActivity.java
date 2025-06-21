package com.mindspace.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * PersonalizedPlanActivity - Displays AI-generated personalized mental health plans
 * Shows weekly recommendations and insights based on user patterns
 */
public class PersonalizedPlanActivity extends AppCompatActivity {
    private static final String TAG = "PersonalizedPlan";
    
    private PlanGenerator planGenerator;
    private LinearLayout weeklyPlanContainer;
    private LinearLayout insightsContainer;
    private TextView headerTitle;
    private TextView headerSubtitle;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalized_plan);
        
        Log.d(TAG, "Personalized Plan Activity started");
        
        // Initialize views
        initializeViews();
        
        // Initialize plan generator
        planGenerator = new PlanGenerator(this);
        
        // Setup header
        setupHeader();
        
        // Generate and display personalized plan
        generateAndDisplayPlan();
        
        // Setup back button
        setupBackButton();
    }
    
    private void initializeViews() {
        weeklyPlanContainer = findViewById(R.id.weeklyPlanContainer);
        insightsContainer = findViewById(R.id.insightsContainer);
        headerTitle = findViewById(R.id.headerTitle);
        headerSubtitle = findViewById(R.id.headerSubtitle);
    }
    
    private void setupHeader() {
        headerTitle.setText("Your Personal Plan");
        
        // Get current week info
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat weekFormat = new SimpleDateFormat("MMMM d", Locale.getDefault());
        String currentWeek = weekFormat.format(cal.getTime());
        
        cal.add(Calendar.DAY_OF_YEAR, 6);
        String weekEnd = weekFormat.format(cal.getTime());
        
        headerSubtitle.setText("Week of " + currentWeek + " - " + weekEnd);
        
        Log.d(TAG, "Header setup complete");
    }
    
    private void generateAndDisplayPlan() {
        Log.d(TAG, "Generating personalized plan");
        
        // Generate weekly plan
        List<PlanGenerator.DailyRecommendation> weeklyPlan = planGenerator.generateWeeklyPlan();
        displayWeeklyPlan(weeklyPlan);
        
        // Generate weekly insights
        List<PlanGenerator.WeeklyInsight> insights = planGenerator.generateWeeklyInsights();
        displayWeeklyInsights(insights);
        
        Log.d(TAG, "Plan generation and display complete");
    }
    
    private void displayWeeklyPlan(List<PlanGenerator.DailyRecommendation> weeklyPlan) {
        weeklyPlanContainer.removeAllViews();
        
        for (PlanGenerator.DailyRecommendation recommendation : weeklyPlan) {
            View dayCard = createDayRecommendationCard(recommendation);
            weeklyPlanContainer.addView(dayCard);
        }
        
        Log.d(TAG, "Displayed " + weeklyPlan.size() + " daily recommendations");
    }
    
    private View createDayRecommendationCard(PlanGenerator.DailyRecommendation recommendation) {
        // Create main card
        CardView cardView = new CardView(this);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(0, 0, 0, dpToPx(16));
        cardView.setLayoutParams(cardParams);
        cardView.setCardElevation(dpToPx(4));
        cardView.setRadius(dpToPx(12));
        cardView.setUseCompatPadding(true);
        
        // Set card background color based on activity type
        int backgroundColor = getActivityTypeColor(recommendation.activityType);
        cardView.setCardBackgroundColor(backgroundColor);
        
        // Create content layout
        LinearLayout contentLayout = new LinearLayout(this);
        contentLayout.setOrientation(LinearLayout.VERTICAL);
        contentLayout.setPadding(dpToPx(20), dpToPx(16), dpToPx(20), dpToPx(16));
        
        // Day header
        LinearLayout dayHeader = new LinearLayout(this);
        dayHeader.setOrientation(LinearLayout.HORIZONTAL);
        dayHeader.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        
        // Day name
        TextView dayName = new TextView(this);
        dayName.setText(recommendation.day);
        dayName.setTextSize(18);
        dayName.setTextColor(getResources().getColor(android.R.color.white));
        dayName.setTypeface(null, android.graphics.Typeface.BOLD);
        
        // Time estimate
        TextView timeEstimate = new TextView(this);
        timeEstimate.setText(recommendation.estimatedMinutes + " min");
        timeEstimate.setTextSize(14);
        timeEstimate.setTextColor(getResources().getColor(android.R.color.white));
        timeEstimate.setAlpha(0.9f);
        LinearLayout.LayoutParams timeParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        timeParams.weight = 1;
        timeParams.gravity = android.view.Gravity.END;
        timeEstimate.setLayoutParams(timeParams);
        timeEstimate.setGravity(android.view.Gravity.END);
        
        dayHeader.addView(dayName);
        dayHeader.addView(timeEstimate);
        
        // Activity title
        TextView activityTitle = new TextView(this);
        activityTitle.setText(recommendation.primaryActivity);
        activityTitle.setTextSize(16);
        activityTitle.setTextColor(getResources().getColor(android.R.color.white));
        activityTitle.setTypeface(null, android.graphics.Typeface.BOLD);
        LinearLayout.LayoutParams activityParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        activityParams.setMargins(0, dpToPx(8), 0, 0);
        activityTitle.setLayoutParams(activityParams);
        
        // Reason
        TextView reason = new TextView(this);
        reason.setText(recommendation.reason);
        reason.setTextSize(14);
        reason.setTextColor(getResources().getColor(android.R.color.white));
        reason.setAlpha(0.9f);
        LinearLayout.LayoutParams reasonParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        reasonParams.setMargins(0, dpToPx(4), 0, 0);
        reason.setLayoutParams(reasonParams);
        
        // Motivational message
        TextView motivation = new TextView(this);
        motivation.setText(recommendation.motivationalMessage);
        motivation.setTextSize(14);
        motivation.setTextColor(getResources().getColor(android.R.color.white));
        motivation.setTypeface(null, android.graphics.Typeface.ITALIC);
        motivation.setAlpha(0.9f);
        LinearLayout.LayoutParams motivationParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        motivationParams.setMargins(0, dpToPx(8), 0, 0);
        motivation.setLayoutParams(motivationParams);
        
        // Add all views to content layout
        contentLayout.addView(dayHeader);
        contentLayout.addView(activityTitle);
        contentLayout.addView(reason);
        contentLayout.addView(motivation);
        
        // Add click listener to start the recommended activity
        cardView.setOnClickListener(v -> startRecommendedActivity(recommendation));
        
        cardView.addView(contentLayout);
        return cardView;
    }
    
    private void displayWeeklyInsights(List<PlanGenerator.WeeklyInsight> insights) {
        insightsContainer.removeAllViews();
        
        for (PlanGenerator.WeeklyInsight insight : insights) {
            View insightCard = createInsightCard(insight);
            insightsContainer.addView(insightCard);
        }
        
        Log.d(TAG, "Displayed " + insights.size() + " weekly insights");
    }
    
    private View createInsightCard(PlanGenerator.WeeklyInsight insight) {
        // Create main card
        CardView cardView = new CardView(this);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(0, 0, 0, dpToPx(12));
        cardView.setLayoutParams(cardParams);
        cardView.setCardElevation(dpToPx(3));
        cardView.setRadius(dpToPx(12));
        cardView.setUseCompatPadding(true);
        cardView.setCardBackgroundColor(getResources().getColor(R.color.insight_card_background));
        
        // Create content layout
        LinearLayout contentLayout = new LinearLayout(this);
        contentLayout.setOrientation(LinearLayout.VERTICAL);
        contentLayout.setPadding(dpToPx(20), dpToPx(16), dpToPx(20), dpToPx(16));
        
        // Insight title
        TextView title = new TextView(this);
        title.setText(insight.title);
        title.setTextSize(16);
        title.setTextColor(getResources().getColor(R.color.insight_title_color));
        title.setTypeface(null, android.graphics.Typeface.BOLD);
        
        // Insight description
        TextView description = new TextView(this);
        description.setText(insight.description);
        description.setTextSize(14);
        description.setTextColor(getResources().getColor(R.color.insight_text_color));
        LinearLayout.LayoutParams descParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        descParams.setMargins(0, dpToPx(6), 0, 0);
        description.setLayoutParams(descParams);
        
        // Action suggestion
        TextView actionSuggestion = new TextView(this);
        actionSuggestion.setText("💡 " + insight.actionSuggestion);
        actionSuggestion.setTextSize(14);
        actionSuggestion.setTextColor(getResources().getColor(R.color.insight_action_color));
        actionSuggestion.setTypeface(null, android.graphics.Typeface.ITALIC);
        LinearLayout.LayoutParams actionParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        actionParams.setMargins(0, dpToPx(8), 0, 0);
        actionSuggestion.setLayoutParams(actionParams);
        
        // Add all views to content layout
        contentLayout.addView(title);
        contentLayout.addView(description);
        contentLayout.addView(actionSuggestion);
        
        cardView.addView(contentLayout);
        return cardView;
    }
    
    private int getActivityTypeColor(String activityType) {
        switch (activityType) {
            case "meditation":
                return getResources().getColor(R.color.meditation_purple);
            case "cbt":
                return getResources().getColor(R.color.cbt_primary);
            case "mixed":
                return getResources().getColor(R.color.plan_mixed_color);
            default:
                return getResources().getColor(R.color.plan_default_color);
        }
    }
    
    private void startRecommendedActivity(PlanGenerator.DailyRecommendation recommendation) {
        Log.d(TAG, "Starting recommended activity: " + recommendation.primaryActivity);
        
        Intent intent = null;
        
        if (recommendation.activityType.equals("meditation")) {
            // Start meditation library
            intent = new Intent(this, MeditationLibraryActivity.class);
        } else if (recommendation.activityType.equals("cbt")) {
            // Start CBT exercises
            intent = new Intent(this, CBTExercisesActivity.class);
        } else {
            // Default to main activity
            intent = new Intent(this, MainActivity.class);
        }
        
        if (intent != null) {
            startActivity(intent);
        }
    }
    
    private void setupBackButton() {
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            Log.d(TAG, "Back button clicked");
            finish();
        });
    }
    
    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Refresh plan when returning to activity
        generateAndDisplayPlan();
    }
} 