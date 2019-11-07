package com.dtalkachou.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

//    private Button num_0, num_1, num_2, num_3, num_4, num_5, num_6, num_7, num_8, num_9;
    private final int[] numericIds = new int[] {
            R.id.N0,
            R.id.N1,
            R.id.N2,
            R.id.N3,
            R.id.N4,
            R.id.N5,
            R.id.N6,
            R.id.N7,
            R.id.N8,
            R.id.N9
    };
    private final int[] operationIds = new int[] {
            R.id.addition,
            R.id.subtraction,
            R.id.multiplication,
            R.id.division,
            R.id.equals
    };

    private Calculator calculator;

    private TextView inputNum;
    private Button cancelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputNum = findViewById(R.id.input_str);
        cancelBtn = findViewById(R.id.clear);

        calculator = new Calculator();
        initTextView();
        initNumeric();
        initFunction();
        initOperation();
    }

    private void initTextView() {
        inputNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (after > 7) {
                    inputNum.setTextSize(60);
                }
                else {
                    inputNum.setTextSize(72);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    private void initNumeric() {
        View.OnClickListener numericButtonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelBtn.setText(R.string.clear);
                calculator.onNumPressed(((Button) view).getText().toString());
                inputNum.setText(calculator.getText());
            }
        };

        for (int id: numericIds) {
            findViewById(id).setOnClickListener(numericButtonClickListener);
        }
    }

    private void initFunction() {
        findViewById(R.id.clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cancelBtn.getText().equals(getResources().getText(R.string.clear)))
                {
                    calculator.clear();
                    cancelBtn.setText(R.string.all_clear);
                    inputNum.setText("0");
                }
                else {
                    calculator.all_clear();
                }
            }
        });

        findViewById(R.id.signed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculator.onSignPressed();
                inputNum.setText(calculator.getText());
            }
        });

        findViewById(R.id.percent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculator.onPercentPressed();
                inputNum.setText(calculator.getText());
            }
        });
    }

    public void initOperation() {
        for (int id : operationIds) {
            findViewById(id).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    calculator.onOperationPressed(view.getId());
                    inputNum.setText(calculator.getText());
                }
            });
        }
    }
}
