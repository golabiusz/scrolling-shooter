package com.golabiusz.scrollingshooter;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import org.jetbrains.annotations.NotNull;

class Renderer {

  private Canvas canvas;
  private SurfaceHolder surfaceHolder;
  private Paint paint;

  Renderer(@NotNull SurfaceView surfaceView) {
    surfaceHolder = surfaceView.getHolder();
    paint = new Paint();
  }

  void draw(GameState gameState, HUD hud, ParticleSystem particleSystem) {
    if (surfaceHolder.getSurface().isValid()) {
      canvas = surfaceHolder.lockCanvas();
      canvas.drawColor(Color.argb(255, 0, 0, 0));

      if (gameState.isDrawing()) {
        // Draw all the game objects here
      }

      if (gameState.isGameOver()) {
        // Draw a background graphic here
      }

      // Draw a particle system explosion here
      if (particleSystem.isRunning()) {
        particleSystem.draw(canvas, paint);
      }

      // Now we draw the HUD on top of everything else
      hud.draw(canvas, paint, gameState);

      surfaceHolder.unlockCanvasAndPost(canvas);
    }
  }
}
