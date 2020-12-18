package com.golabiusz.scrollingshooter;

import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import com.golabiusz.scrollingshooter.gameobject.GameObject;
import java.util.ArrayList;

public class GameEngine extends SurfaceView
    implements Runnable, GameStarter, GameEngineBroadcaster, PlayerLaserSpawner {

  private Thread thread = null;
  private long fps;

  private final ArrayList<InputObserver> inputObservers = new ArrayList<>();

  private final UIController uiController;
  private final GameState gameState;
  private final SoundEngine soundEngine;
  private final HUD hud;
  private final Renderer renderer;
  private final PhysicsEngine physicsEngine;
  private final ParticleSystem particleSystem;
  private final Level level;

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

    level = new Level(context, new PointF(size.x, size.y), this);
  }

  public void addObserver(InputObserver observer) {
    inputObservers.add(observer);
  }

  @Override
  public void run() {
    while (gameState.isThreadRunning()) {
      long frameStartTime = System.currentTimeMillis();
      ArrayList<GameObject> objects = level.getGameObjects();

      if (!gameState.isPaused()) {
        if (physicsEngine.update(fps, objects, gameState, soundEngine, particleSystem)) {
          deSpawnReSpawn();
        }
      }

      renderer.draw(objects, gameState, hud, particleSystem);

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

  @Override
  public boolean spawnPlayerLaser(Transform transform) {
    ArrayList<GameObject> objects = level.getGameObjects();

    if (objects.get(Level.nextPlayerLaser).spawn(transform)) {
      soundEngine.playShoot();

      ++Level.nextPlayerLaser;
      if (Level.nextPlayerLaser > Level.LAST_PLAYER_LASER) {
        Level.nextPlayerLaser = Level.FIRST_PLAYER_LASER;
      }
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
    ArrayList<GameObject> objects = level.getGameObjects();

    for (GameObject object : objects) {
      object.setInactive();
    }

    objects.get(Level.PLAYER_INDEX).spawn(objects.get(Level.PLAYER_INDEX).getTransform());
    objects.get(Level.BACKGROUND_INDEX).spawn(objects.get(Level.PLAYER_INDEX).getTransform());
  }
}
