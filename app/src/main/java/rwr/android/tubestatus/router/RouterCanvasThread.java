package rwr.android.tubestatus.router;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

class RouterCanvasThread extends Thread
{
    private final SurfaceHolder surfaceHolder;
    private final RouterSurface routerSurface;
    private boolean running = false;

    public RouterCanvasThread(SurfaceHolder surfaceHolder, RouterSurface routerSurface)
    {
        this.surfaceHolder = surfaceHolder;
        this.routerSurface = routerSurface;
    }

    public void setRunning(boolean run)
    {
        running = run;
    }

    @Override
    public void run()
    {
        Canvas canvas;
        while (running)
        {
            canvas = null;

            try
            {
                canvas = surfaceHolder.lockCanvas(null);

                synchronized (surfaceHolder)
                {
                    routerSurface.doUpdatePhysics();
                    routerSurface.doDraw(canvas);
                }
            }
            finally
            {
                if (canvas != null)
                {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
}