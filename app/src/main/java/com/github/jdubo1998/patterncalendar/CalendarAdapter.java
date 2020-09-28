package com.github.jdubo1998.patterncalendar;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.joda.time.DateTime;

public class CalendarAdapter extends BaseAdapter {
    private int todayDate;
    private LayoutInflater inflater;
    private boolean editMode = false;
    private boolean targetMonth = false;
    private int startPosition = -1;

    public CalendarAdapter(int todayDate) {
        this.todayDate = todayDate;
    }

//    public void update() {
//
//    }

    public int getCount() {return 42;}

    public Object getItem(int position) {return null;}

    public long getItemId(int position) {return 0;}

    public View getView(int position, View convertView, ViewGroup parent) {
        int date = CalendarFragment.getDate(position);
        TextView calendar_day = (TextView) convertView.findViewById(R.id.calendar_day);

        if (inflater == null) {
            inflater = (LayoutInflater) parent.getContext().getSystemService(parent.getContext().LAYOUT_INFLATER_SERVICE);
        }

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.calendardate_layout, null);
        }

        if (editMode) {
            TextView ptrn_icon = convertView.findViewById(R.id.ptrn0_icon);

            if (startPosition < position) {
                String icon = PatternContainer.editPattern.getIcon(position);
                ptrn_icon.setText(icon);
            }

        } else {
            TextView[] ptrn_icons = {convertView.findViewById(R.id.ptrn0_icon), convertView.findViewById(R.id.ptrn1_icon),
                    convertView.findViewById(R.id.ptrn2_icon), convertView.findViewById(R.id.ptrn3_icon),
                    convertView.findViewById(R.id.ptrn4_icon), convertView.findViewById(R.id.ptrn5_icon)};

            if (date == 1) {
                targetMonth = !targetMonth;
            }

            if (date == todayDate && targetMonth) {
                calendar_day.setTextColor(Color.rgb(255, 50, 50));
            } else if (targetMonth) {
                calendar_day.setTextColor(Color.rgb(255, 255, 255));
            } else {
                calendar_day.setTextColor(Color.rgb(150, 150, 150));
            }

            //TODO: Add functionality if getting the icons of only a specific pattern through the name.
            String[] icons = PatternContainer.getIcons(position);
            int[] colors = PatternContainer.getColors();
            for (int i = 0; i < PatternContainer.count(); i++) {
                ptrn_icons[i].setText(icons[i]);
                ptrn_icons[i].setTextColor(colors[i]);
            }

            calendar_day.setText("" + date);
        }

        return convertView;
    }
}
