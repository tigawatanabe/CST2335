package com.example.tiggs.androidlabs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.Toast;

public class ListItemsActivity extends Activity {
    protected static final String ACTIVITY_NAME = "ListItemActivity";
    ImageButton imageButton;
    Switch switchButton;
    RadioButton radioButton;
    CheckBox checkBox;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    //function to take pic
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    //function to save pics
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageView mImageView;
            imageButton.setImageBitmap(imageBitmap);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_items);

        switchButton = findViewById(R.id.switchButton);
        imageButton = findViewById(R.id.imageButton);
        checkBox = findViewById(R.id.checkBox);
        radioButton = findViewById(R.id.radioButtonPOOP);

        imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
        //tell switch to call toasty()
        switchButton.setOnCheckedChangeListener((e, f) -> toasty());
        checkBox.setOnCheckedChangeListener((e,f) -> confirmUser());
    }
    protected void confirmUser(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ListItemsActivity.this);
        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(R.string.dialog_message)
        .setTitle(R.string.dialog_title)
        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
             public void onClick(DialogInterface dialog, int id) {
             // User clicked OK button
                 Intent resultIntent = new Intent(  );
                 resultIntent.putExtra("Response", "My Information to Share");
                 setResult(Activity.RESULT_OK, resultIntent);
                 finish();

             }
                })
        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
             public void onClick(DialogInterface dialog, int id) {
             // User cancelled the dialog
                    }
                })
        .show();

    }
    protected void toasty(){
            if (switchButton.isChecked()){
                CharSequence text = "Switch is On";// "Switch is Off"
                int duration = Toast.LENGTH_SHORT; //= Toast.LENGTH_LONG if Off
                Toast toast = Toast.makeText(this , text, duration); //this is the ListActivity
                toast.show(); //display your message box
            } else {
                CharSequence text = "Switch is Off";// "Switch is On"
                int duration = Toast.LENGTH_LONG; //= Toast.LENGTH_SHORT if on
                Toast toast = Toast.makeText(this , text, duration); //this is the ListActivity
                toast.show(); //display your message box
            }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");
    }
    @Override
    protected void onPause(){
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");
    }
    @Override
    protected void onStop(){
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }
}
