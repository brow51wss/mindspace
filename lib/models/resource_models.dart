class ResourceCategory {
  final String title;
  final String description;
  final List<ResourceArticle> articles;
  bool isExpanded;

  ResourceCategory({
    required this.title,
    required this.description,
    required this.articles,
    this.isExpanded = false,
  });
}

class ResourceArticle {
  final String title;
  final String summary;
  final String content;

  ResourceArticle({
    required this.title,
    required this.summary,
    required this.content,
  });
}

class ResourceData {
  static List<ResourceCategory> getCategories() {
    // Understanding Mental Health
    List<ResourceArticle> mentalHealthArticles = [
      ResourceArticle(
        title: "What is Mental Health?",
        summary: "Understanding the basics of mental wellness and why it matters for students.",
        content: "Mental health includes our emotional, psychological, and social well-being. It affects how we think, feel, and act as we cope with life. For students, maintaining good mental health is crucial for academic success, healthy relationships, and overall life satisfaction.\n\nKey aspects of mental health:\n• Emotional regulation\n• Stress management\n• Healthy relationships\n• Self-awareness\n• Resilience building\n\nRemember: It's normal to experience ups and downs. Seeking help when needed is a sign of strength, not weakness.",
      ),
      ResourceArticle(
        title: "Mental Health Myths vs Facts",
        summary: "Debunking common misconceptions about mental health among teens.",
        content: "MYTH: Mental health problems are rare among teens.\nFACT: 1 in 5 teens experience mental health challenges.\n\nMYTH: Asking for help means you're weak.\nFACT: Seeking support shows courage and self-awareness.\n\nMYTH: Mental health issues will go away on their own.\nFACT: Like physical health, mental health requires attention and care.\n\nMYTH: Medication is the only solution.\nFACT: Many effective treatments exist, including therapy, lifestyle changes, and peer support.\n\nUnderstanding these facts helps reduce stigma and encourages help-seeking behavior.",
      ),
    ];

    // Managing Stress & Anxiety
    List<ResourceArticle> stressArticles = [
      ResourceArticle(
        title: "5 Quick Stress Relief Techniques",
        summary: "Simple methods to calm your mind when feeling overwhelmed.",
        content: "When stress hits, try these evidence-based techniques:\n\n1. **4-7-8 Breathing**\n   Inhale for 4, hold for 7, exhale for 8. Repeat 3-4 times.\n\n2. **Progressive Muscle Relaxation**\n   Tense and release each muscle group from toes to head.\n\n3. **Grounding Technique (5-4-3-2-1)**\n   Notice 5 things you see, 4 you hear, 3 you touch, 2 you smell, 1 you taste.\n\n4. **Mindful Walking**\n   Take a 5-minute walk focusing on each step and your surroundings.\n\n5. **Positive Self-Talk**\n   Replace 'I can't handle this' with 'I can take this one step at a time.'\n\nPractice these regularly so they're ready when you need them most.",
      ),
      ResourceArticle(
        title: "Understanding Anxiety in Students",
        summary: "Recognizing anxiety symptoms and learning healthy coping strategies.",
        content: "Anxiety is your body's natural response to stress, but when it becomes overwhelming, it can interfere with daily life.\n\n**Common Signs of Anxiety:**\n• Racing thoughts or constant worry\n• Physical symptoms (racing heart, sweating)\n• Difficulty concentrating\n• Avoiding certain situations\n• Sleep problems\n\n**Healthy Coping Strategies:**\n• Regular exercise and movement\n• Maintaining a consistent sleep schedule\n• Limiting caffeine and social media\n• Practicing mindfulness and meditation\n• Connecting with supportive friends and family\n• Breaking large tasks into smaller steps\n\n**When to Seek Help:**\nIf anxiety interferes with school, relationships, or daily activities for more than two weeks, consider talking to a counselor, trusted adult, or mental health professional.",
      ),
    ];

    // Sleep & Self-Care
    List<ResourceArticle> sleepArticles = [
      ResourceArticle(
        title: "The Student's Guide to Better Sleep",
        summary: "Why sleep matters for mental health and how to improve your sleep quality.",
        content: "Sleep is crucial for mental health, memory consolidation, and emotional regulation. Most teens need 8-10 hours per night.\n\n**Why Sleep Matters:**\n• Improves mood and reduces irritability\n• Enhances focus and academic performance\n• Strengthens immune system\n• Supports emotional processing\n\n**Sleep Hygiene Tips:**\n• Set a consistent bedtime and wake time\n• Create a relaxing bedtime routine\n• Keep your bedroom cool, dark, and quiet\n• Avoid screens 1 hour before bed\n• Limit caffeine after 2 PM\n• Get natural sunlight during the day\n• Exercise regularly (but not close to bedtime)\n\n**If You Can't Sleep:**\n• Try the 4-7-8 breathing technique\n• Practice progressive muscle relaxation\n• Write down worries to address tomorrow\n• Read a calming book\n• Listen to soft music or nature sounds",
      ),
    ];

    // Building Resilience
    List<ResourceArticle> resilienceArticles = [
      ResourceArticle(
        title: "Building Mental Resilience",
        summary: "Developing the ability to bounce back from challenges and setbacks.",
        content: "Resilience is the ability to adapt and recover from difficult experiences. It's a skill that can be developed over time.\n\n**Key Components of Resilience:**\n• **Emotional Awareness**: Understanding and accepting your emotions\n• **Problem-Solving**: Breaking challenges into manageable steps\n• **Social Support**: Building and maintaining healthy relationships\n• **Self-Care**: Taking care of your physical and mental needs\n• **Optimism**: Focusing on possibilities and growth\n\n**Building Resilience:**\n• Practice gratitude daily\n• Learn from setbacks instead of dwelling on them\n• Develop a growth mindset\n• Build strong relationships with family and friends\n• Take care of your physical health\n• Set realistic goals and celebrate small wins\n• Practice mindfulness and stress management\n\n**Remember**: Resilience doesn't mean avoiding difficult emotions or pretending everything is fine. It means developing healthy ways to cope with life's challenges.",
      ),
    ];

    // Confidence & Self-Esteem
    List<ResourceArticle> confidenceArticles = [
      ResourceArticle(
        title: "Building Healthy Self-Esteem",
        summary: "Practical strategies for developing confidence and self-worth.",
        content: "Self-esteem is how you feel about yourself overall. Healthy self-esteem provides a foundation for mental wellness and positive relationships.\n\n**Signs of Healthy Self-Esteem:**\n• Accepting compliments gracefully\n• Learning from mistakes without harsh self-criticism\n• Setting boundaries in relationships\n• Trying new things despite fear of failure\n• Expressing your needs and opinions\n\n**Building Self-Esteem:**\n• **Practice Self-Compassion**: Treat yourself with the same kindness you'd show a good friend\n• **Challenge Negative Self-Talk**: Replace harsh inner criticism with balanced, realistic thoughts\n• **Celebrate Small Wins**: Acknowledge your daily accomplishments, no matter how small\n• **Develop Your Strengths**: Focus on activities that showcase your talents and interests\n• **Set Achievable Goals**: Build confidence through consistent small successes\n• **Surround Yourself with Positivity**: Spend time with people who support and encourage you\n\n**Daily Affirmations:**\n• 'I am worthy of respect and kindness'\n• 'I can learn and grow from challenges'\n• 'My thoughts and feelings matter'\n• 'I have unique strengths and talents'",
      ),
    ];

    return [
      ResourceCategory(
        title: "🧠 Understanding Mental Health",
        description: "Learn the basics of mental wellness",
        articles: mentalHealthArticles,
      ),
      ResourceCategory(
        title: "😰 Managing Stress & Anxiety",
        description: "Tools for handling overwhelming feelings",
        articles: stressArticles,
      ),
      ResourceCategory(
        title: "😴 Sleep & Self-Care",
        description: "Essential habits for mental wellness",
        articles: sleepArticles,
      ),
      ResourceCategory(
        title: "💪 Building Resilience",
        description: "Develop strength to overcome challenges",
        articles: resilienceArticles,
      ),
      ResourceCategory(
        title: "🎯 Confidence & Self-Esteem",
        description: "Build a positive relationship with yourself",
        articles: confidenceArticles,
      ),
    ];
  }
} 