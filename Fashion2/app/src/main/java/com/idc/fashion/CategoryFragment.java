package com.idc.fashion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.idc.fashion.Adapters.CategoryAdapter;
import com.idc.fashion.Model.Category;
import java.util.List;

public class CategoryFragment extends Fragment implements CategoryAdapter.RecyclerViewClickListener {

    private RecyclerView recyclerView;
    private CategoryAdapter adapter;
    private List<Category> categoryList;

    public CategoryFragment() {
    }

    public static CategoryFragment newInstance() {
        CategoryFragment fragment = new CategoryFragment();
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
        View view = inflater.inflate(R.layout.fragment_category_list, container, false);
        recyclerView = view.findViewById(R.id.category_layout);
        categoryList = DataManager.LoadData();
        refresh(view, categoryList);
        return view;
    }

    private void refresh(View view, List<Category> categories) {
        adapter = new CategoryAdapter(categories, CategoryFragment.this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    @Override
    public void itemClicked(int position, Category category) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        Fragment itemViewFragment = ItemListFragment.newInstance(categoryList, position);
        fragmentManager.beginTransaction()
                .replace(R.id.frameLayout, itemViewFragment)
                .addToBackStack("ItemListFragment")
                .commit();
    }
}