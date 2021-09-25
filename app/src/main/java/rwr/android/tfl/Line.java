package rwr.android.tfl;

import android.graphics.Paint;

public class Line
{
    private final String name;
    private final TrainType trainType;
    private final String tflDataIdentifier;
    private final Paint paint;

    private String reason;
    private int statusSeverity;

    public Line(String name, String tflDataIdentifier, TrainType trainType, int r, int g, int b)
    {
        this.name = name;
        this.trainType = trainType;
        this.tflDataIdentifier = tflDataIdentifier;
        paint = new Paint();
        paint.setARGB(255, r, g, b);
    }

    public Line(Line line)
    {
        this.name = line.getName();
        this.tflDataIdentifier = line.getTflDataIdentifier();
        this.trainType = line.getTrainType();
        this.reason = line.getReason();
        this.statusSeverity = line.getStatusSeverity();
        this.paint = line.getPaint();
    }

    public void setStatus(int statusSeverity, String reason)
    {
        this.reason = reason;
        this.statusSeverity = statusSeverity;
    }

    public String getReason()
    {
        return reason;
    }

    public Paint getPaint()
    {
        return paint;
    }

    public String getName()
    {
        return name;
    }

    public String getTflDataIdentifier()
    {
        return tflDataIdentifier;
    }

    public TrainType getTrainType()
    {
        return trainType;
    }

    public int getStatusSeverity()
    {
        return statusSeverity;
    }
}