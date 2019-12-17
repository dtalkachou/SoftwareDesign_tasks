package com.dtalkachou.calculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dtalkachou.handlers.CalculatorModel;

public class MainActivity extends AppCompatActivity implements CalculatorBaseFragment.OnCalculatorButtonPressedListener {
    private TextView inputNum, historyStr;
    private CalculatorModel calculator;

    public CalculatorModel getCalculator() {
        return calculator;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputNum = findViewById(R.id.input_str);
        historyStr = findViewById(R.id.history_str);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            initChangeMode();
        }

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

    private void initChangeMode() {
        findViewById(R.id.change_mode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                if (getSupportFragmentManager().findFragmentById(R.id.mode) instanceof BasicModeFragment) {
                    fragmentTransaction.replace(R.id.mode, new ScientificModeFragment());
                }
                else {
                    fragmentTransaction.replace(R.id.mode, new BasicModeFragment());
                }
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public void onCalculatorButtonPressed() {
        inputNum.setText(calculator.getInputNum());
        historyStr.setText(calculator.getHistory());
    }
}
