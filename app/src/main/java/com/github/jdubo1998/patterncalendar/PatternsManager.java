package com.github.jdubo1998.patterncalendar;

import android.graphics.Color;
import android.util.Log;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.Arrays;

public class PatternsManager {
    private static final ArrayList<Pattern> patterns = new ArrayList<>();
    private static LocalDate mFirstDate;
    public static int delta_firstDate_today;
    private static String mRevertCode;

    private PatternsManager() {}

    public static void parseCode(String patternCodes) {
        patterns.clear();

        LocalDate today = LocalDate.now();
        int offset = (today.getDayOfWeek() - (today.getDayOfMonth()%7)+8)%7 - 1;
        LocalDate firstDate = today.minusDays(today.getDayOfMonth() + offset); // Date of first day in last month.

        for (String patternCode : patternCodes.split(";;;")) {
            addPattern(patternCode, firstDate);
        }

        delta_firstDate_today = Days.daysBetween(firstDate, today).getDays();
    }

    public static void addPattern(String patternCode, LocalDate firstDate) {
        patterns.add(new Pattern(patterns.size(), patternCode, firstDate));
    }

    public static Pattern getPattern(int index) {
        mRevertCode = patterns.get(index).generateCode();
        return patterns.get(index);
    }

    public static Pattern getPattern(String name) {
        for (Pattern pattern : patterns) {
            if (pattern.mName.equals(name)) {
                return pattern;
            }
        }

        return null;
    }

    public static String[] getIcons() {
        return getIcons("");
    }

    /* Returns a string array of each icon for the given date. */
    public static String[] getIcons(String name) {
        String[] icons = new String[42];

        for (int j = 0; j < icons.length; j++) {
            StringBuilder icon = new StringBuilder();

            for (int i = 0; i < 6; i++) {
                if (i < patterns.size() && (patterns.get(i).mName.equals(name) || name.isEmpty()) && patterns.get(i).getState() == Pattern.VISIBLE) {
                    icon.append(patterns.get(i).getIcon(j));
                } else {
                    icon.append(' ');
                }
            }

            icons[j] = icon.toString();
        }

        return icons;
    }

    public static String[] getIcons(LocalDate firstDate) {
        return getIcons(firstDate, null);
    }

    public static String[] getIcons(LocalDate firstDate, String name) {
        String[] icons = new String[42];
        changeFirstDates(firstDate);

        for (int j = 0; j < icons.length; j++) {
            char[] iconList = new char[6];
            Arrays.fill(iconList, ' ');
            int index = 0;

            for (int i = 0; i < patterns.size(); i++) {
                if (index < 6 && (patterns.get(i).mName.equals(name) || name == null) && patterns.get(i).getState() == Pattern.VISIBLE) {
                    iconList[index] = patterns.get(i).getIcon(j);
                    index++;
                }
            }

            icons[j] = String.valueOf(iconList);
        }

        return icons;
    }

    public static void changeFirstDates(LocalDate firstDate) {
        for (Pattern pattern : patterns) {
            pattern.updateFirstDate(firstDate);
        }
    }

    /* Gets the label for today. */
    public static String[] getLabels() {
        return getLabels(delta_firstDate_today);
    }

    /* Returns the labels for the day where offset is the days after firstDate. */
    public static String[] getLabels(int offset) {
        String[] labels = new String[patterns.size()];

        for (int i = 0; i < labels.length; i++) {
            labels[i] = patterns.get(i).getLabel(offset);
        }

        return labels;
    }

    public static int[] getColors() {
        return getColors(null);
    }

    public static int[] getColors(String name) {
        int[] colors = new int[patterns.size()];

        for (int i = 0; i < colors.length; i++) {
            if (name == null) {
                colors[i] = patterns.get(i).getColor();
            } else if (patterns.get(i).name().equals(name)) {
                colors[0] = patterns.get(i).getColor();
            } else {
                colors[i] = Color.parseColor("#000000");
            }
        }

        return colors;
    }

    public static String[] getNames() {
        String[] names = new String[patterns.size()];

        for (int i = 0; i < names.length; i++) {
            names[i] = patterns.get(i).mName;
        }

        return names;
    }

    public static void revertEditPattern(Pattern editPattern) {
        LocalDate today = LocalDate.now();
        int offset = (today.getDayOfWeek() - (today.getDayOfMonth()%7)+8)%7 - 1;
        LocalDate firstDate = today.minusDays(today.getDayOfMonth() + offset); // Date of first day in last month.

        for (int i = 0; i < patterns.size(); i++) {
            if (patterns.get(i).mID == editPattern.mID) {
                patterns.set(i, new Pattern(editPattern.mID, mRevertCode, firstDate));
            }
        }
    }
}

/**
 * Used to create a Pattern object, which holds all the necessary<br>
 * attributes for a pattern in the personal planner.
 *
 * @author J.B. DuBois
 */
class Pattern implements Cloneable {
    public static final int VISIBLE = 0x00000000;
    public static final int INVISIBLE = 0x00000001;
    public static final int ARCHIVED = 0x00000002;

