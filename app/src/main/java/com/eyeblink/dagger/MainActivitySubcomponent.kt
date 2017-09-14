package com.eyeblink.dagger

import com.eyeblink.MainActivity
import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent
interface MainActivitySubcomponent: AndroidInjector<MainActivity> {
    @Subcomponent.Builder
    public abstract class Builder: AndroidInjector.Builder<MainActivity>() {}
}