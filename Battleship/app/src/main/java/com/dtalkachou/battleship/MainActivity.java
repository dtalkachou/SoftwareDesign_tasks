package com.dtalkachou.battleship;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        addBoard(R.id.my_board);
        addBoard(R.id.opposing_board);
    }

    private void addBoard(@IdRes int containerViewId) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().
                beginTransaction();
        fragmentTransaction.add(containerViewId, new BoardFragment());
        fragmentTransaction.commit();
    }
}
