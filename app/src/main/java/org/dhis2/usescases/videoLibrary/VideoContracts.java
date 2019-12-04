package org.dhis2.usescases.videoLibrary;


public class VideoContracts {
    public interface VideoView{
        void renderVideoLibrary();
    }

    public interface VideoPresenter {
        void init(VideoView videoFragment);
        void onPause();
    }
}
