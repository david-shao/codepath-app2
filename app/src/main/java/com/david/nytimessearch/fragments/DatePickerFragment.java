package com.david.nytimessearch.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.david.nytimessearch.R;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by David on 3/17/2017.
 */

public class DatePickerFragment extends DialogFragment {
    private DatePicker dpDate;

    public DatePickerFragment() {
        //Needs to be empty
    }

    public static DatePickerFragment newInstance(Date date) {
        DatePickerFragment frag = new DatePickerFragment();
        Bundle args = new Bundle();
        args.putSerializable("date", date);
        frag.setArguments(args);
        return frag;
    }

    public interface DatePickerDialogListener {
        void onDateSelected(Date date);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_date_picker, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //initialize date picker
        dpDate = (DatePicker) view.findViewById(R.id.dpDate);
        Calendar calendar = Calendar.getInstance();
        Date date = (Date) getArguments().getSerializable("date");
        if (date != null) {
            calendar.setTime(date);
        }
        dpDate.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);

                DatePickerDialogListener listener = (DatePickerDialogListener) getTargetFragment();
                listener.onDateSelected(calendar.getTime());
                dismiss();
            }
        });
    }
}
