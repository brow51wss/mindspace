import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';
import '../models/gamification_models.dart';
import 'achievement_system_screen.dart';

class ProgressDashboardScreen extends StatefulWidget {
  @override
  _ProgressDashboardScreenState createState() => _ProgressDashboardScreenState();
}

class _ProgressDashboardScreenState extends State<ProgressDashboardScreen> {
  ProgressStats stats = ProgressStats();
  bool isLoading = true;

  @override
  void initState() {
    super.initState();
    _loadProgressData();
  }

  Future<void> _loadProgressData() async {
    final prefs = await SharedPreferences.getInstance();
    
    // Calculate total XP and level
    final totalXP = prefs.getInt('total_xp') ?? 0;
    final currentLevel = GamificationData.calculateLevel(totalXP);
    
    // Get activity stats
    final moodEntries = prefs.getInt('total_mood_entries') ?? 0;
    final meditationSessions = prefs.getInt('total_meditation_sessions') ?? 0;
    final cbtExercises = prefs.getInt('total_cbt_exercises') ?? 0;
    
    // Calculate streaks
    final moodStreak = prefs.getInt('mood_streak') ?? 0;
    final meditationStreak = prefs.getInt('meditation_streak') ?? 0;
    final cbtStreak = prefs.getInt('cbt_streak') ?? 0;
    final highestStreak = [moodStreak, meditationStreak, cbtStreak].reduce((a, b) => a > b ? a : b);
    
    // Calculate total active days (simplified)
    final totalActiveDays = (moodEntries + meditationSessions + cbtExercises).clamp(1, 365);
    
    // Get motivational content
    final motivationalMessages = GamificationData.getMotivationalMessages();
    final weeklyInsights = GamificationData.getWeeklyInsights();
    final randomMotivational = motivationalMessages[DateTime.now().day % motivationalMessages.length];
    final randomInsight = weeklyInsights[DateTime.now().day % weeklyInsights.length];
    
    setState(() {
      stats = ProgressStats(
        totalActiveDays: totalActiveDays,
        currentStreak: highestStreak,
        totalXP: totalXP,
        currentLevel: currentLevel,
        moodEntries: moodEntries,
        meditationMinutes: meditationSessions * 10, // Estimate 10 minutes per session
        cbtExercises: cbtExercises,
        weeklyMoods: GamificationData.getSampleMoodData(),
        weeklyInsight: randomInsight,
        motivationalMessage: randomMotivational,
      );
      isLoading = false;
    });
  }

