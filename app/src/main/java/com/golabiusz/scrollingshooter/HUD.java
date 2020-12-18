package com.golabiusz.scrollingshooter;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

public class HUD {

  private final int textFormatting;
  private final int screenHeight;
  private final int screenWidth;

  private ArrayList<Rect> controls;

  public final static int UP = 0;
  public final static int DOWN = 1;
  public final static int FLIP = 2;
  public final static int SHOOT = 3;
  public final static int PAUSE = 4;

  HUD(@NotNull Point size) {
    screenHeight = size.y;
    screenWidth = size.x;
    textFormatting = size.x / 50;

    prepareControls();
  }

  private void prepareControls() {
    int buttonWidth = screenWidth / 14;
    int buttonHeight = screenHeight / 12;
    int buttonPadding = screenWidth / 90;

    Rect up = new Rect(
        buttonPadding,
        screenHeight - (buttonHeight * 2) - (buttonPadding * 2),
        buttonPadding + buttonWidth,
        screenHeight - buttonHeight - (buttonPadding * 2));

    Rect down = new Rect(
        buttonPadding,
        screenHeight - buttonHeight - buttonPadding,
        buttonPadding + buttonWidth,
        screenHeight - buttonPadding);

    Rect flip = new Rect(
        screenWidth - buttonPadding - buttonWidth,
        screenHeight - buttonPadding - buttonHeight,
        screenWidth - buttonPadding,
        screenHeight - buttonPadding);

    Rect shoot = new Rect(
        screenWidth - buttonPadding - buttonWidth,
        screenHeight - (buttonHeight * 2) - (buttonPadding * 2),
        screenWidth - buttonPadding,
        screenHeight - buttonHeight - (buttonPadding * 2));

    Rect pause = new Rect(
        screenWidth - buttonPadding - buttonWidth,
        buttonPadding,
        screenWidth - buttonPadding,
        buttonPadding + buttonHeight);

    controls = new ArrayList<>();
    controls.add(UP, up);
    controls.add(DOWN, down);
    controls.add(FLIP, flip);
    controls.add(SHOOT, shoot);
    controls.add(PAUSE, pause);
  }

  void draw(@NotNull Canvas canvas, @NotNull Paint paint, @NotNull GameState gameState) {
    paint.setColor(Color.argb(255, 255, 255, 255));
    paint.setTextSize(textFormatting);

    canvas.drawText("Hi: " + gameState.getHighScore(), textFormatting, textFormatting, paint);
    canvas.drawText("Score: " + gameState.getScore(), textFormatting, textFormatting * 2, paint);
    canvas.drawText("Lives: " + gameState.getNumLives(), textFormatting, textFormatting * 3, paint);

    if (gameState.isGameOver()) {
      paint.setTextSize(textFormatting * 5);
      canvas.drawText("PRESS PLAY", screenWidth / 4, screenHeight / 2, paint);
    } else if (gameState.isPaused()) {
      paint.setTextSize(textFormatting * 5);
      canvas.drawText("PAUSED", screenWidth / 3, screenHeight / 2, paint);
    }

    drawControls(canvas, paint);
  }

  private void drawControls(Canvas canvas, @NotNull Paint paint) {
    int color = paint.getColor();

    paint.setColor(Color.argb(100, 255, 255, 255));
    for (Rect rect : controls) {
      canvas.drawRect(rect.left, rect.top, rect.right, rect.bottom, paint);
    }

    paint.setColor(color);
  }

  ArrayList<Rect> getControls() {
    return controls;
  }
}
