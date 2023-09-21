package com.manila.fasaldoctor.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ChatViewPagerAdapter(
    private val context: Context,
    fm : FragmentManager?,
    private val list: ArrayList<Fragment>
) : FragmentPagerAdapter(fm!!) {
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Fragment {
        return list[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
//        return super.getPageTitle(position)
        return TAB_TITLES[position]
    }

    companion object{
        val TAB_TITLES = arrayOf("Chats","Recent")
    }
}