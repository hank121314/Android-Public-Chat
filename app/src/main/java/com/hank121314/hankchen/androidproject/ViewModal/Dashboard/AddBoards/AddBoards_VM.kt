package com.hank121314.hankchen.androidproject.ViewModal.Dashboard.AddBoards

import android.content.Intent
import android.graphics.Bitmap
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import com.hank121314.hankchen.androidproject.Stream.RPC
import com.hank121314.hankchen.androidproject.View.Dashboard.Dashboard
import com.hank121314.hankchen.androidproject.Stream.imageUploaderBoards
import com.hank121314.hankchen.androidproject.components.ProgressDialog
import io.reactivex.*
import io.reactivex.disposables.Disposable
import org.jetbrains.anko.alert
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton
import org.json.JSONObject
import java.util.Date
/**
 * Created by hankchen on 2017/12/27.
 */
class onSumbitBoards(bmp:Bitmap,boradName:String,activity:AppCompatActivity) {
    val buidler = ProgressDialog().dialog(activity,"Logging!!").create()

    val bmp=bmp
    val boradName=boradName
    val activity=activity
    fun onPress(){
        buidler.show()
        var param= JSONObject()
        var obj = JSONObject()
        obj.put("board",boradName.toString())
        obj.put("created", Date().time)
        param.put("parm",obj)
        val socketStream= Observable.create(RPC("addBoards",param))
        val single = Observable.create(imageUploaderBoards(bmp, "${boradName}-${Date().time}"))
        val combineSource = socketStream.takeUntil(single)
        combineSource.subscribe(onUploadComplete(activity,buidler))
    }
}
class onUploadComplete(activity: AppCompatActivity,buidler:AlertDialog):Observer<String>{
    val activity=activity
    val buidler=buidler
    override fun onNext(t: String) {
        return;
    }

    override fun onError(e: Throwable) {
        buidler.dismiss()
        val message = e.toString().replace("java.lang.Throwable:","")
        activity.alert(message) {
            title="Warning"
            yesButton { activity.toast("Please input new Boards name") }
        }.show()
        return;
    }

    override fun onSubscribe(d: Disposable) {
        return;
    }
    override fun onComplete() {
        buidler.dismiss()
        val intent = Intent(activity,Dashboard::class.java)
        activity.startActivity(intent)
    }

}