package rwr.android.tubestatus;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;

import rwr.android.tfl.ITFLDataManager;
import rwr.android.tubestatus.animatedline.IAnimatedLineGridLayout;
import rwr.android.tubestatus.colourschememanager.IColourSchemeClient;
import rwr.android.tubestatus.colourschememanager.IColourSchemeManager;
import rwr.android.tubestatus.image.IBitmapCache;

public class MainView implements IColourSchemeClient
{
    private final IBitmapCache bitmapCache;
    private final IColourSchemeManager colourSchemeManager;
    private final ITFLDataManager tflDataManager;
    private final IAnimatedLineGridLayout gridLayout;

    private Paint textPaint;
    private final Paint backgroundPaint = new Paint();
    private final int titlePosition;
    private final String datePrefix;

    private final Rect refreshTimeTextRect;

    private Rect changeColourIconRect;
    private Rect changeColourButtonRect;

    public MainView(ITFLDataManager tflDataManager, IBitmapCache bitmapCache, IAnimatedLineGridLayout gridLayout, IColourSchemeManager colourSchemeManager, String datePrefix, Point screenSize)
    {
        this.colourSchemeManager = colourSchemeManager;
        this.tflDataManager = tflDataManager;
        this.gridLayout = gridLayout;
        this.bitmapCache = bitmapCache;

        createTextPaint();

        Rect bounds = new Rect();
        textPaint.getTextBounds(datePrefix + " - 1234567890", 0, 23, bounds);
        int titleOffset = (int) ((gridLayout.getRowHeight() - bounds.height()) / 2.0);
        titlePosition = gridLayout.getGridOffset() - titleOffset;
        this.datePrefix = datePrefix;

        refreshTimeTextRect = new Rect(0, 0, screenSize.x, gridLayout.getRowHeight());

        colourSchemeManager.subscribe(this);

        createColourSchemeButton(screenSize);
    }

    public void draw(Canvas canvas)
    {
        canvas.drawRect(refreshTimeTextRect, backgroundPaint);

        if (!tflDataManager.getRefreshTimeString().isEmpty())
        {
            canvas.drawText(datePrefix + " - " + tflDataManager.getRefreshTimeString(), gridLayout.getAnimationFinishPosition(), titlePosition, textPaint);
        }
        else
        {
            canvas.drawText(datePrefix, gridLayout.getAnimationFinishPosition(), titlePosition, textPaint);
        }

        canvas.drawBitmap(bitmapCache.getChangeColourButtonBitmap(), null, changeColourIconRect, bitmapCache.getBitmapPaint());
    }

    public void onTouchEvent(int x, int y)
    {
        if (changeColourButtonRect.contains(x, y))
        {
            colourSchemeManager.cycleColourScheme();
        }
    }

    @Override
    public void setColourScheme(int foregroundColour, int backgroundColour)
    {
        backgroundPaint.setColor(backgroundColour);
        textPaint.setColor(foregroundColour);
    }

    private void createTextPaint()
    {
        textPaint = new Paint();
        textPaint.setTypeface(Typeface.SANS_SERIF);
        textPaint.setTextSize((int) (gridLayout.getRowHeight() * 0.6));
        textPaint.setAntiAlias(true);
        textPaint.setFilterBitmap(true);
    }

    private void createColourSchemeButton(Point screenSize)
    {
        int colourChangeIconSize = (int) (gridLayout.getRowHeight() * 0.7);
        int colourChangeMargin = (int) (gridLayout.getRowHeight() * 0.1);

        changeColourIconRect = new Rect(screenSize.x - colourChangeMargin - colourChangeIconSize,
                (gridLayout.getRowHeight() - colourChangeIconSize) / 2,
                screenSize.x - colourChangeMargin,
                gridLayout.getRowHeight() - (gridLayout.getRowHeight() - colourChangeIconSize) / 2);

        changeColourButtonRect = new Rect(screenSize.x - colourChangeMargin - (int) (colourChangeIconSize * 2.0),
                0,
                screenSize.x,
                gridLayout.getRowHeight());
    }
}