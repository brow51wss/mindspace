import 'package:flutter/material.dart';
import 'dart:async';
import '../models/meditation_models.dart';

class MeditationPlayerScreen extends StatefulWidget {
  final MeditationSession session;

  MeditationPlayerScreen({required this.session});

  @override
  _MeditationPlayerScreenState createState() => _MeditationPlayerScreenState();
}

class _MeditationPlayerScreenState extends State<MeditationPlayerScreen> {
  bool isPlaying = false;
  bool isCompleted = false;
  int currentSeconds = 0;
  late int totalSeconds;
  Timer? timer;

  @override
  void initState() {
    super.initState();
    // Parse duration (e.g., "5 minutes" -> 300 seconds)
    totalSeconds = _parseDurationToSeconds(widget.session.duration);
  }

  @override
  void dispose() {
    timer?.cancel();
    super.dispose();
  }

  int _parseDurationToSeconds(String duration) {
    final regex = RegExp(r'(\d+)');
    final match = regex.firstMatch(duration);
    if (match != null) {
      int minutes = int.parse(match.group(1) ?? '5');
      return minutes * 60;
    }
    return 300; // Default 5 minutes
  }

  String _formatTime(int seconds) {
    int minutes = seconds ~/ 60;
    int remainingSeconds = seconds % 60;
    return '${minutes.toString().padLeft(2, '0')}:${remainingSeconds.toString().padLeft(2, '0')}';
  }

  void _togglePlayPause() {
    setState(() {
      isPlaying = !isPlaying;
    });

    if (isPlaying) {
      _startTimer();
    } else {
      _pauseTimer();
    }
  }

  void _startTimer() {
    timer = Timer.periodic(Duration(seconds: 1), (timer) {
      setState(() {
        currentSeconds++;
        if (currentSeconds >= totalSeconds) {
          isCompleted = true;
          isPlaying = false;
          timer.cancel();
        }
      });
    });
  }

  void _pauseTimer() {
    timer?.cancel();
  }

  void _resetSession() {
    timer?.cancel();
    setState(() {
      currentSeconds = 0;
      isPlaying = false;
      isCompleted = false;
    });
  }

  double get progress {
    if (totalSeconds == 0) return 0.0;
    return currentSeconds / totalSeconds;
  }

