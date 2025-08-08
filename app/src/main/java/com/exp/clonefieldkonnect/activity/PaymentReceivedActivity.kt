package com.exp.clonefieldkonnect.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.fragment.PaymentReceivedFragment

class PaymentReceivedActivity : AppCompatActivity() {


    private lateinit var cardBack: CardView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_received)

        initViews()
    }

    private fun initViews() {

        cardBack = findViewById(R.id.cardBack)
        cardBack.setOnClickListener {
            onBackPressed()
        }
        goToFragment(PaymentReceivedFragment())
    }

    private fun goToFragment(fragment: Fragment) {

        val fragmentTransaction = supportFragmentManager.beginTransaction()

        val args = Bundle()
        args.putString("from", intent.getStringExtra("from"))
        fragment.arguments = args

        fragmentTransaction.replace(R.id.layout, fragment, "Fragment")
        fragmentTransaction.commit()
    }


    override fun onResume() {
        super.onResume()

    }

}