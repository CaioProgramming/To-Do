package com.myself.todo.Utils;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class Utilities {
    public static void saveImage(Bitmap bitmap, String username) {
        OutputStream output;
        String recentImageInCache;
        File filepath = Environment.getExternalStorageDirectory();

        // Create a new folder in SD Card
        File dir = new File(filepath.getAbsolutePath()
                + "/ToDo/user" + username);
        dir.mkdirs();

        // Create a name for the saved image
        File file = new File(dir, username + ".jpg");
        try {

            output = new FileOutputStream(file);

            // Compress into png format image from 0% - 100%
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
            output.flush();
            output.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
