package com.golabiusz.scrollingshooter.gameobject;

import android.graphics.PointF;
import com.golabiusz.scrollingshooter.Transform;
import java.util.Random;
import org.jetbrains.annotations.NotNull;

class AlienHorizontalSpawnComponent implements SpawnComponent {

  @Override
  public void spawn(@NotNull Transform transform, Transform playerTransform) {
    PointF screenSize = transform.getScreenSize();

    // Spawn just off screen randomly left or right
    Random random = new Random();
    boolean left = random.nextBoolean();
    // How far away?
    float distance = random.nextInt(2000) + transform.getScreenSize().x;

    // Generate a height to spawn at where
    // the entire ship is vertically on-screen
    float spawnHeight = random.nextFloat() * screenSize.y - transform.getSize().y;

    if (left) {
      transform.setLocation(-distance, spawnHeight);
      transform.headRight();
    } else {
      transform.setLocation(distance, spawnHeight);
      transform.headLeft();
    }
  }
}