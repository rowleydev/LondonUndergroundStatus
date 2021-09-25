package rwr.android.tubestatus.colourschememanager;

public class ColourScheme
{
    private final int foreground;
    private final int background;

    public ColourScheme(int foreground, int background)
    {
        this.foreground = foreground;
        this.background = background;
    }

    public int getForeground()
    {
        return foreground;
    }

    public int getBackground()
    {
        return background;
    }
}