package com.golabiusz.scrollingshooter.gameobject;

import com.golabiusz.scrollingshooter.Transform;

interface MovementComponent {

  boolean move(long fps, Transform t, Transform playerTransform);
}
