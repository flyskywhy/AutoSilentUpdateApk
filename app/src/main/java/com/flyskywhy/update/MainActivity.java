package com.flyskywhy.update;

import android.app.Activity;
import android.os.Bundle;

import com.auto.update.Config;
import com.auto.update.VersionUpdate;
import com.flyskywhy.update.R;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Config.setUpdateServer("http://github.com/flyskywhy/");
        Config.setUpdateApkname("ThisOrOtherApp.apk");
        Config.setUpdateVerjson("ThisOrOtherApp.json");
        Config.setUpdateSavename("ThisOrOtherApp.apk");
        Config.setUpdatePackagename("this.or.other.app");
        new VersionUpdate(this);
    }
}
