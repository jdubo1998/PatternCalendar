package com.github.jdubo1998.patterncalendar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;

public class CalendarFragment extends Fragment {
    public static final String TAG = CalendarFragment.class.getSimpleName();
    TextView monthYearText;
    private LocalDate mDate;
    private SharedViewModel mViewModel;
    private static final int[] mDaysOfMonth = new int[42];
    private CalendarAdapter mAdapter;
    int[] mParams;

    private Pattern mEditPattern;

    public CalendarFragment() {
        super(R.layout.calendar_layout);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated");

        mViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        final ImageButton nextMonth = view.findViewById(R.id.next_month);
        final ImageButton prevMonth = view.findViewById(R.id.prev_month);
        monthYearText = view.findViewById(R.id.monthyear_text);

        mDate = DateTime.now().toLocalDate();
        mAdapter = new CalendarAdapter();

        /*---   Initialize calendar to today.    ---*/
        updateCalendar(mDate);
//        mAdapter.updatePatterns(PatternsManager.getIcons(), PatternsManager.getColors());

        /*---   Calendar initialization.   ---*/
        GridView calendarGrid = view.findViewById(R.id.calendar_gridView);
        calendarGrid.setAdapter(mAdapter);

        /* Attach observers and listeners. */
        mViewModel.getEditPattern().observe(getViewLifecycleOwner(), new Observer<Pattern>() {
            @Override
            public void onChanged(Pattern pattern) {
                /* Not in edit mode. */
                if (pattern == null) {
                    if (mViewModel.getSave().getValue() != null) {
                        if (!mViewModel.getSave().getValue()) {
                            PatternsManager.revertEditPattern(mEditPattern);
                        }

                        nextMonth.setVisibility(View.VISIBLE);
                        prevMonth.setVisibility(View.VISIBLE);

                        mEditPattern = null;
                        mDate = LocalDate.now();
                    }
                /* In edit mode. */
                } else {
                    nextMonth.setVisibility(View.INVISIBLE);
                    prevMonth.setVisibility(View.INVISIBLE);

                    mEditPattern = pattern;
                    mDate = pattern.startDate();
                }

                updateCalendar(mDate);
            }
        });

        nextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDate = mDate.plusMonths(1);
                updateCalendar(mDate);
                mAdapter.updateTarget(-1);
            }
        });

        prevMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDate = mDate.minusMonths(1);
                updateCalendar(mDate);
                mAdapter.updateTarget(-1);
            }
        });

        calendarGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mParams[0] == 1) {
                    if (mParams[1] <= position && position <= mParams[2]) {
                        mEditPattern.cyclePatternIcon(position - mParams[1]);
                    }

                    mViewModel.setEditPattern(mEditPattern);
                } else {
                    mAdapter.updateTarget(position);
                    mViewModel.setDayOffset(position);
                }
            }
        });
    }

    private void updateCalendar(LocalDate date) {
        String patternName = null;
        int endMonth = 0;
        monthYearText.setText(date.toString("MMMM yyyy"));

        int offset = (date.getDayOfWeek() - (date.getDayOfMonth()%7)+8)%7 - 1; // Number of days from previous month to fill at beginning of calendar.
        LocalDate firstDate = date.minusDays(date.getDayOfMonth() + offset); // DateTime of the first date for the calendar.
        for (int i = 0; i < 42; i++) {
            mDaysOfMonth[i] = firstDate.plusDays(i).getDayOfMonth();
            if (i > 7 && mDaysOfMonth[i] == 1) {
                endMonth = i-1;
            }
        }

        /* If edit mode is active, only show icons for the pattern being edited. */
        if (mEditPattern != null) {
            mParams = new int[3];
            patternName = mEditPattern.name();
            int startPattern = Days.daysBetween(firstDate, mEditPattern.startDate()).getDays();
            int endPattern = startPattern + mEditPattern.length() - 1;

            mParams[0] = 1;
            mParams[1] = startPattern;
            mParams[2] = endPattern;
        } else {
            mParams = new int[4];
            int startMonth = offset + 1;
            int today = Days.daysBetween(firstDate, DateTime.now().toLocalDate()).getDays();

            mParams[0] = 0;
            mParams[1] = startMonth;
            mParams[2] = endMonth;
            mParams[3] = today;
        }

        mAdapter.updatePatterns(PatternsManager.getIcons(firstDate, patternName), PatternsManager.getColors(patternName));
        mAdapter.updateCalendar(mDaysOfMonth, mParams);
    }
}