    public String mName;
    public final int mID;
    private int mState;
    private ArrayList<Integer> mPattern; // TODO: Rename
    private ArrayList<String> mLabels;
    private ArrayList<Character> mIcons;
    private int mColor;
    private LocalDate mStartDate;
    private int delta_startDate_today; // daysBetween startDate and today.
    private int mDelta_firstDate_startDate; // daysBetween firstDate and startDate.

//    public Pattern(int id, String patternCode) {
//        String[] attr = patternCode.split(";;");
//
//        mID = id;
//        mName = attr[0]; // Gets the name of the pattern.
//        String INTEGERPATTERN = attr[1]; // Gets the integer pattern in string form.
//        mLabels = new ArrayList<>(Arrays.asList(attr[2].split(";"))); // Gets the list of labels used.
//        mColor = Color.parseColor("#"+attr[4]); // Gets the color.
//        mStartDate = LocalDate.parse(attr[5], DateTimeFormat.forPattern("MM/dd/yy"));
//        mState = Integer.parseInt(attr[6]);
//
//        /* Converts the integer pattern into an array. */
//        this.mPattern = new ArrayList<>();
//        for (int i = 0; i < INTEGERPATTERN.length(); i++) {
//            this.mPattern.add(Integer.parseInt(""+INTEGERPATTERN.charAt(i)));
//        }
//
//        LocalDate today = LocalDate.now();
//        delta_startDate_today = Days.daysBetween(mStartDate, today).getDays();
//    }

    public Pattern(int id, String patternCode, LocalDate firstDate) {
        String[] attr = patternCode.split(";;");

        mID = id;
        mName = attr[0]; // Gets the name of the pattern.
        String INTEGERPATTERN = attr[1]; // Gets the integer pattern in string form.
        mLabels = new ArrayList<>(Arrays.asList(attr[2].split(";"))); // Gets the list of labels used.
        String[] icons = attr[3].split(";"); // Gets the list of icons used.
        mColor = Color.parseColor("#"+attr[4]); // Gets the color.
        mStartDate = LocalDate.parse(attr[5], DateTimeFormat.forPattern("MM/dd/yy"));

        this.mIcons = new ArrayList<>();
        for (String icon : icons) {
            this.mIcons.add(icon.charAt(0));
        }

        /* Converts the integer pattern into an array. */
        mPattern = new ArrayList<>();
        for (int i = 0; i < INTEGERPATTERN.length(); i++) {
            mPattern.add(Integer.parseInt(""+INTEGERPATTERN.charAt(i)));
        }

        mDelta_firstDate_startDate = Days.daysBetween(firstDate, mStartDate).getDays();

        LocalDate today = LocalDate.now();
        delta_startDate_today = Days.daysBetween(mStartDate, today).getDays();
    }

    public void updateFirstDate(LocalDate firstDate) {
        mDelta_firstDate_startDate = Days.daysBetween(firstDate, mStartDate).getDays();
    }

    public LocalDate startDate() {
        return mStartDate;
    }

    public void updateStartDate(LocalDate startDate) {
        mStartDate = startDate;
    }

    public void setState(int state) {
        mState = state;
    }

    public int getState() {
        return mState;
    }

    public void setName(String name) {
        mName = name;

        if (mIcons.size() == 2) { // TODO: Use a better way of setting default icons.
            mIcons.set(1, mName.charAt(0));
        }
    }

    /* Gets the label referenced at the offset from the firstDate. */
    public String getLabel(int offset) {
        int index;

        if (offset >= mDelta_firstDate_startDate) {
            index = mPattern.get((offset- mDelta_firstDate_startDate) % mPattern.size());

            return mLabels.get(index);
        }

        return null;
    }

    public String name() {
        return mName;
    }

    public int length() {
        return mPattern.size();
    }

    public void cyclePatternIcon(int index) {
        mPattern.set(index, (mPattern.get(index) + 1) % mIcons.size());
    }

    /* Gets the icon referenced at the offset from the firstDate. */
    public Character getIcon(int offset) { // TODO: Edit
        int index;

        if (offset >= mDelta_firstDate_startDate) {
            index = mPattern.get((offset- mDelta_firstDate_startDate) % mPattern.size());

            return mIcons.get(index);
        }

        return ' ';
    }

    public void incrementLength() {
        mPattern.add(0);
    }

    public void decrementLength() {
        mPattern.remove(mPattern.size()-1);
    }

    public int getColor() {
        return mColor;
    }

    public String[] getLabels() {
        return mLabels.toArray(new String[0]);
    }

    public void setLabel(int index, String label) {
        mLabels.set(index, label);

        if (label.equals("Yes")) { // TODO: Use a better way of setting default icons.
            mIcons.set(index, mName.charAt(0));
        } else if (label.equals("No")) {
            mIcons.set(index, ' ');
        } else {
            mIcons.set(index, label.charAt(0));
        }
    }

    public void addLabel(String label) {
        mLabels.add(label);

        if (label.equals("Yes")) { // TODO: Use a better way of setting default icons.
            mIcons.add(mName.charAt(0));
        } else if (label.equals("No")) {
            mIcons.add(' ');
        } else {
            mIcons.add(label.charAt(0));
        }
    }

    public void removeLabel(int index) {
        mLabels.remove(index);
        mIcons.remove(index);
    }

    /* Returns the pattern code. */
    public String generateCode() {
        StringBuilder patternCode = new StringBuilder();

        patternCode.append(mName).append(";;"); // NAME
        for (int code : mPattern) { // CODE
            patternCode.append(code);
        }
        patternCode.append(";;");

        for (String label : mLabels) { // LABELS
            patternCode.append(label).append(";");
        }

        for (Character icon : mIcons) { // ICONS
            patternCode.append(";").append(icon);
        }
        patternCode.append(";;").append(String.format("%06X", 0xFFFFFF & mColor)); // TODO: Remove Hex representation for color.
        patternCode.append(";;").append(mStartDate.toString("MM/dd/yy"));
        patternCode.append(";;").append(mState);

        return patternCode.toString();
    }
}