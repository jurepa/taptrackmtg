package com.tcg.empires.fragment;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.squareup.picasso.Picasso;
import com.tcg.empires.R;
import com.tcg.empires.adapter.SetsAdapter;
import com.tcg.empires.model.ScryfallDetailCard;
import com.tcg.empires.room.TrackedCardEntity;
import com.tcg.empires.viewmodel.CardDetailViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;


//FIXME: APIKEY JUSTTCG tcg_0ec1354933154a4480cc34915a485bb8
public class TrackingFragment extends Fragment {

    private CardDetailViewModel cardDetailViewModel;
    private AutoCompleteTextView buscador;
    private TextView setLabel;
    private Spinner setsDropdown;
    private ImageView cardImage;
    private ImageView glowOverlay;
    private TextView cardName;
    private TextView cardText;
    private TextView oftenText;
    private ChipGroup oftenGroup;
    private MaterialSwitch checkPrice;
    private MaterialSwitch foilSwitch;
    private TextView typeLine;
    private TextView notAvailableText;
    private Button confirmTrackButton;
    private Chip dailyChip;
    private Chip weeklyChip;
    private Chip monthlyChip;
    private ArrayAdapter<String> cartas;
    private List<String> cardNames = new ArrayList<>();

    public static TrackingFragment newInstance() {
        return new TrackingFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cardDetailViewModel = new ViewModelProvider(this).get(CardDetailViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tracking, container, false);
        cardImage = view.findViewById(R.id.cardImage);
        setsDropdown = view.findViewById(R.id.set);
        setLabel = view.findViewById(R.id.sets_label);
        cardName = view.findViewById(R.id.cardName);
        cardText = view.findViewById(R.id.cardText);
        typeLine = view.findViewById(R.id.typeLine);
        checkPrice = view.findViewById(R.id.trackPriceCheck);
        oftenGroup = view.findViewById(R.id.periodChipGroup);
        oftenText = view.findViewById(R.id.oftenText);
        dailyChip = view.findViewById(R.id.daily);
        weeklyChip = view.findViewById(R.id.weekly);
        monthlyChip = view.findViewById(R.id.monthly);
        notAvailableText = view.findViewById(R.id.not_available);
        confirmTrackButton = view.findViewById(R.id.confirmTrackBtn);
        foilSwitch = view.findViewById(R.id.foilSwitch);
        glowOverlay = view.findViewById(R.id.glowOverlay);
        cartas = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, cardNames);
        cartas.setNotifyOnChange(true);
        buscador = view.findViewById(R.id.busquedaCartas);
        buscador.setThreshold(3);
        buscador.setAdapter(cartas);

