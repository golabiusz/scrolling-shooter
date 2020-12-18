package com.golabiusz.scrollingshooter;

import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import java.util.ArrayList;

class GameEngine extends SurfaceView implements Runnable, GameStarter, GameEngineBroadcaster {

  private Thread thread = null;
  private long fps;

  private ArrayList<InputObserver> inputObservers = new ArrayList();

  private UIController uiController;
  private GameState gameState;
  private SoundEngine soundEngine;
  private HUD hud;
  private Renderer renderer;
  private PhysicsEngine physicsEngine;
  private ParticleSystem particleSystem;

  public GameEngine(Context context, Point size) {
    super(context);

    uiController = new UIController(this);
    gameState = new GameState(this, context);
    soundEngine = new SoundEngine(context);
    hud = new HUD(size);
    renderer = new Renderer(this);
    physicsEngine = new PhysicsEngine();

    particleSystem = new ParticleSystem();
    particleSystem.init(100);
  }

  public void addObserver(InputObserver observer) {
    inputObservers.add(observer);
  }

  @Override
  public void run() {
    while (gameState.isThreadRunning()) {
      long frameStartTime = System.currentTimeMillis();

      if (!gameState.isPaused()) {
        // This call to update will evolve with the project
        if (physicsEngine.update(fps, particleSystem)) {
          deSpawnReSpawn();
        }
      }

      renderer.draw(gameState, hud, particleSystem);

      long timeThisFrame = System.currentTimeMillis() - frameStartTime;
      if (timeThisFrame >= 1) {
        final int MILLIS_IN_SECOND = 1000;
        fps = MILLIS_IN_SECOND / timeThisFrame;
      }
    }
  }

  @Override
  public boolean onTouchEvent(MotionEvent motionEvent) {
    for (InputObserver observer : inputObservers) {
      observer.handleInput(motionEvent, gameState, hud.getControls());
    }

    return true;
  }

  void stopThread() {
    gameState.stopEverything();

    try {
      thread.join();
    } catch (InterruptedException e) {
      Log.e("Exception", "stopThread()" + e.getMessage());
    }
  }

  void startThread() {
    gameState.startThread();

    thread = new Thread(this);
    thread.start();
  }

  public void deSpawnReSpawn() {
    // Eventually this will despawn
    // and then respawn all the game objects
  }
}
