package com.github.jdubo1998.patterncalendar;

import android.graphics.Color;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.Arrays;

public class PatternContainer {
    private static final ArrayList<Pattern> PATTERNS = new ArrayList<Pattern>();
    private static int count = 0;
    public static Pattern editPattern;

    private PatternContainer() {}

    public static void addPatterns(String patternCodes) {
        for (String patternCode : patternCodes.split(";;;")) {
            addPattern(patternCode);
        }
    }

    public static void addPattern(String patternCode) {
        PATTERNS.add(new Pattern(patternCode));
        count++;
    }

    public static Pattern getPattern(String name) {
        for (Pattern pattern : PATTERNS) {
            if (pattern.name.equals(name)) {
                return pattern;
            }
        }

        return null;
    }

    public static void REFDATES(DateTime refDate) {
        for (Pattern pattern : PATTERNS) {
            pattern.REFDATE(refDate);
        }
    }

    public static String[] getIcons(int days) {
        return getIcons(days, null);
    }

    /* Returns a string array of each icon for the given date. */
    public static String[] getIcons(int days, String name) {
        String[] icons = new String[count];

        for (int i = 0; i < count; i++) {
            if (PATTERNS.get(i).name.equals(name) || name == null) {
                icons[i] = PATTERNS.get(i).getIcon(days);
            } else {
                icons[i] = "";
            }
        }

        return icons;
    }

    /* Returns a string array of each icon for the given date. */
    public static String[] getLabels(int days) {
        String[] labels = new String[count];

        for (int i = 0; i < count; i++) {
            labels[i] = PATTERNS.get(i).getLabel(days);
        }

        return labels;
    }

    public static int[] getColors() {
        int[] colors = new int[count];

        for (int i = 0; i < count; i++) {
            colors[i] = PATTERNS.get(i).getColor();
        }

        return colors;
    }

    public static String[] getNames() {
        String[] names = new String[count];

        for (int i = 0; i < count; i++) {
            names[i] = PATTERNS.get(i).name;
        }

        return names;
    }

    public static int count() {
        return count;
    }

    public static String getPatternCode(int index) {
        return PATTERNS.get(index).exportPatternCode();
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
    private ArrayList<Integer> codes;
    private ArrayList<String> labels;
    private ArrayList<String> icons;
    private int color;
    private DateTime refDate;
    private int days;

    public Pattern(String patternCode) {
        String[] attr = patternCode.split(";;");

        name = attr[0];
        String codes = attr[1];
        labels = new ArrayList<>(Arrays.asList(attr[2].split(";")));
        icons = new ArrayList<>(Arrays.asList(attr[3].split(";")));
        color = Color.parseColor("#"+attr[4]);
        refDate = DateTime.parse(attr[5], DateTimeFormat.forPattern("MM/dd/yy"));

        /* Converts the code string into an array of integers. */
        this.codes = new ArrayList<>();
        for (int i = 0; i < codes.length(); i++) {
            this.codes.add(Integer.parseInt(""+codes.charAt(i)));
        }
    }

    public void REFDATE(DateTime refDate) {
        days = Days.daysBetween(this.refDate.toLocalDate(), refDate.toLocalDate()).getDays();
    }

    /* Gets the label referenced at the index. */
    public String getLabel(int index) {
        return labels.get(codes.get((days +index) % codes.size()));
    }

    /* Gets the icon referenced at the index. */
    public String getIcon(int index) {
        return icons.get(codes.get((days+index) % codes.size()));
    }

    public int length() {
        return codes.size();
    }

    /* Gets the code that is referenced at the index. */
    public void setCode(int index) {
        
    }

    public void incrementLength() {
        codes.add(0);
    }

    public void decrementLength() {
        codes.remove(codes.size()-1);
    }

    public int getColor() {
        return this.color;
    }

    /* Returns the pattern in the form: CODE;;LABELS;;ICONS */
    public String exportPatternCode() {
        String patternCode = "";

        patternCode += name + ";;"; // NAME
        for (int code : codes) { // CODE
            patternCode += code;
        }
        patternCode += ";;";
        for (String label : labels) { // LABELS
            patternCode += label + ";";
        }
        for (String icon : icons) { // ICONS
            patternCode += ";" + icon;
        }

        return patternCode; // NAME;;CODE;;LABELS;;ICONS
    }
}