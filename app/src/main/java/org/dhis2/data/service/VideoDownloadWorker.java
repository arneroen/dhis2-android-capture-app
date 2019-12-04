package org.dhis2.data.service;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

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
        try {
            //set the download URL, a url that points to a file on the internet
            //this is the file to be downloaded
            URL url = new URL("http://arneroen.no:8081/videos/finished1.mp4");
            //URL url = new URL("http://arneroen.no:8081/");

            //create the new connection
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            //set up some things on the connection
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Accept","*/*");
            urlConnection.setRequestProperty ("User-agent", "mozilla");
            urlConnection.setDoOutput(false);

            //and connect!
            urlConnection.connect();

            int response = urlConnection.getResponseCode();
            InputStream error = urlConnection.getErrorStream();

            //set the path where we want to save the file
            //in this case, going to save it on the root directory of the
            //sd card.
            File SDCardRoot = Environment.getExternalStorageDirectory();
            //create a new file, specifying the path, and the filename
            //which we want to save the file as.
            //File file = new File(SDCardRoot,"somefile.ext");

            //File file = new File(getApplicationContext().getFilesDir(), "test7.mp4");
            File file = new File(getApplicationContext().getExternalFilesDir(null), "test7.mp4");

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
            return Result.success();


//catch some possible errors...
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return Result.failure();
        } catch (IOException e) {
            e.printStackTrace();
            return Result.failure();
        } catch(Exception e) {
            e.printStackTrace();
            return Result.failure();
        }
        /*try {
            Log.i("WORKERSTUFF", "WE ARE IN THE WORKER!");
            //URL u = new URL("http://arneroen.no:8081/video/video3");
            URL u = new URL("http://arneroen.no:8081/videos/finished1.mp4");
            URLConnection c = u.openConnection();

            c.connect();

            InputStream inStream = c.getInputStream();

            int test = inStream.available();

            byte[] buffer = new byte[inStream.available()];
            inStream.read(buffer);

            String state = Environment.getExternalStorageState();
            if (!Environment.MEDIA_MOUNTED.equals(state)){
                return Result.failure();
            }

            //File file = new File(getApplicationContext().getExternalFilesDir(null), "test7.mp4");
            File file = new File(getApplicationContext().getFilesDir(), "test7.mp4");
            FileOutputStream outputStream = null;

            file.createNewFile();
            outputStream = new FileOutputStream(file, false);
            outputStream.write(buffer);
            outputStream.close();
            inStream.close();

            File dir = getApplicationContext().getExternalFilesDir(null);

            Log.i("WORKER", file.getAbsolutePath());

            /*
            //file stuff
            FileOutputStream fOs = getApplicationContext().openFileOutput("test5.mp4", Context.MODE_PRIVATE);
            fOs.write(buffer);
            fOs.close();

            inStream.close();

            File dir = getApplicationContext().getFilesDir();

            Log.i("worker", dir.getAbsolutePath());*/
        /*} catch (Exception e) {
            e.printStackTrace();
        }*/

        //Log.i("DOWNLOADWORKER", "We are running the dowork");

        //return Result.success();
    }
}
