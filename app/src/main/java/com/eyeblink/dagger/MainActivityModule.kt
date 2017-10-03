package com.eyeblink.dagger

import android.app.Activity
import android.content.Context
import com.eyeblink.MainActivity
import dagger.Binds
import dagger.Module
import dagger.android.ActivityKey
import dagger.android.AndroidInjector
import dagger.multibindings.IntoMap
import dagger.Provides

@Module(subcomponents = arrayOf(MainActivitySubcomponent::class))
        abstract class MainActivityModule {
    @Binds
    @IntoMap
    @ActivityKey(MainActivity::class)
    abstract fun bindYourActivityInjectorFactory(builder : MainActivitySubcomponent.Builder) : AndroidInjector.Factory<out Activity>

    @Binds
    abstract fun bindApplicationContext( context : Context) :  Context
}