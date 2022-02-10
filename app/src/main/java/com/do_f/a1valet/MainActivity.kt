package com.do_f.a1valet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.paging.ExperimentalPagingApi
import com.do_f.a1valet.base.BFragment
import com.do_f.a1valet.features.home.ui.HomeFragment

@ExperimentalPagingApi
class MainActivity : AppCompatActivity(), BFragment.OnFragmentInteraction {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, HomeFragment.newInstance())
            .commit()
    }

    override fun replace(f: Fragment, withAnimation: Boolean) {
        val tag: String = f::class.java.simpleName
        if (withAnimation) {
            supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left,
                    R.anim.slide_in_from_left, R.anim.slide_out_to_right)
                .replace(R.id.container, f, tag)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(tag)
                .commit()
        } else {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, f, tag)
                .addToBackStack(tag)
                .commit()
        }
    }

    override fun onBackPressed() {
        val f = supportFragmentManager.findFragmentById(R.id.container)
        if (f is BFragment) {
            if (f.mOnBackPressed()) {
                super.onBackPressed()
            }
        } else {
            super.onBackPressed()
        }
    }
}