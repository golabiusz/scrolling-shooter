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
    chooseDirection(transform, playerTransform);
    alignVertically(fps, transform, playerTransform);
    moveVertically(fps, transform);
    moveHorizontally(fps, transform);

    transform.updateCollider();

    shoot(transform, playerTransform);

    return true;
  }

  private void chooseDirection(@NotNull Transform transform, @NotNull Transform playerTransform) {
    // How far off before the ship doesn't bother chasing?
    float chasingDistance = transform.getScreenSize().x / 3f;

    PointF location = transform.getLocation();
    PointF playerLocation = playerTransform.getLocation();

    // move in the direction of the player but relative to the player's direction of travel
    if (Math.abs(location.x - playerLocation.x) > chasingDistance) {
      if (location.x < playerLocation.x) {
        transform.headRight();
      } else if (location.x > playerLocation.x) {
        transform.headLeft();
      }
    }
  }

  private void alignVertically(
      long fps,
      @NotNull Transform transform,
      @NotNull Transform playerTransform) {
    // How far can the AI see?
    float seeingDistance = transform.getScreenSize().x * .5f - 20f;
    // Prevent the ship locking on too accurately
    float verticalSearchBounce = 20f;
    // Relative speed difference with player
    float slowDownRelativeToPlayer = 1.8f;
    float speed = transform.getSpeed();

    PointF location = transform.getLocation();
    PointF playerLocation = playerTransform.getLocation();

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
  }

  private void moveVertically(long fps, @NotNull Transform transform) {
    float verticalSpeedDifference = .3f;
    float speed = transform.getSpeed();

    PointF location = transform.getLocation();

    // Moving vertically is slower than horizontally, change this to make game harder
    if (transform.isHeadingDown()) {
      location.y += speed * verticalSpeedDifference / fps;
    } else if (transform.isHeadingUp()) {
      location.y -= speed * verticalSpeedDifference / fps;
    }
  }

  private void moveHorizontally(long fps, @NotNull Transform transform) {
    float speed = transform.getSpeed();

    PointF location = transform.getLocation();

    if (transform.isHeadingLeft()) {
      location.x -= (speed) / fps;
    } else {
      location.x += (speed) / fps;
    }
  }

  private void shoot(@NotNull Transform transform, @NotNull Transform playerTransform) {
    // 1 in 100 chances of shot being fired when in line with player
    final int TAKE_SHOT = 0;
    final int SHOT_CHANCE = 100;

    PointF location = transform.getLocation();
    PointF playerLocation = playerTransform.getLocation();

    if (shotRandom.nextInt(SHOT_CHANCE) == TAKE_SHOT) {
      // shoot if the alien within a ships height above, below, or in line with the player?
      if (Math.abs(playerLocation.y - location.y) < transform.getObjectHeight()) {
        // is the alien facing the right direction and close enough to the player
        if ((transform.isFacingRight() && playerLocation.x > location.x
            || !transform.isFacingRight() && playerLocation.x < location.x)
            && Math.abs(playerLocation.x - location.x) < transform.getScreenSize().x) {
          alienLaserSpawner.spawnAlienLaser(transform);
        }
      }
    }
  }
}
