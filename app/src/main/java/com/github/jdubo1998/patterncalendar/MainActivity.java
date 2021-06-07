package com.github.jdubo1998.patterncalendar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    private long backPressTimer;
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
        Log.d(TAG, "onCreate: onCreate");

        String drink_s = "Drink;;0000010;;No;Yes;; ;D;;805140;;06/07/20;;0";
        String workout_s = "Workout;;2340410;;None;Abs;Upper Body;Bike;Full Body;; ;A;U;B;F;;00804f;;04/04/21;;0";
        String applock_s = "Applock;;1010010;;No;Yes;; ;A;;EBA000;;06/07/20;;0";
        String cigar_s = "Cigar;;00000100000000;;No;Yes;; ;C;;803221;;04/11/21;;0";
        String ganja_s = "Chill;;01000001000100;;No;Yes;; ;C;;00800b;;04/04/21;;0";
        PatternsManager.parseCode(drink_s + ";;;" + workout_s + ";;;"  + applock_s + ";;;"  + cigar_s + ";;;"  + ganja_s);

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

        transaction.commit();

        mViewModel.getEditPattern().observe(this, new Observer<Pattern>() {
            @Override
            public void onChanged(Pattern pattern) {
                if (pattern == null) {
                    inEditMode = false;
                } else if (!inEditMode) {
                    inEditMode = true;
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.bottom_fragment, patternEditorFragment).addToBackStack(null).commit();
                }
            }
        });
    }

    public void saveSharedPreferences() {

    }

    public void loadSharedPreferences() {
    }

    @Override
    public void onBackPressed() {
        if (!inEditMode) {
            int TIME_INTERVAL = 5000;

            if (backPressTimer + TIME_INTERVAL > System.currentTimeMillis()) {
                super.onBackPressed();
                saveSharedPreferences();
                return;
            } else {
                Toast.makeText(getBaseContext(), "Tap back again to exit.", Toast.LENGTH_SHORT).show();
            }

            backPressTimer = System.currentTimeMillis();
        } else {
            AlertDialog.Builder dialogMenu = new AlertDialog.Builder(this);
            dialogMenu.setTitle("Exit Pattern Editor");

            dialogMenu.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mViewModel.setSave(true);
                    mViewModel.setEditPattern(null);
                    MainActivity.super.onBackPressed();
                }
            });

            dialogMenu.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            dialogMenu.setNeutralButton("Don't Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mViewModel.setSave(false);
                    mViewModel.setEditPattern(null);
                    MainActivity.super.onBackPressed();
                }
            });

            dialogMenu.show();
        }
    }
}
