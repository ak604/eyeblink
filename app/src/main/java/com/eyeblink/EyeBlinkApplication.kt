package com.eyeblink

import android.app.Application
import android.location.LocationManager
import dagger.android.HasActivityInjector
import javax.inject.Inject
import android.app.Activity
import com.eyeblink.dagger.ApplicationComponent
import com.eyeblink.dagger.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector

/**
 * Created by anand on 1/9/17.
 */


class EyeBlinkApplication: Application(), HasActivityInjector {
   @Inject lateinit var dispatchingActivityInjector:DispatchingAndroidInjector<Activity>
    override fun onCreate() {
        super.onCreate()
       DaggerApplicationComponent.create().inject(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> {
        return dispatchingActivityInjector
    }
}