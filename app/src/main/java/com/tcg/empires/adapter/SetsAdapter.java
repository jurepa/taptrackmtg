package com.tcg.empires.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tcg.empires.model.ScryfallDetailCard;

import java.util.List;

public class SetsAdapter extends ArrayAdapter<ScryfallDetailCard> {


    public SetsAdapter(@NonNull Context context, int resource, @NonNull List<ScryfallDetailCard> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        ScryfallDetailCard scryfallDetailCard = getItem(position);
        String text = scryfallDetailCard.getSetName();
        if(scryfallDetailCard.getFrameEffects() != null && !scryfallDetailCard.getFrameEffects().isEmpty()){
            if(scryfallDetailCard.getFrameEffects().contains("etched")){
                text = text.concat(" (Etched foil)");
            } else if (scryfallDetailCard.getFrameEffects().contains("extendedart")) {
                text = text.concat(" (Extended art)");
            } else if (scryfallDetailCard.getFrameEffects().contains("shatteredglass")) {
                text = text.concat(" (Shattered)");
            }else if(scryfallDetailCard.getFrameEffects().contains("showcase")){
                text = text.concat(" (Showcase frame)");
            }else if(scryfallDetailCard.getFrameEffects().contains("inverted")){
                text = text.concat(" (Alter art)");

            }
        }

        textView.setText(text);
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = super.getDropDownView(position, convertView, parent);
        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        ScryfallDetailCard scryfallDetailCard = getItem(position);
        String text = scryfallDetailCard.getSetName();
        if(scryfallDetailCard.getFrameEffects() != null && !scryfallDetailCard.getFrameEffects().isEmpty()){
            if(scryfallDetailCard.getFrameEffects().contains("etched")){
                text = text.concat(" (Etched foil)");
            } else if (scryfallDetailCard.getFrameEffects().contains("extendedart")) {
                text = text.concat(" (Extended art)");
            } else if (scryfallDetailCard.getFrameEffects().contains("shatteredglass")) {
                text = text.concat(" (Shattered)");
            }else if(scryfallDetailCard.getFrameEffects().contains("showcase")){
                text = text.concat(" (Showcase frame)");
            }else if(scryfallDetailCard.getFrameEffects().contains("inverted")){
                text = text.concat(" (Alter art)");

            }
        }
        textView.setText(text);
        return view;
    }

}
