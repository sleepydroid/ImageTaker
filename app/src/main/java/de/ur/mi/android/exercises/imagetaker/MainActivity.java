package de.ur.mi.android.exercises.imagetaker;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity  {

    private Button takeImage;
    private Button fromGallery;
    private ImageView image;
    private static final int REQUEST_TAKE_PHOTO = 1;
    private String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupUI();
    }

    private void setupUI(){
        takeImage = findViewById(R.id.takeImage);
        fromGallery = findViewById(R.id.fromGallery);
        image = findViewById(R.id.imageView);

        takeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("HEY","TAKE IMAGE");
            }
        });

        fromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Log.d("HEY","FROM GALLERY");
            }
        });
    }

    public void takePicture(int requestCode){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getPackageManager()) != null){
            File photoFile = null;
            try{
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (photoFile !=null){
                Uri photoURI = FileProvider.getUriForFile(this,"com.example.android.fileprovider",photoFile);


                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws  IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",Locale.ENGLISH).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = ;

        File image = File.createTempFile(imageFileName,".jpg",);
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if ((requestCode == REQUEST_IMAGE_CAPTURE) && (resultCode == RESULT_OK)){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            image.setImageBitmap(imageBitmap);
        }
    }

    private void processPicture(String path){
       // "/storage/Pictures"
    }

    public Bitmap getScaledBitmap(String path, Point size){

        int targetW = image.getWidth();
        int targetH = image.getHeight();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(currentPhotoPath, options);
        int photoW = options.outWidth;
        int photoH = options.outHeight;

        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        options.inJustDecodeBounds = false;
        options.inSampleSize = scaleFactor;
        options.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, options);
        image.setImageBitmap(bitmap);


    }

}
