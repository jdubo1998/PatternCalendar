package com.github.jdubo1998.patterncalendar;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity implements CalendarFragment.CalendarFragmentListener, PatternEditorFragment.PatternEditorFragmentListener {
    CalendarFragment calendarFragment;
    PatternEditorFragment patternEditorFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        /* Creates the calendar fragment view on the main activity. */
        calendarFragment = new CalendarFragment();
        transaction.add(R.id.calendarFragment, calendarFragment);
        transaction.addToBackStack(null);
        /* Creates the pattern editor fragment view on the main activity. */
        patternEditorFragment = new PatternEditorFragment();
        transaction.add(R.id.patternEditor, patternEditorFragment);
        transaction.addToBackStack(null);
        transaction.commit();

        //TODO: Delete these lines.
        String drink_s = "Drink;;0000010;;No;Yes;; ;D;;805140;;5/31/20";
        String workout_s = "Workout;;2340410;;None;Abs;Upper Body;Bike;Full Body;; ;A;U;B;F;;00804f;;5/31/20";
        String applock_s = "Applock;;1010010;;No;Yes;; ;A;;EBA000;;5/31/20";
        String cigar_s = "Cigar;;00000100000000;;No;Yes;; ;C;;803221;;6/7/20";
        PatternContainer.addPatterns(drink_s + ";;;" + workout_s + ";;;"  + applock_s + ";;;"  + cigar_s);
//        loadSharedPreferences(); // TODO:Use this line instead
    }

    public void saveSharedPreferences() {
        String name = "PATTERNS_PREFERENCES";
        SharedPreferences preferences = getSharedPreferences(name, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putInt("count", PatternContainer.count());
        for (int i = 0; i < PatternContainer.count(); i++) {
            editor.putString("ptrn" + i, PatternContainer.getPatternCode(i));
        }

        editor.apply();
    }

    public void loadSharedPreferences() {
        String name = "PATTERNS_PREFERENCES";
        SharedPreferences preferences = getSharedPreferences(name, MODE_PRIVATE);

        int count = preferences.getInt("count", 0);

        String patternCode;
        for (int i = 0; i < count; i++) {
            patternCode = preferences.getString("ptrn" + i, null);
            if (patternCode != null) {
                PatternContainer.addPattern(patternCode);
            }
        }
    }

    @Override
    public void enterEditMode() {
        calendarFragment.enterEditMode();
        patternEditorFragment.enterEditMode();
    }

    @Override
    public void exitEdit() {
        calendarFragment.exitEditMode();
        patternEditorFragment.exitEditMode();
    }
}
