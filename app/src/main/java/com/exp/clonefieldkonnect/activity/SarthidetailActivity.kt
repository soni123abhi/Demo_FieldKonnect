package com.exp.clonefieldkonnect.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.connection.APIResultLitener
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.helper.Constant
import com.exp.clonefieldkonnect.helper.DialogClass
import com.exp.clonefieldkonnect.helper.StaticSharedpreference
import com.exp.clonefieldkonnect.model.SarthiPointsModel
import com.exp.import.Utilities
import org.json.JSONObject
import retrofit2.Response

class SarthidetailActivity : AppCompatActivity() {
    lateinit var cardBack_activity: CardView
    lateinit var tv_total_point_val: TextView
    lateinit var tv_total_active_val: TextView
    lateinit var tv_pro_point_val: TextView
    lateinit var tv_total_redeem_val: TextView
    lateinit var tv_rejected_val: TextView
    lateinit var tv_total_balance_val: TextView
    var customerId = ""
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sarthidetail)
        initViews()
    }

    private fun initViews() {
        cardBack_activity = findViewById(R.id.cardBack_activity)
        tv_total_point_val = findViewById(R.id.tv_total_point_val)
        tv_total_active_val = findViewById(R.id.tv_total_active_val)
        tv_pro_point_val = findViewById(R.id.tv_pro_point_val)
        tv_total_redeem_val = findViewById(R.id.tv_total_redeem_val)
        tv_rejected_val = findViewById(R.id.tv_rejected_val)
        tv_total_balance_val = findViewById(R.id.tv_total_balance_val)

        cardBack_activity.setOnClickListener {
            handelbackpress()
        }
        customerId = intent.getStringExtra("customerId").toString()
        println("customerIdcustomerId=="+customerId)

        getsarthipoint(customerId)

    }


    private fun getsarthipoint(customerId: String) {
        isLoading = true
        if (!Utilities.isOnline(this@SarthidetailActivity)) {
            isLoading = false
            return
        }
        var dialog = DialogClass.progressDialog(this@SarthidetailActivity)
        val queryParams = HashMap<String, String>()
        queryParams["customer_id"] = customerId

        ApiClient.getsarthipoint(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this@SarthidetailActivity).toString(),
            queryParams,
            object : APIResultLitener<SarthiPointsModel> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onAPIResult(response: Response<SarthiPointsModel>?, errorMessage: String?) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {

                            tv_total_point_val.text = response.body()!!.data!!.totalPoints
                            tv_total_active_val.text = response.body()!!.data!!.activePoints.toString()
                            tv_pro_point_val.text = response.body()!!.data!!.provisionPoints.toString()
                            tv_total_redeem_val.text = response.body()!!.data!!.totalRedemption.toString()
                            tv_rejected_val.text = response.body()!!.data!!.totalRejected.toString()
                            tv_total_balance_val.text = response.body()!!.data!!.totalBalance.toString()

                        } else {

                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())
                                if (response.code() == 401){
                                    Toast.makeText(this@SarthidetailActivity,jsonObject.getString("message"), Toast.LENGTH_LONG).show()
                                    StaticSharedpreference.deleteSharedPreference(this@SarthidetailActivity)
                                    startActivity(Intent(this@SarthidetailActivity, LoginActivity::class.java))
                                    this@SarthidetailActivity.finishAffinity()
                                    println("Erroorr=="+jsonObject.getString("message"))
                                }else{
                                    DialogClass.alertDialog(
                                        jsonObject.getString("status"),
                                        jsonObject.getString("message"),
                                        this@SarthidetailActivity, false
                                    )
                                }

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        isLoading = false
                    }
                    else {
                        Toast.makeText(this@SarthidetailActivity, resources.getString(R.string.poor_connection), Toast.LENGTH_LONG).show()
                    }
                }
            })
    }

    private fun handelbackpress() {
        onBackPressed()
    }
}