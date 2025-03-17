package com.example.todolist;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.todolist.TaskControl.Task;
import com.example.todolist.TaskControl.SubTaskAdapter;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

//    Create a list of tasks
    public static ArrayList<Task> taskList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

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

    protected void onStart(){
        super.onStart();
        ListView lv = findViewById(R.id.tasksView);
        SubTaskAdapter adapter = new SubTaskAdapter(this, taskList);
    }
}