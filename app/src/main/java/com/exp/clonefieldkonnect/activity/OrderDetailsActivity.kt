package com.exp.clonefieldkonnect.activity

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exp.import.Utilities
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.adapter.OrderDetailsAdapter
import com.exp.clonefieldkonnect.connection.APIResultLitener
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.helper.Constant
import com.exp.clonefieldkonnect.helper.DialogClass
import com.exp.clonefieldkonnect.helper.StaticSharedpreference
import com.exp.clonefieldkonnect.model.AttendanceSubmitModel
import com.exp.clonefieldkonnect.model.OrderDetailsModel
import com.exp.clonefieldkonnect.model.OrderPdfModel
import org.json.JSONObject
import retrofit2.Response
import java.util.HashMap

class OrderDetailsActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var edtDistributor: AutoCompleteTextView
    lateinit var cardSubmit: CardView
    lateinit var cardcancel: CardView
    lateinit var cardBack: CardView
    lateinit var tvTotal: TextView
    lateinit var tvGSTTotal: TextView
    lateinit var tvAmount: TextView
    lateinit var tvBuyer: TextView
    lateinit var tvBuyeradd: TextView
    lateinit var tvSeller: TextView
    lateinit var tvSelleraddd: TextView
    lateinit var tvOrderNo: TextView
    lateinit var linear_ebd: LinearLayout
    lateinit var tv_ebd_amt: TextView
    lateinit var tv_schmedis_value: TextView
    lateinit var linear_cluster: LinearLayout
    lateinit var tv_clusterfilter: TextView
    lateinit var tv_cluster_amt: TextView
    lateinit var linear_cash_pump: LinearLayout
    lateinit var tv_cashfilter_pump: TextView
    lateinit var tv_cash_amt_pump: TextView
    lateinit var linear_total_pump: LinearLayout
    lateinit var edt_totalfilter_pump: TextView
    lateinit var tv_total_amt_pump: TextView
    lateinit var linear_dealer: LinearLayout
    lateinit var edt_dealerfilter: TextView
    lateinit var tv_dealer_amt: TextView
    lateinit var linear_distributor: LinearLayout
    lateinit var edt_distributorfilter: TextView
    lateinit var tv_distributor_amt: TextView
    lateinit var linear_Frieght: LinearLayout
    lateinit var edt_Frieghtfilter: TextView
    lateinit var tv_Frieght_amt: TextView
    lateinit var linear_5gst: LinearLayout
    lateinit var tv_5gstamt: TextView
    lateinit var linear_12gst: LinearLayout
    lateinit var tv_12gstamt: TextView
    lateinit var linear_18gst: LinearLayout
    lateinit var tv_18gstamt: TextView
    lateinit var linear_28gst: LinearLayout
    lateinit var tv_28gstamt: TextView
    lateinit var linearNotes: LinearLayout
    lateinit var tv_remark: TextView
    lateinit var linear_ebd_dis: LinearLayout
    lateinit var tv_ebdfilter: TextView
    lateinit var tv_ebd_dis_amt: TextView
    lateinit var linear_special: LinearLayout
    lateinit var edt_specialfilter: TextView
    lateinit var tv_special_amt: TextView
    lateinit var tvOrderdate: TextView
    lateinit var tvOrdercreatedby: TextView
    lateinit var linear_Doddiscount: LinearLayout
    lateinit var tv_dodvalue: TextView
    lateinit var tv_dod_amt: TextView
    lateinit var linear_Specialdiscount: LinearLayout
    lateinit var tv_specialvalue: TextView
    lateinit var tv_special_amt_fan: TextView
    lateinit var linear_distributionmargindiscount: LinearLayout
    lateinit var tv_marginvalue: TextView
    lateinit var tv_margin_amt: TextView
    lateinit var linear_extradiscount: LinearLayout
    lateinit var tv_extravalue: TextView
    lateinit var tv_extra_amt: TextView
    lateinit var linear_cashdiscount: LinearLayout
    lateinit var tv_cashvalue: TextView
    lateinit var tv_cash_amt: TextView
    lateinit var linear_total_dis: LinearLayout
    lateinit var tv_disvalue: TextView
    lateinit var tv_dis_amt: TextView
    lateinit var cardtransport: CardView

    lateinit var linear_ageri_dis: LinearLayout
    lateinit var tv_agri_disvalue: TextView
    lateinit var tv_agri_dis_amt: TextView
    lateinit var linear_total_ageri_dis: LinearLayout
    lateinit var tv_total_agri_disvalue: TextView
    lateinit var tv_total_agri_dis_amt: TextView
    lateinit var edtadvance_pay: AutoCompleteTextView

    lateinit var cardfully_order_dispatch : CardView

    var customer_type_id = ""

    var buyer_name = ""
    var seller_name = ""
    var order_date = ""
    var createdbyname = ""
    var order_no = ""
    var order_no_Send = ""
    var order_id = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_details)

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
        tvOrderdate = findViewById(R.id.tvOrderdate)
        tvOrdercreatedby = findViewById(R.id.tvOrdercreatedby)
        cardtransport = findViewById(R.id.cardtransport)
        cardcancel = findViewById(R.id.cardcancel)
        edtadvance_pay = findViewById(R.id.edtadvance_pay)

        recyclerView = findViewById(R.id.recyclerView)
        edtDistributor = findViewById(R.id.edtDistributor)
        cardBack = findViewById(R.id.cardBack)

        linear_ebd = findViewById(R.id.linear_ebd)
        tv_ebd_amt = findViewById(R.id.tv_ebd_amt)
        tv_schmedis_value = findViewById(R.id.tv_schmedis_value)

        linear_cluster = findViewById(R.id.linear_cluster)
        tv_clusterfilter = findViewById(R.id.tv_clusterfilter)
        tv_cluster_amt = findViewById(R.id.tv_cluster_amt)

        linear_cash_pump = findViewById(R.id.linear_cash_pump)
        tv_cashfilter_pump = findViewById(R.id.tv_cashfilter_pump)
        tv_cash_amt_pump = findViewById(R.id.tv_cash_amt_pump)

        linear_total_pump = findViewById(R.id.linear_total_pump)
        edt_totalfilter_pump = findViewById(R.id.edt_totalfilter_pump)
        tv_total_amt_pump = findViewById(R.id.tv_total_amt_pump)

        linear_dealer = findViewById(R.id.linear_dealer)
        edt_dealerfilter = findViewById(R.id.edt_dealerfilter)
        tv_dealer_amt = findViewById(R.id.tv_dealer_amt)

        linear_distributor = findViewById(R.id.linear_distributor)
        edt_distributorfilter = findViewById(R.id.edt_distributorfilter)
        tv_distributor_amt = findViewById(R.id.tv_distributor_amt)

        linear_Frieght = findViewById(R.id.linear_Frieght)
        edt_Frieghtfilter = findViewById(R.id.edt_Frieghtfilter)
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

        linear_ebd_dis = findViewById(R.id.linear_ebd_dis)
        tv_ebdfilter = findViewById(R.id.tv_ebdfilter)
        tv_ebd_dis_amt = findViewById(R.id.tv_ebd_dis_amt)

        linear_special = findViewById(R.id.linear_special)
        edt_specialfilter = findViewById(R.id.edt_specialfilter)
        tv_special_amt = findViewById(R.id.tv_special_amt)

        linear_Doddiscount = findViewById(R.id.linear_Doddiscount)
        tv_dodvalue = findViewById(R.id.tv_dodvalue)
        tv_dod_amt = findViewById(R.id.tv_dod_amt)

        linear_Specialdiscount = findViewById(R.id.linear_Specialdiscount)
        tv_specialvalue = findViewById(R.id.tv_specialvalue)
        tv_special_amt_fan = findViewById(R.id.tv_special_amt_fan)

        linear_distributionmargindiscount = findViewById(R.id.linear_distributionmargindiscount)
        tv_marginvalue = findViewById(R.id.tv_marginvalue)
        tv_margin_amt = findViewById(R.id.tv_margin_amt)

        linear_extradiscount = findViewById(R.id.linear_extradiscount)
        tv_extravalue = findViewById(R.id.tv_extravalue)
        tv_extra_amt = findViewById(R.id.tv_extra_amt)

        linear_cashdiscount = findViewById(R.id.linear_cashdiscount)
        tv_cashvalue = findViewById(R.id.tv_cashvalue)
        tv_cash_amt = findViewById(R.id.tv_cash_amt)

        linear_total_dis = findViewById(R.id.linear_total_dis)
        tv_disvalue = findViewById(R.id.tv_disvalue)
        tv_dis_amt = findViewById(R.id.tv_dis_amt)

        linear_ageri_dis = findViewById(R.id.linear_ageri_dis)
        tv_agri_disvalue = findViewById(R.id.tv_agri_disvalue)
        tv_agri_dis_amt = findViewById(R.id.tv_agri_dis_amt)

        linear_total_ageri_dis = findViewById(R.id.linear_total_ageri_dis)
        tv_total_agri_disvalue = findViewById(R.id.tv_total_agri_disvalue)
        tv_total_agri_dis_amt = findViewById(R.id.tv_total_agri_dis_amt)


        cardSubmit = findViewById(R.id.cardSubmit)
        cardfully_order_dispatch = findViewById(R.id.cardfully_order_dispatch)

        recyclerView.layoutManager = GridLayoutManager(this, 1)

        cardBack.setOnClickListener {
            onBackPressed()
        }
        cardcancel.setOnClickListener {
            callcancelorder()
        }

        order_no_Send = intent.getIntExtra("id", 0).toString()

        cardfully_order_dispatch.setOnClickListener {
            val intent = Intent( this, PartiallyDispatchActivity::class.java)
            intent.putExtra("id",order_no_Send)
            startActivity(intent)
        }

        tvOrderNo.setText("Order No. : "+intent.getStringExtra("no"))

        cardSubmit.setOnClickListener {
            getlink(intent.getIntExtra("id", 0).toString(),customer_type_id)
        }

        getOrderDetails()
    }

    private lateinit var alertdialog : AlertDialog

    @SuppressLint("MissingInflatedId")
    private fun showdisptachpopup() {
        val builder = AlertDialog.Builder(this@OrderDetailsActivity)

        val inflater = this@OrderDetailsActivity.layoutInflater
        val view = inflater.inflate(R.layout.popup_disptach_layout, null)
        builder.setCancelable(false)

        val tvOrderNo: TextView = view.findViewById(R.id.tvOrderNo)
        val tvBuyer: TextView = view.findViewById(R.id.tvBuyer)
        val tvSeller: TextView = view.findViewById(R.id.tvSeller)
        val tvOrderdate: TextView = view.findViewById(R.id.tvOrderdate)
        val tvOrdercreatedby: TextView = view.findViewById(R.id.tvOrdercreatedby)

        val textinvoicedate: TextView = view.findViewById(R.id.textinvoicedate)
        val dispatchdate: TextView = view.findViewById(R.id.dispatchdate)
        val edt_invoice_no: EditText = view.findViewById(R.id.edt_invoice_no)
        val edt_transport_detail: EditText = view.findViewById(R.id.edt_transport_detail)
        val edt_lr_no: EditText = view.findViewById(R.id.edt_lr_no)
        val cardbtn_submit: CardView = view.findViewById(R.id.cardbtn_submit)
        val img_close: ImageView = view.findViewById(R.id.img_close)

        tvOrderNo.text = "Order No. : $order_no"
        tvBuyer.text = "Buyer Name :- $buyer_name"
        tvSeller.text = "Seller Name :- $seller_name"
        tvOrderdate.text = "Order Date :- $order_date"
        tvOrdercreatedby.text = "Created By :- $createdbyname"

        textinvoicedate.setOnClickListener {
            Utilities.datePicker2(textinvoicedate,this@OrderDetailsActivity)
        }
        dispatchdate.setOnClickListener {
            Utilities.datePickerFuture2(dispatchdate,this@OrderDetailsActivity)
        }
        cardbtn_submit.setOnClickListener {
            var invoice_date = Utilities.convertDateFormat(textinvoicedate.text.toString())
            var dispatch_date = Utilities.convertDateFormat(dispatchdate.text.toString())
            if (order_id.isEmpty()){
                response_message("Someting went wrong")
            }else if (invoice_date.isEmpty()){
                response_message("Please select invoice date")
            }else if (edt_invoice_no.text.isNullOrEmpty()){
                response_message("Please enter invoice number")
            }else if (dispatch_date.isEmpty()){
                response_message("Please select dispatch date")
            }else{
                submitfullydispatch(order_id,invoice_date,edt_transport_detail.text.toString(),edt_lr_no.text.toString(),
                dispatch_date,edt_invoice_no.text.toString())
            }

        }
        img_close.setOnClickListener {
            alertDialog.dismiss()
        }


        builder.setView(view)

        alertdialog = builder.create()
        alertdialog.show()
    }

    private fun response_message(message: String) {
        Toast.makeText(this@OrderDetailsActivity,message,Toast.LENGTH_SHORT).show()

    }


    private fun submitfullydispatch(
        orderId: String,
        invoice_date: String,
        transport_detail: String,
        lr_no: String,
        dispatch_date: String,
        invoice_no: String
    ) {
        if (!Utilities.isOnline(this@OrderDetailsActivity)) {
            return
        }
        var dialog = DialogClass.progressDialog(this@OrderDetailsActivity)
        val queryParams = HashMap<String, String>()
        queryParams["order_id"] = orderId
        queryParams["invoice_date"] = invoice_date
        queryParams["invoice_no"] = invoice_no
        queryParams["transport_name"] = transport_detail
        queryParams["lr_no"] = lr_no
        queryParams["dispatch_date"] = dispatch_date

        println("Datata=="+orderId+"<<"+invoice_date+"<<"+invoice_no+"<<"+transport_detail+
                "<<"+lr_no+"<<"+dispatch_date)

        ApiClient.fullydispatchorder(StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this@OrderDetailsActivity).toString(), queryParams,
            object : APIResultLitener<AttendanceSubmitModel> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onAPIResult(response: Response<AttendanceSubmitModel>?, errorMessage: String?) {
                    if (response != null && errorMessage == null) {
                        if (response.code() == 200) {
                            dialog.dismiss()
                            Toast.makeText(this@OrderDetailsActivity,response.body()!!.message,Toast.LENGTH_LONG).show()
                            val intent = Intent(this@OrderDetailsActivity, MainActivity::class.java)
                            intent.putExtra("CustomerVisit", "customer_order_his")
                            startActivity(intent)
                        } else {
                            dialog.dismiss()
                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())
                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    this@OrderDetailsActivity, false
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            })
    }


    private fun callcancelorder() {
        if (!Utilities.isOnline(this@OrderDetailsActivity)) {
            return
        }
        var dialog = DialogClass.progressDialog(this@OrderDetailsActivity)
        val queryParams = HashMap<String, String>()
        queryParams["order_id"] = intent.getIntExtra("id", 0).toString()

        ApiClient.callcancelorder(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this@OrderDetailsActivity).toString(),
            queryParams,
            object : APIResultLitener<AttendanceSubmitModel> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onAPIResult(response: Response<AttendanceSubmitModel>?, errorMessage: String?) {
                    if (response != null && errorMessage == null) {
                        if (response.code() == 200) {
                            dialog.dismiss()
                            Toast.makeText(this@OrderDetailsActivity,response.body()!!.message,Toast.LENGTH_LONG).show()
                            val intent = Intent(this@OrderDetailsActivity, MainActivity::class.java)
                            intent.putExtra("CustomerVisit", "customer_order_his")
                            startActivity(intent)
                        } else {
                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    this@OrderDetailsActivity, false
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            })
    }


    private lateinit var alertDialog: AlertDialog
    @SuppressLint("MissingInflatedId")
    private fun showPopupDialog(data: OrderDetailsModel.Data) {

        val builder = AlertDialog.Builder(this@OrderDetailsActivity)
        val inflater = this@OrderDetailsActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.transport_detail_popup_layout, null)
        builder.setCancelable(false)


        val img_close: ImageView = view.findViewById(R.id.img_close)
        val tv_invoice_no: TextView = view.findViewById(R.id.tv_invoice_no)
        val tv_invoice_date: TextView = view.findViewById(R.id.tv_invoice_date)
        val tv_lr_no: TextView = view.findViewById(R.id.tv_lr_no)
        val tv_dis_date: TextView = view.findViewById(R.id.tv_dis_date)
        val tv_trans_detail: TextView = view.findViewById(R.id.tv_trans_detail)

        tv_invoice_no.text = data.invoice_no
        tv_invoice_date.text = data.invoice_date
        tv_lr_no.text = data.lr_no
        tv_dis_date.text = data.dispatch_date
        tv_trans_detail.text = data.transport_name


        img_close.setOnClickListener {
            alertDialog.dismiss()
        }

        builder.setView(view)

        alertDialog = builder.create()
        alertDialog.show()
    }


    private fun getlink(orderid: String, customer_type_id: String) {

        if (!Utilities.isOnline(this)) {
            return
        }
        var dialog = DialogClass.progressDialog(this)
        val queryParams = HashMap<String, String>()
        queryParams["order_id"] = orderid
        queryParams["customer_type_id"] = customer_type_id

        ApiClient.getorderpdf(
            StaticSharedpreference.getInfo(
                Constant.ACCESS_TOKEN,
                this@OrderDetailsActivity
            ).toString(), queryParams, object : APIResultLitener<OrderPdfModel> {
                override fun onAPIResult(
                    response: Response<OrderPdfModel>?,
                    errorMessage: String?
                ) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {
                            var link = response.body()!!.data!!.pdfUrl.toString()
                            println("linklink="+link)
                            downloadPdf(link)
                        }
                    } else {
                        Toast.makeText(
                            this@OrderDetailsActivity,
                            resources.getString(R.string.poor_connection),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        )

    }

    private fun downloadPdf( pdfUrl: String?) {
        val request = DownloadManager.Request(Uri.parse(pdfUrl))
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Order.pdf")
        val downloadManager = this@OrderDetailsActivity.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)
        Toast.makeText(this@OrderDetailsActivity,"Successfully downloaded",Toast.LENGTH_SHORT).show()
    }



    private fun getOrderDetails() {
        if (!Utilities.isOnline(this)) {
            return
        }
        var dialog = DialogClass.progressDialog(this)
        val queryParams = HashMap<String, String>()
        queryParams["order_id"] = intent.getIntExtra("id", 0).toString()
        println("IDDORRRR=="+intent.getIntExtra("id", 0).toString())
        ApiClient.getOrderDetails(StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this@OrderDetailsActivity).toString(), queryParams, object : APIResultLitener<OrderDetailsModel> {
                override fun onAPIResult(response: Response<OrderDetailsModel>?, errorMessage: String?) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {
                            tvGSTTotal.text = response.body()!!.data!!.totalGst
                            tvAmount.text = response.body()!!.data!!.subTotal
                            tvTotal.text = response.body()!!.data!!.grandTotal
                            tvBuyer.text = "Buyer :- "+response.body()!!.data!!.buyer_name +" || "+response.body()!!.data!!.buyers!!.mobile.toString()
                            tvSeller.text ="Seller :- "+response.body()!!.data!!.seller_name +" || "+response.body()!!.data!!.sellers!!.mobile.toString()

                            buyer_name = response.body()!!.data!!.buyer_name.toString()
                            seller_name = response.body()!!.data!!.seller_name.toString()
                            order_date = response.body()!!.data!!.orderDate.toString()
                            createdbyname = response.body()!!.data!!.createdbyname!!.name.toString()
                            order_no = response.body()!!.data!!.orderno.toString()
                            order_no_Send = response.body()!!.data!!.id.toString()
                            order_id = response.body()!!.data!!.id.toString()

                            tvBuyeradd.text = "Buyer Add. :- "+response.body()!!.data!!.buyer_address!!.address1.toString()
                            tvSelleraddd.text = "Seller Add. :- "+response.body()!!.data!!.seller_address!!.address1.toString()
                            customer_type_id =response.body()!!.data!!.buyers!!.customertypes!!.id.toString()
                            tvOrdercreatedby.text = "Created by :- "+response.body()!!.data!!.createdbyname!!.name.toString()
                            tvOrderdate.text = "Order Date :- "+response.body()!!.data!!.orderDate.toString()


                            if (response.body()!!.data!!.status_id.equals("1") || response.body()!!.data!!.status_id.equals("2")){
                                cardtransport.visibility = View.VISIBLE
                            }else{
                                cardtransport.visibility = View.GONE
                            }
                            if (response.body()!!.data!!.status_id.equals("0")){
                                cardcancel.visibility = View.VISIBLE
                            }else{
                                cardcancel.visibility = View.GONE
                            }
                            if (response.body()!!.data!!.status_id.equals("0") || response.body()!!.data!!.status_id.equals("2")){
                                if (response.body()!!.data!!.buyer_type!!.equals("Retailer")){
                                    cardfully_order_dispatch.visibility = View.VISIBLE
                                }else{
                                    cardfully_order_dispatch.visibility = View.GONE
                                }
                            }

                            cardtransport.setOnClickListener {
                                showPopupDialog(response.body()!!.data!!)
                            }

                            val orderDetails =   response.body()!!.data!!.orderdetails
                            val orderAddToCardAdapter = OrderDetailsAdapter(this@OrderDetailsActivity,orderDetails,response.body()!!.data!!.buyer_type
                                    ,response.body()!!.data!!.product_cat_id)
                            recyclerView.adapter = orderAddToCardAdapter

                            if (response.body()?.data?.product_cat_id!!.equals(1)){
                                if (response.body()?.data?.schme_amount?.toString()?.isNotEmpty() == true) {
                                    linear_ebd.visibility = View.VISIBLE
                                    tv_ebd_amt.text = response.body()?.data?.schme_amount?.toString()
                                    tv_schmedis_value.text = response.body()?.data?.schme_val?.toString()
                                }
                                if (response.body()?.data?.ebd_amount?.toString()?.isNotEmpty() == true) {
                                    linear_ebd_dis.visibility = View.VISIBLE
                                    tv_ebdfilter.text = response.body()!!.data!!.ebd_discount!!.toString()
                                    tv_ebd_dis_amt.text = response.body()?.data?.ebd_amount?.toString()
                                }
                                if (response.body()?.data?.special_amount?.toString()?.isNotEmpty() == true) {
                                    linear_special.visibility = View.VISIBLE
                                    edt_specialfilter.text = response.body()!!.data!!.special_discount!!.toString()
                                    tv_special_amt.text = response.body()?.data?.special_amount?.toString()
                                }
                                if (response.body()?.data?.cluster_amount?.toString()?.isNotEmpty() == true) {
                                    linear_cluster.visibility = View.VISIBLE
                                    tv_clusterfilter.text = response.body()!!.data!!.cluster_discount!!.toString()
                                    tv_cluster_amt.text = response.body()!!.data!!.cluster_amount!!.toString()
                                }
                                if (response.body()?.data?.cash_amount?.toString()?.isNotEmpty() == true) {
                                    linear_cash_pump.visibility = View.VISIBLE
                                    tv_cashfilter_pump.text = response.body()!!.data!!.cash_discount!!.toString()
                                    tv_cash_amt_pump.text = response.body()!!.data!!.cash_amount!!.toString()
                                }
                                if (response.body()?.data?.deal_amount?.toString()?.isNotEmpty() == true) {
                                    linear_dealer.visibility = View.VISIBLE
                                    edt_dealerfilter.text = response.body()!!.data!!.deal_discount!!.toString()
                                    tv_dealer_amt.text = response.body()!!.data!!.deal_amount!!.toString()
                                }

                                if (response.body()?.data?.total_amount?.toString()?.isNotEmpty() == true) {
                                    linear_total_pump.visibility = View.VISIBLE
                                    edt_totalfilter_pump.text = response.body()!!.data!!.total_discount!!.toString()
                                    tv_total_amt_pump.text = response.body()!!.data!!.total_amount!!.toString()
                                }
                                if (response.body()?.data?.distributor_amount?.toString()?.isNotEmpty() == true) {
                                    linear_distributor.visibility = View.VISIBLE
                                    edt_distributorfilter.text = response.body()!!.data!!.distributor_discount!!.toString()
                                    tv_distributor_amt.text = response.body()!!.data!!.distributor_amount!!.toString()
                                }
                                if (response.body()?.data?.frieght_amount?.toString()?.isNotEmpty() == true) {
                                    linear_Frieght.visibility = View.VISIBLE
                                    edt_Frieghtfilter.text = response.body()!!.data!!.frieght_discount!!.toString()
                                    tv_Frieght_amt.text = response.body()!!.data!!.frieght_amount!!.toString()
                                }
                            }

                            else if (response.body()?.data?.product_cat_id!!.equals(2)){
                                if (response.body()?.data?.dod_discount?.toString()?.isNotEmpty() == true) {
                                    linear_Doddiscount.visibility = View.VISIBLE
                                    tv_dodvalue.text = response.body()?.data?.dod_discount?.toString()
                                    tv_dod_amt.text = response.body()?.data?.dod_discount_amount?.toString()
                                }
                                if (response.body()?.data?.special_distribution_discount?.toString()?.isNotEmpty() == true) {
                                    linear_Specialdiscount.visibility = View.VISIBLE
                                    tv_specialvalue.text = response.body()?.data?.special_distribution_discount?.toString()
                                    tv_special_amt_fan.text = response.body()?.data?.special_distribution_discount_amount?.toString()
                                }
                                if (response.body()?.data?.distribution_margin_discount?.toString()?.isNotEmpty() == true) {
                                    linear_distributionmargindiscount.visibility = View.VISIBLE
                                    tv_marginvalue.text = response.body()?.data?.distribution_margin_discount?.toString()
                                    tv_margin_amt.text = response.body()?.data?.distribution_margin_discount_amount?.toString()
                                }
                                if (response.body()?.data?.fan_extra_discount?.toString()?.isNotEmpty() == true) {
                                    linear_extradiscount.visibility = View.VISIBLE
                                    tv_extravalue.text = response.body()?.data?.fan_extra_discount?.toString()
                                    tv_extra_amt.text = response.body()?.data?.fan_extra_discount_amount?.toString()
                                }
                                if (response.body()?.data?.cash_discount?.toString()?.isNotEmpty() == true) {
                                    linear_cashdiscount.visibility = View.VISIBLE
                                    tv_cashvalue.text = response.body()?.data?.cash_discount?.toString()
                                    tv_cash_amt.text = response.body()?.data?.cash_amount?.toString()
                                }
                                if (response.body()?.data?.total_fan_discount?.toString()?.isNotEmpty() == true) {
                                    linear_total_dis.visibility = View.VISIBLE
                                    tv_disvalue.text = response.body()?.data?.total_fan_discount?.toString()
                                    tv_dis_amt.text = response.body()?.data?.total_fan_discount_amount?.toString()
                                }
                            }

                            else if (response.body()?.data?.product_cat_id!!.equals(4)){
                                if (response.body()?.data?.agri_standard_discount?.toString()?.isNotEmpty() == true) {
                                    linear_ageri_dis.visibility = View.VISIBLE
                                    tv_agri_disvalue.text = response.body()?.data?.agri_standard_discount?.toString()
                                    tv_agri_dis_amt.text = response.body()?.data?.agri_standard_discount_amount?.toString()
                                }
                                if (response.body()?.data?.total_discount?.toString()?.isNotEmpty() == true) {
                                    linear_total_ageri_dis.visibility = View.VISIBLE
                                    tv_total_agri_disvalue.text = response.body()?.data?.total_discount?.toString()
                                    tv_total_agri_dis_amt.text = response.body()?.data?.total_amount?.toString()
                                }
                                if (response.body()?.data?.advance?.toString()?.isNotEmpty() == true){
                                    edtadvance_pay.visibility = View.VISIBLE
                                    edtadvance_pay.setText("Advance Payment : "+response.body()?.data?.advance.toString())
                                }
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
                                tv_remark.text = response.body()!!.data!!.order_remark!!.toString()
                            }
                        }
                    } else {
                        println("errrroorro=="+response!!.body()!!.message)
                        println("errrroorro=="+errorMessage)
                        Toast.makeText(this@OrderDetailsActivity, resources.getString(R.string.poor_connection), Toast.LENGTH_LONG).show()
                    }
                }
            }
        )
    }
}