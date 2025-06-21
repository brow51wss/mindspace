package com.mindspace.app;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CBTExerciseAdapter extends RecyclerView.Adapter<CBTExerciseAdapter.ExerciseViewHolder> {
    private List<CBTExercisesActivity.CBTExercise> exercises;
    private OnExerciseClickListener clickListener;

    public interface OnExerciseClickListener {
        void onExerciseClick(CBTExercisesActivity.CBTExercise exercise);
    }

    public CBTExerciseAdapter(List<CBTExercisesActivity.CBTExercise> exercises, OnExerciseClickListener clickListener) {
        this.exercises = exercises;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cbt_exercise, parent, false);
        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        CBTExercisesActivity.CBTExercise exercise = exercises.get(position);
        
        holder.titleText.setText(exercise.getTitle());
        holder.descriptionText.setText(exercise.getDescription());
        holder.durationText.setText(exercise.getDuration());
        
        // Set card background color
        try {
            int color = Color.parseColor(exercise.getColor());
            GradientDrawable background = new GradientDrawable();
            background.setShape(GradientDrawable.RECTANGLE);
            background.setCornerRadius(24f);
            background.setColor(color);
            background.setAlpha(20); // Light transparency
            holder.cardView.setBackground(background);
            
            // Set accent color for duration badge
            GradientDrawable durationBg = new GradientDrawable();
            durationBg.setShape(GradientDrawable.RECTANGLE);
            durationBg.setCornerRadius(12f);
            durationBg.setColor(color);
            durationBg.setAlpha(40);
            holder.durationText.setBackground(durationBg);
            
        } catch (IllegalArgumentException e) {
            // Fallback to default color if parsing fails
            holder.cardView.setCardBackgroundColor(Color.parseColor("#F5F5F5"));
        }
        
        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onExerciseClick(exercise);
            }
        });
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView titleText;
        TextView descriptionText;
        TextView durationText;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.exerciseCard);
            titleText = itemView.findViewById(R.id.exerciseTitle);
            descriptionText = itemView.findViewById(R.id.exerciseDescription);
            durationText = itemView.findViewById(R.id.exerciseDuration);
        }
    }
} 