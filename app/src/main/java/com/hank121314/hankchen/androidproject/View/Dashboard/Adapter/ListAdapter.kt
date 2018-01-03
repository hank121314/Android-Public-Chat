package com.hank121314.hankchen.androidproject.View.Dashboard.Adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.media.Image
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import com.hank121314.hankchen.androidproject.R
import org.jetbrains.anko.imageBitmap
import java.io.File
import java.util.*


/**
 * Created by hankchen on 2017/12/21.
 */
class ListAdapter(context: Context, itemList: List<Map<String, String>>):BaseAdapter() {
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
        val v = mLayInf!!.inflate(R.layout.boardlist_view, parent, false)
        val titleView = v.findViewById(R.id.titleView) as TextView
        val txtView = v.findViewById(R.id.createdView) as TextView
        val imageView = v.findViewById(R.id.imageView) as ImageView
        val file = File(mItemList!!.get(position)["image"].toString())
        titleView.text = mItemList!!.get(position)["boards"].toString()
        txtView.text = mItemList!!.get(position)["created"].toString()
        imageView.imageBitmap = BitmapFactory.decodeFile(file.path)
        return v
    }
}