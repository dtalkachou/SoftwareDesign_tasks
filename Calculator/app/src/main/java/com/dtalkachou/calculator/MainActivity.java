package com.dtalkachou.calculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.widget.TextView;

import com.dtalkachou.handlers.CalculatorModel;

public class MainActivity extends AppCompatActivity
        implements CalculatorModeBaseFragment.OnCalculatorButtonPressedListener {
    private TextView inputNum, historyStr;
    private CalculatorModel calculator;

    public CalculatorModel getCalculator() {
        return calculator;
    }

    public void setCalculator(CalculatorModel calculator) {
        this.calculator = calculator;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputNum = findViewById(R.id.input_str);
        historyStr = findViewById(R.id.history_str);

        if (savedInstanceState == null) {
            calculator = new CalculatorModel();
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
        onCalculatorButtonPressed();
    }

    @Override
    public void onCalculatorButtonPressed() {
        inputNum.setText(calculator.getInputNum());
        historyStr.setText(calculator.getHistory());
    }
}
