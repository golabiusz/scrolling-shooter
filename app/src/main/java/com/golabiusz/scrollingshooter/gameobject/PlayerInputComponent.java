package com.golabiusz.scrollingshooter.gameobject;

import android.graphics.Rect;
import android.view.MotionEvent;
import com.golabiusz.scrollingshooter.GameEngine;
import com.golabiusz.scrollingshooter.GameState;
import com.golabiusz.scrollingshooter.HUD;
import com.golabiusz.scrollingshooter.InputObserver;
import com.golabiusz.scrollingshooter.PlayerLaserSpawner;
import com.golabiusz.scrollingshooter.Transform;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

class PlayerInputComponent implements InputComponent, InputObserver {

  private Transform transform;
  private final PlayerLaserSpawner playerLaserSpawner;

  PlayerInputComponent(@NotNull GameEngine gameEngine) {
    gameEngine.addObserver(this);
    playerLaserSpawner = gameEngine;
  }

  @Override
  public void setTransform(Transform transform) {
    this.transform = transform;
  }

  @Override
  public void handleInput(
      @NotNull MotionEvent event,
      GameState gameState,
      ArrayList<Rect> buttons) {
    int i = event.getActionIndex();
    int x = (int) event.getX(i);
    int y = (int) event.getY(i);

    switch (event.getAction() & MotionEvent.ACTION_MASK) {

      case MotionEvent.ACTION_UP:
      case MotionEvent.ACTION_POINTER_UP:
        if (buttons.get(HUD.UP).contains(x, y) || buttons.get(HUD.DOWN).contains(x, y)) {
          transform.stopVertical();
        }
        break;

      case MotionEvent.ACTION_DOWN:
      case MotionEvent.ACTION_POINTER_DOWN:
        if (buttons.get(HUD.UP).contains(x, y)) {
          transform.headUp();
        } else if (buttons.get(HUD.DOWN).contains(x, y)) {
          transform.headDown();
        } else if (buttons.get(HUD.FLIP).contains(x, y)) {
          transform.flip();
        } else if (buttons.get(HUD.SHOOT).contains(x, y)) {
          playerLaserSpawner.spawnPlayerLaser(transform);
        }
        break;
    }
  }
}
