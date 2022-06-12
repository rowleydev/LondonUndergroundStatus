package rwr.android.tfl.datacleaner;

import android.content.Context;

import rwr.android.tfl.R;

public class DataCleaner implements IDataCleaner
{
    private final Context context;

    public DataCleaner(Context context)
    {
        this.context = context;
    }

    @Override
    public int transformStatusSeverity(int statusSeverity)
    {
        switch (statusSeverity)
        {
            case 11:                    // Part closed
            case 12:                    // Exit only
            case 15:                    // Diverted
            case 17:                    // Issues reported
                statusSeverity = 5;
                break;
            case 13:                    // No step free access
            case 14:                    // Change of frequency
                statusSeverity = 7;
                break;
            case 18:                    // No issues
            case 19:                    // Information
                statusSeverity = 10;
                break;
            case 16:                    // Closed
            case 20:                    // Not running
                statusSeverity = 0;
        }

        if (statusSeverity < 0 || statusSeverity > 20)
        {
            // Set out of bound values to 5
            statusSeverity = 5;
        }

        return statusSeverity;
    }

    @Override
    public String cleanReason(String reason, int statusSeverity)
    {
        if (reason == null) {
            reason = "";
        }

        if (reason.isEmpty())
        {
            if (statusSeverity == 10)
            {
                reason = context.getString(R.string.good_service);
            }
            else
            {
                reason = context.getString(R.string.no_data_available);
            }
        }
        return reason;
    }
}