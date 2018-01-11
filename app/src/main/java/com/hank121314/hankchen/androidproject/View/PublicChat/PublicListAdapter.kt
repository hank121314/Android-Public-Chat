package com.hank121314.hankchen.androidproject.View.PublicChat

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.provider.ContactsContract
import android.support.annotation.UiThread
import android.support.v4.graphics.drawable.RoundedBitmapDrawable
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.hank121314.hankchen.androidproject.R
import com.hank121314.hankchen.androidproject.helper.LargeBitmap
import com.hank121314.hankchen.androidproject.helper.ViewHolder
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.imageBitmap
import org.jetbrains.anko.onComplete
import org.jetbrains.anko.uiThread
import java.io.File
import java.sql.Date
import kotlin.math.round

/**
 * Created by hankchen on 2018/1/2.
 */
class PublicListAdapter(context: Context, itemList: ArrayList<Map<String, String>>): BaseAdapter() {
    val context=context
    private var mLayInf: LayoutInflater? = null
    var mItemList=arrayListOf<Map<String, String>>()
    val iconfont = Typeface.createFromAsset(context.assets, "iconfont.ttf")


    init{
        mLayInf = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mItemList = itemList
    }
    override fun getCount(): Int {
        return mItemList.size
    }

    override fun getItem(position: Int): Any {
        return mItemList.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var v:View
        var holder:ViewHolder
        if(convertView==null){
            holder=ViewHolder()
            v = mLayInf!!.inflate(R.layout.chat_view, parent, false)
            holder.imgView=v.findViewById(R.id.imageView) as ImageView
            holder.userView=v.findViewById(R.id.userView) as TextView
            holder.titleView=v.findViewById(R.id.titleView) as TextView
            holder.txtView=v.findViewById(R.id.createdView) as TextView
            holder.sent=v.findViewById(R.id.correct) as ImageView
            holder.txtView!!.setTypeface(iconfont)
            v.setTag(holder)
        }
        else {
            v=convertView
            holder=v.getTag() as ViewHolder
        }
        doAsync {
            val img = File(mItemList!!.get(position)["image"].toString())
            var rounded:RoundedBitmapDrawable
            if(File(mItemList!!.get(position)["image"].toString()).exists()) {
                rounded = RoundedBitmapDrawableFactory.create(context.resources, LargeBitmap().decodeSampledBitmapFromFile(context.resources, img, 50, 50))
                rounded.isCircular = true
                onComplete {
                    uiThread {
                        holder.imgView!!.setImageDrawable(rounded)
                    }
                }
            }
            else{
                onComplete {
                    uiThread {
                        holder.imgView!!.setImageDrawable(null)
                    }
                }
            }

        }
        holder.userView!!.text = mItemList!!.get(position)["user"].toString()
        holder.titleView!!.text = mItemList!!.get(position)["message"].toString()
        holder.txtView!!.text= Date(mItemList!!.get(position)["timestamp"]!!.toLong()).toLocaleString()
        if(mItemList!!.get(position)["sent"].toString()==true.toString()) {
            holder.txtView!!.text="${Date(mItemList!!.get(position)["timestamp"]!!.toLong()).toLocaleString()} ${context.resources.getString(R.string.correct)}"
        }
        return v
    }
}
