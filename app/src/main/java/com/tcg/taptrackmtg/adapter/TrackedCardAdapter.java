package com.tcg.taptrackmtg.adapter;

import static android.view.View.VISIBLE;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tcg.taptrackmtg.R;
import com.tcg.taptrackmtg.room.TrackedCardEntity;

import java.util.ArrayList;
import java.util.List;

public class TrackedCardAdapter extends RecyclerView.Adapter<TrackedCardAdapter.ViewHolder> implements Filterable {


    private final Context context;
    private List<TrackedCardEntity> trackedCards;

    private final List<TrackedCardEntity> allTrackedCards;  // Lista completa (no se modifica)

    public TrackedCardAdapter(Context context, List<TrackedCardEntity> list) {
        this.context = context;
        this.trackedCards = new ArrayList<>(list);
        this.allTrackedCards = new ArrayList<>(list);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<TrackedCardEntity> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(allTrackedCards);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (TrackedCardEntity card : allTrackedCards) {
                        if (card.getCardName().toLowerCase().contains(filterPattern)) {
                            filteredList.add(card);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                trackedCards.clear();
                trackedCards.addAll((List) results.values);
                notifyDataSetChanged();
            }
        };
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
        // Siempre detener animación anterior
        holder.glowingEffect.clearAnimation();
        holder.glowingEffect.setAnimation(null);
        holder.glowingEffect.setVisibility(View.INVISIBLE);
        if(trackedCards.get(position).isFoil()){
            holder.glowingEffect.setVisibility(View.VISIBLE);
            Animation foilAnim = AnimationUtils.loadAnimation(context, R.anim.foil);
            holder.glowingEffect.startAnimation(foilAnim);
        }else {
            holder.glowingEffect.clearAnimation();
            holder.glowingEffect.setVisibility(View.INVISIBLE);
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
    public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if(holder.glowingEffect.getVisibility() == VISIBLE){
            holder.glowingEffect.clearAnimation();
            holder.glowingEffect.setAnimation(null);
            holder.glowingEffect.setVisibility(View.INVISIBLE);
            holder.glowingEffect.setVisibility(View.VISIBLE);
            Animation foilAnim = AnimationUtils.loadAnimation(context, R.anim.foil);
            holder.glowingEffect.startAnimation(foilAnim);
        }
    }

    @Override
    public int getItemCount() {
        return trackedCards.size();
    }
}
