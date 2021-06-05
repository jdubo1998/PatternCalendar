package com.github.jdubo1998.patterncalendar;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Objects;

public class SharedViewModel extends ViewModel {
    public MutableLiveData<Pattern> mEditPattern = new MutableLiveData<>();
    private MutableLiveData<Integer> mDayOffset = new MutableLiveData<>();
    private MutableLiveData<Boolean> mSave = new MutableLiveData<>();

    public LiveData<Pattern> getEditPattern() {
        return mEditPattern;
    }

    public void setEditPattern(Pattern pattern) {
//        if (pattern == null && !Objects.requireNonNull(mSave.getValue())) {
//            PatternsManager.revertEditPattern(mEditPattern.getValue());
//        }

        mEditPattern.setValue(pattern);
    }

    public LiveData<Integer> getDayOffset() {
        return mDayOffset;
    }

    public void setDayOffset(int dayOffset) {
        mDayOffset.setValue(dayOffset);
    }

    public LiveData<Boolean> getSave() {
        return mSave;
    }

    public void setSave(boolean save) {
        mSave.setValue(save);
    }
}
