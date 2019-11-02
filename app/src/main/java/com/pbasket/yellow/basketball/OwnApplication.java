package com.pbasket.yellow.basketball;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.onesignal.OneSignal;

public class OwnApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.fullyInitialize();
        AppEventsLogger.activateApp(this);

        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
    }
}
