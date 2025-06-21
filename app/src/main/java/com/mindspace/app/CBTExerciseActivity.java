package com.mindspace.app;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import java.util.ArrayList;
import java.util.List;

public class CBTExerciseActivity extends AppCompatActivity {
    private static final String TAG = "CBTExercise";
    
    // UI Components
    private TextView titleText, stepText, instructionText, progressText;
    private ProgressBar progressBar;
    private LinearLayout inputContainer;
    private Button nextButton, previousButton, finishButton;
    private CardView mainCard;
    
    // Exercise Data
    private String exerciseId, exerciseTitle, exerciseColor;
    private List<ExerciseStep> steps;
    private List<String> userResponses;
    private int currentStep = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cbt_exercise);

        Log.d(TAG, "CBT Exercise Activity started");

        initializeViews();
        getExerciseData();
        setupExercise();
        setupNavigation();
        showCurrentStep();
    }

    private void initializeViews() {
        titleText = findViewById(R.id.exerciseTitle);
        stepText = findViewById(R.id.stepText);
        instructionText = findViewById(R.id.instructionText);
        progressText = findViewById(R.id.progressText);
        progressBar = findViewById(R.id.progressBar);
        inputContainer = findViewById(R.id.inputContainer);
        nextButton = findViewById(R.id.nextButton);
        previousButton = findViewById(R.id.previousButton);
        finishButton = findViewById(R.id.finishButton);
        mainCard = findViewById(R.id.mainCard);
        
        // Back button
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            Log.d(TAG, "Back button clicked");
            finish();
        });
    }

    private void getExerciseData() {
        exerciseId = getIntent().getStringExtra("exercise_id");
        exerciseTitle = getIntent().getStringExtra("exercise_title");
        exerciseColor = getIntent().getStringExtra("exercise_color");
        
        Log.d(TAG, "Exercise data - ID: " + exerciseId + ", Title: " + exerciseTitle);
    }

    private void setupExercise() {
        titleText.setText(exerciseTitle);
        
        // Set theme color
        try {
            int color = Color.parseColor(exerciseColor);
            mainCard.setCardBackgroundColor(Color.argb(20, Color.red(color), Color.green(color), Color.blue(color)));
        } catch (Exception e) {
            Log.w(TAG, "Failed to parse color: " + exerciseColor);
        }
        
        // Initialize steps based on exercise type
        steps = createExerciseSteps(exerciseId);
        userResponses = new ArrayList<>();
        for (int i = 0; i < steps.size(); i++) {
            userResponses.add("");
        }
        
        progressBar.setMax(steps.size());
        Log.d(TAG, "Exercise setup complete with " + steps.size() + " steps");
    }

    private List<ExerciseStep> createExerciseSteps(String exerciseId) {
        List<ExerciseStep> exerciseSteps = new ArrayList<>();
        
        switch (exerciseId) {
            case "thought_challenging":
                exerciseSteps.add(new ExerciseStep(
                    "Identify the Thought",
                    "Think about a negative or worrying thought you've had recently. Write it down exactly as it appeared in your mind.",
                    "What negative thought have you been having?",
                    "text"
                ));
                exerciseSteps.add(new ExerciseStep(
                    "Rate the Intensity",
                    "On a scale of 1-10, how much do you believe this thought right now? (1 = don't believe it at all, 10 = completely believe it)",
                    "Rate your belief in this thought (1-10):",
                    "number"
                ));
                exerciseSteps.add(new ExerciseStep(
                    "Challenge the Thought",
                    "Ask yourself: Is this thought realistic? What evidence do I have for and against it?",
                    "What evidence supports this thought? What evidence contradicts it?",
                    "text"
                ));
                exerciseSteps.add(new ExerciseStep(
                    "Create a Balanced Thought",
                    "Based on the evidence, write a more balanced and realistic version of your original thought.",
                    "Write a more balanced version of your thought:",
                    "text"
                ));
                exerciseSteps.add(new ExerciseStep(
                    "Re-rate the Intensity",
                    "Now rate how much you believe the original thought (1-10). Has it changed?",
                    "Rate your belief in the original thought now (1-10):",
                    "number"
                ));
                break;
                
            case "mood_thought_connection":
                exerciseSteps.add(new ExerciseStep(
                    "Identify Your Current Mood",
                    "Take a moment to notice how you're feeling right now. Name the emotion you're experiencing.",
                    "What emotion are you feeling right now?",
                    "text"
                ));
                exerciseSteps.add(new ExerciseStep(
                    "Rate the Intensity",
                    "How strong is this emotion on a scale of 1-10? (1 = very mild, 10 = extremely intense)",
                    "Rate the intensity of this emotion (1-10):",
                    "number"
                ));
                exerciseSteps.add(new ExerciseStep(
                    "Identify the Trigger",
                    "What situation or event happened just before you started feeling this way?",
                    "What triggered this emotion?",
                    "text"
                ));
                exerciseSteps.add(new ExerciseStep(
                    "Explore Your Thoughts",
                    "What thoughts went through your mind when the triggering event happened?",
                    "What thoughts did you have about the situation?",
                    "text"
                ));
                exerciseSteps.add(new ExerciseStep(
                    "Make the Connection",
                    "Reflect on how your thoughts about the situation influenced your emotional response.",
                    "How do you think your thoughts influenced your emotions?",
                    "text"
                ));
                break;
                
            case "evidence_examination":
                exerciseSteps.add(new ExerciseStep(
                    "State Your Thought",
                    "Write down a specific thought or belief that's been bothering you.",
                    "What thought or belief would you like to examine?",
                    "text"
                ));
                exerciseSteps.add(new ExerciseStep(
                    "Evidence For",
                    "List all the evidence that supports this thought. What facts make this thought seem true?",
                    "What evidence supports this thought?",
                    "text"
                ));
                exerciseSteps.add(new ExerciseStep(
                    "Evidence Against",
                    "Now list all the evidence that contradicts this thought. What facts suggest it might not be true?",
                    "What evidence contradicts this thought?",
                    "text"
                ));
                exerciseSteps.add(new ExerciseStep(
                    "Alternative Perspectives",
                    "What would you tell a good friend who had this same thought? What other ways could you look at this situation?",
                    "What alternative perspectives are possible?",
                    "text"
                ));
                exerciseSteps.add(new ExerciseStep(
                    "Balanced Conclusion",
                    "Based on all the evidence, what's a more balanced and realistic conclusion?",
                    "What's a more balanced way to think about this?",
                    "text"
                ));
                break;
                
            case "positive_reframing":
                exerciseSteps.add(new ExerciseStep(
                    "Identify Negative Self-Talk",
                    "Think about something you've been telling yourself that's negative or self-critical.",
                    "What negative thing have you been telling yourself?",
                    "text"
                ));
                exerciseSteps.add(new ExerciseStep(
                    "Recognize the Impact",
                    "How does this negative self-talk make you feel? What emotions does it create?",
                    "How does this self-talk make you feel?",
                    "text"
                ));
                exerciseSteps.add(new ExerciseStep(
                    "Find the Learning",
                    "What can you learn from this situation? Is there a growth opportunity hidden here?",
                    "What can you learn from this situation?",
                    "text"
                ));
                exerciseSteps.add(new ExerciseStep(
                    "Reframe Positively",
                    "Rewrite your negative self-talk in a more compassionate, realistic, and encouraging way.",
                    "How can you reframe this more positively?",
                    "text"
                ));
                exerciseSteps.add(new ExerciseStep(
                    "Practice the New Thought",
                    "How will you remind yourself to use this new, positive thought instead of the old negative one?",
                    "How will you practice this new way of thinking?",
                    "text"
                ));
                break;
        }
        
        return exerciseSteps;
    }

    private void setupNavigation() {
        nextButton.setOnClickListener(v -> {
            if (saveCurrentResponse()) {
                if (currentStep < steps.size() - 1) {
                    currentStep++;
                    showCurrentStep();
                } else {
                    showResults();
                }
            }
        });

        previousButton.setOnClickListener(v -> {
            if (currentStep > 0) {
                saveCurrentResponse();
                currentStep--;
                showCurrentStep();
            }
        });

        finishButton.setOnClickListener(v -> {
            Log.d(TAG, "Exercise completed");
            Toast.makeText(this, "Great work! You've completed the exercise.", Toast.LENGTH_LONG).show();
            finish();
        });
    }

    private void showCurrentStep() {
        ExerciseStep step = steps.get(currentStep);
        
        stepText.setText("Step " + (currentStep + 1) + " of " + steps.size());
        instructionText.setText(step.getInstruction());
        progressText.setText(step.getTitle());
        progressBar.setProgress(currentStep + 1);
        
        // Clear and setup input
        inputContainer.removeAllViews();
        createInputField(step.getInputType(), userResponses.get(currentStep));
        
        // Update navigation buttons
        previousButton.setVisibility(currentStep > 0 ? View.VISIBLE : View.GONE);
        nextButton.setVisibility(currentStep < steps.size() - 1 ? View.VISIBLE : View.GONE);
        finishButton.setVisibility(currentStep == steps.size() - 1 ? View.VISIBLE : View.GONE);
        
        Log.d(TAG, "Showing step " + (currentStep + 1) + ": " + step.getTitle());
    }

    private void createInputField(String inputType, String savedResponse) {
        if ("text".equals(inputType)) {
            EditText editText = new EditText(this);
            editText.setId(View.generateViewId());
            editText.setTag("userInput");
            editText.setHint("Type your response here...");
            editText.setText(savedResponse);
            editText.setMinLines(3);
            editText.setMaxLines(6);
            editText.setPadding(16, 16, 16, 16);
            inputContainer.addView(editText);
        } else if ("number".equals(inputType)) {
            EditText editText = new EditText(this);
            editText.setId(View.generateViewId());
            editText.setTag("userInput");
            editText.setHint("Enter a number (1-10)");
            editText.setText(savedResponse);
            editText.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
            editText.setPadding(16, 16, 16, 16);
            inputContainer.addView(editText);
        }
    }

    private boolean saveCurrentResponse() {
        EditText input = inputContainer.findViewWithTag("userInput");
        if (input != null) {
            String response = input.getText().toString().trim();
            if (response.isEmpty()) {
                Toast.makeText(this, "Please enter a response before continuing.", Toast.LENGTH_SHORT).show();
                return false;
            }
            userResponses.set(currentStep, response);
            Log.d(TAG, "Saved response for step " + (currentStep + 1) + ": " + response);
        }
        return true;
    }

    private void showResults() {
        Log.d(TAG, "Showing exercise results");
        
        // Hide progress and navigation
        progressBar.setVisibility(View.GONE);
        progressText.setVisibility(View.GONE);
        nextButton.setVisibility(View.GONE);
        previousButton.setVisibility(View.GONE);
        
        // Show completion message
        stepText.setText("Exercise Complete!");
        instructionText.setText("Congratulations! You've completed the " + exerciseTitle + " exercise. " +
            "Take a moment to reflect on your responses and how you're feeling now.");
        
        // Clear input container and show summary
        inputContainer.removeAllViews();
        
        TextView summaryText = new TextView(this);
        summaryText.setText("💡 Remember: CBT exercises work best when practiced regularly. " +
            "Consider revisiting this exercise when you encounter similar thoughts or situations.");
        summaryText.setPadding(16, 16, 16, 16);
        summaryText.setTextSize(14);
        inputContainer.addView(summaryText);
        
        finishButton.setVisibility(View.VISIBLE);
        finishButton.setText("Complete Exercise");
    }

    // Exercise Step data class
    private static class ExerciseStep {
        private String title;
        private String instruction;
        private String prompt;
        private String inputType;

        public ExerciseStep(String title, String instruction, String prompt, String inputType) {
            this.title = title;
            this.instruction = instruction;
            this.prompt = prompt;
            this.inputType = inputType;
        }

        public String getTitle() { return title; }
        public String getInstruction() { return instruction; }
        public String getPrompt() { return prompt; }
        public String getInputType() { return inputType; }
    }
} 