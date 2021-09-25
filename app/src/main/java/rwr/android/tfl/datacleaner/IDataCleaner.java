package rwr.android.TFL.DataCleaner;

public interface IDataCleaner
{
    int transformStatusSeverity(int statusSeverity);

    String cleanReason(String reason, int statusSeverity);
}