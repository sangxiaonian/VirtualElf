package sang.com.virtualelf;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import sang.com.commonlibrary.base.BaseActivity;
import sang.com.virtuallocation.ui.Loacition_PhoneAppdActivity;
import sang.com.virtuallocation.ui.Loaction_InstallAppActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolTitle("已安装应用");
        setLefttImg(0);
        findViewById(R.id.bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, Loaction_InstallAppActivity.class));
            }
        });


    }
}
