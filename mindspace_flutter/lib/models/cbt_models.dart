class CBTExercise {
  final String id;
  final String title;
  final String description;
  final String details;
  final String color;
  final String duration;

  CBTExercise({
    required this.id,
    required this.title,
    required this.description,
    required this.details,
    required this.color,
    required this.duration,
  });
}

class ExerciseStep {
  final String title;
  final String instruction;
  final String prompt;
  final String inputType; // "text" or "number"

  ExerciseStep({
    required this.title,
    required this.instruction,
    required this.prompt,
    required this.inputType,
  });
}

class CBTData {
  static List<CBTExercise> getCBTExercises() {
    return [
      // Exercise 1: Thought Challenging
      CBTExercise(
        id: 'thought_challenging',
        title: '🔄 Thought Challenging',
        description: 'Challenge negative thoughts with evidence-based questions',
        details: 'Learn to identify and question automatic negative thoughts that may be unrealistic or unhelpful.',
        color: '#4A90E2', // Blue
        duration: '10-15 min',
      ),

      // Exercise 2: Mood-Thought Connection
      CBTExercise(
        id: 'mood_thought_connection',
        title: '📝 Mood-Thought Connection',
        description: 'Explore the link between your feelings and thoughts',
        details: 'Understand how your thoughts influence your emotions and discover patterns in your thinking.',
        color: '#7B68EE', // Medium Slate Blue
        duration: '8-12 min',
      ),

      // Exercise 3: Evidence Examination
      CBTExercise(
        id: 'evidence_examination',
        title: '⚖️ Evidence Examination',
        description: 'Objectively evaluate your thoughts with facts',
        details: 'Learn to separate facts from opinions and examine evidence for and against your thoughts.',
        color: '#20B2AA', // Light Sea Green
        duration: '12-18 min',
      ),

      // Exercise 4: Positive Reframing
      CBTExercise(
        id: 'positive_reframing',
        title: '🌟 Positive Reframing',
        description: 'Transform negative self-talk into balanced thinking',
        details: 'Practice reframing negative thoughts into more balanced, realistic, and helpful perspectives.',
        color: '#FF6B6B', // Coral
        duration: '15-20 min',
      ),
    ];
  }

  static List<ExerciseStep> getExerciseSteps(String exerciseId) {
    switch (exerciseId) {
      case 'thought_challenging':
        return [
          ExerciseStep(
            title: 'Identify the Thought',
            instruction: 'Think about a negative or worrying thought you\'ve had recently. Write it down exactly as it appeared in your mind.',
            prompt: 'What negative thought have you been having?',
            inputType: 'text',
          ),
          ExerciseStep(
            title: 'Rate the Intensity',
            instruction: 'On a scale of 1-10, how much do you believe this thought right now? (1 = don\'t believe it at all, 10 = completely believe it)',
            prompt: 'Rate your belief in this thought (1-10):',
            inputType: 'number',
          ),
          ExerciseStep(
            title: 'Challenge the Thought',
            instruction: 'Ask yourself: Is this thought realistic? What evidence do I have for and against it?',
            prompt: 'What evidence supports this thought? What evidence contradicts it?',
            inputType: 'text',
          ),
          ExerciseStep(
            title: 'Create a Balanced Thought',
            instruction: 'Based on the evidence, write a more balanced and realistic version of your original thought.',
            prompt: 'Write a more balanced version of your thought:',
            inputType: 'text',
          ),
          ExerciseStep(
            title: 'Re-rate the Intensity',
            instruction: 'Now rate how much you believe the original thought (1-10). Has it changed?',
            prompt: 'Rate your belief in the original thought now (1-10):',
            inputType: 'number',
          ),
        ];

      case 'mood_thought_connection':
        return [
          ExerciseStep(
            title: 'Identify Your Current Mood',
            instruction: 'Take a moment to notice how you\'re feeling right now. Name the emotion you\'re experiencing.',
            prompt: 'What emotion are you feeling right now?',
            inputType: 'text',
          ),
          ExerciseStep(
            title: 'Rate the Intensity',
            instruction: 'How strong is this emotion on a scale of 1-10? (1 = very mild, 10 = extremely intense)',
            prompt: 'Rate the intensity of this emotion (1-10):',
            inputType: 'number',
          ),
          ExerciseStep(
            title: 'Identify the Trigger',
            instruction: 'What situation or event happened just before you started feeling this way?',
            prompt: 'What triggered this emotion?',
            inputType: 'text',
          ),
          ExerciseStep(
            title: 'Explore Your Thoughts',
            instruction: 'What thoughts went through your mind when the triggering event happened?',
            prompt: 'What thoughts did you have about the situation?',
            inputType: 'text',
          ),
          ExerciseStep(
            title: 'Make the Connection',
            instruction: 'Reflect on how your thoughts about the situation influenced your emotional response.',
            prompt: 'How do you think your thoughts influenced your emotions?',
            inputType: 'text',
          ),
        ];

      case 'evidence_examination':
        return [
          ExerciseStep(
            title: 'State Your Thought',
            instruction: 'Write down a specific thought or belief that\'s been bothering you.',
            prompt: 'What thought or belief would you like to examine?',
            inputType: 'text',
          ),
          ExerciseStep(
            title: 'Evidence For',
            instruction: 'List all the evidence that supports this thought. What facts make this thought seem true?',
            prompt: 'What evidence supports this thought?',
            inputType: 'text',
          ),
          ExerciseStep(
            title: 'Evidence Against',
            instruction: 'Now list all the evidence that contradicts this thought. What facts suggest it might not be true?',
            prompt: 'What evidence contradicts this thought?',
            inputType: 'text',
          ),
          ExerciseStep(
            title: 'Alternative Perspectives',
            instruction: 'What would you tell a good friend who had this same thought? What other ways could you look at this situation?',
            prompt: 'What alternative perspectives are possible?',
            inputType: 'text',
          ),
          ExerciseStep(
            title: 'Balanced Conclusion',
            instruction: 'Based on all the evidence, what\'s a more balanced and realistic conclusion?',
            prompt: 'What\'s a more balanced way to think about this?',
            inputType: 'text',
          ),
        ];

      case 'positive_reframing':
        return [
          ExerciseStep(
            title: 'Identify Negative Self-Talk',
            instruction: 'Think about something you\'ve been telling yourself that\'s negative or self-critical.',
            prompt: 'What negative thing have you been telling yourself?',
            inputType: 'text',
          ),
          ExerciseStep(
            title: 'Recognize the Impact',
            instruction: 'How does this negative self-talk make you feel? What emotions does it create?',
            prompt: 'How does this self-talk make you feel?',
            inputType: 'text',
          ),
          ExerciseStep(
            title: 'Find the Learning',
            instruction: 'What can you learn from this situation? Is there a growth opportunity hidden here?',
            prompt: 'What can you learn from this situation?',
            inputType: 'text',
          ),
          ExerciseStep(
            title: 'Reframe Positively',
            instruction: 'Rewrite your negative self-talk in a more compassionate, realistic, and encouraging way.',
            prompt: 'How can you reframe this more positively?',
            inputType: 'text',
          ),
          ExerciseStep(
            title: 'Practice the New Thought',
            instruction: 'How will you remind yourself to use this new, positive thought instead of the old negative one?',
            prompt: 'How will you practice this new way of thinking?',
            inputType: 'text',
          ),
        ];

      default:
        return [];
    }
  }
} 