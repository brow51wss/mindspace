import 'package:flutter/material.dart';
import '../models/resource_models.dart';
import 'article_detail_screen.dart';

class ResourceHubScreen extends StatefulWidget {
  const ResourceHubScreen({super.key});

  @override
  State<ResourceHubScreen> createState() => _ResourceHubScreenState();
}

class _ResourceHubScreenState extends State<ResourceHubScreen> {
  late List<ResourceCategory> categories;

  @override
  void initState() {
    super.initState();
    categories = ResourceData.getCategories();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Mental Health Resources'),
        backgroundColor: Colors.blue,
        foregroundColor: Colors.white,
        elevation: 1,
      ),
      body: ListView.builder(
        padding: const EdgeInsets.all(16),
        itemCount: categories.length,
        itemBuilder: (context, index) {
          return _buildCategoryCard(categories[index], index);
        },
      ),
    );
  }

  Widget _buildCategoryCard(ResourceCategory category, int index) {
    return Card(
      margin: const EdgeInsets.only(bottom: 16),
      elevation: 2,
      child: Column(
        children: [
          ListTile(
            title: Text(
              category.title,
              style: const TextStyle(
                fontSize: 18,
                fontWeight: FontWeight.bold,
              ),
            ),
            subtitle: Text(
              category.description,
              style: const TextStyle(
                fontSize: 14,
                color: Colors.grey,
              ),
            ),
            trailing: Icon(
              category.isExpanded 
                ? Icons.expand_less 
                : Icons.expand_more,
              size: 28,
            ),
            onTap: () {
              setState(() {
                category.isExpanded = !category.isExpanded;
              });
            },
          ),
          if (category.isExpanded) ...[
            const Divider(height: 1),
            ...category.articles.map((article) => _buildArticleItem(article)),
          ],
        ],
      ),
    );
  }

  Widget _buildArticleItem(ResourceArticle article) {
    return ListTile(
      contentPadding: const EdgeInsets.symmetric(horizontal: 24, vertical: 8),
      title: Text(
        article.title,
        style: const TextStyle(
          fontSize: 16,
          fontWeight: FontWeight.w600,
        ),
      ),
      subtitle: Text(
        article.summary,
        style: const TextStyle(
          fontSize: 14,
          color: Colors.grey,
        ),
        maxLines: 2,
        overflow: TextOverflow.ellipsis,
      ),
      trailing: const Icon(
        Icons.arrow_forward_ios,
        size: 16,
        color: Colors.grey,
      ),
      onTap: () {
        Navigator.push(
          context,
          MaterialPageRoute(
            builder: (context) => ArticleDetailScreen(article: article),
          ),
        );
      },
    );
  }
}