  @override
  Widget build(BuildContext context) {
    if (isLoading) {
      return Scaffold(
        backgroundColor: const Color(0xFF1E1E2E),
        body: const Center(
          child: CircularProgressIndicator(color: Color(0xFF6C5CE7)),
        ),
      );
    }

    return Scaffold(
      backgroundColor: const Color(0xFF1E1E2E),
      appBar: AppBar(
        backgroundColor: const Color(0xFF2D2D42),
        title: const Text(
          'Progress Dashboard',
          style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold),
        ),
        leading: IconButton(
          icon: const Icon(Icons.arrow_back, color: Colors.white),
          onPressed: () => Navigator.pop(context),
        ),
        elevation: 0,
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16),
        child: Column(
          children: [
            // Header Stats Row
            _buildHeaderStats(),
            const SizedBox(height: 20),
            
            // Mood Chart
            _buildMoodChart(),
            const SizedBox(height: 20),
            
            // Activity Stats Cards
            _buildActivityStats(),
            const SizedBox(height: 20),
            
            // Achievement Card
            _buildAchievementCard(),
            const SizedBox(height: 20),
            
            // Insights Card
            _buildInsightsCard(),
          ],
        ),
      ),
    );
  }

  Widget _buildHeaderStats() {
    return Container(
      width: double.infinity,
      padding: const EdgeInsets.all(20),
      decoration: BoxDecoration(
        color: const Color(0xFF2D2D42),
        borderRadius: BorderRadius.circular(16),
        boxShadow: [
          BoxShadow(
            color: Colors.black26,
            offset: const Offset(0, 4),
            blurRadius: 8,
          ),
        ],
      ),
      child: Row(
        children: [
          Expanded(
            child: _buildStatItem('Total Days', '${stats.totalActiveDays}', Icons.calendar_today, Colors.blue),
          ),
          Container(width: 1, height: 40, color: Colors.white24),
          Expanded(
            child: _buildStatItem('Current Streak', '${GamificationData.getStreakEmoji(stats.currentStreak)} ${stats.currentStreak}', Icons.local_fire_department, Colors.orange),
          ),
          Container(width: 1, height: 40, color: Colors.white24),
          Expanded(
            child: _buildStatItem('Total XP', '${stats.totalXP}', Icons.star, Colors.yellow),
          ),
          Container(width: 1, height: 40, color: Colors.white24),
          Expanded(
            child: _buildStatItem('Level', '${stats.currentLevel}', Icons.trending_up, Colors.green),
          ),
        ],
      ),
    );
  }

  Widget _buildStatItem(String label, String value, IconData icon, Color color) {
    return Column(
      children: [
        Icon(icon, color: color, size: 24),
        const SizedBox(height: 8),
        Text(
          value,
          style: const TextStyle(
            color: Colors.white,
            fontSize: 18,
            fontWeight: FontWeight.bold,
          ),
        ),
        const SizedBox(height: 4),
        Text(
          label,
          style: const TextStyle(
            color: Colors.white70,
            fontSize: 12,
          ),
          textAlign: TextAlign.center,
        ),
      ],
    );
  }

  Widget _buildMoodChart() {
    return Container(
      width: double.infinity,
      padding: const EdgeInsets.all(20),
      decoration: BoxDecoration(
        color: const Color(0xFF2D2D42),
        borderRadius: BorderRadius.circular(16),
        boxShadow: [
          BoxShadow(
            color: Colors.black26,
            offset: const Offset(0, 4),
            blurRadius: 8,
          ),
        ],
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          const Text(
            '📊 Weekly Mood Chart',
            style: TextStyle(
              color: Colors.white,
              fontSize: 18,
              fontWeight: FontWeight.bold,
            ),
          ),
          const SizedBox(height: 20),
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceEvenly,
            children: List.generate(stats.weeklyMoods.length, (index) {
              return Column(
                children: [
                  Text(
                    stats.weeklyMoods[index],
                    style: const TextStyle(fontSize: 24),
                  ),
                  const SizedBox(height: 8),
                  Text(
                    GamificationData.getMoodChartLabels()[index],
                    style: const TextStyle(
                      color: Colors.white70,
                      fontSize: 12,
                    ),
                  ),
                ],
              );
            }),
          ),
        ],
      ),
    );
  }

  Widget _buildActivityStats() {
    return Row(
      children: [
        Expanded(
          child: _buildActivityCard('😊', 'Mood Entries', '${stats.moodEntries}', Colors.blue),
        ),
        const SizedBox(width: 12),
        Expanded(
          child: _buildActivityCard('🧘', 'Meditation', '${stats.meditationMinutes} min', Colors.green),
        ),
        const SizedBox(width: 12),
        Expanded(
          child: _buildActivityCard('🧠', 'CBT Exercises', '${stats.cbtExercises}', Colors.purple),
        ),
      ],
    );
  }

  Widget _buildActivityCard(String emoji, String title, String value, Color color) {
    return Container(
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(
        color: const Color(0xFF2D2D42),
        borderRadius: BorderRadius.circular(16),
        border: Border(
          left: BorderSide(color: color, width: 4),
        ),
        boxShadow: [
          BoxShadow(
            color: Colors.black26,
            offset: const Offset(0, 4),
            blurRadius: 8,
          ),
        ],
      ),
      child: Column(
        children: [
          Text(emoji, style: const TextStyle(fontSize: 24)),
          const SizedBox(height: 8),
          Text(
            value,
            style: const TextStyle(
              color: Colors.white,
              fontSize: 18,
              fontWeight: FontWeight.bold,
            ),
          ),
          const SizedBox(height: 4),
          Text(
            title,
            style: const TextStyle(
              color: Colors.white70,
              fontSize: 12,
            ),
            textAlign: TextAlign.center,
          ),
        ],
      ),
    );
  }

  Widget _buildAchievementCard() {
    return Container(
      width: double.infinity,
      padding: const EdgeInsets.all(20),
      decoration: BoxDecoration(
        color: const Color(0xFF2D2D42),
        borderRadius: BorderRadius.circular(16),
        boxShadow: [
          BoxShadow(
            color: Colors.black26,
            offset: const Offset(0, 4),
            blurRadius: 8,
          ),
        ],
      ),
      child: InkWell(
        onTap: () {
          Navigator.push(
            context,
            MaterialPageRoute(
              builder: (context) => AchievementSystemScreen(),
            ),
          );
        },
        borderRadius: BorderRadius.circular(16),
        child: Row(
          children: [
            Container(
              width: 60,
              height: 60,
              decoration: BoxDecoration(
                color: Colors.amber.withOpacity(0.2),
                borderRadius: BorderRadius.circular(30),
              ),
              child: const Icon(
                Icons.emoji_events,
                color: Colors.amber,
                size: 32,
              ),
            ),
            const SizedBox(width: 16),
            Expanded(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  const Text(
                    '🏆 Achievements',
                    style: TextStyle(
                      color: Colors.white,
                      fontSize: 18,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                  const SizedBox(height: 4),
                  Text(
                    'Tap to view your badges and progress',
                    style: const TextStyle(
                      color: Colors.white70,
                      fontSize: 14,
                    ),
                  ),
                ],
              ),
            ),
            const Icon(
              Icons.arrow_forward_ios,
              color: Colors.white38,
              size: 16,
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildInsightsCard() {
    return Container(
      width: double.infinity,
      padding: const EdgeInsets.all(20),
      decoration: BoxDecoration(
        color: const Color(0xFF2D2D42),
        borderRadius: BorderRadius.circular(16),
        boxShadow: [
          BoxShadow(
            color: Colors.black26,
            offset: const Offset(0, 4),
            blurRadius: 8,
          ),
        ],
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          const Text(
            '💡 Weekly Insight',
            style: TextStyle(
              color: Colors.white,
              fontSize: 18,
              fontWeight: FontWeight.bold,
            ),
          ),
          const SizedBox(height: 12),
          Text(
            stats.weeklyInsight,
            style: const TextStyle(
              color: Colors.white70,
              fontSize: 16,
              height: 1.4,
            ),
          ),
          const SizedBox(height: 16),
          Container(
            width: double.infinity,
            padding: const EdgeInsets.all(16),
            decoration: BoxDecoration(
              color: const Color(0xFF6C5CE7).withOpacity(0.1),
              borderRadius: BorderRadius.circular(12),
              border: Border.all(
                color: const Color(0xFF6C5CE7).withOpacity(0.3),
              ),
            ),
            child: Text(
              stats.motivationalMessage,
              style: const TextStyle(
                color: Color(0xFF6C5CE7),
                fontSize: 16,
                fontWeight: FontWeight.w600,
              ),
              textAlign: TextAlign.center,
            ),
          ),
        ],
      ),
    );
  }
} 