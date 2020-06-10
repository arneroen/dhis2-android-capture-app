package org.dhis2.usescases.videoLibrary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.google.gson.Gson;

import org.dhis2.Components;
import org.dhis2.R;
import org.dhis2.data.service.VideoDownloadWorker;
import org.dhis2.data.videoDatabase.VideoDatabaseClient;
import org.dhis2.data.videoDatabase.entities.StoredVideoEntity;
import org.dhis2.databinding.FragmentVideoBinding;
import org.dhis2.usescases.general.FragmentGlobalAbstract;
import org.dhis2.usescases.videoPlayer.VideoActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.annotation.Nullable;
import javax.inject.Inject;

public class VideoFragment extends FragmentGlobalAbstract implements VideoContracts.VideoView {
    @Inject
    VideoContracts.VideoPresenter presenter;

    private FragmentVideoBinding videoBinding;
    private VideoListAdapter videoListAdapter;
    
    @Override
    public void renderVideoLibrary(){
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

        RecyclerView rv = videoBinding.videoListRecyclerView;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getActivity());
        rv.setLayoutManager(linearLayoutManager);

        List<StoredVideoEntity> videoList = new ArrayList<>();
        try {
            videoList = (List<StoredVideoEntity>) new VideoDatabaseClient(getContext(), 0).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        videoListAdapter = new VideoListAdapter(videoList, (uid, fileName, languages) -> {
            Log.d("d", "we got it?");
            //connect to starting the video activity here, with the filename!
            Intent intent = new Intent(getContext(), VideoActivity.class);
            intent.putExtra("fileName", fileName);
            intent.putExtra("languages", (new Gson()).toJson(languages));
            startActivity(intent);

        });
        rv.setAdapter(videoListAdapter);

        videoBinding.searchText.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_UP && event.getRawX() >= (videoBinding.searchText.getRight() - videoBinding.searchText.getCompoundDrawables()[2].getBounds().width())) {
                videoBinding.searchText.setText(new char[0], 0, 0);
                videoListAdapter.getFilter().filter("");
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                return true;
            }
            return false;
        });

        videoBinding.searchButton.setOnClickListener(view -> {
            String searchText = videoBinding.searchText.getText().toString();
            videoListAdapter.getFilter().filter(searchText);
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        });

        videoBinding.searchText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                videoBinding.searchButton.performClick();
                return true;
            }
            return false;
        });

        videoBinding.downloadAllButton.setOnClickListener(view -> {
            Log.i("button3", "we are in button 3 download terretory!");
            OneTimeWorkRequest uploadWorkRequest = new OneTimeWorkRequest.Builder(VideoDownloadWorker.class)
                    .build();
            WorkManager.getInstance(getContext()).enqueue(uploadWorkRequest);
        });

        videoBinding.getExistingButton.setOnClickListener(view -> {
            Log.d("stuff", "it's getting clicked!");
            try {
                List<StoredVideoEntity> ents = (List<StoredVideoEntity>) new VideoDatabaseClient(getContext(), 0).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
                Log.i("d", "we did it!");
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });


        videoBinding.deleteAllButton.setOnClickListener(view -> {
            new VideoDatabaseClient(getContext(), 2).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        });

        videoBinding.searchLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                videoBinding.searchLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                float buttonWidth = videoBinding.searchButton.getWidth();
                float layoutWidth = videoBinding.searchLayout.getWidth();
                float searchWidth = layoutWidth - buttonWidth;
                ViewGroup.LayoutParams params = videoBinding.searchText.getLayoutParams();
                params.width = (int) Math.floor((double) searchWidth);
                videoBinding.searchText.setLayoutParams(params);
            }
        });



        return videoBinding.getRoot();

    }

}
