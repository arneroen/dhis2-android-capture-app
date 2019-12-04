package org.dhis2.usescases.videoLibrary;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import org.dhis2.Components;
import org.dhis2.R;
import org.dhis2.data.service.VideoDownloadWorker;
import org.dhis2.databinding.FragmentVideoBinding;
import org.dhis2.usescases.general.FragmentGlobalAbstract;
import org.dhis2.usescases.videoPlayer.VideoActivity;

import javax.annotation.Nullable;
import javax.inject.Inject;

public class VideoFragment extends FragmentGlobalAbstract implements VideoContracts.VideoView {
    @Inject
    VideoContracts.VideoPresenter presenter;

    private FragmentVideoBinding videoBinding;

    @Override
    public void renderVideoLibrary(){
        String text = String.format(getString(R.string.about_connected), "hello");
        videoBinding.aboutConnected.setText(text);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((Components) context.getApplicationContext()).userComponent()
                .plus(new VideoModule()).inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @androidx.annotation.Nullable ViewGroup container, @androidx.annotation.Nullable Bundle savedInstanceState) {
        videoBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_video, container, false);
        videoBinding.setPresenter(presenter);

        videoBinding.button2.setOnClickListener(view -> {
            Log.i("VIDEOLIB", "Trying to start worker");

            Intent intent = new Intent(getContext(), VideoActivity.class);
            startActivity(intent);
        });
        videoBinding.button3.setOnClickListener(view -> {
            Log.i("button3", "we are in button 3 download terretory!");
            OneTimeWorkRequest uploadWorkRequest = new OneTimeWorkRequest.Builder(VideoDownloadWorker.class)
                    .build();
            WorkManager.getInstance(getContext()).enqueue(uploadWorkRequest);

        });



        return videoBinding.getRoot();

    }

}
