package hongzhen.com.superimagview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import hongzhen.com.yhzgesturedetector.SuperImageView;

public class MainActivity extends AppCompatActivity {

    private SuperImageView imageView;
    private ImageView imageView_show;
    private Button button;
    private TextView tvShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.image);

        imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.bg));
        tvShow = findViewById(R.id.tv_show);
        imageView_show = findViewById(R.id.imageView);
        button = findViewById(R.id.button_save);

        imageView.setListener(new SuperImageView.onTypeListener() {
            @Override
            public void onTypeChangeListener(String type) {
                tvShow.setText(imageView.mType);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView_show.setImageBitmap(createViewBitmap(imageView));
            }
        });
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
