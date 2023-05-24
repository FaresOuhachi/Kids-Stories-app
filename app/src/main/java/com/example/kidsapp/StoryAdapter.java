package com.example.kidsapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.ViewHolder> {

    private final List<StoryStructure> items;

    private final ItemClickListener mItemListener;


    public StoryAdapter(List<StoryStructure> items, ItemClickListener itemClickListener) {
        this.items = items;
        this.mItemListener = itemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        StoryStructure item = items.get(position);
        holder.imageView.setImageResource(item.getImageResource());
        holder.title.setText(item.getTitle());
        holder.author.setText(item.getAuthor());

        holder.itemView.setOnClickListener(view -> {
            mItemListener.onItemClick(items.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface ItemClickListener {
        void onItemClick(StoryStructure item);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView title, author;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.cover);
            title = itemView.findViewById(R.id.title);
            author = itemView.findViewById(R.id.author);

        }


    }

}
