package com.dtalkachou.calculator;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class DisplayFragment extends Fragment {
    private HorizontalScrollView horizontalScrollView;
    private OnChangeModePressedListener listener;

    public interface OnChangeModePressedListener {
        void onChangeModePressed();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.calculator_display, container, false);

        initHistoryStr(view);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            initChangeMode(view);
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (OnChangeModePressedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    " must implement OnChangeModePressedListener");
        }
    }

    private void initHistoryStr(View view) {
        horizontalScrollView = view.findViewById(R.id.history_scroll_view);
        ((TextView) view.findViewById(R.id.history_str)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                horizontalScrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
            }
        });
    }

    private void initChangeMode(View view) {
        if (BuildConfig.FLAVOR.equals("demo")) {
            view.findViewById(R.id.change_mode).setVisibility(View.GONE);
        }
        else {
            view.findViewById(R.id.change_mode).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onChangeModePressed();
                }
            });
        }
    }
}
