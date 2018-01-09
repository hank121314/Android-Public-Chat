package com.hank121314.hankchen.androidproject.ViewModal.Dashboard.Pager

import android.graphics.BitmapFactory
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.hank121314.hankchen.androidproject.R
import com.hank121314.hankchen.androidproject.Stream.imageDownloaderBoards
import com.hank121314.hankchen.androidproject.Stream.imageDownloaderUser
import com.hank121314.hankchen.androidproject.View.Dashboard.Adapter.ListAdapter
import com.hank121314.hankchen.androidproject.View.Dashboard.Dashboard
import com.hank121314.hankchen.androidproject.View.Dashboard.Pager.Profile
import com.hank121314.hankchen.androidproject.helper.LargeBitmap
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import org.jetbrains.anko.imageBitmap
import org.w3c.dom.Text
import java.io.File
import java.util.*

/**
 * Created by hankchen on 2018/1/4.
 */
class Profile_VM {
}
class FetchingUser(activity: Dashboard,Profile:Profile,alert:AlertDialog): Observer<String> {
    val activity=activity
    val alert=alert
    val Profile=Profile
    override fun onComplete() {
        return;
    }

    override fun onNext(t: String) {
        val parser = JsonParser()
        val data=parser.parse(t)
        val dataValue =data.asJsonObject.get("fulfillmentValue").asJsonObject.get("data").asJsonObject
        val username = dataValue.get("username").asString
        val imageStream = Observable.create(imageDownloaderUser(activity,username))
        imageStream.subscribe { s->
            val imgView  = activity.findViewById<ImageView>(R.id.profile_ImageView)
            val username  = activity.findViewById<TextView>(R.id.profile_username)
            val name = activity.findViewById<TextView>(R.id.profile_name)
            val birth = activity.findViewById<TextView>(R.id.profile_birth)
            username.setText("${dataValue.get("username").asString}")
            name.setText("${dataValue.get("name").asString}")
            birth.setText("${dataValue.get("birth").asString}")
            val img = File(s)
            val rounded = RoundedBitmapDrawableFactory.create(activity.resources,LargeBitmap().decodeSampledBitmapFromFile(activity.resources,img,500,500))
            rounded.cornerRadius=500F
            imgView.setImageDrawable(rounded)
            Profile.fetched=true
            alert.dismiss()
        }
    }

    override fun onError(e: Throwable) {
        return;
    }

    override fun onSubscribe(d: Disposable) {
        return;
    }

}