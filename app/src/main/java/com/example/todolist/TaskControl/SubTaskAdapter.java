package com.example.todolist.TaskControl;

import android.text.Editable;
import android.text.TextWatcher;
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
//    private static final int TYPE_SUBTASK = 0; // For existing subtasks
//    private static final int TYPE_INPUT = 1;   // For the input field

    private static final int TYPE_SUBTASK_DISPLAY = 0;
    private static final int TYPE_SUBTASK_EDIT = 1;
    private static final int TYPE_INPUT = 2;


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
        if (isEditable) {
            if (position < subTasks.size()) {
                return TYPE_SUBTASK_EDIT; // Existing subtasks in edit mode
            } else {
                return TYPE_INPUT; // Input field for new subtasks
            }
        } else {
            return TYPE_SUBTASK_DISPLAY; // Display mode for all subtasks
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_SUBTASK_DISPLAY) {
            View view = inflater.inflate(R.layout.subtask_display_item, parent, false);
            return new SubTaskDisplayViewHolder(view);
        } else { // TYPE_SUBTASK_EDIT or TYPE_INPUT
            View view = inflater.inflate(R.layout.sub_task_item, parent, false);
            return new SubTaskDisplayViewHolder.SubTaskEditViewHolder(view);
        }
    }

//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        if (holder instanceof SubTaskDisplayViewHolder) {
//            SubTask subTask = subTasks.get(position);
//            SubTaskDisplayViewHolder displayHolder = (SubTaskDisplayViewHolder) holder;
//            displayHolder.checkBox.setChecked(subTask.isChecked);
//            displayHolder.textView.setText(subTask.description);
//            displayHolder.checkBox.setOnCheckedChangeListener((button, isChecked) -> {
//                subTask.isChecked = isChecked;
//            });
//        } else if (holder instanceof SubTaskInputViewHolder) {
//            SubTaskInputViewHolder inputHolder = (SubTaskInputViewHolder) holder;
//            inputHolder.checkBox.setVisibility(View.GONE); // Hide checkbox
//            inputHolder.editText.setText(""); // Clear input field
//
//            // Add subtask when Enter is pressed
//            inputHolder.editText.setOnEditorActionListener((v, actionId, event) -> {
//                if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
//                    String text = v.getText().toString().trim();
//                    if (!text.isEmpty()) {
//                        SubTask newSubTask = new SubTask(text, false);
//                        subTasks.add(newSubTask);
//                        notifyItemInserted(subTasks.size() - 1);
//                        v.setText(""); // Clear input
//                        if (listener != null) {
//                            listener.onSubTaskAdded(newSubTask);
//                        }
//                    }
//                    return true;
//                }
//                return false;
//            });
//        }
//    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == TYPE_SUBTASK_DISPLAY) {
            SubTask subTask = subTasks.get(position);
            SubTaskDisplayViewHolder displayHolder = (SubTaskDisplayViewHolder) holder;
            displayHolder.checkBox.setChecked(subTask.isChecked);
            displayHolder.textView.setText(subTask.description);
            displayHolder.checkBox.setOnCheckedChangeListener((button, isChecked) -> subTask.isChecked = isChecked);
        } else if (viewType == TYPE_SUBTASK_EDIT) {
            SubTask subTask = subTasks.get(position);
            SubTaskDisplayViewHolder.SubTaskEditViewHolder editHolder = (SubTaskDisplayViewHolder.SubTaskEditViewHolder) holder;
            editHolder.bind(subTask, false, null);
        } else if (viewType == TYPE_INPUT) {
            SubTaskDisplayViewHolder.SubTaskEditViewHolder inputHolder = (SubTaskDisplayViewHolder.SubTaskEditViewHolder) holder;
            inputHolder.bind(null, true, listener);
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
        static class SubTaskEditViewHolder extends RecyclerView.ViewHolder {
            CheckBox checkBox;
            EditText editText;
            SubTask currentSubTask;
            TextWatcher textWatcher;

            SubTaskEditViewHolder(View itemView) {
                super(itemView);
                checkBox = itemView.findViewById(R.id.checkBox);
                editText = itemView.findViewById(R.id.editText);
                textWatcher = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}
                    @Override
                    public void afterTextChanged(Editable s) {
                        if (currentSubTask != null) {
                            currentSubTask.description = s.toString();
                        }
                    }
                };
            }

            void bind(SubTask subTask, boolean isInput, SubTaskAdapter.OnSubTaskAddedListener listener) {
                currentSubTask = subTask;
                if (isInput) {
                    checkBox.setVisibility(View.GONE);
                    editText.setText("");
                    editText.setOnEditorActionListener((v, actionId, event) -> {
                        if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                            String text = v.getText().toString().trim();
                            if (!text.isEmpty() && listener != null) {
                                listener.onSubTaskAdded(new SubTask(text, false));
                                v.setText("");
                            }
                            return true;
                        }
                        return false;
                    });
                    editText.removeTextChangedListener(textWatcher);
                } else {
                    checkBox.setVisibility(View.VISIBLE);
                    checkBox.setChecked(subTask.isChecked);
                    editText.setText(subTask.description);
                    editText.addTextChangedListener(textWatcher);
                    checkBox.setOnCheckedChangeListener((button, isChecked) -> subTask.isChecked = isChecked);
                    editText.setOnEditorActionListener(null);
                }
            }
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

