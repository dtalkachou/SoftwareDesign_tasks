package com.dtalkachou.battleship;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.dtalkachou.models.Room;
import com.dtalkachou.models.RoomAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;


public class RoomListFragment extends ListFragment
    implements ChildEventListener {
    private ArrayList<Room> mRoomList;
    private ArrayList<String> mRoomIdList;
    private ArrayAdapter mRoomListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_room_list, container, false);

        mRoomList = new ArrayList<>();
        mRoomIdList = new ArrayList<>();
        mRoomListAdapter = new RoomAdapter(mRoomList, getActivity());
        setListAdapter(mRoomListAdapter);

        return view;
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        mRoomList.add(dataSnapshot.getValue(Room.class));
        mRoomIdList.add(dataSnapshot.getKey());
        mRoomListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
        int index = mRoomIdList.indexOf(dataSnapshot.getKey());
        mRoomIdList.remove(index);
        mRoomList.remove(index);
        mRoomListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}
