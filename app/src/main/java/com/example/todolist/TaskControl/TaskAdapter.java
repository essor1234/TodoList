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

public  class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
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
        }
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position){
        Task task = tasks.get(position);
        holder.checkBox.setChecked(task.isCompleted);
        holder.textView.setText(task.title);

        holder.subTaskAdapter = new SubTaskAdapter(){
            @Override
            public void onSubTaskAdded(SubTask subTask) {
                task.subTasks.add(subTask);
            }
        };
        holder.subTaskRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.subTaskRecyclerView.setAdapter(holder.subTaskAdapter);
        holder.subTaskAdapter.setSubTasks(task.subTasks);


    }
    @Override
    public int getItemCount() {
        return tasks.size();
    }



}
