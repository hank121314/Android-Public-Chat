package com.hank121314.hankchen.androidproject.View.Dashboard.Adapter


import android.content.res.Resources
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import com.hank121314.hankchen.androidproject.R
import com.hank121314.hankchen.androidproject.View.Dashboard.Pager.BoardList
import com.hank121314.hankchen.androidproject.View.Dashboard.Pager.SearchBoard


class PageAdapter(fragmentManager:FragmentManager,activity: AppCompatActivity): FragmentPagerAdapter(fragmentManager) {
    val activity=activity
    override fun getItem(position: Int): Fragment {
        when(position){
            0 -> return BoardList()
            1 -> return SearchBoard()
            else -> return BoardList()
        }
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence {
        when(position){
            0 -> return  "${activity.resources.getString(R.string.dashboard_title)} Boards"
            1 -> return "${activity.resources.getString(R.string.search)} Search"
            2 -> return "${activity.resources.getString(R.string.profile)} Profile"
            else -> return "Boards"
        }
    }

}