package rwr.android.TubeStatus.Router;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import rwr.android.TFL.R;

public class RouterActivity extends Activity
{
    private RouterSurface routerSurface;

    @Override
	public void onCreate(Bundle savedInstanceState)
	{
		getWindow().getAttributes().format = android.graphics.PixelFormat.RGBA_8888;

		setContentView(R.layout.activity_main);
        getWindow().getDecorView().setBackgroundColor(Color.BLACK);

        overridePendingTransition(0, 0);

        super.onCreate(savedInstanceState);

        routerSurface = (RouterSurface)findViewById(R.id.routerPanel);
        routerSurface.createCompositionRoot(this);
    }

    @Override
    public void onDestroy()
    {
        routerSurface.stopCanvasThread();
    	super.onDestroy();
    }

    @Override
    public void onStop()
    {
    	super.onStop();
    }

    @Override
    public void onPause()
    {
    	super.onPause();
    }

    @Override
    public void onResume()
    {
        routerSurface.onResume();
      	super.onResume();
    }

    public void refreshButtonClick(View view)
    {
        routerSurface.refreshButtonClick();
    }
}