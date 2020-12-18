package com.golabiusz.scrollingshooter.gameobject;

import android.graphics.PointF;

public class AlienPatrolSpec extends ObjectSpec {

  private static final String tag = "Alien";
  private static final String bitmapName = "alien_ship2";
  private static final float speed = 5f;
  private static final PointF relativeScale = new PointF(15f, 15f);
  private static final String[] components = new String[]{
      "StdGraphicsComponent",
      "AlienPatrolMovementComponent",
      "AlienHorizontalSpawnComponent"};

  public AlienPatrolSpec() {
    super(tag, bitmapName, speed, relativeScale, components);
  }
}
