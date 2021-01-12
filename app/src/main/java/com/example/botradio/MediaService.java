package com.example.botradio;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.session.MediaSession;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.widget.MediaController;

import androidx.annotation.Nullable;
import androidx.media.MediaSessionManager;


public class MediaService extends Service implements
		MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
		MediaPlayer.OnCompletionListener
{
	//media player
	private MediaPlayer player;
	String url;
	int isPlayed=0;

	private final IBinder musicBind = new MusicBinder();

	@Override
	public void onCreate() {
		//create the service
		super.onCreate();

		//create player
		player = new MediaPlayer();
		initMusicPlayer();
	}

	public void initMusicPlayer(){
		//set player properties
		player.setWakeMode(getApplicationContext(),
				PowerManager.PARTIAL_WAKE_LOCK);
		player.setAudioStreamType(AudioManager.STREAM_MUSIC);
		player.setOnPreparedListener(this);
		player.setOnCompletionListener(this);
		player.setOnErrorListener(this);


	}

	public void setUrl(String theUrl){
		url=theUrl;
	}

	public void playSong(){
		player.reset();
		try{
			player.setDataSource(url);
		}
		catch(Exception e){
			Log.e("MUSIC SERVICE", "Error setting data source", e);
		}

		player.prepareAsync();
		isPlayed=1;
	}

	public void stopSong(){
		player.stop();
		isPlayed=0;
	}

	public class MusicBinder extends Binder {
		MediaService getService() {
			return MediaService.this;
		}
	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return musicBind;
	}

	@Override
	public boolean onUnbind(Intent intent){
		player.stop();
		player.release();
		return false;
	}



	@Override
	public void onCompletion(MediaPlayer mp) {
		player=null;
		player = new MediaPlayer();
		initMusicPlayer();
		playSong();

	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		player=null;
		player = new MediaPlayer();
		initMusicPlayer();
		playSong();
		return false;
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		mp.start();
		isPlayed=2;
	}
}