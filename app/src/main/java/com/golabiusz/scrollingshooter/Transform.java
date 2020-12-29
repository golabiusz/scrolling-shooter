package com.golabiusz.scrollingshooter;

import android.graphics.PointF;
import android.graphics.RectF;

public class Transform {

  // These two members are for scrolling background
  private int xClip;
  private boolean reversedFirst = false;

  private final RectF collider;
  private PointF location;
  private boolean isFacingRight = true;
  private boolean isHeadingUp = false;
  private boolean isHeadingDown = false;
  private boolean isHeadingLeft = false;
  private boolean isHeadingRight = false;
  private final float speed;
  private final float objectHeight;
  private final float objectWidth;
  private static PointF screenSize;

  public Transform(
      float speed,
      float objectWidth,
      float objectHeight,
      PointF startingLocation,
      PointF screenSize) {

    collider = new RectF();
    this.speed = speed;
    this.objectHeight = objectHeight;
    this.objectWidth = objectWidth;
    location = startingLocation;
    Transform.screenSize = screenSize;
  }

  public boolean getReversedFirst() {
    return reversedFirst;
  }

  public void flipReversedFirst() {
    reversedFirst = !reversedFirst;
  }

  public int getXClip() {
    return xClip;
  }

  public void setXClip(int newXClip) {
    xClip = newXClip;
  }

  public PointF getScreenSize() {
    return screenSize;
  }

  public void headUp() {
    isHeadingUp = true;
    isHeadingDown = false;
  }

  public void headDown() {
    isHeadingDown = true;
    isHeadingUp = false;
  }

  public void headRight() {
    isHeadingRight = true;
    isHeadingLeft = false;
    isFacingRight = true;
  }

  public void headLeft() {
    isHeadingLeft = true;
    isHeadingRight = false;
    isFacingRight = false;
  }

  public boolean isHeadingUp() {
    return isHeadingUp;
  }

  public boolean isHeadingDown() {
    return isHeadingDown;
  }

  public boolean isHeadingRight() {
    return isHeadingRight;
  }

  public boolean isHeadingLeft() {
    return isHeadingLeft;
  }

  public void updateCollider() {
    // Pull the borders in a bit (10%)
    collider.top = location.y + (objectHeight / 10);
    collider.left = location.x + (objectWidth / 10);
    collider.bottom = (collider.top + objectHeight) - objectHeight / 10;
    collider.right = (collider.left + objectWidth) - objectWidth / 10;
  }

  public float getObjectHeight() {
    return objectHeight;
  }

  public void stopVertical() {
    isHeadingDown = false;
    isHeadingUp = false;
  }

  public float getSpeed() {
    return speed;
  }

  public void setLocation(float horizontal, float vertical) {
    location = new PointF(horizontal, vertical);
    updateCollider();
  }

  public PointF getLocation() {
    return location;
  }

  public PointF getSize() {
    return new PointF((int) objectWidth, (int) objectHeight);
  }

  public void flip() {
    isFacingRight = !isFacingRight;
  }

  public boolean isFacingRight() {
    return isFacingRight;
  }

  RectF getCollider() {
    return collider;
  }

  public PointF getFiringLocation(float laserLength) {
    PointF firingLocation = new PointF();

    if (isFacingRight) {
      firingLocation.x = location.x + (objectWidth / 8f);
    } else {
      firingLocation.x = location.x + (objectWidth / 8f) - (laserLength);
    }
    firingLocation.y = location.y + (objectHeight / 1.28f);

    return firingLocation;
  }
}
