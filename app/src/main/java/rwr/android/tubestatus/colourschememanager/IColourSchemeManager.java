package rwr.android.TubeStatus.ColourSchemeManager;

public interface IColourSchemeManager
{
    void cycleColourScheme();

    ColourScheme getColourScheme();

    void subscribe(IColourSchemeClient client);
}