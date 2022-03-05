package com.idc.fashion.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.idc.fashion.Model.Category;
import com.idc.fashion.R;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private List<Category> categories;
    private static RecyclerViewClickListener itemListener;



    public interface RecyclerViewClickListener {
        void itemClicked(int position, Category category);
    }

    public CategoryAdapter(List<Category> categories, RecyclerViewClickListener listener) {
        this.categories = categories;
        this.itemListener = listener;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View categoryView = inflater.inflate(R.layout.category_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(categoryView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       Category categoryIndex = categories.get(position);

       holder.categoryName.setText(categoryIndex.getName());
       holder.cardViewCategories.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               itemListener.itemClicked(position, categoryIndex);
           }
       });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView categoryName;
        CardView cardViewCategories;

        public ViewHolder(View itemView) {
            super(itemView);

            categoryName = itemView.findViewById(R.id.category_name);
            cardViewCategories = itemView.findViewById(R.id.card);
        }

    }
}
