package com.eyeblink.cartoon
import android.content.Context
import android.support.v4.app.Fragment
import com.eyeblink.MainViewModel
import com.eyeblink.camera.BlinkDataSource
import javax.inject.Inject

 class UserCartoonFace( var viewModel: MainViewModel,var context: Context){

     init{
        BlinkDataSource(viewModel, context)
    }
}