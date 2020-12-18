package com.golabiusz.scrollingshooter.gameobject;

import android.graphics.PointF;
import com.golabiusz.scrollingshooter.Transform;
import org.jetbrains.annotations.NotNull;

class PlayerMovementComponent implements MovementComponent {

  @Override
  public boolean move(long fps, @NotNull Transform transform, Transform playerTransform) {
    // How high is the screen?
    float screenHeight = transform.getScreenSize().y;
    // Where is the player?
    PointF location = transform.getLocation();
    // How fast is it going
    float speed = transform.getSpeed();
    // How tall is the ship
    float height = transform.getObjectHeight();

    // Move the ship up or down if needed
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

    transform.updateCollider();

    return true;
  }
}
