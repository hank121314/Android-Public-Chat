package com.hank121314.hankchen.androidproject.View.Dashboard.Pager

import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hank121314.hankchen.androidproject.R
import com.hank121314.hankchen.androidproject.View.Dashboard.Dashboard
import com.hank121314.hankchen.androidproject.ViewModal.Dashboard.Dashboard_VM
import org.jetbrains.anko.button
import org.jetbrains.anko.sdk25.listeners.onClick
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.textView
import org.jetbrains.anko.verticalLayout

/**
 * Created by hankchen on 2017/12/20.
 */
class SearchBoard: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val iconfont = Typeface.createFromAsset((activity as Dashboard).assets, "iconfont.ttf")
        return UI {
            verticalLayout {
                textView {
                    text = "${resources.getString(R.string.search)} Search"
                    textSize = 48f
                    textAlignment = View.TEXT_ALIGNMENT_CENTER
                    typeface = iconfont
                }
            }
        }.view
    }
}