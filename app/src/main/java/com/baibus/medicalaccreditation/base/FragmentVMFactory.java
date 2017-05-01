package com.baibus.medicalaccreditation.base;

import android.os.Bundle;
import android.support.annotation.NonNull;


public interface FragmentVMFactory<VM extends FragmentVM, V extends Fragment> {
    @NonNull VM create(V view);
    @NonNull VM restore(V view, @NonNull Bundle savedInstanceState);
}