package com.mindspace.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import android.content.Intent;
import android.net.Uri;
import java.util.ArrayList;
import java.util.List;

public class ResourceHubActivity extends AppCompatActivity {

    private RecyclerView categoriesRecyclerView;
    private ResourceCategoryAdapter categoryAdapter;
    private List<ResourceCategory> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource_hub);
        
        setupActionBar();
        initializeViews();
        loadResourceCategories();
    }
    
    private void setupActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Mental Health Resources");
        }
    }
    
    private void initializeViews() {
        categoriesRecyclerView = findViewById(R.id.categories_recycler_view);
        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        categories = new ArrayList<>();
        categoryAdapter = new ResourceCategoryAdapter(categories);
        categoriesRecyclerView.setAdapter(categoryAdapter);
    }
    
    private void loadResourceCategories() {
        // Understanding Mental Health
        List<ResourceArticle> mentalHealthArticles = new ArrayList<>();
        mentalHealthArticles.add(new ResourceArticle(
            "What is Mental Health?",
            "Understanding the basics of mental wellness and why it matters for students.",
            "Mental health includes our emotional, psychological, and social well-being. It affects how we think, feel, and act as we cope with life. For students, maintaining good mental health is crucial for academic success, healthy relationships, and overall life satisfaction.\n\nKey aspects of mental health:\n• Emotional regulation\n• Stress management\n• Healthy relationships\n• Self-awareness\n• Resilience building\n\nRemember: It's normal to experience ups and downs. Seeking help when needed is a sign of strength, not weakness."
        ));
        mentalHealthArticles.add(new ResourceArticle(
            "Mental Health Myths vs Facts",
            "Debunking common misconceptions about mental health among teens.",
            "MYTH: Mental health problems are rare among teens.\nFACT: 1 in 5 teens experience mental health challenges.\n\nMYTH: Asking for help means you're weak.\nFACT: Seeking support shows courage and self-awareness.\n\nMYTH: Mental health issues will go away on their own.\nFACT: Like physical health, mental health requires attention and care.\n\nMYTH: Medication is the only solution.\nFACT: Many effective treatments exist, including therapy, lifestyle changes, and peer support.\n\nUnderstanding these facts helps reduce stigma and encourages help-seeking behavior."
        ));
        
        // Managing Stress & Anxiety
        List<ResourceArticle> stressArticles = new ArrayList<>();
        stressArticles.add(new ResourceArticle(
            "5 Quick Stress Relief Techniques",
            "Simple methods to calm your mind when feeling overwhelmed.",
            "When stress hits, try these evidence-based techniques:\n\n1. **4-7-8 Breathing**\n   Inhale for 4, hold for 7, exhale for 8. Repeat 3-4 times.\n\n2. **Progressive Muscle Relaxation**\n   Tense and release each muscle group from toes to head.\n\n3. **Grounding Technique (5-4-3-2-1)**\n   Notice 5 things you see, 4 you hear, 3 you touch, 2 you smell, 1 you taste.\n\n4. **Mindful Walking**\n   Take a 5-minute walk focusing on each step and your surroundings.\n\n5. **Positive Self-Talk**\n   Replace 'I can't handle this' with 'I can take this one step at a time.'\n\nPractice these regularly so they're ready when you need them most."
        ));
        stressArticles.add(new ResourceArticle(
            "Understanding Anxiety in Students",
            "Recognizing anxiety symptoms and learning healthy coping strategies.",
            "Anxiety is your body's natural response to stress, but when it becomes overwhelming, it can interfere with daily life.\n\n**Common Signs of Anxiety:**\n• Racing thoughts or constant worry\n• Physical symptoms (racing heart, sweating)\n• Difficulty concentrating\n• Avoiding certain situations\n• Sleep problems\n\n**Healthy Coping Strategies:**\n• Regular exercise and movement\n• Maintaining a consistent sleep schedule\n• Limiting caffeine and social media\n• Practicing mindfulness and meditation\n• Connecting with supportive friends and family\n• Breaking large tasks into smaller steps\n\n**When to Seek Help:**\nIf anxiety interferes with school, relationships, or daily activities for more than two weeks, consider talking to a counselor, trusted adult, or mental health professional."
        ));
        
        // Sleep & Self-Care
        List<ResourceArticle> sleepArticles = new ArrayList<>();
        sleepArticles.add(new ResourceArticle(
            "The Student's Guide to Better Sleep",
            "Why sleep matters for mental health and how to improve your sleep quality.",
            "Sleep is crucial for mental health, memory consolidation, and emotional regulation. Most teens need 8-10 hours per night.\n\n**Why Sleep Matters:**\n• Improves mood and reduces irritability\n• Enhances focus and academic performance\n• Strengthens immune system\n• Supports emotional processing\n\n**Sleep Hygiene Tips:**\n• Set a consistent bedtime and wake time\n• Create a relaxing bedtime routine\n• Keep your bedroom cool, dark, and quiet\n• Avoid screens 1 hour before bed\n• Limit caffeine after 2 PM\n• Get natural sunlight during the day\n• Exercise regularly (but not close to bedtime)\n\n**If You Can't Sleep:**\n• Try the 4-7-8 breathing technique\n• Practice progressive muscle relaxation\n• Write down worries to address tomorrow\n• Read a calming book\n• Listen to soft music or nature sounds"
        ));
        
        // Building Resilience
        List<ResourceArticle> resilienceArticles = new ArrayList<>();
        resilienceArticles.add(new ResourceArticle(
            "Building Mental Resilience",
            "Developing the ability to bounce back from challenges and setbacks.",
            "Resilience is the ability to adapt and recover from difficult experiences. It's a skill that can be developed over time.\n\n**Key Components of Resilience:**\n• **Emotional Awareness**: Understanding and accepting your emotions\n• **Problem-Solving**: Breaking challenges into manageable steps\n• **Social Support**: Building and maintaining healthy relationships\n• **Self-Care**: Taking care of your physical and mental needs\n• **Optimism**: Focusing on possibilities and growth\n\n**Building Resilience:**\n• Practice gratitude daily\n• Learn from setbacks instead of dwelling on them\n• Develop a growth mindset\n• Build strong relationships with family and friends\n• Take care of your physical health\n• Set realistic goals and celebrate small wins\n• Practice mindfulness and stress management\n\n**Remember**: Resilience doesn't mean avoiding difficult emotions or pretending everything is fine. It means developing healthy ways to cope with life's challenges."
        ));
        
        // Confidence & Self-Esteem
        List<ResourceArticle> confidenceArticles = new ArrayList<>();
        confidenceArticles.add(new ResourceArticle(
            "Building Healthy Self-Esteem",
            "Practical strategies for developing confidence and self-worth.",
            "Self-esteem is how you feel about yourself overall. Healthy self-esteem provides a foundation for mental wellness and positive relationships.\n\n**Signs of Healthy Self-Esteem:**\n• Accepting compliments gracefully\n• Learning from mistakes without harsh self-criticism\n• Setting boundaries in relationships\n• Trying new things despite fear of failure\n• Expressing your needs and opinions\n\n**Building Self-Esteem:**\n• **Practice Self-Compassion**: Treat yourself with the same kindness you'd show a good friend\n• **Challenge Negative Self-Talk**: Replace harsh inner criticism with balanced, realistic thoughts\n• **Celebrate Small Wins**: Acknowledge your daily accomplishments, no matter how small\n• **Develop Your Strengths**: Focus on activities that showcase your talents and interests\n• **Set Achievable Goals**: Build confidence through consistent small successes\n• **Surround Yourself with Positivity**: Spend time with people who support and encourage you\n\n**Daily Affirmations:**\n• 'I am worthy of respect and kindness'\n• 'I can learn and grow from challenges'\n• 'My thoughts and feelings matter'\n• 'I have unique strengths and talents'"
        ));
        
        // Add categories
        categories.add(new ResourceCategory("🧠 Understanding Mental Health", "Learn the basics of mental wellness", mentalHealthArticles));
        categories.add(new ResourceCategory("😰 Managing Stress & Anxiety", "Tools for handling overwhelming feelings", stressArticles));
        categories.add(new ResourceCategory("😴 Sleep & Self-Care", "Essential habits for mental wellness", sleepArticles));
        categories.add(new ResourceCategory("💪 Building Resilience", "Develop strength to overcome challenges", resilienceArticles));
        categories.add(new ResourceCategory("🎯 Confidence & Self-Esteem", "Build a positive relationship with yourself", confidenceArticles));
        
        categoryAdapter.notifyDataSetChanged();
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    // Data classes
    public static class ResourceCategory {
        public String title;
        public String description;
        public List<ResourceArticle> articles;
        public boolean isExpanded;
        
        public ResourceCategory(String title, String description, List<ResourceArticle> articles) {
            this.title = title;
            this.description = description;
            this.articles = articles;
            this.isExpanded = false;
        }
    }
    
    public static class ResourceArticle {
        public String title;
        public String summary;
        public String content;
        
        public ResourceArticle(String title, String summary, String content) {
            this.title = title;
            this.summary = summary;
            this.content = content;
        }
    }
    
    // Adapter for categories
    private class ResourceCategoryAdapter extends RecyclerView.Adapter<ResourceCategoryAdapter.CategoryViewHolder> {
        private List<ResourceCategory> categories;
        
        public ResourceCategoryAdapter(List<ResourceCategory> categories) {
            this.categories = categories;
        }
        
        @Override
        public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_resource_category, parent, false);
            return new CategoryViewHolder(view);
        }
        
        @Override
        public void onBindViewHolder(CategoryViewHolder holder, int position) {
            ResourceCategory category = categories.get(position);
            holder.bind(category);
        }
        
        @Override
        public int getItemCount() {
            return categories.size();
        }
        
        class CategoryViewHolder extends RecyclerView.ViewHolder {
            TextView titleText;
            TextView descriptionText;
            ImageView expandIcon;
            RecyclerView articlesRecyclerView;
            
            public CategoryViewHolder(View itemView) {
                super(itemView);
                titleText = itemView.findViewById(R.id.category_title);
                descriptionText = itemView.findViewById(R.id.category_description);
                expandIcon = itemView.findViewById(R.id.expand_icon);
                articlesRecyclerView = itemView.findViewById(R.id.articles_recycler_view);
                
                articlesRecyclerView.setLayoutManager(new LinearLayoutManager(ResourceHubActivity.this));
            }
            
            public void bind(ResourceCategory category) {
                titleText.setText(category.title);
                descriptionText.setText(category.description);
                
                // Set up expand/collapse
                expandIcon.setRotation(category.isExpanded ? 180 : 0);
                articlesRecyclerView.setVisibility(category.isExpanded ? View.VISIBLE : View.GONE);
                
                if (category.isExpanded) {
                    ArticleAdapter articleAdapter = new ArticleAdapter(category.articles);
                    articlesRecyclerView.setAdapter(articleAdapter);
                }
                
                itemView.setOnClickListener(v -> {
                    category.isExpanded = !category.isExpanded;
                    notifyItemChanged(getAdapterPosition());
                });
            }
        }
    }
    
    // Adapter for articles within categories
    private class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {
        private List<ResourceArticle> articles;
        
        public ArticleAdapter(List<ResourceArticle> articles) {
            this.articles = articles;
        }
        
        @Override
        public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_resource_article, parent, false);
            return new ArticleViewHolder(view);
        }
        
        @Override
        public void onBindViewHolder(ArticleViewHolder holder, int position) {
            ResourceArticle article = articles.get(position);
            holder.bind(article);
        }
        
        @Override
        public int getItemCount() {
            return articles.size();
        }
        
        class ArticleViewHolder extends RecyclerView.ViewHolder {
            TextView titleText;
            TextView summaryText;
            
            public ArticleViewHolder(View itemView) {
                super(itemView);
                titleText = itemView.findViewById(R.id.article_title);
                summaryText = itemView.findViewById(R.id.article_summary);
            }
            
            public void bind(ResourceArticle article) {
                titleText.setText(article.title);
                summaryText.setText(article.summary);
                
                itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(ResourceHubActivity.this, ArticleDetailActivity.class);
                    intent.putExtra("title", article.title);
                    intent.putExtra("content", article.content);
                    startActivity(intent);
                });
            }
        }
    }
} 