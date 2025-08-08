package com.exp.clonefieldkonnect.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.fragment.AttendanceFragment

class AttendanceActivity : AppCompatActivity() {

    private lateinit var cardBack: CardView

    var working_type = ""
    var punchin_flag = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_received)
        working_type = intent.getStringExtra("working_type_").toString()
        punchin_flag = intent.getStringExtra("punchin_flag_").toString()
        println("punchin_flag 1 ${punchin_flag}")
        initViews()
    }

    private fun initViews() {

        cardBack = findViewById(R.id.cardBack)
        cardBack.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
//            onBackPressed()
        }

        goToFragment(AttendanceFragment())
    }

    private fun goToFragment(fragment: Fragment) {

        val fragmentTransaction = supportFragmentManager.beginTransaction()

        val args = Bundle()
        args.putString("from", intent.getStringExtra("from"))
        args.putString("working_type_", working_type)
        args.putString("punchin_flag", punchin_flag)
        fragment.arguments = args

        fragmentTransaction.replace(R.id.layout, fragment, "Fragment")
        fragmentTransaction.commit()
    }


    override fun onResume() {
        super.onResume()

    }

}