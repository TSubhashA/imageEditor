package com.example.imageeditor;

import android.content.Context;
import android.net.Uri;

import androidx.exifinterface.media.ExifInterface;

import java.io.IOException;
import java.io.InputStream;

public class ExifClass {

    public static String exifInfo(Context context, Uri img) {

        ExifInterface exif;
        String msg = null;
        try (InputStream in = context.getContentResolver().openInputStream(img)) {
            exif = new ExifInterface(in);
            // Now you can extract any Exif tag you want
            // Assuming the image is a JPEG or supported raw format

//            Log.w("data", exif.getAttribute(ExifInterface.TAG_DATETIME));
            msg = "Image Location : " + Util.getFilePathFromContentUri(img, context.getContentResolver()) + "\n"
                    + "Resolution : " + exif.getAttribute(ExifInterface.TAG_IMAGE_LENGTH) + "x" + exif.getAttribute(ExifInterface.TAG_IMAGE_WIDTH) +
                    "\n"
                    + "Date : " + exif.getAttribute(ExifInterface.TAG_DATETIME_ORIGINAL) + "\n"
                    + "Size : " + Util.calculateFileSize(img) + "\n"
                    + "Lens Make : " + exif.getAttribute(ExifInterface.TAG_LENS_MAKE) + "\n"
                    + "GPSLatitude : " + exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE) + "\n"
                    + "GPSLatitude Ref : " + exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF) + "\n"
                    + "GPSLongitude : " + exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE) + "\n"
                    + "GPSLongitude Ref : " + exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF) + "\n"
                    + "Model : " + exif.getAttribute(ExifInterface.TAG_MODEL) + "\n"
                    + "Orientation : " + exif.getAttribute(ExifInterface.TAG_ORIENTATION) + "\n"
                    + "White Balance : " + exif.getAttribute(ExifInterface.TAG_WHITE_BALANCE) + "\n"
                    + "FNumber : " + exif.getAttribute(ExifInterface.TAG_F_NUMBER) + "\n"
                    + "ISO SPeed : " + exif.getAttribute(ExifInterface.TAG_ISO_SPEED) + "\n";

        } catch (IOException e) {
            // Handle any errors
        }

        return msg;
    }
}
