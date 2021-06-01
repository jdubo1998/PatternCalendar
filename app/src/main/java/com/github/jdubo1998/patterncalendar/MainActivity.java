package com.github.jdubo1998.patterncalendar;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class MainActivity extends AppCompatActivity {
    private SharedViewModel mViewModel;
    private boolean inEditMode;
    FragmentManager manager;
    CalendarFragment calendarFragment;
    PatternViewerFragment patternViewerFragment;
    PatternEditorFragment patternEditorFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        mViewModel = new ViewModelProvider(this).get(SharedViewModel.class);

        manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        /* Creates the calendar fragment view on the main activity. */
        calendarFragment = new CalendarFragment();
        transaction.add(R.id.top_fragment, calendarFragment);

        /* Creates the pattern viewer fragment view on the main activity. */
        patternViewerFragment = new PatternViewerFragment();
        transaction.add(R.id.bottom_fragment, patternViewerFragment);

        patternEditorFragment = new PatternEditorFragment();

        mViewModel.getEditPattern().observe(this, new Observer<Pattern>() {
            @Override
            public void onChanged(Pattern pattern) {
                if (pattern == null) {
                    inEditMode = false;
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.bottom_fragment, patternViewerFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else if (!inEditMode) {
                    inEditMode = true;
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.bottom_fragment, patternEditorFragment);
                    transaction.commit();
                }
            }
        });

//        mViewModel.getEditMode().observe(this, new Observer<Boolean>() {
//            @Override
//            public void onChanged(Boolean aBoolean) {
//                if (aBoolean) {
//                    FragmentTransaction transaction = manager.beginTransaction();
//                    transaction.replace(R.id.bottom_fragment, patternEditorFragment);
//                    transaction.addToBackStack(null);
//                    transaction.commit();
//                } else {
//                    FragmentTransaction transaction = manager.beginTransaction();
//                    transaction.replace(R.id.bottom_fragment, patternViewerFragment);
//                    transaction.commit();
//                }
//            }
//        });
//
//        /* Creates the pattern editor fragment view on the main activity. */
//        patternEditorFragment = new PatternEditorFragment();
//        transaction.add(R.id.patternEditor, patternEditorFragment);
//        transaction.addToBackStack(null);

        transaction.commit();

        //TODO: Delete these lines.
        String drink_s = "Drink;;0000010;;No;Yes;; ;D;;805140;;6/7/20";
        String workout_s = "Workout;;2340410;;None;Abs;Upper Body;Bike;Full Body;; ;A;U;B;F;;00804f;;4/4/21";
        String applock_s = "Applock;;1010010;;No;Yes;; ;A;;EBA000;;6/7/20";
        String cigar_s = "Cigar;;00000100000000;;No;Yes;; ;C;;803221;;4/11/21";
        String ganja_s = "Chill;;01000001000100;;No;Yes;; ;C;;00800b;;4/4/21";
        PatternsManager.parseCode(drink_s + ";;;" + workout_s + ";;;"  + applock_s + ";;;"  + cigar_s + ";;;"  + ganja_s);
    }

    // TODO: Edit how shared preferences are saved.
    public void saveSharedPreferences() {
//        String name = "PATTERNS_PREFERENCES";
//        SharedPreferences preferences = getSharedPreferences(name, MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//
//        editor.putInt("count", PatternsManager.count());
//        for (int i = 0; i < PatternsManager.count(); i++) {
//            editor.putString("ptrn" + i, PatternsManager.generateCode(i));
//        }
//
//        editor.apply();
    }

    public void loadSharedPreferences() {
//        String name = "PATTERNS_PREFERENCES";
//        SharedPreferences preferences = getSharedPreferences(name, MODE_PRIVATE);
//
//        int count = preferences.getInt("count", 0);
//
//        String patternCode;
//        for (int i = 0; i < count; i++) {
//            patternCode = preferences.getString("ptrn" + i, null);
//            if (patternCode != null) {
//                PatternsManager.addPattern(patternCode);
//            }
//        }
    }
}
