package com.auto.update;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.IPackageInstallObserver;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class VersionUpdate {
    private static final String TAG = "Update";
    public ProgressDialog pBar;
    private Handler handler = new Handler();
    private Context ctx;

    private int newVerCode = 0;
    private String newVerName = "";

    public VersionUpdate(Context context) {
        ctx = context;
        if (getServerVerCode()) {
            int vercode = Config.getVerCode(context);
            if (newVerCode > vercode) {
                doNewVersionUpdate();
            } else {
                notNewVersionShow();
            }
        }
    }

    private boolean getServerVerCode() {
        try {
            String verjson = NetworkTool.getContent(Config.updateServer
                    + Config.updateVerjson);
            JSONArray array = new JSONArray(verjson);
            if (array.length() > 0) {
                JSONObject obj = array.getJSONObject(0);
                try {
                    newVerCode = Integer.parseInt(obj.getString("verCode"));
                    newVerName = obj.getString("verName");
                } catch (Exception e) {
                    newVerCode = -1;
                    newVerName = "";
                    return false;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return false;
        }
        return true;
    }

    private void notNewVersionShow() {
//        int verCode = Config.getVerCode(ctx);
//        String verName = Config.getVerName(ctx);
//        StringBuffer sb = new StringBuffer();
//        sb.append("当前版本:");
//        sb.append(verName);
//        sb.append(" Code:");
//        sb.append(verCode);
//        sb.append(",\n已是最新版,无需更新!");
//        Toast.makeText(ctx, sb.toString(), Toast.LENGTH_SHORT).show();
    }

    private void doNewVersionUpdate() {
        int verCode = Config.getVerCode(ctx);
        String verName = Config.getVerName(ctx);
        StringBuffer sb = new StringBuffer();
        sb.append("当前版本:");
        sb.append(verName);
        sb.append(" Code:");
        sb.append(verCode);
        sb.append(", 发现新版本:");
        sb.append(newVerName);
        sb.append(" Code:");
        sb.append(newVerCode);
        Toast.makeText(ctx, sb.toString(), Toast.LENGTH_SHORT).show();
        pBar = new ProgressDialog(ctx);
        pBar.setTitle("正在下载 " + Config.updateApkname + " " + verName);
        pBar.setMessage("请稍候...");
        pBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        downFile(Config.updateServer
                + Config.updateApkname);
    }

    void downFile(final String url) {
        pBar.show();
        new Thread() {
            public void run() {
                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(url);
                HttpResponse response;
                try {
                    response = client.execute(get);
                    HttpEntity entity = response.getEntity();
                    long length = entity.getContentLength();
                    InputStream is = entity.getContent();
                    FileOutputStream fileOutputStream = null;
                    if (is != null) {

                        File file = new File(
                                Environment.getExternalStorageDirectory(),
                                Config.updateSavename);
                        fileOutputStream = new FileOutputStream(file);

                        byte[] buf = new byte[1024];
                        int ch = -1;
                        int count = 0;
                        while ((ch = is.read(buf)) != -1) {
                            fileOutputStream.write(buf, 0, ch);
                            count += ch;
                            if (length > 0) {
                            }
                        }

                    }
                    fileOutputStream.flush();
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                    down();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }.start();

    }

    void down() {
        handler.post(new Runnable() {
            public void run() {
                pBar.cancel();
                update();
            }
        });

    }

    void update() {
        PackageManager pm = ctx.getPackageManager();
        File file = new File(Environment.getExternalStorageDirectory(),
                Config.updateSavename);
        MyPakcageInstallObserver observer = new MyPakcageInstallObserver();
        observer.setCx(ctx);

        pm.installPackage(Uri.fromFile(file), observer, PackageManager.INSTALL_REPLACE_EXISTING
                , Config.updatePackagename
        );

//		Intent intent = new Intent(Intent.ACTION_VIEW);
//		intent.setDataAndType(Uri.fromFile(new File(Environment
//				.getExternalStorageDirectory(), Config.updateSavename)),
//				"application/vnd.android.package-archive");
//		startActivity(intent);
    }

    private static class MyPakcageInstallObserver extends IPackageInstallObserver.Stub {
        private Context ctx;

        public void setCx(Context cx) {
            this.ctx = cx;
        }

        @Override
        public void packageInstalled(String packageName, int returnCode)
                throws android.os.RemoteException {
            if (1 == returnCode) {
                Intent intent = ctx.getPackageManager().getLaunchIntentForPackage(packageName);
                if (intent != null) {
                    ctx.startActivity(intent);
                }
            }
        }
    }
}