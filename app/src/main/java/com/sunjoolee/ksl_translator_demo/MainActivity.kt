package com.sunjoolee.ksl_translator_demo

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import org.opencv.android.OpenCVLoader
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc
import java.io.IOException

class MainActivity : AppCompatActivity() {
    val SELECT_CODE = 100

    val imageView : ImageView by lazy{ findViewById(R.id.image_view) }
    val selectButton : Button by lazy{ findViewById(R.id.select_button) }
    val cameraButton : Button by lazy{ findViewById(R.id.camera_button) }

    lateinit var bitmap:Bitmap
    lateinit var mat: Mat //Mat: openCV 기본 데이터 타입, 행렬(Matrix) 구조체
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(OpenCVLoader.initDebug()) Log.d("MainActivity_OpenCV", "Loaded successfully.")
        else Log.d("MainActivity_OpenCV", "Load failed.")

        selectButton.setOnClickListener {
            val intent : Intent = Intent(Intent.ACTION_GET_CONTENT)
                .setType("image/*")
            startActivityForResult(intent, SELECT_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == SELECT_CODE && data != null) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, data.data)
                //imageView.setImageBitmap(bitmap)

                mat = Mat().apply{
                    //bitmap -> mat
                    Utils.bitmapToMat(bitmap, this)
                    //이미지 색상 흑백으로 변경
                    Imgproc.cvtColor(this, this, Imgproc.COLOR_RGB2GRAY)
                }
                //mat -> bitmap
                Utils.matToBitmap(mat, bitmap)
                imageView.setImageBitmap(bitmap)
            }catch(e:IOException){
                e.printStackTrace()
            }
        }
    }
}