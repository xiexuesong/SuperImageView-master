package hongzhen.com.yhzgesturedetector;

import android.content.Context;
import android.graphics.PointF;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

public class YhzGestureDetector implements ScaleGestureDetector.OnScaleGestureListener, GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {
    private static final float MIN_ROTATE = 0.2F;
    private String TAG = "YhzGestureDetector";
    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;
    private float mDegree;
    private Mode mode;
    private float mPointDistinct;
    private static final float MIN_POINT_DISTINCT = 10F;
    private float rotate;
    private int roteteNow;
    private PointF mCenterPoint = new PointF();
    //是否可以旋转
    private boolean isRotateAble = true;
    //是否可以缩放
    private boolean isScaleAble= true;
    //是否可以单指拖动
    private boolean isOneFingerScrollAble= true;
    //是否可以双指拖动
    private boolean isDoubleFingerScrollAble= false;
    //是否可以单击
    private boolean isSingleClickAble= true;
    //是否可以双击
    private boolean isDoubleClickAble= true;
    //是否可以长按
    private boolean isLongClickAble= true;
    private GestureDetectorListener listener;

    public void setRotateAble(boolean rotateAble) {
        isRotateAble = rotateAble;
    }

    public void setScaleAble(boolean scaleAble) {
        isScaleAble = scaleAble;
    }

    public void setOneFingerScrollAble(boolean oneFingerScrollAble) {
        isOneFingerScrollAble = oneFingerScrollAble;
    }

    public void setDoubleFingerScrollAble(boolean doubleFingerScrollAble) {
        isDoubleFingerScrollAble = doubleFingerScrollAble;
    }

    public void setSingleClickAble(boolean singleClickAble) {
        isSingleClickAble = singleClickAble;
    }

    public void setDoubleClickAble(boolean doubleClickAble) {
        isDoubleClickAble = doubleClickAble;
    }

    public boolean isRotateAble() {
        return isRotateAble;
    }

    public boolean isScaleAble() {
        return isScaleAble;
    }

    public boolean isOneFingerScrollAble() {
        return isOneFingerScrollAble;
    }

    public boolean isDoubleFingerScrollAble() {
        return isDoubleFingerScrollAble;
    }

    public boolean isSingleClickAble() {
        return isSingleClickAble;
    }

    public boolean isDoubleClickAble() {
        return isDoubleClickAble;
    }

    public boolean isLongClickAble() {
        return isLongClickAble;
    }

    public void setLongClickAble(boolean longClickAble) {
        isLongClickAble = longClickAble;
    }

    public void setGestureDetectorListener(GestureDetectorListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        if (isSingleClickAble){
            listener.onSingleClick();
        }
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        if (isDoubleClickAble){
            listener.onDoubleClick();
        }
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    /**
     * 事件的对外接口
     */
    public interface GestureDetectorListener {
        void onOneFingerScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY);

        void onDoubleFingerScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY);

        void onSingleClick();

        void onDoubleClick();

        void onLongClick();

        void onScale(ScaleGestureDetector detector);

        void onRotate(int roteteNow, float x, float y);
    }

    public YhzGestureDetector(Context context) {
        scaleGestureDetector = new ScaleGestureDetector(context, this);
        gestureDetector = new GestureDetector(context, this);
    }

    /**
     * 接收处理事件的方法
     *
     * @param event
     * @return
     */
    public boolean onTouchEvent(MotionEvent event) {
        if (listener == null) {
            try {
                throw new Exception("GestureDetectorListener Can not be null!");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
        scaleGestureDetector.onTouchEvent(event);
        gestureDetector.onTouchEvent(event);
        rotateGestureDetector(event);
        return true;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        Log.i(TAG, "onDown");
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        Log.i(TAG, "onShowPress");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Log.i(TAG, "onSingleTapUp");
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (e2.getPointerCount() == 2) {
            if (isDoubleFingerScrollAble){
                listener.onDoubleFingerScroll(e1, e2, distanceX, distanceY);
            }
        } else if (e2.getPointerCount() == 1) {
            if (isOneFingerScrollAble){
                listener.onOneFingerScroll(e1, e2, distanceX, distanceY);
            }
        }
        Log.i(TAG, "onScroll");
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        if (isLongClickAble){
            listener.onLongClick();
        }
        Log.i(TAG, "onLongPress");
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.i(TAG, "onFling");
        return true;
    }


    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        if (isScaleAble){
            listener.onScale(detector);
        }
        Log.i(TAG, "onScale");
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        Log.i(TAG, "onScaleBegin");
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        Log.i(TAG, "onScaleEnd");
    }

    /**
     * 旋转的事件处理
     *
     * @param event
     */
    private void rotateGestureDetector(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "ACTION_DOWN");
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                Log.i(TAG, "ACTION_POINTER_DOWN");
                mPointDistinct = callSpacing(event);
                if (mPointDistinct > MIN_POINT_DISTINCT) {
                    mode = Mode.MOVE;
                }
                mDegree = callRotation(event);
                Log.i(TAG,"mDegree:"+mDegree);
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == Mode.MOVE && event.getPointerCount() == 2) {  //只能2只手
                    if (isRotateAble) {
                        rotate = callRotation(event);
                        callPoint(mCenterPoint, event);
                        roteteNow = (int) (rotate - mDegree);
                        listener.onRotate(roteteNow, mCenterPoint.x, mCenterPoint.y);
                        mDegree = rotate;
                    }
                    Log.i(TAG, "选择角度roteteNow：" + roteteNow);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                Log.i(TAG, "ACTION_POINTER_UP");
            case MotionEvent.ACTION_CANCEL:
                Log.i(TAG, "ACTION_CANCEL");
                mode = Mode.NONE;
                break;

        }
    }

    enum Mode {
        NONE, DOWN, MOVE, ROTATE
    }

    /**
     * 计算旋转的角度
     *
     * @param event
     * @return
     */
    private float callRotation(MotionEvent event) {
        double deltaX = (event.getX(0) - event.getX(1));
        double deltaY = (event.getY(0) - event.getY(1));
        double radius = Math.atan2(deltaY, deltaX);
        return (float) Math.toDegrees(radius);
    }

    /**
     * 计算两点距离
     *
     * @param event
     * @return
     */
    private float callSpacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * 计算两点中心的点坐标
     *
     * @param point
     * @param event
     */
    private void callPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }
}

