package com.golabiusz.scrollingshooter;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import java.util.ArrayList;
import java.util.Random;

class ParticleSystem {

  private float duration;
  private ArrayList<Particle> particles;
  private final Random random = new Random();
  private boolean isRunning = false;

  void init(int numParticles) {
    particles = new ArrayList<>();

    final int MAX_ANGLE = 360;
    final int MAX_SPEED = 20;

    for (int i = 0; i < numParticles; i++) {
      float degrees = (random.nextInt(MAX_ANGLE));
      float radian = degrees * 3.14f / 180.f;
      float speed = (random.nextInt(MAX_SPEED) + 1);

      PointF direction = new PointF(
          (float) Math.cos(radian) * speed,
          (float) Math.sin(radian) * speed);

      particles.add(new Particle(direction));
    }
  }

  void emitParticles(PointF startPosition) {
    isRunning = true;
    duration = 1f;

    for (Particle particle : particles) {
      particle.setPosition(startPosition);
    }
  }

  void update(long fps) {
    duration -= (1f / fps);

    for (Particle particle : particles) {
      particle.update();
    }

    if (duration < 0) {
      isRunning = false;
    }
  }

  void draw(Canvas canvas, Paint paint) {
    final int particleSize = 5;

    for (Particle particle : particles) {
      paint.setARGB(
          255,
          random.nextInt(256),
          random.nextInt(256),
          random.nextInt(256));

      // Uncomment the next line to have plain white particles
      //paint.setColor(Color.argb(255,255,255,255));
      canvas.drawRect(
          particle.getPosition().x,
          particle.getPosition().y,
          particle.getPosition().x + particleSize,
          particle.getPosition().y + particleSize, paint);
    }
  }

  boolean isRunning() {
    return isRunning;
  }
}
