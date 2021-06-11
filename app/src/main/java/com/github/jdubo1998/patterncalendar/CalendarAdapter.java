package com.github.jdubo1998.patterncalendar;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CalendarAdapter extends BaseAdapter {
    private LayoutInflater mInflater;

    private int mTarget = -1;
    private int[] mDaysOfMonth;
    private String[] mIcons;
    private int[] mColors;
    private int[] mParams;

    public int getCount() {return 42;}

    public Object getItem(int position) {return null;}

    public long getItemId(int position) {return 0;}

    public View getView(int position, View convertView, ViewGroup parent) {
        if (mInflater == null) {
            mInflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (convertView == null) {
            assert mInflater != null;
            convertView = mInflater.inflate(R.layout.calendardate_item, parent, false);
        }

        Resources resources = parent.getResources();

        TextView[] ptrn_icons = {convertView.findViewById(R.id.ptrn0_icon), convertView.findViewById(R.id.ptrn1_icon), convertView.findViewById(R.id.ptrn2_icon),
                convertView.findViewById(R.id.ptrn3_icon), convertView.findViewById(R.id.ptrn4_icon), convertView.findViewById(R.id.ptrn5_icon)};

//        int date = CalendarFragment.getDate(position);
        TextView calendar_day = convertView.findViewById(R.id.calendar_day);

        /* Edit Mode */
        if (mParams[0] == 1) {
            if (mParams[1] <= position && position <= mParams[2]) {
                calendar_day.setTextColor(resources.getColor(R.color.primaryDate)); // Dates inside the target month are colored black.
            } else {
                calendar_day.setTextColor(resources.getColor(R.color.secondaryDate)); // Dates outside of the target month are colored gray.
            }

        /* Pattern Viewer Mode */
        } else {
            if (position == mParams[3]) {
                calendar_day.setTextColor(resources.getColor(R.color.today)); // Date of today is colored red.
            } else if (position == mTarget) {
                calendar_day.setTextColor(resources.getColor(R.color.target)); // Target day and target month is colored blue.
            } else if (mParams[1] <= position && position <= mParams[2]) {
                calendar_day.setTextColor(resources.getColor(R.color.primaryDate)); // Dates inside the target month are colored black.
            } else {
                calendar_day.setTextColor(resources.getColor(R.color.secondaryDate)); // Dates outside of the target month are colored gray.
            }
        }

        for (int i = 0; i < 6; i++) {
            int editPatternStartDate = -1; // TODO: What is this variable used for..

            if (mDaysOfMonth[position] >= editPatternStartDate) {
                ptrn_icons[i].setText(Character.valueOf(mIcons[position].charAt(i)).toString());
            } else {
                ptrn_icons[i].setText(" ");
            }

            if (i < mColors.length) {
                ptrn_icons[i].setTextColor(mColors[i]);
            }
        }

        calendar_day.setText(resources.getString(R.string.int_placeholder, mDaysOfMonth[position]));
        return convertView;
    }

    public void updatePatterns(String[] icons, int[] colors) {
        mIcons = icons;
        mColors = colors;
    }

    public void updateCalendar(int[] dayOfMonth, int[] params) {
        mDaysOfMonth = dayOfMonth;
        mParams = params;

        notifyDataSetChanged();
    }

    public void updateTarget(int target) {
        mTarget = target;

        notifyDataSetChanged();
    }
}
