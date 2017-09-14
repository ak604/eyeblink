package com.eyeblink

import android.arch.lifecycle.LifecycleActivity
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import dagger.android.AndroidInjection
import eyeblink.com.eyeblink.R


class MainActivity : LifecycleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this);
        setContentView(R.layout.main)
        val model = ViewModelProviders.of(this).get(MainViewModel::class.java)
    }
}
