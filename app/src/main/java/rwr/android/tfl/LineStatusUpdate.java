package rwr.android.TFL;

public class LineStatusUpdate
{
    private final int statusSeverity;
    private final String reason;
    private final String identifier;

    public LineStatusUpdate(String identifier, int statusSeverity, String reason)
    {
        this.identifier = identifier;
        this.reason = reason;
        this.statusSeverity = statusSeverity;
    }

    public int getStatusSeverity()
    {
        return statusSeverity;
    }

    public String getReason()
    {
        return reason;
    }

    public String getIdentifier()
    {
        return identifier;
    }
}