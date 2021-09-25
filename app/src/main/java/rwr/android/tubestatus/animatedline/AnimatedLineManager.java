package rwr.android.TubeStatus.AnimatedLine;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.HashMap;

import rwr.android.TFL.ITFLDataManager;
import rwr.android.TFL.Line;
import rwr.android.TubeStatus.StatusUpdater.IStatusUpdater;
import rwr.android.TubeStatus.Image.IBitmapCache;
import rwr.android.TubeStatus.Image.PositionedBitmap;

public class AnimatedLineManager implements IAnimatedLineManager
{
    private final long animationStaggerNS = 200000000;

    private AnimationPhase animationPhase = AnimationPhase.WAITING_FOR_DATA;
    private long animationStartTime = -1;
    private final Point screenSize;
    private double previousRenderTime = System.nanoTime();

    private final Rect gridBottomPaddingRect;
    private final Paint backgroundPaint = new Paint();

    private final ArrayList<AnimatedLine> animatedLines = new ArrayList<>();

    private final ITFLDataManager tflDataManager;
    private final IBitmapCache bitmapCache;
    private final IAnimatedLineGridLayout gridLayout;
    private final IStatusUpdater statusUpdater;

    private boolean newDataAvailable = false;

    public AnimatedLineManager(ITFLDataManager tflDataManager, IBitmapCache bitmapCache, IAnimatedLineGridLayout gridLayout, IStatusUpdater statusUpdater, Point screenSize)
    {
        this.screenSize = screenSize;
        this.bitmapCache = bitmapCache;
        this.gridLayout = gridLayout;
        this.tflDataManager = tflDataManager;
        this.statusUpdater = statusUpdater;

        int lastRowEnd = (tflDataManager.getData().size()) * gridLayout.getRowHeight() + gridLayout.getGridOffset();
        gridBottomPaddingRect = new Rect(0, lastRowEnd, screenSize.x, lastRowEnd + gridLayout.getRowHeight()*2);

        populateInitialLineData(tflDataManager.getData());

        tflDataManager.subscribe(this);
    }

    @Override
    public void refresh()
    {
        if (allStopped())
        {
            tflDataManager.requestDataRefresh();
            animationPhase = AnimationPhase.OUT;
        }
    }

    @Override
    public boolean allStopped()
    {
        return (animationPhase == AnimationPhase.PAUSE) && (animationPhaseComplete(AnimationPhase.PAUSE));
    }

    @Override
    public void setColourScheme(int foregroundColour, int backgroundColour)
    {
        backgroundPaint.setColor(backgroundColour);
    }

    @Override
    public void draw(Canvas canvas)
    {
        for (AnimatedLine line : animatedLines)
        {
            line.draw(canvas);
        }
        canvas.drawRect(gridBottomPaddingRect, backgroundPaint);
    }

    @Override
    public void updatePhysics()
    {
        long now = System.nanoTime();
        double elapsed = (now - previousRenderTime) / 1e9;

        for (AnimatedLine line : animatedLines)
        {
            line.updatePhysics(elapsed);
        }

        updateStaggeredAnimationPhase(now);

        // Populate new data only when in appropriate animation stage
        if (newDataAvailable && allWaitingForData())
        {
            newDataAvailable = false;
            repopulateLineData(tflDataManager.getData());
            animationPhase = AnimationPhase.IN;

            statusUpdater.setSelectLineCaption();
        }

        previousRenderTime = now;
    }

    @Override
    public void reset()
    {
        for (AnimatedLine line : animatedLines)
        {
            line.reset();
        }

        animationPhase = AnimationPhase.WAITING_FOR_DATA;
        animationStartTime = -1;
        newDataAvailable = false;
    }

    @Override
    public void newData(ArrayList<Line> lines)
    {
        newDataAvailable = true;
    }

