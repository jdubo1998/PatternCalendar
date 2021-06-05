package com.github.jdubo1998.patterncalendar;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Months;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
//    String drink_s = "Drink;;0000010;;No;Yes;; ;D;;805140;;5/31/20";
//    String workout_s = "Workout;;2340410;;None;Abs;Upper Body;Bike;Full Body;; ;A;U;B;F;;00804f;;5/31/20";
//    String applock_s = "Applock;;1010010;;No;Yes;; ;A;;EBA000;;5/2/21";
//    String cigar_s = "Cigar;;00000100000000;;No;Yes;; ;C;;803221;;4/25/21";
//    String cigar_s = "Cigar;;00000100000000;;No;Yes;; ;C;;803221;;4/11/21";
//    String code = drink_s + ";;;" + workout_s + ";;;"  + applock_s + ";;;"  + cigar_s;

    String code1_s = "0 to 5;;012345;;zero;one;two;three;four;five;;0;1;2;3;4;5;;000000;;4/14/21;;0";
    String code2_s = "Ramp Up;;001012012301234012345;;zero;one;two;three;four;five;;0;1;2;3;4;5;;000000;;5/14/21;;0";
    String code = code1_s + ";;;" + code2_s;

    Pattern mEditPattern = new Pattern(0, code2_s, LocalDate.now());
    int[] mDaysOfMonth = new int[42];
    int curMonthOffset = 0;

    @Test
    public void calendarTests() {
        DateTime today = DateTime.now();
        int offset = (today.getDayOfWeek() - (today.getDayOfMonth()%7)+8)%7 - 1;
        DateTime firstDate = today.minusDays(today.getDayOfMonth() + offset); // First day in calendar view.

        PatternsManager.parseCode(code);
        String[] names = PatternsManager.getNames();
        String[] labels = PatternsManager.getLabels();

        for (int i = 0; i < 42; i++) {
            System.out.print(StringUtils.center(Integer.toString(firstDate.plusDays(i).getDayOfMonth()),4));

            if ((i+1)%7 == 0) {
                System.out.print("\n");
            } else {
                System.out.print("|");
            }
        }

        System.out.print("\n");

        String[] icons = PatternsManager.getIcons();

        for (int i = 0; i < 42; i++) {
            System.out.print(StringUtils.center(icons[i],4));

            if ((i+1)%7 == 0) {
                System.out.print("\n");
            } else {
                System.out.print("|");
            }
        }

        System.out.println("\n" + firstDate);
//        assertEquals(4, 2 + 2);
    }

    @Test
    public void getLabelsTest() {
        PatternsManager.parseCode(code);
        String[] names = PatternsManager.getNames();
        String[] labels = PatternsManager.getLabels();
        String[] labels_indexed1 = PatternsManager.getLabels(33);
        String[] labels_indexed2 = PatternsManager.getLabels(24);

        System.out.println();
        for (int i = 0; i < labels.length; i++) {
            System.out.println(String.format("%-10s: %s", names[i], labels[i]));
        }
        System.out.println();
        for (int i = 0; i < labels.length; i++) {
            System.out.println(String.format("%-10s: %s", names[i], labels_indexed1[i]));
        }
        System.out.println();
        for (int i = 0; i < labels.length; i++) {
            System.out.println(String.format("%-10s: %s", names[i], labels_indexed2[i]));
        }

//        assertEquals(4, 2 + 2);
    }

    @Test
    public void DateTimeFormatTest() {
        DateTime today = DateTime.now();
//        System.out.println(today.toString("MM/dd/yy"));
//        System.out.println(today.toString("MMMM"));
//        System.out.println(today.plusMonths(5).toString("MMMM"));

        System.out.println(today.toLocalDate().plusDays(-4).toString("MM/dd/yyyy h:mm"));
        System.out.println(today.plusDays(-4).plusMonths(9).toLocalDate().toString("MM/dd/yyyy h:mm"));

        System.out.println(Months.monthsBetween(today.toLocalDate(), today.plusMonths(5).toLocalDate()).getMonths());
    }

    @Test
    public void CalendarAdapterTest() {
//        LocalDate date = DateTime.now().toLocalDate();
        updateDaysOfMonths(mEditPattern.startDate());
    }

    private void updateDaysOfMonths(LocalDate date) {
        String patternName = null;
        int endMonth = 0;

//        monthYearText.setText(date.toString("MMMM yyyy"));
//        System.out.println("monthYearText: " + date.toString("MMMM yyyy"));
        int today_DayOfMonth = -1;

        int offset = (date.getDayOfWeek() - (date.getDayOfMonth()%7)+8)%7 - 1; // Number of days from previous month to fill at beginning of calendar.
        LocalDate firstDate = date.minusDays(date.getDayOfMonth() + offset); // DateTime of the first date for the calendar.

        for (int i = 0; i < 42; i++) {
            mDaysOfMonth[i] = firstDate.plusDays(i).getDayOfMonth();
            if (i > 7 && mDaysOfMonth[i] == 1) {
                endMonth = i-1;
            }
        }

        /* If the target current month is the month of today. Then set mToday_dayOfMonth, so that is shows up as red. */
        if (curMonthOffset == 0) {
            today_DayOfMonth = DateTime.now().getDayOfMonth();
        }

        /* If edit mode is active, only show icons for the pattern being edited. */
        if (mEditPattern != null) {
            patternName = mEditPattern.mName;
        }

        PatternsManager.parseCode(code);
        String[] icons = PatternsManager.getIcons();

//        System.out.println(mEditPattern.startDate().toLocalDate());
        int startPosition = Days.daysBetween(firstDate, mEditPattern.startDate()).getDays();
        int endPosition = startPosition + mEditPattern.length() - 1;
        int today = Days.daysBetween(firstDate, DateTime.now().toLocalDate()).getDays();

        int startMonth = offset + 1;
        System.out.println(endMonth);

//        System.out.println(startPosition);
//        System.out.println(endPosition);
//        System.out.println(today);

//        mAdapter.updateMonth(mDaysOfMonth, PatternsManager.getIcons(patternName), PatternsManager.getColors(), today_DayOfMonth);
        for (int i = 0; i < 42; i++) {
            if (i >= startMonth && i <= endMonth) {
                System.out.print("O");
            } else {
                System.out.print(".");
            }

            if ((i+1)%7 == 0) {
                System.out.print("\n");
            } else {
                System.out.print("|");
            }
        }
    }

    @Test
    public void ArrayListSizeTest() {
        ArrayList<Boolean> arr = new ArrayList<>();
        arr.add(true);
        arr.add(true);
        System.out.println(arr.size());
        arr.add(FooConstructor(arr.size()));
    }

    private Boolean FooConstructor(int size) {
        System.out.println(size);
        return false;
    }

    @Test
    public void ArrayListSetValueTest() {
        ArrayList<Boolean> arr = new ArrayList<>(Arrays.asList(true, true, false, true, false));

        for (int i = 0; i < arr.size(); i++) {
            arr.set(i, false);
        }

        for (Boolean i : arr) {
            System.out.println(i);
        }
    }
}

class StringUtils {
    public static String center(String s, int size) {
        return center(s, size, ' ');
    }

    public static String center(String s, int size, char pad) {
        if (s == null || size <= s.length())
            return s;

        StringBuilder sb = new StringBuilder(size);
        for (int i = 0; i < (size - s.length()) / 2; i++) {
            sb.append(pad);
        }
        sb.append(s);
        while (sb.length() < size) {
            sb.append(pad);
        }
        return sb.toString();
    }
}