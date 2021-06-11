package com.github.jdubo1998.patterncalendar;

import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class PatternViewerFragment extends Fragment {
    public static final String TAG = PatternViewerFragment.class.getSimpleName();
    SharedViewModel mViewModel;
    PatternViewerAdapter adapter;

    private int offset = 0; // TODO: Make local variable?

    public PatternViewerFragment() {
        super(R.layout.patternviewer_layout);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated");

        mViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        /*---   Pattern Viewer Initialization   ---*/
        ListView patternLabelsList = view.findViewById(R.id.patterns_list); // TODO: Change naming schemes.
        adapter = new PatternViewerAdapter(PatternsManager.getNames(), PatternsManager.getLabels(), PatternsManager.getColors());
        patternLabelsList.setAdapter(adapter);
        registerForContextMenu(patternLabelsList);

        /*---   Day Offset Observer   ---*/
        mViewModel.getDayOffset().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                offset = integer;
                adapter.updateLabels(PatternsManager.getLabels(offset));
            }
        });

        patternLabelsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /* If a new pattern is clicked, it creates and adds a new empty pattern as the edit pattern. */
                if (position == PatternsManager.getNames().length) {
                    Pattern pattern = new Pattern(PatternsManager.getNames().length);
                    PatternsManager.addPattern(pattern);
                    mViewModel.setEditPattern(pattern);
                }
            }
        });
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = requireActivity().getMenuInflater();
        inflater.inflate(R.menu.patternviewer_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        if (item.getItemId() == R.id.active_action) {
            if (PatternsManager.getState(info.position) == Pattern.VISIBLE) {
                PatternsManager.setState(info.position, Pattern.INVISIBLE);
            } else {
                PatternsManager.setState(info.position, Pattern.VISIBLE);
            }

            adapter.update(PatternsManager.getNames(), PatternsManager.getLabels(offset), PatternsManager.getColors());
            mViewModel.setEditPattern(null); // TODO: Better way to update.

        } else if (item.getItemId() == R.id.edit_action) {
            mViewModel.setEditPattern(PatternsManager.getPattern(info.position));

        } else if (item.getItemId() == R.id.delete_action) {
            PatternsManager.removePattern(info.position);
            adapter.update(PatternsManager.getNames(), PatternsManager.getLabels(offset), PatternsManager.getColors());
            mViewModel.setEditPattern(null); // TODO: Better way to update.
        }

        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.update(PatternsManager.getNames(), PatternsManager.getLabels(), PatternsManager.getColors());
    }
}
