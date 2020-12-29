package com.golabiusz.scrollingshooter.gameobject;

import android.graphics.PointF;
import com.golabiusz.scrollingshooter.AlienLaserSpawner;
import com.golabiusz.scrollingshooter.Transform;
import java.util.Random;
import org.jetbrains.annotations.NotNull;

class AlienChaseMovementComponent implements MovementComponent {

  private final Random shotRandom = new Random();

  private final AlienLaserSpawner alienLaserSpawner;

  AlienChaseMovementComponent(AlienLaserSpawner alienLaserSpawner) {
    this.alienLaserSpawner = alienLaserSpawner;
  }

  @Override
  public boolean move(long fps, @NotNull Transform transform, @NotNull Transform playerTransform) {

    // 1 in 100 chances of shot being fired when in line with player
    final int TAKE_SHOT = 0;
    final int SHOT_CHANCE = 100;

    float screenWidth = transform.getScreenSize().x;
    PointF playerLocation = playerTransform.getLocation();

    float height = transform.getObjectHeight();
    boolean facingRight = transform.isFacingRight();

    // How far off before the ship doesn't bother chasing?
    float chasingDistance = transform.getScreenSize().x / 3f;
    // How far can the AI see?
    float seeingDistance = transform.getScreenSize().x / 1.5f;

    PointF location = transform.getLocation();
    float speed = transform.getSpeed();

    // Relative speed difference with player
    float verticalSpeedDifference = .3f;
    float slowDownRelativeToPlayer = 1.8f;
    // Prevent the ship locking on too accurately
    float verticalSearchBounce = 20f;

    // move in the direction of the player but relative to the player's direction of travel
    if (Math.abs(location.x - playerLocation.x) > chasingDistance) {
      if (location.x < playerLocation.x) {
        transform.headRight();
      } else if (location.x > playerLocation.x) {
        transform.headLeft();
      }
    }

    // Can the Alien "see" the player? If so, try and align vertically
    if (Math.abs(location.x - playerLocation.x) <= seeingDistance) {
      // Use a cast to get rid of unnecessary floats that make ship judder
      if ((int) location.y - playerLocation.y < -verticalSearchBounce) {
        transform.headDown();
      } else if ((int) location.y - playerLocation.y > verticalSearchBounce) {
        transform.headUp();
      }

      // Compensate for movement relative to player - but only when in view.
      // Otherwise alien will disappear miles off to one side
      if (!playerTransform.isFacingRight()) {
        location.x += speed * slowDownRelativeToPlayer / fps;
      } else {
        location.x -= speed * slowDownRelativeToPlayer / fps;
      }
    } else {
      // stop vertical movement otherwise alien will disappear off the top or bottom
      transform.stopVertical();
    }

    // Moving vertically is slower than horizontally
    // Change this to make game harder
    if (transform.isHeadingDown()) {
      location.y += speed * verticalSpeedDifference / fps;
    } else if (transform.isHeadingUp()) {
      location.y -= speed * verticalSpeedDifference / fps;
    }

    // Move horizontally
    if (transform.isHeadingLeft()) {
      location.x -= (speed) / fps;
    }
    if (transform.isHeadingRight()) {
      location.x += (speed) / fps;
    }

    // Update the collider
    transform.updateCollider();

    // Shoot if the alien is within a ships height above,
    // below, or in line with the player?
    // This could be a hit or a miss
    if (shotRandom.nextInt(SHOT_CHANCE) == TAKE_SHOT) {
      if (Math.abs(playerLocation.y - location.y) < height) {
        // Is the alien facing the right direction
        // and close enough to the player
        if ((facingRight && playerLocation.x > location.x
            || !facingRight && playerLocation.x < location.x)
            && Math.abs(playerLocation.x - location.x) < screenWidth) {
          // Fire!
          alienLaserSpawner.spawnAlienLaser(transform);
        }
      }
    }

    return true;
  }
}
