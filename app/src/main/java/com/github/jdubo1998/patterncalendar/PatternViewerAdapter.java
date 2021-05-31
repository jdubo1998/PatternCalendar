package com.github.jdubo1998.patterncalendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PatternViewerAdapter extends BaseAdapter {
    private LayoutInflater mInflater;

    private String[] mNames;
    private String[] mLabels;
    private int[] mColors;

    public PatternViewerAdapter(String[] names, String[] labels, int[] colors) {
        update(names, labels, colors);
    }

    @Override
    public int getCount() {
        return mNames.length;
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
        if (mInflater == null) {
            mInflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (convertView == null) {
            assert mInflater != null;
            convertView = mInflater.inflate(R.layout.patternviewer_item, parent, false);
        }

        TextView patternLabelText = convertView.findViewById(R.id.patternlabel_text);
        TextView patternValueText = convertView.findViewById(R.id.patternvalue_text);

        patternLabelText.setText(mNames[position]);
        patternValueText.setText(mLabels[position]);
        patternLabelText.setTextColor(mColors[position]);
        patternValueText.setTextColor(mColors[position]);

        return convertView;
    }

    public void update(String[] names, String[] labels, int[] colors) {
        mNames = names;
        mLabels = labels;
        mColors = colors;

        notifyDataSetChanged();
    }

    public void updateLabels(String[] labels) {
        mLabels = labels;

        notifyDataSetChanged();
    }
}
