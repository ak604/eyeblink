package com.eyeblink

import android.app.Activity
import dagger.Binds
import dagger.Module
import dagger.android.ActivityKey
import dagger.android.AndroidInjector
import dagger.multibindings.IntoMap

@Module(subcomponents = arrayOf(MainActivitySubcomponent::class))
        abstract class MainActivityModule {
    @Binds
    @IntoMap
    @ActivityKey(MainActivity::class)
    abstract fun bindYourActivityInjectorFactory(builder : MainActivitySubcomponent.Builder) : AndroidInjector.Factory<out Activity>
}