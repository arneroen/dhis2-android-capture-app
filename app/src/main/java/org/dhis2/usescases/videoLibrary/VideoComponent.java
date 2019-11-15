package org.dhis2.usescases.videoLibrary;

import org.dhis2.data.dagger.PerFragment;
import dagger.Subcomponent;

@PerFragment
@Subcomponent(modules = VideoModule.class)
public interface VideoComponent {
    void inject(VideoFragment programFragment);
}
