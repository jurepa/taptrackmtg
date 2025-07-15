package com.tcg.empires.fragment;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tcg.empires.R;
import com.tcg.empires.adapter.TrackedCardAdapter;
import com.tcg.empires.room.TrackedCardEntity;
import com.tcg.empires.viewmodel.CardDetailViewModel;
import com.tcg.empires.viewmodel.TrackedCardsViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TrackedCardsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrackedCardsFragment extends Fragment {


    private SearchView searchView;

    private TrackedCardsViewModel trackedCardsViewModel;

    private RecyclerView recyclerView;

    private TrackedCardAdapter trackedCardAdapter;

    public static TrackedCardsFragment newInstance() {
        return new TrackedCardsFragment();
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
            }
        });

        trackedCardsViewModel.getUserTrackedCards();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return v;
    }
}