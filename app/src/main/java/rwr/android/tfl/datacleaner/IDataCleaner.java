package rwr.android.tfl.datacleaner;

public interface IDataCleaner
{
    int transformStatusSeverity(int statusSeverity);

    String cleanReason(String reason, int statusSeverity);
}