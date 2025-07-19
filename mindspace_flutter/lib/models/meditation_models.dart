class MeditationSession {
  final String title;
  final String duration;
  final String level;
  final String description;

  MeditationSession({
    required this.title,
    required this.duration,
    required this.level,
    required this.description,
  });
}

class MeditationCategory {
  final String name;
  final String color;
  final List<MeditationSession> sessions;
  bool isExpanded;

  MeditationCategory({
    required this.name,
    required this.color,
    required this.sessions,
    this.isExpanded = false,
  });
}

class MeditationData {
  static List<MeditationCategory> getMeditationCategories() {
    return [
      // Stress Relief Category
      MeditationCategory(
        name: '😌 Stress Relief',
        color: '#FF6B6B',
        sessions: [
          MeditationSession(
            title: 'Quick Calm',
            duration: '5 minutes',
            level: 'Beginner',
            description: 'A rapid stress-relief technique perfect for busy students. Uses progressive muscle relaxation and breathing to quickly reduce tension and anxiety.',
          ),
          MeditationSession(
            title: 'Deep Relaxation',
            duration: '15 minutes',
            level: 'Intermediate',
            description: 'Comprehensive body scan and breathing meditation designed to release deep-seated stress and promote full-body relaxation.',
          ),
          MeditationSession(
            title: 'Tension Release',
            duration: '10 minutes',
            level: 'Beginner',
            description: 'Targeted meditation focusing on common stress points like shoulders, jaw, and mind. Perfect after long study sessions.',
          ),
        ],
      ),

      // Focus & Concentration Category
      MeditationCategory(
        name: '🧠 Focus & Concentration',
        color: '#4ECDC4',
        sessions: [
          MeditationSession(
            title: 'Study Focus',
            duration: '12 minutes',
            level: 'Intermediate',
            description: 'Enhance concentration and mental clarity before studying. Uses mindfulness techniques to improve attention span and reduce distractions.',
          ),
          MeditationSession(
            title: 'Mind Clarity',
            duration: '8 minutes',
            level: 'Beginner',
            description: 'Clear mental fog and improve decision-making. Perfect before exams or important presentations to sharpen cognitive function.',
          ),
          MeditationSession(
            title: 'Attention Training',
            duration: '20 minutes',
            level: 'Advanced',
            description: 'Advanced concentration practice using single-point focus techniques. Builds sustained attention and mental discipline.',
          ),
        ],
      ),

      // Sleep & Rest Category
      MeditationCategory(
        name: '😴 Sleep & Rest',
        color: '#9B59B6',
        sessions: [
          MeditationSession(
            title: 'Bedtime Calm',
            duration: '15 minutes',
            level: 'Beginner',
            description: 'Gentle guided meditation designed to quiet racing thoughts and prepare your mind and body for restful sleep.',
          ),
          MeditationSession(
            title: 'Sleep Preparation',
            duration: '10 minutes',
            level: 'Beginner',
            description: 'Progressive relaxation technique that helps transition from daily stress to peaceful sleep. Perfect for students with busy minds.',
          ),
        ],
      ),

      // Confidence Building Category
      MeditationCategory(
        name: '💪 Confidence Building',
        color: '#F39C12',
        sessions: [
          MeditationSession(
            title: 'Self-Esteem Boost',
            duration: '12 minutes',
            level: 'Intermediate',
            description: 'Positive affirmation meditation combined with visualization techniques to build self-confidence and reduce self-doubt.',
          ),
          MeditationSession(
            title: 'Inner Strength',
            duration: '18 minutes',
            level: 'Intermediate',
            description: 'Develop resilience and inner confidence through mindfulness and self-compassion practices. Perfect for overcoming challenges.',
          ),
        ],
      ),

      // Quick Energy Category
      MeditationCategory(
        name: '⚡ Quick Energy',
        color: '#2ECC71',
        sessions: [
          MeditationSession(
            title: '5-Min Reset',
            duration: '5 minutes',
            level: 'Beginner',
            description: 'Quick energy boost using breathing techniques and gentle movement. Perfect between classes or during study breaks.',
          ),
          MeditationSession(
            title: 'Morning Boost',
            duration: '8 minutes',
            level: 'Beginner',
            description: 'Energizing morning meditation to start your day with clarity, positivity, and focused intention.',
          ),
        ],
      ),
    ];
  }
} 