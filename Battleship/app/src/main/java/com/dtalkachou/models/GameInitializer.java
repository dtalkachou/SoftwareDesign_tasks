package com.dtalkachou.models;

import android.graphics.Rect;

import java.util.ArrayList;
import java.util.Random;


public class GameInitializer {
    private ArrayList<Rect> ships;
    private OnUpdateListener mListener;

    public ArrayList<Rect> getShips() {
        return ships;
    }

    private int[] leftCount;

    public int getLeftCount(int i) {
        return leftCount[i - 1];
    }

    public GameInitializer() {
        mListener = null;
        leftCount = new int[4];
        init();
    }

    private void init() {
        ships = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            leftCount[i] = 4 - i;
        }
    }

    private boolean touch(Rect s1, Rect s2) {
        return s2.intersects(s1.left - 2, s1.top - 2, s1.right + 2, s1.bottom + 2);
    }

    public void randomShuffle() {
        Random r = new Random();
        for (int i = 0; i < 4; i++) {
            while (leftCount[i] > 0) {
                while (true) {
                    int x = r.nextInt(10);
                    int y = r.nextInt(10);
                    int dir = r.nextInt(2);

                    Rect ship = new Rect(
                            x, y,
                            x + (dir == 0 ? i : 0),
                            y + (dir == 1 ? i : 0)
                    );

                    if (ship.right >= 10 || ship.bottom >= 10) {
                        continue;
                    }

                    boolean ok = true;
                    for (int j = 0; j < ships.size(); j++) {
                        if (touch(ships.get(j), ship)) {
                            ok = false;
                            break;
                        }
                    }

                    if (ok) {
                        ships.add(ship);
                        break;
                    }
                }
                leftCount[i] -= 1;
            }
        }

        if (mListener != null) {
            mListener.onUpdate();
        }
    }

    public void setUpdateListener(OnUpdateListener listener) {
        mListener = listener;
    }

    public interface OnUpdateListener {
        void onUpdate();
    }
}