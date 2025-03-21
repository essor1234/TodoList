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
import com.example.todolist.TaskControl.TaskListAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class AddTask extends BottomSheetDialogFragment {
    private RecyclerView recyclerView;
    private SubTaskAdapter adapter;
    private EditText taskTitleInput;
    private Button saveButton;
    private ArrayList<SubTask> subTasks = new ArrayList<>(); // Temporary list for subtasks

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for the bottom sheet
        View view = inflater.inflate(R.layout.activity_add_task, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.subTaskList);
        taskTitleInput = view.findViewById(R.id.taskTitleInput);
        saveButton = view.findViewById(R.id.saveBtn);


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new SubTaskAdapter() {
            @Override
            public void onSubTaskAdded(SubTask subTask) {
                subTasks.add(subTask); // Add to temporary list instead of MainActivity.taskList
            }
        };
        recyclerView.setAdapter(adapter);

        // Set up save button
        saveButton.setOnClickListener(v -> {
            String title = taskTitleInput.getText().toString();
            if (!title.isEmpty()) {
                // Create task with title and subtasks
                Task newTask = new Task(title, new ArrayList<>(subTasks));
                MainActivity.taskList.add(newTask); // Add to MainActivity.taskList
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).adapter.notifyDataSetChanged();
                }
                dismiss();
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

//        Dialog dialog = getDialog();
//        if (dialog != null && dialog.getWindow() != null) {
//            DisplayMetrics displayMetrics = new DisplayMetrics();
//            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//            int screenWidth = displayMetrics.widthPixels;
//            int screenHeight = displayMetrics.heightPixels;
//            int dialogWidth = (int) (screenWidth * 0.8);
//            int dialogHeight = (int) (screenHeight * 0.5);
//            dialog.getWindow().setLayout(dialogWidth, dialogHeight);
        }
    }
}