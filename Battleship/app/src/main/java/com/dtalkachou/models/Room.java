package com.dtalkachou.models;

import com.google.firebase.database.IgnoreExtraProperties;


@IgnoreExtraProperties
public class Room {
    public String roomId;
    public String ownerId;
    public String password;

    public Room() {
        // Default constructor required for calls to DataSnapshot.getValue(Room.class)
    }

    public Room (String roomId, String ownerId, String password) {
        this.roomId = roomId;
        this.ownerId = ownerId;
        this.password = password;
    }
}
