package com.example.botradio;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.gson.Gson;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import pl.droidsonroids.gif.GifImageView;




public class MainActivity extends AppCompatActivity {
    private MediaService musicSrv;
    private Intent playIntent;
    private boolean musicBound=false;

    ImageButton image128, image320, imageVk, imageFb, imageDonate, imageFooter;
    GifImageView imagePlay;
    RelativeLayout relativeBody;
    ImageView imageMusic;
    GifImageView imagePlayed;
    ScrollView scrollView;
    TextView nameSong, nameArtist, name1, name2, name3, name4, name5;
    MediaService mediaService = new MediaService();
    boolean bit=true, flag=true;
    final Song[] data = {new Song()};
    final ArrayList<String> urls=new ArrayList<String>();

    //connect to the service
    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MediaService.MusicBinder binder = (MediaService.MusicBinder)service;
            //get service
            musicSrv = binder.getService();
            musicSrv.setUrl("https://bot-radio.com/bot320");
            //pass list
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        if(playIntent==null){
            playIntent = new Intent(this, MediaService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }


    private void goToUrl (String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

    private void json(){
        new Thread(new Runnable() {
            public void run() {
                URL url = null;
                try {
                    // Definition of the URL with the JSON-Strings
                    url = new URL("https://bot-radio.com/meta/playing.json");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                InputStreamReader reader = null;
                try {
                    // InputStreamReader is responsible to open and consume the
                    // URL
                    assert url != null;
                    reader = new InputStreamReader(url.openStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // Conversion via GSON
                assert reader != null;
                data[0] = new Gson().fromJson(reader, Song.class);
            }
        }).start();
    }

    private void txt(){
        new Thread(new Runnable(){
            public void run(){
                //to read each line
                //TextView t; //to show the result, please declare and find it inside onCreate()
                try {
                    // Create a URL for the desired page
                    URL url = new URL("https://bot-radio.com/meta/playhist.txt"); //My text file location
                    //First open the connection
                    HttpURLConnection conn=(HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(2000); // timing out in a minute

                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    //t=(TextView)findViewById(R.id.TextView1); // ideally do this in onCreate()
                    String str;
                    while ((str = in.readLine()) != null) {
                        urls.add(str);
                    }
                    in.close();
                } catch (Exception e) {
                    Log.d("MyTag",e.toString());
                }
            }
        }).start();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        imagePlay=findViewById(R.id.imagePlay);
        image128=findViewById(R.id.image128);
        image320=findViewById(R.id.image320);
        imageVk=findViewById(R.id.imageVk);
        imageFb=findViewById(R.id.imageFb);
        imageFooter=findViewById(R.id.imageFooter);
        imagePlayed=findViewById(R.id.imagePlayed);
        imageDonate = findViewById(R.id.imageDonate);
        nameSong=findViewById(R.id.nameSong);
        name1=findViewById(R.id.name1);
        name2=findViewById(R.id.name2);
        name3=findViewById(R.id.name3);
        name4=findViewById(R.id.name4);
        name5=findViewById(R.id.name5);
        nameArtist=findViewById(R.id.nameArtist);
        imageMusic=findViewById(R.id.imageMusic);
        relativeBody=findViewById(R.id.relativeBody);
        scrollView=findViewById(R.id.scroll);



        imageVk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToUrl("https://vk.com/botradio");
            }
        });
        imageFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToUrl("https://web.facebook.com/b0tradio?_rdc=1&_rdr");
            }
        });
        imageDonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToUrl("https://bot-radio.com");
            }
        });



        image128.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bit){
                    image128.setBackgroundResource(R.drawable.minkb_max);
                    image320.setBackgroundResource(R.drawable.maxkb_min);
                    imagePlay.setImageResource(R.drawable.play);
                    musicSrv.stopSong();
                    musicSrv.setUrl("https://bot-radio.com/bot128");
                    bit=false;
                }
            }
        });
        image320.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!bit){
                    image128.setBackgroundResource(R.drawable.minkb_min);
                    image320.setBackgroundResource(R.drawable.maxkb_max);
                    imagePlay.setImageResource(R.drawable.play);
                    musicSrv.stopSong();
                    musicSrv.setUrl("https://bot-radio.com/bot320");
                    bit=true;
                }
            }
        });


        onStart();
        imagePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (musicSrv.isPlayed!=2) musicSrv.playSong();
                if (musicSrv.isPlayed==2) {
                    musicSrv.stopSong();
                }

                if(musicSrv.isPlayed==1){
                    imagePlay.setImageResource(R.drawable.circle);
                }

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable(){
                    public void run(){
                        if (musicSrv.isPlayed==2) {
                            imagePlay.setImageResource(R.drawable.stop);
                        }
                        handler.postDelayed(this, 1000);
                    }
                }, 1000);

                if (musicSrv.isPlayed==0)  imagePlay.setImageResource(R.drawable.play);

            }
        });



        imageFooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imagePlayed.getVisibility()==View.VISIBLE)
                {
                    imagePlayed.setVisibility(View.INVISIBLE);
                    imagePlay.setVisibility(View.VISIBLE);
                    scrollView.setVisibility(View.INVISIBLE);
                    name1.setVisibility(View.INVISIBLE);
                    name2.setVisibility(View.INVISIBLE);
                    name3.setVisibility(View.INVISIBLE);
                    name4.setVisibility(View.INVISIBLE);
                    name5.setVisibility(View.INVISIBLE);
                }
                else
                {
                    imagePlayed.setVisibility(View.VISIBLE);
                    imagePlay.setVisibility(View.INVISIBLE);
                    scrollView.setVisibility(View.VISIBLE);
                    name1.setVisibility(View.VISIBLE);
                    name2.setVisibility(View.VISIBLE);
                    name3.setVisibility(View.VISIBLE);
                    name4.setVisibility(View.VISIBLE);
                    name5.setVisibility(View.VISIBLE);
                }
            }
        });

        Picasso.get().load("https://bot-radio.com/meta/cover.jpg").memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).fit().centerCrop().into(imageMusic);


        json();
        txt();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            public void run(){
                String temp= data[0].getTitle();
                json();
                txt();
                SystemClock.sleep(1000);
                if(temp.equals(data[0].getTitle())){

                }
                else {
                    Picasso.get().load("https://bot-radio.com/meta/cover.jpg").memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).fit().centerCrop().into(imageMusic);
                    name1.setText(urls.get(0));
                    name2.setText(urls.get(1));
                    name3.setText(urls.get(2));
                    name4.setText(urls.get(3));
                    name5.setText(urls.get(4));
                    nameSong.setText(data[0].getTitle());
                    nameArtist.setText(data[0].getArtist());
                }
                handler.postDelayed(this, 10000);
            }
        }, 10000);



        SystemClock.sleep(5000);
        name1.setText(urls.get(0));
        name2.setText(urls.get(1));
        name3.setText(urls.get(2));
        name4.setText(urls.get(3));
        name5.setText(urls.get(4));
        nameSong.setText(data[0].getTitle());
        nameArtist.setText(data[0].getArtist());
/*
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification);
        remoteViews.setTextViewText(R.id.textView, "Custom notification text");
      //  remoteViews.setOnClickPendingIntent(R.id.root, rootPendingIntent);

      //  RemoteViews remoteViewsExtended = new RemoteViews(getPackageName(), R.layout.extended_notification);
       // remoteViewsExtended.setTextViewText(R.id.textView, "Extended custom notification text");
      //  remoteViewsExtended.setOnClickPendingIntent(R.id.root, rootPendingIntent);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setCustomContentView(remoteViews)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle());

        Notification notification = builder.build();

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);

*/




    }






}


