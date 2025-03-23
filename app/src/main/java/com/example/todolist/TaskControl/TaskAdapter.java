package com.example.todolist.TaskControl;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private List<Task> tasks;

    public TaskAdapter(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView textView;
        RecyclerView subTaskRecyclerView;
        SubTaskAdapter subTaskAdapter;

        public TaskViewHolder(View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.taskComplete);
            textView = itemView.findViewById(R.id.taskTitle);
            subTaskRecyclerView = itemView.findViewById(R.id.tasksView); // Initialize here
            subTaskRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
            subTaskAdapter = new SubTaskAdapter(false) { // Non-editable mode
                @Override
                public void onSubTaskAdded(SubTask subTask) {
                    // This won't be called in display mode, but keep it empty or remove it
                }
            };
            subTaskRecyclerView.setAdapter(subTaskAdapter);
        }
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.checkBox.setChecked(task.isCompleted);
        holder.textView.setText(task.title);
        holder.subTaskAdapter.setSubTasks(task.subTasks);

        // Update task completion status when checkbox is toggled
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            task.isCompleted = isChecked;
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }
}


