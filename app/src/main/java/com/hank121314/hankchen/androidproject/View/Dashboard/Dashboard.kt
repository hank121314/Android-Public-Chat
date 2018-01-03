package com.hank121314.hankchen.androidproject.View.Dashboard

import android.graphics.Typeface
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.facebook.stetho.Stetho
import com.hank121314.hankchen.androidproject.R
import com.hank121314.hankchen.androidproject.services.socket
import android.view.ViewGroup
import com.hank121314.hankchen.androidproject.View.Dashboard.Adapter.PageAdapter
import com.hank121314.hankchen.androidproject.View.MainActivity


/**
 * Created by hankchen on 2017/12/19.
 */
class Dashboard :AppCompatActivity(){
    val socketIO= socket()
    private lateinit var tabLayout:TabLayout
    private lateinit var viewPager:ViewPager
    private lateinit var adapter: PageAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(
                                Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(
                                Stetho.defaultInspectorModulesProvider(this))
                        .build())
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard)
        setupView()
    }
    override fun onStart(){
        super.onStart()
        val iconfont = Typeface.createFromAsset(assets,"iconfont.ttf")
        val vg = tabLayout.getChildAt(0) as ViewGroup
        val tabsCount = vg.childCount
        for (j in 0 until tabsCount) {
            val vgTab = vg.getChildAt(j) as ViewGroup
            val tabChildsCount = vgTab.childCount
            for (i in 0 until tabChildsCount) {
                val tabViewChild = vgTab.getChildAt(i)
                (tabViewChild as? TextView)?.setTypeface(iconfont, Typeface.NORMAL)
            }
        }
    }
    private fun setupView() {
        // adapter
        adapter = PageAdapter(supportFragmentManager, this)

        // viewPager
        viewPager = findViewById(R.id.layout_main_viewPager)
        viewPager.adapter = adapter

        // tabLayout
        tabLayout = findViewById(R.id.layout_main_tabLayout)
        tabLayout.setupWithViewPager(viewPager)
    }
    companion object {
        private val TAG = Dashboard::class.java.simpleName
    }
}