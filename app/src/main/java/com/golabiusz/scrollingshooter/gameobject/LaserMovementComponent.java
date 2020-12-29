package com.golabiusz.scrollingshooter.gameobject;

import android.graphics.PointF;
import com.golabiusz.scrollingshooter.Transform;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

class LaserMovementComponent implements MovementComponent {

  @Override
  public boolean move(long fps, @NotNull Transform transform, Transform playerTransform) {
    updateLocation(fps, transform);

    if (hasGoneOutOfRange(transform.getScreenSize(), transform.getLocation())) {
      return false;
    }

    transform.updateCollider();

    return true;
  }

  private void updateLocation(long fps, @NotNull Transform transform) {
    PointF location = transform.getLocation();
    float speed = transform.getSpeed();

    if (transform.isHeadingRight()) {
      location.x += speed / fps;
    } else if (transform.isHeadingLeft()) {
      location.x -= speed / fps;
    }
  }

  @Contract(pure = true)
  private boolean hasGoneOutOfRange(@NotNull PointF screenSize, @NotNull PointF location) {
    // laser can only travel two screen widths
    float range = screenSize.x * 2;

    return location.x < -range || location.x > range;
  }
}
