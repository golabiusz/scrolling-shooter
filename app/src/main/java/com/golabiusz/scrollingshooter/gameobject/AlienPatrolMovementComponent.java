package com.golabiusz.scrollingshooter.gameobject;

import android.graphics.PointF;
import com.golabiusz.scrollingshooter.AlienLaserSpawner;
import com.golabiusz.scrollingshooter.Transform;
import java.util.Random;
import org.jetbrains.annotations.NotNull;

class AlienPatrolMovementComponent implements MovementComponent {

  private final AlienLaserSpawner alienLaserSpawner;
  private final Random shotRandom = new Random();

  AlienPatrolMovementComponent(AlienLaserSpawner als) {
    alienLaserSpawner = als;
  }

  @Override
  public boolean move(long fps, @NotNull Transform transform, @NotNull Transform playerTransform) {
    moveHorizontally(fps, transform, playerTransform);
    moveVertically(fps, transform);

    transform.updateCollider();

    shoot(transform, playerTransform);

    return true;
  }

  private void moveHorizontally(
      long fps,
      @NotNull Transform transform,
      @NotNull Transform playerTransform) {

    float screenX = transform.getScreenSize().x;
    float speed = transform.getSpeed();
    final float MAX_HORIZONTAL_BOUNDS = 2 * screenX;
    final float MIN_HORIZONTAL_BOUNDS = 2 * -screenX;

    PointF location = transform.getLocation();

    float horizontalSpeedAdjustmentRelativeToPlayer =
        calculateHorizontalSpeedAdjustment(transform, playerTransform);

    // Move horizontally taking into account the speed modification
    if (transform.isHeadingLeft()) {
      location.x -= (speed + horizontalSpeedAdjustmentRelativeToPlayer) / fps;

      // Turn the ship around when it reaches the extent of its horizontal patrol area
      if (location.x < MIN_HORIZONTAL_BOUNDS) {
        location.x = MIN_HORIZONTAL_BOUNDS;
        transform.headRight();
      }
    } else {
      location.x += (speed + horizontalSpeedAdjustmentRelativeToPlayer) / fps;

      // Turn the ship around when it reaches the extent of its horizontal patrol area
      if (location.x > MAX_HORIZONTAL_BOUNDS) {
        location.x = MAX_HORIZONTAL_BOUNDS;
        transform.headLeft();
      }
    }
  }

  private float calculateHorizontalSpeedAdjustment(
      @NotNull Transform transform,
      @NotNull Transform playerTransform) {
    // Adjust the horizontal speed relative to the player's heading
    float horizontalSpeedAdjustmentRelativeToPlayer = 0;
    // How much to speed up or slow down relative to player's heading
    float horizontalSpeedAdjustmentModifier = .8f;
    // How far ahead can the alien see?
    float seeingDistance = transform.getScreenSize().x * .5f;
    float speed = transform.getSpeed();

    // Can the Alien "see" the player? If so make speed relative
    if (Math.abs(transform.getLocation().x - playerTransform.getLocation().x) < seeingDistance) {
      if (playerTransform.isFacingRight() != transform.isFacingRight()) {
        // Facing a different way speed up the alien
        horizontalSpeedAdjustmentRelativeToPlayer = speed * horizontalSpeedAdjustmentModifier;
      } else {
        // Facing the same way slow it down
        horizontalSpeedAdjustmentRelativeToPlayer = -(speed * horizontalSpeedAdjustmentModifier);
      }
    }

    return horizontalSpeedAdjustmentRelativeToPlayer;
  }

  private void moveVertically(long fps, @NotNull Transform transform) {
    float screenY = transform.getScreenSize().y;
    final float MIN_VERTICAL_BOUNDS = 0;
    float MAX_VERTICAL_BOUNDS = screenY - transform.getObjectHeight();

    PointF location = transform.getLocation();

    if (transform.isHeadingDown()) {
      location.y += (transform.getSpeed()) / fps;
      if (location.y > MAX_VERTICAL_BOUNDS) {
        transform.headUp();
      }
    } else {
      location.y -= (transform.getSpeed()) / fps;
      if (location.y < MIN_VERTICAL_BOUNDS) {
        transform.headDown();
      }
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
