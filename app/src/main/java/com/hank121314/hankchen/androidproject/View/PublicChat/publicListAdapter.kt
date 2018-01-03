package com.hank121314.hankchen.androidproject.View.PublicChat

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.hank121314.hankchen.androidproject.R
import org.jetbrains.anko.imageBitmap
import java.io.File

/**
 * Created by hankchen on 2018/1/2.
 */
class publicListAdapter(context: Context, itemList: List<Map<String, String>>): BaseAdapter() {
    val context=context
    private var mLayInf: LayoutInflater? = null
    var mItemList: List<Map<String, String>>? = null

    init{
        mLayInf = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mItemList = itemList
    }
    override fun getCount(): Int {
        return mItemList!!.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val v = mLayInf!!.inflate(R.layout.chat_view, parent, false)
        val titleView = v.findViewById(R.id.titleView) as TextView
        val userView = v.findViewById(R.id.userView) as TextView
        val txtView = v.findViewById(R.id.createdView) as TextView
        userView.text=mItemList!!.get(position)["send"].toString()
        titleView.text = mItemList!!.get(position)["message"].toString()
        txtView.text = mItemList!!.get(position)["timestamp"].toString()
        return v
    }
}