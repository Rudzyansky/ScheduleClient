package ru.falseteam.schedule.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

public class BitmapUtils {
    public static Bitmap getCircleBitmap(Bitmap source) {
        int diam = source.getWidth();
        int radius = diam >> 1;

        final Path path = new Path();
        path.addCircle(radius, radius, radius, Path.Direction.CCW);

        Bitmap targetBitmap = Bitmap.createBitmap(diam, diam, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(targetBitmap);

        canvas.clipPath(path);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        canvas.drawBitmap(source, 0, 0, paint);

        return targetBitmap;
    }
}
