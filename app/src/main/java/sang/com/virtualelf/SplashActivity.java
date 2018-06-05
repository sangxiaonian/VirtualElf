package sang.com.virtualelf;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;

import sang.com.commonlibrary.base.BaseActivity;
import sang.com.virtuallocation.ui.Loacition_PhoneAppdActivity;
import sang.com.virtuallocation.ui.Loaction_InstallAppActivity;
import sang.com.virtuallocation.ui.Loaction_MapActivity;

public class SplashActivity extends BaseActivity {

    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
          view = findViewById(R.id.img);
        ActivityOptionsCompat compat = ActivityOptionsCompat.makeScaleUpAnimation(view,
                view.getWidth() / 2, view.getHeight() / 2, 0, 0);
        ActivityCompat.startActivity(mContext, new Intent(mContext, Loaction_InstallAppActivity.class),
                compat.toBundle());
        finish();
    }
}
