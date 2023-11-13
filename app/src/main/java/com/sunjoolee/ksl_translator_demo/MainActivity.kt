package com.sunjoolee.ksl_translator_demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import org.opencv.android.OpenCVLoader

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(OpenCVLoader.initDebug()) Log.d("MainActivity_OpenCV", "Loaded successfully.")
        else Log.d("MainActivity_OpenCV", "Load failed.")
    }
}