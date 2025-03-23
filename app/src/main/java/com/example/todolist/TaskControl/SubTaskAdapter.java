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

public class SubTaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_SUBTASK = 0; // For existing subtasks
    private static final int TYPE_INPUT = 1;   // For the input field
    private final boolean isEditable;
    private List<SubTask> subTasks = new ArrayList<>();
    private OnSubTaskAddedListener listener;

    public SubTaskAdapter(boolean isEditable) {
        this.isEditable = isEditable;
    }

    public void setSubTasks(List<SubTask> subTasks) {
        this.subTasks = subTasks;
        notifyDataSetChanged();
    }

    public void setOnSubTaskAddedListener(OnSubTaskAddedListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        // In editable mode, the last item is the input field
        if (isEditable && position == subTasks.size()) {
            return TYPE_INPUT;
        }
        return TYPE_SUBTASK;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_SUBTASK) {
            View view = inflater.inflate(R.layout.subtask_display_item, parent, false);
            return new SubTaskDisplayViewHolder(view);
        } else { // TYPE_INPUT
            View view = inflater.inflate(R.layout.sub_task_item, parent, false);
            return new SubTaskInputViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SubTaskDisplayViewHolder) {
            SubTask subTask = subTasks.get(position);
            SubTaskDisplayViewHolder displayHolder = (SubTaskDisplayViewHolder) holder;
            displayHolder.checkBox.setChecked(subTask.isChecked);
            displayHolder.textView.setText(subTask.description);
            displayHolder.checkBox.setOnCheckedChangeListener((button, isChecked) -> {
                subTask.isChecked = isChecked;
            });
        } else if (holder instanceof SubTaskInputViewHolder) {
            SubTaskInputViewHolder inputHolder = (SubTaskInputViewHolder) holder;
            inputHolder.checkBox.setVisibility(View.GONE); // Hide checkbox
            inputHolder.editText.setText(""); // Clear input field

            // Add subtask when Enter is pressed
            inputHolder.editText.setOnEditorActionListener((v, actionId, event) -> {
                if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    String text = v.getText().toString().trim();
                    if (!text.isEmpty()) {
                        SubTask newSubTask = new SubTask(text, false);
                        subTasks.add(newSubTask);
                        notifyItemInserted(subTasks.size() - 1);
                        v.setText(""); // Clear input
                        if (listener != null) {
                            listener.onSubTaskAdded(newSubTask);
                        }
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

    // ViewHolder classes
    static class SubTaskDisplayViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView textView;

        SubTaskDisplayViewHolder(View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox);
            textView = itemView.findViewById(R.id.textView);
        }
    }

    static class SubTaskInputViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        EditText editText;

        SubTaskInputViewHolder(View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox);
            editText = itemView.findViewById(R.id.editText);
        }
    }

    // Listener interface
    public interface OnSubTaskAddedListener {
        void onSubTaskAdded(SubTask subTask);
    }
}

