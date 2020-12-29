package com.golabiusz.scrollingshooter;

import android.graphics.PointF;
import android.graphics.RectF;
import com.golabiusz.scrollingshooter.gameobject.GameObject;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

class PhysicsEngine {

  private final ParticleSystem particleSystem;

  PhysicsEngine(ParticleSystem particleSystem) {
    this.particleSystem = particleSystem;
  }

  boolean update(long fps, @NotNull ArrayList<GameObject> objects, GameState gameState) {
    for (GameObject object : objects) {
      if (object.isActive()) {
        object.update(fps, objects.get(Level.PLAYER_INDEX).getTransform());
      }
    }

    if (particleSystem.isRunning()) {
      particleSystem.update(fps);
    }

    return detectCollisions(gameState, objects);
  }

  private boolean detectCollisions(GameState gameState, @NotNull ArrayList<GameObject> objects) {
    boolean playerHit = false;

    for (GameObject firstObject : objects) {
      if (firstObject.isActive()) {
        for (GameObject secondObject : objects) {
          if (secondObject.isActive() && RectF.intersects(
              firstObject.getTransform().getCollider(),
              secondObject.getTransform().getCollider())) {

            playerHit = evaluateCollision(firstObject, secondObject, gameState, objects, playerHit);
          }
        }
      }
    }

    return playerHit;
  }

  private boolean evaluateCollision(
      @NotNull GameObject firstObject,
      @NotNull GameObject secondObject,
      GameState gameState,
      @NotNull ArrayList<GameObject> objects,
      boolean playerHit) {

    switch (firstObject.getTag() + " with " + secondObject.getTag()) {
      case "Player with Alien Laser":
      case "Player with Alien":
        playerHit = true;
        gameState.loseLife();
        break;

      case "Player Laser with Alien":
        gameState.increaseScore();
        // Respawn the alien
        particleSystem.emitParticles(new PointF(
            secondObject.getTransform().getLocation().x,
            secondObject.getTransform().getLocation().y));
        secondObject.setInactive();
        secondObject.spawn(objects.get(Level.PLAYER_INDEX).getTransform());

        firstObject.setInactive();
        break;

      default:
        break;
    }

    return playerHit;
  }
}
