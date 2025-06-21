package com.mindspace.app;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AchievementAdapter extends RecyclerView.Adapter<AchievementAdapter.AchievementViewHolder> {
    
    private List<AchievementSystemActivity.Achievement> achievements;
    
    public AchievementAdapter(List<AchievementSystemActivity.Achievement> achievements) {
        this.achievements = achievements;
    }
    
    @NonNull
    @Override
    public AchievementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_achievement_badge, parent, false);
        return new AchievementViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull AchievementViewHolder holder, int position) {
        AchievementSystemActivity.Achievement achievement = achievements.get(position);
        
        holder.titleText.setText(achievement.title);
        holder.descriptionText.setText(achievement.description);
        holder.xpText.setText("+" + achievement.xpReward + " XP");
        
        // Style based on unlock status
        if (achievement.isUnlocked) {
            // Unlocked - Gold theme
            holder.cardView.setCardBackgroundColor(Color.parseColor("#FFD700")); // Gold
            holder.titleText.setTextColor(Color.parseColor("#8B4513")); // Dark brown
            holder.descriptionText.setTextColor(Color.parseColor("#A0522D")); // Saddle brown
            holder.xpText.setTextColor(Color.parseColor("#B8860B")); // Dark goldenrod
            holder.cardView.setCardElevation(8f);
            holder.cardView.setAlpha(1.0f);
        } else {
            // Locked - Gray theme
            holder.cardView.setCardBackgroundColor(Color.parseColor("#E0E0E0")); // Light gray
            holder.titleText.setTextColor(Color.parseColor("#757575")); // Gray
            holder.descriptionText.setTextColor(Color.parseColor("#9E9E9E")); // Light gray
            holder.xpText.setTextColor(Color.parseColor("#BDBDBD")); // Very light gray
            holder.cardView.setCardElevation(2f);
            holder.cardView.setAlpha(0.6f);
        }
        
        // Category-based accent colors for unlocked achievements
        if (achievement.isUnlocked) {
            switch (achievement.category) {
                case "mood":
                    holder.cardView.setCardBackgroundColor(Color.parseColor("#FFF8DC")); // Cornsilk
                    break;
                case "meditation":
                    holder.cardView.setCardBackgroundColor(Color.parseColor("#E6E6FA")); // Lavender
                    break;
                case "cbt":
                    holder.cardView.setCardBackgroundColor(Color.parseColor("#F0F8FF")); // Alice blue
                    break;
                case "special":
                    holder.cardView.setCardBackgroundColor(Color.parseColor("#FFD700")); // Gold
                    break;
            }
        }
    }
    
    @Override
    public int getItemCount() {
        return achievements.size();
    }
    
    static class AchievementViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView titleText;
        TextView descriptionText;
        TextView xpText;
        
        public AchievementViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            titleText = itemView.findViewById(R.id.achievementTitle);
            descriptionText = itemView.findViewById(R.id.achievementDescription);
            xpText = itemView.findViewById(R.id.achievementXP);
        }
    }
} 