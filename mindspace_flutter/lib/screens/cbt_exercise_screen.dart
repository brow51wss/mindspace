import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import '../models/cbt_models.dart';

class CBTExerciseScreen extends StatefulWidget {
  final CBTExercise exercise;

  CBTExerciseScreen({required this.exercise});

  @override
  _CBTExerciseScreenState createState() => _CBTExerciseScreenState();
}

class _CBTExerciseScreenState extends State<CBTExerciseScreen> {
  late List<ExerciseStep> steps;
  late List<String> userResponses;
  late List<TextEditingController> controllers;
  int currentStep = 0;

  @override
  void initState() {
    super.initState();
    _setupExercise();
  }

  @override
  void dispose() {
    for (var controller in controllers) {
      controller.dispose();
    }
    super.dispose();
  }

  void _setupExercise() {
    steps = CBTData.getExerciseSteps(widget.exercise.id);
    userResponses = List.filled(steps.length, '');
    controllers = List.generate(steps.length, (_) => TextEditingController());
    
    // Load any saved responses if needed (for future feature)
    for (int i = 0; i < controllers.length; i++) {
      controllers[i].addListener(() {
        userResponses[i] = controllers[i].text;
      });
    }
  }

  Color _parseColor(String colorString) {
    return Color(int.parse(colorString.replaceFirst('#', '0xFF')));
  }

  @override
  Widget build(BuildContext context) {
    Color exerciseColor = _parseColor(widget.exercise.color);
    
    return Scaffold(
      backgroundColor: const Color(0xFF1E1E2E),
      appBar: AppBar(
        backgroundColor: const Color(0xFF2D2D42),
        title: Text(
          widget.exercise.title.substring(2), // Remove emoji
          style: const TextStyle(color: Colors.white, fontWeight: FontWeight.bold),
        ),
        leading: IconButton(
          icon: const Icon(Icons.arrow_back, color: Colors.white),
          onPressed: () => Navigator.pop(context),
        ),
        elevation: 0,
      ),
      body: Column(
        children: [
          // Progress Bar
          Container(
            padding: const EdgeInsets.all(20),
            color: const Color(0xFF2D2D42),
            child: Column(
              children: [
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    Text(
                      'Step ${currentStep + 1} of ${steps.length}',
                      style: const TextStyle(
                        color: Colors.white70,
                        fontSize: 16,
                        fontWeight: FontWeight.w600,
                      ),
                    ),
                    Text(
                      steps[currentStep].title,
                      style: const TextStyle(
                        color: Colors.white,
                        fontSize: 16,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                  ],
                ),
                const SizedBox(height: 12),
                LinearProgressIndicator(
                  value: (currentStep + 1) / steps.length,
                  backgroundColor: Colors.white24,
                  valueColor: AlwaysStoppedAnimation<Color>(exerciseColor),
                  minHeight: 4,
                ),
              ],
            ),
          ),
          
          // Main Content
          Expanded(
            child: SingleChildScrollView(
              padding: const EdgeInsets.all(20),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  // Step Content Card
                  Container(
                    width: double.infinity,
                    padding: const EdgeInsets.all(24),
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
                        Text(
                          steps[currentStep].title,
                          style: const TextStyle(
                            color: Colors.white,
                            fontSize: 20,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                        const SizedBox(height: 16),
                        Text(
                          steps[currentStep].instruction,
                          style: const TextStyle(
                            color: Colors.white70,
                            fontSize: 16,
                            height: 1.5,
                          ),
                        ),
                      ],
                    ),
                  ),
                  
                  const SizedBox(height: 24),
                  
                  // Input Field
                  Container(
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
                        Text(
                          steps[currentStep].prompt,
                          style: const TextStyle(
                            color: Colors.white,
                            fontSize: 16,
                            fontWeight: FontWeight.w600,
                          ),
                        ),
                        const SizedBox(height: 12),
                        _buildInputField(steps[currentStep].inputType),
                      ],
                    ),
                  ),
                  
                  const SizedBox(height: 30),
                ],
              ),
            ),
          ),
          
