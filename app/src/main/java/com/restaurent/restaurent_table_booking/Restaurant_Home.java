package com.restaurent.restaurent_table_booking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.common.api.internal.BaseImplementation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import io.paperdb.Paper;

public class Restaurant_Home extends AppCompatActivity {
    String exist="yes";
    CardView restaurant_home_card_stock,restaurant_home_card_add_food;
    RelativeLayout restaurant_home_rel_empty;
    Button restaurant_home_stock,restaurant_home_add_food;
    Button restaurant_home_food_choose_photo,restaurant_home_food_submit;
    Button restaurant_home_update_food_id,restaurant_home_update_food_name,restaurant_home_update;
    EditText restaurant_home_update_food_price,restaurant_home_update_food_stock;
    LinearLayout restaurant_home_lin1,restaurant_home_lin4,restaurant_home_lin3;
    ScrollView restaurant_home_lin2;
    TextView restaurant_home_name;
    ImageView restaurant_home_food_image;
    EditText restaurant_home_food_id,restaurant_home_food_name,restaurant_home_food_price,restaurant_home_food_qty;
    Uri rest_Image_Uri;
    StorageReference storageReference;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference,storage_databaseReference,update_databaseReference;
    String root,hotel_category="Select Category",url,_Stock,_Price,key;
    String[] main_root;
    ArrayList<String> hotel_cat;
    ProgressDialog progressDialog;
    Spinner restaurant_home_spinner;
    SearchView restaurant_home_search;
    RecyclerView restaurant_home_recyclerview;
    FirebaseRecyclerAdapter<Reg,RH> firebaseRecyclerAdapter,firebaseRecyclerAdapter1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_home);
        FirebaseApp.initializeApp(this);
        try {
            Picasso.setSingletonInstance(new Picasso.Builder(this).build());
        } catch (Exception e) {
            e.printStackTrace();
        }
        root=getIntent().getStringExtra("mail");
        main_root=root.split("\\.");
        progressDialog=new ProgressDialog(Restaurant_Home.this);
        hotel_cat=new ArrayList<>();
        hotel_cat.add("Select Category");
        hotel_cat.add("Vegetarian");
        hotel_cat.add("Non-vegetarian");

        restaurant_home_search=findViewById(R.id.restaurant_home_search);
        restaurant_home_rel_empty=findViewById(R.id.restaurant_home_rel_empty);
        restaurant_home_recyclerview=findViewById(R.id.restaurant_home_recyclerview);
        restaurant_home_recyclerview.setHasFixedSize(true);
        restaurant_home_recyclerview.setLayoutManager(new LinearLayoutManager(this));

        restaurant_home_spinner=findViewById(R.id.restaurant_home_spinner);
        restaurant_home_food_id=findViewById(R.id.restaurant_home_food_id);
        restaurant_home_food_name=findViewById(R.id.restaurant_home_food_name);
        restaurant_home_food_price=findViewById(R.id.restaurant_home_food_price);
        restaurant_home_food_qty=findViewById(R.id.restaurant_home_food_qty);
        restaurant_home_food_choose_photo=findViewById(R.id.restaurant_home_food_choose_photo);
        restaurant_home_food_submit=findViewById(R.id.restaurant_home_food_submit);
        restaurant_home_food_image=findViewById(R.id.restaurant_home_food_image);

        restaurant_home_name=findViewById(R.id.restaurant_home_name);
        restaurant_home_card_stock=findViewById(R.id.restaurant_home_card_stock);
        restaurant_home_card_add_food=findViewById(R.id.restaurant_home_card_add_food);
        restaurant_home_stock=findViewById(R.id.restaurant_home_stock);
        restaurant_home_add_food=findViewById(R.id.restaurant_home_add_food);

        restaurant_home_lin1=findViewById(R.id.restaurant_home_lin1);
        restaurant_home_lin2=findViewById(R.id.restaurant_home_lin2);
        restaurant_home_lin3=findViewById(R.id.restaurant_home_lin3);
        restaurant_home_lin4=findViewById(R.id.restaurant_home_lin4);

        restaurant_home_update_food_id=findViewById(R.id.restaurant_home_update_food_id);
        restaurant_home_update_food_name=findViewById(R.id.restaurant_home_update_food_name);
        restaurant_home_update_food_price=findViewById(R.id.restaurant_home_update_food_price);
        restaurant_home_update_food_stock=findViewById(R.id.restaurant_home_update_food_stock);
        restaurant_home_update=findViewById(R.id.restaurant_home_update);

        restaurant_home_spinner.setAdapter(new ArrayAdapter<>(Restaurant_Home.this,android.R.layout.simple_spinner_dropdown_item,hotel_cat));
        restaurant_home_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hotel_category=parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        restaurant_home_lin1.setVisibility(View.VISIBLE);
        restaurant_home_card_add_food.setCardBackgroundColor(getResources().getColor(R.color.green));
        restaurant_home_add_food.setBackgroundColor(getResources().getColor(R.color.black));
        restaurant_home_add_food.setTextColor(getResources().getColor(R.color.white));

        firebaseDatabase = FirebaseDatabase.getInstance();
        update_databaseReference=FirebaseDatabase.getInstance().getReference().child("Stock").child(main_root[0]);
        storage_databaseReference=firebaseDatabase.getReference().child("Stock").child(main_root[0]).child(restaurant_home_food_id.getText().toString());
        storage_databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    exist="nil";
                    restaurant_home_lin1.setVisibility(View.INVISIBLE);
                    restaurant_home_rel_empty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference=firebaseDatabase.getReference().child("Restaurant Registration Details").child(main_root[0]);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String n="Hi, "+snapshot.child("_Restaurant_Name").getValue().toString()+"!";
                restaurant_home_name.setText(n);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        BottomNavigationView bottomNavigationView=findViewById(R.id.restaurant_home_navigat);
        bottomNavigationView.setSelectedItemId(R.id.rest_menu_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.rest_menu_home:
                        return true;
                    case R.id.rest_menu_booked:
                        Intent i1=new Intent(getApplicationContext(),Restaurant_Booked.class);
                        i1.putExtra("mail",root);
                        startActivity(i1);
                        finish();
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.rest_menu_payment:
                        Intent i2=new Intent(getApplicationContext(),Restaurant_Payment.class);
                        i2.putExtra("mail",root);
                        startActivity(i2);
                        finish();
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.rest_menu_time_slot:

                        Intent i3=new Intent(getApplicationContext(),Restaurant_Time_Slot.class);
                        i3.putExtra("mail",root);
                        startActivity(i3);
                        finish();
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.rest_menu_profile:
                        Intent i4=new Intent(getApplicationContext(),Restaurant_Profile.class);
                        i4.putExtra("mail",root);
                        startActivity(i4);
                        finish();
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
        restaurant_home_food_choose_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(!validatefoodid()|!validatefoodname()|!validatefoodprice()|!validatefoodqty()|!validateCategory())
                        return;
                checkAndroidVersion();
            }
        });
        restaurant_home_food_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setTitle("Please Wait...");
                progressDialog.setMessage("Adding food details to the database...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                UploadFile();
            }
        });
        restaurant_home_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isStockChanged() || isPriceChanged()){
                    AlertDialog.Builder builder=new AlertDialog.Builder(Restaurant_Home.this);
                    builder.setTitle("Successfully Updated!").setMessage("Successfully Data has been Updated")
                            .setPositiveButton(
                                    "Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            restaurant_home_lin1.setVisibility(View.VISIBLE);
                                            restaurant_home_lin2.setVisibility(View.INVISIBLE);
                                            restaurant_home_lin3.setVisibility(View.VISIBLE);
                                            restaurant_home_lin4.setVisibility(View.INVISIBLE);
                                        }
                                    });
                    AlertDialog alertDialog=builder.create();
                    alertDialog.show();
                }
                else{
                    Toasty.success(getApplicationContext(),"Data is same and cannot be updated!", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder=new AlertDialog.Builder(Restaurant_Home.this);
                    builder.setTitle("Data is same!").setMessage("Data is same and cannot be updated!!")
                            .setPositiveButton(
                                    "Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            restaurant_home_lin1.setVisibility(View.VISIBLE);
                                            restaurant_home_lin2.setVisibility(View.INVISIBLE);
                                            restaurant_home_lin3.setVisibility(View.VISIBLE);
                                            restaurant_home_lin4.setVisibility(View.INVISIBLE);
                                        }
                                    });
                    AlertDialog alertDialog=builder.create();
                    alertDialog.show();
                }
            }
        });

    }

    private boolean isStockChanged() {

        if(!_Stock.equals(restaurant_home_update_food_stock.getEditableText().toString().trim())){
            update_databaseReference.child(key).child("_Food_Quantity").setValue(restaurant_home_update_food_stock.getEditableText().toString().trim());
            return true;
        }
        else{
            return false;
        }
    }
    private boolean isPriceChanged() {
        if(!_Price.equals(restaurant_home_update_food_price.getEditableText().toString().trim())){
            update_databaseReference.child(key).child("_Food_Price").setValue(restaurant_home_update_food_price.getEditableText().toString().trim());
            return true;
        }
        else{
            return false;
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        if(restaurant_home_search!=null)
            restaurant_home_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    firebaseSearch(query);
                    return false;
                }



                @Override
                public boolean onQueryTextChange(String newText) {
                    firebaseSearch(newText);
                    return false;
                }
            });
        FirebaseRecyclerOptions<Reg> options=new FirebaseRecyclerOptions.Builder<Reg>().setQuery(storage_databaseReference,Reg.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Reg, RH>(options) {
            @Override
            protected void onBindViewHolder(@NonNull RH holder, final int position, @NonNull final Reg model) {
                holder.food_id.setText(model.get_Food_Id());
                holder.food_name.setText(model.get_Food_Name());
                holder.food_price.setText(model.get_Food_Price());
                holder.food_qty.setText(model.get_Food_Quantity());
                holder.food_cat.setText(model.get_Food_Category());
                Picasso.get().load(model.get_Food_Image_Url()).into(holder.food_image);
                holder.food_option.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                        popupMenu.getMenu().add("Update").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                restaurant_home_lin1.setVisibility(View.INVISIBLE);
                                restaurant_home_lin2.setVisibility(View.INVISIBLE);
                                restaurant_home_lin3.setVisibility(View.INVISIBLE);
                                restaurant_home_lin4.setVisibility(View.VISIBLE);

                                key=getRef(position).getKey();
                                _Stock=model.get_Food_Quantity();
                                _Price=model.get_Food_Price();

                                restaurant_home_update_food_id.setText(model.get_Food_Id());
                                restaurant_home_update_food_name.setText(model.get_Food_Name());
                                restaurant_home_update_food_price.setText(model.get_Food_Price());
                                restaurant_home_update_food_stock.setText(model.get_Food_Quantity());
                                return false;
                            }
                        });
                        popupMenu.show();
                    }
                });
            }

            @NonNull
            @Override
            public RH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rest_home_food_card, parent, false);
                return new RH(view);
            }
        };
        firebaseRecyclerAdapter.startListening();
        firebaseRecyclerAdapter.notifyDataSetChanged();
        restaurant_home_recyclerview.setAdapter(firebaseRecyclerAdapter);

    }
    public static class RH extends RecyclerView.ViewHolder {

        public TextView food_id, food_name, food_price, food_qty, food_cat;
        public ImageView food_image,food_option;

        public RH(@NonNull View itemView) {
            super(itemView);
            food_id = itemView.findViewById(R.id.rest_home_food_id);
            food_name = itemView.findViewById(R.id.rest_home_food_name);
            food_price = itemView.findViewById(R.id.rest_home_food_price);
            food_qty = itemView.findViewById(R.id.rest_home_food_qty);
            food_cat = itemView.findViewById(R.id.rest_home_food_category);
            food_image = itemView.findViewById(R.id.rest_home_food_image);
            food_option = itemView.findViewById(R.id.rest_home_food_option);
        }
    }
    private void firebaseSearch(String query) {
        String one=query.toLowerCase();
        String two=query.toUpperCase();
        Query q=storage_databaseReference.orderByChild("_Food_Name").startAt(two).endAt(one+"\uf0ff");
        FirebaseRecyclerOptions<Reg> options=new FirebaseRecyclerOptions.Builder<Reg>().setQuery(q,Reg.class).build();
        firebaseRecyclerAdapter1 = new FirebaseRecyclerAdapter<Reg, RH>(options) {
            @Override
            protected void onBindViewHolder(@NonNull RH holder, int position, @NonNull final Reg model) {
                holder.food_id.setText(model.get_Food_Id());
                holder.food_name.setText(model.get_Food_Name());
                holder.food_price.setText(model.get_Food_Price());
                holder.food_qty.setText(model.get_Food_Quantity());
                holder.food_cat.setText(model.get_Food_Category());
                Picasso.get().load(model.get_Food_Image_Url()).into(holder.food_image);
                holder.food_option.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                        popupMenu.getMenu().add("Update").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                restaurant_home_lin1.setVisibility(View.INVISIBLE);
                                restaurant_home_lin2.setVisibility(View.INVISIBLE);
                                restaurant_home_lin3.setVisibility(View.INVISIBLE);
                                restaurant_home_lin4.setVisibility(View.VISIBLE);

                                restaurant_home_update_food_id.setText(model.get_Food_Id());
                                restaurant_home_update_food_name.setText(model.get_Food_Name());
                                restaurant_home_update_food_price.setText(model.get_Food_Price());
                                restaurant_home_update_food_stock.setText(model.get_Food_Quantity());
                                return false;
                            }
                        });
                        popupMenu.show();
                    }
                });
            }

            @NonNull
            @Override
            public RH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rest_home_food_card, parent, false);
                return new RH(view);
            }
        };
        firebaseRecyclerAdapter1.startListening();
        firebaseRecyclerAdapter1.notifyDataSetChanged();
        restaurant_home_recyclerview.setAdapter(firebaseRecyclerAdapter1);
    }
    public void checkAndroidVersion(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            try{
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},555);
            }catch (Exception e){
            }
        }
        else{
            pickimage();
        }
    }
    public void pickimage(){
        CropImage.startPickImageActivity(this);
    }
    private void croprequest(Uri mImageuri){
        CropImage.activity(mImageuri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true).start(this);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==555 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            pickimage();
        }
        else{
            checkAndroidVersion();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            rest_Image_Uri = CropImage.getPickImageResultUri(this, data);
            croprequest(rest_Image_Uri);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),resultUri);
                    restaurant_home_food_image.setImageBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
    private String getFileExt(Uri uri){
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void UploadFile() {
        if (rest_Image_Uri!=null){
            storage_databaseReference.orderByChild("_Food_Id").equalTo(restaurant_home_food_id.getText().toString().trim()).addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        progressDialog.dismiss();
                        restaurant_home_food_id.setError("Already Exists");
                        restaurant_home_food_id.setText("");
                        AlertDialog.Builder builder=new AlertDialog.Builder(Restaurant_Home.this);
                        builder.setTitle("Already Exists!").setMessage(restaurant_home_food_id.getText().toString().trim()+" Food Id Already Exists")
                                .setNegativeButton("Ok",null).setCancelable(false);
                        AlertDialog alertDialog=builder.create();
                        alertDialog.show();
                        restaurant_home_food_id.requestFocus();
                    }
                    else {
                        storageReference= FirebaseStorage.getInstance().getReference().child(main_root[0]+restaurant_home_food_id.getText().toString()+"."+getFileExt(rest_Image_Uri));
                        storageReference.putFile(rest_Image_Uri)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                progressDialog.dismiss();
                                               url=String.valueOf(uri);
                                                AlertDialog.Builder builder=new AlertDialog.Builder(Restaurant_Home.this);
                                                builder.setTitle("Successfully Added!").setMessage("Successfully Added the "+restaurant_home_food_id.getText().toString().trim()+" Id Details!")
                                                        .setPositiveButton(
                                                                "Ok", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        String fid=restaurant_home_food_id.getText().toString().trim();
                                                                        String fn=restaurant_home_food_name.getText().toString().trim();
                                                                        String fp=restaurant_home_food_price.getText().toString().trim();
                                                                        String fq=restaurant_home_food_qty.getText().toString().trim();
                                                                        Reg bks=new Reg(fid,fn,hotel_category,fp,fq,url);
                                                                        storage_databaseReference.child(fid).setValue(bks);
                                                                        restaurant_home_food_id.setText("");
                                                                        restaurant_home_food_name.setText("");
                                                                        restaurant_home_food_price.setText("");
                                                                        restaurant_home_food_qty.setText("");
                                                                        restaurant_home_food_image.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_background));
                                                                        restaurant_home_lin1.setVisibility(View.VISIBLE);
                                                                        restaurant_home_lin2.setVisibility(View.INVISIBLE);
                                                                        restaurant_home_card_add_food.setCardBackgroundColor(getResources().getColor(R.color.green));
                                                                        restaurant_home_add_food.setBackgroundColor(getResources().getColor(R.color.black));
                                                                        restaurant_home_add_food.setTextColor(getResources().getColor(R.color.white));
                                                                        restaurant_home_card_stock.setCardBackgroundColor(getResources().getColor(R.color.black));
                                                                        restaurant_home_stock.setBackgroundColor(getResources().getColor(R.color.qq));
                                                                        restaurant_home_stock.setTextColor(getResources().getColor(R.color.black));
                                                                    }
                                                                });
                                                AlertDialog alertDialog=builder.create();
                                                alertDialog.show();
                                            }
                                        });

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Toasty.error(getApplicationContext(),e.getMessage(),Toasty.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else {
            progressDialog.dismiss();
            Toasty.error(getApplicationContext(),"No File Selected",Toast.LENGTH_SHORT).show();
        }
    }
    private Boolean validatefoodid(){
        String val = restaurant_home_food_id.getText().toString().trim();
        if (val.isEmpty()) {
            restaurant_home_food_id.setError("Fields cannot be empty");
            return false;
        } else {
            restaurant_home_food_id.setError(null);
            return true;
        }
    }
    private Boolean validatefoodname(){
        String val = restaurant_home_food_name.getText().toString().trim();
        if (val.isEmpty()) {
            restaurant_home_food_name.setError("Fields cannot be empty");
            return false;
        } else {
            restaurant_home_food_name.setError(null);
            return true;
        }
    }
    private Boolean validatefoodprice(){
        String val = restaurant_home_food_price.getText().toString().trim();
        if (val.isEmpty()) {
            restaurant_home_food_price.setError("Fields cannot be empty");
            return false;
        } else {
            restaurant_home_food_price.setError(null);
            return true;
        }
    }
    private Boolean validatefoodqty(){
        String val = restaurant_home_food_qty.getText().toString().trim();
        if (val.isEmpty()) {
            restaurant_home_food_qty.setError("Fields cannot be empty");
            return false;
        } else {
            restaurant_home_food_qty.setError(null);
            return true;
        }
    }
    private Boolean validateCategory() {
        if (hotel_category.equals("Select Category")) {
            Toasty.error(getApplicationContext(),"Select Category",Toasty.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder=new AlertDialog.Builder(Restaurant_Home.this);
        builder.setTitle("Really Logout?").setMessage("Are you sure?")
                .setPositiveButton(
                        "Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(true) {
                                    Paper.book().destroy();
                                    Intent intent = new Intent(Restaurant_Home.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else{
                                    Intent intent = new Intent(Restaurant_Home.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                            }
                        }
                )
                .setNegativeButton("Cancel",null).setCancelable(false);
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }
    public void backpress(View view){
        AlertDialog.Builder builder=new AlertDialog.Builder(Restaurant_Home.this);
        builder.setTitle("Really Logout?").setMessage("Are you sure?")
                .setPositiveButton(
                        "Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(true) {
                                    Paper.book().destroy();
                                    Intent intent = new Intent(Restaurant_Home.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else{
                                    Intent intent = new Intent(Restaurant_Home.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                            }
                        }
                )
                .setNegativeButton("Cancel",null).setCancelable(false);
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }
    public void add_food(View view){
        restaurant_home_lin2.setVisibility(View.VISIBLE);
        restaurant_home_lin1.setVisibility(View.INVISIBLE);
        restaurant_home_card_stock.setCardBackgroundColor(getResources().getColor(R.color.green));
        restaurant_home_stock.setBackgroundColor(getResources().getColor(R.color.black));
        restaurant_home_stock.setTextColor(getResources().getColor(R.color.white));
        restaurant_home_card_add_food.setCardBackgroundColor(getResources().getColor(R.color.black));
        restaurant_home_add_food.setBackgroundColor(getResources().getColor(R.color.qq));
        restaurant_home_add_food.setTextColor(getResources().getColor(R.color.black));
    }
    public void stock(View view){
        if(exist.equals("yes")){
            restaurant_home_lin1.setVisibility(View.VISIBLE);
            restaurant_home_rel_empty.setVisibility(View.INVISIBLE);
            restaurant_home_lin2.setVisibility(View.INVISIBLE);
            restaurant_home_card_add_food.setCardBackgroundColor(getResources().getColor(R.color.green));
            restaurant_home_add_food.setBackgroundColor(getResources().getColor(R.color.black));
            restaurant_home_add_food.setTextColor(getResources().getColor(R.color.white));
            restaurant_home_card_stock.setCardBackgroundColor(getResources().getColor(R.color.black));
            restaurant_home_stock.setBackgroundColor(getResources().getColor(R.color.qq));
            restaurant_home_stock.setTextColor(getResources().getColor(R.color.black));
        }
        else{
            restaurant_home_lin1.setVisibility(View.INVISIBLE);
            restaurant_home_rel_empty.setVisibility(View.VISIBLE);
            restaurant_home_lin2.setVisibility(View.INVISIBLE);
            restaurant_home_card_add_food.setCardBackgroundColor(getResources().getColor(R.color.green));
            restaurant_home_add_food.setBackgroundColor(getResources().getColor(R.color.black));
            restaurant_home_add_food.setTextColor(getResources().getColor(R.color.white));
            restaurant_home_card_stock.setCardBackgroundColor(getResources().getColor(R.color.black));
            restaurant_home_stock.setBackgroundColor(getResources().getColor(R.color.qq));
            restaurant_home_stock.setTextColor(getResources().getColor(R.color.black));
        }

    }
}