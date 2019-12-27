package org.dhis2.usescases.videoPlayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import org.dhis2.R;
import org.dhis2.data.videoDatabase.VideoDatabaseClient;
import org.dhis2.data.videoDatabase.entities.StoredVideoEntity;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class VideoActivity extends AppCompatActivity {

    private static final String VIDEO_SAMPLE = "tacoma_narrows";
    private VideoView mVideoView;
    private String videoFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.videoFileName = getIntent().getExtras().getString("fileName");
        setContentView(R.layout.activity_video);
        mVideoView = findViewById(R.id.videoview);
        MediaController controller = new MediaController(this);
        controller.setMediaPlayer(mVideoView);
        mVideoView.setMediaController(controller);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initializePlayer();
    }
    @Override
    protected void onStop() {
        super.onStop();

        releasePlayer();
    }
    @Override
    protected void onPause() {
        super.onPause();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            mVideoView.pause();
        }
    }

    private Uri getMedia(String mediaName) {
        return Uri.parse("android.resource://" + getPackageName() +
                "/raw/" + mediaName);
    }
    private void initializePlayer() {
        String fileName = this.videoFileName;
        /*String fileName;
        try {
            List<StoredVideoEntity> videos = (List<StoredVideoEntity>) new VideoDatabaseClient(this, 0).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
            fileName = videos.get(0).getFileName();
        } catch (InterruptedException e) {
            e.printStackTrace();
            fileName = "test9.mp4";
        } catch (ExecutionException e) {
            e.printStackTrace();
            fileName = "test9.mp4";
        }*/
        File file = new File(getExternalFilesDir(null).getAbsolutePath() + "/" + fileName);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PERMISSION_GRANTED){
            Log.i("work", "we dont have permission?!");
            Uri uri = Uri.fromFile(file);
            mVideoView.setVideoPath(uri.getPath());
            mVideoView.start();
        } else {
            Log.i("work", "we have permission");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9909);
        }


    }
    private void releasePlayer() {
        mVideoView.stopPlayback();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 9909 && grantResults[0] == PERMISSION_GRANTED) {
            initializePlayer();
        }
    }
}
