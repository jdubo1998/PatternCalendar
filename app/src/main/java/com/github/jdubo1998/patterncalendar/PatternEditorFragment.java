package com.github.jdubo1998.patterncalendar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class PatternEditorFragment extends Fragment {
    SharedViewModel mViewModel;
    private Pattern mEditPattern;
//    private int mPatternIndex;
//    private int mNumberOfDays = 0;

    public PatternEditorFragment() {
        super(R.layout.patterneditor_layout);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        final EditText patternNameEditText = view.findViewById(R.id.patternname_edittext);
        final EditText startDateEditText = view.findViewById(R.id.startdate_edittext); // TODO: better method to get date.
        Button incrementDaysButton = view.findViewById(R.id.incrementdays_button);
        Button decrementDaysButton = view.findViewById(R.id.decrementdays_button);
        final EditText numberOfDays = view.findViewById(R.id.numberofdays_text);
        // TODO: Add color changing functionality.
        final CheckBox activeCheckBox = view.findViewById(R.id.active_checkbox);

        ListView labelsList = view.findViewById(R.id.labels_list);
        LabelsListAdapter adapter = new LabelsListAdapter(PatternsManager.getLabels());
        labelsList.setAdapter(adapter);

        /* Attach listeners and observers. */
        mViewModel.getEditPattern().observe(getViewLifecycleOwner(), new Observer<Pattern>() {
            @Override
            public void onChanged(Pattern pattern) {
                mEditPattern = pattern;
                startDateEditText.setText(mEditPattern.startDate().toString("MM/dd/yy"));
                patternNameEditText.setText(mEditPattern.name);
                numberOfDays.setText("" + mEditPattern.length());
                activeCheckBox.setActivated(mEditPattern.isActive);
            }
        });

//        mViewModel.getPatternIndex().observe(getViewLifecycleOwner(), new Observer<Integer>() {
//            @Override
//            public void onChanged(Integer integer) {
//                mPatternIndex = integer;
//                Pattern pattern = PatternsManager.getPattern(mPatternIndex);
//
//                startDateEditText.setText(pattern.getDate());
//
//                patternNameEditText.setText(pattern.name);
//
//                mNumberOfDays = pattern.length();
//                numberOfDays.setText("" + mNumberOfDays);
//
//                activeCheckBox.setActivated(pattern.isActive);
//            }
//        });

        incrementDaysButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mNumberOfDays++;
                mEditPattern.incrementLength();
                numberOfDays.setText("" + mEditPattern.length());
                mViewModel.setEditPattern(mEditPattern);
//                mViewModel.setPatternLength(mNumberOfDays);
            }
        });

        decrementDaysButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditPattern.length() > 0) {
                    mEditPattern.decrementLength();
//                    mNumberOfDays--;
                    numberOfDays.setText("" + mEditPattern.length());
                    mViewModel.setEditPattern(mEditPattern);
//                    mViewModel.setPatternLength(mNumberOfDays);
                }
            }
        });
    }
}
