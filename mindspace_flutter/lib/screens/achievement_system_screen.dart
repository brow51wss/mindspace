import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';
import '../models/gamification_models.dart';

class AchievementSystemScreen extends StatefulWidget {
  @override
  _AchievementSystemScreenState createState() => _AchievementSystemScreenState();
}

class _AchievementSystemScreenState extends State<AchievementSystemScreen> {
  List<Achievement> achievements = [];
  int totalXP = 0;
  int currentLevel = 1;
  int totalBadges = 0;
  bool isLoading = true;

  @override
  void initState() {
    super.initState();
    _loadAchievements();
  }

  Future<void> _loadAchievements() async {
    // Update achievements first
    await AchievementManager.checkAndUpdateAchievements();
    
    // Then load the updated achievements
    final loadedAchievements = await AchievementManager.getUnlockedAchievements();
    final prefs = await SharedPreferences.getInstance();
    
    final xp = prefs.getInt('total_xp') ?? 0;
    final level = GamificationData.calculateLevel(xp);
    final badges = loadedAchievements.where((a) => a.isUnlocked).length;
    
    setState(() {
      achievements = loadedAchievements;
      totalXP = xp;
      currentLevel = level;
      totalBadges = badges;
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
          'Achievements',
          style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold),
        ),
        leading: IconButton(
          icon: const Icon(Icons.arrow_back, color: Colors.white),
          onPressed: () => Navigator.pop(context),
        ),
        elevation: 0,
      ),
      body: Column(
        children: [
          // Header Stats
          _buildHeaderStats(),
          
          // Achievements Grid
          Expanded(
            child: _buildAchievementsGrid(),
          ),
        ],
      ),
    );
  }

  Widget _buildHeaderStats() {
    return Container(
      width: double.infinity,
      padding: const EdgeInsets.all(24),
      decoration: BoxDecoration(
        color: const Color(0xFF2D2D42),
        borderRadius: const BorderRadius.only(
          bottomLeft: Radius.circular(24),
          bottomRight: Radius.circular(24),
        ),
        boxShadow: [
          BoxShadow(
            color: Colors.black26,
            offset: const Offset(0, 4),
            blurRadius: 8,
          ),
        ],
      ),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceEvenly,
        children: [
          _buildHeaderStatItem('🏆', 'Total Badges', '$totalBadges', Colors.amber),
          Container(width: 1, height: 50, color: Colors.white24),
          _buildHeaderStatItem('⭐', 'Total XP', '$totalXP', Colors.yellow),
          Container(width: 1, height: 50, color: Colors.white24),
          _buildHeaderStatItem('📈', 'Level', '$currentLevel', Colors.green),
        ],
      ),
    );
  }

  Widget _buildHeaderStatItem(String emoji, String label, String value, Color color) {
    return Column(
      children: [
        Text(emoji, style: const TextStyle(fontSize: 28)),
        const SizedBox(height: 8),
        Text(
          value,
          style: TextStyle(
            color: color,
            fontSize: 20,
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
        ),
      ],
    );
  }

  Widget _buildAchievementsGrid() {
    return Padding(
      padding: const EdgeInsets.all(16),
      child: Column(
        children: [
          // Category tabs
          _buildCategoryTabs(),
          const SizedBox(height: 16),
          
          // Achievements grid
          Expanded(
            child: GridView.builder(
              gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
                crossAxisCount: 2,
                crossAxisSpacing: 12,
                mainAxisSpacing: 12,
                childAspectRatio: 1.0,
              ),
              itemCount: achievements.length,
              itemBuilder: (context, index) {
                return _buildAchievementCard(achievements[index]);
              },
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildCategoryTabs() {
    return Container(
      height: 40,
      child: ListView(
        scrollDirection: Axis.horizontal,
        children: [
          _buildCategoryTab('All', isSelected: true),
          _buildCategoryTab('😊 Mood'),
          _buildCategoryTab('🧘 Meditation'),
          _buildCategoryTab('🧠 CBT'),
          _buildCategoryTab('⭐ Special'),
        ],
      ),
    );
  }

  Widget _buildCategoryTab(String label, {bool isSelected = false}) {
    return Container(
      margin: const EdgeInsets.only(right: 8),
      padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
      decoration: BoxDecoration(
        color: isSelected ? const Color(0xFF6C5CE7) : const Color(0xFF2D2D42),
        borderRadius: BorderRadius.circular(20),
        border: Border.all(
          color: isSelected ? const Color(0xFF6C5CE7) : Colors.white24,
        ),
      ),
      child: Text(
        label,
        style: TextStyle(
          color: isSelected ? Colors.white : Colors.white70,
          fontSize: 14,
          fontWeight: isSelected ? FontWeight.bold : FontWeight.normal,
        ),
      ),
    );
  }

  Widget _buildAchievementCard(Achievement achievement) {
    return Container(
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(
        color: const Color(0xFF2D2D42),
        borderRadius: BorderRadius.circular(16),
        border: Border.all(
          color: achievement.isUnlocked 
              ? _getCategoryColor(achievement.category)
              : Colors.white12,
          width: 2,
        ),
        boxShadow: [
          if (achievement.isUnlocked)
            BoxShadow(
              color: _getCategoryColor(achievement.category).withOpacity(0.3),
              offset: const Offset(0, 4),
              blurRadius: 12,
            ),
        ],
      ),
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          // Achievement icon/emoji
          Container(
            width: 50,
            height: 50,
            decoration: BoxDecoration(
              color: achievement.isUnlocked 
                  ? _getCategoryColor(achievement.category).withOpacity(0.2)
                  : Colors.white12,
              borderRadius: BorderRadius.circular(25),
            ),
            child: Center(
              child: Text(
                _getAchievementEmoji(achievement),
                style: const TextStyle(fontSize: 24),
              ),
            ),
          ),
          const SizedBox(height: 12),
          
          // Achievement title
          Text(
            achievement.title,
            style: TextStyle(
              color: achievement.isUnlocked ? Colors.white : Colors.white54,
              fontSize: 14,
              fontWeight: FontWeight.bold,
            ),
            textAlign: TextAlign.center,
            maxLines: 2,
            overflow: TextOverflow.ellipsis,
          ),
          const SizedBox(height: 6),
          
          // Achievement description
          Text(
            achievement.description,
            style: TextStyle(
              color: achievement.isUnlocked ? Colors.white70 : Colors.white38,
              fontSize: 12,
            ),
            textAlign: TextAlign.center,
            maxLines: 2,
            overflow: TextOverflow.ellipsis,
          ),
          const SizedBox(height: 8),
          
          // XP reward
          Container(
            padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
            decoration: BoxDecoration(
              color: achievement.isUnlocked 
                  ? _getCategoryColor(achievement.category).withOpacity(0.2)
                  : Colors.white10,
              borderRadius: BorderRadius.circular(12),
            ),
            child: Text(
              '${achievement.xpReward} XP',
              style: TextStyle(
                color: achievement.isUnlocked 
                    ? _getCategoryColor(achievement.category)
                    : Colors.white54,
                fontSize: 12,
                fontWeight: FontWeight.bold,
              ),
            ),
          ),
          
          // Unlock date if unlocked
          if (achievement.isUnlocked && achievement.unlockedDate != null) ...[
            const SizedBox(height: 4),
            Text(
              'Unlocked ${_formatDate(achievement.unlockedDate!)}',
              style: const TextStyle(
                color: Colors.white54,
                fontSize: 10,
              ),
            ),
          ],
        ],
      ),
    );
  }

  Color _getCategoryColor(String category) {
    switch (category) {
      case 'mood':
        return Colors.blue;
      case 'meditation':
        return Colors.green;
      case 'cbt':
        return Colors.purple;
      case 'special':
        return Colors.amber;
      default:
        return Colors.grey;
    }
  }

  String _getAchievementEmoji(Achievement achievement) {
    // Extract emoji from title (first character)
    final titleParts = achievement.title.split(' ');
    return titleParts.isNotEmpty ? titleParts.first : '🏆';
  }

  String _formatDate(DateTime date) {
    final now = DateTime.now();
    final difference = now.difference(date);
    
    if (difference.inDays == 0) {
      return 'today';
    } else if (difference.inDays == 1) {
      return '1 day ago';
    } else if (difference.inDays < 7) {
      return '${difference.inDays} days ago';
    } else if (difference.inDays < 30) {
      final weeks = (difference.inDays / 7).floor();
      return weeks == 1 ? '1 week ago' : '$weeks weeks ago';
    } else {
      final months = (difference.inDays / 30).floor();
      return months == 1 ? '1 month ago' : '$months months ago';
    }
  }
} 