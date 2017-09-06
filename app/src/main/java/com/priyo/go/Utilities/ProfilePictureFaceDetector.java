package com.priyo.go.Utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.util.SparseArray;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import static com.priyo.go.Utilities.Utilities.getImageBitmap;

/**
 * Created by sajid.shahriar on 4/23/17.
 */

public class ProfilePictureFaceDetector {

    private Context context;
    private final Bitmap imageBitmap;

    public ProfilePictureFaceDetector(Context context, final String filePath) {
        this(context, getImageBitmap(filePath));
    }

    public ProfilePictureFaceDetector(Context context, final Bitmap imageBitmap) {
        this.context = context;
        this.imageBitmap = imageBitmap;
/*
        // In case of any chance not to destroy original image, uncomment this part.

        this.imageBitmap = Bitmap.createBitmap(this.imageBitmap.getWidth(), this.imageBitmap.getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(this.imageBitmap);
        canvas.drawBitmap(this.imageBitmap, 0, 0, null);
*/
    }


    public int countFaces() {

        FaceDetector faceDetector = new FaceDetector.Builder(context).setTrackingEnabled(false).build();
        if (!faceDetector.isOperational()) {
            new AlertDialog.Builder(context).setMessage("Could not set up the face detector!").show();
            return 0;
        } else {
            Frame frame = new Frame.Builder().setBitmap(imageBitmap).build();
            SparseArray<Face> faces = faceDetector.detect(frame);
            return faces.size();
        }
    }

    public boolean hasAnyFace() {
        return this.countFaces() > 0;
    }
}
