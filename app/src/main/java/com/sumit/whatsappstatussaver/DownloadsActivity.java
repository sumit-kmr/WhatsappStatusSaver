package com.sumit.whatsappstatussaver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class DownloadsActivity extends AppCompatActivity implements Callback {
    private RecyclerView recyclerView;
    private TextView textView;
    private ArrayList<Status> statuses;
    private StatusRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloads);
        recyclerView = findViewById(R.id.downloads_recycler_view);
        textView = findViewById(R.id.no_downloads_text_view);

        statuses = getImageBitmaps();
        if(statuses.size() == 0) {
            showNoDownloads();
        } else {
            adapter = new StatusRecyclerAdapter(this, statuses, true);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        }
    }

    private ArrayList<Status> getImageBitmaps() {
        File fileDirectory = new File(Environment.getExternalStorageDirectory() + "/WhatsappStatusSaver");
        File[] files = fileDirectory.listFiles();
        ArrayList<Status> statuses = new ArrayList<>();

        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                char[] fileName = files[i].getName().toCharArray();
                int nameLength = fileName.length;
                if (fileName[nameLength - 3] == 'j' && fileName[nameLength - 2] == 'p' && fileName[nameLength - 1] == 'g') {
                    Bitmap bitmap = BitmapFactory.decodeFile(files[i].getAbsolutePath());
                    statuses.add(new Status(Status.IMAGE_TYPE, bitmap, files[i].getAbsolutePath(), String.valueOf(fileName)));
                } else if(fileName[nameLength - 3] == 'm' && fileName[nameLength - 2] == 'p' && fileName[nameLength - 1] == '4') {
                    Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(files[i].getAbsolutePath(), MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
                    statuses.add(new Status(Status.VIDEO_TYPE, bitmap, files[i].getAbsolutePath(), String.valueOf(fileName)));
                }
            }
        }
        return statuses;
    }

    private void showNoDownloads() {
        textView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void callback() {
        statuses = getImageBitmaps();
        adapter.updateList(statuses);
        if(statuses.size() == 0) {
            showNoDownloads();
        }
        Toast.makeText(this, "Item deleted", Toast.LENGTH_SHORT).show();
    }
}