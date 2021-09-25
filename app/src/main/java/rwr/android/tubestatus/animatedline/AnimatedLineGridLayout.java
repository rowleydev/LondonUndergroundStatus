package rwr.android.TubeStatus.AnimatedLine;

import android.graphics.Point;

public class AnimatedLineGridLayout implements IAnimatedLineGridLayout
{
    private final int rowHeight;
    private final float animationFinishPosition;
    private final int gridOffset;

    public AnimatedLineGridLayout(Point screenSize, int lineCount, float screenFraction)
    {
        animationFinishPosition = (float)(screenSize.x / 40);
        rowHeight = (int)((screenSize.y * screenFraction) / (lineCount + 1));
        gridOffset = rowHeight;
    }

    @Override
    public int getRowHeight()
    {
        return rowHeight;
    }

    @Override
    public float getAnimationFinishPosition()
    {
        return animationFinishPosition;
    }

    @Override
    public int getGridOffset()
    {
        return gridOffset;
    }
}