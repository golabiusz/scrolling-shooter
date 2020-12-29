package com.golabiusz.scrollingshooter.gameobject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import com.golabiusz.scrollingshooter.Transform;
import org.jetbrains.annotations.NotNull;

public class GameObject {

  private Transform transform;
  private boolean isActive = false;
  private String tag;

  private GraphicsComponent graphicsComponent;
  private MovementComponent movementComponent;
  private SpawnComponent spawnComponent;

  void setSpawner(SpawnComponent spawnComponent) {
    this.spawnComponent = spawnComponent;
  }

  void setGraphics(
      @NotNull GraphicsComponent graphicsComponent,
      Context context,
      ObjectSpec spec,
      PointF objectSize) {
    this.graphicsComponent = graphicsComponent;
    graphicsComponent.initialize(context, spec, objectSize);
  }

  void setMovement(MovementComponent movementComponent) {
    this.movementComponent = movementComponent;
  }

  void setInput(@NotNull InputComponent inputComponent) {
    inputComponent.setTransform(transform);
  }

  void setTag(String tag) {
    this.tag = tag;
  }

  void setTransform(Transform transform) {
    this.transform = transform;
  }

  public void draw(Canvas canvas, Paint paint) {
    graphicsComponent.draw(canvas, paint, transform);
  }

  public void update(long fps, Transform playerTransform) {
    if (!movementComponent.move(fps, transform, playerTransform)) {
      isActive = false;
    }
  }

  public boolean spawn(Transform playerTransform) {
    if (!isActive) {
      spawnComponent.spawn(transform, playerTransform);
      isActive = true;

      return true;
    }

    return false;
  }

  public boolean isActive() {
    return isActive;
  }

  public String getTag() {
    return tag;
  }

  public void setInactive() {
    isActive = false;
  }

  public Transform getTransform() {
    return transform;
  }
}
