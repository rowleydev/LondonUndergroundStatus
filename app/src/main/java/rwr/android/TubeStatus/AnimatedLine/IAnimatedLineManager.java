package rwr.android.TubeStatus.AnimatedLine;

import android.graphics.Canvas;

import rwr.android.TFL.ITFLDataClient;
import rwr.android.TubeStatus.ColourSchemeManager.IColourSchemeClient;

public interface IAnimatedLineManager extends ITFLDataClient, IColourSchemeClient
{
    void refresh();

    void onTouchEvent(int x, int y);

    boolean allStopped();

    void updatePhysics();

    void draw(Canvas canvas);

    void reset();
}