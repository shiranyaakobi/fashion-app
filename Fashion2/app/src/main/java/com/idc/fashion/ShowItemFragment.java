package com.idc.fashion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.idc.fashion.Model.Category;
import com.idc.fashion.Model.Item;

public class ShowItemFragment extends Fragment {

    Category category;
    Item item;
    int position;
    private static ShowItemFragment.ItemClickListener clickListener;

    public interface ItemClickListener {
        void deleteClicked(int position);
    }

    public static ShowItemFragment newInstance(int position, Category category, ItemClickListener listener) {
        ShowItemFragment fragment = new ShowItemFragment();
        fragment.setListener(listener);
        fragment.setCategory(category);
        fragment.setItem(category.getItems().get(position));
        fragment.setPosition(position);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_show_item, container, false);
        ImageView avatar = view.findViewById(R.id.item_pic);
        TextView name = view.findViewById(R.id.item_name);
        TextView brand = view.findViewById(R.id.item_brand);
        TextView desc = view.findViewById(R.id.item_desc);
        TextView price = view.findViewById(R.id.item_price);
        TextView condition = view.findViewById(R.id.item_condition);
        TextView size = view.findViewById(R.id.item_size);
        ImageView delete_button = view.findViewById(R.id.btn_delete);

        if(item != null) {
            if(item.getEncodedBitmap() != null) {
                avatar.setImageBitmap(DataManager.decodeBase64(item.getEncodedBitmap()));
            } else {
                avatar.setBackgroundResource(item.getAvatar());
            }
            name.setText(item.getName());
            brand.setText(item.getBrand());
            desc.setText(item.getDescription());
            price.setText(Integer.toString(item.getPrice()) + "₪");
            size.setText(item.getSize().toString());
            condition.setText(condition.getText() + item.getCondition().toString());
        }

        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.deleteClicked(position);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                Fragment itemViewFragment = new ItemListFragment(category);
                fragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, itemViewFragment)
                        .addToBackStack("ItemListFragment")
                        .commit();
            }
        });
        return view;
    }

    private void setItem(Item item) {
        this.item = item;
    }

    private void setCategory(Category category) {
        this.category = category;
    }

    private void setPosition(int position) {
        this.position = position;
    }

    private void setListener(ItemClickListener listener) {
        clickListener = listener;
    }
}
