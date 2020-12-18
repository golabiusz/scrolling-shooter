package com.golabiusz.scrollingshooter.gameobject;

import android.graphics.PointF;
import com.golabiusz.scrollingshooter.Transform;
import org.jetbrains.annotations.NotNull;

class LaserMovementComponent implements MovementComponent {

  @Override
  public boolean move(long fps, @NotNull Transform transform, Transform playerTransform) {

    PointF location = transform.getLocation();
    float speed = transform.getSpeed();

    if (transform.isHeadingRight()) {
      location.x += speed / fps;
    } else if (transform.isHeadingLeft()) {
      location.x -= speed / fps;
    }

    // Has the laser gone out of range (laser can only travel two screen widths)
    float range = transform.getScreenSize().x * 2;
    if (location.x < -range || location.x > range) {
      return false;
    }

    transform.updateCollider();

    return true;
  }
}
