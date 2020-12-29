package com.golabiusz.scrollingshooter.gameobject;

import com.golabiusz.scrollingshooter.Transform;
import java.util.Random;
import org.jetbrains.annotations.NotNull;

class AlienVerticalSpawnComponent implements SpawnComponent {

  public void spawn(@NotNull Transform transform, Transform playerLTransform) {
    // Spawn just off screen randomly but within the screen width
    Random random = new Random();
    float xPosition = random.nextInt((int) transform.getScreenSize().x);

    // Set the height to vertically just above the visible game
    float spawnHeight = random.nextInt(300) - transform.getObjectHeight();

    // Spawn the ship
    transform.setLocation(xPosition, spawnHeight);
    // Always going down
    transform.headDown();
  }
}
