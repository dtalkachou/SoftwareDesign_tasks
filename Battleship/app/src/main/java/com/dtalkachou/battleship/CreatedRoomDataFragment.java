package com.dtalkachou.battleship;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.dtalkachou.models.Room;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;


public class CreatedRoomDataFragment extends Fragment
        implements ValueEventListener {
    private static String ARG_ROOM_ID = "mRoomId";
    private static String ARG_PASSWORD = "mPassword";

    private String mRoomId;
    private String mPassword;

    private OnRemoveRoomListener mListener;

    public static CreatedRoomDataFragment newInstance(String roomId, String password) {
        CreatedRoomDataFragment fragment = new CreatedRoomDataFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ROOM_ID, roomId);
        args.putString(ARG_PASSWORD, password);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mRoomId = getArguments().getString(ARG_ROOM_ID);
            mPassword = getArguments().getString(ARG_PASSWORD);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_created_room_data, container, false);

        Button removeRoomButton = view.findViewById(R.id.remove_room_button);
        final EditText roomIdEditText = view.findViewById(R.id.room_id_edit_text);
        TextInputLayout roomIdLayout = view.findViewById(R.id.room_id_layout);

        removeRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onRemoveRoom(mRoomId);
                }
            }
        });

        roomIdEditText.setText(mRoomId);

        roomIdLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getActivity().
                        getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(null, roomIdEditText.getText());
                clipboard.setPrimaryClip(clip);

                Toast.makeText(getContext(), "Room ID copied", Toast.LENGTH_SHORT).show();
            }
        });

        if (mPassword != null) {
            EditText passwordEditText = view.findViewById(R.id.password_edit_text);
            LinearLayout passwordLayout = view.findViewById(R.id.password_layout);

            passwordLayout.setVisibility(View.VISIBLE);

            passwordEditText.setText(mPassword);
        }

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnRemoveRoomListener) {
            mListener = (OnRemoveRoomListener) context;
        }
        else {
            throw new RuntimeException(context.toString()
                    + " must implement OnRemoveRoomListener");
        }
    }

    @Override
    public void onDestroyView() {
        // fixme: remove fragment with app closing
        if (mListener != null) {
            mListener.onRemoveRoom(mRoomId);
        }
        super.onDestroyView();
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        String opponentId = dataSnapshot.getValue(String.class);

        if (opponentId != null) {
            Intent intent = new Intent(getActivity(), GameActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }

    public interface OnRemoveRoomListener {
        void onRemoveRoom(String roomId);
    }
}
