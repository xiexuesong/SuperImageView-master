package hongzhen.com.superimagview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.FrameLayout;
import android.widget.VideoView;

public class ScaleVideoView extends VideoView {

    private final static float MAX_SCALE = 10;
    private final static float MIN_SCALE = 0.5f;
    private final static int MAX_MOVE_CLICK = 50;//点击最长移动距离
    private final static int MAX_SINGLE_CLICK_TIME = 50;// 单击最长等待时间


    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;
    private float preScale = 1.0f;//之前的伸缩值
    private float curScale;//当前的伸缩值

    private float lastX;
    private float lastY;
    private double oldDist, moveDist;
    private Bitmap bitmap_play;
    private Paint paint;

    public ScaleVideoView(Context context) {
        super(context);
        initData(context);
    }

    public ScaleVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData(context);
    }

    public ScaleVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData(context);
    }

    private void initData(Context context) {
        scaleGestureDetector = new ScaleGestureDetector(context, new MyDoubleGestureListener());
        gestureDetector = new GestureDetector(context, new MyGestureListener());
        bitmap_play = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        Log.i("MDL","initData" + " width:" + bitmap_play.getWidth() + " height:" + bitmap_play.getHeight());
        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i("MDL","onDraw");
    /*    float left = (getWidth() - bitmap_play.getWidth()) / 2;
        float top = (getHeight() - bitmap_play.getHeight()) / 2;
        canvas.drawBitmap(bitmap_play, getLeft() + left, getTop() + top, paint);*/
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //必须要 &Motion.ACTION_MASK,否则监听不到双指按钮的事件
        switch (ev.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                lastX = ev.getX();
                lastY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (ev.getPointerCount() == 1) {
                    //单指移动
                    float distanceX = ev.getX() - lastX;
                    float distanceY = ev.getY() - lastY;
                    layout((int) (getLeft() + distanceX), (int) (getTop() + distanceY), (int) (getRight() + distanceX), (int) (getBottom() + distanceY));
                } else if (ev.getPointerCount() == 2) {
                    //双指
                    moveDist = spacing(ev);
                    double space = moveDist - oldDist;
                    float scale = (float) (getScaleX() + space / getWidth());
                    if (scale >= MAX_SCALE) {
                        setScale(MAX_SCALE);
                    } else if (scale <= MIN_SCALE) {
                        setScale(MIN_SCALE);
                    } else {
                        setScale(scale);
                    }
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN://双指按下
                oldDist = spacing(ev);
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 计算两个点的距离
     *
     * @param event
     * @return
     */
    private double spacing(MotionEvent event) {
        if (event.getPointerCount() == 2) {
            float x = event.getX(0) - event.getX(1);
            float y = event.getY(0) - event.getY(1);
            return Math.sqrt(x * x + y * y);
        } else {
            return 0;
        }
    }

    private void setScale(float scale) {
        setScaleX(scale);
        setScaleY(scale);
    }

    //双指操作
    private class MyDoubleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        //缩放
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scaleFactor = detector.getScaleFactor();
            int width = (int) (getWidth() * scaleFactor);
            int height = (int) (getHeight() * scaleFactor);
            setScale(scaleFactor);
            return true;
        }
    }

    //单指操作
    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        //移动
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (e2.getPointerCount() == 1) {
                int left = (int) (getLeft() - distanceX);
                int top = (int) (getTop() - distanceY);
                int right = (int) (getRight() - distanceX);
                int bottom = (int) (getBottom() - distanceY);
                layout(left, top, right, bottom);
            }
            return true;
        }
    }


}
