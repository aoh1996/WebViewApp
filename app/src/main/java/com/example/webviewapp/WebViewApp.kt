package com.example.webviewapp

import NetworkConnectionMonitor
import android.app.Application
import com.appsflyer.AppsFlyerLib
import com.onesignal.OneSignal

private const val ONESIGNAL_APP_ID = "303ecb33-bba5-4973-a6b2-2887a3536287"
private const val APPSFLYER_DEV_KEY = "iokBJWvQSeQMpnh2RR2vTk"

class WebViewApp : Application() {

    lateinit var networkConnectionMonitor: NetworkConnectionMonitor

    override fun onCreate() {
        networkConnectionMonitor = NetworkConnectionMonitor.getInstance(this)

        // Logging set to help debug issues, remove before releasing your app.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)

        // OneSignal Initialization
        OneSignal.initWithContext(this)
        OneSignal.setAppId(ONESIGNAL_APP_ID)

        AppsFlyerLib.getInstance().init(APPSFLYER_DEV_KEY, null, this)
        AppsFlyerLib.getInstance().start(this)

        super.onCreate()
    }
}