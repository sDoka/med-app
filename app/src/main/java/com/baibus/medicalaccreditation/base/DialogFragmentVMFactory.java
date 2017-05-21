package com.baibus.medicalaccreditation.base;

import android.os.Bundle;
import android.support.annotation.NonNull;


public interface DialogFragmentVMFactory<VM extends DialogFragmentVM, V extends DialogFragment> {
    @NonNull
    VM create(V view);
    @NonNull VM restore(V view, @NonNull Bundle savedInstanceState);
}
