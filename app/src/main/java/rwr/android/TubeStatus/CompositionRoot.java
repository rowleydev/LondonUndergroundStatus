package rwr.android.TubeStatus;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Point;
import android.preference.PreferenceManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import rwr.android.TFL.DataCleaner.DataCleaner;
import rwr.android.TFL.DataCleaner.IDataCleaner;
import rwr.android.TFL.IDataProvider;
import rwr.android.TFL.IJsonEndpoint;
import rwr.android.TFL.ITFLDataManager;
import rwr.android.TFL.R;
import rwr.android.TFL.TFLApiDataProvider;
import rwr.android.TFL.TFLApiJsonEndpoint;
import rwr.android.TubeStatus.AnimatedLine.AnimatedLineGridLayout;
import rwr.android.TubeStatus.AnimatedLine.AnimatedLineManager;
import rwr.android.TubeStatus.AnimatedLine.IAnimatedLineGridLayout;
import rwr.android.TubeStatus.AnimatedLine.IAnimatedLineManager;
import rwr.android.TubeStatus.ColourSchemeManager.IColourSchemeManager;
import rwr.android.TubeStatus.ColourSchemeManager.RelativeLayoutColourSchemeManager;
import rwr.android.TubeStatus.DataFailureHandler.DataFailureHandler;
import rwr.android.TubeStatus.DataFailureHandler.IDataFailureHandler;
import rwr.android.TubeStatus.StatusUpdater.IStatusUpdater;
import rwr.android.TubeStatus.StatusUpdater.StatusUpdater;
import rwr.android.TubeStatus.Image.BitmapCache;
import rwr.android.TubeStatus.ColourSchemeManager.ColourSchemeManager;
import rwr.android.TFL.TFLDataManager;
import rwr.android.TubeStatus.Image.IBitmapCache;
import rwr.android.Util.Utilities;

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