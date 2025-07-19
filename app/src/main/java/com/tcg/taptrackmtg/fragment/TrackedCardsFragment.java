package com.tcg.taptrackmtg.fragment;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tcg.taptrackmtg.R;
import com.tcg.taptrackmtg.adapter.TrackedCardAdapter;
import com.tcg.taptrackmtg.room.TrackedCardEntity;
import com.tcg.taptrackmtg.viewmodel.TrackedCardsViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TrackedCardsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrackedCardsFragment extends Fragment {


    private FirebaseUser currentUser;

    private SearchView searchView;

    private TrackedCardsViewModel trackedCardsViewModel;

    private RecyclerView recyclerView;

    private TrackedCardAdapter trackedCardAdapter;

    public static TrackedCardsFragment newInstance(FirebaseUser currentUser) {
        return new TrackedCardsFragment(currentUser);
    }

    private TrackedCardsFragment(FirebaseUser currentUser){
        super();
        this.currentUser = currentUser;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        trackedCardsViewModel = new ViewModelProvider(this).get(TrackedCardsViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tracked_cards, container, false);
        searchView = v.findViewById(R.id.searchView);
        searchView.setIconified(false);
        searchView.setQueryHint("Buscar...");
        recyclerView = v.findViewById(R.id.recyclerViewTrackedCards);

        trackedCardsViewModel.getTrackedCards().observe(requireActivity(), trackedCardEntities -> {
            if(trackedCardEntities != null){
                trackedCardAdapter = new TrackedCardAdapter(requireContext(), trackedCardEntities);
                recyclerView.setAdapter(trackedCardAdapter);
                ItemTouchHelper itemTouchHelper = getItemTouchHelper();
                itemTouchHelper.attachToRecyclerView(recyclerView);
            }
        });

        trackedCardsViewModel.getUserTrackedCards(currentUser.getUid());

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                trackedCardAdapter.getFilter().filter(newText);
                return true;
            }
        });

        return v;
    }

    @NonNull
    private ItemTouchHelper getItemTouchHelper() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT) {
                    TrackedCardEntity card = trackedCardAdapter.getItemAt(position);
                    trackedCardsViewModel.removeTrackedCardById(card.getCardId(), currentUser.getUid());
                    trackedCardAdapter.removeItem(position);
                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c,
                                    @NonNull RecyclerView recyclerView,
                                    @NonNull RecyclerView.ViewHolder viewHolder,
                                    float dX, float dY,
                                    int actionState, boolean isCurrentlyActive) {

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                View itemView = viewHolder.itemView;
                Paint backgroundPaint = new Paint();
                backgroundPaint.setColor(ContextCompat.getColor(recyclerView.getContext(), R.color.primary));
                RectF background = new RectF(itemView.getRight() + dX, itemView.getTop(),
                        itemView.getRight(), itemView.getBottom());
                c.drawRect(background, backgroundPaint);

                Drawable icon = ContextCompat.getDrawable(recyclerView.getContext(), R.drawable.outline_bookmark_remove_24);
                if (icon != null) {
                    int iconSize = icon.getIntrinsicWidth();
                    int iconTop = itemView.getTop() + (itemView.getHeight() / 2) - iconSize;
                    int iconBottom = iconTop + iconSize;


                    float swipeProgress = Math.min(-dX, itemView.getWidth() / 2f);
                    int centerX = itemView.getRight() - (int) swipeProgress / 2;

                    int iconLeft = centerX - (iconSize / 2);
                    int iconRight = centerX + (iconSize / 2);

                    icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                    icon.draw(c);

                    // Dibuja el texto debajo
                    Paint textPaint = new Paint();
                    textPaint.setColor(Color.WHITE);
                    textPaint.setTextSize(32f);
                    textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    textPaint.setTextAlign(Paint.Align.CENTER);

                    String text = "Remove";
                    float textX = centerX;
                    float textY = iconBottom + 36;

                    c.drawText(text, textX, textY, textPaint);
                }

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };


        return new ItemTouchHelper(simpleCallback);
    }
}