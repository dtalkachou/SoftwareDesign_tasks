package com.dtalkachou.battleship;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.dtalkachou.models.GameInitializer;
import com.dtalkachou.models.Room;
import com.dtalkachou.models.RoomAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;


public class RoomListFragment extends ListFragment
    implements ChildEventListener {
    private String mCurrentUserId;
    private ArrayList<Room> mRoomList;
    private ArrayList<DatabaseReference> mRoomRefList;
    private ArrayAdapter mRoomListAdapter;

    public void setCurrentUserId(String currentUserId) {
        mCurrentUserId = currentUserId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_room_list, container, false);

        mRoomList = new ArrayList<>();
        mRoomRefList = new ArrayList<>();
        mRoomListAdapter = new RoomAdapter(mRoomList, getActivity());
        setListAdapter(mRoomListAdapter);

        return view;
    }

    @Override
    public void onListItemClick(ListView parent, View v, int position, long id) {
        super.onListItemClick(parent, v, position, id);

        Room room = mRoomList.get(position);
        if (!room.ownerId.equals(mCurrentUserId)) {
            // todo: if it room with password then open password confirm dialog
            mRoomList.get(position).setOpponentId(mCurrentUserId);
            mRoomRefList.get(position).setValue(room);

            GameInitializer gameInitializer = new GameInitializer();
            gameInitializer.randomShuffle();

            Intent intent = new Intent(getActivity(), GameActivity.class);
            intent.putExtra("gameId", mRoomRefList.get(position).getKey());
            intent.putExtra("role", 2);
            intent.putExtra("myField", gameInitializer.getShips());
            startActivity(intent);
        }
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        mRoomList.add(dataSnapshot.getValue(Room.class));
        mRoomRefList.add(dataSnapshot.getRef());
        mRoomListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
        int index = mRoomRefList.indexOf(dataSnapshot.getRef());
        mRoomRefList.remove(index);
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
