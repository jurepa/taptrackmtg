package com.tcg.empires.adapter;

import static android.view.View.VISIBLE;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tcg.empires.R;
import com.tcg.empires.room.TrackedCardEntity;

import java.util.ArrayList;
import java.util.List;

public class TrackedCardAdapter extends RecyclerView.Adapter<TrackedCardAdapter.ViewHolder> {

    private final Context context;
    private List<TrackedCardEntity> trackedCards;

    public TrackedCardAdapter(Context context, List<TrackedCardEntity> list) {
        this.context = context;
        this.trackedCards = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView cardName, textTrackingPeriod, textLastPrice;
        ImageView imageCardSmall;

        ImageView glowingEffect;
        public ViewHolder(View itemView) {
            super(itemView);
            cardName = itemView.findViewById(R.id.textCardName);
            textTrackingPeriod = itemView.findViewById(R.id.textTrackingPeriod);
            textLastPrice = itemView.findViewById(R.id.textLastPrice);
            imageCardSmall = itemView.findViewById(R.id.imageCardSmall);
            glowingEffect = itemView.findViewById(R.id.glowOverlayAdapter);
        }
    }


    @NonNull
    @Override
    public TrackedCardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TrackedCardAdapter.ViewHolder viewHolder = null;

        viewHolder = new TrackedCardAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_tracked_card, parent, false));

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Picasso.get().load(trackedCards.get(position).getImageUrl()).into(holder.imageCardSmall);
        if(trackedCards.get(position).isFoil()){
            Animation foilAnim = AnimationUtils.loadAnimation(this.context, R.anim.foil);
            holder.glowingEffect.startAnimation(foilAnim);
            holder.glowingEffect.setVisibility(VISIBLE);
        }
        holder.cardName.setText(trackedCards.get(position).getCardName() + " - " + trackedCards.get(position).getSetName());
        holder.textLastPrice.setText("Last price tracked: " + trackedCards.get(position).getLastKnownPrice() + trackedCards.get(position).getSymbol());
        int period = trackedCards.get(position).getPeriod();
        switch (period){
            case 3: holder.textTrackingPeriod.setText("Tracking period: Daily" );
                break;
            case 4:holder.textTrackingPeriod.setText("Tracking period: Weekly" );
                break;
            case 5:holder.textTrackingPeriod.setText("Tracking period: Monthly");
                break;
        }

    }

    @Override
    public int getItemCount() {
        return trackedCards.size();
    }
}
