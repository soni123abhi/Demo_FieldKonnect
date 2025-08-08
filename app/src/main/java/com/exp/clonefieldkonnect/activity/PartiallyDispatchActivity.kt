package com.exp.clonefieldkonnect.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.adapter.OrderDetailsAdapterPartially
import com.exp.clonefieldkonnect.connection.APIResultLitener
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.helper.Constant
import com.exp.clonefieldkonnect.helper.DialogClass
import com.exp.clonefieldkonnect.helper.StaticSharedpreference
import com.exp.clonefieldkonnect.model.OrderDetailsModel
import com.exp.clonefieldkonnect.model.ParticallyorderDetailsRequestModel
import com.exp.import.Utilities
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Response
import java.text.DecimalFormat
import java.util.HashMap

class PartiallyDispatchActivity : AppCompatActivity(),OrderDetailsAdapterPartially.OnEmailClick {
    lateinit var recyclerView: RecyclerView
    lateinit var edtDistributor: AutoCompleteTextView
    lateinit var cardfully_dispatch: CardView
    lateinit var card_partically_dispatch: CardView
    lateinit var cardBack: CardView
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
    lateinit var linear_12gst: LinearLayout
    lateinit var linear_18gst: LinearLayout
    lateinit var linear_28gst: LinearLayout
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


    companion object {
        lateinit var tvTotal: TextView
        lateinit var tvGSTTotal: TextView
        lateinit var tvAmount: TextView
        lateinit var tv_5gstamt: TextView
        lateinit var tv_12gstamt: TextView
        lateinit var tv_18gstamt: TextView
        lateinit var tv_28gstamt: TextView
//        lateinit var tv_ebd_amt: TextView
//        lateinit var tv_stdfilter: TextView
//        lateinit var tv_cluster_amt: TextView
//        lateinit var tv_clusterfilter: TextView
//        lateinit var tv_dealerfilter: TextView
//        lateinit var tv_dealer_amt: TextView
//        lateinit var tv_distributorfilter: TextView
//        lateinit var tv_distributor_amt: TextView
//        lateinit var tv_Frieghtfilter: TextView
//        lateinit var tv_Frieght_amt: TextView
//        lateinit var tv_Specialfilter: TextView
//        lateinit var tv_Special_amt: TextView
//        lateinit var tv_ebdfilter: TextView
//        lateinit var tv_ebd_dis_amt: TextView
//        lateinit var tv_dodvalue: TextView
//        lateinit var tv_dod_amt: TextView
//        lateinit var tv_specialvalue: TextView
//        lateinit var tv_special_amt: TextView
//        lateinit var tv_marginvalue: TextView
//        lateinit var tv_margin_amt: TextView
//        lateinit var tv_extravalue: TextView
//        lateinit var tv_extra_amt: TextView
//        lateinit var tv_cashvalue: TextView
//        lateinit var tv_cash_amt: TextView
//        lateinit var tv_disvalue: TextView
//        lateinit var tv_dis_amt: TextView
//        lateinit var tv_cashvalue_pump: TextView
//        lateinit var tv_cash_amt_pump: TextView
//        lateinit var tv_disvalue_pump: TextView
//        lateinit var tv_dis_amt_pump: TextView
//        lateinit var tv_std_agrivalue: TextView
//        lateinit var tv_agri_dis_amt: TextView
//        lateinit var tv_agri_totalvalue: TextView
//        lateinit var tv_agri_total_amt: TextView
//        lateinit var tv_remaining_pay: TextView
//        lateinit var edt_advance_pay: EditText
    }


    var customer_type_id = ""

    var buyer_name = ""
    var seller_name = ""
    var order_date = ""
    var createdbyname = ""
    var order_no = ""
    var order_id = ""
    var order_id_new = ""

