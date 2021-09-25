package rwr.android.tubestatus;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Point;
import android.preference.PreferenceManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import rwr.android.tfl.datacleaner.DataCleaner;
import rwr.android.tfl.datacleaner.IDataCleaner;
import rwr.android.tfl.IDataProvider;
import rwr.android.tfl.IJsonEndpoint;
import rwr.android.tfl.ITFLDataManager;
import rwr.android.tfl.R;
import rwr.android.tfl.TFLApiDataProvider;
import rwr.android.tfl.TFLApiJsonEndpoint;
import rwr.android.tubestatus.animatedline.AnimatedLineGridLayout;
import rwr.android.tubestatus.animatedline.AnimatedLineManager;
import rwr.android.tubestatus.animatedline.IAnimatedLineGridLayout;
import rwr.android.tubestatus.animatedline.IAnimatedLineManager;
import rwr.android.tubestatus.colourschememanager.IColourSchemeManager;
import rwr.android.tubestatus.colourschememanager.RelativeLayoutColourSchemeManager;
import rwr.android.tubestatus.datafailurehandler.DataFailureHandler;
import rwr.android.tubestatus.datafailurehandler.IDataFailureHandler;
import rwr.android.tubestatus.statusupdater.IStatusUpdater;
import rwr.android.tubestatus.statusupdater.StatusUpdater;
import rwr.android.tubestatus.image.BitmapCache;
import rwr.android.tubestatus.colourschememanager.ColourSchemeManager;
import rwr.android.tfl.TFLDataManager;
import rwr.android.tubestatus.image.IBitmapCache;
import rwr.android.util.Utilities;

public class CompositionRoot
{
    private final MainView mainView;
    private final ITFLDataManager tflDataManager;
    private final IAnimatedLineManager animatedLineManager;

    public CompositionRoot(Activity activity, float canvasSize)
    {
        // Create the object graph
        Point screenSize = Utilities.getScreenSize(activity);
        TextView statusTextView = (TextView) activity.findViewById(R.id.statusTextView);
        RelativeLayout backgroundLayout = (RelativeLayout) activity.findViewById(R.id.backgroundLayout);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        IColourSchemeManager colourSchemeManager = new ColourSchemeManager(sharedPreferences);
        IDataFailureHandler dataFailureHandler = new DataFailureHandler(activity, statusTextView);
        IStatusUpdater statusUpdater = new StatusUpdater(activity, statusTextView, colourSchemeManager);
        IDataCleaner dataCleaner = new DataCleaner(activity);
        IJsonEndpoint tflJsonEndPoint = new TFLApiJsonEndpoint(dataFailureHandler);
        IDataProvider dataProvider = new TFLApiDataProvider(tflJsonEndPoint, dataCleaner);
        tflDataManager = new TFLDataManager(dataProvider);
        IAnimatedLineGridLayout gridLayout = new AnimatedLineGridLayout(screenSize, tflDataManager.getData().size(), canvasSize);
        IBitmapCache bitmapCache = new BitmapCache(activity, gridLayout.getRowHeight());
        animatedLineManager = new AnimatedLineManager(tflDataManager, bitmapCache, gridLayout, statusUpdater, screenSize);

        colourSchemeManager.subscribe(new RelativeLayoutColourSchemeManager(backgroundLayout));
        colourSchemeManager.subscribe(animatedLineManager);

        mainView = new MainView(tflDataManager, bitmapCache, gridLayout, colourSchemeManager, activity.getString(R.string.date_prefix), screenSize);
    }

    public void updatePhysics()
    {
        animatedLineManager.updatePhysics();
        tflDataManager.maintainData();
    }

    public boolean onTouchEvent(int x, int y)
    {
        animatedLineManager.onTouchEvent(x, y);
        mainView.onTouchEvent(x, y);
        return true;
    }

    public void draw(Canvas canvas)
    {
        mainView.draw(canvas);
        animatedLineManager.draw(canvas);
    }

    public void refreshButtonClick()
    {
        animatedLineManager.refresh();
    }

    public void onResume()
    {
        animatedLineManager.reset();
        tflDataManager.requestDataRefresh();
    }
}