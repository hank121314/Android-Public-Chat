package com.hank121314.hankchen.androidproject.View.Dashboard.Adapter


import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v7.app.AppCompatActivity
import com.hank121314.hankchen.androidproject.R
import com.hank121314.hankchen.androidproject.View.Dashboard.Pager.BoardList
import com.hank121314.hankchen.androidproject.View.Dashboard.Pager.Profile
import com.hank121314.hankchen.androidproject.View.Dashboard.Pager.SearchBoard

class PageAdapter(fragmentManager:FragmentManager,activity: AppCompatActivity): FragmentStatePagerAdapter(fragmentManager) {
    val activity=activity
    override fun getItem(position: Int): Fragment {
        when(position){
            0 -> return BoardList()
//            1 -> return SearchBoard()
            1 -> return Profile()
            else->return BoardList()
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence {
        when(position){
            0 -> return  "${activity.resources.getString(R.string.dashboard_title)} ${activity.resources.getString(R.string.board)} "
//            1 -> return "${activity.resources.getString(R.string.search)} Search"
            1 -> return "${activity.resources.getString(R.string.profile)} ${activity.resources.getString(R.string.Profile)}"
            else -> return "Boards"
        }
    }

}