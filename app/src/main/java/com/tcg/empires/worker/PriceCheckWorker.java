package com.tcg.empires.worker;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.tcg.empires.repository.TrackedCardRepository;
import com.tcg.empires.room.TrackedCardEntity;

import java.util.List;

public class PriceCheckWorker extends Worker {

    public PriceCheckWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        TrackedCardRepository trackedCardRepository = new TrackedCardRepository((Application) getApplicationContext());
        int period = getInputData().getInt("period", -1);

        SharedPreferences prefs = getApplicationContext()
                .getSharedPreferences("prefs", Context.MODE_PRIVATE);

        long lastWeeklyRun = prefs.getLong("last_weekly_check", 0);
        long lastMonthlyRun = prefs.getLong("last_monthly_check", 0);
        long oneWeekInMillis = 7L * 24 * 60 * 60 * 1000;
        long oneMonthInMillis = 30L * 24 * 60 * 60 * 1000;
        long now = System.currentTimeMillis();
        List<TrackedCardEntity> trackedCardsByPeriod = trackedCardRepository.getTrackedCardsByPeriod(period);

        if(period == 3){

        }else if(period == 4 && (now - lastWeeklyRun >= oneWeekInMillis)){

            prefs.edit().putLong("last_weekly_check", now).apply();
        }else if(period == 5 && (now - lastMonthlyRun >= oneMonthInMillis)){

            prefs.edit().putLong("last_monthly_check", now).apply();
        }

        return Result.success(); // o retry() o failure()
    }
}
