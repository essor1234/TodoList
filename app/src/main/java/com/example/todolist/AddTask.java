package com.example.todolist;

import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.TaskControl.TaskAdapter;


public class AddTask extends DialogFragment {
    private RecyclerView recyclerView;
    private TaskAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TaskAdapter();
        recyclerView.setAdapter(adapter);
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
//        Inflate the layout for the dialog
        return inflater.inflate(R.layout.activity_add_task, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Adjust the dialog's size when it starts
        Dialog dialog = getDialog();
        if (dialog != null) {
            // Get the screen dimensions
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int screenWidth = displayMetrics.widthPixels;
            int screenHeight = displayMetrics.heightPixels;

            // Set the dialog size: 80% of screen width and 50% of screen height
            int dialogWidth = (int) (screenWidth * 0.8);
            int dialogHeight = (int) (screenHeight * 0.5);
            dialog.getWindow().setLayout(dialogWidth, dialogHeight);
            }
        }


}