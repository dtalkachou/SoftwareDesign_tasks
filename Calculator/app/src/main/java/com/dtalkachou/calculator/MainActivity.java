package com.dtalkachou.calculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.res.Configuration;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dtalkachou.handlers.CalculatorModel;

public class MainActivity extends AppCompatActivity implements
        ModeBaseFragment.OnButtonPressedListener,
        DisplayFragment.OnChangeModePressedListener {
    private TextView inputNum, historyStr;
    private CalculatorModel calculator;

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
        onButtonPressed();
    }

    @Override
    public void onButtonPressed() {
        inputNum.setText(calculator.getInputNum());
        historyStr.setText(calculator.getHistory());
    }

    @Override
    public CalculatorModel getCalculator() {
        return calculator;
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
