package com.golabiusz.scrollingshooter;

import org.jetbrains.annotations.NotNull;

class PhysicsEngine {

  boolean update(long fps, @NotNull ParticleSystem particleSystem) {
    if (particleSystem.isRunning()) {
      particleSystem.update(fps);
    }

    return false;
  }

  // Collision detection method will go here
}
