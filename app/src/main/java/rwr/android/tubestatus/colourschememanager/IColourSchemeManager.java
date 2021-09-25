package rwr.android.tubestatus.colourschememanager;

public interface IColourSchemeManager
{
    void cycleColourScheme();

    ColourScheme getColourScheme();

    void subscribe(IColourSchemeClient client);
}