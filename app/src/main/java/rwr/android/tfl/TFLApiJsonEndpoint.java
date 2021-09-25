package rwr.android.TFL;

import android.util.JsonReader;
import android.util.Log;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import rwr.android.TubeStatus.DataFailureHandler.IDataFailureHandler;

public class TFLApiJsonEndpoint implements IJsonEndpoint
{
    private final static String tflURL = "https://api.tfl.gov.uk/line/mode/tube,overground,dlr,tflrail/status";

    private final IDataFailureHandler dataFailureHandler;

    public TFLApiJsonEndpoint(IDataFailureHandler dataFailureHandler)
    {
        this.dataFailureHandler = dataFailureHandler;
    }

    @Override
    public JsonReader getReader()
    {
        try
        {
            Log.w("TFL", "Opening remote connection");

            URL endPoint = new URL(tflURL);
            HttpsURLConnection httpsConnection = (HttpsURLConnection) endPoint.openConnection();

            if (httpsConnection.getResponseCode() == 200)
            {
                InputStream serverResponse = httpsConnection.getInputStream();
                InputStreamReader responseReader = new InputStreamReader(serverResponse, "UTF-8");

                return new JsonReader(responseReader);
            }
            else
            {
                throw new Exception("Bad response code: " + httpsConnection.getResponseCode());
            }
        }
        catch (Exception ex)
        {
            Log.w("TFL", "Remote connection error: " + ex.getMessage());

            if(dataFailureHandler != null)
            {
                dataFailureHandler.dataDownloadFailure();
            }
        }

        return null;
    }
}