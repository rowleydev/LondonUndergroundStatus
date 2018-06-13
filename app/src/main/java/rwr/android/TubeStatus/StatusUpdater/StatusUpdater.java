package rwr.android.TubeStatus.StatusUpdater;

import android.app.Activity;
import android.widget.TextView;

import rwr.android.TFL.R;
import rwr.android.TubeStatus.ColourSchemeManager.IColourSchemeClient;
import rwr.android.TubeStatus.ColourSchemeManager.IColourSchemeManager;

public class StatusUpdater implements IStatusUpdater, IColourSchemeClient
{
    private final Activity activity;
    private final TextView statusTextView;

    public StatusUpdater(Activity activity, TextView statusTextView, IColourSchemeManager colourSchemeManager)
    {
        this.activity = activity;
        this.statusTextView = statusTextView;
        colourSchemeManager.subscribe(this);
    }

    @Override
    public void setColourScheme(int foregroundColour, int backgroundColour)
    {
        statusTextView.setTextColor(foregroundColour);
    }

    @Override
    public void setSelectLineCaption()
    {
        if(activity != null && statusTextView != null)
        {
            activity.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    statusTextView.setText(R.string.select_caption);
                }
            });
        }
    }

    @Override
    public void setStatus(String caption)
    {
        if(activity != null && statusTextView != null)
        {
             statusTextView.setText(caption);
        }
    }
}