package org.dhis2.usescases.videoLibrary;

import android.widget.Filter;

import org.dhis2.data.videoDatabase.entities.StoredVideoEntity;

import java.util.ArrayList;
import java.util.List;

public class VideoSearchFilter extends Filter {

    private final VideoListAdapter adapter;
    private final List<StoredVideoEntity> originalList;
    private final List<StoredVideoEntity> filteredList;

    public VideoSearchFilter(VideoListAdapter adapter, List<StoredVideoEntity> originalList) {
        super();
        this.adapter = adapter;
        this.originalList = originalList;
        this.filteredList = new ArrayList<>();
    }
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        filteredList.clear();
        final FilterResults results = new FilterResults();

        if (constraint.length() == 0) {
            filteredList.addAll(originalList);
        }
        else {
            final String filterPattern = constraint.toString().toLowerCase().trim();
            for (StoredVideoEntity videoEntity : originalList) {
                if (videoEntity.getVideoTitle().toLowerCase().contains(filterPattern)) {
                    filteredList.add(videoEntity);
                }
            }
        }
        results.values = filteredList;
        results.count = filteredList.size();

        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.videoList.clear();
        adapter.videoList.addAll((List<StoredVideoEntity>) results.values);
        adapter.notifyDataSetChanged();



    }
}
