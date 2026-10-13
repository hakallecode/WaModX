package dev.soughtxp.wamodx.core;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

public class StatusDrawables {

    public static class SeenDrawable extends Drawable {
        private final Paint stroke = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final Paint fill = new Paint(Paint.ANTI_ALIAS_FLAG);

        public SeenDrawable() {
            stroke.setColor(Color.WHITE);
            stroke.setStyle(Paint.Style.STROKE);
            stroke.setStrokeWidth(40 * 0.10f);
            stroke.setStrokeCap(Paint.Cap.ROUND);
            stroke.setStrokeJoin(Paint.Join.ROUND);
            fill.setColor(Color.WHITE);
            fill.setStyle(Paint.Style.FILL);
        }

        @Override
        public int getIntrinsicWidth() {
            return 40;
        }

        @Override
        public int getIntrinsicHeight() {
            return 40;
        }

        @Override
        public void draw(Canvas canvas) {
            float w = getBounds().width();
            float h = getBounds().height();

            RectF eye = new RectF(w * 0.12f, h * 0.30f, w * 0.88f, h * 0.70f);
            canvas.drawOval(eye, stroke);

            canvas.drawCircle(w * 0.5f, h * 0.5f, w * 0.13f, fill);
        }

        @Override
        public void setAlpha(int alpha) {
            stroke.setAlpha(alpha);
            fill.setAlpha(alpha);
        }

        @Override
        public void setColorFilter(ColorFilter colorFilter) {
            stroke.setColorFilter(colorFilter);
            fill.setColorFilter(colorFilter);
        }

        @Override
        public int getOpacity() {
            return PixelFormat.TRANSLUCENT;
        }
    }

    public static class CopyDrawable extends Drawable {
        private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        public CopyDrawable() {
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(40 * 0.10f);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setStrokeJoin(Paint.Join.ROUND);
        }

        @Override
        public int getIntrinsicWidth() {
            return 40;
        }

        @Override
        public int getIntrinsicHeight() {
            return 40;
        }

        @Override
        public void draw(Canvas canvas) {
            float w = getBounds().width();
            float h = getBounds().height();

            RectF backRect = new RectF(w * 0.15f, h * 0.15f, w * 0.65f, h * 0.65f);
            canvas.drawRoundRect(backRect, w * 0.08f, w * 0.08f, paint);

            RectF frontRect = new RectF(w * 0.35f, h * 0.35f, w * 0.85f, h * 0.85f);
            canvas.drawRoundRect(frontRect, w * 0.08f, w * 0.08f, paint);
        }

        @Override
        public void setAlpha(int alpha) {
            paint.setAlpha(alpha);
        }

        @Override
        public void setColorFilter(ColorFilter colorFilter) {
            paint.setColorFilter(colorFilter);
        }

        @Override
        public int getOpacity() {
            return PixelFormat.TRANSLUCENT;
        }
    }

    public static class DownloadDrawable extends Drawable {
        private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        public DownloadDrawable() {
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(40 * 0.12f);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setStrokeJoin(Paint.Join.ROUND);
        }

        @Override
        public int getIntrinsicWidth() {
            return 40;
        }

        @Override
        public int getIntrinsicHeight() {
            return 40;
        }

        @Override
        public void draw(Canvas canvas) {
            float w = getBounds().width();
            float h = getBounds().height();

            canvas.drawLine(w / 2, h * 0.10f, w / 2, h * 0.60f, paint);

            Path path = new Path();
            path.moveTo(w * 0.25f, h * 0.40f);
            path.lineTo(w / 2, h * 0.65f);
            path.lineTo(w * 0.75f, h * 0.40f);
            canvas.drawPath(path, paint);

            canvas.drawLine(w * 0.15f, h * 0.85f, w * 0.85f, h * 0.85f, paint);
        }

        @Override
        public void setAlpha(int alpha) {
            paint.setAlpha(alpha);
        }

        @Override
        public void setColorFilter(ColorFilter colorFilter) {
            paint.setColorFilter(colorFilter);
        }

        @Override
        public int getOpacity() {
            return PixelFormat.TRANSLUCENT;
        }
    }
}