        buscador.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() >= 3){
                    cardDetailViewModel.searchCards(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        buscador.setOnItemClickListener((parent, view1, position, id) -> {
            buscador.dismissDropDown();
            String text = buscador.getText().toString();
            cardDetailViewModel.searchByExactName(text);
            setsDropdown.setVisibility(VISIBLE);
            setLabel.setVisibility(VISIBLE);
        });

        cardDetailViewModel.getAutocomplete().observe(getViewLifecycleOwner(), cardList -> {
            cardNames.clear();
            if (cardList != null && cardList.getData() != null && !cardList.getData().isEmpty()) {

                List<String> newCards = cardList.getData();
                cartas = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, newCards);
                cartas.getFilter().filter(buscador.getText().toString());
                buscador.setAdapter(cartas);

            }else if(cardList == null){
                Toast.makeText(requireContext(), "Hubo un error consultando las cartas", Toast.LENGTH_SHORT).show();
            }
        });

        cardDetailViewModel.getCardDetail().observe(getViewLifecycleOwner(), scryfallDetailCard -> {

            if(scryfallDetailCard != null){

                cardDetailViewModel.getAllPrintsFromOracleId(scryfallDetailCard.getOracleId());

            }else{
                Toast.makeText(requireContext(), "Hubo un error consultando el detalle de la carta", Toast.LENGTH_SHORT).show();
            }
        });

        cardDetailViewModel.getAllPrintsFromOracleId().observe(getViewLifecycleOwner(), scryfallCardDetailList -> {

            if(scryfallCardDetailList != null && scryfallCardDetailList.getData() != null){
                SetsAdapter spinnerAdapter = new SetsAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        scryfallCardDetailList.getData().stream().filter(scryfallDetailCard -> !(scryfallDetailCard.getGames().size() == 1 && scryfallDetailCard.getGames().contains("arena"))).collect(Collectors.toList())
                );
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                setsDropdown.setAdapter(spinnerAdapter);

            }else{
                Toast.makeText(requireContext(), "Hubo un error consultando los prints de la carta", Toast.LENGTH_SHORT).show();

            }

        });

        setsDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ScryfallDetailCard setSelected = (ScryfallDetailCard) setsDropdown.getAdapter().getItem(position);

                cardName.setText(String.format("Card name: %s", setSelected.getName()));
                cardText.setText(String.format("Card text: %s", setSelected.getOracleText()));
                typeLine.setText(String.format("Type line: %s", setSelected.getTypeLine()));
                cardDetailViewModel.getByCardId(setSelected.getId(), new Consumer<List<TrackedCardEntity>>() {
                    @Override
                    public void accept(List<TrackedCardEntity> trackedCardEntities) {
                        requireActivity().runOnUiThread(() ->{
                            checkPrice.setVisibility(VISIBLE);
                            foilSwitch.setVisibility(VISIBLE);
                            checkPrice.setChecked(false);
                            oftenGroup.clearCheck();
                            if((setSelected.isFoil() && !setSelected.isNonfoil()) || (setSelected.getFrameEffects() != null && setSelected.getFrameEffects().contains("etched"))){
                                foilSwitch.setChecked(true);
                                foilSwitch.setEnabled(false);
                            }else if(!setSelected.isFoil() && setSelected.isNonfoil()){
                                foilSwitch.setChecked(false);
                                foilSwitch.setEnabled(false);
                            }else {
                                foilSwitch.setChecked(false);
                                foilSwitch.setEnabled(true);
                            }
                            if(trackedCardEntities != null && !trackedCardEntities.isEmpty()){
                                checkPrice.setEnabled(false);
                                checkPrice.setChecked(true);
                                foilSwitch.setEnabled(false);
                                foilSwitch.setChecked(trackedCardEntities.get(0).isFoil());
                                oftenText.setVisibility(VISIBLE);
                                oftenGroup.setVisibility(VISIBLE);
                                oftenGroup.setEnabled(false);
                                for(int i = 2; i < oftenGroup.getChildCount(); i++){
                                    ((Chip)oftenGroup.getChildAt(i)).setEnabled(false);
                                }
                                checkChip(trackedCardEntities.get(0).getPeriod());
                                confirmTrackButton.setText(R.string.stop_tracking);
                            }else{
                                checkPrice.setEnabled(true);
                                for(int i = 2; i < oftenGroup.getChildCount(); i++){
                                    ((Chip)oftenGroup.getChildAt(i)).setEnabled(true);
                                }
                                confirmTrackButton.setText(R.string.track_price);
                            }
                        });
                    }
                });



                if(setSelected.getImageUris() != null) {
                    notAvailableText.setVisibility(INVISIBLE);
                    cardImage.setVisibility(VISIBLE);
                    Picasso.get()
                            .load(setSelected.getImageUris().getNormal())
                            .error(R.drawable.ic_launcher_background)
                            .into(cardImage);
                }else{
                    cardImage.setVisibility(INVISIBLE);
                    notAvailableText.setVisibility(VISIBLE);

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                checkPrice.setVisibility(INVISIBLE);
                foilSwitch.setVisibility(INVISIBLE);
            }
        });

        foilSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Animation foilAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.foil);
                    glowOverlay.startAnimation(foilAnim);
                    glowOverlay.setVisibility(VISIBLE);
                }else {
                    glowOverlay.clearAnimation();
                    glowOverlay.setVisibility(GONE);
                }
            }
        });

        checkPrice.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                oftenText.setVisibility(VISIBLE);
                oftenGroup.setVisibility(VISIBLE);
            }else{
                oftenGroup.clearCheck();
                oftenText.setVisibility(INVISIBLE);
                oftenGroup.setVisibility(INVISIBLE);
                confirmTrackButton.setVisibility(INVISIBLE);
            }
        });

        oftenGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if(!checkedIds.isEmpty() && !checkedIds.contains(-1)) {
                confirmTrackButton.setVisibility(VISIBLE);
            }else {
                confirmTrackButton.setVisibility(INVISIBLE);
            }
        });

        confirmTrackButton.setOnClickListener(v -> {
           int radioButtonId = oftenGroup.getCheckedChipId();
           ScryfallDetailCard detailCard = (ScryfallDetailCard) setsDropdown.getSelectedItem();
           cardDetailViewModel.getByCardId(detailCard.getId(), new Consumer<>() {
               @Override
               public void accept(List<TrackedCardEntity> trackedCardEntities) {

                   requireActivity().runOnUiThread(() -> {
                       if (trackedCardEntities != null && !trackedCardEntities.isEmpty()) {
                           cardDetailViewModel.stopTrackingByCardId(trackedCardEntities.get(0).getCardId());
                           checkPrice.setChecked(false);
                           oftenGroup.clearCheck();
                           confirmTrackButton.setText(R.string.track_price);
                           checkPrice.setEnabled(true);
                           if((detailCard.isFoil() && !detailCard.isNonfoil()) || (detailCard.getFrameEffects() != null && detailCard.getFrameEffects().contains("etched"))){
                               foilSwitch.setChecked(true);
                               foilSwitch.setEnabled(false);
                           }else if(!detailCard.isFoil() && detailCard.isNonfoil()){
                               foilSwitch.setChecked(false);
                               foilSwitch.setEnabled(false);
                           }else {
                               foilSwitch.setChecked(false);
                               foilSwitch.setEnabled(true);
                           }

                           for (int i = 2; i < oftenGroup.getChildCount(); i++) {
                               ((Chip) oftenGroup.getChildAt(i)).setEnabled(true);
                           }
                           Toast.makeText(requireContext(), "You stopped tracking " + detailCard.getName() + "!", Toast.LENGTH_SHORT).show();
                       } else {
                           TrackedCardEntity trackedCardEntity = new TrackedCardEntity();
                           trackedCardEntity.setCardId(detailCard.getId());
                           trackedCardEntity.setImageUrl(detailCard.getImageUris().getSmall());
                           trackedCardEntity.setCardName(detailCard.getName());
                           trackedCardEntity.setSetName(detailCard.getSetName());
                           trackedCardEntity.setPeriod(getPeriodNumber(radioButtonId));
                           trackedCardEntity.setFoil(foilSwitch.isChecked());
                           if ((detailCard.getPrices().getEur() != null && !detailCard.getPrices().getEur().isEmpty())
                           || (foilSwitch.isChecked() && detailCard.getPrices().getEurFoil() != null && !detailCard.getPrices().getEurFoil().isEmpty())) {
                               trackedCardEntity.setLastKnownPrice(!foilSwitch.isChecked() ? Double.parseDouble(detailCard.getPrices().getEur()) : Double.parseDouble(detailCard.getPrices().getEurFoil()));
                               trackedCardEntity.setSymbol("€");
                               cardDetailViewModel.insertCard(trackedCardEntity);
                               Toast.makeText(requireContext(), "Tracking " + detailCard.getName() + "!", Toast.LENGTH_SHORT).show();
                               confirmTrackButton.setText(R.string.stop_tracking);
                               for (int i = 2; i < oftenGroup.getChildCount(); i++) {
                                   ((Chip) oftenGroup.getChildAt(i)).setEnabled(false);
                               }
                               checkPrice.setEnabled(false);
                               foilSwitch.setEnabled(false);
                           } else if ((detailCard.getPrices().getUsd() != null && !detailCard.getPrices().getUsd().isEmpty())
                           || (foilSwitch.isChecked() && detailCard.getPrices().getUsdFoil() != null && !detailCard.getPrices().getUsdFoil().isEmpty())) {
                               trackedCardEntity.setLastKnownPrice(!foilSwitch.isChecked() ? Double.parseDouble(detailCard.getPrices().getUsd()) : Double.parseDouble(detailCard.getPrices().getUsdFoil()));
                               trackedCardEntity.setSymbol("$");
                               cardDetailViewModel.insertCard(trackedCardEntity);
                               Toast.makeText(requireContext(), "Tracking " + detailCard.getName() + "!", Toast.LENGTH_SHORT).show();
                               confirmTrackButton.setText(R.string.stop_tracking);
                               checkPrice.setEnabled(false);
                               foilSwitch.setEnabled(false);
                               for (int i = 2; i < oftenGroup.getChildCount(); i++) {
                                   ((Chip) oftenGroup.getChildAt(i)).setEnabled(false);
                               }
                           } else if(detailCard.getFrameEffects() != null && detailCard.getFrameEffects().contains("etched") && !detailCard.getPrices().getUsdEtched().isEmpty()){
                               trackedCardEntity.setLastKnownPrice(Double.parseDouble(detailCard.getPrices().getUsdEtched()));
                               trackedCardEntity.setSymbol("$");
                               cardDetailViewModel.insertCard(trackedCardEntity);
                               Toast.makeText(requireContext(), "Tracking " + detailCard.getName() + "!", Toast.LENGTH_SHORT).show();
                               confirmTrackButton.setText(R.string.stop_tracking);
                               checkPrice.setEnabled(false);
                               foilSwitch.setEnabled(false);
                               for (int i = 2; i < oftenGroup.getChildCount(); i++) {
                                   ((Chip) oftenGroup.getChildAt(i)).setEnabled(false);
                               }
                           }else {
                               Toast.makeText(requireContext(), "Price not available for this print", Toast.LENGTH_SHORT).show();
                           }
                       }
                   });
               }
           });



        });
        return view;
    }

    private void checkChip(int period) {
        switch (period){
            case 3: dailyChip.setChecked(true);
                break;
            case 4: weeklyChip.setChecked(true);
                break;
            case 5:monthlyChip.setChecked(true);
                break;
        }
    }

    private int getPeriodNumber(int chipId){
        int period = 0;
        if(chipId == R.id.daily){
            period = 3;
        } else if (chipId == R.id.weekly) {
            period = 4;
        }else{
            period = 5;
        }
        return period;
    }

}