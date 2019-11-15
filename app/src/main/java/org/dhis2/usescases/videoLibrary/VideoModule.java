package org.dhis2.usescases.videoLibrary;

import androidx.annotation.NonNull;

import org.dhis2.data.dagger.PerFragment;
import org.hisp.dhis.android.core.D2;

import dagger.Module;
import dagger.Provides;


@Module
public class VideoModule {

    @Provides
    @PerFragment
    VideoContracts.VideoPresenter providesPresenter(@NonNull D2 d2) {
        return new VideoPresenterImpl(d2);
    }
}
