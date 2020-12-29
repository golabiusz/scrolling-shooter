package com.golabiusz.scrollingshooter;

import android.graphics.Rect;
import android.view.MotionEvent;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

class UIController implements InputObserver {

  public UIController(@NotNull GameEngineBroadcaster broadcaster) {
    broadcaster.addObserver(this);
  }

  @Override
  public void handleInput(
      @NotNull MotionEvent event,
      GameState gameState,
      ArrayList<Rect> buttons) {
    int actionIndex = event.getActionIndex();
    int x = (int) event.getX(actionIndex);
    int y = (int) event.getY(actionIndex);

    int eventType = event.getAction() & MotionEvent.ACTION_MASK;

    if (eventType == MotionEvent.ACTION_UP || eventType == MotionEvent.ACTION_POINTER_UP) {
      if (buttons.get(HUD.PAUSE).contains(x, y)) {
        handlePauseButton(gameState);
      }
    }
  }

  private void handlePauseButton(@NotNull GameState gameState) {
    if (!gameState.isPaused()) {
      gameState.pause();
    } else if (gameState.isGameOver()) {
      gameState.startNewGame();
    } else {
      gameState.resume();
    }
  }
}
