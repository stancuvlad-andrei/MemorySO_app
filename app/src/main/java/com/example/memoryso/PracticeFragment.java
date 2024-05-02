package com.example.memoryso;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PracticeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PracticeFragment extends Fragment {
    private EditText memorySizeInput;
    private EditText numProcessesInput;
    private EditText numOperationsInput;
    private LinearLayout processSizesLayout;
    private LinearLayout operationSizesLayout;
    private Button allocateButton;
    private RadioGroup algorithmRadioGroup;
    private int selectedAlgorithm = 0;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PracticeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PracticeFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        allocateButton = rootView.findViewById(R.id.allocate_button);
        algorithmRadioGroup = rootView.findViewById(R.id.algorithm_radio_group);

        allocateButton.setOnClickListener(v -> {
            int memorySize = Integer.parseInt(memorySizeInput.getText().toString());
            int numProcesses = Integer.parseInt(numProcessesInput.getText().toString());
            int numOperations = Integer.parseInt(numOperationsInput.getText().toString());
            List<Integer> processSizes = getProcessSizes(processSizesLayout);
            List<String> operations = getOperations(operationSizesLayout);

            performMemoryAllocation(memorySize, processSizes, operations, selectedAlgorithm);
        });

        numProcessesInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateProcessSizeInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        numOperationsInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateOperationInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {}
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



    private void updateProcessSizeInputs() {
        int numProcesses = Integer.parseInt(numProcessesInput.getText().toString());
        processSizesLayout.removeAllViews();
        for (int i = 0; i < numProcesses; i++) {
            EditText processSizeInput = new EditText(requireContext());
            processSizeInput.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            processSizeInput.setHint("Process " + (i + 1) + " Size");
            processSizesLayout.addView(processSizeInput);
        }
    }

    private void updateOperationInputs() {
        int numOperations = Integer.parseInt(numOperationsInput.getText().toString());
        operationSizesLayout.removeAllViews();
        for (int i = 0; i < numOperations; i++) {
            EditText operationSizeInput = new EditText(requireContext());
            operationSizeInput.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            operationSizeInput.setHint("Operation " + (i + 1));
            operationSizesLayout.addView(operationSizeInput);
        }
    }

    private List<Integer> getProcessSizes(LinearLayout layout) {
        List<Integer> processSizes = new ArrayList<>();
        for (int i = 0; i < layout.getChildCount(); i++) {
            EditText processSizeInput = (EditText) layout.getChildAt(i);
            processSizes.add(Integer.parseInt(processSizeInput.getText().toString()));
        }
        return processSizes;
    }

    private List<String> getOperations(LinearLayout layout) {
        List<String> operations = new ArrayList<>();
        for (int i = 0; i < layout.getChildCount(); i++) {
            EditText operationInput = (EditText) layout.getChildAt(i);
            String operation = operationInput.getText().toString();
            if (operation.startsWith("+p")) {
                operations.add(operation);
            } else {
                // Handle invalid operation format
                operations.add(""); // or throw an exception
            }
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
                break;
            case 1:
                allocationStatus.append("Best Fit");
                break;
            case 2:
                allocationStatus.append("Worst Fit");
                break;
            case 3:
                allocationStatus.append("Next Fit");
                break;
        }

        TextView allocationStatusTextView = getView().findViewById(R.id.allocation_status_text_view);
        if (allocationStatusTextView != null) {
            allocationStatusTextView.setText(allocationStatus.toString());
        }
    }

}