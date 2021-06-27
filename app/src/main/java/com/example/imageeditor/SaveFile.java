package com.example.imageeditor;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SaveFile extends AsyncTask<Void, Void, Void> {


    final String filename;
    final Uri image;
    private ProgressDialog progressdialog;
    private final Context context;

    public SaveFile(Context context, String filename, Uri image) {
        this.context = context;
        this.filename = filename;
        this.image = image;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressdialog = new ProgressDialog(context);
        progressdialog.setMessage("Please Wait....");
        progressdialog.show();
        progressdialog.setCancelable(false);
        progressdialog.setCanceledOnTouchOutside(false);

    }

    @Override
    protected Void doInBackground(Void... strings) {
        saveImage(context, filename, image);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        if (progressdialog != null && progressdialog.isShowing()) {
            progressdialog.dismiss();
        }
        Toast.makeText(context, "Image saved succesfully", Toast.LENGTH_SHORT).show();

    }

    private void saveImage(Context context, String filename, Uri img) {
        FileOutputStream fileOutputStream;
        try {
            Bitmap bitm = MediaStore.Images.Media.getBitmap(context.getContentResolver(), img);

            File filepath = Environment.getExternalStorageDirectory();
            File dir = new File(filepath.getAbsolutePath() + "/ImageEditor/");
            dir.mkdir();
            File file = new File(dir, filename + "_" + System.currentTimeMillis() + ".jpg");

            fileOutputStream = new FileOutputStream(file);

            bitm.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);


        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