    @Override
    public void onTouchEvent(int x, int y)
    {
        if (animationPhase == AnimationPhase.IN || animationPhase == AnimationPhase.PAUSE)
        {
            for (AnimatedLine line : animatedLines)
            {
                if (line.getBackgroundRect().contains(x, y))
                {
                    // Deal with the common case of the status starting with the name of the line
                    if (line.getReason().toUpperCase().startsWith(line.getName().toUpperCase()))
                    {
                        statusUpdater.setStatus(line.getReason());
                    }
                    else
                    {
                        statusUpdater.setStatus(line.getName().toUpperCase() + ": " + line.getReason());
                    }
                    return;
                }
            }
        }
    }

    private boolean allWaitingForData()
    {
        return (animationPhase == AnimationPhase.WAITING_FOR_DATA) && (animationPhaseComplete(AnimationPhase.WAITING_FOR_DATA));
    }

    private void updateStaggeredAnimationPhase(long now)
    {
        switch (animationPhase)
        {
            case IN:
                if (animationStartTime < 0)
                {
                    animationStartTime = now;
                }

                for (int i = 0; i < animatedLines.size(); i++)
                {
                    // Stagger the line animations
                    if ((now - animationStartTime) > (i * animationStaggerNS))
                    {
                        animatedLines.get(i).setInAnimation();

                        if (i == animatedLines.size() - 1)
                        {
                            animationPhase = AnimationPhase.PAUSE;
                            animationStartTime = -1;
                        }
                    }
                }
                break;

            case OUT:
                if (animationStartTime < 0)
                {
                    animationStartTime = now;
                }

                for (int i = 0; i < animatedLines.size(); i++)
                {
                    if ((now - animationStartTime) > (i * animationStaggerNS))
                    {
                        animatedLines.get(i).setOutAnimationIfPaused();

                        if (i == animatedLines.size() - 1)
                        {
                            animationPhase = AnimationPhase.WAITING_FOR_DATA;
                            animationStartTime = -1;
                        }
                    }
                }
                break;
        }
    }

    private boolean animationPhaseComplete(AnimationPhase targetPhase)
    {
        boolean animationComplete = true;
        for (AnimatedLine line : animatedLines)
        {
            if (line.getAnimationPhase() != targetPhase)
            {
                animationComplete = false;
            }
        }

        return animationComplete;
    }

    private void repopulateLineData(ArrayList<Line> lines)
    {
        for(int i = 0; i < animatedLines.size(); i++)
        {
            Line line = lines.get(i);
            animatedLines.get(i).setStatus(line.getStatusSeverity(), line.getReason());
        }
    }

    private void populateInitialLineData(ArrayList<Line> lines)
    {
        HashMap<String, PositionedBitmap> featureImages = new HashMap<>();
        featureImages.put("Jubilee Line", new PositionedBitmap(bitmapCache.getDomeBitmap(), 1));
        featureImages.put("District Line", new PositionedBitmap(bitmapCache.getBigBenBitmap(), 8));
        featureImages.put("Waterloo & City Line", new PositionedBitmap(bitmapCache.getGherkinBitmap(), 3));
        featureImages.put("Central Line", new PositionedBitmap(bitmapCache.getStPaulsBitmap(), 3));
        featureImages.put("Victoria Line", new PositionedBitmap(bitmapCache.getBtTowerBitmap(), 9));

        for (int i = 0; i < lines.size(); i++)
        {
            int top = i * gridLayout.getRowHeight() + gridLayout.getGridOffset();

            // Copy line from the data manager, enabling independence from remote data update events
            Line line = new Line(lines.get(i));

            animatedLines.add(new AnimatedLine(screenSize,
                    line,
                    new Rect(0, top, screenSize.x, top + gridLayout.getRowHeight()),
                    gridLayout.getAnimationFinishPosition(),
                    -1f,
                    0.5f,
                    -1.0f,
                    featureImages.get(lines.get(i).getName()),
                    bitmapCache
            ));
        }
    }
}