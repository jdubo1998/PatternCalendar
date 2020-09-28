package com.github.jdubo1998.patterncalendar;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.joda.time.DateTime;

import java.util.ArrayList;

public class CalendarFragment extends Fragment {
    private static int[] dates = new int[42];

    private CalendarFragmentListener listener;
    private GridView calendarGridView;
    boolean curMonth = true;
    private final DateTime today = DateTime.now();
    private boolean editMode = false;

    public interface CalendarFragmentListener {
        void enterEditMode();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.calendar_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        /*---   Date calculations.    ---*/
//        DateTime today = this.today.plusDays(2); // TODO: Remove this testing line.
        int offset = (today.getDayOfWeek() - (today.getDayOfMonth()%7)+8)%7 - 1; // Number of days in the previous month.
        DateTime start = today.minusDays(today.getDayOfMonth() + offset); // DateTime of the first date in the calendar. (previous month)
        PatternContainer.REFDATES(start);
        final int todayDate = (curMonth) ? today.getDayOfMonth() : -1;
//        DateTime refDate = today.minusDays(today.getDayOfMonth()-1).minusMonths(2); // Reference date for when the pattern beings. // TODO: Delete this.
//        int daysStart = Days.daysBetween(refDate.toLocalDate(), start.toLocalDate()).getDays(); // TODO: Delete this.

//        TextView[] ptrn_labels = new TextView[]{view.findViewById(R.id.ptrn1_label), view.findViewById(R.id.ptrn2_label),
//                view.findViewById(R.id.ptrn3_label), view.findViewById(R.id.ptrn4_label),
//                view.findViewById(R.id.ptrn5_label), view.findViewById(R.id.ptrn6_label)};

        /*---   Calendar initialization.   ---*/
        calendarGridView = view.findViewById(R.id.calendar_gridView);
        final CalendarAdapter adapter = new CalendarAdapter(todayDate);
        calendarGridView.setAdapter(adapter);

        ArrayList<LinearLayout> patternList = new ArrayList<>();

        for (int i = 0; i < PatternContainer.count(); i++) {
            patternList.add(new LinearLayout(getContext()));
            patternList.get(0).child;
        }

//        if (editMode) {
//        } else {
//        }
//
//        /*---   Adds touch functionality to the dates in the calendar.   ---*/
//        calendarGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                listener.enterEditMode();
//                return true;
//            }
//        });

        calendarGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (editMode) {
                }

//                adapter.updateEditMode();
                calendarGridView.invalidateViews();
            }
        });

        for (int i = 0; i < 42; i++) {
            dates[i] = start.plusDays(i).getDayOfMonth();
        }

        /*---   Sets the labels.   ---*/
        String[] labels = PatternContainer.getLabels(today.getDayOfMonth() + offset);
        String[] names = PatternContainer.getNames();
        int[] colors = PatternContainer.getColors();

        for (int i = 0; i < PatternContainer.count(); i++) {
            String label = names[i] + ": " + labels[i];
//            ptrn_labels[i].setText(label);
//            ptrn_labels[i].setTextColor(colors[i]);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof CalendarFragmentListener) {
            listener = (CalendarFragmentListener) context;
        } else {
            throw new RuntimeException("ERROR: CalendarFragmentListener not implemented.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public void enterEditMode() {
        editMode = true;
    }

    public void exitEditMode() {
        editMode = false;
    }

    public static int getDate(int position) {
        return dates[position];
    }
}
