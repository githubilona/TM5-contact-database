package com.example.lab555.tasks;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class LoadImageTask extends AsyncTask<String,Integer,String> {
    private ProgressBar progressBar;
    private ImageView imageView;

    public LoadImageTask(ProgressBar progressBar, ImageView imageView) {
        this.progressBar = progressBar;
        this.imageView = imageView;
    }
    @Override
    protected String doInBackground(String... params) {
        int count;
        //dajemy nazwe pliku na sztywno, lecz w porządnej aplikacji powinna być ona generowana
        File yourFile = new
                File(Environment.getExternalStorageDirectory().getAbsoluteFile(),"/2011.jpg"
        );
        try {
            URL url = new URL(params[0]);
            URLConnection conection = url.openConnection();
            conection.connect();
            //sprawdzamy długość pliku w celu ustalenia dzielnika
            int lenghtOfFile = conection.getContentLength();
            //pobieranie pliku
            InputStream input = new BufferedInputStream(url.openStream(), 8192);
            //tworzenie pliku jeśli nie istnieje
            if(!yourFile.exists()) {
                yourFile.createNewFile();
            }
            FileOutputStream oFile = new FileOutputStream(yourFile, false);
            OutputStream output = oFile;
            byte data[] = new byte[1024];
            long total = 0;
            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                // After this onProgressUpdate will be called
                publishProgress((int) ((total * 100) / lenghtOfFile));
                // writing data to file
                output.write(data, 0, count);
            }
            //czyszczeie i zamykanie strumieni
            output.flush();
            output.close();
            input.close();
        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }
        return yourFile.getAbsolutePath();
    }
    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
        progressBar.setProgress(progress[0]);
    }
    @Override
    protected void onPostExecute(String file_url) {
        imageView.setImageURI(Uri.parse(file_url));
    }


}
