package com.example.kidsapp.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kidsapp.FavoritesDB;
import com.example.kidsapp.R;
import com.example.kidsapp.Classes.StoryStructure;

import java.util.List;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.ViewHolder> {

    private final List<StoryStructure> items;

    private final ItemClickListener mItemListener;

    private Context context;
    private FavoritesDB db;

    public StoryAdapter(List<StoryStructure> items, ItemClickListener itemClickListener) {
        this.items = items;
        this.mItemListener = itemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext(); // Assign the valid Context object to 'context'
        db = new FavoritesDB(context);

        SharedPreferences prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);
        if (firstStart) {
            createTableFirstStart();
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        return new ViewHolder(view);
    }


    private void createTableFirstStart() {
        System.out.println(db.getDatabaseName());
        db.insertEmpty();
        SharedPreferences prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", false);
        editor.apply(); // or editor.commit() to save the changes
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        StoryStructure item = items.get(position);
        holder.favBtn.setBackgroundTintList(null);
        readCursorData(item, holder);
        holder.imageView.setImageResource(R.drawable.cover3);
//        holder.imageView.setImageResource(item.getImageResource());
        holder.title.setText(item.getTitle());
        holder.author.setText(item.getAuthor());
        holder.itemView.setOnClickListener(view -> {
            mItemListener.onItemClick(items.get(position));
        });
    }

    private void readCursorData(StoryStructure item, ViewHolder holder) {
        Cursor cursor = db.read_all_data(item.getId().toString());
        SQLiteDatabase db = this.db.getReadableDatabase();
        try {
            while (cursor.moveToNext()) {
                Integer column = cursor.getColumnIndex(FavoritesDB.FAVORITE_STATUS);
                String item_fav_status = cursor.getString(column);
                System.out.println("HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH item_fav_status: " + item_fav_status);
                item.setFavStatus(item_fav_status);

                if (item_fav_status != null && item_fav_status.equals("1")) {
                    holder.favBtn.setBackgroundResource(R.drawable.heart);
                } else if (item_fav_status != null && item_fav_status.equals("0")) {
                    holder.favBtn.setBackgroundResource(R.drawable.no_heart);
                }
            }
        } finally {
            if (cursor != null && cursor.isClosed())
                cursor.close();
            db.close();
        }
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

        Button favBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.cover);
            title = itemView.findViewById(R.id.title);
            author = itemView.findViewById(R.id.author);
            favBtn = itemView.findViewById(R.id.fav);

            favBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    StoryStructure item = items.get(position);

                    if (item.getFavStatus().equals("0")) {
                        item.setFavStatus("1");
                        db.insertIntoTheDatabase(item.getTitle(), item.getAuthor(),item.getImageResource(),item.getId().toString(),  item.getFavStatus());
                        favBtn.setBackgroundResource(R.drawable.heart);
                    } else {
                        item.setFavStatus("0");
                        db.remove_fav(item.getTitle());
                        favBtn.setBackgroundResource(R.drawable.no_heart);
                    }
                }
            });

        }


    }

}
