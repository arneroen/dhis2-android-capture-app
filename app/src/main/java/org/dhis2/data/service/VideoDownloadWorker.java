package org.dhis2.data.service;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.dhis2.data.videoDatabase.VideoDatabaseClient;
import org.dhis2.data.videoDatabase.entities.StoredVideoEntity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

public class VideoDownloadWorker extends Worker {
    /*@Inject
    SyncPresenter presenter;*/


    public VideoDownloadWorker(
            @NonNull Context context,
            @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork(){
        String server = "http://192.168.12.1:8081";

        try {
            URL videoListUrl = new URL(server + "/videos");
            List<StoredVideoEntity> videos = getAllVideosToDownload(videoListUrl);
            List<StoredVideoEntity> videosToDownload = getVideosToDownload(videos);
            downloadVideos(server, videosToDownload);
            return Result.success();

        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return Result.failure();
    }

    private void downloadVideo(URL url, String fileName) throws IOException {
        //create the new connection
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        //set up some things on the connection
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("Accept","*/*");
        urlConnection.setRequestProperty ("User-agent", "mozilla");
        urlConnection.setDoOutput(false);

        //and connect!
        urlConnection.connect();

        File file = new File(getApplicationContext().getExternalFilesDir(null), fileName);

        //this will be used to write the downloaded data into the file we created
        FileOutputStream fileOutput = new FileOutputStream(file);

        //this will be used in reading the data from the internet
        InputStream inputStream = urlConnection.getInputStream();

        //this is the total size of the file
        int totalSize = urlConnection.getContentLength();
        //variable to store total downloaded bytes
        int downloadedSize = 0;

        //create a buffer...
        byte[] buffer = new byte[1024];
        int bufferLength = 0; //used to store a temporary size of the buffer

        //now, read through the input buffer and write the contents to the file
        while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
            //add the data in the buffer to the file in the file output stream (the file on the sd card
            fileOutput.write(buffer, 0, bufferLength);
            //add up the size so we know how much is downloaded
            downloadedSize += bufferLength;
            //this is where you would do something to report the prgress, like this maybe
            //updateProgress(downloadedSize, totalSize);

        }
        //close the output stream when done
        fileOutput.close();
    }

    private StoredVideoEntity getVideoInfo(URL url, String uid) throws IOException {
        InputStream is = url.openStream();
        Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8);

        Gson gson = new Gson();
        StoredVideoEntity storedVideoEntity = gson.fromJson(reader, StoredVideoEntity.class);

        storedVideoEntity.setUid(uid);

        String[] fileNameSplit = storedVideoEntity.getFileName().split("\\.");
        String fileName = uid + "." + fileNameSplit[fileNameSplit.length -1];
        storedVideoEntity.setFileName(fileName);

        return storedVideoEntity;
    }

    private List<StoredVideoEntity> getAllVideosToDownload(URL url) throws IOException {
        InputStream is = url.openStream();
        Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
        Gson gson = new Gson();
        Type listType = new TypeToken<List<StoredVideoEntity>>(){}.getType();
        List<StoredVideoEntity> videos = gson.fromJson(reader, listType);
        return videos;
    }

    private List<StoredVideoEntity> getVideosToDownload(List<StoredVideoEntity> videos) throws ExecutionException, InterruptedException {
        List<StoredVideoEntity> storedVideos = (List<StoredVideoEntity>) new VideoDatabaseClient(getApplicationContext(), 0).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();

        List<StoredVideoEntity> videosToDownload = new ArrayList<>();

        for (StoredVideoEntity video : videos) {
            if (!storedVideos.contains(video)){
                videosToDownload.add(video);
            }
        }
        return videosToDownload;
    }

    private void downloadVideos(String server, List<StoredVideoEntity> videos) throws IOException {
        for (StoredVideoEntity video : videos) {
            URL url = new URL(server + "/video/" + video.getUid());

            String[] fileNameSplit = video.getFileName().split("\\.");
            String fileName = video.getUid() + "." + fileNameSplit[fileNameSplit.length -1];
            video.setFileName(fileName);

            String[] thumbnailFileNameSplit = video.getThumbnailFileName().split("\\.");
            String thumbnailFileName = video.getUid() + "." + thumbnailFileNameSplit[thumbnailFileNameSplit.length -1];
            video.setThumbnailFileName(thumbnailFileName);

            downloadVideo(url, video.getFileName());
            downloadThumbnail(server, video);


            new VideoDatabaseClient(getApplicationContext(), 1).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, video);
        }
    }
    private void downloadThumbnail(String server, StoredVideoEntity video) throws IOException {
        URL url = new URL(server + "/thumbnail/" + video.getUid());
        downloadVideo(url, video.getThumbnailFileName());
    }
}

