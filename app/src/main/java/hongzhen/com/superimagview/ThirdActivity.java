package hongzhen.com.superimagview;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.MediaController;

public class ThirdActivity extends Activity implements View.OnClickListener, OnVideoViewClickListener {

    private ScaleVideoView myVideoView;
    private String path = Environment.getExternalStorageDirectory() + "/soft-input-surface2.3gp";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        myVideoView = findViewById(R.id.myVideoView);
        myVideoView.setVideoPath(path);
        myVideoView.start();
        initEvent();
    }

    private void initEvent() {
    }

    @Override
    public void onClick(View v) {

    }
}
