package com.golabiusz.scrollingshooter.gameobject;

import com.golabiusz.scrollingshooter.Transform;
import org.jetbrains.annotations.NotNull;

class PlayerSpawnComponent implements SpawnComponent {

  @Override
  public void spawn(@NotNull Transform transform, Transform playerTransform) {
    // Spawn in the centre of the screen
    transform.setLocation(transform.getScreenSize().x / 2, transform.getScreenSize().y / 2);
  }
}
