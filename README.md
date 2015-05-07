#<center>Auto Silent Update</center>

## Open this sample project in Android Studio

Actually, not `Open`, but `Import Project` at the first time, because of my optimized .gitignore file for Android Studio.

## Merge AutoUpdate module into your App source

Just put follow files into your project:

    /app/src/main/java/com/auto/update/NetworkTool.java
    /app/src/main/java/com/auto/update/Config.java
    /app/src/main/java/com/auto/update/VersionUpdate.java
    /app/src/main/java/android/content/pm/PackageManager.java
    /app/src/main/aidl/android/content/pm/IPackageInstallObserver.aidl

Add follow code in your Activity like onCreate():

    Config.setUpdateServer("http://github.com/flyskywhy/");
    Config.setUpdateApkname("ThisOrOtherApp.apk");
    Config.setUpdateVerjson("ThisOrOtherApp.json");
    Config.setUpdateSavename("ThisOrOtherApp.apk");
    Config.setUpdatePackagename("this.or.other.app");
    new VersionUpdate(this);

Add `android.permission.INSTALL_PACKAGES` in your AndroidManifest.xml.

## Install your App

Because AutoUpdate need system permission, so you need put your apk into `/system/app/` of rooted Android device, or you need sign apk like this:

    java -jar signapk.jar platform.x509.pem platform.pk8 YourApp-release-unsigned.apk YourApp-release-signed.apk
    zipalign -f 4 YourApp-release-signed.apk YourApp-release-signed-aligned.apk

ps: `YourApp-release-unsigned.apk` comes from double-click `assemble` in the right dock bar of Android Studio named `Gradle`.

ps: If there's a .so file in apk, but your App can't find it, here is the solution: push a apk with smaller `android:versionCode` in `AndroidManifest.xml` to  `/system/app/` , then upgrade the App with a bigger `android:versionCode` one.

## Update your App

* Increase versionCode and versionName in AndroidManifest.xml, then generate the apk.
* Increase verCode and verName in json file within `/Misc/AutoUpdateServer/`
* Put the new apk and json files into folder like `http://github.com/flyskywhy/`.
* When your App startup next time, it will automatically get json for verCode from server like `http://github.com/flyskywhy/` to compare with the versionCode of local installed apk, then download and silently update the new apk if needed.
* When `Config.setUpdateApkname` and so on is not set to your App itself, then the specified apk will be invoked after updated.

## Thanks

* [Android应用的自动升级、更新模块的实现](http://blog.csdn.net/xjanker2/article/details/6303937)
* [Android apk的安装、卸载、更新升级（通过Eclipse实现静默安装）](http://my.oschina.net/zhoulc/blog/120423)