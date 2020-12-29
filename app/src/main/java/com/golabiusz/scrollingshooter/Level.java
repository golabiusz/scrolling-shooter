package com.golabiusz.scrollingshooter;

import android.content.Context;
import android.graphics.PointF;

import com.golabiusz.scrollingshooter.gameobject.AlienChaseSpec;
import com.golabiusz.scrollingshooter.gameobject.AlienDiverSpec;
import com.golabiusz.scrollingshooter.gameobject.AlienLaserSpec;
import com.golabiusz.scrollingshooter.gameobject.AlienPatrolSpec;
import com.golabiusz.scrollingshooter.gameobject.BackgroundSpec;
import com.golabiusz.scrollingshooter.gameobject.GameObject;
import com.golabiusz.scrollingshooter.gameobject.GameObjectFactory;
import com.golabiusz.scrollingshooter.gameobject.PlayerLaserSpec;
import com.golabiusz.scrollingshooter.gameobject.PlayerSpec;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

class Level {

  // Keep track of specific types
  public static final int BACKGROUND_INDEX = 0;
  public static final int PLAYER_INDEX = 1;
  public static final int FIRST_PLAYER_LASER = 2;
  public static final int LAST_PLAYER_LASER = 4;
  public static int nextPlayerLaser;
  public static final int FIRST_ALIEN = 5;
  public static final int SECOND_ALIEN = 6;
  public static final int THIRD_ALIEN = 7;
  public static final int FOURTH_ALIEN = 8;
  public static final int FIFTH_ALIEN = 9;
  public static final int SIXTH_ALIEN = 10;
  public static final int LAST_ALIEN = 10;
  public static final int FIRST_ALIEN_LASER = 11;
  public static final int LAST_ALIEN_LASER = 15;
  public static int nextAlienLaser;

  private final ArrayList<GameObject> objects;

  public Level(Context context, PointF screenSize, GameEngine gameEngine) {
    objects = new ArrayList<>();

    GameObjectFactory factory = new GameObjectFactory(context, screenSize, gameEngine);
    buildGameObjects(factory);
  }

  private void buildGameObjects(@NotNull GameObjectFactory factory) {
    objects.clear();

    objects.add(BACKGROUND_INDEX, factory.create(new BackgroundSpec()));

    createPlayer(factory);
    createPlayerLasers(factory);

    createAliens(factory);
    createAliensLasers(factory);
  }

  private void createPlayer(@NotNull GameObjectFactory factory) {
    objects.add(PLAYER_INDEX, factory.create(new PlayerSpec()));
  }

  private void createPlayerLasers(GameObjectFactory factory) {
    for (int i = FIRST_PLAYER_LASER; i != LAST_PLAYER_LASER + 1; i++) {
      objects.add(i, factory.create(new PlayerLaserSpec()));
    }
    nextPlayerLaser = FIRST_PLAYER_LASER;
  }

  private void createAliens(@NotNull GameObjectFactory factory) {
    objects.add(FIRST_ALIEN, factory.create(new AlienChaseSpec()));
    objects.add(SECOND_ALIEN, factory.create(new AlienPatrolSpec()));
    objects.add(THIRD_ALIEN, factory.create(new AlienPatrolSpec()));
    objects.add(FOURTH_ALIEN, factory.create(new AlienChaseSpec()));
    objects.add(FIFTH_ALIEN, factory.create(new AlienDiverSpec()));
    objects.add(SIXTH_ALIEN, factory.create(new AlienDiverSpec()));
  }

  private void createAliensLasers(GameObjectFactory factory) {
    for (int i = FIRST_ALIEN_LASER; i < LAST_ALIEN_LASER + 1; i++) {
      objects.add(i, factory.create(new AlienLaserSpec()));
    }
    nextAlienLaser = FIRST_ALIEN_LASER;
  }

  ArrayList<GameObject> getGameObjects() {
    return objects;
  }
}
