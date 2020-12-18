package com.golabiusz.scrollingshooter.gameobject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import com.golabiusz.scrollingshooter.Transform;
import org.jetbrains.annotations.NotNull;

class BackgroundGraphicsComponent implements GraphicsComponent {

  private Bitmap bitmap;
  private Bitmap bitmapReversed;

  @Override
  public void initialize(
      @NotNull Context context,
      @NotNull ObjectSpec spec,
      @NotNull PointF objectSize) {
    // Make a resource id out of the string of the file name
    int resID = context.getResources()
        .getIdentifier(spec.getBitmapName(), "drawable", context.getPackageName());

    // Load the bitmap using the id
    bitmap = BitmapFactory.decodeResource(context.getResources(), resID);

    // Resize the bitmap
    bitmap = Bitmap.createScaledBitmap(bitmap, (int) objectSize.x, (int) objectSize.y, false);

    // Create a mirror image of the bitmap
    Matrix matrix = new Matrix();
    matrix.setScale(-1, 1);
    bitmapReversed = Bitmap
        .createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
  }

  @Override
  public void draw(Canvas canvas, Paint paint, @NotNull Transform transform) {
    int xClip = transform.getXClip();
    int width = bitmap.getWidth();
    int height = bitmap.getHeight();
    int startY = 0;
    int endY = (int) transform.getScreenSize().y + 20;

    // For the regular bitmap
    Rect fromRect1 = new Rect(0, 0, width - xClip, height);
    Rect toRect1 = new Rect(xClip, startY, width, endY);

    // For the reversed background
    Rect fromRect2 = new Rect(width - xClip, 0, width, height);
    Rect toRect2 = new Rect(0, startY, xClip, endY);

    // draw the two background bitmaps
    if (!transform.getReversedFirst()) {
      canvas.drawBitmap(bitmap, fromRect1, toRect1, paint);
      canvas.drawBitmap(bitmapReversed, fromRect2, toRect2, paint);
    } else {
      canvas.drawBitmap(bitmap, fromRect2, toRect2, paint);
      canvas.drawBitmap(bitmapReversed, fromRect1, toRect1, paint);
    }
  }
}