    var Schme_Dis: String = ""
    var Ebd_Dis:String = ""
    var Mou_Dis:String = ""
    var Special_Dis:String = ""
    var Frieght_Dis:String = ""
    var Cluster_Dis:String = ""
    var Cash_Dis:String = ""
    var Delear_Dis  :String = ""
    var customerId  :String = ""
    var diss_id  :String = ""
    var order_staus  :String = ""
    var total_order_qty  : Int = 0
    var total_order_qty_afteredit  : Int = 0
    private var orderDetails: List<OrderDetailsModel.Data.Orderdetail>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_partially_dispatch)
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


        cardfully_dispatch = findViewById(R.id.cardfully_dispatch)
        card_partically_dispatch = findViewById(R.id.card_partically_dispatch)

        recyclerView.layoutManager = GridLayoutManager(this, 1)

        cardBack.setOnClickListener {
            onBackPressed()
        }


        println("Order No. : ${intent.getStringExtra("id")}")
        order_id_new = intent.getStringExtra("id").toString()
        tvOrderNo.setText("Order No. : "+intent.getStringExtra("id"))


        card_partically_dispatch.setOnClickListener {
            total_order_qty_afteredit = 0
            for (value in orderDetails!!) {
                total_order_qty_afteredit += value.quantity.toString().toInt()
            }
            println("QQQQ=="+total_order_qty+"<<"+total_order_qty_afteredit)

            if (total_order_qty != total_order_qty_afteredit && total_order_qty_afteredit != 0){
                order_staus = "2"
                showdisptachpopup(order_staus)
            }else{
                response_message("Please fully dispatch this order")
            }
        }
        cardfully_dispatch.setOnClickListener {
            total_order_qty_afteredit = 0
            for (value in orderDetails!!) {
                total_order_qty_afteredit += value.quantity.toString().toInt()
            }
            println("QQQQ=="+total_order_qty+"<<"+total_order_qty_afteredit)

            if (total_order_qty == total_order_qty_afteredit && total_order_qty_afteredit != 0){
                order_staus = "1"
                showdisptachpopup(order_staus)
             }else{
                response_message("Please partically dispatch this order")
            }


        }

        getOrderDetails(order_id_new)
    }

    private lateinit var alertdialog : AlertDialog
    @SuppressLint("MissingInflatedId")
    private fun showdisptachpopup(order_staus: String) {
        val builder = AlertDialog.Builder(this@PartiallyDispatchActivity)

        val inflater = this@PartiallyDispatchActivity.layoutInflater
        val view = inflater.inflate(R.layout.popup_disptach_layout, null)
        builder.setCancelable(false)

        val textview_head: TextView = view.findViewById(R.id.textview_head)
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
        if (order_staus.equals("2")){
            textview_head.text = "Partically Dispatch Order"
        }else{
            textview_head.text = "Fully Dispatch Order"
        }

        tvOrderNo.text = "Order No. : $order_no"
        tvBuyer.text = "Buyer Name :- $buyer_name"
        tvSeller.text = "Seller Name :- $seller_name"
        tvOrderdate.text = "Order Date :- $order_date"
        tvOrdercreatedby.text = "Created By :- $createdbyname"

        textinvoicedate.setOnClickListener {
            Utilities.datePicker2(textinvoicedate,this@PartiallyDispatchActivity)
        }
        dispatchdate.setOnClickListener {
            Utilities.datePicker(dispatchdate,this@PartiallyDispatchActivity)
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
                updateorder(order_id,invoice_date,edt_transport_detail.text.toString(),edt_lr_no.text.toString(),
                    dispatch_date,edt_invoice_no.text.toString(),order_staus)
            }

        }
        img_close.setOnClickListener {
            alertdialog.dismiss()
        }

        builder.setView(view)

        alertdialog = builder.create()
        alertdialog.show()
    }

    private fun updateorder(
        orderId: String,
        invoiceDate: String,
        transport_detail: String,
        lr_no: String,
        dispatch_date: String,
        invoice_no: String,
        order_staus: String
    ) {
        if (!Utilities.isOnline(this)) {
            return
        }
        var dialog = DialogClass.progressDialog(this)

        var orderDetailsRequestModel = ParticallyorderDetailsRequestModel()

        var requestArrLIst: ArrayList<ParticallyorderDetailsRequestModel.Datum> = arrayListOf()


        orderDetailsRequestModel.orderId = orderId
        orderDetailsRequestModel.invoiceDate = invoiceDate
        orderDetailsRequestModel.transport_detail = transport_detail
        orderDetailsRequestModel.lr_no = lr_no
        orderDetailsRequestModel.dispatch_date = dispatch_date
        orderDetailsRequestModel.invoice_no = invoice_no
        orderDetailsRequestModel.buyer_id = customerId
        orderDetailsRequestModel.seller_id = diss_id
        orderDetailsRequestModel.status_id = order_staus

        /*if (intent.getStringExtra("checkin") == "n") {
            orderDetailsRequestModel.buyer_id = customerId
            if (customertype.equals("Retailer") || customertype.equals("Service Center")){
                orderDetailsRequestModel.seller_id = diss_id
            }else{
                orderDetailsRequestModel.seller_id = customerId
            }

        } else {
            orderDetailsRequestModel.buyer_id = StaticSharedpreference.getInfo(Constant.CHECKIN_CUST_ID, this).toString()

            if (customertypein.equals("Retailer") || customertypein.equals("Service Center")){
                orderDetailsRequestModel.seller_id = diss_id
            }else{
                orderDetailsRequestModel.seller_id = StaticSharedpreference.getInfo(Constant.CHECKIN_CUST_ID, this).toString()
            }

        }*/

        orderDetailsRequestModel.total_gst = tvGSTTotal.text.toString()
        orderDetailsRequestModel.sub_total = tvAmount.text.toString()
        orderDetailsRequestModel.grand_total = tvTotal.text.toString()
        orderDetailsRequestModel.beatScheduleId = null
        orderDetailsRequestModel.remark = tv_remark.text.toString()
        orderDetailsRequestModel.gst5_amt = tv_5gstamt.text.toString()
        orderDetailsRequestModel.gst12_amt = tv_12gstamt.text.toString()
        orderDetailsRequestModel.gst18_amt = tv_18gstamt.text.toString()
        orderDetailsRequestModel.gst28_amt = tv_28gstamt.text.toString()


/*        if (ordercustomertype.equals("Dealer") || ordercustomertype.equals("Distributor")){
            orderDetailsRequestModel.schme_amount = PartiallyDispatchActivity.tv_ebd_amt.text.toString()
            orderDetailsRequestModel.schme_val = PartiallyDispatchActivity.tv_stdfilter.text.toString()
            orderDetailsRequestModel.ebd_discount = PartiallyDispatchActivity.tv_ebdfilter.text.toString()
            orderDetailsRequestModel.ebd_amount = PartiallyDispatchActivity.tv_ebd_dis_amt.text.toString()
            orderDetailsRequestModel.special_discount = PartiallyDispatchActivity.tv_Specialfilter.text.toString()
            orderDetailsRequestModel.special_amount = PartiallyDispatchActivity.tv_Special_amt.text.toString()
            orderDetailsRequestModel.cluster_discount = PartiallyDispatchActivity.tv_clusterfilter.text.toString()
            orderDetailsRequestModel.cluster_amount = PartiallyDispatchActivity.tv_cluster_amt.text.toString()
            orderDetailsRequestModel.deal_discount = PartiallyDispatchActivity.tv_dealerfilter.text.toString()
            orderDetailsRequestModel.deal_amount = PartiallyDispatchActivity.tv_dealer_amt.text.toString()
            orderDetailsRequestModel.distributor_discount = PartiallyDispatchActivity.tv_distributorfilter.text.toString()
            orderDetailsRequestModel.distributor_amount = PartiallyDispatchActivity.tv_distributor_amt.text.toString()
            orderDetailsRequestModel.frieght_discount = PartiallyDispatchActivity.tv_Frieghtfilter.text.toString()
            orderDetailsRequestModel.frieght_amount = PartiallyDispatchActivity.tv_Frieght_amt.text.toString()


            if (PartiallyDispatchActivity.tv_cashvalue_pump.text.isNotEmpty()){
                orderDetailsRequestModel.cash_discount = PartiallyDispatchActivity.tv_cashvalue_pump.text.toString()
                orderDetailsRequestModel.cash_amount = PartiallyDispatchActivity.tv_cash_amt_pump.text.toString()
            }else{
                orderDetailsRequestModel.cash_discount = PartiallyDispatchActivity.tv_cashvalue.text.toString()
                orderDetailsRequestModel.cash_amount = PartiallyDispatchActivity.tv_cash_amt.text.toString()
            }
            if (PartiallyDispatchActivity.tv_agri_totalvalue.text.isNotEmpty()){
                orderDetailsRequestModel.total_discount = PartiallyDispatchActivity.tv_agri_totalvalue.text.toString()
                orderDetailsRequestModel.total_amount = PartiallyDispatchActivity.tv_agri_total_amt.text.toString()
                orderDetailsRequestModel.advance = PartiallyDispatchActivity.edt_advance_pay.text.toString()
            }else{
                orderDetailsRequestModel.total_discount = PartiallyDispatchActivity.tv_disvalue_pump.text.toString()
                orderDetailsRequestModel.total_amount = PartiallyDispatchActivity.tv_dis_amt_pump.text.toString()
            }


            orderDetailsRequestModel.dod_discount = PartiallyDispatchActivity.tv_dodvalue.text.toString()
            orderDetailsRequestModel.dod_discount_amount = PartiallyDispatchActivity.tv_dod_amt.text.toString()
            orderDetailsRequestModel.special_distribution_discount = PartiallyDispatchActivity.tv_specialvalue.text.toString()
            orderDetailsRequestModel.special_distribution_discount_amount = PartiallyDispatchActivity.tv_special_amt.text.toString()
            orderDetailsRequestModel.distribution_margin_discount = PartiallyDispatchActivity.tv_marginvalue.text.toString()
            orderDetailsRequestModel.distribution_margin_discount_amount = PartiallyDispatchActivity.tv_margin_amt.text.toString()
            orderDetailsRequestModel.fan_extra_discount = PartiallyDispatchActivity.tv_extravalue.text.toString()
            orderDetailsRequestModel.fan_extra_discount_amount = PartiallyDispatchActivity.tv_extra_amt.text.toString()
            orderDetailsRequestModel.total_fan_discount = PartiallyDispatchActivity.tv_disvalue.text.toString()
            orderDetailsRequestModel.total_fan_discount_amount = PartiallyDispatchActivity.tv_dis_amt.text.toString()

            orderDetailsRequestModel.agri_standard_discount = PartiallyDispatchActivity.tv_std_agrivalue.text.toString()
            orderDetailsRequestModel.agri_standard_discount_amount = PartiallyDispatchActivity.tv_agri_dis_amt.text.toString()

            println("orderrrrr=="+ PartiallyDispatchActivity.tv_std_agrivalue.text.toString()+"<<"+ PartiallyDispatchActivity.tv_agri_dis_amt.text.toString()+"<<"+
                    PartiallyDispatchActivity.tv_agri_totalvalue.text.toString()+"<<"+ PartiallyDispatchActivity.tv_agri_total_amt.text.toString())

            val categoryId = ProductActivity.productArrAddToCart.get(0)?.categoryId


            if (ordercustomertype.equals("Distributor") && categoryId!!.equals(4)){
                for (value in ProductActivity.productArrAddToCart) {
                    val requestModel = InsertOrderRequestModel().Datum()
                    requestModel.product_id = value!!.id.toString()
                    requestModel.quantity = value.quantity.toString()
                    requestModel.price = ((value.price?.toDouble() ?: 0.0) / 1.06).toString()
                    requestModel.line_total = (value!!.quantity.toString().toDouble() *  value!!.product_ebd_amount.toString().toDouble()).toString()
                    requestModel.ebd_amount = value.product_ebd_amount.toString()
                    requestArrLIst.add(requestModel)
                }
            }else{
                for (value in ProductActivity.productArrAddToCart) {
                    val requestModel = InsertOrderRequestModel().Datum()
                    requestModel.product_id = value!!.id.toString()
                    requestModel.quantity = value.quantity.toString()
                    requestModel.price = value.price.toString()
                    requestModel.line_total = (value!!.quantity.toString().toDouble() *  value!!.product_ebd_amount.toString().toDouble()).toString()
                    requestModel.ebd_amount = value.product_ebd_amount.toString()
                    requestArrLIst.add(requestModel)
                }
            }



        } else{*/

            orderDetailsRequestModel.schme_amount = null
            orderDetailsRequestModel.ebd_discount = null
            orderDetailsRequestModel.ebd_amount = null
            orderDetailsRequestModel.special_discount = null
            orderDetailsRequestModel.special_amount = null
            orderDetailsRequestModel.cluster_discount = null
            orderDetailsRequestModel.cluster_amount = null
            orderDetailsRequestModel.deal_discount = null
            orderDetailsRequestModel.deal_amount = null
            orderDetailsRequestModel.distributor_discount = null
            orderDetailsRequestModel.distributor_amount = null
            orderDetailsRequestModel.frieght_discount = null
            orderDetailsRequestModel.frieght_amount = null

            orderDetailsRequestModel.dod_discount = null
            orderDetailsRequestModel.dod_discount_amount = null
            orderDetailsRequestModel.special_distribution_discount = null
            orderDetailsRequestModel.special_distribution_discount_amount = null
            orderDetailsRequestModel.distribution_margin_discount = null
            orderDetailsRequestModel.distribution_margin_discount_amount = null
            orderDetailsRequestModel.fan_extra_discount = null
            orderDetailsRequestModel.fan_extra_discount_amount = null
            orderDetailsRequestModel.cash_discount = null
            orderDetailsRequestModel.cash_amount = null
            orderDetailsRequestModel.total_fan_discount = null
            orderDetailsRequestModel.total_fan_discount_amount = null
            orderDetailsRequestModel.total_discount = null
            orderDetailsRequestModel.total_amount = null

            orderDetailsRequestModel.agri_standard_discount = null
            orderDetailsRequestModel.agri_standard_discount_amount = null
            orderDetailsRequestModel.advance = null

        println("ABhiiiiii=vaa=="+orderDetailsRequestModel.gst5_amt+"<<"+orderDetailsRequestModel.gst12_amt+"<<"+
                orderDetailsRequestModel.gst18_amt+"<<"+orderDetailsRequestModel.gst28_amt+"<<"+orderDetailsRequestModel.total_gst+"<<"+
                orderDetailsRequestModel.sub_total+"<<"+orderDetailsRequestModel.grand_total+"<<"+orderDetailsRequestModel.beatScheduleId+"<<"+
                orderDetailsRequestModel.remark+"<<"+orderDetailsRequestModel.buyer_id+"<<"+orderDetailsRequestModel.seller_id)

            for (value in orderDetails!!) {
                val requestModel = ParticallyorderDetailsRequestModel().Datum()
                requestModel.product_id = value!!.productId.toString()
                requestModel.quantity = value.quantity.toString()
                requestModel.price = value.price.toString()
                requestModel.line_total = value.lineTotal.toString()
                requestModel.ebd_amount = value.ebd_amount.toString()
                requestArrLIst.add(requestModel)
                println("ABhiiiiii=="+requestModel.product_id+"<<"+requestModel.quantity+"<<"+requestModel.price+"<<"+requestModel.line_total
                +"<<"+requestModel.ebd_amount)
            }
        orderDetailsRequestModel.orderdetail = requestArrLIst

        ApiClient.particallydispatchorder(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this).toString(),
            orderDetailsRequestModel,
            object : APIResultLitener<JsonObject> {
                override fun onAPIResult(response: Response<JsonObject>?, errorMessage: String?) {

                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {

                            alertDialog("Success", "" + response.body()!!.get("message").asString)
                            dialog.dismiss()

                        } else {
                            dialog.dismiss()

                            var jsonObject: JSONObject? = null
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                println("Errorooror=="+jsonObject.getString("message"))

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    this@PartiallyDispatchActivity,
                                    false
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    } else {
                        dialog.dismiss()

                    }
                }
            },
            this)

        println("Datata=="+orderId+"<<"+invoiceDate+"<<"+invoice_no+"<<"+transport_detail+
                "<<"+lr_no+"<<"+dispatch_date)


    }

    fun alertDialog(
        title: String,
        description: String
    ) {

        val builder = android.app.AlertDialog.Builder(this)

        builder.setMessage(description)
            .setCancelable(false)
            .setPositiveButton("OK") { dialog, id ->

                MainActivity.tabPosition = 0
                startActivity(Intent(this@PartiallyDispatchActivity, MainActivity::class.java))
                finishAffinity()

               /* if (intent.getStringExtra("checkin") == "n") {
                    MainActivity.tabPosition = 0
                    startActivity(Intent(this@PartiallyDispatchActivity, MainActivity::class.java))
                    finishAffinity()
                } else {
//                    ProductActivity.isBack = true
                    finish()
                }*/
            }

        val alert = builder.create()
        alert.setCanceledOnTouchOutside(false)
        alert.setCancelable(false)
        alert.setTitle(title)
        alert.show()
    }

    private fun response_message(s: String) {
        Toast.makeText(this@PartiallyDispatchActivity,s,Toast.LENGTH_SHORT).show()
    }

    private fun getOrderDetails(order_id_new: String) {
        if (!Utilities.isOnline(this)) {
            return
        }
        var dialog = DialogClass.progressDialog(this)
        val queryParams = HashMap<String, String>()
        queryParams["order_id"] = order_id_new
        println("IDDORRRR=="+order_id_new)
        ApiClient.getOrderDetails(StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this@PartiallyDispatchActivity).toString(), queryParams, object :
            APIResultLitener<OrderDetailsModel> {
            override fun onAPIResult(response: Response<OrderDetailsModel>?, errorMessage: String?) {
                dialog.dismiss()
                if (response != null && errorMessage == null) {

                    if (response.code() == 200) {
                        customerId = response.body()!!.data!!.buyerId.toString()
                        diss_id = response.body()!!.data!!.seller_id.toString()
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

                        /*  cardtransport.setOnClickListener {
                              showPopupDialog(response.body()!!.data!!)
                          }*/

                        Schme_Dis = response.body()?.data?.schme_val?.toString()!!
                        Ebd_Dis = response.body()!!.data!!.ebd_discount!!.toString()!!
                        Mou_Dis =  response.body()!!.data!!.distributor_discount!!.toString()!!
                        Special_Dis = response.body()!!.data!!.special_discount!!.toString()!!
                        Frieght_Dis = response.body()!!.data!!.frieght_discount!!.toString()!!
                        Cluster_Dis = response.body()!!.data!!.cluster_discount!!.toString()!!
                        Cash_Dis =   response.body()!!.data!!.cash_discount!!.toString()!!
                        Delear_Dis =  response.body()!!.data!!.deal_discount!!.toString()!!
                        orderDetails =   response.body()!!.data!!.orderdetails
                        val orderAddToCardAdapter = OrderDetailsAdapterPartially(this@PartiallyDispatchActivity,orderDetails,response.body()!!.data!!.buyer_type
                            ,response.body()!!.data!!.product_cat_id, Schme_Dis, Ebd_Dis, Mou_Dis,
                            Special_Dis, Frieght_Dis, Cluster_Dis,
                            Cash_Dis, Delear_Dis, this@PartiallyDispatchActivity,linear_5gst,linear_12gst,linear_18gst,linear_28gst)
                        recyclerView.adapter = orderAddToCardAdapter

                        orderDetails?.forEach { orderDetail ->
                            var qty = orderDetail.quantity.toString().toInt() - orderDetail.shippedQty.toString().toInt()
                            total_order_qty += qty.toInt()
                        }

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
                    Toast.makeText(this@PartiallyDispatchActivity, resources.getString(R.string.poor_connection), Toast.LENGTH_LONG).show()
                }
            }
        }
        )
    }

    override fun onClickEmail(remainingAmount: Double,Schme_Dis_amt: Double,Ebd_Dis_amt: Double,Mou_Dis_amt: Double,Cluster_Dis_amt: Double
                              ,Frieght_Dis: Double,Cash_Dis: Double,Delear_Dis: Double,Special_Dis: Double,totalAmount: Double) {

        tv_ebd_amt.text =DecimalFormat("##.##").format(Schme_Dis_amt.toString().toFloat()).toString()
        tv_ebd_dis_amt.text = DecimalFormat("##.##").format(Ebd_Dis_amt.toString().toFloat()).toString()
        tv_distributor_amt.text =DecimalFormat("##.##").format(Mou_Dis_amt.toString().toFloat()).toString()
        tv_cluster_amt.text =DecimalFormat("##.##").format(Cluster_Dis_amt.toString().toFloat()).toString()
        tv_Frieght_amt.text = DecimalFormat("##.##").format(Frieght_Dis.toString().toFloat()).toString()
        tv_cash_amt_pump.text = DecimalFormat("##.##").format(Cash_Dis.toString().toFloat()).toString()
        tv_dealer_amt.text = DecimalFormat("##.##").format(Delear_Dis.toString().toFloat()).toString()
        tv_special_amt.text = DecimalFormat("##.##").format(Special_Dis.toString().toFloat()).toString()
        tv_total_amt_pump.text =DecimalFormat("##.##").format(totalAmount.toString().toFloat()).toString()
        tvAmount.text =DecimalFormat("##.##").format(remainingAmount.toString().toFloat()).toString()

    }

}