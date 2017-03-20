package com.david.nytimessearch.fragments;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;

import com.david.nytimessearch.R;
import com.david.nytimessearch.databinding.FragmentSettingsBinding;
import com.david.nytimessearch.models.Settings;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by David on 3/17/2017.
 */

public class SettingsFragment extends DialogFragment implements DatePickerFragment.DatePickerDialogListener {

    EditText etDate;
    Spinner spSort;
    CheckBox cbArts;
    CheckBox cbFashion;
    CheckBox cbSports;
    Button btnSave;

    Settings settings;

    FragmentSettingsBinding binding;

    public SettingsFragment() {
        //Needs to be empty
    }

    public static SettingsFragment newInstance(Settings settings) {
        SettingsFragment frag = new SettingsFragment();
        Bundle args = new Bundle();
        args.putParcelable("settings", settings);
        frag.setArguments(args);
        return frag;
    }

    public interface SettingsDialogListener {
        void onSave(Settings settings);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, true);
        return binding.getRoot();
//        return inflater.inflate(R.layout.fragment_settings, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        etDate = (EditText) view.findViewById(R.id.etDate);
//        spSort = (Spinner) view.findViewById(R.id.spSort);
//        cbArts = (CheckBox) view.findViewById(R.id.cbArts);
//        cbFashion = (CheckBox) view.findViewById(R.id.cbFashion);
//        cbSports = (CheckBox) view.findViewById(R.id.cbSports);
//        btnSave = (Button) view.findViewById(R.id.btnSave);
        etDate = binding.etDate;
        spSort = binding.spSort;
        cbArts = binding.cbArts;
        cbFashion = binding.cbFashion;
        cbSports = binding.cbSports;
        btnSave = binding.btnSave;

        settings = getArguments().getParcelable("settings");

        setupViews();
    }

    private void setupViews() {
        binding.setSettings(settings);
        binding.executePendingBindings();

        Date beginDate = settings.getBeginDate();
        if (beginDate != null) {
            etDate.setText(DateFormat.getDateInstance().format(beginDate));
        } else {
            etDate.setText("");
        }

        //edit text handler for picking date
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(settings.getBeginDate());
                datePickerFragment.setTargetFragment(SettingsFragment.this, 1);
                datePickerFragment.show(fm, "fragment_date_picker");
            }
        });

        //setup sort spinner
        CharSequence[] sorts = new CharSequence[] {
                getResources().getText(R.string.sortNewest),
                getResources().getText(R.string.sortOldest)
        };
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, sorts);
        spSort.setAdapter(adapter);
        spSort.setSelection(settings.getSort());
        spSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                settings.setSort(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //do nothing
            }
        });

        //setup checkboxes
//        cbArts.setChecked(settings.artsFilter());
        cbArts.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                settings.setArtsFilter(b);
            }
        });
//        cbFashion.setChecked(settings.fashionFilter());
        cbFashion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                settings.setFashionFilter(b);
            }
        });
//        cbSports.setChecked(settings.sportsFilter());
        cbSports.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                settings.setSportsFilter(b);
            }
        });

        //save button handler for returning back to activity
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingsDialogListener listener = (SettingsDialogListener) getActivity();
                listener.onSave(settings);
                //close dialog fragment
                dismiss();
            }
        });
    }

    @Override
    public void onDateSelected(Date date) {
        settings.setBeginDate(date);
        etDate.setText(DateFormat.getDateInstance().format(date));
    }
}
