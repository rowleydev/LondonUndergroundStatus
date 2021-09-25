package rwr.android.tubestatus.animatedline;

import android.graphics.Canvas;

import rwr.android.tfl.ITFLDataClient;
import rwr.android.tubestatus.colourschememanager.IColourSchemeClient;

public interface IAnimatedLineManager extends ITFLDataClient, IColourSchemeClient
{
    void refresh();

    void onTouchEvent(int x, int y);

    boolean allStopped();

    void updatePhysics();

    void draw(Canvas canvas);

    void reset();
}