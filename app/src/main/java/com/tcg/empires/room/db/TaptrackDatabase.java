package com.tcg.empires.room.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.tcg.empires.room.TrackedCardEntity;
import com.tcg.empires.room.dao.TrackedCardDao;

@Database(entities = {TrackedCardEntity.class}, version = 1)
public abstract class TaptrackDatabase extends RoomDatabase {
    public abstract TrackedCardDao trackedCardDao();
}
