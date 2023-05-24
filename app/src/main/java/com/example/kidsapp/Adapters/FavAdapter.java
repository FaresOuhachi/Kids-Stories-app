package com.example.kidsapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kidsapp.Classes.StoryStructure;
import com.example.kidsapp.FavoritesDB;
import com.example.kidsapp.R;

import java.util.List;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.ViewHolder>{

    Context context;
    List<StoryStructure> favStories;

    FavoritesDB favDB;

    public FavAdapter(Context context, List<StoryStructure> favStories) {
        this.context = context;
        this.favStories = favStories;
        favDB = new FavoritesDB(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fav_item, parent, false);
        favDB = new FavoritesDB(context);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(favStories.get(position).getTitle());
        holder.author.setText(favStories.get(position).getAuthor());
        holder.favBtn.setBackgroundTintList(null);
        holder.cover.setImageResource(favStories.get(position).getImageResource());

    }

    @Override
    public int getItemCount() {
        return favStories.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title,author;
        ImageView cover;

        Button favBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            favBtn = itemView.findViewById(R.id.fav_red);
           title = itemView.findViewById(R.id.title_fav);
           author = itemView.findViewById(R.id.author_fav);
            cover = itemView.findViewById(R.id.cover_fav);

            favBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    StoryStructure item = favStories.get(position);
                    favDB.remove_fav(String.valueOf(item.getId()));
                    removeItem(position);
                }
            });
        }

        private void removeItem(int position) {
            favStories.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, favStories.size());
        }
    }
}
