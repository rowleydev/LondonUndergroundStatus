package rwr.android.tfl;

import java.util.ArrayList;

public interface ITFLDataClient
{
    void newData(ArrayList<Line> lines);
}