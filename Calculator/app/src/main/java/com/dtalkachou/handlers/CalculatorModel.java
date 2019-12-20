package com.dtalkachou.handlers;

import android.os.Parcel;
import android.os.Parcelable;

import org.mariuszgromada.math.mxparser.Expression;

import java.lang.Double;

public class CalculatorModel implements Parcelable {
    public final static String ADDITION = "+";
    public final static String SUBTRACTION = "-";
    public final static String MULTIPLICATION = "×";
    public final static String DIVISION = "÷";
    public final static String EQUALS = "=";
    public static final String LEFT_BRACKET = "(";
    public static final String RIGHT_BRACKET = ")";

    private StringBuilder inputNum, history;
    private int notClosedBracket;
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
        dest.writeInt(notClosedBracket);
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
        notClosedBracket = 0;
        clearState = ClearState.ALL_CLEAR;
        state = State.ARGUMENT_INPUT;
    }

    private CalculatorModel(Parcel parcel) {
        inputNum = new StringBuilder(parcel.readString());
        history = new StringBuilder(parcel.readString());
        notClosedBracket = parcel.readInt();
        clearState = ClearState.values()[parcel.readInt()];
        state = State.values()[parcel.readInt()];
    }

    public void addDigit(int num) {
        if (state == State.SHOW_RESULT) {
            allClear();
        }
        else if (state == State.OPERATION_SELECTED) {
            inputNum.setLength(0);
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
        inputNum.append(num / 100);
    }

    public void setOperation(String operation) {
        if (state == State.OPERATION_SELECTED) {
            history.deleteCharAt(history.length() - 1);
            return;
        }
        else if (state == State.SHOW_RESULT && !continueResultInput()) {
            return;
        }

        state = State.OPERATION_SELECTED;
        if (history.length() != 0 && inputNum.toString().startsWith("-")) {
            history.append('(');
            inputToHistory();
            history.append(')');
        }
        else {
            inputToHistory();
        }
        history.append(operation);

        if (operation.equals(EQUALS)) {
            while (notClosedBracket > 0) {
                history.insert(history.length() - 1, RIGHT_BRACKET);
                notClosedBracket--;
            }

            state = State.SHOW_RESULT;
            calculate();
        }
    }

    public void clear() {
        if (state == State.ARGUMENT_INPUT && history.length() != 0) {
            // If true, then always last character in history is an operation
            state = State.OPERATION_SELECTED;
        }
        resetInputNum();
        clearState = ClearState.ALL_CLEAR;
    }

    public void allClear() {
        clear();
        history.setLength(0);
        notClosedBracket = 0;
    }

    public void setFunction(String function) {
        if (state == State.SHOW_RESULT) {
            allClear();
        }

        switch (function) {
            case LEFT_BRACKET:
                notClosedBracket++;
                if (state != State.ARGUMENT_INPUT) {
                    resetInputNum();
                }
                break;
            case RIGHT_BRACKET:
                if (notClosedBracket == 0) {
                    return;
                }
                inputToHistory();
                notClosedBracket--;
                break;
        }
        history.append(function);
    }

    private void resetInputNum() {
        inputNum.setLength(0);
        inputNum.append("0");
    }

    private void inputToHistory() {
        String num = doubleNumProcess(Double.valueOf(inputNum.toString()));
        inputNum.setLength(0);
        inputNum.append(num);
        history.append(num);
    }

    private boolean continueResultInput() {
        if (inputNum.toString().equals("nan")) {
            return false;
        }
        history.setLength(0);
        return true;
    }

    private void calculate() {
        String expr = history.substring(0, history.length() - 1).
                replaceAll(MULTIPLICATION, "*").replaceAll(DIVISION, "/");
        double result = new Expression(expr).calculate();
        if (Double.isNaN(result)) {
            clearState = ClearState.ALL_CLEAR;
        }
        inputNum.setLength(0);
        inputNum.append(doubleNumProcess(result));
    }
}