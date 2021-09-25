package rwr.android.tfl;

import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import rwr.android.tfl.datacleaner.IDataCleaner;

public class TFLApiDataProvider implements IDataProvider
{
    private final IDataCleaner dataCleaner;
    private final IJsonEndpoint jsonEndpoint;

    private final ArrayList<LineStatusUpdate> lines = new ArrayList<>();

    public TFLApiDataProvider(IJsonEndpoint jsonEndpoint, IDataCleaner dataCleaner)
    {
        this.jsonEndpoint = jsonEndpoint;
        this.dataCleaner = dataCleaner;
    }

    @Override
    public ArrayList<LineStatusUpdate> getData()
    {
        lines.clear();

        JsonReader jsonReader = jsonEndpoint.getReader();

        try
        {
            jsonReader.beginArray();
            while (jsonReader.hasNext())
            {
                readLineData(jsonReader);
            }
            return lines;
        }
        catch (Exception ex)
        {
            Log.w("TFL", "Json Reader error: " + ex.getMessage());
        }

        return null;
    }

    private void readLineData(JsonReader jsonReader) throws IOException
    {
        jsonReader.beginObject();

        // Data to capture
        String identifier = "";
        String reason = "";
        int statusSeverity = 10;

        while (jsonReader.hasNext())
        {
            String name = jsonReader.nextName();

            if (name.equals("id"))
            {
                identifier = jsonReader.nextString();
            }
            else if (name.equals("lineStatuses"))
            {
                jsonReader.beginArray();
                while (jsonReader.hasNext())
                {
                    jsonReader.beginObject();
                    while (jsonReader.hasNext())
                    {
                        String statusType = jsonReader.nextName();

                        switch (statusType)
                        {
                            case "statusSeverity":

                                int statusSeverityValue = dataCleaner.transformStatusSeverity(jsonReader.nextInt());

                                // Keep worst severity only
                                if (statusSeverityValue < statusSeverity)
                                {
                                    statusSeverity = statusSeverityValue;
                                }

                                break;

                            case "reason":

                                reason = jsonReader.nextString();
                                break;

                            default:

                                jsonReader.skipValue();
                        }
                    }
                    jsonReader.endObject();
                }
                jsonReader.endArray();
            }
            else
            {
                jsonReader.skipValue();
            }
        }
        jsonReader.endObject();

        // Sanitise the reason
        reason = dataCleaner.cleanReason(reason, statusSeverity);

        lines.add(new LineStatusUpdate(identifier, statusSeverity, reason));
    }
}