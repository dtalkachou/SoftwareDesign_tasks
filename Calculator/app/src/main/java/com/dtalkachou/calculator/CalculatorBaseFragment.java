package com.dtalkachou.calculator;

import android.content.Context;
import android.view.View;

import androidx.fragment.app.Fragment;

public class CalculatorBaseFragment extends Fragment implements View.OnClickListener {
    protected OnCalculatorButtonPressedListener listener;

    public interface OnCalculatorButtonPressedListener {
        void onCalculatorButtonPressed();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (OnCalculatorButtonPressedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnCalculatorButtonPressedListener");
        }
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onCalculatorButtonPressed();
        }
    }
}
