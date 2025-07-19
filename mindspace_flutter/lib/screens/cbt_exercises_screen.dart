import 'package:flutter/material.dart';
import '../models/cbt_models.dart';
import 'cbt_exercise_screen.dart';

class CBTExercisesScreen extends StatefulWidget {
  @override
  _CBTExercisesScreenState createState() => _CBTExercisesScreenState();
}

class _CBTExercisesScreenState extends State<CBTExercisesScreen> {
  late List<CBTExercise> exercises;

  @override
  void initState() {
    super.initState();
    exercises = CBTData.getCBTExercises();
  }

  Color _parseColor(String colorString) {
    return Color(int.parse(colorString.replaceFirst('#', '0xFF')));
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xFF1E1E2E),
      appBar: AppBar(
        backgroundColor: const Color(0xFF2D2D42),
        title: const Text(
          'CBT Exercises',
          style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold),
        ),
        leading: IconButton(
          icon: const Icon(Icons.arrow_back, color: Colors.white),
          onPressed: () => Navigator.pop(context),
        ),
        elevation: 0,
      ),
      body: ListView.builder(
        padding: const EdgeInsets.all(16),
        itemCount: exercises.length,
        itemBuilder: (context, index) {
          return _buildExerciseCard(exercises[index]);
        },
      ),
    );
  }

  Widget _buildExerciseCard(CBTExercise exercise) {
    Color exerciseColor = _parseColor(exercise.color);
    
    return Container(
      margin: const EdgeInsets.only(bottom: 16),
      child: Material(
        color: Colors.transparent,
        child: InkWell(
          onTap: () => _onExerciseClick(exercise),
          borderRadius: BorderRadius.circular(16),
          child: Container(
            padding: const EdgeInsets.all(20),
            decoration: BoxDecoration(
              color: const Color(0xFF2D2D42),
              borderRadius: BorderRadius.circular(16),
              border: Border(
                left: BorderSide(
                  color: exerciseColor,
                  width: 6,
                ),
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
                Row(
                  children: [
                    Container(
                      width: 50,
                      height: 50,
                      decoration: BoxDecoration(
                        color: exerciseColor.withOpacity(0.2),
                        borderRadius: BorderRadius.circular(25),
                      ),
                      child: Center(
                        child: Text(
                          exercise.title.split(' ')[0], // Get emoji
                          style: const TextStyle(fontSize: 24),
                        ),
                      ),
                    ),
                    const SizedBox(width: 16),
                    Expanded(
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Text(
                            exercise.title.substring(2), // Remove emoji
                            style: const TextStyle(
                              color: Colors.white,
                              fontSize: 18,
                              fontWeight: FontWeight.bold,
                            ),
                          ),
                          const SizedBox(height: 4),
                          Container(
                            padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 2),
                            decoration: BoxDecoration(
                              color: exerciseColor.withOpacity(0.2),
                              borderRadius: BorderRadius.circular(8),
                            ),
                            child: Text(
                              exercise.duration,
                              style: TextStyle(
                                color: exerciseColor,
                                fontSize: 12,
                                fontWeight: FontWeight.bold,
                              ),
                            ),
                          ),
                        ],
                      ),
                    ),
                    Icon(
                      Icons.arrow_forward_ios,
                      color: Colors.white38,
                      size: 16,
                    ),
                  ],
                ),
                const SizedBox(height: 16),
                Text(
                  exercise.description,
                  style: const TextStyle(
                    color: Colors.white70,
                    fontSize: 16,
                    height: 1.4,
                  ),
                ),
                const SizedBox(height: 12),
                Text(
                  exercise.details,
                  style: const TextStyle(
                    color: Colors.white54,
                    fontSize: 14,
                    height: 1.3,
                  ),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }

  void _onExerciseClick(CBTExercise exercise) {
    Navigator.push(
      context,
      MaterialPageRoute(
        builder: (context) => CBTExerciseScreen(exercise: exercise),
      ),
    );
  }
} 