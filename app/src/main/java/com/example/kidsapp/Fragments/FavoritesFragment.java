package com.example.kidsapp.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.kidsapp.Adapters.StoryAdapter;
import com.example.kidsapp.Classes.Page;
import com.example.kidsapp.R;
import com.example.kidsapp.Activities.StoryActivity;
import com.example.kidsapp.Classes.StoryStructure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends Fragment {

    private RecyclerView mRecyclerView;

    private StoryAdapter mAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        mRecyclerView = view.findViewById(R.id.favs_recycler);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        List<StoryStructure> items = new ArrayList<>();
        mAdapter = new StoryAdapter(items, new StoryAdapter.ItemClickListener() {
            @Override
            public void onItemClick(StoryStructure item) {
                showToast(item.getTitle() + "Clicked");
                ArrayList<Page> pages = (ArrayList<Page>) item.getPages();
                pages.forEach(Page -> {
                    Page.setImage(getContext().getResources().getIdentifier("image" + Page.getImage(), "drawable", getContext().getPackageName()));
                });
                Intent intent = new Intent(getActivity(), StoryActivity.class);
                Bundle args = new Bundle();
                args.putSerializable("Array", (Serializable) item.getPages());
                intent.putExtra("Pages", args);

                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }
    private void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}