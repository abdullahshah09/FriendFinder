package com.example.teamc.friendfinder;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.ExtractedTextRequest;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;


public class MyProfile extends Fragment {


    private ImageView imageview;
    private Button btnSelectImage;
    private Bitmap bitmap;
    private File destination = null;
    private InputStream inputStreamImg;
    private String imgPath = null;
    private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;
    String Storage_Path = "UserPhoto/";

    public Uri downlaodUrl;

    DatabaseReference dbs;

    Uri FilePath;

    FirebaseStorage storage;
//    StorageReference storageReference;


    StorageReference storageReference;
    DatabaseReference databaseReference;

    public ImageView profileImage;
    public TextView tv;
    public Button uploadBtn;
    public Button hobby1Btn, hobby2Btn, hobby3Btn;

    public static String PACKAGE_NAME;


    public Uri filePath;

    private static final int MY_CAMERA_REQUEST_CODE = 100;



    public MyProfile() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        storageReference = FirebaseStorage.getInstance().getReference();
//        databaseReference = FirebaseDatabase.getInstance().getReference(Storage_Path);



    }







    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);

        uploadBtn = (Button) view.findViewById(R.id.upload_photo);

        profileImage = view.findViewById(R.id.Profile_Picture);
        profileImage.setImageResource(R.drawable.placeholder);

//        hobby1Btn = view.findViewById(R.id.hobby_1);
//        hobby2Btn = view.findViewById(R.id.hobby_2);
//        hobby3Btn = view.findViewById(R.id.hobby_3);




        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        final Intent intent = new Intent(getActivity(), HobbyCodeActivity.class);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });


        ImageButton hobbyIB1 = (ImageButton) getActivity().findViewById(R.id.hobby_1_img_btn);
        ImageButton hobbyIB2 = (ImageButton) getActivity().findViewById(R.id.hobby_2_img_btn);
        ImageButton hobbyIB3 = (ImageButton) getActivity().findViewById(R.id.hobby_3_img_btn);


        hobbyIB1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(intent, 23 ); ;
                //just an arbritrary number as it clashes with the camera
            }
        });

        hobbyIB2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(intent, 24 ); ;
            }
        });

        hobbyIB3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(intent, 25 ); ;
            }
        });


//        hobby1Btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivityForResult(intent, 1 );
//            }
//        });
    }



    private void selectImage()
    {
        requestPermissions(new String[]{Manifest.permission.CAMERA},
                MY_CAMERA_REQUEST_CODE);
        try {

//            Storage_Path = databaseReference.toString();


            PackageManager pm = getActivity().getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getActivity().getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] options = {"Take Photo", "Choose From Gallery","Cancel"};
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                builder.setTitle("Select Option");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Take Photo")) {
                            dialog.dismiss();
                            FirebaseDatabase.getInstance().getReference();
//                            downlaodUrl.getPath();

                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, PICK_IMAGE_CAMERA);
                        } else if (options[item].equals("Choose From Gallery")) {
                            dialog.dismiss();
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
                        } else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            } else
                Toast.makeText(getActivity(), "Camera Permission error", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode == 23 && resultCode == Activity.RESULT_OK){
            String hobby = data.getStringExtra("name");
            byte b[] = data.getByteArrayExtra("picture");

            Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);

            ImageButton hobbyIB1 = (ImageButton) getActivity().findViewById(R.id.hobby_1_img_btn);

            hobbyIB1.setImageBitmap(bmp);
            Toast.makeText(getActivity(), "You selected Hobby: " + hobby, Toast.LENGTH_LONG).show();
        }

        if(requestCode == 24 && resultCode == Activity.RESULT_OK){
            String hobby = data.getStringExtra("name");
            byte b[] = data.getByteArrayExtra("picture");

            Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);

            ImageButton hobbyIB2 = (ImageButton) getActivity().findViewById(R.id.hobby_2_img_btn);

            hobbyIB2.setImageBitmap(bmp);
            Toast.makeText(getActivity(), "You selected Hobby: " + hobby, Toast.LENGTH_LONG).show();
        }

        if(requestCode == 24 && resultCode == Activity.RESULT_OK){
            String hobby = data.getStringExtra("name");
            byte b[] = data.getByteArrayExtra("picture");

            Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);

            ImageButton hobbyIB3 = (ImageButton) getActivity().findViewById(R.id.hobby_3_img_btn);

            hobbyIB3.setImageBitmap(bmp);
            Toast.makeText(getActivity(), "You selected Hobby: " + hobby, Toast.LENGTH_LONG).show();
        }



        inputStreamImg = null;
        if (requestCode == PICK_IMAGE_CAMERA) {
            try {


                FilePath = data.getData();
                Uri selectedImage = data.getData();
                bitmap = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);

                Log.e("Activity", "Pick from Camera::>>> ");

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                destination = new File(Environment.getExternalStorageDirectory() + "/" +
                        getString(R.string.app_name), "IMG_" + timeStamp + ".jpg");
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                imgPath = destination.getAbsolutePath();
                imgPath.toString();
                FilePath = downlaodUrl;
                profileImage.setImageBitmap(bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_IMAGE_GALLERY) {
            Uri selectedImage = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                Log.e("Activity", "Pick from Gallery::>>> ");

                FilePath = data.getData();
                imgPath = getRealPathFromURI(selectedImage);
                destination = new File(imgPath);
                profileImage.setImageBitmap(bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


//    public void setHobby1() {
//
//        final CharSequence[] options = {"Take Photo", "Choose From Gallery","Cancel"};
//        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
//        builder.setTitle("Select Option");
//        builder.setItems(options, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int item) {
//                if (options[item].equals("Take Photo")) {
//                    dialog.dismiss();
//
//                } else if (options[item].equals("Choose From Gallery")) {
//                    dialog.dismiss();
//
//                } else if (options[item].equals("Cancel")) {
//                    dialog.dismiss();
//                }
//            }
//        });
//        builder.show();
//
//    }



    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = getActivity().managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

}



