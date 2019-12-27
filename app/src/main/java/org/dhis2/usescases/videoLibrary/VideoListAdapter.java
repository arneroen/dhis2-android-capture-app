package org.dhis2.usescases.videoLibrary;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.dhis2.R;
import org.dhis2.data.videoDatabase.entities.StoredVideoEntity;

import java.util.List;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.MyViewHolder> {

    private List<StoredVideoEntity> videoList;

    private OnThumbnailClickListener onThumbnailClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView titleText, descriptionText, languagesText;
        public ImageView videoThumbnail;

        public MyViewHolder(View view){
            super(view);
            titleText = view.findViewById(R.id.video_title);
            descriptionText = view.findViewById(R.id.video_description);
            videoThumbnail = view.findViewById(R.id.video_thumbnail);
            languagesText = view.findViewById(R.id.video_languages);
        }
    }

    public VideoListAdapter(List<StoredVideoEntity> videoList, OnThumbnailClickListener onThumbnailClickListener) {
        this.videoList = videoList;
        this.onThumbnailClickListener = onThumbnailClickListener;
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

        String[] langs = video.getVideoLanguages().split(",");
        String langsString = "Languages: ";
        for (String lang : langs){
            langsString += lang + ", ";
        }
        holder.languagesText.setText(langsString);

        holder.videoThumbnail.setOnClickListener(v -> {
            onThumbnailClickListener.onThumbnailClicked(video.getUid(), video.getFileName());
        });

    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public interface OnThumbnailClickListener {
        void onThumbnailClicked(String uid, String fileName);
    }
}
