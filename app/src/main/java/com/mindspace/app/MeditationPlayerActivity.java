package com.mindspace.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MeditationPlayerActivity extends AppCompatActivity {

    private TextView sessionTitle, sessionDescription, timerText, statusText;
    private Button playPauseButton, stopButton;
    private ProgressBar progressBar;
    
    private CountDownTimer meditationTimer;
    private boolean isPlaying = false;
    private boolean isPaused = false;
    private long totalTimeInMillis;
    private long remainingTimeInMillis;
    private StreakTracker streakTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meditation_player);

        // Initialize streak tracker
        streakTracker = new StreakTracker(this);
        
        initializeViews();
        setupSessionData();
        setupButtonListeners();
    }

    private void initializeViews() {
        sessionTitle = findViewById(R.id.sessionTitle);
        sessionDescription = findViewById(R.id.sessionDescription);
        timerText = findViewById(R.id.timerText);
        statusText = findViewById(R.id.statusText);
        playPauseButton = findViewById(R.id.playPauseButton);
        stopButton = findViewById(R.id.stopButton);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupSessionData() {
        String title = getIntent().getStringExtra("session_title");
        String duration = getIntent().getStringExtra("session_duration");
        String level = getIntent().getStringExtra("session_level");
        String description = getIntent().getStringExtra("session_description");

        sessionTitle.setText(title);
        sessionDescription.setText(description);
        
        // Parse duration and set timer
        totalTimeInMillis = parseDurationToMillis(duration);
        remainingTimeInMillis = totalTimeInMillis;
        
        updateTimerDisplay();
        progressBar.setMax(100);
        progressBar.setProgress(0);
        
        statusText.setText("Ready to begin • " + level + " level");
    }

    private long parseDurationToMillis(String duration) {
        // Extract number from duration string (e.g., "5 minutes" -> 5)
        String[] parts = duration.split(" ");
        int minutes = Integer.parseInt(parts[0]);
        return minutes * 60 * 1000; // Convert to milliseconds
    }

    private void setupButtonListeners() {
        playPauseButton.setOnClickListener(v -> {
            if (!isPlaying && !isPaused) {
                startMeditation();
            } else if (isPlaying) {
                pauseMeditation();
            } else if (isPaused) {
                resumeMeditation();
            }
        });

        stopButton.setOnClickListener(v -> stopMeditation());
    }

    private void startMeditation() {
        isPlaying = true;
        isPaused = false;
        playPauseButton.setText("⏸ Pause");
        statusText.setText("🧘‍♀️ Meditating... Find your center");
        
        meditationTimer = new CountDownTimer(remainingTimeInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                remainingTimeInMillis = millisUntilFinished;
                updateTimerDisplay();
                updateProgressBar();
            }

            @Override
            public void onFinish() {
                completeMeditation();
            }
        }.start();
    }

    private void pauseMeditation() {
        if (meditationTimer != null) {
            meditationTimer.cancel();
        }
        isPlaying = false;
        isPaused = true;
        playPauseButton.setText("▶ Resume");
        statusText.setText("⏸ Paused • Take your time");
    }

    private void resumeMeditation() {
        startMeditation(); // This will use the remaining time
    }

    private void stopMeditation() {
        if (meditationTimer != null) {
            meditationTimer.cancel();
        }
        isPlaying = false;
        isPaused = false;
        remainingTimeInMillis = totalTimeInMillis;
        
        playPauseButton.setText("▶ Start");
        statusText.setText("Ready to begin");
        updateTimerDisplay();
        progressBar.setProgress(0);
    }

    private void completeMeditation() {
        isPlaying = false;
        isPaused = false;
        playPauseButton.setText("✅ Complete");
        playPauseButton.setEnabled(false);
        statusText.setText("🎉 Session completed! Well done!");
        progressBar.setProgress(100);
        timerText.setText("00:00");
        
        // Track meditation completion for achievements
        trackMeditationCompletion();
        
        // Re-enable start button after 3 seconds
        timerText.postDelayed(() -> {
            playPauseButton.setText("▶ Start Again");
            playPauseButton.setEnabled(true);
            remainingTimeInMillis = totalTimeInMillis;
            updateTimerDisplay();
            progressBar.setProgress(0);
            statusText.setText("Ready to begin again");
        }, 3000);
    }
    
    private void trackMeditationCompletion() {
        SharedPreferences meditationPrefs = getSharedPreferences("MindSpaceMeditation", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = meditationPrefs.edit();
        
        // Update meditation stats
        int totalSessions = meditationPrefs.getInt("total_sessions", 0) + 1;
        editor.putInt("total_sessions", totalSessions);
        editor.putLong("last_session_time", System.currentTimeMillis());
        editor.apply();
        
        // Update streak tracking with new system
        StreakTracker.StreakResult streakResult = streakTracker.updateStreak(StreakTracker.StreakType.MEDITATION);
        
        Log.d("MeditationPlayer", "Meditation completed (Total sessions: " + totalSessions + ")");
        
        // Show streak milestone message if reached
        if (streakResult.milestoneReached > 0 || streakResult.isNewBest) {
            String streakMessage = streakTracker.getMotivationalMessage(streakResult);
            showStreakToast(streakMessage);
        } else if (streakResult.isStreakIncreased) {
            String streakMessage = streakTracker.getMotivationalMessage(streakResult);
            showStreakToast(streakMessage);
        }
    }
    
    private void showStreakToast(String message) {
        android.os.Handler handler = new android.os.Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                android.widget.Toast.makeText(MeditationPlayerActivity.this, message, android.widget.Toast.LENGTH_LONG).show();
            }
        }, 1500);
    }

    private void updateTimerDisplay() {
        long minutes = remainingTimeInMillis / 60000;
        long seconds = (remainingTimeInMillis % 60000) / 1000;
        timerText.setText(String.format("%02d:%02d", minutes, seconds));
    }

    private void updateProgressBar() {
        long elapsedTime = totalTimeInMillis - remainingTimeInMillis;
        int progress = (int) ((elapsedTime * 100) / totalTimeInMillis);
        progressBar.setProgress(progress);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (meditationTimer != null) {
            meditationTimer.cancel();
        }
    }
} 