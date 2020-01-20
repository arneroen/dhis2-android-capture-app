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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.VideoView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.dhis2.R;
import org.dhis2.data.videoDatabase.entities.VideoLanguageEntity;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class VideoActivity extends AppCompatActivity {

    private VideoView mVideoView;
    private String videoFileName;
    private MediaPlayer mediaPlayer;
    private ImageView backArrow;
    private int videoPos;
    private boolean isFullscreen = true;
    private List<VideoLanguageEntity> langs;
    private TrackPosition selectedLang;
    private LinearLayout languageSelect;
    private int numAudioTracks;
    private LinearLayout toolbar;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("videoPos", mVideoView.getCurrentPosition());
        if (selectedLang != null) {
            outState.putString("videoLang", (new Gson()).toJson(selectedLang));
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle inState) {
        super.onRestoreInstanceState(inState);
        videoPos = inState.getInt("videoPos");
        Type trackPositionType = new TypeToken<TrackPosition>() {
        }.getType();

        selectedLang = (new Gson()).fromJson(inState.getString("videoLang"), trackPositionType);
        int i = 1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (savedInstanceState != null) {
            videoPos = savedInstanceState.getInt("videoPos");
        }
        this.videoFileName = getIntent().getExtras().getString("fileName");
        Type listType = new TypeToken<List<VideoLanguageEntity>>() {
        }.getType();
        this.langs = (new Gson()).fromJson(getIntent().getExtras().getString("languages"), listType);
        setContentView(R.layout.activity_video);
        mVideoView = findViewById(R.id.videoview);
        Spinner dropdown = findViewById(R.id.spinner1);
        Button playButton = findViewById(R.id.playButton);
        languageSelect = findViewById(R.id.languageSelect);
        backArrow = findViewById(R.id.backArrow);
        toolbar = findViewById(R.id.toolbar);

        playButton.setOnClickListener(v -> {
            TrackPosition trackPosition = (TrackPosition) dropdown.getSelectedItem();
            selectedLang = trackPosition;
            languageSelect.setVisibility(View.GONE);
            mediaPlayer.selectTrack(trackPosition.getMpPosition());
            mVideoView.start();
        });


        backArrow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i("Back_Button", "Button was clicked");
                finish();
            }
        });

        mVideoView.setOnPreparedListener(mp -> {

            MediaPlayer.TrackInfo[] tracks = mp.getTrackInfo();
            int trackPos = 0;
            List<TrackPosition> trackPositions = new ArrayList<>();
            numAudioTracks = 0;

            for (int i = 0; i < tracks.length; i++) {
                if (tracks[i].getTrackType() == MediaPlayer.TrackInfo.MEDIA_TRACK_TYPE_AUDIO) {
                    trackPositions.add(new TrackPosition(i, trackPos));
                    numAudioTracks++;
                    trackPos++;
                }
            }
            for (VideoLanguageEntity lang : langs) {
                for (TrackPosition pos : trackPositions) {
                    if (lang.getIndex() == pos.getListPosition()) {
                        pos.setLangName(lang.getLanguageName());
                    }
                }
            }
            if (isFullscreen) {
                toolbar.setVisibility(View.GONE);
            }

            ArrayAdapter<TrackPosition> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, trackPositions);
            dropdown.setAdapter(adapter);

            mediaPlayer = mp;

            if (numAudioTracks <= 1) {
                languageSelect.setVisibility(View.GONE);
                mVideoView.start();
            } else if (selectedLang != null) {
                languageSelect.setVisibility(View.GONE);
                mediaPlayer.selectTrack(selectedLang.getMpPosition());
                mVideoView.start();
            }
        });
        mVideoView.setOnClickListener(v -> {
            if (!isFullscreen) {
                mVideoView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
                toolbar.setVisibility(View.GONE);
                isFullscreen = true;
            } else {
                toolbar.setVisibility(View.VISIBLE);
                isFullscreen = false;
            }
        });

        MediaController controller = new MediaController(this) {
            @Override
            public void show() {
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

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PERMISSION_GRANTED) {
            Log.i("work", "we dont have permission?!");
            Uri uri = Uri.fromFile(file);
            mVideoView.setVideoPath(uri.getPath());
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

    private final class TrackPosition {
        //position in the mediaPlayer trackInfo-array.
        private final int mpPosition;
        //Position of the language gotten from VideoLanguageEntity
        private final int listPosition;
        private String langName;

        public TrackPosition(int mpPosition, int listPosition) {
            this.mpPosition = mpPosition;
            this.listPosition = listPosition;
        }

        public void setLangName(String langName) {
            this.langName = langName;
        }

        public String toString() {
            return langName;
        }

        public int getMpPosition() {
            return mpPosition;
        }

        public int getListPosition() {
            return listPosition;
        }
    }
}
