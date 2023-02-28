package com.example.musicals.views.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.musicals.views.fragments.DownloaderFragment
import com.example.musicals.views.fragments.OnlinePlayerFragment
import com.example.musicals.views.fragments.ProfileFragment

class HomeFragmentAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    private val fragmentList = ArrayList<Fragment>()
    private val fragmentTitle = ArrayList<String?>()

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentTitle[position]
    }

    override fun getItem(position: Int): Fragment {
       return fragmentList[position]
    }

    fun addFragment(fragment: Fragment , title : String?){
        fragmentList.add(fragment)
        fragmentTitle.add(title)
    }
}