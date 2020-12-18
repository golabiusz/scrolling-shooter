package com.golabiusz.scrollingshooter.gameobject;

import com.golabiusz.scrollingshooter.Transform;
import org.jetbrains.annotations.NotNull;

class BackgroundSpawnComponent implements SpawnComponent {

  @Override
  public void spawn(@NotNull Transform transform, Transform playerTransform) {
    transform.setLocation(0f,0f);
  }
}
