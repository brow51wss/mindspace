import 'package:flutter/material.dart';
import '../models/resource_models.dart';

class ArticleDetailScreen extends StatelessWidget {
  final ResourceArticle article;

  const ArticleDetailScreen({super.key, required this.article});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(article.title),
        backgroundColor: Colors.blue,
        foregroundColor: Colors.white,
        elevation: 1,
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // Article Title
            Text(
              article.title,
              style: const TextStyle(
                fontSize: 24,
                fontWeight: FontWeight.bold,
                height: 1.2,
              ),
            ),
            const SizedBox(height: 16),
            
            // Article Content
            Text(
              article.content,
              style: const TextStyle(
                fontSize: 16,
                height: 1.5,
                color: Colors.black87,
              ),
            ),
            
            const SizedBox(height: 32),
            
            // Bottom spacing for better scrolling
            Container(
              height: 1,
              color: Colors.transparent,
            ),
          ],
        ),
      ),
    );
  }
}
