package com.dtalkachou.calculator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dtalkachou.res.ButtonConsts;

public class ScientificModeFragment extends ModeBaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.scientific_mode, container, false);
        initFunctions(view);
        return view;
    }

    private void initFunctions(View view) {
        final int[] viewIds = new int[]{
                R.id.left_bracket,
                R.id.right_bracket,
                R.id.factorial,
                R.id.sqrt,
                R.id.power,
                R.id.natural_logarithm,
                R.id.pi,
                R.id.e,
                R.id.rand,
                R.id.reverse,
                R.id.sin,
                R.id.cos,
                R.id.exponentiation,
                R.id.tan,
                R.id.cat
        };

        for (int id: viewIds) {
            view.findViewById(id).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_bracket:
                listener.onButtonPressed(ButtonConsts.function.LEFT_BRACKET);
                break;
            case R.id.right_bracket:
                listener.onButtonPressed(ButtonConsts.function.RIGHT_BRACKET);
                break;
//            case R.id.left_bracket:
//                listener.onButtonPressed(ButtonConsts.function.LEFT_BRACKET);
//                break;
        }
        super.onClick(view);
    }
}
