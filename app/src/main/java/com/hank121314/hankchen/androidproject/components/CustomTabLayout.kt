package com.hank121314.hankchen.androidproject.components

/**
 * Created by hankchen on 2017/12/20.
 */
import android.content.Context
import android.graphics.Typeface
import android.support.design.widget.TabLayout
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.TextView
import com.hank121314.hankchen.androidproject.R

class CustomTabLayout : TabLayout {
    private var mTypeface: Typeface? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        mTypeface = Typeface.createFromAsset(context.assets,"iconfont.ttf")
    }

    override fun addTab(tab: TabLayout.Tab) {
        super.addTab(tab)

        val mainView = getChildAt(0) as ViewGroup
        val tabView = mainView.getChildAt(tab.position) as ViewGroup

        val tabChildCount = tabView.childCount
        for (i in 0 until tabChildCount) {
            val tabViewChild = tabView.getChildAt(i)
            (tabViewChild as? TextView)?.setTypeface(mTypeface)

        }
    }

}