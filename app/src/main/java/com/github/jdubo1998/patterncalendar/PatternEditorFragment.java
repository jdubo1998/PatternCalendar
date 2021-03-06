package com.github.jdubo1998.patterncalendar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

public class PatternEditorFragment extends Fragment {
    public static final String TAG = PatternEditorFragment.class.getSimpleName();
    SharedViewModel mViewModel;
    private Pattern mEditPattern;
    private CheckBox mActiveCheckBox;
    private CheckBox mArchiveCheckBox;

    public PatternEditorFragment() {
        super(R.layout.patterneditor_layout);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        Log.d(TAG, "onViewCreated");

        final EditText patternNameEditText = view.findViewById(R.id.patternname_edittext);
        final EditText startDateEditText = view.findViewById(R.id.startdate_edittext); // TODO: better method to get date.
        Button incrementDaysButton = view.findViewById(R.id.incrementdays_button);
        Button decrementDaysButton = view.findViewById(R.id.decrementdays_button);
        final EditText numberOfDaysEditText = view.findViewById(R.id.numberofdays_text);
        final FrameLayout colorPickerFrameLayout = view.findViewById(R.id.colorpicker_framelayout);
        mActiveCheckBox = view.findViewById(R.id.active_checkbox);
        mArchiveCheckBox = view.findViewById(R.id.archive_checkbox);

        mEditPattern = mViewModel.getEditPattern().getValue();
        ListView labelsList = view.findViewById(R.id.labels_list);
        final LabelsListAdapter adapter = new LabelsListAdapter(mEditPattern.getLabels());
        labelsList.setAdapter(adapter);

        final ColorPicker colorPicker = new ColorPicker(getActivity(), Color.alpha(mEditPattern.getColor()),
                Color.red(mEditPattern.getColor()),
                Color.green(mEditPattern.getColor()),
                Color.blue(mEditPattern.getColor()));
        colorPicker.enableAutoClose();
        colorPicker.setCallback(new ColorPickerCallback() {
            @Override
            public void onColorChosen(int color) {
                mEditPattern.setColor(color);
                mViewModel.setEditPattern(mEditPattern);
            }
        });

        /* Attach observers and listeners. */
        mViewModel.getEditPattern().observe(getViewLifecycleOwner(), new Observer<Pattern>() {
            @Override
            public void onChanged(Pattern pattern) {
                mEditPattern = pattern;

                if (mEditPattern != null) {
                    startDateEditText.setText(mEditPattern.startDate().toString("MM/dd/yy"));
                    patternNameEditText.setText(mEditPattern.mName);
                    numberOfDaysEditText.setText(getString(R.string.int_placeholder, mEditPattern.length()));
                    colorPickerFrameLayout.setBackgroundColor(mEditPattern.getColor());

                    mActiveCheckBox.setActivated(mEditPattern.getState() != Pattern.VISIBLE);
                    mArchiveCheckBox.setActivated(mEditPattern.getState() == Pattern.VISIBLE);

                    adapter.update(mEditPattern.getLabels());
                }
            }
        });

        patternNameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) { // TODO: Change how to handle this.
                Log.d(TAG, "onFocusChange: patternNameEditText: " + hasFocus);
                if (!hasFocus) {
                    mEditPattern.setName(patternNameEditText.getText().toString());
                    mViewModel.setEditPattern(mEditPattern);
                }
            }
        });

        startDateEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) { // TODO: Change how to handle this.
                if (!hasFocus) {
                    try {
                        LocalDate startDate = LocalDate.parse(startDateEditText.getText().toString(), DateTimeFormat.forPattern("MM/dd/yy"));
                        mEditPattern.updateStartDate(startDate);
                        mViewModel.setEditPattern(mEditPattern);
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Date must be formatted in: MM/dd/yy", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        numberOfDaysEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    return;
                }

                int numberOfDays = Integer.parseInt(s.toString());

                if (mEditPattern.length() == numberOfDays) {
                    return;
                }

                if (mEditPattern.length() < numberOfDays) {
                    for (int i = 0; i < numberOfDays-mEditPattern.length(); i++) {
                        mEditPattern.incrementLength();
                    }
                } else {
                    for (int i = 0; i < mEditPattern.length()-numberOfDays; i++) {
                        mEditPattern.decrementLength();
                    }
                }

                mViewModel.setEditPattern(mEditPattern);
            }
        });

        incrementDaysButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditPattern.incrementLength();
                numberOfDaysEditText.setText(getString(R.string.int_placeholder, mEditPattern.length()));
                mViewModel.setEditPattern(mEditPattern);
            }
        });

        decrementDaysButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditPattern.length() > 0) {
                    mEditPattern.decrementLength();
                    numberOfDaysEditText.setText(getString(R.string.int_placeholder, mEditPattern.length()));
                    mViewModel.setEditPattern(mEditPattern);
                }
            }
        });

        colorPickerFrameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (colorPicker.isShowing()) {
                    colorPicker.hide();
                } else {
                    colorPicker.show();
                }
            }
        });

        mActiveCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCheckBoxesClick();
            }
        });

        mArchiveCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCheckBoxesClick();
            }
        });

        labelsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder dialogMenu = new AlertDialog.Builder(view.getContext());
                /* Editing existing label. */
                if (position < mEditPattern.getLabels().length) {
                    dialogMenu.setTitle("Edit Label");
                /* Adding new label. */
                } else {
                    dialogMenu.setTitle("Add New Label");
                }

                final EditText labelEditText = new EditText(view.getContext());

                /* If clicking on an existing label, text box is set to the label. If clicking on new label, text box is empty. */
                if (position < mEditPattern.getLabels().length) {
                    labelEditText.setText(mEditPattern.getLabels()[position]);
                }

                dialogMenu.setView(labelEditText);
                dialogMenu.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /* If clicking on a label. */
                        if (position < mEditPattern.getLabels().length) {
                            mEditPattern.setLabel(position, labelEditText.getText().toString());
                            /* If clicking to make a new label. */
                        } else {
                            mEditPattern.addLabel(labelEditText.getText().toString());
                        }

                        mViewModel.setEditPattern(mEditPattern);
                    }
                });

                dialogMenu.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                /* Only adds the delete button if the label already exists. */
                if (position < mEditPattern.getLabels().length) {
                    dialogMenu.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mEditPattern.removeLabel(position);
                        }
                    });

                    mViewModel.setEditPattern(mEditPattern);
                }

                dialogMenu.show();
            }
        });
    }

    private void onCheckBoxesClick() {
        if (mArchiveCheckBox.isChecked()) {
            mActiveCheckBox.setChecked(true);
            mEditPattern.setState(Pattern.ARCHIVED);
        } else if (mActiveCheckBox.isChecked()) {
            mEditPattern.setState(Pattern.INVISIBLE);
        } else {
            mEditPattern.setState(Pattern.VISIBLE);
        }

        mViewModel.setEditPattern(mEditPattern);
    }
}
