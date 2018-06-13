package rwr.android.TubeStatus.AnimatedLine;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

import rwr.android.TFL.Line;
import rwr.android.TubeStatus.Image.IBitmapCache;
import rwr.android.TubeStatus.Image.PositionedBitmap;

class AnimatedLine
{
    private final Rect backgroundRect;

    private final IBitmapCache bitmapCache;

    private final Bitmap frontBitmap;
    private final Bitmap backBitmap;
    private final Bitmap centreBitmap;
    private final int bitmapWidth;
    private final int bitmapHeight;
    private final Rect carriageImageRect;

    private Bitmap featureBitmap;
    private Rect featureRect = null;

    private final Line line;

    private final int screenSizeX;

    // Animation fixed parameters
    private final float animationFinishPosition;
    private final float animationInAcceleration;
    private final float animationOutAcceleration;
    private final float animationInInitialSpeed;

    // Animation variables
    private float animationX;
    private float animationSpeed;
    private AnimationPhase animationPhase;

    public AnimatedLine(Point screenSize, Line line, Rect backgroundRect, float animationFinishPosition,
                        float animationOutAcceleration, float animationInAcceleration, float animationInInitialSpeed,
                        PositionedBitmap featurePositionedBitmap, IBitmapCache bitmapCache)
    {
        this.line = line;

        this.bitmapCache = bitmapCache;
        this.animationFinishPosition = animationFinishPosition;
        this.backgroundRect = backgroundRect;
        this.animationOutAcceleration = animationOutAcceleration;
        this.animationInAcceleration = animationInAcceleration;
        this.animationInInitialSpeed = animationInInitialSpeed;

        frontBitmap = bitmapCache.getFrontBitmap(line.getTrainType());
        centreBitmap = bitmapCache.getCentreBitmap(line.getTrainType());
        backBitmap = bitmapCache.getBackBitmap(line.getTrainType());
        bitmapHeight = bitmapCache.getTrainIconHeight(line.getTrainType());
        bitmapWidth = bitmapCache.getTrainIconWidth(line.getTrainType());

        carriageImageRect = new Rect();

        screenSizeX = screenSize.x;

        if (featurePositionedBitmap != null)
        {
            featureRect = new Rect( featurePositionedBitmap.getPosition() * screenSizeX/10,
                                    backgroundRect.bottom - featurePositionedBitmap.getHeight(),
                                    featurePositionedBitmap.getPosition() * screenSizeX/10 + featurePositionedBitmap.getWidth(),
                                    backgroundRect.bottom );

            featureBitmap = featurePositionedBitmap.getBitmap();
        }

        reset();
    }

    public String getName()
    {
        return line.getName();
    }

    public String getReason()
    {
        return line.getReason();
    }

    public void setStatus(int statusSeverity, String reason)
    {
        line.setStatus(statusSeverity, reason);
    }

    public AnimationPhase getAnimationPhase()
    {
        return animationPhase;
    }

    public void setInAnimation()
    {
         animationPhase = AnimationPhase.IN;
    }

    public void setOutAnimationIfPaused()
    {
        if (animationPhase == AnimationPhase.PAUSE)
        {
            animationPhase = AnimationPhase.OUT;
        }
    }

    public Rect getBackgroundRect()
    {
        return backgroundRect;
    }

    public void updatePhysics(double elapsed)
    {
        if (animationPhase == AnimationPhase.IN)
        {
            animationSpeed += elapsed * animationInAcceleration;

            if (animationSpeed > -0.01f)
            {
                animationSpeed = -0.01f;
            }

            animationX += elapsed * animationSpeed * screenSizeX;

            if (animationX < animationFinishPosition)
            {
                animationX = animationFinishPosition;
                animationSpeed = 0;
                animationPhase = AnimationPhase.PAUSE;
            }
        }
        else if (animationPhase == AnimationPhase.OUT)
        {
            animationSpeed += elapsed * animationOutAcceleration;
            animationX += elapsed * animationSpeed * screenSizeX;

            if (animationX < (0-screenSizeX))
            {
                animationX = screenSizeX;
                animationSpeed = animationInInitialSpeed;
                animationPhase = AnimationPhase.WAITING_FOR_DATA;
            }
        }
    }

    public void draw(Canvas canvas)
    {
        canvas.drawRect(backgroundRect, line.getPaint());

        if (featureRect != null)
        {
            canvas.drawBitmap(featureBitmap, null, featureRect, bitmapCache.getBitmapPaint());
        }

        if (line.getStatusSeverity() > 0)
        {
            setCarriageRect(0);
            canvas.drawBitmap(frontBitmap, null, carriageImageRect, bitmapCache.getBitmapPaint());

            for (int carriagePosition = 1; carriagePosition < line.getStatusSeverity() - 1; carriagePosition++)
            {
                setCarriageRect(carriagePosition);
                canvas.drawBitmap(centreBitmap, null, carriageImageRect, bitmapCache.getBitmapPaint());
            }

            if (line.getStatusSeverity() > 1)
            {
                setCarriageRect(line.getStatusSeverity() - 1);
                canvas.drawBitmap(backBitmap, null, carriageImageRect, bitmapCache.getBitmapPaint());
            }
        }
    }

    public void reset()
    {
        animationSpeed = animationInInitialSpeed;
        animationX = screenSizeX;
        animationPhase = AnimationPhase.WAITING_FOR_DATA;
    }

    private void setCarriageRect(int carriagePosition)
    {
        carriageImageRect.set((int)(bitmapWidth * carriagePosition + animationX),
                             backgroundRect.bottom - bitmapHeight,
                             (int)(bitmapWidth* (carriagePosition + 1) + animationX),
                              backgroundRect.bottom);
    }
}