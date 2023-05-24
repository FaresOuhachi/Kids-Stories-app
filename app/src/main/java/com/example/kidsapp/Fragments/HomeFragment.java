package com.example.kidsapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kidsapp.Adapters.StoryAdapter;
import com.example.kidsapp.Classes.Page;
import com.example.kidsapp.R;
import com.example.kidsapp.Activities.StoryActivity;
import com.example.kidsapp.Classes.StoryStructure;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    XmlPullParserFactory parserFactory;
    private RecyclerView mRecyclerView;
    private StoryAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        List<StoryStructure> items = new ArrayList<>();
        //Getting all the data from stories.xml

        try {

            parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);

            InputStream inputStream = getResources().openRawResource(R.raw.stories);


            parser.setInput(inputStream, null);


            int eventType = parser.getEventType();


            Integer id = 0;
            String title = "";
            String author = "";
            Integer pageNum = 0;
            Integer img = 0;
            int cover = 0;
            String texts = "";
            String fav = "0";
            StoryStructure storyStructure = new StoryStructure(id, cover, title, author,fav);
            Page page = new Page(pageNum, img, texts);
            ArrayList<StoryStructure> stories_list = new ArrayList<>();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName;

                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        tagName = parser.getName();
                        Log.e("tag Opened", tagName);
                        parser.next();
                        if (tagName.equals("storyStructure")) {

                            storyStructure = new StoryStructure(id, cover, title, author,fav);
                            title = "";
                            author = "";

                            storyStructure.setTitle(title);
                            storyStructure.setAuthor(author);
                        } else if (tagName.equals("id")) {
                            id = Integer.parseInt(parser.getText());
                            storyStructure.setId(id);

                        } else if (tagName.equals("cover")) {

                            cover = Integer.parseInt(parser.getText());

                            storyStructure.setImageResource(cover);

                        } else if (tagName.equals("title") || tagName.equals("author")) {

                            if (parser.getEventType() == XmlPullParser.TEXT) {
                                if (tagName.equals("title")) {
                                    title = parser.getText();
                                    storyStructure.setTitle(title);
                                } else {
                                    author = parser.getText();
                                    storyStructure.setAuthor(author);
                                }
                            }
                        } else if (tagName.equals("num")) {
                            pageNum = Integer.parseInt(parser.getText());
                            page.setPageNum(pageNum);
                        } else if (tagName.equals("page")) {

                            page = new Page(pageNum, img, texts);
                            img = 0;
                            texts = "";

                            page.setImage(img);
                            page.setText(texts);

                        } else if (tagName.equals("image")) {

                            if (parser.getEventType() == XmlPullParser.TEXT) {
                                img = Integer.parseInt(parser.getText());
                                page.setImage(img);
                            }
                        } else if (tagName.equals("text")) {

                            if (parser.getEventType() == XmlPullParser.TEXT) {
                                texts = parser.getText();
                                page.setText(texts);
                            }
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        tagName = parser.getName();
                        if (tagName.equals("storyStructure")) {
                            if (storyStructure != null) {
                                stories_list.add(storyStructure);
                            } else {
                                Log.e("StoryActivity", "Null storyStructure object");
                            }
                        } else if (tagName.equals("page")) {
                            storyStructure.addPage(page);
                        }
                        break;

                }
                Log.e("Tnaket","stories_list");
                eventType = parser.next();

            };
            items = stories_list;
            System.out.println("items size: "+ items.size());

        } catch (Exception e) {
            e.printStackTrace();
        }
        items.forEach(StoryStructure -> {
            StoryStructure.setImageResource(getContext().getResources().getIdentifier("cover" + StoryStructure.getImageResource(), "drawable", getContext().getPackageName()));
        });
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


