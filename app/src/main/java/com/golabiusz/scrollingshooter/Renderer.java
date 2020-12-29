package com.golabiusz.scrollingshooter;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.golabiusz.scrollingshooter.gameobject.GameObject;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

class Renderer {

  private final SurfaceHolder surfaceHolder;
  private final Paint paint;
  private Canvas canvas;

  Renderer(@NotNull SurfaceView surfaceView) {
    surfaceHolder = surfaceView.getHolder();
    paint = new Paint();
  }

  void draw(
      ArrayList<GameObject> objects,
      GameState gameState,
      HUD hud,
      ParticleSystem particleSystem) {
    if (surfaceHolder.getSurface().isValid()) {
      canvas = surfaceHolder.lockCanvas();
      canvas.drawColor(Color.argb(255, 0, 0, 0));

      if (gameState.isDrawing()) {
        drawGameObjects(objects);
      }

      if (gameState.isGameOver()) {
        objects.get(Level.BACKGROUND_INDEX).draw(canvas, paint);
      }

      if (particleSystem.isRunning()) {
        particleSystem.draw(canvas, paint);
      }

      hud.draw(canvas, paint, gameState);

      surfaceHolder.unlockCanvasAndPost(canvas);
    }
  }

  private void drawGameObjects(@NotNull ArrayList<GameObject> objects) {
    for (GameObject object : objects) {
      if (object.isActive()) {
        object.draw(canvas, paint);
      }
    }
  }
}
