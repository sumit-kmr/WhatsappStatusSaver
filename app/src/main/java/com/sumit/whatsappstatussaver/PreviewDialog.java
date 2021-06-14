package com.sumit.whatsappstatussaver;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

public class PreviewDialog extends Dialog {
    Activity context;
    Status status;
    boolean fromDownloads;

    public PreviewDialog(@NonNull Activity context, Status status, boolean fromDownloads) {
        super(context);
        this.context = context;
        this.status = status;
        this.fromDownloads = fromDownloads;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.preview_dialog_layout);
        VideoView videoView = findViewById(R.id.preview_video_view);
        ImageView imageView = findViewById(R.id.preview_image_view);
        Button playPauseButton = findViewById(R.id.preview_play_pause_button);
        ImageView download = findViewById(R.id.preview_download_image_view);
        ImageView share = findViewById(R.id.preview_share_image_view);
        ImageView delete = findViewById(R.id.preview_delete_image_view);

        if (fromDownloads) {
            download.setVisibility(View.GONE);
            delete.setVisibility(View.VISIBLE);
        }

        if (status.getType() == Status.IMAGE_TYPE) {
            imageView.setImageBitmap(status.getBitmap());
            videoView.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            playPauseButton.setVisibility(View.INVISIBLE);
        } else if (status.getType() == Status.VIDEO_TYPE) {
            MediaController mediaController = new MediaController(context);
            mediaController.setAnchorView(videoView);
            videoView.setMediaController(mediaController);
            videoView.requestFocus();
            videoView.setVideoPath(status.getMediaPath());
            videoView.start();
            videoView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
            playPauseButton.setVisibility(View.VISIBLE);
            playPauseButton.setText("Pause");
            playPauseButton.setOnClickListener(l -> {
                if (videoView.isPlaying()) {
                    videoView.pause();
                    playPauseButton.setText("Play");
                } else {
                    videoView.start();
                    playPauseButton.setText("Pause");
                }
            });
        }

        delete.setOnClickListener(l -> {
            new AlertDialog.Builder(context)
                    .setTitle("Delete item")
                    .setMessage("Are you sure you want to delete this item?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        FileUtil.deleteFile(
                                Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsappStatusSaver/"+status.getFileName());
                        ((DownloadsActivity)context).callback();
                        dismiss();
                    })
                    .setNegativeButton("No", null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        });

        download.setOnClickListener(l -> {
            String fileName = status.getFileName();
            FileUtil.copyFile(context, status.getMediaPath(),
                    Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsappStatusSaver",
                    fileName);
            dismiss();
        });

        share.setOnClickListener(l -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            if (status.getType() == Status.IMAGE_TYPE) {
                shareIntent.setType("image/jpg");
            } else if (status.getType() == Status.VIDEO_TYPE) {
                shareIntent.setType("video/mp4");
            }
            File mediaFileToShare = new File(status.getMediaPath());
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri fileUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", mediaFileToShare);
            shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
            context.startActivity(Intent.createChooser(shareIntent, "Share using"));
        });
    }
}
