package com.golabiusz.scrollingshooter;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;
import java.io.IOException;
import org.jetbrains.annotations.NotNull;

class SoundEngine {

  private SoundPool soundPool;

  private int shootSoundID = -1;
  private int alienExplodeSoundID = -1;
  private int playerExplodeSoundID = -1;

  SoundEngine(Context context) {
    buildSoundPool();
    loadSounds(context);
  }

  private void buildSoundPool() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      AudioAttributes audioAttributes =
          new AudioAttributes.Builder()
              .setUsage(AudioAttributes.USAGE_MEDIA)
              .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
              .build();

      soundPool =
          new SoundPool.Builder().setMaxStreams(5).setAudioAttributes(audioAttributes).build();
    } else {
      soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
    }
  }

  private void loadSounds(@NotNull Context context) {
    try {
      AssetManager assetManager = context.getAssets();
      AssetFileDescriptor descriptor;

      descriptor = assetManager.openFd("shoot.ogg");
      shootSoundID = soundPool.load(descriptor, 0);

      descriptor = assetManager.openFd("alien_explosion.ogg");
      alienExplodeSoundID = soundPool.load(descriptor, 0);

      descriptor = assetManager.openFd("player_explosion.ogg");
      playerExplodeSoundID = soundPool.load(descriptor, 0);
    } catch (IOException e) {
      Log.e("error", "failed to load sound files");
    }
  }

  void playShoot() {
    soundPool.play(shootSoundID, 1, 1, 0, 0, 1);
  }

  void playAlienExplode() {
    soundPool.play(alienExplodeSoundID, 1, 1, 0, 0, 1);
  }

  void playPlayerExplode() {
    soundPool.play(playerExplodeSoundID, 1, 1, 0, 0, 1);
  }
}
