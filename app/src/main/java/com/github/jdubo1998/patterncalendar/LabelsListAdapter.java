package com.github.jdubo1998.patterncalendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LabelsListAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    public static String[] mLabels;

    public LabelsListAdapter(String[] labels) {
        update(labels);
    }

    @Override
    public int getCount() {
        return mLabels.length + 1;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null) {
            inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (convertView == null) {
            assert inflater != null;
            convertView = inflater.inflate(R.layout.patternviewer_item, parent, false);
        }

        TextView patternLabelText = convertView.findViewById(R.id.patternlabel_text);
        if (position == mLabels.length) {
            patternLabelText.setText("[+] Add Label");
        } else {
            patternLabelText.setText(mLabels[position]);
        }

        return convertView;
    }

    public void update(String[] labels) {
        mLabels = labels;

        notifyDataSetChanged();
    }
}
