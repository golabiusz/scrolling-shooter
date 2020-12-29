package com.golabiusz.scrollingshooter.gameobject;

import android.graphics.PointF;
import com.golabiusz.scrollingshooter.AlienLaserSpawner;
import com.golabiusz.scrollingshooter.Transform;
import java.util.Random;
import org.jetbrains.annotations.NotNull;

class AlienPatrolMovementComponent implements MovementComponent {

  private final AlienLaserSpawner alienLaserSpawner;
  private final Random mShotRandom = new Random();

  AlienPatrolMovementComponent(AlienLaserSpawner als) {
    alienLaserSpawner = als;
  }

  @Override
  public boolean move(long fps, @NotNull Transform transform, @NotNull Transform playerTransform) {

    final int TAKE_SHOT = 0;
    // 1 in 100 chance of shot being fired when in line with player
    final int SHOT_CHANCE = 100;

    PointF playerLocation = playerTransform.getLocation();

    float screenX = transform.getScreenSize().x;
    float screenY = transform.getScreenSize().y;

    // How far ahead can the alien see?
    float mSeeingDistance = screenX * .5f;

    PointF location = transform.getLocation();
    float speed = transform.getSpeed();
    float height = transform.getObjectHeight();

    // Stop the alien going too far away
    final float MIN_VERTICAL_BOUNDS = 0;
    float MAX_VERTICAL_BOUNDS = screenY - height;
    final float MAX_HORIZONTAL_BOUNDS = 2 * screenX;
    final float MIN_HORIZONTAL_BOUNDS = 2 * -screenX;

    // Adjust the horizontal speed relative to the player's heading
    // Default is no horizontal speed adjustment
    float horizontalSpeedAdjustmentRelativeToPlayer = 0;
    // How much to speed up or slow down relative to player's heading
    float horizontalSpeedAdjustmentModifier = .8f;

    // Can the Alien "see" the player? If so make speed relative
    if (Math.abs(location.x - playerLocation.x) < mSeeingDistance) {
      if (playerTransform.isFacingRight() != transform.isFacingRight()) {
        // Facing a different way speed up the alien
        horizontalSpeedAdjustmentRelativeToPlayer = speed * horizontalSpeedAdjustmentModifier;
      } else {
        // Facing the same way slow it down
        horizontalSpeedAdjustmentRelativeToPlayer = -(speed * horizontalSpeedAdjustmentModifier);
      }
    }

    // Move horizontally taking into account
    // the speed modification
    if (transform.isHeadingLeft()) {
      location.x -= (speed + horizontalSpeedAdjustmentRelativeToPlayer) / fps;

      // Turn the ship around when it reaches the
      // extent of its horizontal patrol area
      if (location.x < MIN_HORIZONTAL_BOUNDS) {
        location.x = MIN_HORIZONTAL_BOUNDS;
        transform.headRight();
      }
    } else {
      location.x += (speed + horizontalSpeedAdjustmentRelativeToPlayer) / fps;

      // Turn the ship around when it reaches the
      // extent of its horizontal patrol area
      if (location.x > MAX_HORIZONTAL_BOUNDS) {
        location.x = MAX_HORIZONTAL_BOUNDS;
        transform.headLeft();
      }
    }

    // Vertical speed remains same,
    // Not affected by speed adjustment
    if (transform.isHeadingDown()) {
      location.y += (speed) / fps;
      if (location.y > MAX_VERTICAL_BOUNDS) {
        transform.headUp();
      }
    } else {
      location.y -= (speed) / fps;
      if (location.y < MIN_VERTICAL_BOUNDS) {
        transform.headDown();
      }
    }

    transform.updateCollider();

    // Shoot if the alien within a ships height above, below, or in line with the player?
    // This could be a hit or a miss
    if (mShotRandom.nextInt(SHOT_CHANCE) == TAKE_SHOT) {
      if (Math.abs(playerLocation.y - location.y) < height) {
        // is the alien facing the right direction and close enough to the player
        if ((transform.isFacingRight() && playerLocation.x > location.x
            || !transform.isFacingRight() && playerLocation.x < location.x)
            && Math.abs(playerLocation.x - location.x) < screenX) {
          alienLaserSpawner.spawnAlienLaser(transform);
        }
      }
    }

    return true;
  }
}
