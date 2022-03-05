package com.idc.fashion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.idc.fashion.Adapters.ItemListAdapter;
import com.idc.fashion.Model.Category;
import com.idc.fashion.Model.Item;
import java.util.List;

public class ItemListFragment extends Fragment implements ItemListAdapter.RecyclerViewClickListener,
        ShowItemFragment.ItemClickListener,
        AddItemFragment.AddItemListener {

    private RecyclerView recyclerView;
    private ItemListAdapter adapter;
    private static List<Category> categoryList;
    private Category category;
    FloatingActionButton floatingActionButton;

    public ItemListFragment(Category category) {
        setCategory(category);
    }

    public static ItemListFragment newInstance(List<Category> list, int position) {
        categoryList = list;
        ItemListFragment fragment = new ItemListFragment(categoryList.get(position));
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
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        floatingActionButton = view.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                Fragment addItemFragment = AddItemFragment.newInstance(category, ItemListFragment.this);
                fragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, addItemFragment)
                        .addToBackStack("ItemListFragment")
                        .commit();
            }
        });
        recyclerView = view.findViewById(R.id.item_list_layout);
        refresh(view, category);
        return view;
    }

    private void refresh(View view, Category category) {
        adapter = new ItemListAdapter(category.getItems(), ItemListFragment.this);
        recyclerView.setAdapter(adapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gridLayoutManager.setSpanCount(2);
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    @Override
    public void itemClicked(int position) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        Fragment showItemFragment = ShowItemFragment.newInstance(position, category, ItemListFragment.this);
        fragmentManager.beginTransaction()
                .replace(R.id.frameLayout, showItemFragment)
                .addToBackStack("ShowItemFragment")
                .commit();
    }

    @Override
    public void deleteClicked(int position) {
        category.deleteItem(position);
        DataManager.StoreData(categoryList);
    }

    @Override
    public void itemCreated(Item item) {
        category.addItem(item);
        DataManager.StoreData(categoryList);
    }
}
