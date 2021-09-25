package Mocks;

import android.util.JsonReader;
import android.util.Log;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import rwr.android.tfl.IJsonEndpoint;

public class FileJsonEndpoint implements IJsonEndpoint
{
    private final String resourceName;

    public FileJsonEndpoint(String resourceName)
    {
        this.resourceName = resourceName;
    }

    @Override
    public JsonReader getReader()
    {
        try
        {
            ClassLoader classLoader = getClass().getClassLoader();
            InputStream is = classLoader.getResourceAsStream(resourceName);
            InputStreamReader responseReader = new InputStreamReader(is, StandardCharsets.UTF_8);

            return new JsonReader(responseReader);
        }
        catch (Exception ex)
        {
            Log.d("TFL", "Json reader error: " + ex.getMessage());
        }

        return null;
    }
}