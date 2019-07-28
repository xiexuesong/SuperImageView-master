package hongzhen.com.superimagview;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;


import hongzhen.com.yhzgesturedetector.SuperImageView;

public class SecondActivity extends Activity {

    private ImageView imageView;
    private SuperImageView superImageView;
    private Bitmap bitmap;
    private Button button_finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        imageView = findViewById(R.id.imageView);
        superImageView = findViewById(R.id.superImageView);
        button_finish = findViewById(R.id.button_finish);
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.test);
        imageView.setImageResource(R.mipmap.test);
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                RectF rectF = new RectF(superImageView.getLeft() ,superImageView.getTop() ,superImageView.getRight() ,superImageView.getBottom());
                if(rectF.contains(event.getX() , event.getY())){
                    return false;
                }
                return true;
            }
        });
     //   superImageView.setImageResource(R.drawable.bg);
        superImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                imageView.setAlpha(0.5f);
                superImageView.onTouchEvent(event);
                switch (event.getAction()){
                    case MotionEvent.ACTION_UP:
                        //释放还原透明度
                        imageView.setAlpha(1f);
                        break;
                }
                return true;
            }
        });

        button_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageBitmap(createViewBitmap(superImageView));
            }
        });

        //http://res.chuanying520.com/template/ZGK180510_1/koRLv8EpYJ.jpg
        Glide.with(this).load("http://res.chuanying520.com/template/ZGK180510_1/koRLv8EpYJ.jpg").asBitmap().fitCenter().into(superImageView);
     //   ImageOptions.Builder imageOptions = new ImageOptions.Builder();
     //   imageOptions.setImageScaleType(ImageView.ScaleType.CENTER);

     //   x.image().bind(superImageView,"http://res.chuanying520.com/template/ZGK180510_1/koRLv8EpYJ.jpg",imageOptions.build());
       // superImageView.setImageURI(Uri.parse("http://res.chuanying520.com/template/ZGK180510_1/koRLv8EpYJ.jpg"));

    }

    public Bitmap createViewBitmap(View v) {
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.TRANSPARENT);
        v.draw(canvas);
        return bitmap;
    }
}
