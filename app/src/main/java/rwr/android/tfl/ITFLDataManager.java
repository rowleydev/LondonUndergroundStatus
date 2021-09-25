package rwr.android.tfl;

import java.util.ArrayList;

public interface ITFLDataManager
{
    ArrayList<Line> getData();

    String getRefreshTimeString();

    void requestDataRefresh();

    void maintainData();

    void subscribe(ITFLDataClient client);
}