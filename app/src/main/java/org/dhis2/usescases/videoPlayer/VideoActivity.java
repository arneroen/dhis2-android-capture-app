package org.dhis2.usescases.videoPlayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import org.dhis2.R;

import java.io.File;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class VideoActivity extends AppCompatActivity {

    private VideoView mVideoView;
    private String videoFileName;
    private MediaPlayer mediaPlayer;
    private Button audioButton;
    private ImageView backArrow;
    private MediaPlayer.TrackInfo[] trackInfos;
    private int videoPos;
    private boolean isFullscreen = true;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("videoPos", mVideoView.getCurrentPosition());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle inState) {
        super.onRestoreInstanceState(inState);
        videoPos = inState.getInt("videoPos");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (savedInstanceState != null) {
            videoPos = savedInstanceState.getInt("videoPos");
        }
        this.videoFileName = getIntent().getExtras().getString("fileName");
        setContentView(R.layout.activity_video);
        mVideoView = findViewById(R.id.videoview);
        audioButton = findViewById(R.id.audioButton);
        backArrow = findViewById(R.id.backArrow);

        audioButton.setOnClickListener(view -> {
            //mediaPlayer.selectTrack(2);
            mediaPlayer.seekTo(2000);
        });

        backArrow.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Log.i("Back_Button", "Button was clicked");
                finish();
            }
        });

        mVideoView.setOnPreparedListener(mp -> {

            trackInfos = mp.getTrackInfo();
            mediaPlayer = mp;
        });
        mVideoView.setOnClickListener(v -> {
            if (!isFullscreen) {
                mVideoView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
                isFullscreen = true;
            } else isFullscreen = false;
        });

        MediaController controller = new MediaController(this){
            @Override
            public void show(){
                super.show(0);
            }
        };

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

    private void initializePlayer() {
        File file = new File(getExternalFilesDir(null).getAbsolutePath() + "/" + videoFileName);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PERMISSION_GRANTED){
            Log.i("work", "we dont have permission?!");
            Uri uri = Uri.fromFile(file);
            mVideoView.setVideoPath(uri.getPath());
            mVideoView.start();
            mVideoView.seekTo(videoPos);
        } else {
            Log.i("work", "we have permission");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9909);
        }
        mVideoView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
        | View.SYSTEM_UI_FLAG_FULLSCREEN);


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
