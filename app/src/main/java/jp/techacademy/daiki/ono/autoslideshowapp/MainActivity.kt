package jp.techacademy.daiki.ono.autoslideshowapp

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.provider.MediaStore
import android.content.ContentUris
import android.database.Cursor
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import android.os.Handler

open class MainActivity : AppCompatActivity() {

    private val PERMISSIONS_REQUEST_CODE = 100
    var cursor:Cursor?=null
    var scount=2
    var mTimer:Timer?=null
   // var mTimerSec=0.0
    var mHandler=Handler()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // パーミッションの許可状態を確認する
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // 許可されている

                getContentsInfo()
            } else {
                // 許可されていないので許可ダイアログを表示する
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSIONS_REQUEST_CODE)
            }
            // Android 5系以下の場合
        } else {
            getContentsInfo()
        }
    }






    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_REQUEST_CODE ->
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContentsInfo()
                }
        }
    }



    private fun getContentsInfo() {


        // 画像の情報を取得する
        val resolver = contentResolver
            cursor = resolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // データの種類
                null, // 項目（null = 全項目）
                null, // フィルタ条件（null = フィルタなし）
                null, // フィルタ用パラメータ
                null // ソート (nullソートなし）
            )



            if (cursor!!.moveToFirst()) {
                // indexからIDを取得し、そのIDから画像のURIを取得する
                val fieldIndex = cursor!!.getColumnIndex(MediaStore.Images.Media._ID)
                val id = cursor!!.getLong(fieldIndex)
                val imageUri =
                    ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                imageView.setImageURI(imageUri)
            }

            slide_button.setOnClickListener {
                if(scount%2==0) {
                    mTimer=Timer()
                    mTimer!!.schedule(object : TimerTask() {
                        override fun run() {
                           // mTimerSec += 0.1
                            mHandler.post {
                                if (cursor!!.moveToNext()) {
                                    // indexからIDを取得し、そのIDから画像のURIを取得する
                                    val fieldIndex = cursor!!.getColumnIndex(MediaStore.Images.Media._ID)
                                    val id = cursor!!.getLong(fieldIndex)
                                    val imageUri =
                                        ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                                    imageView.setImageURI(imageUri)
                                } else if (cursor!!.moveToFirst()) {
                                    val fieldIndex = cursor!!.getColumnIndex(MediaStore.Images.Media._ID)
                                    val id = cursor!!.getLong(fieldIndex)
                                    val imageUri =
                                        ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                                    imageView.setImageURI(imageUri)
                                }
                            }
                        }
                    }, 2000, 2000)
                    slide_button.text = "停止"

                }else{
                    slide_button.text="再生"
                    if (mTimer != null){
                        mTimer!!.cancel()
                        mTimer = null
                    }
                }
                scount++
            }

            next_button.setOnClickListener {
                if (cursor!!.moveToNext()) {
                    // indexからIDを取得し、そのIDから画像のURIを取得する
                    val fieldIndex = cursor!!.getColumnIndex(MediaStore.Images.Media._ID)
                    val id = cursor!!.getLong(fieldIndex)
                    val imageUri =
                        ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                    imageView.setImageURI(imageUri)
                } else if (cursor!!.moveToFirst()) {
                    val fieldIndex = cursor!!.getColumnIndex(MediaStore.Images.Media._ID)
                    val id = cursor!!.getLong(fieldIndex)
                    val imageUri =
                        ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                    imageView.setImageURI(imageUri)
                }
            }

            previous_button.setOnClickListener {
                if (cursor!!.moveToPrevious()) {
                    val fieldIndex = cursor!!.getColumnIndex(MediaStore.Images.Media._ID)
                    val id = cursor!!.getLong(fieldIndex)
                    val imageUri =
                        ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                    imageView.setImageURI(imageUri)
                } else if (cursor!!.moveToLast()) {
                    val fieldIndex = cursor!!.getColumnIndex(MediaStore.Images.Media._ID)
                    val id = cursor!!.getLong(fieldIndex)
                    val imageUri =
                        ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                    imageView.setImageURI(imageUri)
                }
            }
    }



    override fun onDestroy() {
        super.onDestroy()
        cursor!!.close()
    }


}

