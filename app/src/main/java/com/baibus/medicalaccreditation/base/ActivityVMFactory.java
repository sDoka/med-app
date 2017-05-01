package com.baibus.medicalaccreditation.base;

import android.os.Bundle;
import android.support.annotation.NonNull;

public interface ActivityVMFactory<VM extends ActivityVM, V extends Activity> {
    @NonNull VM create(V view);
    @NonNull VM restore(V view, @NonNull Bundle savedInstanceState);
}
