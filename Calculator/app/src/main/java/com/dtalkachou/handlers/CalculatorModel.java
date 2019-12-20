package com.dtalkachou.handlers;

import android.os.Parcel;
import android.os.Parcelable;

import org.mariuszgromada.math.mxparser.Expression;

import java.lang.Double;
import java.util.Random;

public class CalculatorModel implements Parcelable {
    private StringBuilder inputNum, history;
    private State state;
    private ClearState clearState;

    public ClearState getClearState() {
        return clearState;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(inputNum.toString());
        dest.writeString(history.toString());
        dest.writeInt(clearState.ordinal());
        dest.writeInt(state.ordinal());
    }

    public static final Parcelable.Creator<CalculatorModel> CREATOR =
            new Parcelable.Creator<CalculatorModel>() {
                public CalculatorModel createFromParcel(Parcel in) {
                    return new CalculatorModel(in);
                }

                public CalculatorModel[] newArray(int size) {
                    return new CalculatorModel[size];
                }
            };

    private static String doubleNumProcess(Double num) {
        return String.valueOf(num).replaceAll(".0$", "").toLowerCase();
    }

    public CalculatorModel() {
        inputNum = new StringBuilder("0");
        history = new StringBuilder();
        clearState = ClearState.ALL_CLEAR;
        state = State.ARGUMENT_INPUT;
    }

    private CalculatorModel(Parcel parcel) {
        inputNum = new StringBuilder(parcel.readString());
        history = new StringBuilder(parcel.readString());
        clearState = ClearState.values()[parcel.readInt()];
        state = State.values()[parcel.readInt()];
    }

    public void addDigit(int num) {
        if (state == State.SHOW_RESULT) {
            clear();
        }

        state = State.ARGUMENT_INPUT;
        if (inputNum.toString().matches("^(|-)0$")) {
            if (num == 0) {
                return;
            }
            inputNum.deleteCharAt(inputNum.length() - 1);
        }
        inputNum.append(num);
        clearState = ClearState.CLEAR;
    }

    public void setDecimalSeparator() {
        if (state == State.SHOW_RESULT && !continueResultInput()) {
            return;
        }

        state = State.ARGUMENT_INPUT;
        if (!inputNum.toString().contains(".")) {
            inputNum.append(".");
        }
        else if (inputNum.toString().endsWith(".")) {
            inputNum.deleteCharAt(inputNum.length() - 1);
        }
    }

    public void changeSign() {
        if (state == State.SHOW_RESULT && !continueResultInput()) {
            return;
        }

        state = State.ARGUMENT_INPUT;
        if (inputNum.toString().startsWith("-")) {
            inputNum.deleteCharAt(0);
        }
        else {
            inputNum.insert(0, "-");
        }
    }

    public void makePercent() {
        if (state == State.SHOW_RESULT && !continueResultInput()) {
            return;
        }

        state = State.ARGUMENT_INPUT;
        double num = Double.parseDouble(inputNum.toString());
        inputNum.setLength(0);
        inputNum.append(doubleNumProcess(num / 100));
    }

    public void setOperation(char operationSymbol) {
        if (state == State.OPERATION_SELECTED) {
            history.deleteCharAt(history.length() - 1);
        }
        else {
            if (state == State.SHOW_RESULT && !continueResultInput()) {
                return;
            }

            double num = Double.parseDouble(inputNum.toString());
            if (history.length() != 0 && inputNum.toString().startsWith("-")) {
                history.append('(').append(doubleNumProcess(num)).append(')');
            }
            else {
                history.append(doubleNumProcess(num));
            }
        }
        history.append(operationSymbol);

        if (operationSymbol == '=') {
            inputNum.setLength(0);
            inputNum.append(doubleNumProcess(calculate()));
            state = State.SHOW_RESULT;
        }
        else {
            resetInputNum();
            state = State.OPERATION_SELECTED;
        }
    }

    public void clear() {
        if (state == State.SHOW_RESULT) {
            clearHistory();
            state = State.ARGUMENT_INPUT;
        }
        else if (state == State.ARGUMENT_INPUT && history.length() != 0) {
            // If true, then always last character in history is an operation
            state = State.OPERATION_SELECTED;
        }
        resetInputNum();
        clearState = ClearState.ALL_CLEAR;
    }

    public void allClear() {
        clear();
        clearHistory();
    }

    public void setFunction(int functionId) {

    }

    private void resetInputNum() {
        inputNum.setLength(0);
        inputNum.append("0");
    }

    private void clearHistory() {
        history.setLength(0);
    }

    private boolean continueResultInput() {
        if (inputNum.toString().equals("nan")) {
            return false;
        }
        clearHistory();
        return true;
    }

    private double calculate() {
        String expr = history.substring(0, history.length() - 1).replace('–', '-').
                replace('×', '*').replace('÷', '/');
        double result = new Expression(expr).calculate();
        if (Double.isNaN(result)) {
            clearState = ClearState.ALL_CLEAR;
        }
        return result;
    }
}