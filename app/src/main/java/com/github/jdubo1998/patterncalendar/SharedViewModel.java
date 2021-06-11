package com.github.jdubo1998.patterncalendar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<Pattern> mEditPattern = new MutableLiveData<>();
    private final MutableLiveData<Integer> mDayOffset = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mSave = new MutableLiveData<>();

    public LiveData<Pattern> getEditPattern() {
        return mEditPattern;
    }

    public void setEditPattern(Pattern pattern) {
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
