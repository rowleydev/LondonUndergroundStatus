package rwr.android.tubestatus.datafailurehandler;

import android.app.Activity;
import android.widget.TextView;

import rwr.android.tfl.R;

public class DataFailureHandler implements IDataFailureHandler
{
    private final Activity activity;
    private final TextView statusTextView;

    public DataFailureHandler(Activity activity, TextView statusTextView)
    {
        this.activity = activity;
        this.statusTextView = statusTextView;
    }

    @Override
    public void dataDownloadFailure()
    {
        if (activity != null && statusTextView != null)
        {
            activity.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    statusTextView.setText(R.string.waiting_caption);
                }
            });
        }
    }
}