package com.dtalkachou.calculator;

import android.content.Context;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.dtalkachou.handlers.CalculatorModel;

public class ModeBaseFragment extends Fragment implements View.OnClickListener {
    protected OnButtonPressedListener listener;

    public interface OnButtonPressedListener {
        void onButtonPressed(String button);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (OnButtonPressedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    " must implement OnButtonPressedListener");
        }
    }

    @Override
    public void onClick(View view) {
    }
}
