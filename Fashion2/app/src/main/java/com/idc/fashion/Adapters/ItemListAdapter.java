package com.idc.fashion.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.idc.fashion.DataManager;
import com.idc.fashion.MainActivity;
import com.idc.fashion.Model.Item;
import com.idc.fashion.R;
import java.util.List;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ViewHolder> {
    private List<Item> items;
    private static RecyclerViewClickListener itemListener;

    public interface RecyclerViewClickListener {
        void itemClicked(int position);
    }

    public ItemListAdapter(List<Item> items, RecyclerViewClickListener listener) {
        this.items = items;
        this.itemListener = listener;
    }

    @NonNull
    @Override
    public ItemListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_card, parent, false);
        ItemListAdapter.ViewHolder viewHolder = new ItemListAdapter.ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = items.get(position);
        if(item.getEncodedBitmap() != null) {
            holder.itemPhoto.setImageBitmap(DataManager.decodeBase64(item.getEncodedBitmap()));
        } else {
            holder.itemPhoto.setBackgroundResource(item.getAvatar());
        }
        holder.itemPrice.setText(Integer.toString(item.getPrice()) + "₪");
        holder.cardViewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemListener.itemClicked(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView itemPhoto;
        TextView itemPrice;
        CardView cardViewItem;

        public ViewHolder(View itemView) {
            super(itemView);

            itemPhoto = itemView.findViewById(R.id.item_photo);
            itemPrice = itemView.findViewById(R.id.item_price);
            cardViewItem = itemView.findViewById(R.id.items_card);
        }

    }
}
