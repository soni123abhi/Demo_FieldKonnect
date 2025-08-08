package com.exp.clonefieldkonnect.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.adapter.CustomerActivityAdapter
import com.exp.clonefieldkonnect.helper.Constant
import com.exp.clonefieldkonnect.helper.StaticSharedpreference
import com.exp.clonefieldkonnect.model.CustomerActivityModel

class CustomerHistoryActivity : AppCompatActivity() {
    lateinit var cardBack_activity: CardView
    lateinit var recyclerView_user_detail: RecyclerView
    var customeractivityList: ArrayList<CustomerActivityModel> = ArrayList()

    var customerId = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_history)
        initViews()
    }

    private fun initViews() {
        cardBack_activity = findViewById(R.id.cardBack_activity)
        recyclerView_user_detail = findViewById(R.id.recyclerView_user_detail)

        cardBack_activity.setOnClickListener {
            handelbackpress()
        }
        customerId = StaticSharedpreference.getInfo(Constant.CHECKIN_CUST_ID, this@CustomerHistoryActivity).toString()
        println("customerIdcustomerId=="+customerId)

        customeractivityList = intent.getSerializableExtra("activity_list") as? ArrayList<CustomerActivityModel> ?: arrayListOf()
        setuprecyclerview()


    }

    private fun setuprecyclerview() {
        var mLayoutManager = LinearLayoutManager(this@CustomerHistoryActivity)
        recyclerView_user_detail.layoutManager = mLayoutManager
        val useractivity = CustomerActivityAdapter(this@CustomerHistoryActivity, customeractivityList)
        recyclerView_user_detail.adapter = useractivity
    }

    private fun handelbackpress() {
        onBackPressed()
    }
}