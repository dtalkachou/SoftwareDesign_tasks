package com.dtalkachou.calculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE &&
                BuildConfig.FLAVOR.equals("demo")) {
            findViewById(R.id.scientific_mode).setVisibility(View.GONE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    7f
            );
            findViewById(R.id.basic_mode).setLayoutParams(params);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable("calculator", calculator);
        savedInstanceState.putString("clearBtnText",
                ((Button) findViewById(R.id.clear)).getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        calculator = savedInstanceState.getParcelable("calculator");
        ((Button) findViewById(R.id.clear)).setText(savedInstanceState.getString("clearBtnText"));
        // Initialize calculator on the display.
        onCalculatorButtonPressed();
    }

    @Override
    public void onCalculatorButtonPressed() {
        inputNum.setText(calculator.getInputNum());
        historyStr.setText(calculator.getHistory());
    }
}
