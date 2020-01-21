package org.dhis2.usescases.videoLibrary;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.dhis2.R;
import org.dhis2.data.videoDatabase.entities.StoredVideoEntity;
import org.dhis2.data.videoDatabase.entities.VideoLanguageEntity;

import java.util.ArrayList;
import java.util.List;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.MyViewHolder> implements Filterable {

    private VideoSearchFilter filter;
    private List<StoredVideoEntity> originalVideoList;
    public List<StoredVideoEntity> videoList;

    private OnRowClickListener onRowClickListener;

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new VideoSearchFilter(this, originalVideoList);
        }
        return filter;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView titleText, descriptionText, languagesText;
        public ImageView videoThumbnail;
        private ImageView thumbnail;
        private View currentView;

        public MyViewHolder(View view){
            super(view);
            titleText = view.findViewById(R.id.video_title);
            descriptionText = view.findViewById(R.id.video_description);
            videoThumbnail = view.findViewById(R.id.video_thumbnail);
            languagesText = view.findViewById(R.id.video_languages);
            thumbnail = view.findViewById(R.id.video_thumbnail);
            this.currentView = view;
        }
    }

    public VideoListAdapter(List<StoredVideoEntity> videoList, OnRowClickListener onRowClickListener) {
        this.originalVideoList = videoList;
        this.videoList = new ArrayList<>();
        this.videoList.addAll(videoList);
        this.onRowClickListener = onRowClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_video_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        StoredVideoEntity video = videoList.get(position);
        holder.titleText.setText(video.getVideoTitle());
        holder.descriptionText.setText(video.getDescription());

        List<VideoLanguageEntity> langs = video.getVideoLanguages();
        String langsString = "";

        for (VideoLanguageEntity lang : langs){
            if (!langsString.equalsIgnoreCase("")){
                langsString += ", ";
            }
            langsString += lang.getLanguageName();
        }
        langsString = "Languages: " + langsString;
        holder.languagesText.setText(langsString);

        if (video.getThumbnailFileName() != null && !video.getThumbnailFileName().equalsIgnoreCase("")) {
            Bitmap thumbnailBitmap = BitmapFactory.decodeFile(holder.currentView.getContext().getExternalFilesDir(null).getAbsolutePath() + "/" + video.getThumbnailFileName());
            holder.videoThumbnail.setImageBitmap(thumbnailBitmap);
        }
        holder.itemView.setOnClickListener(v -> {
            onRowClickListener.onRowClicked(video.getUid(), video.getFileName(), video.getVideoLanguages());
        });

    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public interface OnRowClickListener {
        void onRowClicked(String uid, String fileName, List<VideoLanguageEntity> languages);
    }
}
