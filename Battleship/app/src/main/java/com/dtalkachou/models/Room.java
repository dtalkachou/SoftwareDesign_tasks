package com.dtalkachou.models;

import com.google.firebase.database.IgnoreExtraProperties;


@IgnoreExtraProperties
public class Room {
    public static String ROOM_ID = "roomId";
    public static String OWNER_ID = "ownerId";
    public static String PASSWORD = "password";
    public static String OPPONENT_ID = "opponentId";

    public String roomId;
    public String ownerId;
    public String password;
    public String opponentId;

    public void setOpponentId(String opponentId) {
        this.opponentId = opponentId;
    }

    public Room() {
        // Default constructor required for calls to DataSnapshot.getValue(Room.class)
    }

    public Room (String roomId, String ownerId, String password) {
        this.roomId = roomId;
        this.ownerId = ownerId;
        this.password = password;
    }
}
