package com.github.jdubo1998.patterncalendar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    public MutableLiveData<Pattern> mEditPattern = new MutableLiveData<>();
    private MutableLiveData<Integer> mDayOffset = new MutableLiveData<>();

    public void setEditPattern(Pattern pattern) {
        mEditPattern.setValue(pattern);
    }

    public LiveData<Pattern> getEditPattern() {
        return mEditPattern;
    }

    public void setDayOffset(int dayOffset) {
        mDayOffset.setValue(dayOffset);
    }

    public LiveData<Integer> getDayOffset() {
        return mDayOffset;
    }
}
