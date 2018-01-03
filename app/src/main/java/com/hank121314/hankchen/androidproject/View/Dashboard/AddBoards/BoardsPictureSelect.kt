package com.hank121314.hankchen.androidproject.View.Dashboard.AddBoards

import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import com.hank121314.hankchen.androidproject.R
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.listeners.onClick
import android.util.DisplayMetrics
import android.view.Gravity
import com.hank121314.hankchen.androidproject.Modal.Binder
import com.hank121314.hankchen.androidproject.Modal.photoModel
import com.hank121314.hankchen.androidproject.bind
import com.hank121314.hankchen.androidproject.helper.LargeBitmap
import com.hank121314.hankchen.androidproject.unBind
import java.io.ByteArrayOutputStream


/**
 * Created by hankchen on 2017/12/20.
 */
class BoardsPictureSelect:AppCompatActivity(){

    val ACTION_CAMERA_REQUEST_CODE=100
    val ACTION_ALBUM_REQUEST_CODE=200
    lateinit var model:photoModel

    override fun onCreate(savedInstanceState: Bundle?) {
        model = photoModel(Binder(LargeBitmap().decodeSampledBitmapFromResource(this.applicationContext.resources,R.drawable.wood1,100,100)), Binder(true))
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels-800
        super.onCreate(savedInstanceState)
        val self =this
        verticalLayout {
            setGravity(Gravity.CENTER)
            imageView {
                id = R.id.addBoard_ImageView
                bind(model.bindOn){
                    when(it){
                        true->bind(model.bindImage) { imageBitmap = it }
                        false->unBind(model.bindImage)
                    }
                }
            }.lparams(width = matchParent, height = height)
            button("Album") {
                backgroundResource = R.drawable.login_button
                onClick {
                    val intent = Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(intent, ACTION_ALBUM_REQUEST_CODE)
                }
            }.lparams(width = matchParent)
            button("Camera") {
                backgroundResource = R.drawable.login_button
                onClick {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(intent, ACTION_CAMERA_REQUEST_CODE)
                }
            }.lparams(width = matchParent) {
                topMargin = 30
            }
            button("Sumbit") {
                backgroundResource = R.drawable.register_button
                onClick {
                    val intent=Intent(self,Named_Boards::class.java)
                    val stream = ByteArrayOutputStream()
                    model.bindImage.item.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                    val byteArray = stream.toByteArray()
                    intent.putExtra("image",byteArray)
                    startActivity(intent)
                }
            }.lparams(width = matchParent) {
                topMargin = 30
            }
        }
    }
    fun displayImage(display:Bitmap){
        model.bindImage.item=display
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            ACTION_CAMERA_REQUEST_CODE->{
                if(resultCode == Activity.RESULT_OK && data != null){
                    displayImage(data.extras.get("data") as Bitmap)
                }
            }
            ACTION_ALBUM_REQUEST_CODE -> {
                if(resultCode == Activity.RESULT_OK && data != null){
                    val resolver = this.contentResolver
                    val bitmap = MediaStore.Images.Media.getBitmap(resolver, data?.data)
                    displayImage(bitmap)
                }
            }

        }
    }
}