package com.tcg.taptrackmtg.worker;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tcg.taptrackmtg.R;
import com.tcg.taptrackmtg.client.ScryfallClient;
import com.tcg.taptrackmtg.model.ScryfallDetailCard;
import com.tcg.taptrackmtg.repository.TrackedCardRepository;
import com.tcg.taptrackmtg.room.TrackedCardEntity;
import com.tcg.taptrackmtg.service.ScryfallService;

import java.io.IOException;
import java.util.List;

import retrofit2.Response;

public class PriceCheckWorker extends Worker {

    public PriceCheckWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            Log.i(TAG,"doWork:noUserLogged: NO HAY USUARIO LOGADO, NO SE COMPRUEBA NADA");
            return Result.success();
        }

        createNotificationChannel();
        String title = "¡Ojo a esta carta!";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "PRICE_ALERTS")
                .setSmallIcon(R.mipmap.ic_notification)
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

        try{
            if(period == 3){
                trackPricesAndNotify(period, builder);
            }else if(period == 4 && (now - lastWeeklyRun >= oneWeekInMillis)){
                trackPricesAndNotify(period, builder);
                prefs.edit().putLong("last_weekly_check", now).apply();
            }else if(period == 5 && (now - lastMonthlyRun >= oneMonthInMillis)){
                trackPricesAndNotify(period, builder);
                prefs.edit().putLong("last_monthly_check", now).apply();
            }
        }catch (IOException e){
            Log.e("PriceCheckWorker", "Error al obtener la carta de scryfall: " + e);
        }

        return Result.success(); // o retry() o failure()
    }

    private void trackPricesAndNotify(int period, NotificationCompat.Builder builder) throws IOException {

        TrackedCardRepository trackedCardRepository = new TrackedCardRepository((Application) getApplicationContext());
        List<TrackedCardEntity> trackedCardsByPeriod = trackedCardRepository.getTrackedCardsByPeriod(period);


        for(TrackedCardEntity lastTrackedEntity : trackedCardsByPeriod) {
            ScryfallService scryfallService = ScryfallClient.getScryfallService();
            Response<ScryfallDetailCard> response = scryfallService.searchCardsSyncById(lastTrackedEntity.getCardId()).execute();
            ScryfallDetailCard card = response.body();
            if(card != null ){
                TrackedCardEntity trackedCardEntity = new TrackedCardEntity();
                trackedCardEntity.setCardId(card.getId());
                trackedCardEntity.setImageUrl(card.getImageUris().getSmall());
                trackedCardEntity.setPeriod(period);
                trackedCardEntity.setCardName(card.getName());
                trackedCardEntity.setSetName(card.getSetName());
                if(lastTrackedEntity.getSymbol().equals("$") && (card.getPrices().getUsd() != null && !card.getPrices().getUsd().isEmpty())
                        || (lastTrackedEntity.isFoil() && card.getPrices().getUsdFoil() != null && !card.getPrices().getUsdFoil().isEmpty())) {
                    trackedCardEntity.setLastKnownPrice(!lastTrackedEntity.isFoil() ? Double.parseDouble(card.getPrices().getUsd()) : Double.parseDouble(card.getPrices().getUsdFoil()));
                    trackedCardEntity.setSymbol("$");
                    trackedCardRepository.insert(trackedCardEntity);
                }else if(lastTrackedEntity.getSymbol().equals("€") && (card.getPrices().getEur() != null && !card.getPrices().getEur().isEmpty())
                        || (lastTrackedEntity.isFoil() && card.getPrices().getEurFoil() != null && !card.getPrices().getEurFoil().isEmpty())){
                    trackedCardEntity.setLastKnownPrice(!lastTrackedEntity.isFoil() ? Double.parseDouble(card.getPrices().getEur()) : Double.parseDouble(card.getPrices().getEurFoil()));
                    trackedCardEntity.setSymbol("€");
                    trackedCardRepository.insert(trackedCardEntity);
                } else if(card.getFrameEffects() != null && card.getFrameEffects().contains("etched") && !card.getPrices().getUsdEtched().isEmpty()) {
                    trackedCardEntity.setLastKnownPrice(Double.parseDouble(card.getPrices().getUsdEtched()));
                    trackedCardEntity.setSymbol("$");
                    trackedCardRepository.insert(trackedCardEntity);
                }

                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                    notify(trackedCardEntity, card, builder);
                }
            }
        }
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private boolean notify(TrackedCardEntity lastTrackedEntity, ScryfallDetailCard currentCard, NotificationCompat.Builder builder){
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        boolean notify = false;
        double lastKnownPrice = lastTrackedEntity.getLastKnownPrice();
        double currentPrice = 0.0;
        if(lastTrackedEntity.getSymbol().equals("$") && (currentCard.getPrices().getUsd() != null && !currentCard.getPrices().getUsd().isEmpty())
                || (lastTrackedEntity.isFoil() && currentCard.getPrices().getUsdFoil() != null && !currentCard.getPrices().getUsdFoil().isEmpty())) {
            currentPrice = !lastTrackedEntity.isFoil() ? Double.parseDouble(currentCard.getPrices().getUsd()) : Double.parseDouble(currentCard.getPrices().getUsdFoil());
        }else if(lastTrackedEntity.getSymbol().equals("€") && (currentCard.getPrices().getEur() != null && !currentCard.getPrices().getEur().isEmpty())
                || (lastTrackedEntity.isFoil() && currentCard.getPrices().getEurFoil() != null && !currentCard.getPrices().getEurFoil().isEmpty())){
            currentPrice = !lastTrackedEntity.isFoil() ? Double.parseDouble(currentCard.getPrices().getEur()) : Double.parseDouble(currentCard.getPrices().getEurFoil());
        } else if(currentCard.getFrameEffects() != null && currentCard.getFrameEffects().contains("etched") && !currentCard.getPrices().getUsdEtched().isEmpty()) {
            currentPrice = Double.parseDouble(currentCard.getPrices().getUsdEtched());
        }

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
