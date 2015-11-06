package com.samstudio.temanusaha;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.samstudio.temanusaha.util.APIAgent;
import com.samstudio.temanusaha.util.CommonConstants;
import com.samstudio.temanusaha.util.Utility;
import com.soundcloud.android.crop.Crop;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import cz.msebera.android.httpclient.Header;

/**
 * Created by satryaway on 9/17/2015.
 * activity to input profile
 */
public class CustomerProfileActivity extends AppCompatActivity {
    private ImageView profilePictureIV;
    private EditText firstNameET, lastNameET, placeOfBirthET, dateOfBirthET, idCardNumberET, expiredIdET,
            emailET, addressET, phoneNumberET;
    private RadioButton maleRB, femaleRB, marriedRB, singleRB;
    private Button saveBtn;
    private boolean isPickDateOfBirth;
    private DatePickerDialog datePickerDialog;
    private SharedPreferences sharedPreferences;
    private File userImageFile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        setCallBack();
        putData();
    }

    private void initUI() {
        setContentView(R.layout.customer_profile_layout);
        profilePictureIV = (ImageView) findViewById(R.id.profile_picture_iv);
        firstNameET = (EditText) findViewById(R.id.first_name_et);
        lastNameET = (EditText) findViewById(R.id.last_name_et);
        maleRB = (RadioButton) findViewById(R.id.male_rb);
        femaleRB = (RadioButton) findViewById(R.id.female_rb);
        placeOfBirthET = (EditText) findViewById(R.id.place_of_birth_et);
        dateOfBirthET = (EditText) findViewById(R.id.date_of_birth_et);
        idCardNumberET = (EditText) findViewById(R.id.id_card_et);
        expiredIdET = (EditText) findViewById(R.id.expired_id_et);
        emailET = (EditText) findViewById(R.id.email_et);
        addressET = (EditText) findViewById(R.id.address_et);
        phoneNumberET = (EditText) findViewById(R.id.phone_number_et);
        marriedRB = (RadioButton) findViewById(R.id.married_rb);
        singleRB = (RadioButton) findViewById(R.id.single_rb);
        saveBtn = (Button) findViewById(R.id.save_btn);
        maleRB.setChecked(true);
        marriedRB.setChecked(true);

        dateOfBirthET.setFocusable(false);
        expiredIdET.setFocusable(false);
        datePickerDialog = new DatePickerDialog(this, dateListener, 1990, 1, 1);
    }

    private void setCallBack() {
        profilePictureIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePopupDialog();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFormVerified()) {
                    Intent intent = new Intent(CustomerProfileActivity.this, PickShapeActivity.class);
                    startActivity(intent);
                }
            }
        });

        dateOfBirthET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPickDateOfBirth = true;
                datePickerDialog.show();
            }
        });

        expiredIdET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPickDateOfBirth = false;
                datePickerDialog.show();
            }
        });
    }

    private DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            showDate(year, monthOfYear, dayOfMonth);
        }
    };

    private void showDate(int year, int month, int day) {
        String fixedMonth = month < 10 ? "0" + month : "" + month;
        String fixedDay = day < 10 ? "0" + day : "" + day;
        if (isPickDateOfBirth)
            dateOfBirthET.setText(new StringBuilder().append(year).append("-").append(fixedMonth).append("-").append(fixedDay));
        else
            expiredIdET.setText(new StringBuilder().append(year).append("-").append(fixedMonth).append("-").append(fixedDay));
    }

    private boolean isFormVerified() {
        int filledFormTotal = 0;

        if (firstNameET.getText().length() < 2)
            firstNameET.setError(getString(R.string.minimum_two_char_error));
        else
            filledFormTotal++;

        if (lastNameET.getText().length() < 2)
            lastNameET.setError(getString(R.string.minimum_two_char_error));
        else
            filledFormTotal++;

        if (placeOfBirthET.getText().length() == 0)
            placeOfBirthET.setError(getString(R.string.should_not_be_empty_error));
        else
            filledFormTotal++;

        if (dateOfBirthET.getText().length() == 0)
            dateOfBirthET.setError(getString(R.string.should_not_be_empty_error));
        else
            filledFormTotal++;

        if (idCardNumberET.getText().length() == 0)
            idCardNumberET.setError(getString(R.string.should_not_be_empty_error));
        else
            filledFormTotal++;

        if (phoneNumberET.getText().length() == 0)
            phoneNumberET.setError(getString(R.string.should_not_be_empty_error));
        else
            filledFormTotal++;

        if (expiredIdET.getText().length() == 0)
            expiredIdET.setError(getString(R.string.should_not_be_empty_error));
        else
            filledFormTotal++;

        if (!Utility.isEmailValid(emailET.getText().toString()))
            emailET.setError(getString(R.string.email_validation_error));
        else
            filledFormTotal++;

        if (addressET.getText().length() == 0)
            addressET.setError(getString(R.string.should_not_be_empty_error));
        else
            filledFormTotal++;

        if (phoneNumberET.getText().length() == 0)
            phoneNumberET.setError(getString(R.string.should_not_be_empty_error));
        else
            filledFormTotal++;

        if (userImageFile == null)
            Toast.makeText(CustomerProfileActivity.this, R.string.null_picture_error, Toast.LENGTH_SHORT).show();
        else
            filledFormTotal++;

        return filledFormTotal == 11;
    }

    /*private void updateProfile() throws FileNotFoundException {
        String url = CommonConstants.SERVICE_UPDATE_USER_PROFILE;

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));
        progressDialog.show();

        firstName = firstNameET.getField().getText().toString();
        lastName = lastNameET.getField().getText().toString();
        description = yourInformationET.getField().getText().toString();

        RequestParams parameters = new RequestParams();
        parameters.put(CommonConstants.FIRST_NAME, firstName);
        parameters.put(CommonConstants.LAST_NAME, lastName);
        parameters.put(CommonConstants.DESCRIPTION, description);

        if (userImageFile != null)
            parameters.put(CommonConstants.PROFILE_PIC, userImageFile);

        if (coverImageFile != null)
            parameters.put(CommonConstants.COVER_IMAGE, coverImageFile);

        parameters.put(CommonConstants.USER_ID, LivesApplication.getInstance().getUserId());
        parameters.put(CommonConstants.AUTH_TOKEN, LivesApplication.getInstance().getAuthToken());

        APIAgent.post(url, parameters, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                progressDialog.setProgress(0);
                progressDialog.show();
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getInt(CommonConstants.RESCODE) == 0) {
                        changePreferences(response);
                        Intent returnIntent = new Intent(CustomerProfileActivity.this, MyDashboardActivity.class);
                        returnIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(returnIntent);
                    } else {
                        Toast.makeText(CustomerProfileActivity.this, R.string.failed, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(CustomerProfileActivity.this, R.string.failed, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(CustomerProfileActivity.this, R.string.failed, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                progressDialog.dismiss();
            }
        });
    }*/


    private void putData() {
        sharedPreferences = TemanUsahaApplication.getInstance().getSharedPreferences();
        firstNameET.setText(sharedPreferences.getString(CommonConstants.FIRST_NAME, ""));
        lastNameET.setText(sharedPreferences.getString(CommonConstants.LAST_NAME, ""));
        placeOfBirthET.setText(sharedPreferences.getString(CommonConstants.PLACE_OF_BIRTH, ""));
        dateOfBirthET.setText(sharedPreferences.getString(CommonConstants.DATE_OF_BIRTH, ""));
        idCardNumberET.setText(sharedPreferences.getString(CommonConstants.ID_CARD_NUMBER, ""));
        emailET.setText(sharedPreferences.getString(CommonConstants.EMAIL, ""));
        expiredIdET.setText(sharedPreferences.getString(CommonConstants.ID_CARD_EXP_DATE, ""));
        addressET.setText(sharedPreferences.getString(CommonConstants.ADDRESS, ""));
        phoneNumberET.setText(sharedPreferences.getString(CommonConstants.PHONE, ""));

        if (sharedPreferences.getString(CommonConstants.MARITAL_STATUS, CommonConstants.SINGLE).equals(CommonConstants.SINGLE))
            singleRB.setChecked(true);
        else
            marriedRB.setChecked(true);

        if (sharedPreferences.getString(CommonConstants.GENDER, CommonConstants.MALE).equals(CommonConstants.MALE))
            maleRB.setChecked(true);
        else
            femaleRB.setChecked(true);
    }

    private void makePopupDialog() {
        final String[] option = getResources().getStringArray(R.array.set_picture_item);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(CustomerProfileActivity.this, android.R.layout.select_dialog_item, option);
        AlertDialog.Builder builder = new AlertDialog.Builder(CustomerProfileActivity.this);

        builder.setTitle(R.string.set_profile_picture);
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                switch (which) {
                    case 0:
                        Crop.pickImage(CustomerProfileActivity.this);
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
            Bitmap imageBitmap = setPic(Crop.getOutput(result));
            userImageFile = storeImage(imageBitmap);
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private File storeImage(Bitmap image) {
        File f = getOutputMediaFile();

        String TAG = CommonConstants.PROFILE_PICTURE;
        if (f == null) {
            Log.d(TAG, "Error creating media file, check storage permissions: ");// e.getMessage());
            return null;
        }
        try {
            FileOutputStream fos = new FileOutputStream(f);
            image.compress(Bitmap.CompressFormat.JPEG, 80, fos);
            fos.close();
            return f;
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }

        return null;
    }

    private File getOutputMediaFile() {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Images");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        // Create a media file name
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "profile_pic" + ".jpg");
        return mediaFile;
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