  Color _getLevelColor() {
    switch (widget.session.level.toLowerCase()) {
      case 'beginner':
        return Colors.green;
      case 'intermediate':
        return Colors.orange;
      case 'advanced':
        return Colors.red;
      default:
        return Colors.blue;
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xFF1E1E2E),
      appBar: AppBar(
        backgroundColor: const Color(0xFF2D2D42),
        title: const Text(
          'Meditation Player',
          style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold),
        ),
        leading: IconButton(
          icon: const Icon(Icons.arrow_back, color: Colors.white),
          onPressed: () => Navigator.pop(context),
        ),
        elevation: 0,
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(20),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            const SizedBox(height: 20),
            
            // Session Info
            Container(
              width: double.infinity,
              padding: const EdgeInsets.all(20),
              decoration: BoxDecoration(
                color: const Color(0xFF2D2D42),
                borderRadius: BorderRadius.circular(20),
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
                  Text(
                    widget.session.title,
                    style: const TextStyle(
                      color: Colors.white,
                      fontSize: 22,
                      fontWeight: FontWeight.bold,
                    ),
                    textAlign: TextAlign.center,
                  ),
                  const SizedBox(height: 10),
                  
                  Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      Container(
                        padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
                        decoration: BoxDecoration(
                          color: const Color(0xFF6C5CE7).withOpacity(0.2),
                          borderRadius: BorderRadius.circular(12),
                        ),
                        child: Text(
                          widget.session.duration,
                          style: const TextStyle(
                            color: Color(0xFF6C5CE7),
                            fontSize: 12,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                      ),
                      const SizedBox(width: 12),
                      Container(
                        padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
                        decoration: BoxDecoration(
                          color: _getLevelColor().withOpacity(0.2),
                          borderRadius: BorderRadius.circular(12),
                        ),
                        child: Text(
                          widget.session.level,
                          style: TextStyle(
                            color: _getLevelColor(),
                            fontSize: 12,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                      ),
                    ],
                  ),
                  const SizedBox(height: 12),
                  
                  Text(
                    widget.session.description,
                    style: const TextStyle(
                      color: Colors.white70,
                      fontSize: 14,
                      height: 1.4,
                    ),
                    textAlign: TextAlign.center,
                    maxLines: 3,
                    overflow: TextOverflow.ellipsis,
                  ),
                ],
              ),
            ),
            
            const SizedBox(height: 40),
            
            // Progress Circle
            Container(
              width: 180,
              height: 180,
              child: Stack(
                alignment: Alignment.center,
                children: [
                  // Progress Circle
                  SizedBox(
                    width: 180,
                    height: 180,
                    child: CircularProgressIndicator(
                      value: progress,
                      strokeWidth: 8,
                      backgroundColor: const Color(0xFF3D3D52),
                      valueColor: AlwaysStoppedAnimation<Color>(
                        isCompleted ? Colors.green : const Color(0xFF6C5CE7),
                      ),
                    ),
                  ),
                  
                  // Time Display
                  Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      Text(
                        _formatTime(currentSeconds),
                        style: const TextStyle(
                          color: Colors.white,
                          fontSize: 28,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                      Text(
                        '/ ${_formatTime(totalSeconds)}',
                        style: const TextStyle(
                          color: Colors.white54,
                          fontSize: 14,
                        ),
                      ),
                    ],
                  ),
                ],
              ),
            ),
            
            const SizedBox(height: 40),
            
            // Control Buttons
            Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                // Reset Button
                Container(
                  width: 50,
                  height: 50,
                  decoration: BoxDecoration(
                    color: const Color(0xFF3D3D52),
                    borderRadius: BorderRadius.circular(25),
                  ),
                  child: IconButton(
                    icon: const Icon(Icons.refresh, color: Colors.white, size: 20),
                    onPressed: _resetSession,
                  ),
                ),
                
                const SizedBox(width: 30),
                
                // Play/Pause Button
                Container(
                  width: 70,
                  height: 70,
                  decoration: BoxDecoration(
                    color: isCompleted 
                        ? Colors.green 
                        : const Color(0xFF6C5CE7),
                    borderRadius: BorderRadius.circular(35),
                    boxShadow: [
                      BoxShadow(
                        color: (isCompleted ? Colors.green : const Color(0xFF6C5CE7))
                            .withOpacity(0.3),
                        offset: const Offset(0, 4),
                        blurRadius: 12,
                      ),
                    ],
                  ),
                  child: IconButton(
                    icon: Icon(
                      isCompleted 
                          ? Icons.check 
                          : (isPlaying ? Icons.pause : Icons.play_arrow),
                      color: Colors.white,
                      size: 32,
                    ),
                    onPressed: isCompleted ? null : _togglePlayPause,
                  ),
                ),
                
                const SizedBox(width: 30),
                
                // Skip Forward Button
                Container(
                  width: 50,
                  height: 50,
                  decoration: BoxDecoration(
                    color: const Color(0xFF3D3D52),
                    borderRadius: BorderRadius.circular(25),
                  ),
                  child: IconButton(
                    icon: const Icon(Icons.skip_next, color: Colors.white, size: 20),
                    onPressed: () {
                      setState(() {
                        currentSeconds = (currentSeconds + 30).clamp(0, totalSeconds);
                        if (currentSeconds >= totalSeconds) {
                          isCompleted = true;
                          isPlaying = false;
                          timer?.cancel();
                        }
                      });
                    },
                  ),
                ),
              ],
            ),
            
            const SizedBox(height: 30),
            
            // Completion Message
            if (isCompleted)
              Container(
                width: double.infinity,
                padding: const EdgeInsets.all(16),
                decoration: BoxDecoration(
                  color: Colors.green.withOpacity(0.1),
                  borderRadius: BorderRadius.circular(16),
                  border: Border.all(color: Colors.green.withOpacity(0.3)),
                ),
                child: Column(
                  children: [
                    const Icon(
                      Icons.check_circle,
                      color: Colors.green,
                      size: 40,
                    ),
                    const SizedBox(height: 8),
                    const Text(
                      'Meditation Completed!',
                      style: TextStyle(
                        color: Colors.green,
                        fontSize: 16,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                    const SizedBox(height: 4),
                    const Text(
                      'Great job! You\'ve completed this meditation session.',
                      style: TextStyle(
                        color: Colors.white70,
                        fontSize: 12,
                      ),
                      textAlign: TextAlign.center,
                    ),
                  ],
                ),
              ),
          ],
        ),
      ),
    );
  }
} 