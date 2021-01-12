package com.golabiusz.scrollingshooter.physics;

import android.graphics.PointF;
import org.jetbrains.annotations.NotNull;

class Particle {

  private final PointF position;
  private final PointF velocity;

  Particle(@NotNull PointF direction) {
    position = new PointF();

    velocity = new PointF();
    velocity.x = direction.x;
    velocity.y = direction.y;
  }

  void update() {
    position.x += velocity.x;
    position.y += velocity.y;
  }

  void setPosition(@NotNull PointF position) {
    this.position.x = position.x;
    this.position.y = position.y;
  }

  PointF getPosition() {
    return position;
  }
}
