package rwr.android.tubestatus.image;

import android.graphics.Bitmap;
import android.graphics.Paint;

import rwr.android.tfl.TrainType;

public interface IBitmapCache
{
    int getTrainIconHeight(TrainType trainType);

    int getTrainIconWidth(TrainType trainType);

    Paint getBitmapPaint();

    Bitmap getFrontBitmap(TrainType trainType);

    Bitmap getBackBitmap(TrainType trainType);

    Bitmap getCentreBitmap(TrainType trainType);

    ScaledBitmap getBigBenBitmap();

    ScaledBitmap getDomeBitmap();

    ScaledBitmap getGherkinBitmap();

    ScaledBitmap getStPaulsBitmap();

    ScaledBitmap getBtTowerBitmap();

    Bitmap getChangeColourButtonBitmap();
}