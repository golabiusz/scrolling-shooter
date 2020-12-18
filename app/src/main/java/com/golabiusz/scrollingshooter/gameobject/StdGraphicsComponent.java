package com.golabiusz.scrollingshooter.gameobject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import com.golabiusz.scrollingshooter.Transform;
import org.jetbrains.annotations.NotNull;

class StdGraphicsComponent implements GraphicsComponent {

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
    if (transform.isFacingRight()) {
      canvas.drawBitmap(bitmap, transform.getLocation().x, transform.getLocation().y, paint);
    } else {
      canvas
          .drawBitmap(bitmapReversed, transform.getLocation().x, transform.getLocation().y, paint);
    }
  }
}
