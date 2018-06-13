import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import rwr.android.TFL.DataCleaner.DataCleaner;
import rwr.android.TFL.DataCleaner.IDataCleaner;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class DataCleanerTests
{
    private IDataCleaner dataCleaner;

    @Before
    public void setup()
    {
        dataCleaner = new DataCleaner(InstrumentationRegistry.getTargetContext());
    }

    @Test
    public void StatusSeverity_mappings()
    {
        assertEquals(5, dataCleaner.transformStatusSeverity(-10));
        assertEquals(5, dataCleaner.transformStatusSeverity(-1));

        assertEquals(0, dataCleaner.transformStatusSeverity(0));

        assertEquals(1, dataCleaner.transformStatusSeverity(1));
        assertEquals(2, dataCleaner.transformStatusSeverity(2));
        assertEquals(3, dataCleaner.transformStatusSeverity(3));
        assertEquals(4, dataCleaner.transformStatusSeverity(4));
        assertEquals(5, dataCleaner.transformStatusSeverity(5));
        assertEquals(6, dataCleaner.transformStatusSeverity(6));
        assertEquals(7, dataCleaner.transformStatusSeverity(7));
        assertEquals(8, dataCleaner.transformStatusSeverity(8));
        assertEquals(9, dataCleaner.transformStatusSeverity(9));
        assertEquals(10, dataCleaner.transformStatusSeverity(10));

        assertEquals(5, dataCleaner.transformStatusSeverity(11));
        assertEquals(5, dataCleaner.transformStatusSeverity(12));
        assertEquals(7, dataCleaner.transformStatusSeverity(13));
        assertEquals(7, dataCleaner.transformStatusSeverity(14));
        assertEquals(5, dataCleaner.transformStatusSeverity(15));
        assertEquals(0, dataCleaner.transformStatusSeverity(16));
        assertEquals(5, dataCleaner.transformStatusSeverity(17));
        assertEquals(10, dataCleaner.transformStatusSeverity(18));
        assertEquals(10, dataCleaner.transformStatusSeverity(19));
        assertEquals(0, dataCleaner.transformStatusSeverity(20));

        assertEquals(5, dataCleaner.transformStatusSeverity(21));
        assertEquals(5, dataCleaner.transformStatusSeverity(30));
    }

    @Test
    public void Clean_blank_reason()
    {
        assertEquals("Good service.", dataCleaner.cleanReason("", 10));
        assertEquals("No data available.", dataCleaner.cleanReason("", 5));
    }

    @Test
    public void Clean_normal_reason_should_not_change()
    {
        assertEquals("This reason should not change", dataCleaner.cleanReason("This reason should not change", 10));
        assertEquals("This reason should not change", dataCleaner.cleanReason("This reason should not change", 5));
    }
}