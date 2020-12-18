package com.golabiusz.scrollingshooter.gameobject;

import android.graphics.PointF;

public class AlienLaserSpec extends ObjectSpec {

  private static final String tag = "Alien Laser";
  private static final String bitmapName = "alien_laser";
  private static final float speed = .75f;
  private static final PointF relativeScale = new PointF(14f, 160f);
  private static final String[] components = new String[]{
      "StdGraphicsComponent",
      "LaserMovementComponent",
      "LaserSpawnComponent"};

  public AlienLaserSpec() {
    super(tag, bitmapName, speed, relativeScale, components);
  }
}
