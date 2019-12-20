package com.dtalkachou.calculator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dtalkachou.handlers.ClearState;
import com.dtalkachou.res.ButtonConsts;

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
                R.id.digit_zero,
                R.id.digit_one,
                R.id.digit_two,
                R.id.digit_three,
                R.id.digit_four,
                R.id.digit_five,
                R.id.digit_six,
                R.id.digit_seven,
                R.id.digit_eight,
                R.id.digit_nine,
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
        // fixme: change approach to this feature
        ClearState clearState = ((MainActivity) listener).calculator.getClearState();
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
            case R.id.digit_zero:
                listener.onButtonPressed(ButtonConsts.digit.ZERO);
                break;
            case R.id.digit_one:
                listener.onButtonPressed(ButtonConsts.digit.ONE);
                break;
            case R.id.digit_two:
                listener.onButtonPressed(ButtonConsts.digit.TWO);
                break;
            case R.id.digit_three:
                listener.onButtonPressed(ButtonConsts.digit.THREE);
                break;
            case R.id.digit_four:
                listener.onButtonPressed(ButtonConsts.digit.FOUR);
                break;
            case R.id.digit_five:
                listener.onButtonPressed(ButtonConsts.digit.FIVE);
                break;
            case R.id.digit_six:
                listener.onButtonPressed(ButtonConsts.digit.SIX);
                break;
            case R.id.digit_seven:
                listener.onButtonPressed(ButtonConsts.digit.SEVEN);
                break;
            case R.id.digit_eight:
                listener.onButtonPressed(ButtonConsts.digit.EIGHT);
                break;
            case R.id.digit_nine:
                listener.onButtonPressed(ButtonConsts.digit.NINE);
                break;
            case R.id.addition:
                listener.onButtonPressed(ButtonConsts.operation.ADDITION);
                break;
            case R.id.subtraction:
                listener.onButtonPressed(ButtonConsts.operation.SUBTRACTION);
                break;
            case R.id.multiplication:
                listener.onButtonPressed(ButtonConsts.operation.MULTIPLICATION);
                break;
            case R.id.division:
                listener.onButtonPressed(ButtonConsts.operation.DIVISION);
                break;
            case R.id.equals:
                listener.onButtonPressed(ButtonConsts.operation.EQUALS);
                break;
            case R.id.decimal_separator:
                listener.onButtonPressed(ButtonConsts.other.DECIMAL_SEPARATOR);
                break;
            case R.id.percent:
                listener.onButtonPressed(ButtonConsts.other.PERCENT);
                break;
            case R.id.signed:
                listener.onButtonPressed(ButtonConsts.other.SIGNED);
                break;
            case R.id.clear:
                listener.onButtonPressed(ButtonConsts.other.CLEAR);
                break;
            case R.id.all_clear:
                listener.onButtonPressed(ButtonConsts.other.ALL_CLEAR);
                break;
        }
        setClearButton();

        super.onClick(view);
    }
}
