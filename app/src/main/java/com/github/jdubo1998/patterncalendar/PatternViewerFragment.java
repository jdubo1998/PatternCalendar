package com.github.jdubo1998.patterncalendar;

import android.os.Bundle;
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
    SharedViewModel mViewModel;
    PatternViewerAdapter adapter;

    private int offset = 0; // TODO: Make local variable?

    public PatternViewerFragment() {
        super(R.layout.patternviewer_layout);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        /*---   Pattern Viewer Initialization.   ---*/
        ListView patternLabelsList = view.findViewById(R.id.patterns_list); // TODO: Change naming schemes.
        adapter = new PatternViewerAdapter(PatternsManager.getNames(), PatternsManager.getLabels(), PatternsManager.getColors());
        patternLabelsList.setAdapter(adapter);
        registerForContextMenu(patternLabelsList);

        mViewModel.getDayOffset().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                offset = integer;
                adapter.updateLabels(PatternsManager.getLabels(offset));
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

        switch (item.getItemId()) {
            case R.id.active_action:
                System.out.println("active_action");
                break;
            case R.id.edit_action:
                mViewModel.setEditPattern(PatternsManager.getPattern(info.position));
                break;
            default:
                return super.onContextItemSelected(item);
        }

        return true;
    }
}
