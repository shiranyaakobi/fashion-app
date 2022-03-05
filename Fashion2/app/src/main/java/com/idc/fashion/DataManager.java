package com.idc.fashion;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.util.Base64;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.idc.fashion.Model.Category;
import com.idc.fashion.Model.Item;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DataManager {
    public static Context Context;

    public static void StoreData(List<Category> categories)
    {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Context);
        SharedPreferences.Editor editor = sp.edit();
        String json = new Gson().toJson(categories);
        editor.putString("categories-data-1", json);
        editor.commit();
    }

    public static List<Category> LoadData()
    {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Context);
        String json = sp.getString("categories-data-1",null);
        if (json != null)
        {
            Type type = new TypeToken<List<Category>>(){}.getType();
            return new Gson().fromJson(json,type);
        }
        else
        {
            List<Category> categoryList = new ArrayList<>();
            categoryList.add(new Category("Shirts", new ArrayList<>()));
            categoryList.add(new Category("Pants", new ArrayList<>()));
            categoryList.add(new Category("Dresses", new ArrayList<>()));
            categoryList.add(new Category("Skirts", new ArrayList<>()));

            Item shirt = new Item(R.drawable.shirt_default, "T-shirt", "Golf", "This is the perfect T-shirt", Item.Condition.EXCELLENT, Item.Size.XL, 250);
            categoryList.get(0).addItem(shirt);
            Item pants = new Item(R.drawable.pants_default, "Yoga Pants", "Alo", "This is the perfect Yoga pants", Item.Condition.EXCELLENT, Item.Size.S, 500);
            categoryList.get(1).addItem(pants);
            Item dress = new Item(R.drawable.dress_default, "Flower Dress", "H&M", "This is the perfect Dress", Item.Condition.EXCELLENT, Item.Size.M, 450);
            categoryList.get(2).addItem(dress);
            Item skirt = new Item(R.drawable.skirt_default, "Skirt", "Golf", "This is the perfect Skirt", Item.Condition.GOOD, Item.Size.XS, 150);
            categoryList.get(3).addItem(skirt);

            return categoryList;
        }
    }

    public static String encodeTobase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        return imageEncoded;
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
