package com.eyeblink

import android.arch.lifecycle.*


class MainViewModel() : ViewModel() {
    private val blinkData = MutableLiveData<BlinkData>()
    private val blink : LiveData<Blink>

    init{
        val blinkDetector = BlinkDetector()
        blink= Transformations.map<BlinkData, Blink>(blinkData,{ valuex -> blinkDetector.isBlinked(valuex!!) })
       // getBlinkData().observeForever(Observer<BlinkData>{ blinkData -> blinkDetector.update(blinkData!!)})
    }
    fun setBlinkData(item: BlinkData) {
        blinkData.value = item
    }

    fun getBlinkData(): LiveData<BlinkData> {
        return blinkData
    }
    fun getBlink(): LiveData<Blink> {
        return blink
    }

}