package com.github.jdubo1998.patterncalendar;

import android.graphics.Color;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.Arrays;

public class PatternsManager {
    private static final ArrayList<Pattern> patterns = new ArrayList<>();
    public static Pattern editPattern;
    public static int delta_firstDate_today;

    private PatternsManager() {}

    public static void parseCode(String patternCodes) {
        DateTime today = DateTime.now();
        int offset = (today.getDayOfWeek() - (today.getDayOfMonth()%7)+8)%7 - 1;
        DateTime firstDate = today.minusDays(today.getDayOfMonth() + offset); // Date of first day in last month.

        for (String patternCode : patternCodes.split(";;;")) {
            addPattern(patternCode, firstDate);
        }

        delta_firstDate_today = Days.daysBetween(firstDate.toLocalDate(), today.toLocalDate()).getDays();
    }

    public static String generateCode(int index) {
        return patterns.get(index).generateCode();
    }

    public static void addPattern(String patternCode, DateTime firstDate) {
        patterns.add(new Pattern(patternCode, firstDate));
    }

    public static void addPattern(String patternCode) {
//        patterns.add(new Pattern(patternCode));
    }

    public static Pattern[] getPatterns() {
        return patterns.toArray(new Pattern[0]);
    }

    public static Pattern getPattern(int index) {
        return patterns.get(index);
    }

    public static Pattern getPattern(String name) {
        for (Pattern pattern : patterns) {
            if (pattern.name.equals(name)) {
                return pattern;
            }
        }

        return null;
    }

    public static void getStartDates(DateTime refDate) {
        ArrayList<String> startDates;

//        for (Pattern pattern : patterns) {
//            startDates.add(pattern.getStartDate);
//        }
    }

    public static void REFDATES(DateTime refDate) {
        for (Pattern pattern : patterns) {
            int deltaDays = 0;
            pattern.REFDATE(refDate, deltaDays);
        }
    }

    public static String[] getIcons() {
        return getIcons(null);
    }

    public static int getColor(int index) {
        return patterns.get(index).getColor();
    }

    public static String[] getLabels(String name) {
        for (int i = 0; i < patterns.size(); i++) {
            if (patterns.get(i).name.equals(name)) {
                return patterns.get(i).getLabels();
            }
        }
        return null;
//        return patterns.get(index).getLabels;
    }

    /* Returns a string array of each icon for the given date. */
    public static String[] getIcons(String name) {
        String[] icons = new String[42];

        for (int j = 0; j < icons.length; j++) {
            StringBuilder icon = new StringBuilder();

            for (int i = 0; i < 6; i++) {
                if (i < patterns.size() && (patterns.get(i).name.equals(name) || name == null)) {
                    icon.append(patterns.get(i).getIcon(j));
                } else {
                    icon.append(' ');
                }
            }

            icons[j] = icon.toString();
        }

        return icons;
    }

    /* Gets the label for today. */
    public static String[] getLabels() {
        return getLabels(delta_firstDate_today);
//        String[] labels = new String[patterns.size()];
//
//        for (int i = 0; i < labels.length; i++) {
//            labels[i] = patterns.get(i).getLabel();
//        }
//
//        return labels;
    }

    /* Returns the labels for the day where offset is the days after firstDate. */
    public static String[] getLabels(int offset) {
        String[] labels = new String[patterns.size()];

        for (int i = 0; i < labels.length; i++) {
            labels[i] = patterns.get(i).getLabel(offset);
        }

        return labels;
    }

    /* Returns a string array of each icon for the given date. */
    public static String[] getLabels(int days, int poop) {
        String[] labels = new String[patterns.size()];

        for (int i = 0; i < labels.length; i++) {
            labels[i] = patterns.get(i).getLabel(days);
        }

        return labels;
    }

    public static int[] getColors() {
        int[] colors = new int[patterns.size()];

        for (int i = 0; i < colors.length; i++) {
            colors[i] = patterns.get(i).getColor();
        }

        return colors;
    }

    public static String[] getNames() {
        String[] names = new String[patterns.size()];

        for (int i = 0; i < names.length; i++) {
            names[i] = patterns.get(i).name;
        }

        return names;
    }

    public static int count() {
        return patterns.size();
    }
}

/**
 * Used to create a Pattern object, which holds all the necessary<br>
 * attributes for a pattern in the personal planner.
 *
 * @author J.B. DuBois
 */
class Pattern {
    public String name = "";
    public String category = "";
    private String label;
    public boolean isActive;
    private ArrayList<Integer> INTEGERPATTERN; // TODO: Rename
    private ArrayList<String> labels;
    private ArrayList<Character> icons;
    private int color;
    private LocalDate startDate;
    private int delta_startDate_today; // daysBetween startDate and today.
    private int delta_firstDate_startDate; // daysBetween firstDate and startDate.

