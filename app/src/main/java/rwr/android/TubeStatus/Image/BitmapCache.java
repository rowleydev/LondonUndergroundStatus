package rwr.android.TubeStatus.Image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Point;

import rwr.android.TFL.R;
import rwr.android.TubeStatus.AnimatedLine.TrainBitmapCollection;
import rwr.android.TFL.TrainType;
import rwr.android.Util.Utilities;

public class BitmapCache implements IBitmapCache
{
    private final TrainBitmapCollection undergroundTrain;
    private final TrainBitmapCollection overgroundTrain;
    private final TrainBitmapCollection dlrTrain;
    private final TrainBitmapCollection tflRailTrain;

    private final ScaledBitmap bigBenBitmap;
    private final ScaledBitmap domeBitmap;
    private final ScaledBitmap gherkinBitmap;
    private final ScaledBitmap stPaulsBitmap;
    private final ScaledBitmap btTowerBitmap;

    private final Bitmap changeColourButtonBitmap;

    private final Paint bitmapPaint;

    public BitmapCache(Context context, int rowHeight)
    {
        Point screenSize = Utilities.getScreenSize(context);
        int trainWidth = (int)(screenSize.x / 10.5f);

        undergroundTrain = new TrainBitmapCollection(
                BitmapFactory.decodeResource(context.getResources(), R.drawable.undergroundfront),
                BitmapFactory.decodeResource(context.getResources(), R.drawable.undergroundcentre),
                BitmapFactory.decodeResource(context.getResources(), R.drawable.undergroundback),
                trainWidth, 26/140f );

        overgroundTrain = new TrainBitmapCollection(
                BitmapFactory.decodeResource(context.getResources(), R.drawable.overgroundfront),
                BitmapFactory.decodeResource(context.getResources(), R.drawable.overgroundcentre),
                BitmapFactory.decodeResource(context.getResources(), R.drawable.overgroundback),
                trainWidth, 26/140f );

        dlrTrain = new TrainBitmapCollection(
                BitmapFactory.decodeResource(context.getResources(), R.drawable.dlrfront),
                BitmapFactory.decodeResource(context.getResources(), R.drawable.dlrcentre),
                BitmapFactory.decodeResource(context.getResources(), R.drawable.dlrback),
                trainWidth, 26/107f );

        tflRailTrain = new TrainBitmapCollection(
                BitmapFactory.decodeResource(context.getResources(), R.drawable.tflrailfront),
                BitmapFactory.decodeResource(context.getResources(), R.drawable.tflrailcentre),
                BitmapFactory.decodeResource(context.getResources(), R.drawable.tflrailback),
                trainWidth, 26/140f );

        bigBenBitmap = new ScaledBitmap(
                BitmapFactory.decodeResource(context.getResources(), R.drawable.bigben),
                rowHeight * 0.9f);

        domeBitmap = new ScaledBitmap(
                BitmapFactory.decodeResource(context.getResources(), R.drawable.dome),
                rowHeight * 0.9f);

        gherkinBitmap = new ScaledBitmap(
                BitmapFactory.decodeResource(context.getResources(), R.drawable.gherkin),
                rowHeight * 0.9f);

        stPaulsBitmap = new ScaledBitmap(
                BitmapFactory.decodeResource(context.getResources(), R.drawable.stpauls),
                rowHeight * 0.9f);

        btTowerBitmap = new ScaledBitmap(
                BitmapFactory.decodeResource(context.getResources(), R.drawable.bttower),
                rowHeight * 0.9f);

        changeColourButtonBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.colourbutton);

        bitmapPaint = new Paint();
        bitmapPaint.setAntiAlias(true);
        bitmapPaint.setFilterBitmap(true);
    }

    @Override
    public int getTrainIconHeight(TrainType trainType)
    {
        switch (trainType)
        {
            case UNDERGROUND:
                return undergroundTrain.getHeight();
            case DLR:
                return dlrTrain.getHeight();
            case OVERGROUND:
                return overgroundTrain.getHeight();
            case TFL_RAIL:
                return tflRailTrain.getHeight();
            default:
                return 0;
        }
    }

    @Override
    public int getTrainIconWidth(TrainType trainType)
    {
        switch (trainType)
        {
            case UNDERGROUND:
                return undergroundTrain.getWidth();
            case DLR:
                return dlrTrain.getWidth();
            case OVERGROUND:
                return overgroundTrain.getWidth();
            case TFL_RAIL:
                return tflRailTrain.getWidth();
            default:
                return 0;
        }
    }

    @Override
    public Paint getBitmapPaint()
    {
        return bitmapPaint;
    }

    @Override
    public Bitmap getFrontBitmap(TrainType trainType)
    {
        switch (trainType)
        {
            case UNDERGROUND:
                return undergroundTrain.getFront();
            case DLR:
                return dlrTrain.getFront();
            case OVERGROUND:
                return overgroundTrain.getFront();
            case TFL_RAIL:
                return tflRailTrain.getFront();
            default:
                return null;
        }
    }

    @Override
    public Bitmap getBackBitmap(TrainType trainType)
    {
        switch (trainType)
        {
            case UNDERGROUND:
                return undergroundTrain.getBack();
            case DLR:
                return dlrTrain.getBack();
            case OVERGROUND:
                return overgroundTrain.getBack();
            case TFL_RAIL:
                return tflRailTrain.getBack();
            default:
                return null;
        }
    }

    @Override
    public Bitmap getCentreBitmap(TrainType trainType)
    {
        switch (trainType)
        {
            case UNDERGROUND:
                return undergroundTrain.getCentre();
            case DLR:
                return dlrTrain.getCentre();
            case OVERGROUND:
                return overgroundTrain.getCentre();
            case TFL_RAIL:
                return tflRailTrain.getCentre();
            default:
                return null;
        }
    }

    @Override
    public ScaledBitmap getBigBenBitmap()
    {
        return bigBenBitmap;
    }

    @Override
    public ScaledBitmap getDomeBitmap()
    {
        return domeBitmap;
    }

    @Override
    public ScaledBitmap getGherkinBitmap()
    {
        return gherkinBitmap;
    }

    @Override
    public ScaledBitmap getStPaulsBitmap()
    {
        return stPaulsBitmap;
    }

    @Override
    public ScaledBitmap getBtTowerBitmap()
    {
        return btTowerBitmap;
    }

    @Override
    public Bitmap getChangeColourButtonBitmap()
    {
        return changeColourButtonBitmap;
    }
}