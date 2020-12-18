package com.golabiusz.scrollingshooter.gameobject;

import com.golabiusz.scrollingshooter.Transform;
import org.jetbrains.annotations.NotNull;

class BackgroundMovementComponent implements MovementComponent {

  @Override
  public boolean move(long fps, @NotNull Transform transform, @NotNull Transform playerTransform) {

    int currentXClip = transform.getXClip();

    if (playerTransform.isFacingRight()) {
      currentXClip -= transform.getSpeed() / fps;
      transform.setXClip(currentXClip);
    } else {
      currentXClip += transform.getSpeed() / fps;
      transform.setXClip(currentXClip);
    }

    if (currentXClip >= transform.getSize().x) {
      transform.setXClip(0);
      transform.flipReversedFirst();
    } else if (currentXClip <= 0) {
      transform.setXClip((int) transform.getSize().x);
      transform.flipReversedFirst();
    }

    return true;
  }
}
