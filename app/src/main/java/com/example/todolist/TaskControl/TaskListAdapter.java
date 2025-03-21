package com.example.todolist.TaskControl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.todolist.R;

import java.util.ArrayList;

public class TaskListAdapter extends ArrayAdapter<Task> {
    private ArrayList<Task> tasks;

    public TaskListAdapter(Context context, ArrayList<Task> tasks){
        super(context, 0, tasks);
        this.tasks = tasks;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.task_item, parent, false);
        }

        Task task = tasks.get(position);
        TextView taskTitle = convertView.findViewById(R.id.taskTitle);
        taskTitle.setText(task.title);
        return convertView;
    }
}
