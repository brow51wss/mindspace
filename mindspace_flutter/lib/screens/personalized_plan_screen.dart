import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import '../models/personalized_plan_models.dart';

class PersonalizedPlanScreen extends StatefulWidget {
  @override
  _PersonalizedPlanScreenState createState() => _PersonalizedPlanScreenState();
}

class _PersonalizedPlanScreenState extends State<PersonalizedPlanScreen> {
  List<DailyRecommendation> weeklyPlan = [];
  List<WeeklyInsight> weeklyInsights = [];
  bool isLoading = true;
  String currentWeekText = "";

  @override
  void initState() {
    super.initState();
    _setupHeader();
    _generatePlan();
  }

  void _setupHeader() {
    DateTime now = DateTime.now();
    DateTime weekStart = now.subtract(Duration(days: now.weekday - 1));
    DateTime weekEnd = weekStart.add(Duration(days: 6));
    
    String startText = DateFormat('MMMM d').format(weekStart);
    String endText = DateFormat('MMMM d').format(weekEnd);
    
    setState(() {
      currentWeekText = "Week of $startText - $endText";
    });
  }

  Future<void> _generatePlan() async {
    try {
      // Generate weekly plan and insights
      final plan = await PlanGenerator.generateWeeklyPlan();
      final insights = await PlanGenerator.generateWeeklyInsights();
      
      setState(() {
        weeklyPlan = plan;
        weeklyInsights = insights;
        isLoading = false;
      });
    } catch (e) {
      print('Error generating plan: $e');
      setState(() {
        isLoading = false;
      });
    }
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
          'Your Personal Plan',
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
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // Header
            _buildHeader(),
            const SizedBox(height: 20),
            
            // Weekly Plan Section
            _buildSectionTitle('📅 Your Weekly Plan'),
            const SizedBox(height: 16),
            ...weeklyPlan.map((recommendation) => _buildDayRecommendationCard(recommendation)).toList(),
            
            const SizedBox(height: 32),
            
            // Weekly Insights Section
            _buildSectionTitle('💡 Weekly Insights'),
            const SizedBox(height: 16),
            ...weeklyInsights.map((insight) => _buildInsightCard(insight)).toList(),
            
            const SizedBox(height: 20),
          ],
        ),
      ),
    );
  }

  Widget _buildHeader() {
    return Container(
      width: double.infinity,
      padding: const EdgeInsets.all(24),
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
        crossAxisAlignment: CrossAxisAlignment.center,
        children: [
          const Text(
            '🎯',
            style: TextStyle(fontSize: 32),
          ),
          const SizedBox(height: 12),
          const Text(
            'Personalized for You',
            style: TextStyle(
              color: Colors.white,
              fontSize: 22,
              fontWeight: FontWeight.bold,
            ),
          ),
          const SizedBox(height: 8),
          Text(
            currentWeekText,
            style: const TextStyle(
              color: Colors.white70,
              fontSize: 16,
            ),
          ),
          const SizedBox(height: 12),
          Container(
            padding: const EdgeInsets.all(12),
            decoration: BoxDecoration(
              color: const Color(0xFF6C5CE7).withOpacity(0.1),
              borderRadius: BorderRadius.circular(12),
              border: Border.all(
                color: const Color(0xFF6C5CE7).withOpacity(0.3),
              ),
            ),
            child: const Text(
              'AI-powered recommendations based on your patterns',
              style: TextStyle(
                color: Color(0xFF6C5CE7),
                fontSize: 14,
                fontWeight: FontWeight.w500,
              ),
              textAlign: TextAlign.center,
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildSectionTitle(String title) {
    return Text(
      title,
      style: const TextStyle(
        color: Colors.white,
        fontSize: 20,
        fontWeight: FontWeight.bold,
      ),
    );
  }

  Widget _buildDayRecommendationCard(DailyRecommendation recommendation) {
    Color activityColor = _getActivityTypeColor(recommendation.activityType);
    
    return Container(
      width: double.infinity,
      margin: const EdgeInsets.only(bottom: 16),
      decoration: BoxDecoration(
        color: activityColor,
        borderRadius: BorderRadius.circular(16),
        boxShadow: [
          BoxShadow(
            color: activityColor.withOpacity(0.3),
            offset: const Offset(0, 4),
            blurRadius: 12,
          ),
        ],
      ),
      child: Padding(
        padding: const EdgeInsets.all(20),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // Day header with time estimate
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Text(
                  recommendation.day,
                  style: const TextStyle(
                    color: Colors.white,
                    fontSize: 18,
                    fontWeight: FontWeight.bold,
                  ),
                ),
                Container(
                  padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
                  decoration: BoxDecoration(
                    color: Colors.white.withOpacity(0.2),
                    borderRadius: BorderRadius.circular(12),
                  ),
                  child: Text(
                    '${recommendation.estimatedMinutes} min',
                    style: const TextStyle(
                      color: Colors.white,
                      fontSize: 12,
                      fontWeight: FontWeight.w500,
                    ),
                  ),
                ),
              ],
            ),
            const SizedBox(height: 12),
            
            // Activity title with emoji
            Row(
              children: [
                Text(
                  _getActivityEmoji(recommendation.activityType),
                  style: const TextStyle(fontSize: 20),
                ),
                const SizedBox(width: 8),
                Expanded(
                  child: Text(
                    recommendation.primaryActivity,
                    style: const TextStyle(
                      color: Colors.white,
                      fontSize: 16,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                ),
              ],
            ),
            const SizedBox(height: 8),
            
            // Reason
            Text(
              recommendation.reason,
              style: TextStyle(
                color: Colors.white.withOpacity(0.9),
                fontSize: 14,
                height: 1.4,
              ),
            ),
            const SizedBox(height: 12),
            
            // Motivational message
            Container(
              width: double.infinity,
              padding: const EdgeInsets.all(12),
              decoration: BoxDecoration(
                color: Colors.white.withOpacity(0.1),
                borderRadius: BorderRadius.circular(10),
              ),
              child: Text(
                recommendation.motivationalMessage,
                style: TextStyle(
                  color: Colors.white.withOpacity(0.9),
                  fontSize: 14,
                  fontStyle: FontStyle.italic,
                  fontWeight: FontWeight.w500,
                ),
                textAlign: TextAlign.center,
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildInsightCard(WeeklyInsight insight) {
    Color insightColor = _getInsightTypeColor(insight.insightType);
    IconData insightIcon = _getInsightTypeIcon(insight.insightType);
    
    return Container(
      width: double.infinity,
      margin: const EdgeInsets.only(bottom: 16),
      padding: const EdgeInsets.all(20),
      decoration: BoxDecoration(
        color: const Color(0xFF2D2D42),
        borderRadius: BorderRadius.circular(16),
        border: Border(
          left: BorderSide(color: insightColor, width: 4),
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
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          // Insight header
          Row(
            children: [
              Container(
                width: 40,
                height: 40,
                decoration: BoxDecoration(
                  color: insightColor.withOpacity(0.2),
                  borderRadius: BorderRadius.circular(20),
                ),
                child: Icon(
                  insightIcon,
                  color: insightColor,
                  size: 20,
                ),
              ),
              const SizedBox(width: 12),
              Expanded(
                child: Text(
                  insight.title,
                  style: const TextStyle(
                    color: Colors.white,
                    fontSize: 16,
                    fontWeight: FontWeight.bold,
                  ),
                ),
              ),
            ],
          ),
          const SizedBox(height: 16),
          
          // Description
          Text(
            insight.description,
            style: const TextStyle(
              color: Colors.white70,
              fontSize: 14,
              height: 1.5,
            ),
          ),
          const SizedBox(height: 12),
          
          // Action suggestion
          Container(
            width: double.infinity,
            padding: const EdgeInsets.all(12),
            decoration: BoxDecoration(
              color: insightColor.withOpacity(0.1),
              borderRadius: BorderRadius.circular(10),
              border: Border.all(
                color: insightColor.withOpacity(0.3),
              ),
            ),
            child: Row(
              children: [
                Icon(
                  Icons.lightbulb_outline,
                  color: insightColor,
                  size: 16,
                ),
                const SizedBox(width: 8),
                Expanded(
                  child: Text(
                    insight.actionSuggestion,
                    style: TextStyle(
                      color: insightColor,
                      fontSize: 14,
                      fontWeight: FontWeight.w500,
                    ),
                  ),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }

  Color _getActivityTypeColor(String activityType) {
    switch (activityType) {
      case 'meditation':
        return const Color(0xFF4CAF50); // Green
      case 'cbt':
        return const Color(0xFF9C27B0); // Purple
      case 'mixed':
        return const Color(0xFF2196F3); // Blue
      default:
        return const Color(0xFF6C5CE7); // Default purple
    }
  }

  String _getActivityEmoji(String activityType) {
    switch (activityType) {
      case 'meditation':
        return '🧘';
      case 'cbt':
        return '🧠';
      case 'mixed':
        return '⚖️';
      default:
        return '🌟';
    }
  }

  Color _getInsightTypeColor(String insightType) {
    switch (insightType) {
      case 'mood_pattern':
        return Colors.blue;
      case 'streak_motivation':
        return Colors.orange;
      case 'activity_preference':
        return Colors.teal;
      default:
        return Colors.grey;
    }
  }

  IconData _getInsightTypeIcon(String insightType) {
    switch (insightType) {
      case 'mood_pattern':
        return Icons.mood;
      case 'streak_motivation':
        return Icons.local_fire_department;
      case 'activity_preference':
        return Icons.favorite;
      default:
        return Icons.insights;
    }
  }
} 