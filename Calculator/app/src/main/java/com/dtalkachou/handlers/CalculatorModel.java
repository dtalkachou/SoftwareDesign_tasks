package com.dtalkachou.handlers;

import android.os.Parcel;
import android.os.Parcelable;

import com.dtalkachou.calculator.R;
import org.mariuszgromada.math.mxparser.Expression;

import java.lang.Double;

public class CalculatorModel implements Parcelable {
    private int maxInputLength = 10;
    private StringBuilder inputNum, history;
    private State state;

    public void setMaxInputLength(int maxInputLength) {
        this.maxInputLength = maxInputLength;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(inputNum.toString());
        dest.writeString(history.toString());
        dest.writeInt(state.ordinal());
    }

    public static final Parcelable.Creator<CalculatorModel> CREATOR = new
            Parcelable.Creator<CalculatorModel>() {
        public CalculatorModel createFromParcel(Parcel in) {
            return new CalculatorModel(in);
        }

        public CalculatorModel[] newArray(int size) {
            return new CalculatorModel[size];
        }
    };

    public enum State {
        argInput,
        operationSelected,
        showResult
    }

    public String getInputNum() {
        if (inputNum.toString().equals("nan")) {
            return "Error";
        }
        return inputNum.toString();
    }

    public String getHistory() {
        return history.toString();
    }

    private static String doubleNumProcess(Double num) {
        return String.valueOf(num).replaceAll(".0$", "").toLowerCase();
    }

    public CalculatorModel() {
        inputNum = new StringBuilder("0");
        history = new StringBuilder();
        state = State.argInput;
    }

    private CalculatorModel(Parcel parcel) {
        inputNum = new StringBuilder(parcel.readString());
        history = new StringBuilder(parcel.readString());
        state = State.values()[parcel.readInt()];
    }

    public boolean onNumPressed(String num) {
        if (state == State.showResult) {
            clear();
        }

        if (inputNum.length() == maxInputLength) {
            return false;
        }

        state = State.argInput;
        if (inputNum.toString().matches("^(|-)0$")) {
            if (num.equals("0")) {
                return false;
            }
            else {
                inputNum.deleteCharAt(inputNum.length() - 1);
            }
        }
        inputNum.append(num);
        return true;
    }

    public void onDecimalSeparatorPressed() {
        if (state == State.showResult) {
            clearHistory();
        }

        if (inputNum.length() == maxInputLength) {
            return;
        }

        state = State.argInput;
        if (!inputNum.toString().contains(".")) {
            inputNum.append(".");
        }
        else if (inputNum.toString().endsWith(".")) {
            inputNum.deleteCharAt(inputNum.length() - 1);
        }
    }

    public void onSignPressed() {
        if (state == State.showResult) {
            clearHistory();
        }

        state = State.argInput;
        if (inputNum.toString().startsWith("-")) {
            inputNum.deleteCharAt(0);
        }
        else {
            inputNum.insert(0, "-");
        }
    }

    public void onPercentPressed() {
        if (state == State.showResult) {
            clearHistory();
        }

        if (inputNum.length() == maxInputLength) {
            return;
        }

        state = State.argInput;
        double num = Double.parseDouble(inputNum.toString());
        inputNum.setLength(0);
        inputNum.append(doubleNumProcess(num / 100));
    }

    public void onOperationPressed(int operationId) {
        if (state == State.operationSelected) {
            history.deleteCharAt(history.length() - 1);
        }
        else {
            if (state == State.showResult) {
                clearHistory();
            }

            double num = Double.parseDouble(inputNum.toString());
            if (history.length() != 0 && inputNum.toString().startsWith("-")) {
                history.append('(').append(doubleNumProcess(num)).append(')');
            }
            else {
                history.append(doubleNumProcess(num));
            }
        }

        switch (operationId) {
            case R.id.addition:
                history.append("+");
                break;
            case R.id.subtraction:
                history.append("–");
                break;
            case R.id.multiplication:
                history.append("×");
                break;
            case R.id.division:
                history.append("÷");
                break;
            default:
                history.append("=");
        }

        if (operationId == R.id.equals) {
            inputNum.setLength(0);
            inputNum.append(doubleNumProcess(calculate()));
            state = State.showResult;
        }
        else {
            resetInputNum();
            state = State.operationSelected;
        }
    }

    private void resetInputNum() {
        inputNum.setLength(0);
        inputNum.append("0");
    }

    private void clearHistory() {
        history.setLength(0);
    }

    public void clear() {
        if (state == State.showResult) {
            clearHistory();
            state = State.argInput;
        }
        else if (state == State.argInput && history.length() != 0) {
            state = State.operationSelected;
        }
        resetInputNum();
    }

    public void allClear() {
        clear();
        clearHistory();
    }

    private double calculate() {
        String expr = history.substring(0, history.length() - 1).replace('–', '-')
                .replace('×', '*').replace('÷', '/');
        return new Expression(expr).calculate();
    }
}