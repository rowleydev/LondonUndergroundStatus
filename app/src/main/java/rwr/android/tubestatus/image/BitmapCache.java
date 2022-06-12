package rwr.android.tubestatus.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Point;

import rwr.android.tfl.R;
import rwr.android.tubestatus.animatedline.TrainBitmapCollection;
import rwr.android.tfl.TrainType;
import rwr.android.util.Utilities;

public class BitmapCache implements IBitmapCache
{
    private final TrainBitmapCollection undergroundTrain;
    private final TrainBitmapCollection overgroundTrain;
    private final TrainBitmapCollection dlrTrain;
    private final TrainBitmapCollection elizabethLineTrain;

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
        int trainWidth = (int) (screenSize.x / 10.5f);

        undergroundTrain = new TrainBitmapCollection(
                BitmapFactory.decodeResource(context.getResources(), R.drawable.undergroundfront),
                BitmapFactory.decodeResource(context.getResources(), R.drawable.undergroundcentre),
                BitmapFactory.decodeResource(context.getResources(), R.drawable.undergroundback),
                trainWidth, 26 / 140f);

        overgroundTrain = new TrainBitmapCollection(
                BitmapFactory.decodeResource(context.getResources(), R.drawable.overgroundfront),
                BitmapFactory.decodeResource(context.getResources(), R.drawable.overgroundcentre),
                BitmapFactory.decodeResource(context.getResources(), R.drawable.overgroundback),
                trainWidth, 26 / 140f);

        dlrTrain = new TrainBitmapCollection(
                BitmapFactory.decodeResource(context.getResources(), R.drawable.dlrfront),
                BitmapFactory.decodeResource(context.getResources(), R.drawable.dlrcentre),
                BitmapFactory.decodeResource(context.getResources(), R.drawable.dlrback),
                trainWidth, 26 / 107f);

        elizabethLineTrain = new TrainBitmapCollection(
                BitmapFactory.decodeResource(context.getResources(), R.drawable.elizabethlinefront),
                BitmapFactory.decodeResource(context.getResources(), R.drawable.elizabethlinecentre),
                BitmapFactory.decodeResource(context.getResources(), R.drawable.elizabethlineback),
                trainWidth, 26 / 140f);

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
            case ELIZABETH_LINE:
                return elizabethLineTrain.getHeight();
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
            case ELIZABETH_LINE:
                return elizabethLineTrain.getWidth();
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
            case ELIZABETH_LINE:
                return elizabethLineTrain.getFront();
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
            case ELIZABETH_LINE:
                return elizabethLineTrain.getBack();
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
            case ELIZABETH_LINE:
                return elizabethLineTrain.getCentre();
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