package com.example.imageeditor;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity;
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants;

import java.io.IOException;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    final int PIC_CROP = 2;
    ImageView imageview;
    Uri img = null;
    Bitmap bitmap;
    boolean ishorizontalFlip = false;
    boolean isverticalFlip = false;
    Button horFlibtn, verFlipBtn, cropBtn, infoBtn, editBtn;
    Toolbar myToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        myToolbar.setTitle("Image Editor");

        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);


        imageview = findViewById(R.id.image_view);
        horFlibtn = findViewById(R.id.flip_hor);
        verFlipBtn = findViewById(R.id.flip_ver);
        cropBtn = findViewById(R.id.crop_image);
        infoBtn = findViewById(R.id.info_image);
        editBtn = findViewById(R.id.edit_image);

        horFlibtn.setOnClickListener(this);
        verFlipBtn.setOnClickListener(this);
        cropBtn.setOnClickListener(this);
        infoBtn.setOnClickListener(this);
        editBtn.setOnClickListener(this);


        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("img")) {
            img = Uri.parse(extras.getString("img"));
            imageview.setImageURI(img);
        }

        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), img);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == 200) {
                img = data.getData();
                imageview.setImageURI(img);
            } else if (requestCode == PIC_CROP) {
                //get the returned data
                Bundle extras = data.getExtras();
                //get the cropped bitmap
                bitmap = extras.getParcelable("data");
                img = Util.getImageUriFromBitmap(this, bitmap);
                imageview.setImageURI(img);
            }
        }
    }

    void verticalFLip() {
        Bitmap bInput = bitmap, bOutput;
        Matrix matrix = new Matrix();
        if (!ishorizontalFlip) {
            if (!isverticalFlip) {
                matrix.preScale(1.0f, -1.0f);
                isverticalFlip = true;
            } else {
                matrix.preScale(1.0f, 1.0f);
                isverticalFlip = false;
            }
        } else {
            if (!isverticalFlip) {
                matrix.preScale(-1.0f, -1.0f);
                isverticalFlip = true;
            } else {
                matrix.preScale(-1.0f, 1.0f);
                isverticalFlip = false;
            }

        }
        bOutput = Bitmap.createBitmap(bInput, 0, 0, bInput.getWidth(), bInput.getHeight(), matrix, true);
        imageview.setImageBitmap(bOutput);
    }

    void horizontalFLip() {

        Bitmap bInput = bitmap, bOutput;

        Matrix matrix = new Matrix();

        if (!isverticalFlip) {
            if (!ishorizontalFlip) {
                matrix.preScale(-1.0f, 1.0f);
                ishorizontalFlip = true;
            } else {
                matrix.preScale(1.0f, 1.0f);
                ishorizontalFlip = false;
            }
        } else {
            if (!ishorizontalFlip) {
                matrix.preScale(-1.0f, -1.0f);
                ishorizontalFlip = true;
            } else {
                matrix.preScale(1.0f, -1.0f);
                ishorizontalFlip = false;
            }

        }

        bOutput = Bitmap.createBitmap(bInput, 0, 0, bInput.getWidth(), bInput.getHeight(), matrix, true);
        imageview.setImageBitmap(bOutput);
    }

    void dsEditor() {

        // If the input image uri for DS Photo Editor is "inputImageUri", launch the editor UI

        // using the following code

        Intent dsPhotoEditorIntent = new Intent(this, DsPhotoEditorActivity.class);

        dsPhotoEditorIntent.setData(img);

        // This is optional. By providing an output directory, the edited photo

        // will be saved in the specified folder on your device's external storage;

        // If this is omitted, the edited photo will be saved to a folder

        // named "DS_Photo_Editor" by default.

        dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY, "YOUR_OUTPUT_IMAGE_FOLDER");

        // Optional customization: hide some tools you don't need as below

        int[] toolsToHide = {DsPhotoEditorActivity.TOOL_CONTRAST, DsPhotoEditorActivity.TOOL_DRAW, DsPhotoEditorActivity.TOOL_WARMTH, DsPhotoEditorActivity.TOOL_EXPOSURE, DsPhotoEditorActivity.TOOL_FILTER, DsPhotoEditorActivity.TOOL_FRAME, DsPhotoEditorActivity.TOOL_CROP, DsPhotoEditorActivity.TOOL_ORIENTATION};

        dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_TOOLS_TO_HIDE, toolsToHide);

        startActivityForResult(dsPhotoEditorIntent, 200);

    }

    void picInfo() {

        String msg = ExifClass.exifInfo(this, img);

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.info_dialog);

        TextView text = dialog.findViewById(R.id.text_dialogue);

        text.setText(msg);

        Button dialogButton = (Button) dialog.findViewById(R.id.dialog_button);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    void saveDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.save_dialogue);


        EditText text = dialog.findViewById(R.id.input);
        Button dialogOkButton = (Button) dialog.findViewById(R.id.dialog_ok_button);
        Button dialogCancelButton = (Button) dialog.findViewById(R.id.dialog_cancel_button);

        dialogOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = text.getText().toString().trim();
                if (name.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Name cant be blank", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.dismiss();
                    new SaveFile(MainActivity.this, name, img).execute();
                }
            }
        });
        dialogCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    void performCrop() {
        try {

            //call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(img, "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            //indicate output X and Y
            cropIntent.putExtra("outputX", 500);
            cropIntent.putExtra("outputY", 500);
            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);

        } catch (ActivityNotFoundException anfe) {
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.flip_hor:
                horizontalFLip();
                break;
            case R.id.flip_ver:
                verticalFLip();
                break;
            case R.id.crop_image:
                performCrop();
                break;
            case R.id.info_image:
                picInfo();
                break;
            case R.id.edit_image:
                dsEditor();
                break;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_save) {
            saveDialog();
        }
        return super.onOptionsItemSelected(item);
    }
}