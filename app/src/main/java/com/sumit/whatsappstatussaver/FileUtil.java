package com.sumit.whatsappstatussaver;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtil {
    public static void copyFile(Context context, String inputPath, String outputPath, String outputFileName) {

        InputStream in = null;
        OutputStream out = null;
        try {

            //create output directory if it doesn't exist
            File dir = new File(outputPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }


            in = new FileInputStream(inputPath);
            out = new FileOutputStream(outputPath + "/" + outputFileName);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();

            // write the output file (You have now copied the file)
            out.flush();
            out.close();

        } catch (Exception e) {
            Log.e("ERROR[Copy]: ", e.getMessage());
            Toast.makeText(context, "Some error occured", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(context, "File downloaded at " + outputPath + "/" + outputFileName, Toast.LENGTH_SHORT).show();

    }

    public static void deleteFile(String inputPath) {
        try {
            // delete the original file
            new File(inputPath).delete();
        }
        catch (Exception e) {
            Log.e("tag", e.getMessage());
        }
    }
}