          // Navigation Buttons
          Container(
            padding: const EdgeInsets.all(20),
            color: const Color(0xFF2D2D42),
            child: Row(
              children: [
                // Previous Button
                if (currentStep > 0)
                  Expanded(
                    child: ElevatedButton(
                      onPressed: _previousStep,
                      style: ElevatedButton.styleFrom(
                        backgroundColor: Colors.white24,
                        foregroundColor: Colors.white,
                        padding: const EdgeInsets.symmetric(vertical: 16),
                        shape: RoundedRectangleBorder(
                          borderRadius: BorderRadius.circular(12),
                        ),
                      ),
                      child: const Text(
                        'Previous',
                        style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
                      ),
                    ),
                  ),
                
                if (currentStep > 0) const SizedBox(width: 12),
                
                // Next/Finish Button
                Expanded(
                  child: ElevatedButton(
                    onPressed: _nextStep,
                    style: ElevatedButton.styleFrom(
                      backgroundColor: exerciseColor,
                      foregroundColor: Colors.white,
                      padding: const EdgeInsets.symmetric(vertical: 16),
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(12),
                      ),
                    ),
                    child: Text(
                      currentStep < steps.length - 1 ? 'Next' : 'Finish',
                      style: const TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
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

  Widget _buildInputField(String inputType) {
    if (inputType == 'number') {
      return TextField(
        controller: controllers[currentStep],
        keyboardType: TextInputType.number,
        inputFormatters: [
          FilteringTextInputFormatter.digitsOnly,
          LengthLimitingTextInputFormatter(2),
        ],
        style: const TextStyle(color: Colors.white, fontSize: 16),
        decoration: InputDecoration(
          hintText: 'Enter a number from 1 to 10',
          hintStyle: const TextStyle(color: Colors.white38),
          filled: true,
          fillColor: Colors.white10,
          border: OutlineInputBorder(
            borderRadius: BorderRadius.circular(12),
            borderSide: BorderSide.none,
          ),
          contentPadding: const EdgeInsets.symmetric(horizontal: 16, vertical: 16),
        ),
      );
    } else {
      return TextField(
        controller: controllers[currentStep],
        maxLines: 4,
        style: const TextStyle(color: Colors.white, fontSize: 16),
        decoration: InputDecoration(
          hintText: 'Share your thoughts here...',
          hintStyle: const TextStyle(color: Colors.white38),
          filled: true,
          fillColor: Colors.white10,
          border: OutlineInputBorder(
            borderRadius: BorderRadius.circular(12),
            borderSide: BorderSide.none,
          ),
          contentPadding: const EdgeInsets.all(16),
        ),
      );
    }
  }

  void _previousStep() {
    if (currentStep > 0) {
      setState(() {
        currentStep--;
      });
    }
  }

  void _nextStep() {
    if (_validateCurrentStep()) {
      if (currentStep < steps.length - 1) {
        setState(() {
          currentStep++;
        });
      } else {
        _finishExercise();
      }
    }
  }

  bool _validateCurrentStep() {
    String response = controllers[currentStep].text.trim();
    
    if (response.isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          content: Text('Please provide a response before continuing.'),
          backgroundColor: Colors.red,
        ),
      );
      return false;
    }
    
    if (steps[currentStep].inputType == 'number') {
      int? value = int.tryParse(response);
      if (value == null || value < 1 || value > 10) {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(
            content: Text('Please enter a number between 1 and 10.'),
            backgroundColor: Colors.red,
          ),
        );
        return false;
      }
    }
    
    return true;
  }

  void _finishExercise() {
    // Save exercise completion (could integrate with achievements/streaks)
    // For now, just show completion message
    
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          backgroundColor: const Color(0xFF2D2D42),
          title: const Text(
            'Exercise Completed!',
            style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold),
          ),
          content: const Text(
            'Great work! You\'ve completed this CBT exercise. Regular practice can help you develop healthier thought patterns.',
            style: TextStyle(color: Colors.white70),
          ),
          actions: [
            TextButton(
              onPressed: () {
                Navigator.of(context).pop(); // Close dialog
                Navigator.of(context).pop(); // Go back to exercises list
              },
              child: Text(
                'Done',
                style: TextStyle(
                  color: _parseColor(widget.exercise.color),
                  fontWeight: FontWeight.bold,
                ),
              ),
            ),
          ],
        );
      },
    );
  }
} 