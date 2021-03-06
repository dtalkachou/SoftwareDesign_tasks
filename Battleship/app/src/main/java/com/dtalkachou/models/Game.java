package com.dtalkachou.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;

@IgnoreExtraProperties
public class Game {
    public enum CellState {
        EMPTY,
        HIT,
        MISS,
        SHIP
    }

    public enum GameState {
        FIRST_MOVE,
        SECOND_MOVE
    }

    public GameState gameState;

    public List<List<CellState>> mField1, mField2;
    public int playersConnected;
    public boolean finished;



    public Game() {
        gameState = GameState.FIRST_MOVE;
        finished = false;

        mField1 = new ArrayList<>();
        mField2 = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ArrayList<CellState> a = new ArrayList<>();
            ArrayList<CellState> b = new ArrayList<>();

            for (int j = 0; j < 10; j++) {
                a.add(CellState.EMPTY);
                b.add(CellState.EMPTY);
            }

            mField1.add(a);
            mField2.add(b);
        }
    }


    public boolean makeMove(GameState role, int x, int y) {
        if (role == GameState.FIRST_MOVE) {
            if (mField2.get(x).get(y) == CellState.HIT || mField2.get(x).get(y) == CellState.MISS) {
                return false;
            }
            if (mField2.get(x).get(y) == CellState.EMPTY) {
                gameState = GameState.SECOND_MOVE;
                mField2.get(x).set(y, CellState.MISS);
            } else {
                mField2.get(x).set(y, CellState.HIT);
            }

            return true;
        } else {
            if (mField1.get(x).get(y) == CellState.HIT || mField1.get(x).get(y) == CellState.MISS) {
                return false;
            }
            if (mField1.get(x).get(y) == CellState.EMPTY) {
                gameState = GameState.FIRST_MOVE;
                mField1.get(x).set(y, CellState.MISS);
            } else {
                mField1.get(x).set(y, CellState.HIT);
            }
            return true;
        }
    }
}