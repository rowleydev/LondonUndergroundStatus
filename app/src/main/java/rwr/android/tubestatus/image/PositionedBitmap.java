package rwr.android.tubestatus.image;

public class PositionedBitmap extends ScaledBitmap
{
    private final int position;

    public PositionedBitmap(ScaledBitmap scaledBitmap, int position)
    {
        super(scaledBitmap);
        this.position = position;
    }

    public int getPosition()
    {
        return position;
    }
}