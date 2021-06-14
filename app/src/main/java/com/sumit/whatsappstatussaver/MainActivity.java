package com.sumit.whatsappstatussaver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.status_recycler_view);
        textView = findViewById(R.id.no_status_text_view);

        ArrayList<Status> statuses = getImageBitmaps();
        if(statuses.size() == 0) {
            showNoStatus();
        } else {
            StatusRecyclerAdapter adapter = new StatusRecyclerAdapter(MainActivity.this, statuses, false);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        }

    }

    private ArrayList<Status> getImageBitmaps() {
        File fileDirectory = new File(Environment.getExternalStorageDirectory() + "/WhatsApp/Media/.Statuses");
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

    private void showNoStatus() {
        textView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.action_menu_downloads) {
            Intent intent = new Intent(MainActivity.this, DownloadsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}