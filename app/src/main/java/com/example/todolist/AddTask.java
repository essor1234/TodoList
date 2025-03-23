package com.example.todolist;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.TaskControl.SubTask;
import com.example.todolist.TaskControl.SubTaskAdapter;
import com.example.todolist.TaskControl.Task;
import com.example.todolist.TaskControl.TaskAdapter;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class AddTask extends BottomSheetDialogFragment {
    private RecyclerView recyclerView;
    private SubTaskAdapter adapter;
    private EditText taskTitleInput;
    private Button saveButton;
    private ArrayList<SubTask> subTasks = new ArrayList<>(); // Temporary list for subtasks/i

//    Edit Task
    private Task taskToEdit; // Task to edit, null if adding

    // Static method to create instance for editing
    public static AddTask newInstance(Task task) {
        AddTask fragment = new AddTask();
        fragment.taskToEdit = task;
        return fragment;
    }


    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_add_task, container, false);

        taskTitleInput = view.findViewById(R.id.taskTitleInput);
        saveButton = view.findViewById(R.id.saveBtn);
        subTasks = new ArrayList<>();

        // Set up RecyclerView
        RecyclerView subTaskRecyclerView = view.findViewById(R.id.subTaskList);
        subTaskRecyclerView.setLayoutManager(new LinearLayoutManager(getContext())); // Add LayoutManager
//        adapter = new SubTaskAdapter(true); // Editable mode
//        adapter.setSubTasks(subTasks); // Pass the subTasks list to the adapter
//        subTaskRecyclerView.setAdapter(adapter);
        adapter = new SubTaskAdapter(true); // Editable mode
        subTaskRecyclerView.setAdapter(adapter);
        adapter.setSubTasks(subTasks); // Assuming this sets the initial list
        adapter.setOnSubTaskAddedListener(newSubTask -> {
            subTasks.add(newSubTask);
            adapter.notifyItemInserted(subTasks.size() - 1);
        });


        // If editing, pre-fill fields
        if (taskToEdit != null) {
            taskTitleInput.setText(taskToEdit.title);
            subTasks.addAll(taskToEdit.subTasks);
            adapter.setSubTasks(subTasks); // Update adapter with the populated list
        }

        // Save button logic
        saveButton.setOnClickListener(v -> {
            String title = taskTitleInput.getText().toString();
            if (!title.isEmpty()) {
                if (taskToEdit != null) {
                    // Update existing task
                    taskToEdit.title = title;
                    taskToEdit.subTasks = new ArrayList<>(subTasks);
                    int position = MainActivity.taskList.indexOf(taskToEdit);
                    if (position != -1) {
                        MainActivity.taskList.set(position, taskToEdit);
                        if (getActivity() instanceof MainActivity) {
                            ((MainActivity) getActivity()).adapter.notifyItemChanged(position);
                        }
                    }
                } else {
                    // Add new task
                    Task newTask = new Task(title, new ArrayList<>(subTasks));
                    MainActivity.taskList.add(newTask);
                    if (getActivity() instanceof MainActivity) {
                        ((MainActivity) getActivity()).adapter.notifyItemInserted(MainActivity.taskList.size() - 1);
                    }
                }
                dismiss(); // Close dialog
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Optional: Set height to 50% of screen
        View view = getView();
        if (view != null) {
            ViewGroup.LayoutParams params = view.getLayoutParams();
            params.height = (int) (getResources().getDisplayMetrics().heightPixels * 0.5);
            view.setLayoutParams(params);

        }
    }
}