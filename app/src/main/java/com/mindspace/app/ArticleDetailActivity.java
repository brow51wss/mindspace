package com.mindspace.app;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.ScrollView;

public class ArticleDetailActivity extends AppCompatActivity {

    private TextView titleText;
    private TextView contentText;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);
        
        setupActionBar();
        initializeViews();
        loadArticleContent();
    }
    
    private void setupActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Article");
        }
    }
    
    private void initializeViews() {
        titleText = findViewById(R.id.article_title);
        contentText = findViewById(R.id.article_content);
        scrollView = findViewById(R.id.scroll_view);
    }
    
    private void loadArticleContent() {
        String title = getIntent().getStringExtra("title");
        String content = getIntent().getStringExtra("content");
        
        if (title != null) {
            titleText.setText(title);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(title);
            }
        }
        
        if (content != null) {
            contentText.setText(content);
        }
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
} 