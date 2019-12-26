package com.dtalkachou.battleship;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.dtalkachou.models.Room;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;


public class RoomListFragment extends ListFragment {
    private static String ROOMS_REF_PATH = "roomsRefPath";

    private ArrayAdapter mRoomListAdapter;
    private DatabaseReference mRoomsRef;



    public static RoomListFragment newInstance(String roomsRefPath) {
        RoomListFragment roomListFragment = new RoomListFragment();
        Bundle args = new Bundle();
        args.putString(ROOMS_REF_PATH, roomsRefPath);
        roomListFragment.setArguments(args);
        return roomListFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mRoomsRef = FirebaseDatabase.getInstance().getReference(ROOMS_REF_PATH);

            mRoomsRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    dataSnapshot.getKey();
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Room room = dataSnapshot.getValue(Room.class);
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_room_list, container, false);

//        String data[] = new String[] { "one", "two", "three", "four" };
//
//        ArrayAdapter adapter = new ArrayAdapter<>(getActivity(),
//                R.layout.fragment_room_list, data);
//        setListAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRoomListAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1);
        mRoomListAdapter.add(new Room("123", "1243", "453"));
        setListAdapter(mRoomListAdapter);
    }
}
