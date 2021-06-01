package com.github.jdubo1998.patterncalendar;

import android.os.Bundle;
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
import org.joda.time.Months;

public class CalendarFragment extends Fragment {
//    public static final String TAG = CalendarFragment.class.getSimpleName();
    TextView monthYearText;
    private LocalDate mDate;
    private SharedViewModel mViewModel;
    private static int[] mDaysOfMonth = new int[42];
    private int mToday_dayOfMonth = -1;
    int curMonthOffset = 0;
//    private boolean editMode = false;
    private CalendarAdapter mAdapter;

    private Pattern mEditPattern;
//    private int mPatternLength;
//    private String mEditPatternName = null;

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

        mViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        final ImageButton nextMonth = view.findViewById(R.id.next_month);
        final ImageButton prevMonth = view.findViewById(R.id.prev_month);
        monthYearText = view.findViewById(R.id.monthyear_text);

        mDate = DateTime.now().toLocalDate();
//        mAdapter = new CalendarAdapter(mDaysOfMonths, PatternsManager.getIcons(), PatternsManager.getColors(), mToday_dayOfMonth);
        mAdapter = new CalendarAdapter();

        /*---   Initialize calendar to today.    ---*/
        mToday_dayOfMonth = mDate.getDayOfMonth();
        updateCalendar(mDate);
        mAdapter.updatePatterns(PatternsManager.getIcons(), PatternsManager.getColors());

        /*---   Calendar initialization.   ---*/
        GridView calendarGrid = view.findViewById(R.id.calendar_gridView);
        calendarGrid.setAdapter(mAdapter);

        /* Attach observers and listeners. */
        mViewModel.getEditPattern().observe(getViewLifecycleOwner(), new Observer<Pattern>() {
            @Override
            public void onChanged(Pattern pattern) {
                if (pattern == null) {
                    nextMonth.setVisibility(View.VISIBLE);
                    prevMonth.setVisibility(View.VISIBLE);

                    mEditPattern = null;
                    updateCalendar(DateTime.now().toLocalDate());
                } else {
//                    editMode = true;
                    nextMonth.setVisibility(View.INVISIBLE);
                    prevMonth.setVisibility(View.INVISIBLE);

                    mEditPattern = pattern;
                    curMonthOffset += Months.monthsBetween(mDate, pattern.startDate()).getMonths();
                    mDate = pattern.startDate();
                    updateCalendar(mDate);
                }
            }
        });

        nextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curMonthOffset++;
                mDate = mDate.plusMonths(1);
                updateCalendar(mDate);
            }
        });

        prevMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curMonthOffset--;
                mDate = mDate.minusMonths(1);
                updateCalendar(mDate);
            }
        });

        calendarGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mEditPattern == null) {
                    mViewModel.setDayOffset(position);
                } else {
                    int date = mDaysOfMonth[position];
                    int startDate = mEditPattern.startDate().getDayOfMonth();

                    if (date >= startDate && date < (startDate + mEditPattern.length())) {
                        mEditPattern.cyclePatternIcon((date - startDate) % mEditPattern.length());
                    }

                    mViewModel.setEditPattern(mEditPattern);
                }
            }
        });
    }

    private void updateCalendar(LocalDate date) {
//        String patternName = null;
        int endMonth = 0;
        int[] params;

        monthYearText.setText(date.toString("MMMM yyyy"));
//        int today_DayOfMonth = -1;

        int offset = (date.getDayOfWeek() - (date.getDayOfMonth()%7)+8)%7 - 1; // Number of days from previous month to fill at beginning of calendar.
        LocalDate firstDate = date.minusDays(date.getDayOfMonth() + offset); // DateTime of the first date for the calendar.
        for (int i = 0; i < 42; i++) {
            mDaysOfMonth[i] = firstDate.plusDays(i).getDayOfMonth();
            if (i > 7 && mDaysOfMonth[i] == 1) {
                endMonth = i-1;
            }
        }

        if(Months.monthsBetween(date, LocalDate.now()).getMonths() == 0) {
            System.out.println("Target month.");
        }

        /* If the target current month is the month of today. Then set mToday_dayOfMonth, so that is shows up as red. */
//        if (curMonthOffset == 0) {
//            today_DayOfMonth = mToday_dayOfMonth;
//        }

        /* If edit mode is active, only show icons for the pattern being edited. */
        if (mEditPattern != null) {
            params = new int[3];
//            patternName = mEditPattern.name;
            int startPattern = Days.daysBetween(firstDate, mEditPattern.startDate()).getDays();
            int endPattern = startPattern + mEditPattern.length() - 1;

            params[0] = 1;
            params[1] = startPattern;
            params[2] = endPattern;
        } else {
            params = new int[4];
            int startMonth = offset + 1;
            int today = Days.daysBetween(firstDate, DateTime.now().toLocalDate()).getDays();

            params[0] = 0;
            params[1] = startMonth;
            params[2] = endMonth;
            params[3] = today;
        }

        mAdapter.updatePatterns(PatternsManager.getIcons(), PatternsManager.getColors());
        mAdapter.updateCalendar(mDaysOfMonth, params);
    }
}
