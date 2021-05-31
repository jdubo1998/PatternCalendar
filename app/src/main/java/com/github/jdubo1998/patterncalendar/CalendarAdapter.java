package com.github.jdubo1998.patterncalendar;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CalendarAdapter extends BaseAdapter {
    private LayoutInflater inflater;

    private int today_dayOfMonth;
    private int target_position;
    private int[] mDaysOfMonth;
    private String[] icons;
    private int[] colors;
    private boolean targetMonth = false;

    private boolean editMode = false;
    private int mEditPatternStartDate = -1;
    private int mEditPatternLength = -1;

//    public CalendarAdapter(int[] dayOfMonths, String[] icons, int[] colors, int todayDayOfMonth) {
//        updateMonth(dayOfMonths, icons, colors, todayDayOfMonth);
//    }

    public int getCount() {return 42;}

    public Object getItem(int position) {return null;}

    public long getItemId(int position) {return 0;}

    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null) {
            inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (convertView == null) {
            assert inflater != null;
            convertView = inflater.inflate(R.layout.calendardate_item, parent, false);
        }

        TextView[] ptrn_icons = {convertView.findViewById(R.id.ptrn0_icon), convertView.findViewById(R.id.ptrn1_icon), convertView.findViewById(R.id.ptrn2_icon),
                convertView.findViewById(R.id.ptrn3_icon), convertView.findViewById(R.id.ptrn4_icon), convertView.findViewById(R.id.ptrn5_icon)};

//        int date = CalendarFragment.getDate(position);
        TextView calendar_day = convertView.findViewById(R.id.calendar_day);

        if (editMode) {
            if (mDaysOfMonth[position] == 1) {
                targetMonth = !targetMonth;
            }

            if (targetMonth && mDaysOfMonth[position] >= mEditPatternStartDate && mDaysOfMonth[position] < (mEditPatternStartDate + mEditPatternLength)) {
                calendar_day.setTextColor(Color.rgb(0, 0, 0)); // Dates inside the pattern are colored black.
            } else {
                calendar_day.setTextColor(Color.rgb(150, 150, 150)); // Days outside of pattern are colored gray.
            }
        } else {
            if (mDaysOfMonth[position] == 1) {
                targetMonth = !targetMonth;
            }

            if (mDaysOfMonth[position] == today_dayOfMonth && (mDaysOfMonth[position] < 23 || position >= 22)) {
                calendar_day.setTextColor(Color.rgb(255, 50, 50)); // Date of today is colored red.
            } else if (position == target_position) {
                calendar_day.setTextColor(Color.rgb(50, 50, 255)); // Target day and target month is colored blue.
            } else if (targetMonth) {
                calendar_day.setTextColor(Color.rgb(0, 0, 0)); // Dates inside the target month are colored black.
            } else {
                calendar_day.setTextColor(Color.rgb(150, 150, 150)); // Dates outside of the target month are colored gray.
            }
        }

        for (int i = 0; i < 6; i++) {
            if (mDaysOfMonth[position] >= mEditPatternStartDate) {
                ptrn_icons[i].setText(Character.valueOf(icons[position].charAt(i)).toString());
            } else {
                ptrn_icons[i].setText(" ");
            }

            if (i < colors.length) {
                ptrn_icons[i].setTextColor(colors[i]);
            }
        }

        calendar_day.setText(String.format("%d", mDaysOfMonth[position]));
        return convertView;
    }

    public void updateMonth(int[] dayOfMonths, String[] icons, int[] colors, int todayDayOfMonth) {
        this.today_dayOfMonth = todayDayOfMonth;
        this.mDaysOfMonth = dayOfMonths;
        this.icons = icons;
        this.colors = colors;
        this.target_position = -1;

        notifyDataSetChanged();
    }

    public void updateTargetDayOfMonth(int target_position) {
        this.target_position = target_position;
        notifyDataSetChanged();
    }

    public void updateEditPattern(int editPatternStartDate, int editPatternLength) {
        mEditPatternStartDate = editPatternStartDate;
        mEditPatternLength = editPatternLength;

        editMode = editPatternLength > 0;

        notifyDataSetChanged();
    }
}
