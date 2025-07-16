package com.tcg.taptrackmtg.room.db;

import android.content.Context;

import androidx.room.Room;

public class DatabaseClient {
    private static DatabaseClient instance;
    private final TaptrackDatabase database;

    private DatabaseClient(Context context) {
        database = Room.databaseBuilder(context.getApplicationContext(),
                        TaptrackDatabase.class, "taptrack_database.db")
                .build();
    }

    public static synchronized DatabaseClient getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseClient(context);
        }
        return instance;
    }

    public TaptrackDatabase getTaptrackDatabase() {
        return database;
    }
}
