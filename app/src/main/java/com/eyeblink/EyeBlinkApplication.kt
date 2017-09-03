package com.eyeblink

import android.app.Application
import android.location.LocationManager
import dagger.android.HasActivityInjector
import javax.inject.Inject
import android.app.Activity
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector

/**
 * Created by anand on 1/9/17.
 */


class EyeBlinkApplication: Application(), HasActivityInjector {
   @Inject lateinit var dispatchingActivityInjector:DispatchingAndroidInjector<Activity>
    override fun onCreate() {
        super.onCreate()


    }

    override fun activityInjector(): AndroidInjector<Activity> {
        return dispatchingActivityInjector
    }
}