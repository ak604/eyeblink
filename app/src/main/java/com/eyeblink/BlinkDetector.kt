package com.eyeblink

class BlinkDetector{
    
    fun isBlinked(blinkData : BlinkData):Boolean {
        val (left, right) = blinkData
        if(left <40 || right<40)
            return true;
        return false;
    }
}
