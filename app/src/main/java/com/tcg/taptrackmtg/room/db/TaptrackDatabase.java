package com.tcg.taptrackmtg.room.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.tcg.taptrackmtg.room.TrackedCardEntity;
import com.tcg.taptrackmtg.room.dao.TrackedCardDao;

@Database(entities = {TrackedCardEntity.class}, version = 1)
public abstract class TaptrackDatabase extends RoomDatabase {
    public abstract TrackedCardDao trackedCardDao();
}
