package com.eyeblink

import android.arch.lifecycle.*
import com.eyeblink.cartoon.UserCartoonFace
import com.eyeblink.cartoon.UserCartoonFace_MembersInjector
import javax.inject.Inject

public class MainViewModel() : ViewModel() {
    private val blinkData = MutableLiveData<BlinkData>()
    private val blink : LiveData<Blink>

    init{
        val blinkDetector = BlinkDetector()
        blink= Transformations.map<BlinkData, Blink>(blinkData,{ valuex -> blinkDetector.isBlinked(valuex!!) })
        UserCartoonFace(this)
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