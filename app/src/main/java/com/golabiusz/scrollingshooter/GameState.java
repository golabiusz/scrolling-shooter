
package com.golabiusz.scrollingshooter;

import android.content.Context;
import android.content.SharedPreferences;
import org.jetbrains.annotations.NotNull;

public final class GameState {

  private static volatile boolean isThreadRunning = false;
  private static volatile boolean isPaused = true;
  private static volatile boolean isGameOver = true;
  private static volatile boolean isDrawing = false;

  // This object will have access to the deSpawnReSpawn method in GameEngine
  private final GameStarter gameStarter;
  private final SoundEngine soundEngine;

  private int score;
  private int highScore;
  private int numLives;

  // This is how we will make all the high scores persist
  private final SharedPreferences.Editor editor;

  GameState(GameStarter gameStarter, SoundEngine soundEngine, @NotNull Context context) {
    this.gameStarter = gameStarter;
    this.soundEngine = soundEngine;

    // Get the current high score
    SharedPreferences prefs;
    prefs = context.getSharedPreferences("HiScore", Context.MODE_PRIVATE);

    // Initialize the editor ready
    editor = prefs.edit();

    // Load high score from a entry in the file labeled "hiscore"
    // if not available highScore set to zero 0
    highScore = prefs.getInt("hi_score", 0);
  }

  void startNewGame() {
    score = 0;
    numLives = 3;
    // Don't want to be drawing objects
    // while deSpawnReSpawn is clearing them and spawning them again
    stopDrawing();
    gameStarter.deSpawnReSpawn();
    resume();

    // Now we can draw again
    startDrawing();
  }

  public void loseLife() {
    numLives--;
    soundEngine.playPlayerExplode();

    if (numLives == 0) {
      pause();
      endGame();
    }
  }

  private void endGame() {
    isGameOver = true;
    isPaused = true;
    if (score > highScore) {
      highScore = score;

      editor.putInt("hi_score", highScore);
      editor.commit();
    }
  }

  boolean isThreadRunning() {
    return isThreadRunning;
  }

  void startThread() {
    isThreadRunning = true;
  }

  boolean isPaused() {
    return isPaused;
  }

  void pause() {
    isPaused = true;
  }

  void resume() {
    isGameOver = false;
    isPaused = false;
  }

  boolean isGameOver() {
    return isGameOver;
  }

  boolean isDrawing() {
    return isDrawing;
  }

  private void stopDrawing() {
    isDrawing = false;
  }

  private void startDrawing() {
    isDrawing = true;
  }

  void stopEverything() {
    isPaused = true;
    isGameOver = true;
    isThreadRunning = false;
  }

  int getScore() {
    return score;
  }

  public void increaseScore() {
    score++;
    soundEngine.playAlienExplode();
  }

  int getHighScore() {
    return highScore;
  }

  int getNumLives() {
    return numLives;
  }
}
