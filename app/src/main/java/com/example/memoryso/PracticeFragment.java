package com.example.memoryso;

import android.graphics.Typeface;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PracticeFragment extends Fragment {
    private EditText memorySizeInput;
    private EditText numProcessesInput;
    private EditText numOperationsInput;
    private LinearLayout processSizesLayout;
    private LinearLayout operationSizesLayout;
    private LinearLayout visualizationLayout;
    private Button allocateButton;
    private RadioGroup algorithmRadioGroup;
    private int selectedAlgorithm = 0;
    private int nextFitIndex = 0;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public PracticeFragment() {
        // Required empty public constructor
    }

    public static PracticeFragment newInstance(String param1, String param2) {
        PracticeFragment fragment = new PracticeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_practice, container, false);

        memorySizeInput = rootView.findViewById(R.id.memory_size_input);
        numProcessesInput = rootView.findViewById(R.id.num_processes_input);
        numOperationsInput = rootView.findViewById(R.id.num_operations_input);
        processSizesLayout = rootView.findViewById(R.id.process_sizes_layout);
        operationSizesLayout = rootView.findViewById(R.id.operation_sizes_layout);
        visualizationLayout = rootView.findViewById(R.id.visualization_layout);
        allocateButton = rootView.findViewById(R.id.allocate_button);
        algorithmRadioGroup = rootView.findViewById(R.id.algorithm_radio_group);

        allocateButton.setOnClickListener(v -> {
            if (validateInputs()) {
                int memorySize = Integer.parseInt(memorySizeInput.getText().toString());
                int numProcesses = Integer.parseInt(numProcessesInput.getText().toString());
                int numOperations = Integer.parseInt(numOperationsInput.getText().toString());
                List<Integer> processSizes = getProcessSizes(processSizesLayout);
                List<String> operations = getOperations(operationSizesLayout);
                performMemoryAllocation(memorySize, processSizes, operations, selectedAlgorithm);
            }
        });

        numProcessesInput.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateProcessSizeInputs();
            }
        });

        numOperationsInput.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateOperationInputs();
            }
        });

        algorithmRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.first_fit_radio) {
                selectedAlgorithm = 0;
            } else if (checkedId == R.id.best_fit_radio) {
                selectedAlgorithm = 1;
            } else if (checkedId == R.id.worst_fit_radio) {
                selectedAlgorithm = 2;
            } else if (checkedId == R.id.next_fit_radio) {
                selectedAlgorithm = 3;
            }
        });

        return rootView;
    }

    private boolean validateInputs() {
        return !isEmpty(memorySizeInput) && !isEmpty(numProcessesInput) && !isEmpty(numOperationsInput);
    }

    private void updateProcessSizeInputs() {
        int numProcesses = getIntegerFromEditText(numProcessesInput);
        processSizesLayout.removeAllViews();
        for (int i = 0; i < numProcesses; i++) {
            TextView processSizeLabel = createLabel("Process " + (i + 1) + " Size:");
            EditText processSizeInput = createEditText("Enter size for Process " + (i + 1));
            processSizesLayout.addView(processSizeLabel);
            processSizesLayout.addView(processSizeInput);
        }
    }

    private void updateOperationInputs() {
        int numOperations = getIntegerFromEditText(numOperationsInput);
        operationSizesLayout.removeAllViews();
        for (int i = 0; i < numOperations; i++) {
            TextView operationLabel = createLabel("Operation " + (i + 1) + ":");
            EditText operationSizeInput = createEditText("Enter size for Operation " + (i + 1));
            // Remove this line to avoid setting the input type to numeric
            // operationSizeInput.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
            operationSizesLayout.addView(operationLabel);
            operationSizesLayout.addView(operationSizeInput);
        }
    }


    private List<Integer> getProcessSizes(LinearLayout layout) {
        List<Integer> processSizes = new ArrayList<>();
        for (int i = 0; i < layout.getChildCount(); i += 2) {
            EditText processSizeInput = (EditText) layout.getChildAt(i + 1);
            processSizes.add(Integer.parseInt(processSizeInput.getText().toString()));
        }
        return processSizes;
    }

    private List<String> getOperations(LinearLayout layout) {
        List<String> operations = new ArrayList<>();
        for (int i = 0; i < layout.getChildCount(); i += 2) {
            EditText operationInput = (EditText) layout.getChildAt(i + 1);
            String operation = operationInput.getText().toString().trim();
            operations.add(operation);
        }
        return operations;
    }

    private void performMemoryAllocation(int memorySize, List<Integer> processSizes, List<String> operations, int algorithm) {
        StringBuilder allocationStatus = new StringBuilder();
        allocationStatus.append("Memory Size: ").append(memorySize).append("\n");
        allocationStatus.append("Process Sizes:\n");
        for (int i = 0; i < processSizes.size(); i++) {
            allocationStatus.append("Process ").append(i + 1).append(": ").append(processSizes.get(i)).append("\n");
        }
        allocationStatus.append("Operations:\n");
        for (int i = 0; i < operations.size(); i++) {
            allocationStatus.append("Operation ").append(i + 1).append(": ").append(operations.get(i)).append("\n");
        }
        allocationStatus.append("Selected Algorithm: ");
        switch (algorithm) {
            case 0:
                allocationStatus.append("First Fit");
                firstFit(memorySize, processSizes, operations);
                break;
            case 1:
                allocationStatus.append("Best Fit");
                bestFit(memorySize, processSizes, operations);
                break;
            case 2:
                allocationStatus.append("Worst Fit");
                worstFit(memorySize, processSizes, operations);
                break;
            case 3:
                allocationStatus.append("Next Fit");
                nextFit(memorySize, processSizes, operations);
                break;
        }
        TextView allocationStatusTextView = getView().findViewById(R.id.allocation_status_text_view);
        if (allocationStatusTextView != null) {
            allocationStatusTextView.setText(allocationStatus.toString());
        }
    }

    private void firstFit(int memorySize, List<Integer> processSizes, List<String> operations) {
        List<Integer> memory = new ArrayList<>(Collections.nCopies(memorySize, 0));
        int step = 1;
        for (String operation : operations) {
            if (operation.startsWith("+p")) {
                int processIndex = Integer.parseInt(operation.substring(2)) - 1;
                int processSize = processSizes.get(processIndex);
                boolean allocated = false;
                for (int i = 0; i < memory.size(); i++) {
                    if (memory.get(i) == 0 && memory.size() - i >= processSize) {
                        for (int j = i; j < i + processSize; j++) {
                            memory.set(j, processSize);
                        }
                        allocated = true;
                        break;
                    }
                }
                if (!allocated) {
                    // Process could not be allocated
                }
                visualizeStep(memorySize, memory, step++);
            } else if (operation.startsWith("-p")) {
                int processIndex = Integer.parseInt(operation.substring(2)) - 1;
                int processSize = processSizes.get(processIndex);
                for (int i = 0; i < memory.size(); i++) {
                    if (memory.get(i) == processSize) {
                        for (int j = i; j < i + processSize; j++) {
                            memory.set(j, 0);
                        }
                        break;
                    }
                }
                visualizeStep(memorySize, memory, step++);
            }
        }
    }

    private void bestFit(int memorySize, List<Integer> processSizes, List<String> operations) {
        List<Integer> memory = new ArrayList<>(Collections.nCopies(memorySize, 0));
        int step = 1;
        for (String operation : operations) {
            if (operation.startsWith("+p")) {
                int processIndex = Integer.parseInt(operation.substring(2)) - 1;
                int processSize = processSizes.get(processIndex);
                int bestFitIndex = -1;
                int bestFitSize = Integer.MAX_VALUE;
                for (int i = 0; i < memory.size(); i++) {
                    if (memory.get(i) == 0) {
                        int j = i;
                        while (j < memory.size() && memory.get(j) == 0) {
                            j++;
                        }
                        int freeBlockSize = j - i;
                        if (freeBlockSize >= processSize && freeBlockSize < bestFitSize) {
                            bestFitIndex = i;
                            bestFitSize = freeBlockSize;
                        }
                        i = j;
                    }
                }
                if (bestFitIndex != -1) {
                    for (int j = bestFitIndex; j < bestFitIndex + processSize; j++) {
                        memory.set(j, processSize);
                    }
                } else {
                    // Process could not be allocated
                }
                visualizeStep(memorySize, memory, step++);
            } else if (operation.startsWith("-p")) {
                int processIndex = Integer.parseInt(operation.substring(2)) - 1;
                int processSize = processSizes.get(processIndex);
                for (int i = 0; i < memory.size(); i++) {
                    if (memory.get(i) == processSize) {
                        for (int j = i; j < i + processSize; j++) {
                            memory.set(j, 0);
                        }
                        break;
                    }
                }
                visualizeStep(memorySize, memory, step++);
            }
        }
    }

    private void worstFit(int memorySize, List<Integer> processSizes, List<String> operations) {
        List<Integer> memory = new ArrayList<>(Collections.nCopies(memorySize, 0));
        int step = 1;
        for (String operation : operations) {
            if (operation.startsWith("+p")) {
                int processIndex = Integer.parseInt(operation.substring(2)) - 1;
                int processSize = processSizes.get(processIndex);
                int worstFitIndex = -1;
                int worstFitSize = 0;
                for (int i = 0; i < memory.size(); i++) {
                    if (memory.get(i) == 0) {
                        int j = i;
                        while (j < memory.size() && memory.get(j) == 0) {
                            j++;
                        }
                        int freeBlockSize = j - i;
                        if (freeBlockSize >= processSize && freeBlockSize > worstFitSize) {
                            worstFitIndex = i;
                            worstFitSize = freeBlockSize;
                        }
                        i = j;
                    }
                }
                if (worstFitIndex != -1) {
                    for (int j = worstFitIndex; j < worstFitIndex + processSize; j++) {
                        memory.set(j, processSize);
                    }
                } else {
                    // Process could not be allocated
                }
                visualizeStep(memorySize, memory, step++);
            } else if (operation.startsWith("-p")) {
                int processIndex = Integer.parseInt(operation.substring(2)) - 1;
                int processSize = processSizes.get(processIndex);
                for (int i = 0; i < memory.size(); i++) {
                    if (memory.get(i) == processSize) {
                        for (int j = i; j < i + processSize; j++) {
                            memory.set(j, 0);
                        }
                        break;
                    }
                }
                visualizeStep(memorySize, memory, step++);
            }
        }
    }

    private void nextFit(int memorySize, List<Integer> processSizes, List<String> operations) {
        List<Integer> memory = new ArrayList<>(Collections.nCopies(memorySize, 0));
        int step = 1;
        for (String operation : operations) {
            if (operation.startsWith("+p")) {
                int processIndex = Integer.parseInt(operation.substring(2)) - 1;
                int processSize = processSizes.get(processIndex);
                boolean allocated = false;
                for (int i = nextFitIndex; i < memory.size(); i++) {
                    if (memory.get(i) == 0 && memory.size() - i >= processSize) {
                        for (int j = i; j < i + processSize; j++) {
                            memory.set(j, processSize);
                        }
                        allocated = true;
                        nextFitIndex = i + processSize;
                        break;
                    }
                }
                if (!allocated) {
                    for (int i = 0; i < nextFitIndex; i++) {
                        if (memory.get(i) == 0 && memory.size() - i >= processSize) {
                            for (int j = i; j < i + processSize; j++) {
                                memory.set(j, processSize);
                            }
                            allocated = true;
                            nextFitIndex = i + processSize;
                            break;
                        }
                    }
                }
                if (!allocated) {
                    // Process could not be allocated
                }
                visualizeStep(memorySize, memory, step++);
            } else if (operation.startsWith("-p")) {
                int processIndex = Integer.parseInt(operation.substring(2)) - 1;
                int processSize = processSizes.get(processIndex);
                for (int i = 0; i < memory.size(); i++) {
                    if (memory.get(i) == processSize) {
                        for (int j = i; j < i + processSize; j++) {
                            memory.set(j, 0);
                        }
                        break;
                    }
                }
                visualizeStep(memorySize, memory, step++);
            }
        }
    }

    private EditText createEditText(String hint) {
        EditText editText = new EditText(getActivity());
        editText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        editText.setHint(hint);
        // Remove or comment out this line to avoid setting the input type to numeric
        // editText.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        return editText;
    }


    private TextView createLabel(String text) {
        TextView textView = new TextView(getActivity());
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textView.setText(text);
        textView.setTypeface(null, Typeface.BOLD);
        return textView;
    }

    private boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().isEmpty();
    }

    private int getIntegerFromEditText(EditText editText) {
        return Integer.parseInt(editText.getText().toString().trim());
    }

    abstract class SimpleTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        @Override
        public void afterTextChanged(Editable s) { }
    }

    private void visualizeStep(int memorySize, List<Integer> memory, int step) {
        TextView stepView = new TextView(getActivity());
        stepView.setText("Step " + step + ": " + memory.toString());
        stepView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.step_background));
        stepView.setPadding(8, 8, 8, 8);
        visualizationLayout.addView(stepView);
    }
}
