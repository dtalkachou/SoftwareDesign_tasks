package com.dtalkachou.calculator;

import java.lang.Double;

public class Calculator {

    private double bufferValue = 0;
    private StringBuilder inputNum;
    private boolean argsEntered = false;
    private int operationSelectedId = -1;

    private State state;

    private enum State {
        argInput,
        operationSelected,
    }

    public Calculator() {
        state = State.argInput;
        inputNum = new StringBuilder();
    }

    public void onNumPressed(String num) {
        if (state != State.argInput) {
            state = State.argInput;
            inputNum.setLength(0);
        }

        if (inputNum.length() == 0 && num.equals("0")) {
            return;
        }

        inputNum.append(num);
    }

    public void onSignPressed() {
        if (inputNum.length() != 0) {
            if (inputNum.substring(0, 1).equals("-")) {
                inputNum.delete(0, 1);
            }
            else {
                inputNum.insert(0, "-");
            }
        }
    }

    public void onPercentPressed() {
        if (inputNum.length() != 0) {
            double num = Double.parseDouble(inputNum.toString());
            inputNum.setLength(0);
            inputNum.append(num / 100);
        }
    }

    public void onOperationPressed(int operationId) {
        if (state != State.operationSelected) {
            state = State.operationSelected;
            if (!argsEntered && inputNum.length() != 0)
            {
                argsEntered = true;
                bufferValue = Double.parseDouble(inputNum.toString());
            }
            else {
                switch (operationSelectedId) {
                    case R.id.addition:
                        bufferValue += Double.parseDouble(inputNum.toString());
                        break;
                    case R.id.subtraction:
                        bufferValue -= Double.parseDouble(inputNum.toString());
                        break;
                    case R.id.multiplication:
                        bufferValue *= Double.parseDouble(inputNum.toString());
                        break;
                    case R.id.division:
                        bufferValue /= Double.parseDouble(inputNum.toString());
                        break;
                    default:
                        return;
                }
            }
        }

        operationSelectedId = operationId;
    }

    public String getText() {
        StringBuilder str = new StringBuilder();
        switch (state) {
            case argInput:
                if (inputNum.length() != 0) {
                    str.append(inputNum);
                } else {
                    return "0";
                }
                break;
            case operationSelected:
                str.append(bufferValue);
                break;
        }
        return str.toString().replaceAll(".0$", "").toLowerCase();
    }

    public void clear() {
        state = State.argInput;
        inputNum.setLength(0);
    }

    public void all_clear() {
        bufferValue = 0;
        argsEntered = false;
    }
}