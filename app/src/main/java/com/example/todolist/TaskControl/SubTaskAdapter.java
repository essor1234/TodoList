package com.example.todolist.TaskControl;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;

import java.util.ArrayList;
import java.util.List;

public abstract class SubTaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<SubTask> subTasks = new ArrayList<>();
    private static final int TYPE_SUBTASK = 0;
    private static final int TYPE_INPUT = 1;
    private boolean isEditable = true; // Flag to switch between edit and display modes

    // Constructor for editable mode (AddTask)
    public SubTaskAdapter() {
        this.isEditable = true;
    }

    // Constructor for display mode (MainActivity)
    public SubTaskAdapter(boolean isEditable) {
        this.isEditable = isEditable;
    }

    // Set subtasks for display mode
    public void setSubTasks(List<SubTask> subTasks) {
        this.subTasks = new ArrayList<>(subTasks);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (isEditable && position == subTasks.size()) {
            return TYPE_INPUT; // Last item is for adding new subtask
        }
        return TYPE_SUBTASK; // Display existing subtasks
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_SUBTASK) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subtask_display_item, parent, false);
            return new SubTaskDisplayViewHolder(view);
        } else { // TYPE_INPUT
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_task_item, parent, false);
            return new SubTaskInputViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SubTaskDisplayViewHolder) {
            SubTask subTask = subTasks.get(position);
            SubTaskDisplayViewHolder displayHolder = (SubTaskDisplayViewHolder) holder;
            displayHolder.checkBox.setChecked(subTask.isChecked);
            displayHolder.textView.setText(subTask.description);

            // Update subtask checked status when checkbox is toggled
            displayHolder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                subTask.isChecked = isChecked;
            });
        } else if (holder instanceof SubTaskInputViewHolder) {
            SubTaskInputViewHolder inputHolder = (SubTaskInputViewHolder) holder;
            inputHolder.checkBox.setVisibility(View.GONE);
            inputHolder.editText.setText("");

            inputHolder.editText.setOnEditorActionListener((v, actionId, event) -> {
                if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    String text = v.getText().toString().trim();
                    if (!text.isEmpty()) {
                        SubTask newSubTask = new SubTask(text, false);
                        subTasks.add(newSubTask);
                        notifyItemInserted(subTasks.size() - 1);
                        v.setText("");
                        onSubTaskAdded(newSubTask);
                    }
                    return true;
                }
                return false;
            });
        }
    }

    @Override
    public int getItemCount() {
        return isEditable ? subTasks.size() + 1 : subTasks.size();
    }

    // ViewHolder for displaying subtasks (MainActivity)
    public static class SubTaskDisplayViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView textView;

        public SubTaskDisplayViewHolder(View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox);
            textView = itemView.findViewById(R.id.textView);
        }
    }

    // ViewHolder for input (AddTask)
    public static class SubTaskInputViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        EditText editText;

        public SubTaskInputViewHolder(View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox);
            editText = itemView.findViewById(R.id.editText);
        }
    }

    public abstract void onSubTaskAdded(SubTask subTask);
}