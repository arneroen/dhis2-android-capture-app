package org.dhis2.usescases.video;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import org.dhis2.R;

public class VideoActivity extends AppCompatActivity {

    private static final String VIDEO_SAMPLE = "tacoma_narrows";
    private VideoView mVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        Uri videoUri = getMedia(VIDEO_SAMPLE);
        mVideoView.setVideoURI(videoUri);
        mVideoView.start();

    }
    private void releasePlayer() {
        mVideoView.stopPlayback();
    }
}
