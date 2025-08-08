package com.exp.clonefieldkonnect.activity

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.adapter.OrderEditDetailAdapter
import com.exp.clonefieldkonnect.connection.APIResultLitener
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.helper.Constant
import com.exp.clonefieldkonnect.helper.DialogClass
import com.exp.clonefieldkonnect.helper.StaticSharedpreference
import com.exp.clonefieldkonnect.model.OrderDetailsModel
import com.exp.import.Utilities
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Response

class OrderDetailEditActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var edtDistributor: AutoCompleteTextView
    lateinit var cardBack: CardView
    lateinit var tvBuyer: TextView
    lateinit var tvBuyeradd: TextView
    lateinit var tvSeller: TextView
    lateinit var tvSelleraddd: TextView
    lateinit var tvOrderNo: TextView
    lateinit var tvOrderdate: TextView
    lateinit var tvOrdercreatedby: TextView
    lateinit var tvTitle: TextView

    lateinit var linear_ebd: LinearLayout
    lateinit var linear_cluster: LinearLayout
    lateinit var linear_cash: LinearLayout
    lateinit var linear_total: LinearLayout
    lateinit var linear_dealer: LinearLayout
    lateinit var linear_distributor: LinearLayout
    lateinit var linear_Frieght: LinearLayout
    lateinit var linear_ebd_dis: LinearLayout
    lateinit var linear_special: LinearLayout
    lateinit var linear_5gst: LinearLayout
    lateinit var linear_12gst: LinearLayout
    lateinit var linear_18gst: LinearLayout
    lateinit var linear_28gst: LinearLayout

    lateinit var linearNotes: LinearLayout
    lateinit var linearButton: LinearLayout
    lateinit var cardapprove: CardView
    lateinit var cardreject: CardView

    var disPos = -1
    var storePos = -1
    var customer_type_id = ""
    var status = ""
    var type = ""

    companion object {
        lateinit var tvTotal : TextView
        lateinit var tvGSTTotal: TextView
        lateinit var tvAmount: TextView
        lateinit var tv_5gstamt: TextView
        lateinit var tv_12gstamt: TextView
        lateinit var tv_18gstamt: TextView
        lateinit var tv_28gstamt: TextView
        lateinit var tv_ebd_amt: TextView
        lateinit var tv_cluster_amt: TextView
        lateinit var tv_clusterfilter: TextView
        lateinit var tv_dealerfilter: TextView
        lateinit var tv_dealer_amt: TextView
        lateinit var tv_distributorfilter: TextView
        lateinit var tv_distributor_amt: TextView
        lateinit var tv_frieghtfilter: TextView
        lateinit var tv_Frieght_amt: TextView
        lateinit var tv_remark: EditText
        lateinit var tv_ebdfilter: TextView
        lateinit var tv_ebd_dis_amt: TextView
        lateinit var tv_specialfilter: TextView
        lateinit var tv_special_amt: TextView
        lateinit var tv_cashfilter: TextView
        lateinit var tv_cash_amt: TextView
        lateinit var tv_totalfilter: TextView
        lateinit var tv_total_amt: TextView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail_edit)
        initViews()
    }

    private fun initViews() {
        tvOrderNo = findViewById(R.id.tvOrderNo)
        tvBuyer = findViewById(R.id.tvBuyer)
        tvBuyeradd = findViewById(R.id.tvBuyeradd)
        tvSeller = findViewById(R.id.tvSeller)
        tvSelleraddd = findViewById(R.id.tvSelleraddd)
        tvTotal = findViewById(R.id.tvTotal)
        tvGSTTotal = findViewById(R.id.tvGSTTotal)
        tvAmount = findViewById(R.id.tvAmount)
        tvOrdercreatedby = findViewById(R.id.tvOrdercreatedby)
        tvOrderdate = findViewById(R.id.tvOrderdate)
        tvTitle = findViewById(R.id.tvTitle)

        recyclerView = findViewById(R.id.recyclerView)
        edtDistributor = findViewById(R.id.edtDistributor)
        cardBack = findViewById(R.id.cardBack)

        linear_ebd = findViewById(R.id.linear_ebd)
        tv_ebd_amt = findViewById(R.id.tv_ebd_amt)

        linear_cluster = findViewById(R.id.linear_cluster)
        tv_clusterfilter = findViewById(R.id.tv_clusterfilter)
        tv_cluster_amt = findViewById(R.id.tv_cluster_amt)

        linear_cash = findViewById(R.id.linear_cash)
        tv_cashfilter = findViewById(R.id.tv_cashfilter)
        tv_cash_amt = findViewById(R.id.tv_cash_amt)

        linear_total = findViewById(R.id.linear_total)
        tv_totalfilter = findViewById(R.id.tv_totalfilter)
        tv_total_amt = findViewById(R.id.tv_total_amt)

        linear_dealer = findViewById(R.id.linear_dealer)
        tv_dealerfilter = findViewById(R.id.tv_dealerfilter)
        tv_dealer_amt = findViewById(R.id.tv_dealer_amt)

        linear_distributor = findViewById(R.id.linear_distributor)
        tv_distributorfilter = findViewById(R.id.tv_distributorfilter)
        tv_distributor_amt = findViewById(R.id.tv_distributor_amt)

        linear_Frieght = findViewById(R.id.linear_Frieght)
        tv_frieghtfilter = findViewById(R.id.tv_frieghtfilter)
        tv_Frieght_amt = findViewById(R.id.tv_Frieght_amt)

        linear_5gst = findViewById(R.id.linear_5gst)
        tv_5gstamt = findViewById(R.id.tv_5gstamt)

        linear_12gst = findViewById(R.id.linear_12gst)
        tv_12gstamt = findViewById(R.id.tv_12gstamt)

        linear_18gst = findViewById(R.id.linear_18gst)
        tv_18gstamt = findViewById(R.id.tv_18gstamt)

        linear_28gst = findViewById(R.id.linear_28gst)
        tv_28gstamt = findViewById(R.id.tv_28gstamt)

        linearNotes = findViewById(R.id.linearNotes)
        tv_remark = findViewById(R.id.tv_remark)

        linearButton = findViewById(R.id.linearButton)
        cardapprove = findViewById(R.id.cardapprove)
        cardreject = findViewById(R.id.cardreject)

        linear_ebd_dis = findViewById(R.id.linear_ebd_dis)
        tv_ebdfilter = findViewById(R.id.tv_ebdfilter)
        tv_ebd_dis_amt = findViewById(R.id.tv_ebd_dis_amt)

        linear_special = findViewById(R.id.linear_special)
        tv_specialfilter = findViewById(R.id.tv_specialfilter)
        tv_special_amt = findViewById(R.id.tv_special_amt)

        recyclerView.layoutManager = GridLayoutManager(this, 1)

        cardBack.setOnClickListener {
            onBackPressed()
        }

        tvOrderNo.setText("Order No. : "+intent.getStringExtra("no"))
        status = intent.getStringExtra("Status").toString()
        type = intent.getStringExtra("type").toString()

        var roll_id = StaticSharedpreference.getInfo(Constant.ROLL_ID,this@OrderDetailEditActivity)
        val gson = Gson()
        val rollIdListType = object : TypeToken<List<Int>>() {}.type
        val rollIdList: List<Int> = gson.fromJson(roll_id, rollIdListType)
        var compare_ids = listOf(1,19)
        var containsSimilarValue = rollIdList.any { it in compare_ids }

        println("statusstatus="+status+"<<"+type+"<<"+containsSimilarValue)

        if (type.equals("special")){
            tvTitle.text = "Special Order Detail"
        }else{
            tvTitle.text = "Cluster Order Detail"
        }
        getOrderDetails(status,type, containsSimilarValue)
    }

    private fun getOrderDetails(status: String, type: String, containsSimilarValue: Boolean) {

        if (!Utilities.isOnline(this)) {
            return
        }

        var dialog = DialogClass.progressDialog(this)
        val queryParams = HashMap<String, String>()
        queryParams["order_id"] = intent.getIntExtra("id", 0).toString()
        println("IDDORRRR=="+intent.getIntExtra("id", 0).toString())
        ApiClient.getOrderDetails(
            StaticSharedpreference.getInfo(
                Constant.ACCESS_TOKEN,
                this@OrderDetailEditActivity
            ).toString(), queryParams, object : APIResultLitener<OrderDetailsModel> {
                override fun onAPIResult(
                    response: Response<OrderDetailsModel>?,
                    errorMessage: String?
                ) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {

                            tvGSTTotal.text = response.body()!!.data!!.totalGst
                            tvAmount.text = response.body()!!.data!!.subTotal
                            tvTotal.text = response.body()!!.data!!.grandTotal
                            tvBuyer.text = "Buyer :- "+response.body()!!.data!!.buyer_name +" || "+response.body()!!.data!!.buyers!!.mobile.toString()
                            tvSeller.text ="Seller :- "+response.body()!!.data!!.seller_name +" || "+response.body()!!.data!!.sellers!!.mobile.toString()
                            tvBuyeradd.text = "Buyer Add. :- "+response.body()!!.data!!.buyer_address!!.address1.toString()
                            tvSelleraddd.text = "Seller Add. :- "+response.body()!!.data!!.seller_address!!.address1.toString()
                            customer_type_id =response.body()!!.data!!.buyers!!.customertypes!!.id.toString()
                            tvOrdercreatedby.text = "Created by :- "+response.body()!!.data!!.createdbyname!!.name.toString()
                            tvOrderdate.text = "Order Date :- "+response.body()!!.data!!.orderDate.toString()

//                            println("SHHHHHH++="+containsSimilarValue+"<<"+status+"<<"+
//                                    response.body()!!.data!!.created_by+"<<"+StaticSharedpreference.getInfo(Constant.USERID,this@OrderDetailEditActivity).toString()+
//                            "<<"+type)

                            if (status.equals("Approved")){
                                tv_clusterfilter.isEnabled = false
                                tv_cluster_amt.isEnabled = false
                                tv_ebdfilter.isEnabled = false
                                tv_ebd_dis_amt.isEnabled = false
                                tv_specialfilter.isEnabled = false
                                tv_special_amt.isEnabled = false
                                tv_dealerfilter.isEnabled = false
                                tv_dealer_amt.isEnabled = false
                                tv_distributorfilter.isEnabled = false
                                tv_distributor_amt.isEnabled = false
                                tv_frieghtfilter.isEnabled = false
                                tv_Frieght_amt.isEnabled = false
                                tv_cashfilter.isEnabled = false
                                tv_cash_amt.isEnabled = false
                                cardapprove.visibility = View.GONE
                                cardreject.visibility = View.GONE

                            }
                            else if (response.body()!!.data!!.created_by.equals(StaticSharedpreference.getInfo(Constant.USERID,this@OrderDetailEditActivity).toString())){
                                tv_clusterfilter.isEnabled = false
                                tv_cluster_amt.isEnabled = false
                                tv_ebdfilter.isEnabled = false
                                tv_ebd_dis_amt.isEnabled = false
                                tv_specialfilter.isEnabled = false
                                tv_special_amt.isEnabled = false
                                tv_dealerfilter.isEnabled = false
                                tv_dealer_amt.isEnabled = false
                                tv_distributorfilter.isEnabled = false
                                tv_distributor_amt.isEnabled = false
                                tv_frieghtfilter.isEnabled = false
                                tv_Frieght_amt.isEnabled = false
                                tv_cashfilter.isEnabled = false
                                tv_cash_amt.isEnabled = false
                                cardapprove.visibility = View.GONE
                                cardreject.visibility = View.GONE
                            }
                            else if (type.equals("special") && containsSimilarValue.equals(true)){
                                tv_clusterfilter.isEnabled = true
                                tv_cluster_amt.isEnabled = true
                                tv_ebdfilter.isEnabled = true
                                tv_ebd_dis_amt.isEnabled = true
                                tv_specialfilter.isEnabled = true
                                tv_special_amt.isEnabled = true
                                tv_dealerfilter.isEnabled = true
                                tv_dealer_amt.isEnabled = true
                                tv_distributorfilter.isEnabled = true
                                tv_distributor_amt.isEnabled = true
                                tv_frieghtfilter.isEnabled = true
                                tv_Frieght_amt.isEnabled = true
                                tv_cashfilter.isEnabled = true
                                tv_cash_amt.isEnabled = true
                                cardapprove.visibility = View.VISIBLE
                                cardreject.visibility = View.VISIBLE
                            }
                            else if (status.equals("Pending") && !type.equals("special")){
                                tv_clusterfilter.isEnabled = true
                                tv_cluster_amt.isEnabled = true
                                tv_ebdfilter.isEnabled = true
                                tv_ebd_dis_amt.isEnabled = true
                                tv_specialfilter.isEnabled = true
                                tv_special_amt.isEnabled = true
                                tv_dealerfilter.isEnabled = true
                                tv_dealer_amt.isEnabled = true
                                tv_distributorfilter.isEnabled = true
                                tv_distributor_amt.isEnabled = true
                                tv_frieghtfilter.isEnabled = true
                                tv_Frieght_amt.isEnabled = true
                                tv_cashfilter.isEnabled = true
                                tv_cash_amt.isEnabled = true
                                cardapprove.visibility = View.VISIBLE
                                cardreject.visibility = View.VISIBLE
                            }
                            var flag_ebd = false
                            flag_ebd = response.body()?.data?.ebd_amount?.toString()?.isNotEmpty() == true

                            val orderDetails =   response.body()!!.data!!.orderdetails

                            val orderAddToCardAdapter = OrderEditDetailAdapter(this@OrderDetailEditActivity,orderDetails,response.body()!!.data!!.buyer_type,
                                cardapprove,cardreject,response.body()!!.data!!.id,response.body()!!.data!!.created_by,type,flag_ebd)
                            recyclerView.adapter = orderAddToCardAdapter

                            if (response.body()?.data?.schme_amount?.toString()?.isNotEmpty() == true) {
                                linear_ebd.visibility = View.VISIBLE
                                tv_ebd_amt.text = response.body()?.data?.schme_amount?.toString()
                            }
                            if (response.body()?.data?.ebd_amount?.toString()?.isNotEmpty() == true) {
                                linear_ebd_dis.visibility = View.VISIBLE
                                tv_ebdfilter.text = response.body()!!.data!!.ebd_discount!!.toString()
                                tv_ebd_dis_amt.text = response.body()?.data?.ebd_amount?.toString()
                            }
                            if (response.body()?.data?.special_amount?.toString()?.isNotEmpty() == true) {
                                linear_special.visibility = View.VISIBLE
                                tv_specialfilter.text = response.body()!!.data!!.special_discount!!.toString()
                                tv_special_amt.text = response.body()?.data?.special_amount?.toString()
                            }
                            if (response.body()?.data?.cluster_amount?.toString()?.isNotEmpty() == true) {
                                linear_cluster.visibility = View.VISIBLE
                                tv_clusterfilter.text = response.body()!!.data!!.cluster_discount!!.toString()
                                tv_cluster_amt.text = response.body()!!.data!!.cluster_amount!!.toString()
                            }

                            if (response.body()?.data?.cash_amount?.toString()?.isNotEmpty() == true) {
                                linear_cash.visibility = View.VISIBLE
                                tv_cashfilter.text = response.body()!!.data!!.cash_discount!!.toString()
                                tv_cash_amt.text = response.body()!!.data!!.cash_amount!!.toString()
                            }

                            if (response.body()?.data?.total_amount?.toString()?.isNotEmpty() == true) {
                                linear_total.visibility = View.VISIBLE
                                tv_totalfilter.text = response.body()!!.data!!.total_discount!!.toString()
                                tv_total_amt.text = response.body()!!.data!!.total_amount!!.toString()
                            }
                            if (response.body()?.data?.deal_amount?.toString()?.isNotEmpty() == true) {
                                linear_dealer.visibility = View.VISIBLE
                                tv_dealerfilter.text = response.body()!!.data!!.deal_discount!!.toString()
                                tv_dealer_amt.text = response.body()!!.data!!.deal_amount!!.toString()
                            }
                            if (response.body()?.data?.distributor_amount?.toString()?.isNotEmpty() == true) {
                                linear_distributor.visibility = View.VISIBLE
                                tv_distributorfilter.text = response.body()!!.data!!.distributor_discount!!.toString()
                                tv_distributor_amt.text = response.body()!!.data!!.distributor_amount!!.toString()
                            }
                            if (response.body()?.data?.frieght_amount?.toString()?.isNotEmpty() == true) {
                                linear_Frieght.visibility = View.VISIBLE
                                tv_frieghtfilter.text = response.body()!!.data!!.frieght_discount!!.toString()
                                tv_Frieght_amt.text = response.body()!!.data!!.frieght_amount!!.toString()
                            }
                            if (response.body()?.data?.gst5_amt?.toString()?.isNotEmpty() == true) {
                                linear_5gst.visibility = View.VISIBLE
                                tv_5gstamt.text = response.body()!!.data!!.gst5_amt!!.toString()
                            }
                            if (response.body()?.data?.gst12_amt?.toString()?.isNotEmpty() == true) {
                                linear_12gst.visibility = View.VISIBLE
                                tv_12gstamt.text = response.body()!!.data!!.gst12_amt!!.toString()
                            }
                            if (response.body()?.data?.gst18_amt?.toString()?.isNotEmpty() == true) {
                                linear_18gst.visibility = View.VISIBLE
                                tv_18gstamt.text = response.body()!!.data!!.gst18_amt!!.toString()
                            }
                            if (response.body()?.data?.gst28_amt?.toString()?.isNotEmpty() == true) {
                                linear_28gst.visibility = View.VISIBLE
                                tv_28gstamt.text = response.body()!!.data!!.gst28_amt!!.toString()
                            }
                            if (response.body()?.data?.order_remark?.toString()?.isNotEmpty() == true) {
                                linearNotes.visibility = View.VISIBLE
                                tv_remark.setText(response.body()!!.data!!.order_remark!!.toString())
                            }
                        }
                    } else {
                        Toast.makeText(
                            this@OrderDetailEditActivity,
                            resources.getString(R.string.poor_connection),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        )
    }
}