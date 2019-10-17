package com.example.translateapp;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import 	java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import 	java.nio.charset.Charset;
import android.util.Log;
import android.widget.TextView;
import 	java.net.URL;
import java.net.URLConnection;
import 	java.io.DataInputStream;
import androidx.appcompat.app.AppCompatActivity;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Locale;
import android.os.AsyncTask;
import android.net.Uri;
import 	android.app.DownloadManager;
import 	android.content.Context;
import android.os.Environment;
import android.os.Build;
import android.content.pm.PackageManager;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import	java.io.File;
import java.io.FileInputStream;
import android.net.Uri;
import android.view.ViewGroup;

public class MainActivity extends AppCompatActivity {

    public static boolean hasPermissions(Context context, String... permissions) {
        //Check permissions
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public  boolean setupPermissions() {
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                android.Manifest.permission.ACCESS_WIFI_STATE,
                android.Manifest.permission.INTERNET,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        //Request Permissions if they are not present
        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //check and request for permissions
        setupPermissions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){

            //you have the permission now.
            DownloadManager downloadmanager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            Uri Download_Uri = Uri.parse("https://srv-file7.gofile.io/download/rsRoAz/Locale.csv");
            DownloadManager.Request request = new DownloadManager.Request(Download_Uri);

            //Restrict the types of networks over which this download may proceed.
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);

            //Set whether this download may proceed over a roaming connection.
            request.setAllowedOverRoaming(false);

            //Set the title of this download, to be displayed in notifications (if enabled).
            request.setTitle("Locale");

            //Set a description of this download, to be displayed in notifications (if enabled)
            request.setDescription("Downloading File");
            request.setVisibleInDownloadsUi(true);

            //Set the local destination for the downloaded file to a path within the application's external files directory
            File root = new File(Environment.getExternalStorageDirectory() + "/Download");
            Uri path = Uri.withAppendedPath(Uri.fromFile(root), "Locale");
            request.setDestinationUri(path);

            //Enqueue a new download and same the referenceId
            downloadmanager.enqueue(request);
        }
    }

    private String getStringResourceByName(String aString) {
        //Get string resource id from its name
        String packageName = getPackageName();
        int resId = getResources().getIdentifier(aString, "string", packageName);
        return getString(resId);
    }

    /* Find text views from all the views */
    public void findViews(View viewInstance, String buttonText) {
        try {
            if (viewInstance instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) viewInstance;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    // recursively call this method
                    findViews(child, buttonText);
                }
            }
            else if (viewInstance instanceof Button) {
            }
            else if (viewInstance instanceof TextView) {

                //Find the text id from csv file for each view, and get the localised strings
                TextView tv = (TextView) viewInstance;
                String tvId = getResources().getResourceEntryName(viewInstance.getId());

                String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download";
                File fileLocale = new File(path + "/Locale");
                if(fileLocale.exists())
                {
                    Uri uri = Uri.fromFile(fileLocale);
                    InputStream is = getContentResolver().openInputStream(uri);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                    //Every single line in csv file
                    String line = "";
                    //Language button selected index in csv
                    int langIndex = -1;

                    try {

                        //Identify the language coloumn from csv
                        if((line = reader.readLine()) != null)
                        {
                            String[] tokens = line.split(",");

                            for(int i=0; i<tokens.length; i++)
                            {
                                if (tokens[i].equals(buttonText))
                                {
                                    langIndex = i;
                                    break;
                                }
                            }
                        }

                        //Identify text id from csv
                        while ((line = reader.readLine()) != null) {
                            // Split the line into different tokens (using the comma as a separator).
                            String[] tokens = line.split(",");

                            //Check the first coloumn for string id
                            if (tokens[0].equals(tvId))
                            {
                                //Select the coloumn with the language id determined before
                                String translateText = tokens[langIndex].toString();
                                tv.setText(translateText);
                                break;
                            }
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* Function called when any of the buttons are selected */
    public void onClickBtn(View viewInstance) throws FileNotFoundException
    {
        //Get Button Text
        Button b = (Button)viewInstance;
        String buttonText = b.getText().toString();

        //Change the language to load all strings
        Locale locale = new Locale(getStringResourceByName(buttonText));
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        this.setContentView(R.layout.activity_main);

        //Find text from csv file, and over ride the values for text views
        View viewGroup = ( (ViewGroup) findViewById(android.R.id.content));
        findViews(viewGroup, buttonText);
    }
}

