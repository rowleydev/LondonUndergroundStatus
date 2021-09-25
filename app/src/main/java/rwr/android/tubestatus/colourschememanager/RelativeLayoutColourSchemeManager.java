package rwr.android.TubeStatus.ColourSchemeManager;

import android.widget.RelativeLayout;

public class RelativeLayoutColourSchemeManager implements IColourSchemeClient
{
    private final RelativeLayout relativeLayout;

    public RelativeLayoutColourSchemeManager(RelativeLayout relativeLayout)
    {
        this.relativeLayout = relativeLayout;
    }

    @Override
    public void setColourScheme(int foregroundColour, int backgroundColour)
    {
        relativeLayout.setBackgroundColor(backgroundColour);
    }
}