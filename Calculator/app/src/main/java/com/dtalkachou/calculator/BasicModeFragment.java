package com.dtalkachou.calculator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dtalkachou.handlers.ClearState;

public class BasicModeFragment extends ModeBaseFragment {
    private Button clearButton, allClearButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.basic_mode, container, false);

        clearButton = view.findViewById(R.id.clear);
        allClearButton = view.findViewById(R.id.all_clear);

        setButtonsOnClickListener(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setClearButton();
    }

    private void setButtonsOnClickListener(View view) {
        final int[] viewIds = new int[] {
                R.id.N0,
                R.id.N1,
                R.id.N2,
                R.id.N3,
                R.id.N4,
                R.id.N5,
                R.id.N6,
                R.id.N7,
                R.id.N8,
                R.id.N9,
                R.id.percent,
                R.id.addition,
                R.id.subtraction,
                R.id.multiplication,
                R.id.division,
                R.id.equals,
                R.id.decimal_separator,
                R.id.all_clear,
                R.id.clear,
                R.id.signed
        };

        for (int id: viewIds) {
            view.findViewById(id).setOnClickListener(this);
        }
    }

    private void setClearButton() {
        ClearState clearState = listener.getCalculator().getClearState();
        if (clearState == ClearState.CLEAR && allClearButton.getVisibility() == View.VISIBLE) {
            clearButton.setVisibility(View.VISIBLE);
            allClearButton.setVisibility(View.INVISIBLE);
        }
        else if (clearState == ClearState.ALL_CLEAR &&
                clearButton.getVisibility() == View.VISIBLE)  {
            allClearButton.setVisibility(View.VISIBLE);
            clearButton.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.N0:
            case R.id.N1:
            case R.id.N2:
            case R.id.N3:
            case R.id.N4:
            case R.id.N5:
            case R.id.N6:
            case R.id.N7:
            case R.id.N8:
            case R.id.N9:
                listener.getCalculator().onNumPressed(((Button) view).getText().toString());
                break;
            case R.id.addition:
            case R.id.subtraction:
            case R.id.multiplication:
            case R.id.division:
            case R.id.equals:
                listener.getCalculator().onOperationPressed(view.getId());
                break;
            case R.id.decimal_separator:
                listener.getCalculator().onDecimalSeparatorPressed();
                break;
            case R.id.clear:
                listener.getCalculator().onClearPressed();
                break;
            case R.id.all_clear:
                listener.getCalculator().onAllClearPressed();
                break;
            case R.id.signed:
                listener.getCalculator().onSignPressed();
                break;
            case R.id.percent:
                listener.getCalculator().onPercentPressed();
                break;
        }
        setClearButton();

        super.onClick(view);
    }
}
