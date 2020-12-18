package com.golabiusz.scrollingshooter.gameobject;

import android.graphics.PointF;

abstract class ObjectSpec {

  private final String tag;
  private final String bitmapName;
  private final float speed;
  private final PointF sizeScale;
  private final String[] components;

  ObjectSpec(
      String tag,
      String bitmapName,
      float speed,
      PointF relativeScale,
      String[] components) {

    this.tag = tag;
    this.bitmapName = bitmapName;
    this.speed = speed;
    sizeScale = relativeScale;
    this.components = components;
  }

  String getTag() {
    return tag;
  }

  public String getBitmapName() {
    return bitmapName;
  }

  float getSpeed() {
    return speed;
  }

  PointF getScale() {
    return sizeScale;
  }

  String[] getComponents() {
    return components;
  }
}
