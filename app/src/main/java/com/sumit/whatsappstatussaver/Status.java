package com.sumit.whatsappstatussaver;

import android.graphics.Bitmap;

public class Status {
    public static final int VIDEO_TYPE = 1;
    public static final int IMAGE_TYPE = 2;

    private int type;
    private Bitmap bitmap;
    private String mediaPath;
    private String fileName;

    public Status(int type, Bitmap bitmap, String path, String fileName) {
        this.type = type;
        this.bitmap = bitmap;
        this.mediaPath = path;
        this.fileName = fileName;
    }

    public int getType() {
        return type;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public String getMediaPath() {
        return mediaPath;
    }

    public String getFileName() {
        return fileName;
    }
}
