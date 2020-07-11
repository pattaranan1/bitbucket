package com.readaily.app.Timer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.readaily.app.R
import com.readaily.app.ui.main.TimerSectionsPagerAdapter
import kotlinx.android.synthetic.main.fragment_timer.*


class TimerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_timer, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val timersectionsPagerAdapter = TimerSectionsPagerAdapter(requireContext(), childFragmentManager)
        view_pager2.adapter = timersectionsPagerAdapter
        tabs2.setupWithViewPager(view_pager2)
        tabs2.getTabAt(0)!!.setIcon(R.drawable.ic_countdown)
        tabs2.getTabAt(1)!!.setIcon(R.drawable.ic_stopwatch)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)

        fragmentManager!!.beginTransaction().detach(this).attach(this).commit()
    }

}
