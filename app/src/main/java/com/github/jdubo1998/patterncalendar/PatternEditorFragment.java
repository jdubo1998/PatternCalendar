package com.github.jdubo1998.patterncalendar;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class PatternEditorFragment extends Fragment {
    private PatternEditorFragmentListener listener;
    private boolean editMode = false;

    public interface PatternEditorFragmentListener {
        void exitEdit();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.patterneditor_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ImageButton exitEditMode = view.findViewById(R.id.TEST_exitEditMode);
        exitEditMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.exitEdit();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof PatternEditorFragmentListener) {
            listener = (PatternEditorFragmentListener) context;
        } else {
            throw new RuntimeException("ERROR: PatternEditorFragmentListener not implemented");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public void enterEditMode() {
        editMode = true;
        getView().setVisibility(View.VISIBLE);
    }

    public void exitEditMode() {
        editMode = true;
        getView().setVisibility(View.INVISIBLE);
    }
}
