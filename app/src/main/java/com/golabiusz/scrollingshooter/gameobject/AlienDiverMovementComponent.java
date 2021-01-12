package com.golabiusz.scrollingshooter.gameobject;

import android.graphics.PointF;
import com.golabiusz.scrollingshooter.Transform;
import java.util.Random;
import org.jetbrains.annotations.NotNull;

class AlienDiverMovementComponent implements MovementComponent {

  @Override
  public boolean move(long fps, @NotNull Transform transform, @NotNull Transform playerTransform) {
    PointF location = transform.getLocation();
    float speed = transform.getSpeed();

    // Relative speed difference with player
    float slowDownRelativeToPlayer = 1.8f;

    // Compensate for movement relative to player - but only when in view.
    // Otherwise alien will disappear miles off to one side
    if (!playerTransform.isFacingRight()) {
      location.x += speed * slowDownRelativeToPlayer / fps;
    } else {
      location.x -= speed * slowDownRelativeToPlayer / fps;
    }

    fallDown(fps, transform);

    transform.updateCollider();

    return true;
  }

  private void fallDown(long fps, @NotNull Transform transform) {
    float speed = transform.getSpeed();
    PointF location = transform.getLocation();

    location.y += speed / fps;
    if (location.y > transform.getScreenSize().y) {
      Random random = new Random();
      location.y = 0 - transform.getObjectHeight();
      location.x = random.nextInt((int) transform.getScreenSize().x);
    }
  }
}
