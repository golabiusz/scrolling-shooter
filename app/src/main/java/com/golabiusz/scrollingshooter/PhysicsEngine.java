package com.golabiusz.scrollingshooter;

import com.golabiusz.scrollingshooter.gameobject.GameObject;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

class PhysicsEngine {

  boolean update(
      long fps,
      @NotNull ArrayList<GameObject> objects,
      GameState gameState,
      SoundEngine soundEngine,
      ParticleSystem particleSystem) {
    for (GameObject object : objects) {
      if (object.checkActive()) {
        object.update(fps, objects.get(Level.PLAYER_INDEX).getTransform());
      }
    }

    if (particleSystem.isRunning()) {
      particleSystem.update(fps);
    }

    return false;
  }

  // Collision detection method will go here
}
