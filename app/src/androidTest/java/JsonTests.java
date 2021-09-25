import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import Mocks.FileJsonEndpoint;
import rwr.android.tfl.datacleaner.DataCleaner;
import rwr.android.tfl.datacleaner.IDataCleaner;
import rwr.android.tfl.IJsonEndpoint;
import rwr.android.tfl.LineStatusUpdate;
import rwr.android.tfl.TFLApiDataProvider;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class JsonTests
{
    private List<LineStatusUpdate> statusUpdates;

    @Before
    public void setup()
    {
        IJsonEndpoint jsonEndPoint = new FileJsonEndpoint("sample.json");
        IDataCleaner dataCleaner = new DataCleaner(InstrumentationRegistry.getTargetContext());

        TFLApiDataProvider tfl = new TFLApiDataProvider(jsonEndPoint, dataCleaner);
        statusUpdates = tfl.getData();
    }

    @Test
    public void Data_count()
    {
        assertEquals(statusUpdates.size(), 14);
    }

    @Test
    public void Id_parse()
    {
        assertEquals(statusUpdates.get(0).getIdentifier(), "bakerloo");
        assertEquals(statusUpdates.get(1).getIdentifier(), "central");
        assertEquals(statusUpdates.get(2).getIdentifier(), "circle");
        assertEquals(statusUpdates.get(3).getIdentifier(), "district");
        assertEquals(statusUpdates.get(4).getIdentifier(), "dlr");
        assertEquals(statusUpdates.get(5).getIdentifier(), "hammersmith-city");
        assertEquals(statusUpdates.get(6).getIdentifier(), "jubilee");
        assertEquals(statusUpdates.get(7).getIdentifier(), "london-overground");
        assertEquals(statusUpdates.get(8).getIdentifier(), "metropolitan");
        assertEquals(statusUpdates.get(9).getIdentifier(), "northern");
        assertEquals(statusUpdates.get(10).getIdentifier(), "piccadilly");
        assertEquals(statusUpdates.get(11).getIdentifier(), "tfl-rail");
        assertEquals(statusUpdates.get(12).getIdentifier(), "victoria");
        assertEquals(statusUpdates.get(13).getIdentifier(), "waterloo-city");
    }

    @Test
    public void Status_severity_parse()
    {
        assertEquals(statusUpdates.get(5).getStatusSeverity(), 10);
        assertEquals(statusUpdates.get(7).getStatusSeverity(), 1);
        assertEquals(statusUpdates.get(9).getStatusSeverity(), 5);
        assertEquals(statusUpdates.get(11).getStatusSeverity(), 4);
    }

    @Test
    public void Reason_parse()
    {
        assertEquals(statusUpdates.get(7).getReason(), "LONDON OVERGROUND: Sample reason");
        assertEquals(statusUpdates.get(9).getReason(), "NORTHERN LINE: Sample reason");
        assertEquals(statusUpdates.get(11).getReason(), "TFL RAIL: Sample reason");
    }
}