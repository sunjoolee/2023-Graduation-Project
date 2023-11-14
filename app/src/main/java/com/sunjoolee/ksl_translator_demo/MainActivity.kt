package com.sunjoolee.ksl_translator_demo

import android.content.Intent
import android.content.pm.PackageManager
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

    val imageView : ImageView by lazy{ findViewById(R.id.image_view) }
    val selectButton : Button by lazy{ findViewById(R.id.select_button) }
    val cameraButton : Button by lazy{ findViewById(R.id.camera_button) }

    //request code for permission
    val CAMERA_PERMISSION_CODE = 300
    //result code or intent
    val SELECT_CODE = 100
    val CAMERA_CODE = 200
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(OpenCVLoader.initDebug()) Log.d("MainActivity_OpenCV", "Loaded successfully.")
        else Log.d("MainActivity_OpenCV", "Load failed.")

        getPermissions()

        selectButton.setOnClickListener {
            val intent : Intent = Intent(Intent.ACTION_GET_CONTENT)
                .setType("image/*")
            startActivityForResult(intent, SELECT_CODE)
        }

        cameraButton.setOnClickListener {
            val intent : Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, CAMERA_CODE)
        }
    }

    private fun getPermissions() {
        if(checkCallingOrSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(arrayOf(android.Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == CAMERA_PERMISSION_CODE && grantResults.isNotEmpty()){
            if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
                getPermissions() //request permission again
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == SELECT_CODE && data != null) {
            try {
                var bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, data.data)
                //imageView.setImageBitmap(bitmap)

                //Mat: openCV 기본 데이터 타입, 행렬(Matrix) 구조체
                var mat = Mat().apply{
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
        if(requestCode == CAMERA_CODE && data != null) {
            try{
                var bitmap = data.extras!!.get("data") as Bitmap

                //Mat: openCV 기본 데이터 타입, 행렬(Matrix) 구조체
                var mat = Mat().apply{
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