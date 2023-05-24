package com.example.kidsapp.Fragments;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.kidsapp.Activities.StoryActivity;
import com.example.kidsapp.Adapters.FavAdapter;
import com.example.kidsapp.Adapters.StoryAdapter;
import com.example.kidsapp.Classes.Page;
import com.example.kidsapp.Classes.StoryStructure;
import com.example.kidsapp.FavoritesDB;
import com.example.kidsapp.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class FavoritesFragment extends Fragment {

    RecyclerView recyclerView;

    FavoritesDB favDB;

    List<StoryStructure> favStories = new ArrayList<>();

    FavAdapter favAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        favDB = new FavoritesDB(getActivity());

        recyclerView = view.findViewById(R.id.favs_recycler);

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        loadData();

        return view;
    }

    private void showToast(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }

    private void loadData() {
        if (favStories != null){
            favStories.clear();
        }
        SQLiteDatabase db = favDB.getReadableDatabase();
        Cursor cursor = favDB.select_all_favorite_list();
        try {
            while (cursor.moveToNext()){
                String title = cursor.getString(cursor.getColumnIndex(FavoritesDB.ITEM_TITLE));
                String author = cursor.getString(cursor.getColumnIndex(FavoritesDB.ITEM_AUTHOR));
                int image = Integer.parseInt(cursor.getString(cursor.getColumnIndex(FavoritesDB.ITEM_IMAGE)));
                Integer id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(FavoritesDB.ID)));
                String fav = cursor.getString(cursor.getColumnIndex(FavoritesDB.FAVORITE_STATUS));
                StoryStructure story = new StoryStructure( id, image,title, author,fav);
                favStories.add(story);
            }
        }
        finally {
            if (cursor != null && cursor.isClosed())
                cursor.close();
            db.close();
        }
        favAdapter = new FavAdapter(getActivity(),favStories);

        recyclerView.setAdapter(favAdapter);

    }



}