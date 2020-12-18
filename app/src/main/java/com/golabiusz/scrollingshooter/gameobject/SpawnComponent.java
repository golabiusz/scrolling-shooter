package com.golabiusz.scrollingshooter.gameobject;

import com.golabiusz.scrollingshooter.Transform;

interface SpawnComponent {

  void spawn(Transform transform, Transform playerTransform);
}
