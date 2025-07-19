package com.tcg.taptrackmtg.worker;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.tcg.taptrackmtg.repository.TrackedCardRepository;
import com.tcg.taptrackmtg.room.TrackedCardEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoomToFirebaseWorker extends Worker {


    private final FirebaseFirestore firestore;

    public RoomToFirebaseWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        firestore = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public Result doWork() {
        TrackedCardRepository trackedCardRepository = new TrackedCardRepository((Application) getApplicationContext());
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return Result.success(); // No user = no sync
        }

        try {
            List<TrackedCardEntity> trackedCards = trackedCardRepository.getAllCards();

            String userId = user.getUid();
            WriteBatch batch = firestore.batch();

            for (TrackedCardEntity card : trackedCards) {
                DocumentReference docRef = firestore
                        .collection("users")
                        .document(userId)
                        .collection("tracked_cards")
                        .document(card.getCardId()); // <-- Usa cardId como ID único
                batch.set(docRef, card);
            }

            // Commit de los cambios a Firestore
            batch.commit().addOnSuccessListener(unused -> {
                Log.d("RoomToFirestoreWorker", "Backup successful");
            }).addOnFailureListener(e -> {
                Log.e("RoomToFirestoreWorker", "Backup failed", e);
            });



            return Result.success();

        } catch (Exception e) {
            Log.e("RoomToFirestoreWorker", "Unexpected error: " + e.getMessage());
            return Result.retry(); // Si algo falla, WorkManager lo intentará de nuevo
        }
    }
}
