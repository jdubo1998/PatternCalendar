package com.github.jdubo1998.patterncalendar;

import android.graphics.Color;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.Arrays;

public class PatternsManager {
    private static final ArrayList<Pattern> mPatterns = new ArrayList<>();
    public static int delta_firstDate_today;
    private static String mRevertCode;

    private PatternsManager() {}

    public static void parseCode(String patternCodes) {
        mPatterns.clear();

        if (patternCodes == null || patternCodes.equals("")) {
            return;
        }

        LocalDate today = LocalDate.now();
        int offset = (today.getDayOfWeek() - (today.getDayOfMonth()%7)+8)%7 - 1;
        LocalDate firstDate = today.minusDays(today.getDayOfMonth() + offset); // Date of first day in last month.

        for (String patternCode : patternCodes.split(";;;")) {
            addPattern(patternCode, firstDate);
        }

        delta_firstDate_today = Days.daysBetween(firstDate, today).getDays();
    }

    public static String generateCode() {
        StringBuilder code = new StringBuilder();

        for (Pattern pattern : mPatterns) {
            code.append(pattern.generateCode()).append(";;;");
        }

        return code.toString();
    }

    /* Pattern Methods */

    public static Pattern getPattern(int index) {
        mRevertCode = mPatterns.get(index).generateCode();
        return mPatterns.get(index);
    }

    public static Pattern getPattern(String name) {
        for (Pattern pattern : mPatterns) {
            if (pattern.mName.equals(name)) {
                return pattern;
            }
        }

        return null;
    }

    public static void addPattern(Pattern pattern) {
        mRevertCode = "null";
        mPatterns.add(pattern);
    }

    public static void addPattern(String patternCode, LocalDate firstDate) {
        mPatterns.add(new Pattern(mPatterns.size(), patternCode, firstDate));
    }

    public static void removePattern(int index) {
        mPatterns.remove(index);
    }

    public static void revertEditPattern(Pattern editPattern) {
        if (mRevertCode.equals("null")) { // TODO: Use better way to check if pattern is a new pattern.
            mPatterns.remove(mPatterns.size()-1);
            return;
        }

        LocalDate today = LocalDate.now();
        int offset = (today.getDayOfWeek() - (today.getDayOfMonth()%7)+8)%7 - 1;
        LocalDate firstDate = today.minusDays(today.getDayOfMonth() + offset); // Date of first day in last month.

        for (int i = 0; i < mPatterns.size(); i++) {
            if (mPatterns.get(i).mID == editPattern.mID) {
                mPatterns.set(i, new Pattern(editPattern.mID, mRevertCode, firstDate));
            }
        }
    }

    /* ---   Label Methods   --- */

    /* Gets the label for today. */
    public static String[] getLabels() {
        return getLabels(delta_firstDate_today);
    }

    /* Returns the labels for the day where offset is the days after firstDate. */
    public static String[] getLabels(int offset) {
        String[] labels = new String[mPatterns.size()];

        for (int i = 0; i < labels.length; i++) {
            labels[i] = mPatterns.get(i).getLabel(offset);
        }

        return labels;
    }

    /* ---   Icon Methods   --- */

    public static String[] getIcons(LocalDate firstDate, String name) {
        String[] icons = new String[42];
        setFirstDates(firstDate);

        for (int j = 0; j < icons.length; j++) {
            char[] iconList = new char[6];
            Arrays.fill(iconList, ' ');
            int index = 0;

            for (int i = 0; i < mPatterns.size(); i++) {
                if (index < 6 && (mPatterns.get(i).mName.equals(name) || name == null) && mPatterns.get(i).getState() == Pattern.VISIBLE) {
                    iconList[index] = mPatterns.get(i).getIcon(j);
                    index++;
                }
            }

            icons[j] = String.valueOf(iconList);
        }

        return icons;
    }

    /* ---   Extra Methods   --- */

    public static void setFirstDates(LocalDate firstDate) {
        for (Pattern pattern : mPatterns) {
            pattern.updateFirstDate(firstDate);
        }
    }

    public static void setState(int index, int state) {
        mPatterns.get(index).setState(state);
    }

    public static int getState(int index) {
        return mPatterns.get(index).getState();
    }

    public static int[] getColors() {
        return getColors(null);
    }

    public static int[] getColors(String name) {
        int[] colors = new int[mPatterns.size()];

        for (int i = 0; i < colors.length; i++) {
            if (name == null) {
                colors[i] = mPatterns.get(i).getColor();
            } else if (mPatterns.get(i).name().equals(name)) {
                colors[0] = mPatterns.get(i).getColor();
            } else {
                colors[i] = Color.parseColor("#000000");
            }
        }

        return colors;
    }

