package com.example.todolist.TaskControl;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;

import java.util.ArrayList;
import java.util.List;

public class SubTaskAdapter extends RecyclerView.Adapter<SubTaskAdapter.TaskViewHolder> {
//    Create a list of sub task
    private List<SubTask> subTasks = new ArrayList<>();
//    Hold UI elements for each item
    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        EditText editText;
        public TaskViewHolder(View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox);
            editText = itemView.findViewById(R.id.editText);

        }
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_task_item, parent, false);
        return new TaskViewHolder(view);
    }


//    Bind data to the TaskViewHolder at the specified position
    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
//       Check if the position is within the bounds of tasks list
        if (position < subTasks.size()) {
//            Bind the existing task
//            Get the task at the current position
            SubTask subTask = subTasks.get(position);
//            Set checkbox state based on isChecked property of the task
            holder.checkBox.setChecked(subTask.isChecked);
//            Set task description in the EditText
            holder.editText.setText(subTask.description);

//            Update task description when user Enter
//            Set a Listener on EditText
            holder.editText.setOnEditorActionListener((v, actionId, event) -> {
//                Check if the key was pressed
                if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    int currentPosition = holder.getAdapterPosition();
//                    Update the description of the Task at the current position
                    if (currentPosition != RecyclerView.NO_POSITION && currentPosition < subTasks.size()) {
                        subTasks.get(currentPosition).description = v.getText().toString().trim();
                    }
                    return true;
                }
                return false;
            });
        } else {
                // Bind the add-new-task item
//            Set the visibility of the CheckBox to GONE
                holder.checkBox.setVisibility(View.GONE);
//                set editText to empty
                holder.editText.setText("");

                // Add a new task when user presses Enter
                holder.editText.setOnEditorActionListener((v, actionId, event) -> {
//                    If the Enter trigger and the text is not empty
                    if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                        String text = v.getText().toString().trim();
                        if (!text.isEmpty()) {
//                            Create new Task
                            SubTask newSubTask = new SubTask(text, false);
//                            Add into Task list
                            subTasks.add(newSubTask);
                            notifyItemInserted(subTasks.size() - 1); // Notify that a new task was added
                            v.setText(""); // Clear the input field
                        }
                        return true;
                    }
                    return false;
                });
            }

        }
    @Override
    public int getItemCount() {
        return subTasks.size() + 1;
    }

//    Save the task and display to main screen
}



