package com.samstudio.temanusaha;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by satryaway on 9/17/2015.
 * activity to input profile
 */
public class InsertProfileActivity extends AppCompatActivity {
    private ImageView profilePictureIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        setCallBack();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(result.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, result);
        }
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            profilePictureIV.setImageBitmap(null);
            profilePictureIV.setImageBitmap(setPic(Crop.getOutput(result)));
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void initUI() {
        setContentView(R.layout.insert_profile_layout);
        profilePictureIV = (ImageView) findViewById(R.id.profile_picture_iv);
    }

    private void setCallBack() {
        profilePictureIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePopupDialog();
            }
        });
    }

    private void makePopupDialog() {
        final String[] option = getResources().getStringArray(R.array.set_picture_item);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(InsertProfileActivity.this, android.R.layout.select_dialog_item, option);
        AlertDialog.Builder builder = new AlertDialog.Builder(InsertProfileActivity.this);

        builder.setTitle(R.string.set_profile_picture);
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                switch (which) {
                    case 0:
                        Crop.pickImage(InsertProfileActivity.this);
                        break;
                    default:
                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                        startActivityForResult(intent, Crop.REQUEST_PICK);
                        break;
                }
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();
    }

    private Bitmap setPic(Uri uri) {
        int targetW;
        int targetH;

        // Get the dimensions of the View
        targetW = profilePictureIV.getWidth();
        targetH = profilePictureIV.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(uri.getPath(), bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(uri.getPath(), bmOptions);

        //Checking image orientation
        try {
            int orientation = getExifOrientation(uri.getPath());

            if (orientation == 1) {
                return bitmap;
            }
            boolean isRotated = false;
            Matrix matrix = new Matrix();
            switch (orientation) {
                case 2:
                    matrix.setScale(-1, 1);
                    break;
                case 3:
                    matrix.setRotate(180);
                    break;
                case 4:
                    matrix.setRotate(180);
                    matrix.postScale(-1, 1);
                    break;
                case 5:
                    matrix.setRotate(90);
                    matrix.postScale(-1, 1);
                    isRotated = true;
                    break;
                case 6:
                    matrix.setRotate(90);
                    isRotated = true;
                    break;
                case 7:
                    matrix.setRotate(-90);
                    matrix.postScale(-1, 1);
                    isRotated = true;
                    break;
                case 8:
                    matrix.setRotate(-90);
                    isRotated = true;
                    break;
                default:
                    return bitmap;
            }

            try {
                if (isRotated) {
                    Bitmap oriented = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    Bitmap result = Bitmap.createScaledBitmap(oriented, oriented.getHeight(), oriented.getWidth(), true);
                    bitmap.recycle();
                    return result;
                } else {
                    Bitmap oriented = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    bitmap.recycle();
                    return oriented;
                }
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                return bitmap;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    private static int getExifOrientation(String src) throws IOException {
        int orientation = 1;

        try {
            if (Build.VERSION.SDK_INT >= 5) {
                Class<?> exifClass = Class.forName("android.media.ExifInterface");
                Constructor<?> exifConstructor = exifClass.getConstructor(new Class[]{String.class});
                Object exifInstance = exifConstructor.newInstance(new Object[]{src});
                Method getAttributeInt = exifClass.getMethod("getAttributeInt", new Class[]{String.class, int.class});
                Field tagOrientationField = exifClass.getField("TAG_ORIENTATION");
                String tagOrientation = (String) tagOrientationField.get(null);
                orientation = (Integer) getAttributeInt.invoke(exifInstance, new Object[]{tagOrientation, 1});
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        return orientation;
    }
}
