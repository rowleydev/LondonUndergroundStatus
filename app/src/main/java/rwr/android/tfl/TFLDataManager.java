package rwr.android.tfl;

import android.os.AsyncTask;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TFLDataManager implements ITFLDataManager
{
    private final long maxRetryTimeNS = 1000000000;

    private final ArrayList<Line> lines = new ArrayList<>();
    private String refreshTimeString = "";

    private Boolean gettingData = false;
    private Long lastRequestAttemptTime = null;
    private Boolean needNewData = true;

    private final IDataProvider dataGetter;

    private final ArrayList<ITFLDataClient> clients = new ArrayList<>();

    public TFLDataManager(IDataProvider dataProvider)
    {
        // Define the lines we are expecting from the returned JSON, we do not trust the remote data source
        lines.add(new Line("Bakerloo Line", "bakerloo", TrainType.UNDERGROUND, 140, 80, 30));
        lines.add(new Line("Central Line", "central", TrainType.UNDERGROUND, 235, 5, 5));
        lines.add(new Line("Circle Line", "circle", TrainType.UNDERGROUND, 255, 230, 0));
        lines.add(new Line("District Line", "district", TrainType.UNDERGROUND, 0, 180, 20));
        lines.add(new Line("DLR", "dlr", TrainType.DLR, 0, 175, 175));
        lines.add(new Line("Hammersmith & City Line", "hammersmith-city", TrainType.UNDERGROUND, 230, 130, 230));
        lines.add(new Line("Jubilee Line", "jubilee", TrainType.UNDERGROUND, 150, 150, 150));
        lines.add(new Line("Metropolitan Line", "metropolitan", TrainType.UNDERGROUND, 130, 20, 75));
        lines.add(new Line("Northern Line", "northern", TrainType.UNDERGROUND, 0, 0, 0));
        lines.add(new Line("Piccadilly Line", "piccadilly", TrainType.UNDERGROUND, 0, 0, 200));
        lines.add(new Line("Victoria Line", "victoria", TrainType.UNDERGROUND, 50, 150, 255));
        lines.add(new Line("Waterloo & City Line", "waterloo-city", TrainType.UNDERGROUND, 120, 210, 190));
        lines.add(new Line("London Overground", "london-overground", TrainType.OVERGROUND, 230, 100, 15));
        lines.add(new Line("Elizabeth Line", "elizabeth", TrainType.ELIZABETH_LINE, 105, 80, 161));

        this.dataGetter = dataProvider;

        requestDataRefresh();
    }

    @Override
    public void subscribe(ITFLDataClient client)
    {
        clients.add(client);
    }

    @Override
    public ArrayList<Line> getData()
    {
        return lines;
    }

    @Override
    public String getRefreshTimeString()
    {
        return refreshTimeString;
    }

    @Override
    public void requestDataRefresh()
    {
        needNewData = true;
    }

    @Override
    public void maintainData()
    {
        if (needNewData && !gettingData)
        {
            if (lastRequestAttemptTime == null || (System.nanoTime() > lastRequestAttemptTime + maxRetryTimeNS)) // Limit retry rate
            {
                gettingData = true;

                Log.w("TFL", "Starting data provider thread");
                AsyncTask.execute(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        lastRequestAttemptTime = System.nanoTime();

                        ArrayList<LineStatusUpdate> lineStatusUpdates = dataGetter.getData();

                        if (lineStatusUpdates != null)
                        {
                            Log.w("TFL", "Updating local data");

                            // Update local data
                            for (LineStatusUpdate lineStatusUpdate : lineStatusUpdates)
                            {
                                // Do not fully trust the data provider, perform a lookup on the identifier field and then validate data
                                Line currentLine = getLine(lineStatusUpdate.getIdentifier().toLowerCase());
                                if (currentLine != null)
                                {
                                    if (lineStatusUpdate.getStatusSeverity() >= 0 && lineStatusUpdate.getStatusSeverity() <= 10)
                                    {
                                        currentLine.setStatus(lineStatusUpdate.getStatusSeverity(), lineStatusUpdate.getReason());
                                    }
                                }
                            }

                            // Update refresh time string
                            Date now = Calendar.getInstance().getTime();
                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss, d MMM");
                            refreshTimeString = sdf.format(now);

                            // Tell clients new data is ready
                            Log.w("TFL", "Informing clients of new data");
                            for (ITFLDataClient client : clients)
                            {
                                client.newData(lines);
                            }

                            needNewData = false;
                        }

                        gettingData = false;

                        Log.w("TFL", "Ending data provider thread");
                    }
                });
            }
        }
    }

    private Line getLine(String lineName)
    {
        for (Line line : lines)
        {
            if (line.getTflDataIdentifier().equals(lineName))
            {
                return line;
            }
        }

        return null;
    }
}
