package com.dtalkachou.battleship;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;


public class CreateRoomFragment extends Fragment {
    private DatabaseReference mRoomsRef;

    private OnCreatedRoomListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_create_room, container, false);

        initCreateRoomButton(view);

        return view;
    }

    private void initCreateRoomButton(View view) {
        view.findViewById(R.id.create_room_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onCreatedRoom("test room 123");
                }
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnCreatedRoomListener) {
            mListener = (OnCreatedRoomListener) context;
        }
        else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCreatedRoomListener");
        }
    }

    public interface OnCreatedRoomListener {
        void onCreatedRoom(String value);
    }
}
