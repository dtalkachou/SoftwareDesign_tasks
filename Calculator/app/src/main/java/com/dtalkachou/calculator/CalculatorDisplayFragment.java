package com.dtalkachou.calculator;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class CalculatorDisplayFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.calculator_display, container, false);

        initChangeMode(view);
        return view;
    }

    private void initChangeMode(View view) {
        if (getResources().getConfiguration().orientation != Configuration.ORIENTATION_PORTRAIT) {
            return;
        }

        if (BuildConfig.FLAVOR.equals("demo")) {
            view.findViewById(R.id.change_mode).setVisibility(View.GONE);
        }
        else {
            view.findViewById(R.id.change_mode).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentTransaction fragmentTransaction = getActivity()
                            .getSupportFragmentManager().beginTransaction();
                    if (getActivity().getSupportFragmentManager().findFragmentById(R.id.mode)
                            instanceof BasicModeFragmentMode) {
                        fragmentTransaction.replace(R.id.mode, new ScientificModeFragmentMode());
                    } else {
                        fragmentTransaction.replace(R.id.mode, new BasicModeFragmentMode());
                    }
                    fragmentTransaction.commit();
                }
            });
        }
    }
}
