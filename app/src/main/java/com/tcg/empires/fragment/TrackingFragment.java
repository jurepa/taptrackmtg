package com.tcg.empires.fragment;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.squareup.picasso.Picasso;
import com.tcg.empires.R;
import com.tcg.empires.adapter.SetsAdapter;
import com.tcg.empires.model.ScryfallDetailCard;
import com.tcg.empires.viewmodel.CardDetailViewModel;

import java.util.ArrayList;
import java.util.List;


//FIXME: APIKEY JUSTTCG tcg_0ec1354933154a4480cc34915a485bb8
public class TrackingFragment extends Fragment {

    private CardDetailViewModel cardDetailViewModel;

    private AutoCompleteTextView buscador;

    private Spinner setsDropdown;

    private ImageView cardImage;
    private TextView cardName;
    private TextView cardText;

    private TextView oftenText;

    private RadioGroup oftenGroup;
    private CheckBox checkPrice;
    private TextView typeLine;
    private TextView notAvailableText;
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
        cardName = view.findViewById(R.id.cardName);
        cardText = view.findViewById(R.id.cardText);
        typeLine = view.findViewById(R.id.typeLine);
        checkPrice = view.findViewById(R.id.trackPriceCheck);
        oftenGroup = view.findViewById(R.id.periodRadioGroup);
        oftenText = view.findViewById(R.id.oftenText);
        notAvailableText = view.findViewById(R.id.not_available);
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

        buscador.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                buscador.dismissDropDown();
                String text = buscador.getText().toString();
                cardDetailViewModel.searchByExactName(text);
            }
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
                String imageUrl = scryfallDetailCard.getImageUris().getNormal();

                cardDetailViewModel.getAllPrintsFromOracleId(scryfallDetailCard.getOracleId());

                Picasso.get()
                        .load(imageUrl)
                        .error(R.drawable.ic_launcher_background)
                        .into(cardImage);

            }else{
                Toast.makeText(requireContext(), "Hubo un error consultando el detalle de la carta", Toast.LENGTH_SHORT).show();
            }
        });

        cardDetailViewModel.getAllPrintsFromOracleId().observe(getViewLifecycleOwner(), scryfallCardDetailList -> {

            if(scryfallCardDetailList != null && scryfallCardDetailList.getData() != null){
                SetsAdapter spinnerAdapter = new SetsAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        scryfallCardDetailList.getData()
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
                checkPrice.setVisibility(VISIBLE);
                checkPrice.setChecked(false);
                oftenGroup.clearCheck();

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
            }
        });

        checkPrice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    oftenText.setVisibility(VISIBLE);
                    oftenGroup.setVisibility(VISIBLE);
                }else{
                    oftenText.setVisibility(INVISIBLE);
                    oftenGroup.setVisibility(INVISIBLE);
                }
            }
        });
        return view;
    }

}