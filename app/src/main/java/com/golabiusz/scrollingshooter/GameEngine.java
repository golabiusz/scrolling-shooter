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
    implements Runnable, GameStarter, GameEngineBroadcaster, PlayerLaserSpawner, AlienLaserSpawner {

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
    soundEngine = new SoundEngine(context);
    gameState = new GameState(this, soundEngine, context);
    hud = new HUD(size);
    renderer = new Renderer(this);

    particleSystem = new ParticleSystem();
    particleSystem.init(100);
    physicsEngine = new PhysicsEngine(particleSystem);

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
        if (physicsEngine.update(fps, objects, gameState)) {
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

  @Override
  public void spawnAlienLaser(Transform transform) {
    ArrayList<GameObject> objects = level.getGameObjects();

    if (objects.get(Level.nextAlienLaser).spawn(transform)) {
      soundEngine.playShoot();

      ++Level.nextAlienLaser;
      if (Level.nextAlienLaser == Level.LAST_ALIEN_LASER + 1) {
        Level.nextAlienLaser = Level.FIRST_ALIEN_LASER;
      }
    }
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

    for (int i = Level.FIRST_ALIEN; i < Level.LAST_ALIEN + 1; ++i) {
      objects.get(i).spawn(objects.get(Level.PLAYER_INDEX).getTransform());
    }
  }
}
