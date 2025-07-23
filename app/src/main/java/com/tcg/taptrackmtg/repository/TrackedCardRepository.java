package com.tcg.taptrackmtg.repository;


import static android.content.ContentValues.TAG;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.WriteBatch;
import com.tcg.taptrackmtg.room.TrackedCardEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class TrackedCardRepository {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference cardsRef = db.collection("trackedCards");

    public void insert(final TrackedCardEntity card) {
        cardsRef
                .whereEqualTo("userId", card.getUserId())
                .whereEqualTo("cardId", card.getCardId())
                .orderBy("id", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    int nextId = 1;
                    if (!queryDocumentSnapshots.isEmpty()) {
                        TrackedCardEntity last = queryDocumentSnapshots.getDocuments().get(0).toObject(TrackedCardEntity.class);
                        if (last != null && last.getId() != null) {
                            nextId = last.getId() + 1;
                        }
                    }
                    card.setId(nextId);

                    String docId = card.getUserId() + "_" + card.getCardId() + "_" + nextId;

                    cardsRef.document(docId).set(card)
                            .addOnSuccessListener(aVoid -> Log.d("Firestore", "Card inserted with docId: " + docId))
                            .addOnFailureListener(e -> Log.e("Firestore", "Error inserting card", e));
                }).addOnFailureListener(e -> Log.e("Firestore", "Error inserting card", e));
    }

    public void getByCardId(String userId, String cardId, Consumer<List<TrackedCardEntity>> callback) {
        cardsRef
                .whereEqualTo("userId", userId)
                .whereEqualTo("cardId", cardId)
                .orderBy("id", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(snapshot -> {
                    List<TrackedCardEntity> list = new ArrayList<>();
                    for (DocumentSnapshot doc : snapshot) {
                        list.add(doc.toObject(TrackedCardEntity.class));
                    }
                    callback.accept(list);
                })
                .addOnFailureListener( e -> {
                    Log.e(TAG,e.getMessage());
                });
    }

    public void stopTrackingByCardId(String cardId, String userId) {
        cardsRef
                .whereEqualTo("userId", userId)
                .whereEqualTo("cardId", cardId)
                .get()
                .addOnSuccessListener(snapshot -> {
                    WriteBatch batch = db.batch();
                    for (DocumentSnapshot doc : snapshot) {
                        batch.delete(doc.getReference());
                    }
                    batch.commit();
                });
    }

    public void getTrackedCards(String userId, Consumer<List<TrackedCardEntity>> callback) {
        cardsRef
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener(snapshot -> {
                    Map<String, TrackedCardEntity> latestByCardId = new HashMap<>();
                    for (DocumentSnapshot doc : snapshot) {
                        TrackedCardEntity card = doc.toObject(TrackedCardEntity.class);
                        if (card != null) {
                            String cardId = card.getCardId();
                            TrackedCardEntity existing = latestByCardId.get(cardId);
                            if (existing == null || card.getId() > existing.getId()) {
                                latestByCardId.put(cardId, card);
                            }
                        }
                    }
                    callback.accept(new ArrayList<>(latestByCardId.values()));
                });
    }

    public void getTrackedCardsByPeriod(int period, String userId, Consumer<List<TrackedCardEntity>> callback) {

        cardsRef
                .whereEqualTo("userId", userId)
                .whereEqualTo("period", period)
                .get()
                .addOnSuccessListener(snapshot -> {
                    Map<String, TrackedCardEntity> latestByCardId = new HashMap<>();
                    for (DocumentSnapshot doc : snapshot) {
                        TrackedCardEntity card = doc.toObject(TrackedCardEntity.class);
                        if (card != null) {
                            String cardId = card.getCardId();
                            TrackedCardEntity existing = latestByCardId.get(cardId);
                            if (existing == null || card.getId() > existing.getId()) {
                                latestByCardId.put(cardId, card);
                            }
                        }
                    }
                    callback.accept(new ArrayList<>(latestByCardId.values()));
                });

    }


}
