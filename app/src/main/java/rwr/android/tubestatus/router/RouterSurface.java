package rwr.android.TubeStatus.Router;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import rwr.android.TubeStatus.CompositionRoot;
import rwr.android.Util.Utilities;

public class RouterSurface extends SurfaceView implements SurfaceHolder.Callback
{
	private final float canvasSize = 0.6f;

	private RouterCanvasThread routerCanvasThread;
	private CompositionRoot compositionRoot;

	public RouterSurface(Context context, AttributeSet attributeSet)
	{
		super(context, attributeSet);

		getHolder().addCallback(this);

		SurfaceHolder surfaceHolder = getHolder();

		Point screenSize = Utilities.getScreenSize(context);
		surfaceHolder.setFixedSize(screenSize.x, (int)(screenSize.y * canvasSize));

        routerCanvasThread = new RouterCanvasThread(surfaceHolder, this);

		setFocusable(true);
	}

	public void createCompositionRoot(Activity activity)
	{
		compositionRoot = new CompositionRoot(activity, canvasSize);
	}

	public void doDraw(Canvas canvas)
	{
		if (canvas != null)
		{
			compositionRoot.draw(canvas);
		}
	}
	
	public void doUpdatePhysics()
	{
		compositionRoot.updatePhysics();
	}

	public boolean onTouchEvent(MotionEvent event)
	{
		if (event.getAction() == MotionEvent.ACTION_DOWN)
		{
			int x = (int) event.getX();
			int y = (int) event.getY();
			return compositionRoot.onTouchEvent(x, y);
		}
		return true;
	}

	public void onResume()
	{
		compositionRoot.onResume();
	}

	public void refreshButtonClick()
	{
		compositionRoot.refreshButtonClick();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder)
	{
		if (routerCanvasThread.getState() == Thread.State.TERMINATED)
		{ 
			routerCanvasThread = new RouterCanvasThread(getHolder(),this);
        }
		
		routerCanvasThread.setRunning(true);
		routerCanvasThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) 
	{
		boolean tryJoin = true;
		routerCanvasThread.setRunning(false);

		while (tryJoin)
		{
			try 
			{
				routerCanvasThread.join();
				tryJoin = false;
			} 
			catch (Exception e)
			{
				Log.e("TFL", "Canvas thread join failed");
			}
		}
	}
	
	public void stopCanvasThread()
	{
		routerCanvasThread.setRunning(false);
		routerCanvasThread.interrupt();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) 
	{
		// Required implementation
	}
}