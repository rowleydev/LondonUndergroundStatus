package rwr.android.tubestatus.colourschememanager;

import android.content.SharedPreferences;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

public class ColourSchemeManager implements IColourSchemeManager
{
    private static final String preferenceName = "ColourScheme";

    private final SharedPreferences sharedPreferences;
    private int colourSchemeIndex;
    private final ArrayList<ColourScheme> colourSchemes = new ArrayList<>();

    private final List<IColourSchemeClient> clients = new ArrayList<>();

    public ColourSchemeManager(SharedPreferences sharedPreferences)
    {
        colourSchemes.add(new ColourScheme(Color.argb(255, 255, 255, 255), Color.argb(255, 0, 68, 204)));
        colourSchemes.add(new ColourScheme(Color.argb(255, 255, 255, 255), Color.argb(255, 0, 0, 0)));
        colourSchemes.add(new ColourScheme(Color.argb(255, 255, 255, 255), Color.argb(255, 50, 210, 140)));
        colourSchemes.add(new ColourScheme(Color.argb(255, 255, 255, 255), Color.argb(255, 110, 25, 245)));
        colourSchemes.add(new ColourScheme(Color.argb(255, 255, 255, 255), Color.argb(255, 200, 40, 40)));
        colourSchemes.add(new ColourScheme(Color.argb(255, 0, 0, 0), Color.argb(255, 255, 255, 255)));

        this.sharedPreferences = sharedPreferences;

        colourSchemeIndex = sharedPreferences.getInt(preferenceName, 0);
    }

    @Override
    public void subscribe(IColourSchemeClient client)
    {
        clients.add(client);
        client.setColourScheme(getColourScheme().getForeground(), getColourScheme().getBackground());
    }

    @Override
    public void cycleColourScheme()
    {
        cycleColourSchemeIndex();

        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
        sharedPreferencesEditor.putInt(preferenceName, colourSchemeIndex);
        sharedPreferencesEditor.apply();

        for (IColourSchemeClient client : clients)
        {
            client.setColourScheme(getColourScheme().getForeground(), getColourScheme().getBackground());
        }
    }

    @Override
    public ColourScheme getColourScheme()
    {
        return colourSchemes.get(colourSchemeIndex);
    }

    private void cycleColourSchemeIndex()
    {
        colourSchemeIndex++;
        colourSchemeIndex = colourSchemeIndex % colourSchemes.size();
    }
}