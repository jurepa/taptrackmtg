package com.tcg.empires.worker;

import android.Manifest;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.tcg.empires.R;
import com.tcg.empires.client.ScryfallClient;
import com.tcg.empires.model.ScryfallCardDetailList;
import com.tcg.empires.model.ScryfallDetailCard;
import com.tcg.empires.repository.TrackedCardRepository;
import com.tcg.empires.room.TrackedCardEntity;
import com.tcg.empires.service.ScryfallService;

import java.util.List;

public class PriceCheckWorker extends Worker {

    public PriceCheckWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        createNotificationChannel();
        String title = "¡Ojo a esta carta!";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "PRICE_ALERTS")
                .setSmallIcon(R.mipmap.ic_notification) // asegúrate de tener un icono válido
                .setContentTitle(title)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        int period = getInputData().getInt("period", -1);

        SharedPreferences prefs = getApplicationContext()
                .getSharedPreferences("prefs", Context.MODE_PRIVATE);

        long lastWeeklyRun = prefs.getLong("last_weekly_check", 0);
        long lastMonthlyRun = prefs.getLong("last_monthly_check", 0);
        long oneWeekInMillis = 7L * 24 * 60 * 60 * 1000;
        long oneMonthInMillis = 30L * 24 * 60 * 60 * 1000;
        long now = System.currentTimeMillis();

        if(period == 3){
            trackPricesAndNotify(period, builder);
        }else if(period == 4 && (now - lastWeeklyRun >= oneWeekInMillis)){
            trackPricesAndNotify(period, builder);
            prefs.edit().putLong("last_weekly_check", now).apply();
        }else if(period == 5 && (now - lastMonthlyRun >= oneMonthInMillis)){
            trackPricesAndNotify(period, builder);
            prefs.edit().putLong("last_monthly_check", now).apply();
        }

        return Result.success(); // o retry() o failure()
    }

    private void trackPricesAndNotify(int period, NotificationCompat.Builder builder){

        TrackedCardRepository trackedCardRepository = new TrackedCardRepository((Application) getApplicationContext());
        List<TrackedCardEntity> trackedCardsByPeriod = trackedCardRepository.getTrackedCardsByPeriod(period);


        for(TrackedCardEntity entity : trackedCardsByPeriod) {
            ScryfallService scryfallService = ScryfallClient.getScryfallService();
            String oracleId = "oracleId:";
            String setCode = " set:";
            ScryfallCardDetailList card = scryfallService.searchCardsSync("released", oracleId.concat(entity.getOracleId()).concat(setCode).concat(entity.getSetCode()), "prints");
            if(card != null && card.getData() != null && !card.getData().isEmpty()){
                ScryfallDetailCard uniqueCard = card.getData().get(0);
                TrackedCardEntity trackedCardEntity = new TrackedCardEntity();
                trackedCardEntity.setOracleId(uniqueCard.getOracleId());
                trackedCardEntity.setSetCode(uniqueCard.getSet());
                trackedCardEntity.setPeriod(period);
                if(uniqueCard.getPrices().getUsd() != null && !uniqueCard.getPrices().getUsd().isEmpty()) {
                    trackedCardEntity.setLastKnownPrice(Double.parseDouble(uniqueCard.getPrices().getUsd()));
                    trackedCardEntity.setSymbol("$");
                    trackedCardRepository.insert(trackedCardEntity);
                }else if(uniqueCard.getPrices().getEur() != null && !uniqueCard.getPrices().getEur().isEmpty()){
                    trackedCardEntity.setLastKnownPrice(Double.parseDouble(uniqueCard.getPrices().getEur()));
                    trackedCardEntity.setSymbol("€");
                    trackedCardRepository.insert(trackedCardEntity);
                }

                notify(trackedCardEntity, uniqueCard, builder);

            }
        }
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private boolean notify(TrackedCardEntity lastTrackedEntity, ScryfallDetailCard currentCard, NotificationCompat.Builder builder){
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        boolean notify = false;
        double lastKnownPrice = lastTrackedEntity.getLastKnownPrice();
        double currentPrice = "€".equals(lastTrackedEntity.getSymbol()) ? Double.parseDouble(currentCard.getPrices().getEur()) : Double.parseDouble(currentCard.getPrices().getUsd());

        double percentage = ((currentPrice - lastKnownPrice) / lastKnownPrice) * 100;

        String message = "";
        if(percentage >= 5.0){
            message = "La carta " + currentCard.getName() + " ha aumentado su precio un " + percentage + "% y ahora vale " + currentPrice + lastTrackedEntity.getSymbol();
            builder.setContentText(message);
            notificationManager.notify((int)System.currentTimeMillis(), builder.build());
        }else if(percentage <= -5.0){
            message = "La carta " + currentCard.getName() + " ha bajado su precio un " + percentage + "% y ahora vale " + currentPrice + lastTrackedEntity.getSymbol();
            builder.setContentText(message);
            notificationManager.notify((int)System.currentTimeMillis(), builder.build());
        }

        return notify;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Price Alerts";
            String description = "Notificaciones de seguimiento de precios";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("PRICE_ALERTS", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getApplicationContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
