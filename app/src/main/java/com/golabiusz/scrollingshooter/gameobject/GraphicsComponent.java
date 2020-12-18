package com.golabiusz.scrollingshooter.gameobject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import com.golabiusz.scrollingshooter.Transform;
import com.golabiusz.scrollingshooter.gameobject.ObjectSpec;

interface GraphicsComponent {

  void initialize(Context c, ObjectSpec s, PointF screensize);

  void draw(Canvas canvas, Paint paint, Transform t);
}