    public static String[] getNames() {
        String[] names = new String[mPatterns.size()];

        for (int i = 0; i < names.length; i++) {
            names[i] = mPatterns.get(i).mName;
        }

        return names;
    }
}

/**
 * Used to create a Pattern object, which holds all the necessary<br>
 * attributes for a pattern in the personal planner.
 *
 * @author J.B. DuBois
 */
class Pattern {
    public static final int VISIBLE = 0x00000000;
    public static final int INVISIBLE = 0x00000001;
    public static final int ARCHIVED = 0x00000002;

    public String mName;
    public final int mID;
    private int mState;
    private final ArrayList<Integer> mPattern;
    private final ArrayList<String> mLabels;
    private final ArrayList<Character> mIcons;
    private int mColor;
    private LocalDate mStartDate;
    private int mDelta_firstDate_startDate; // daysBetween firstDate and startDate.

    public Pattern(int id) {
        LocalDate today = LocalDate.now();
        int offset = (today.getDayOfWeek() - (today.getDayOfMonth()%7)+8)%7 - 1;
        LocalDate firstDate = today.minusDays(today.getDayOfMonth() + offset); // Date of first day in last month.

        mID = id;
        mName = "";
        mPattern = new ArrayList<>();
        mLabels = new ArrayList<>();
        mIcons = new ArrayList<>();
        mColor = 0xFF000000;
        mStartDate = LocalDate.now();
        mState = 0;

        mDelta_firstDate_startDate = Days.daysBetween(firstDate, mStartDate).getDays();
    }

    public Pattern(int id, String patternCode, LocalDate firstDate) {
        String[] attr = patternCode.split(";;");

        mID = id;
        mName = attr[0]; // Gets the name of the pattern.
        String Pattern = attr[1]; // Gets the integer pattern in string form.
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
        for (int i = 0; i < Pattern.length(); i++) {
            mPattern.add(Integer.parseInt(""+Pattern.charAt(i)));
        }

        mDelta_firstDate_startDate = Days.daysBetween(firstDate, mStartDate).getDays();

        LocalDate today = LocalDate.now();
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

    /* ---   Label Methods   --- */

    public String[] getLabels() {
        return mLabels.toArray(new String[0]);
    }

    /* Gets the label referenced at the offset from the firstDate. */
    public String getLabel(int offset) {
        int index;

        if (offset >= mDelta_firstDate_startDate) {
            if (mPattern.size() > 0 && mLabels.size() > 0) {
                index = mPattern.get((offset- mDelta_firstDate_startDate) % mPattern.size());
            } else {
                return null;
            }

            return mLabels.get(index);
        }

        return null;
    }

    public void setLabel(int index, String label) {
        mLabels.set(index, label);

        if (label.equals("Yes")) { // TODO: Use a better way of setting default icons.
            if (!mName.isEmpty()) {
                mIcons.set(index, mName.charAt(0));
            }
        } else if (label.equals("No")) {
            mIcons.set(index, ' ');
        } else {
            mIcons.set(index, label.charAt(0));
        }
    }

    public void addLabel(String label) {
        mLabels.add(label);

        if (label.equals("Yes")) { // TODO: Use a better way of setting default icons.
            if (!mName.isEmpty()) {
                mIcons.add(mName.charAt(0));
            }
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

    /* ---   Icon Methods   --- */

    /* Gets the icon referenced at the offset from the firstDate. */
    public Character getIcon(int offset) {
        int index;

        if (offset >= mDelta_firstDate_startDate) {
            if (mPattern.size() > 0 && mIcons.size() > 0) {
                index = mPattern.get((offset - mDelta_firstDate_startDate) % mPattern.size());
            } else {
                return ' ';
            }

            return mIcons.get(index);
        }

        return ' ';
    }

    /* ---   Extra Methods   --- */

    public String name() {
        return mName;
    }

    public void setName(String name) {
        mName = name;

        if (mIcons.size() == 2) { // TODO: Use a better way of setting default icons.
            mIcons.set(1, mName.charAt(0));
        }
    }

    public void cyclePatternIcon(int index) {
        if (mIcons.size() > 0) {
            mPattern.set(index, (mPattern.get(index) + 1) % mIcons.size());
        }
    }

    public void incrementLength() {
        mPattern.add(0);
    }

    public void decrementLength() {
        mPattern.remove(mPattern.size()-1);
    }

    public int length() {
        return mPattern.size();
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        mColor = color;
    }

    public LocalDate startDate() {
        return mStartDate;
    }

    public void updateStartDate(LocalDate startDate) {
        mStartDate = startDate;
    }

    public void updateFirstDate(LocalDate firstDate) {
        mDelta_firstDate_startDate = Days.daysBetween(firstDate, mStartDate).getDays();
    }

    public void setState(int state) {
        mState = state;
    }

    public int getState() {
        return mState;
    }
}