    public Pattern(String patternCode) {
        String[] attr = patternCode.split(";;");

        name = attr[0]; // Gets the name of the pattern.
        String INTEGERPATTERN = attr[1]; // Gets the integer pattern in string form.
        labels = new ArrayList<>(Arrays.asList(attr[2].split(";"))); // Gets the list of labels used.
//        icons = new ArrayList<>(Arrays.asList(attr[3].split(";"))); // Gets the list of icons used.
        color = Color.parseColor("#"+attr[4]); // Gets the color.
        startDate = LocalDate.parse(attr[5], DateTimeFormat.forPattern("MM/dd/yy"));
//        startDate = DateTime.parse(attr[5], DateTimeFormat.forPattern("MM/dd/yy"));

        /* Converts the integer pattern into an array. */
        this.INTEGERPATTERN = new ArrayList<>();
        for (int i = 0; i < INTEGERPATTERN.length(); i++) {
            this.INTEGERPATTERN.add(Integer.parseInt(""+INTEGERPATTERN.charAt(i)));
        }

        DateTime today = DateTime.now();
        delta_startDate_today = Days.daysBetween(startDate, today.toLocalDate()).getDays();
        label = labels.get(this.INTEGERPATTERN.get(delta_startDate_today % this.INTEGERPATTERN.size()));
    }

    public Pattern(String patternCode, DateTime firstDate) {
        String[] attr = patternCode.split(";;");

        name = attr[0]; // Gets the name of the pattern.
        String INTEGERPATTERN = attr[1]; // Gets the integer pattern in string form.
        labels = new ArrayList<>(Arrays.asList(attr[2].split(";"))); // Gets the list of labels used.
        String[] icons = attr[3].split(";"); // Gets the list of icons used.
        color = Color.parseColor("#"+attr[4]); // Gets the color.
        startDate = LocalDate.parse(attr[5], DateTimeFormat.forPattern("MM/dd/yy"));

        this.icons = new ArrayList<>();
        for (String icon : icons) {
            this.icons.add(icon.charAt(0));
        }

        /* Converts the integer pattern into an array. */
        this.INTEGERPATTERN = new ArrayList<>();
        for (int i = 0; i < INTEGERPATTERN.length(); i++) {
            this.INTEGERPATTERN.add(Integer.parseInt(""+INTEGERPATTERN.charAt(i)));
        }

        delta_firstDate_startDate = Days.daysBetween(firstDate.toLocalDate(), startDate).getDays();

        DateTime today = DateTime.now();
        delta_startDate_today = Days.daysBetween(startDate, today.toLocalDate()).getDays();
        label = labels.get(this.INTEGERPATTERN.get(delta_startDate_today % this.INTEGERPATTERN.size()));
    }

    public void REFDATE(DateTime refDate, int deltaDays) {
        deltaDays = Days.daysBetween(this.startDate, refDate.toLocalDate()).getDays();
    }

    public LocalDate startDate() {
        return startDate;
    }

    /* Gets the label referenced at the offset from the firstDate. */
    public String getLabel(int offset) {
        int index;

        if (offset >= delta_firstDate_startDate) {
            index = INTEGERPATTERN.get((offset-delta_firstDate_startDate) % INTEGERPATTERN.size());

            return labels.get(index);
        }

        return null;
    }

    public String[] getLabels() {
        return labels.toArray(new String[0]);
    }

    public int length() {
        return INTEGERPATTERN.size();
    }

    public void cyclePatternIcon(int index) {
        INTEGERPATTERN.set(index, (INTEGERPATTERN.get(index) + 1) % icons.size());
    }

    /* Gets the icon referenced at the offset from the firstDate. */
    public Character getIcon(int offset) { // TODO: Edit
        int index;

        if (offset >= delta_firstDate_startDate) {
            index = INTEGERPATTERN.get((offset-delta_firstDate_startDate) % INTEGERPATTERN.size());

            return icons.get(index);
        }

        return ' ';
    }

    public int count() {
        return INTEGERPATTERN.size();
    }

    /* Gets the code that is referenced at the index. */
    public void setCode(int index) {

    }

    public void incrementLength() {
        INTEGERPATTERN.add(0);
    }

    public void decrementLength() {
        INTEGERPATTERN.remove(INTEGERPATTERN.size()-1);
    }

    public int getColor() {
        return this.color;
    }

    /* Returns the pattern in the form: CODE;;LABELS;;ICONS */
    public String generateCode() {
        StringBuilder patternCode = new StringBuilder();

        patternCode.append(name).append(";;"); // NAME
        for (int code : INTEGERPATTERN) { // CODE
            patternCode.append(code);
        }
        patternCode.append(";;");
        for (String label : labels) { // LABELS
            patternCode.append(label).append(";");
        }
        for (Character icon : icons) { // ICONS
            patternCode.append(";").append(icon);
        }

        return patternCode.toString(); // NAME;;CODE;;LABELS;;ICONS
    }
}