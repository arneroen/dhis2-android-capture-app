package org.dhis2.usescases.videoLibrary;

import androidx.annotation.NonNull;

import org.hisp.dhis.android.core.D2;

import io.reactivex.disposables.CompositeDisposable;

public class VideoPresenterImpl implements VideoContracts.VideoPresenter {

    private final D2 d2;
    private CompositeDisposable compositeDisposable;

    VideoPresenterImpl(@NonNull D2 d2) {
        this.d2 = d2;
    }

    @Override
    public void init(VideoContracts.VideoView view) {
        compositeDisposable = new CompositeDisposable();

    }

    @Override
    public void onPause() {
        compositeDisposable.clear();
    }
}
