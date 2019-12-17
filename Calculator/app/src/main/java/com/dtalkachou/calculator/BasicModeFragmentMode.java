package com.dtalkachou.calculator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class BasicModeFragmentMode extends CalculatorModeBaseFragment {
    private Button cancelBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.basic_mode, container, false);
        cancelBtn = view.findViewById(R.id.clear);

        initViews(view);
        return view;
    }

    private void initViews(View view) {
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
                R.id.clear,
                R.id.signed
        };

        for (int id: viewIds) {
            view.findViewById(id).setOnClickListener(this);
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
                if (listener instanceof MainActivity) {
                    boolean pressed = ((MainActivity) listener).getCalculator()
                            .onNumPressed(((Button) view).getText().toString());
                    if (pressed) {
                        cancelBtn.setText(R.string.clear);
                    }
                }
                break;
            case R.id.addition:
            case R.id.subtraction:
            case R.id.multiplication:
            case R.id.division:
            case R.id.equals:
                if (listener instanceof MainActivity) {
                    ((MainActivity) listener).getCalculator().onOperationPressed(view.getId());
                }
                break;
            case R.id.decimal_separator:
                if (listener instanceof MainActivity) {
                    ((MainActivity) listener).getCalculator().onDecimalSeparatorPressed();
                }
                break;
            case R.id.clear:
                if (listener instanceof MainActivity) {
                    if (cancelBtn.getText().equals(getResources().getText(R.string.clear))) {
                        ((MainActivity) listener).getCalculator().clear();
                        cancelBtn.setText(R.string.all_clear);
                    } else {
                        ((MainActivity) listener).getCalculator().allClear();
                    }
                }
                break;
            case R.id.signed:
                if (listener instanceof MainActivity) {
                    ((MainActivity) listener).getCalculator().onSignPressed();
                }
                break;
            case R.id.percent:
                if (listener instanceof MainActivity) {
                    ((MainActivity) listener).getCalculator().onPercentPressed();
                }
                break;
        }

        super.onClick(view);
    }
}
