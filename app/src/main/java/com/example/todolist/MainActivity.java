package com.example.todolist;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.TaskControl.Task;
import com.example.todolist.TaskControl.TaskAdapter;
import com.example.todolist.TaskControl.TaskListAdapter;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

//    Create a list of tasks
    public static ArrayList<Task> taskList = new ArrayList<>();
    private RecyclerView recyclerView;
    TaskAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Initialize ListView and adapter
        recyclerView = findViewById(R.id.taskList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TaskAdapter(taskList);
        recyclerView.setAdapter(adapter);


        // Find the "+" button and set click listener
        Button addButton = findViewById(R.id.addBtn);
        addButton.setOnClickListener(v -> {
            AddTask bottomSheet = new AddTask();
            bottomSheet.show(getSupportFragmentManager(), "AddTaskBottomSheet");
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

//    Refresh this list when the activity starts
    protected void onStart(){
        super.onStart();
        adapter.notifyDataSetChanged();
    }
}