package com.idc.fashion;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.idc.fashion.Model.Category;
import com.idc.fashion.Model.Item;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddItemFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    public interface AddItemListener {
        void itemCreated(Item item);
    }

    static final int REQUEST_TAKE_PHOTO = 1;
    private static AddItemFragment.AddItemListener addItemListener;

    private Category category;
    private ImageView itemAvatar;
    private EditText itemName, itemBrand, itemDesc, itemPrice;
    private RadioGroup radioGroup;
    private RadioButton radioExcellent, radioGood, radioBad;
    private Button addButton;
    private Bitmap imageBitmap;
    MainActivity activity;
    Item.Size itemSize;

    String[] size = {"XXL", "XL", "L", "M", "S", "XS"};

    public AddItemFragment() {
    }

    public static AddItemFragment newInstance(Category category, AddItemListener addItemListener) {
        AddItemFragment fragment = new AddItemFragment();
        fragment.setListener(addItemListener);
        fragment.setCategory(category);
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
        View view = inflater.inflate(R.layout.fragment_add_item, container, false);

        itemAvatar = view.findViewById(R.id.item_avatar);
        itemName = view.findViewById(R.id.item_name_editText);
        itemBrand = view.findViewById(R.id.item_brand_editText);
        itemDesc = view.findViewById(R.id.item_desc_editText);
        itemPrice = view.findViewById(R.id.item_price_editText);
        activity = (MainActivity) getActivity();
        Spinner spin = view.findViewById(R.id.size_spinner);
        spin.setOnItemSelectedListener(AddItemFragment.this);
        ArrayAdapter aa = new ArrayAdapter(getActivity(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, size);
        aa.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spin.setAdapter(aa);
        itemAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        radioGroup = view.findViewById(R.id.item_condition_group);
        radioExcellent = view.findViewById(R.id.radio_excellent);
        radioGood = view.findViewById(R.id.radio_good);
        radioBad = view.findViewById(R.id.radio_bad);

        addButton = view.findViewById(R.id.add_button);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemToCategory();
            }
        });

        return view;
    }

    private void addItemToCategory() {
        String name = itemName.getText().toString();
        String brand = itemBrand.getText().toString();
        String desc = itemDesc.getText().toString();
        int price = 0;
        if (!itemPrice.getText().toString().isEmpty()) {
            price = Integer.parseInt(itemPrice.getText().toString());
        }
        int selectedId = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = getActivity().findViewById(selectedId);
        Item.Condition condition = Item.Condition.BAD;
        if (radioButton != null) {
            String choice = radioButton.getText().toString();
            switch (choice) {
                case "Excellent":
                    condition = Item.Condition.EXCELLENT;
                    break;
                case "Good":
                    condition = Item.Condition.GOOD;
                    break;
                case "Bad":
                default:
                    condition = Item.Condition.BAD;
                    break;
            }
        }
        int defaultImage = 0;
        switch (category.getName()) {
            case "Shirts":
                defaultImage = R.drawable.shirt_default;
                break;
            case "Pants":
                defaultImage = R.drawable.pants_default;
                break;
            case "Dresses":
                defaultImage = R.drawable.dress_default;
                break;
            case "Skirts":
                defaultImage = R.drawable.skirt_default;
                break;
        }
        if (!name.isEmpty() && !brand.isEmpty() && price != 0 && condition != null && itemSize != null) {
            Item itemToAdd = new Item(defaultImage, name, brand, desc, condition, itemSize, price);
            if (imageBitmap != null) {
                itemToAdd.setEncodedBitmap(DataManager.encodeTobase64(imageBitmap));
            }

            addItemListener.itemCreated(itemToAdd);
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            Fragment itemViewFragment = new ItemListFragment(category);
            fragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, itemViewFragment)
                    .addToBackStack("ItemListFragment")
                    .commit();
        } else {
            Toast.makeText(activity, "Need to fill all fields", Toast.LENGTH_SHORT)
                    .show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            // Show the full sized image.
            imageBitmap = setFullImageFromFilePath(activity.getCurrentPhotoPath(), itemAvatar);

        } else {
            Toast.makeText(activity, "Image Capture Failed", Toast.LENGTH_SHORT)
                    .show();
        }
    }


    public void setCategory(Category category) {
        this.category = category;
    }


    private void dispatchTakePictureIntent() {
        PackageManager packageManager = getContext().getPackageManager();
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA) == false) {
            Toast.makeText(getActivity(), "This device does not have a camera.", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create the File where the photo should go
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            // Error occurred while creating the File
            Toast toast = Toast.makeText(activity, "There was a problem saving the photo...", Toast.LENGTH_SHORT);
            toast.show();
        }
        // Continue only if the File was successfully created
        if (photoFile != null) {
            Uri fileUri = FileProvider.getUriForFile(activity,
                    "com.idc.fashion.fileprovider",
                    photoFile);
            activity.setCapturedImageURI(fileUri);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                    activity.getCapturedImageURI());
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        activity.setCurrentPhotoPath(image.getAbsolutePath());
        return image;
    }


    private Bitmap setFullImageFromFilePath(String imagePath, ImageView imageView) {
        // Get the dimensions of the View
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, bmOptions);
        itemAvatar.setImageBitmap(bitmap);
        itemAvatar.getLayoutParams().width = 300;
        itemAvatar.getLayoutParams().height = 300;

        return bitmap;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (size[position]) {
            case "XXL":
                itemSize = Item.Size.XXL;
                break;
            case "XL":
                itemSize = Item.Size.XL;
                break;
            case "L":
                itemSize = Item.Size.L;
                break;
            case "M":
                itemSize = Item.Size.M;
                break;
            case "S":
                itemSize = Item.Size.S;
                break;
            case "XS":
                itemSize = Item.Size.XS;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private void setListener(AddItemListener listener) {
        addItemListener = listener;
    }
}
