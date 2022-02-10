package com.do_f.a1valet.base

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.do_f.a1valet.R

abstract class BFragment: Fragment() {

    private var mListener: OnFragmentInteraction? = null

    protected fun replace(f: Fragment, withAnimation: Boolean = true) {
        mListener?.replace(f, withAnimation)
    }

    protected fun mPopBackStack() {
        try {
            parentFragmentManager.popBackStack()
        } catch (e: IllegalStateException) {
            Log.e("BFragment", "mPopBackStack: ", e)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initListeners()
    }

    protected fun showErrorDialog(stringId: Int, yes: DialogInterface.OnClickListener?) {
        showErrorDialog(getString(stringId), yes)
    }

    protected fun showErrorDialog(message: String, yes: DialogInterface.OnClickListener?) {
        context?.let {
            val error = AlertDialog.Builder(it)
            error.setMessage(message)
            error.setCancelable(false)
            error.setPositiveButton(R.string.ok, yes)
            error.create().show()
        }
    }

    open fun mOnBackPressed(): Boolean = true

    abstract fun initViews()
    abstract fun initListeners()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteraction) {
            mListener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    interface OnFragmentInteraction {
        fun replace(f: Fragment, withAnimation: Boolean)
    }
}