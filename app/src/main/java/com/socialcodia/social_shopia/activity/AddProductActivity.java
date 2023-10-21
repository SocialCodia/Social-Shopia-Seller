package com.socialcodia.social_shopia.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.socialcodia.social_shopia.R;
import com.socialcodia.social_shopia.storage.Constants;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AddProductActivity extends AppCompatActivity {

    private EditText inputProductTitle, inputProductDescription ,inputProductQuantity, inputProductPrice, inputProductDiscount, inputProductDiscountNote;
    private Button btnAddProduct;
    private ImageView ivProductImage;
    private TextView tvProductCategory;
    private SwitchCompat switchProductDiscount;
    private ImageButton btnBack;

    //Firebase
    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    DatabaseReference mRef;
    FirebaseStorage mStorage;
    StorageReference mStorageRef;
    FirebaseUser mUser;

    Uri filPath;
    String productTitle, productDescription, productQuantity, productPrice, productDiscount, productDiscountNote, productImage;
    boolean discountAvailable;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        //Firebase Init

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
        mStorage = FirebaseStorage.getInstance();
        mStorageRef = mStorage.getReference();
        mUser = mAuth.getCurrentUser();

        //UI init
        inputProductTitle = findViewById(R.id.inputProductTitle);
        inputProductDescription = findViewById(R.id.inputProductDescription);
        inputProductQuantity = findViewById(R.id.inputProductQuantity);
        inputProductPrice = findViewById(R.id.inputProductPrice);
        inputProductDiscount = findViewById(R.id.inputProductDiscount);
        inputProductDiscountNote = findViewById(R.id.inputProductDiscountNote);
        btnAddProduct = findViewById(R.id.btnAddProduct);
        tvProductCategory = findViewById(R.id.tvProductCategory);
        switchProductDiscount = findViewById(R.id.switchProductDiscount);
        ivProductImage = findViewById(R.id.ivProductImage);
        btnBack = findViewById(R.id.btnBack);

        if (mUser!=null)
        {
            userId = mUser.getUid();
        }

        tvProductCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCategoryDialogue();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ivProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });

        switchProductDiscount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    inputProductDiscount.setVisibility(View.VISIBLE);
                    inputProductDiscountNote.setVisibility(View.VISIBLE);
                }
                else
                {
                    inputProductDiscount.setVisibility(View.GONE);
                    inputProductDiscountNote.setVisibility(View.GONE);
                }
            }
        });

    }

    private void showCategoryDialogue()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Product Category").setItems(Constants.productCategory, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String category = Constants.productCategory[which];
                tvProductCategory.setText(category);
            }
        }).show();
    }

    private void chooseImage()
    {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==100 && resultCode==RESULT_OK && data!=null)
        {
            filPath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filPath);
                ivProductImage.setImageBitmap(bitmap);
            }
            catch (Exception e)
            {
                Toast.makeText(this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void validateData()
    {

         productTitle = inputProductTitle.getText().toString().trim();
         productDescription = inputProductDescription.getText().toString().trim();
         productQuantity = inputProductQuantity.getText().toString().trim();
         productPrice = inputProductPrice.getText().toString().trim();
         discountAvailable = switchProductDiscount.isChecked();

        if (productTitle.isEmpty())
        {
            inputProductTitle.setError("Enter Title");
            inputProductTitle.requestFocus();
        }
        else if (productDescription.isEmpty())
        {
            inputProductDescription.setError("Enter Description");
            inputProductDescription.requestFocus();
        }
        else if (productQuantity.isEmpty())
        {
            inputProductQuantity.setError("Enter Quantity");
            inputProductQuantity.requestFocus();
        }
        else if (productPrice.isEmpty())
        {
            inputProductPrice.setError("Enter Price");
            inputProductPrice.requestFocus();
        }
        else if (discountAvailable)
        {
            productDiscount = inputProductDiscount.getText().toString().trim();
            productDiscountNote = inputProductDiscountNote.getText().toString().trim();
            if (productDiscount.isEmpty())
            {

                inputProductDiscount.setError("Enter Discount");
                inputProductDiscount.requestFocus();
            }
            else
            {
                productDiscount = "0";
                productDiscountNote = "";
            }
        }
        else if (filPath == null)
        {
            Toast.makeText(this, "Select Product Image", Toast.LENGTH_SHORT).show();
        }
        else
        {
            uploadImage();
        }
    }

    private void uploadImage()
    {
        btnAddProduct.setEnabled(false);
        String filePathAndName = "product_image/SocialShopia_"+System.currentTimeMillis();
        mStorageRef.child(filePathAndName).putFile(filPath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful())
                {
                    task.getResult().getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageDownloadUrl = uri.toString();
                            addProduct(imageDownloadUrl);
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                btnAddProduct.setEnabled(true);
                Toast.makeText(AddProductActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearData()
    {
        inputProductTitle.setText("");
        inputProductDescription.setText("");
        inputProductQuantity.setText("");
        inputProductPrice.setText("");
        inputProductDiscount.setText("");
        inputProductDiscountNote.setText("");
        tvProductCategory.setText("");
        filPath=null;
        try {
            Picasso.get().load(R.drawable.store).into(ivProductImage);
        }
        catch (Exception e)
        {
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void addProduct(String imageDownloadUrl)
    {
        String productId = mRef.push().getKey();
        HashMap<String , Object> map = new HashMap<>();
        map.put(Constants.PRODUCT_TITLE,productTitle);
        map.put(Constants.PRODUCT_DESCRIPTION,productDescription);
        map.put(Constants.PRODUCT_QUANTITY,productQuantity);
        map.put(Constants.PRODUCT_PRICE,productPrice);
        map.put(Constants.PRODUCT_DISCOUNT,productDiscount);
        map.put(Constants.PRODUCT_DISCOUNT_NOTE,productDiscountNote);
        map.put(Constants.PRODUCT_IMAGE,imageDownloadUrl);
        map.put(Constants.PRODUCT_ID,productId);
        map.put(Constants.SHOP_ID,userId);
        map.put(Constants.PRODUCT_DISCOUNT_AVAILABLE,String.valueOf(discountAvailable));
        map.put(Constants.TIMESTAMP,String.valueOf(System.currentTimeMillis()));
        mRef.child(Constants.PRODUCTS).child(productId).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                btnAddProduct.setEnabled(true);
                clearData();
                Toast.makeText(AddProductActivity.this, "Product Added Successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
