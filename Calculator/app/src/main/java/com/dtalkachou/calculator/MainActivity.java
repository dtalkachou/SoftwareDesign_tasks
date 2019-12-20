package com.dtalkachou.calculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.TextView;

import com.dtalkachou.handlers.CalculatorModel;

import com.dtalkachou.res.ButtonConsts;

public class MainActivity extends AppCompatActivity implements
        ModeBaseFragment.OnButtonPressedListener,
        DisplayFragment.OnChangeModePressedListener {
    private TextView inputNum, historyStr;

    // fixme: change to private
    public CalculatorModel calculator;

    public void setCalculator(CalculatorModel calculator) {
        this.calculator = calculator;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            calculator = new CalculatorModel();
        }

        inputNum = findViewById(R.id.input_str);
        historyStr = findViewById(R.id.history_str);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            // True only for the first run of activity.
            // Without checking, we do not save the fragment
            // from which we switched to landscape orientation.
            if (!(fragmentManager.findFragmentById(R.id.frame_fragment) instanceof Fragment)) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.frame_fragment, new BasicModeFragment());
                fragmentTransaction.commit();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable("calculator", calculator);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        calculator = savedInstanceState.getParcelable("calculator");
    }

    @Override
    public void onResume() {
        super.onResume();
        loadState();
    }

    private void loadState() {
        inputNum.setText(calculator.getInputNum());
        historyStr.setText(calculator.getHistory());
    }

    @Override
    public void onButtonPressed(String button) {
        switch (button) {
            case ButtonConsts.digit.ZERO:
                calculator.addDigit(0);
                break;
            case ButtonConsts.digit.ONE:
                calculator.addDigit(1);
                break;
            case ButtonConsts.digit.TWO:
                calculator.addDigit(2);
                break;
            case ButtonConsts.digit.THREE:
                calculator.addDigit(3);
                break;
            case ButtonConsts.digit.FOUR:
                calculator.addDigit(4);
                break;
            case ButtonConsts.digit.FIVE:
                calculator.addDigit(5);
                break;
            case ButtonConsts.digit.SIX:
                calculator.addDigit(6);
                break;
            case ButtonConsts.digit.SEVEN:
                calculator.addDigit(7);
                break;
            case ButtonConsts.digit.EIGHT:
                calculator.addDigit(8);
                break;
            case ButtonConsts.digit.NINE:
                calculator.addDigit(9);
                break;
            case ButtonConsts.operation.ADDITION:
                calculator.setOperation(CalculatorModel.ADDITION);
                break;
            case ButtonConsts.operation.SUBTRACTION:
                calculator.setOperation(CalculatorModel.SUBTRACTION);
                break;
            case ButtonConsts.operation.MULTIPLICATION:
                calculator.setOperation(CalculatorModel.MULTIPLICATION);
                break;
            case ButtonConsts.operation.DIVISION:
                calculator.setOperation(CalculatorModel.DIVISION);
                break;
            case ButtonConsts.operation.EQUALS:
                calculator.setOperation(CalculatorModel.EQUALS);
                break;
            case ButtonConsts.other.DECIMAL_SEPARATOR:
                calculator.setDecimalSeparator();
                break;
            case ButtonConsts.other.PERCENT:
                calculator.makePercent();
                break;
            case ButtonConsts.other.SIGNED:
                calculator.changeSign();
                break;
            case ButtonConsts.other.CLEAR:
                calculator.clear();
                break;
            case ButtonConsts.other.ALL_CLEAR:
                calculator.allClear();
                break;
            case ButtonConsts.function.LEFT_BRACKET:
                calculator.setFunction(CalculatorModel.LEFT_BRACKET);
                break;
            case ButtonConsts.function.RIGHT_BRACKET:
                calculator.setFunction(CalculatorModel.RIGHT_BRACKET);
                break;
        }

        loadState();
    }

    @Override
    public void onChangeModePressed() {
        Fragment fragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentById(R.id.frame_fragment) instanceof
                BasicModeFragment) {
            fragment = new ScientificModeFragment();
        } else {
            fragment = new BasicModeFragment();
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_fragment, fragment);
        fragmentTransaction.commit();
    }
}
