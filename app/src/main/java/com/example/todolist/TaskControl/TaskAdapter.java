package com.example.todolist.TaskControl;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.AddTask;
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
        Button deleteBtn;
        Button editBtn;


        public TaskViewHolder(View itemView) {
            super(itemView);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
            editBtn = itemView.findViewById(R.id.editBtn);
            checkBox = itemView.findViewById(R.id.taskComplete);
            textView = itemView.findViewById(R.id.taskTitle);
            subTaskRecyclerView = itemView.findViewById(R.id.tasksView); // Initialize here
            subTaskRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
            subTaskAdapter = new SubTaskAdapter(true); // Editable mode
            subTaskAdapter = new SubTaskAdapter(false); // Non-editable mode
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

        holder.deleteBtn.setOnClickListener(v -> {
            tasks.remove(position); // Remove task from list
            notifyItemRemoved(position); // Notify adapter of removal
            notifyItemRangeChanged(position, tasks.size()); // Update remaining items
        });

        holder.editBtn.setOnClickListener(v -> {
            AddTask bottomSheet = AddTask.newInstance(task);
            bottomSheet.show(((AppCompatActivity) holder.itemView.getContext()).getSupportFragmentManager(), "EditTaskBottomSheet");
        });

    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }
}


