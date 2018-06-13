package rwr.android.TFL;

import java.util.ArrayList;

public interface ITFLDataClient
{
    void newData(ArrayList<Line> lines);
}