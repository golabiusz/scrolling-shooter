package com.golabiusz.scrollingshooter.gameobject;

import android.graphics.PointF;
import com.golabiusz.scrollingshooter.Transform;
import org.jetbrains.annotations.NotNull;

class PlayerMovementComponent implements MovementComponent {

  @Override
  public boolean move(long fps, @NotNull Transform transform, Transform playerTransform) {
    moveVertically(fps, transform);

    transform.updateCollider();

    return true;
  }

  private void moveVertically(long fps, @NotNull Transform transform) {
    float screenHeight = transform.getScreenSize().y;
    float height = transform.getObjectHeight();
    float speed = transform.getSpeed();

    PointF location = transform.getLocation();

    if (transform.isHeadingDown()) {
      location.y += speed / fps;
    } else if (transform.isHeadingUp()) {
      location.y -= speed / fps;
    }

    // Make sure the ship can't go off the screen
    if (location.y > screenHeight - height) {
      location.y = screenHeight - height;
    } else if (location.y < 0) {
      location.y = 0;
    }
  }
}
