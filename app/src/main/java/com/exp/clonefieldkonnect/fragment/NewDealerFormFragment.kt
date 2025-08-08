package com.exp.clonefieldkonnect.fragment

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.activity.MainActivity

class NewDealerFormFragment(linearTopreport: CardView, tabPosition: Int, s: String) : Fragment() {

    lateinit var activityLocal: Activity
    private lateinit var rootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_new_dealer_form, container, false)
        activityLocal = activity as MainActivity
        initViews()
        return rootView
    }

    private fun initViews() {
        Toast.makeText(activityLocal,"Working on it..!!", Toast.LENGTH_SHORT).show()
        var rec_user_main_apooo : RelativeLayout = rootView.findViewById(R.id.rec_user_main_apooo)
        rec_user_main_apooo.visibility = View.VISIBLE
    }

}