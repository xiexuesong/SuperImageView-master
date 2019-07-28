package hongzhen.com.yhzgesturedetector;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

/**
 * 开发者：hongzhen
 * 创建时间： 2018/11/15 13:21
 * 公司名称：
 * 类名：SuperImageView.java
 * 描述：支持双指缩放、平移、旋转，单指平移、单击、双击、长按功能的图片控件
 */
@SuppressLint("AppCompatCustomView")
public class SuperImageView extends ImageView implements YhzGestureDetector.GestureDetectorListener {
    private String TAG = "GestureDetectorView";

    //手势识别器
    private YhzGestureDetector yhzGestureDetector;
    private Matrix mMatrix;
   // private Bitmap mBitmap;
    private Context mContext;
    private double mMaxScale = 5;
    private float mFocusX;
    private float mFocusY;
    private double mMinScale = 0;
    private Matrix matrixSave;
    private int screenHeight;
    private int screenWidth;
    public String mType = "";
    private onTypeListener listener;

    public void setListener(onTypeListener listener) {
        this.listener = listener;
    }

    public interface onTypeListener {
        void onTypeChangeListener(String type);
    }

    public SuperImageView(Context context, Bitmap mBitmap) {
        this(context, null, 0);
      //  this.mBitmap = mBitmap;
    }

    public SuperImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SuperImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setBackgroundColor(Color.TRANSPARENT);
        /*TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SuperImageView);
        BitmapDrawable drawable = (BitmapDrawable) typedArray.getDrawable(R.styleable.SuperImageView_src);
        mBitmap = drawable.getBitmap();*/
        mContext = context;
    //    setImageBitmap(mBitmap);
        mMatrix = new Matrix();
        setScaleType(ScaleType.MATRIX);
        yhzGestureDetector = new YhzGestureDetector(context);
        yhzGestureDetector.setGestureDetectorListener(this);
//        Log.i("MDL","width:" + mBitmap.getWidth() + " height:" + mBitmap.getHeight());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        int width = getWidth();
        int height = getHeight();
        if(getDrawable() != null) {
            Bitmap bitmap = ((BitmapDrawable) getDrawable()).getBitmap();
            if (bitmap != null) {
                int drawableWidth = bitmap.getWidth();
                int drawableHeight = bitmap.getHeight();
                Log.i(TAG, "宽：" + drawableWidth);
                //下面进行缩放
                float scale = 1.0f;
                if (drawableWidth > width && drawableHeight < height) { //如果图片很宽，但高度低
                    scale = width * 1.0f / drawableWidth;
                }
                if (drawableWidth < width && drawableHeight > height) { //如果图片很窄，但是高度很高
                    scale = height * 1.0f / drawableHeight;
                }
                if (drawableWidth > width && drawableHeight > height) { //如果图片的宽和高都很大
                    scale = Math.min(width * 1.0f / drawableWidth, height * 1.0f / drawableHeight);
                }
                if (drawableWidth < width && drawableHeight < height) { //如果图片的宽和高都很小
                    scale = Math.min(width * 1.0f / drawableWidth, height * 1.0f / drawableHeight);
                }
                //将图片移动到控件的中心
                int dx = width / 2 - drawableWidth / 2;
                int dy = height / 2 - drawableHeight / 2;
                mMinScale = scale;
                mMatrix.postTranslate(dx, dy);
                mMatrix.postScale(scale, scale, width / 2, height / 2);
                setImageMatrix(mMatrix);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.i(TAG, "onMeasure");
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        screenHeight = dm.heightPixels;
        screenWidth = dm.widthPixels;
        int width = measureDimension(screenWidth, widthMeasureSpec);
        int height = measureDimension(screenHeight, heightMeasureSpec);
        //将计算的宽和高设置进去，保存，最后一步一定要有
        setMeasuredDimension(width, height);
    }

    /**
     * @param defualtSize 设置的默认大小
     * @param measureSpec 父控件传来的widthMeasureSpec，heightMeasureSpec
     * @return 结果
     */
    public int measureDimension(int defualtSize, int measureSpec) {
        int result = defualtSize;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        //1,layout中自定义组件给出来确定的值，比如100dp
        //2,layout中自定义组件使用的是match_parent，但父控件的size已经可以确定了，比如设置的具体的值或者match_parent
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        }
        //layout中自定义组件使用的wrap_content
        else if (specMode == MeasureSpec.AT_MOST) {
            result = Math.min(defualtSize, specSize);//建议：result不能大于specSize
        }
        //UNSPECIFIED,没有任何限制，所以可以设置任何大小
        else {
            result = defualtSize;
        }
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        yhzGestureDetector.onTouchEvent(event);
        //必须返回true，否则只会执行down，不会执行其他事件
        return true;
    }

    @Override
    public void onOneFingerScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        mMatrix.postTranslate(-distanceX, -distanceY);
        setImageMatrix(mMatrix);
        mType = "单指移动";
        if (listener != null) {
            listener.onTypeChangeListener(mType);
        }
        Log.i(TAG, "onOneFingerScroll:" + distanceX);
    }

    @Override
    public void onDoubleFingerScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        mMatrix.postTranslate(-distanceX, -distanceY);
        setImageMatrix(mMatrix);
        if (listener != null) {
            listener.onTypeChangeListener(mType);
        }
        Log.i(TAG, "onDoubleFingerScroll:" + distanceX);
    }

    @Override
    public void onSingleClick() {
        mType = "点击";
        if (listener != null) {
            listener.onTypeChangeListener(mType);
        }
        Log.i(TAG, "onSingleClick");
    }

    @Override
    public void onDoubleClick() {
        mType = "双击";
        if (listener != null) {
            listener.onTypeChangeListener(mType);
        }
        Log.i(TAG, "onDoubleClick");
    }

    @Override
    public void onLongClick() {
        mType = "长按";
        if (listener != null) {
            listener.onTypeChangeListener(mType);
        }
        Log.i(TAG, "onLongClick");
    }

    @Override
    public void onScale(ScaleGestureDetector detector) {
        Log.i(TAG, "onScale");
        float scale = getCurrentScale(mMatrix);
        //拿到手指触控后得到的缩放的值，可能是1点几，也可能是0点几
        float scaleFactor = detector.getScaleFactor();

        if (getDrawable() == null) {
            return;
        }
        Log.i("getDrawable", "非空" + " scale:" + scale);
        //进行缩放detector.getFocusX()和detector.getFocusY()是以手指触控的中心点
        if (scale >= mMaxScale && scaleFactor > 1) {
            return;
        }
        Log.i("mMaxScale", "非空");
      //  if (scale <= mMinScale && scaleFactor < 1) {
        //scale可以在原图基础上继续缩小
        if(scale <= 0){
            return;
        }

        mMatrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());
        mFocusX = detector.getFocusX();
        mFocusY = detector.getFocusY();
        Log.i("mInitScale", "mFocusX:" + mFocusX + "--mFocusY:" + mFocusY);
        setImageMatrix(mMatrix);
        if (listener != null) {
            listener.onTypeChangeListener(mType);
        }
    }

    @Override
    public void onRotate(int roteteNow, float x, float y) {
        Log.i(TAG, "onRotate:" + roteteNow);
        mMatrix.postRotate(roteteNow, x, y);
        setImageMatrix(mMatrix);
        if (listener != null) {
            listener.onTypeChangeListener(mType);
        }
    }

    /**
     * 得到当前的缩放比例
     */
    public float getCurrentScale(Matrix matrix) {
        float[] values = new float[9];
        matrix.getValues(values);
        return values[Matrix.MSCALE_X];
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
