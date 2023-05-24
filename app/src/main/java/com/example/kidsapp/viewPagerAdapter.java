package com.example.kidsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;

public class viewPagerAdapter extends PagerAdapter {

    Context context;
    ArrayList<Page> pages;
    LayoutInflater layoutInflater;

    public viewPagerAdapter(Context context, ArrayList<Page> pages) {
        this.context = context;
        this.pages = pages;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return pages.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {

        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.viewpager_item, container, false);
        ImageView imageView = view.findViewById(R.id.story_image);
        imageView.setImageResource(pages.get(position).getImage());
        TextView textView = view.findViewById(R.id.story_text);
        textView.setText(pages.get(position).getText());
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((View) object);
    }
}
