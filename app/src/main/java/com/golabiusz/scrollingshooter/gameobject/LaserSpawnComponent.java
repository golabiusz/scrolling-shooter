package com.golabiusz.scrollingshooter.gameobject;

import android.graphics.PointF;
import com.golabiusz.scrollingshooter.Transform;
import org.jetbrains.annotations.NotNull;

class LaserSpawnComponent implements SpawnComponent {

  @Override
  public void spawn(@NotNull Transform transform, @NotNull Transform playerTransform) {
    PointF startPosition = playerTransform.getFiringLocation(transform.getSize().x);

    transform.setLocation((int) startPosition.x, (int) startPosition.y);

    if (playerTransform.isFacingRight()) {
      transform.headRight();
    } else {
      transform.headLeft();
    }
  }
}
