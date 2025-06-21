package com.mindspace.app;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

/**
 * SplashActivity - Beautiful branded splash screen with smooth animations
 * Shows MindSpace logo with fade-in animation, then transitions to MainActivity
 */
public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";
    private static final int SPLASH_DURATION = 2500; // 2.5 seconds
    private static final int LOGO_ANIMATION_DURATION = 800; // Logo fade-in duration
    private static final int TEXT_ANIMATION_DELAY = 400; // Delay before text appears
    private static final int TEXT_ANIMATION_DURATION = 600; // Text fade-in duration
    
    private ImageView logoImageView;
    private TextView appNameText;
    private TextView taglineText;
    private View backgroundView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        
        Log.d(TAG, "Splash screen started");
        
        // Initialize views
        initializeViews();
        
        // Start animations
        startSplashAnimations();
        
        // Schedule transition to MainActivity
        scheduleMainActivityTransition();
    }
    
    private void initializeViews() {
        logoImageView = findViewById(R.id.logoImageView);
        appNameText = findViewById(R.id.appNameText);
        taglineText = findViewById(R.id.taglineText);
        backgroundView = findViewById(R.id.backgroundView);
        
        // Set initial visibility - everything starts invisible
        logoImageView.setAlpha(0f);
        appNameText.setAlpha(0f);
        taglineText.setAlpha(0f);
        
        // Set initial scale for logo (slightly smaller)
        logoImageView.setScaleX(0.8f);
        logoImageView.setScaleY(0.8f);
        
        Log.d(TAG, "Views initialized");
    }
    
    private void startSplashAnimations() {
        // Animate logo fade-in and scale
        animateLogo();
        
        // Animate app name text after delay
        animateAppName();
        
        // Animate tagline text after additional delay
        animateTagline();
    }
    
    private void animateLogo() {
        // Logo fade-in animation
        ObjectAnimator logoFadeIn = ObjectAnimator.ofFloat(logoImageView, "alpha", 0f, 1f);
        logoFadeIn.setDuration(LOGO_ANIMATION_DURATION);
        logoFadeIn.setInterpolator(new AccelerateDecelerateInterpolator());
        
        // Logo scale animation
        ObjectAnimator logoScaleX = ObjectAnimator.ofFloat(logoImageView, "scaleX", 0.8f, 1f);
        ObjectAnimator logoScaleY = ObjectAnimator.ofFloat(logoImageView, "scaleY", 0.8f, 1f);
        logoScaleX.setDuration(LOGO_ANIMATION_DURATION);
        logoScaleY.setDuration(LOGO_ANIMATION_DURATION);
        logoScaleX.setInterpolator(new AccelerateDecelerateInterpolator());
        logoScaleY.setInterpolator(new AccelerateDecelerateInterpolator());
        
        // Start logo animations
        logoFadeIn.start();
        logoScaleX.start();
        logoScaleY.start();
        
        Log.d(TAG, "Logo animation started");
    }
    
    private void animateAppName() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ObjectAnimator appNameFadeIn = ObjectAnimator.ofFloat(appNameText, "alpha", 0f, 1f);
                appNameFadeIn.setDuration(TEXT_ANIMATION_DURATION);
                appNameFadeIn.setInterpolator(new AccelerateDecelerateInterpolator());
                
                // Subtle slide up animation for app name
                ObjectAnimator appNameSlideUp = ObjectAnimator.ofFloat(appNameText, "translationY", 30f, 0f);
                appNameSlideUp.setDuration(TEXT_ANIMATION_DURATION);
                appNameSlideUp.setInterpolator(new AccelerateDecelerateInterpolator());
                
                appNameFadeIn.start();
                appNameSlideUp.start();
                
                Log.d(TAG, "App name animation started");
            }
        }, TEXT_ANIMATION_DELAY);
    }
    
    private void animateTagline() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ObjectAnimator taglineFadeIn = ObjectAnimator.ofFloat(taglineText, "alpha", 0f, 1f);
                taglineFadeIn.setDuration(TEXT_ANIMATION_DURATION);
                taglineFadeIn.setInterpolator(new AccelerateDecelerateInterpolator());
                
                // Subtle slide up animation for tagline
                ObjectAnimator taglineSlideUp = ObjectAnimator.ofFloat(taglineText, "translationY", 20f, 0f);
                taglineSlideUp.setDuration(TEXT_ANIMATION_DURATION);
                taglineSlideUp.setInterpolator(new AccelerateDecelerateInterpolator());
                
                taglineFadeIn.start();
                taglineSlideUp.start();
                
                Log.d(TAG, "Tagline animation started");
            }
        }, TEXT_ANIMATION_DELAY + 200); // Slightly after app name
    }
    
    private void scheduleMainActivityTransition() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                transitionToMainActivity();
            }
        }, SPLASH_DURATION);
    }
    
    private void transitionToMainActivity() {
        Log.d(TAG, "Starting transition to MainActivity");
        
        // Create fade-out animation for the entire splash screen
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(backgroundView, "alpha", 1f, 0f);
        fadeOut.setDuration(500);
        fadeOut.setInterpolator(new AccelerateDecelerateInterpolator());
        
        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // Start MainActivity after fade-out completes
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                
                // Custom transition animation
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                
                // Finish splash activity
                finish();
                
                Log.d(TAG, "Transitioned to MainActivity");
            }
        });
        
        fadeOut.start();
    }
    
    @Override
    public void onBackPressed() {
        // Disable back button on splash screen
        // User should not be able to go back from splash
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        // If user switches apps during splash, finish immediately when they return
        finish();
    }
} 