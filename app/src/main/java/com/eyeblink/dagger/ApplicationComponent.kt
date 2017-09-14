package com.eyeblink.dagger

import android.content.Context
import com.eyeblink.EyeBlinkApplication
import com.eyeblink.MainActivity
import dagger.Component
import javax.inject.Singleton
import dagger.android.support.AndroidSupportInjectionModule



/**
 * Created by anand on 1/9/17.
 */
@Singleton
@Component(modules = arrayOf(AndroidSupportInjectionModule::class, MainActivityModule::class))
interface ApplicationComponent {
    fun inject(application: EyeBlinkApplication)
    fun inject(mainActivity: MainActivity)
}