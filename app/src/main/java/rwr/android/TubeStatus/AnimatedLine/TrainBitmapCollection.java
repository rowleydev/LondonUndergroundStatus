package rwr.android.TubeStatus.AnimatedLine;

import android.graphics.Bitmap;

public class TrainBitmapCollection
{
    private final Bitmap front;
    private final Bitmap centre;
    private final Bitmap back;
    private final int width;
    private final int height;

    public TrainBitmapCollection(Bitmap front, Bitmap centre, Bitmap back, float width, float heightRatio)
    {
        this.front = front;
        this.centre = centre;
        this.back = back;

        this.width = (int)width;
        this.height = (int)(width * heightRatio);
    }

    public Bitmap getFront()
    {
        return front;
    }

    public Bitmap getCentre()
    {
        return centre;
    }

    public Bitmap getBack()
    {
        return back;
    }

    public int getHeight()
    {
        return height;
    }

    public int getWidth()
    {
        return width;
    }
}