package rwr.android.tubestatus.image;

import android.graphics.Bitmap;

public class ScaledBitmap
{
    private final Bitmap bitmap;
    private final int width;
    private final int height;

    ScaledBitmap(ScaledBitmap scaledBitmap)
    {
        this.bitmap = scaledBitmap.bitmap;
        this.width = scaledBitmap.width;
        this.height = scaledBitmap.height;
    }

    public ScaledBitmap(Bitmap bitmap, float height)
    {
        this.bitmap = bitmap;
        this.height = (int) height;
        this.width = (int) (height * bitmap.getWidth() / bitmap.getHeight());
    }

    public Bitmap getBitmap()
    {
        return bitmap;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }
}