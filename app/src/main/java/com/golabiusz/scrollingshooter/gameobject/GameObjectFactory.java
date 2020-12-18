package com.golabiusz.scrollingshooter.gameobject;

import android.content.Context;
import android.graphics.PointF;
import com.golabiusz.scrollingshooter.GameEngine;
import com.golabiusz.scrollingshooter.Transform;
import org.jetbrains.annotations.NotNull;

public class GameObjectFactory {

  private final Context context;
  private final PointF screenSize;
  private final GameEngine gameEngine;

  public GameObjectFactory(Context context, PointF screenSize, GameEngine gameEngine) {
    this.context = context;
    this.screenSize = screenSize;
    this.gameEngine = gameEngine;
  }

  public GameObject create(@NotNull ObjectSpec spec) {
    GameObject object = new GameObject();

    final float HIDDEN = -2000f;

    object.setTag(spec.getTag());

    float speed = screenSize.x / spec.getSpeed();

    PointF objectSize = new PointF(
        screenSize.x / spec.getScale().x,
        screenSize.y / spec.getScale().y);

    PointF location = new PointF(HIDDEN, HIDDEN);

    object.setTransform(new Transform(speed, objectSize.x, objectSize.y, location, screenSize));

    for (int i = 0, numComponents = spec.getComponents().length; i < numComponents; i++) {
      switch (spec.getComponents()[i]) {
        case "PlayerInputComponent":
          object.setInput(new PlayerInputComponent(gameEngine));
          break;
        case "StdGraphicsComponent":
          object.setGraphics(new StdGraphicsComponent(), context, spec, objectSize);
          break;
        case "PlayerMovementComponent":
          object.setMovement(new PlayerMovementComponent());
          break;
        case "LaserMovementComponent":
          object.setMovement(new LaserMovementComponent());
          break;
        case "PlayerSpawnComponent":
          object.setSpawner(new PlayerSpawnComponent());
          break;
        case "LaserSpawnComponent":
          object.setSpawner(new LaserSpawnComponent());
          break;
        case "BackgroundGraphicsComponent":
          object.setGraphics(new BackgroundGraphicsComponent(), context, spec, objectSize);
          break;
        case "BackgroundMovementComponent":
          object.setMovement(new BackgroundMovementComponent());
          break;
        case "BackgroundSpawnComponent":
          object.setSpawner(new BackgroundSpawnComponent());
          break;

        default:
          // Error unidentified component
          break;
      }
    }

    return object;
  }
}
