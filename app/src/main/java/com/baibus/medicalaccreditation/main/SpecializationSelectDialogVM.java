package com.baibus.medicalaccreditation.main;

import android.app.Dialog;
import android.content.DialogInterface;
import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.baibus.medicalaccreditation.MedApplication;
import com.baibus.medicalaccreditation.base.DialogFragmentVM;
import com.baibus.medicalaccreditation.base.DialogFragmentVMFactory;
import com.baibus.medicalaccreditation.common.db.entities.Specialization;
import com.baibus.medicalaccreditation.databinding.ItemSpecializationBinding;

import java.util.Collections;
import java.util.List;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_NEUTRAL;
import static android.content.DialogInterface.BUTTON_POSITIVE;

/**
 * Created by Android Studio.
 * User: yanbaev.is
 * Date: 22.05.2017
 * Time: 1:18
 * To change this template use File | settings | File Templates.
 */
class SpecializationSelectDialogVM extends DialogFragmentVM<SpecializationSelectDialog>
        implements DialogInterface.OnClickListener {
    final static String ARGUMENT_IS_NEED = "need";

    private final static String BUNDLE_SELECTION = "selectedPosition";

    private int selectedPosition = -1;
    private final ObservableArrayList<Specialization> specializations = new ObservableArrayList<>();

    private SpecializationSelectDialogVM(SpecializationSelectDialog fragment, @Nullable Bundle savedInstanceState) {
        super(fragment, savedInstanceState);

        fragment
                .addSubscription(MedApplication.getInstance()
                .subscribeOnSpecializations(specializations::addAll));

        if (savedInstanceState != null) {
            selectedPosition = savedInstanceState.getInt(BUNDLE_SELECTION, -1);
        }

        fragment.setCancelable(false);
    }

    @Override
    public Dialog onBuildDialog(AlertDialog.Builder builder, Bundle params) {
        builder.setTitle("Выберите специализацию");
        builder.setSingleChoiceItems(new SpecializationAdapter(specializations), selectedPosition, this);
        builder.setPositiveButton(android.R.string.ok, this);
        if (fragment.getArguments().getBoolean(ARGUMENT_IS_NEED)) {
            builder.setCancelable(false);
        } else {
            builder.setNegativeButton(android.R.string.cancel, this);
            builder.setCancelable(true);
        }

        return builder.create();
    }

    @Override
    public void onShow(AlertDialog dialog) {
        super.onShow(dialog);
        dialog.getButton(BUTTON_POSITIVE).setEnabled(selectedPosition >= 0);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(BUNDLE_SELECTION, selectedPosition);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case BUTTON_POSITIVE:
                MedApplication.getInstance()
                        .setSpecializationId(specializations.get(selectedPosition).getId());
            case BUTTON_NEUTRAL:
            case BUTTON_NEGATIVE:
                dialog.dismiss();
                break;
            default:
                if (dialog == fragment.getDialog() && which >= 0) {
                    ((AlertDialog) dialog).getButton(BUTTON_POSITIVE).setEnabled(true);
                    selectedPosition = which;
                }
        }
    }

    private static class SpecializationAdapter extends BaseAdapter {
        final List<Specialization> mObjects;

        SpecializationAdapter(List<Specialization> objects) {
            mObjects = Collections.unmodifiableList(objects);
        }

        @Override
        public int getCount() {
            return mObjects.size();
        }

        @Nullable
        @Override
        public Specialization getItem(int position) {
            return mObjects.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ItemSpecializationBinding binding;
            if (convertView == null) {
                binding = ItemSpecializationBinding.inflate(LayoutInflater.from(parent.getContext()));
                convertView = binding.getRoot();
                convertView.setTag(binding);
            } else {
                binding = (ItemSpecializationBinding) convertView.getTag();
            }
            binding.setItem(getItem(position));
            binding.executePendingBindings();
            return convertView;
        }
    }

    static class VMFactory implements DialogFragmentVMFactory<SpecializationSelectDialogVM, SpecializationSelectDialog> {
        @NonNull
        @Override
        public SpecializationSelectDialogVM create(SpecializationSelectDialog view) {
            return new SpecializationSelectDialogVM(view, null);
        }

        @NonNull
        @Override
        public SpecializationSelectDialogVM restore(SpecializationSelectDialog view, @NonNull Bundle savedInstanceState) {
            return new SpecializationSelectDialogVM(view, savedInstanceState);
        }
    }
}
