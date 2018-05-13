package ru.kondratj3v.tltsusch.adapters;


import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

import ru.kondratj3v.tltsusch.R;
import ru.kondratj3v.tltsusch.models.Lesson;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {
    private List<Lesson> lessons;
    private Context context;

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView number;
        private TextView time;
        private TextView type;
        private TextView room;
        private TextView name;
        private TextView teacher;

        ViewHolder(View itemView) {
            super(itemView);
            number = (TextView) itemView.findViewById(R.id.lesson_list_item_number);
            time = (TextView) itemView.findViewById(R.id.lesson_list_item_time);
            type = (TextView) itemView.findViewById(R.id.lesson_list_item_type);
            room = (TextView) itemView.findViewById(R.id.lesson_list_item_room);
            name = (TextView) itemView.findViewById(R.id.lesson_list_item_name);
            teacher = (TextView) itemView.findViewById(R.id.lesson_list_item_teacher);
        }
    }

    public ScheduleAdapter(Context context, List<Lesson> lessons) {
        this.lessons = lessons;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.lesson_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Lesson lesson = lessons.get(position);
        holder.number.setText(lesson.getNumber());
        holder.time.setText(lesson.getTime());
        holder.type.setText(lesson.getType());
        holder.room.setText(lesson.getRoom());
        holder.name.setText(lesson.getName());
        holder.teacher.setText(lesson.getTeacher());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Objects.equals(lesson.getType(), "Лек")) {
                holder.type.setText("Лекция");
                holder.type.setTextColor(ContextCompat.getColor(context, R.color.colorLecture));
            } else if (Objects.equals(lesson.getType(), "Пр")) {
                holder.type.setText("Практика");
                holder.type.setTextColor(ContextCompat.getColor(context, R.color.colorPractice));
            } else if (Objects.equals(lesson.getType(), "Лаб")) {
                holder.type.setText("Лабораторная");
                holder.type.setTextColor(ContextCompat.getColor(context, R.color.colorLab));
            } else if (Objects.equals(lesson.getType(), "Тест")) {
                holder.type.setText("Лабораторная");
                holder.type.setTextColor(ContextCompat.getColor(context, R.color.colorPractice));
            }
        }
    }

    @Override
    public int getItemCount() {
        return lessons.size();
    }
}