package com.exp.clonefieldkonnect.activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exp.import.Utilities
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.activity.CounterDashboardActivity.Companion.beatScheduleId
import com.exp.clonefieldkonnect.activity.ProductActivity.Companion.isBack
import com.exp.clonefieldkonnect.activity.ProductActivity.Companion.productArrAddToCart
import com.exp.clonefieldkonnect.adapter.CustomerDistributorAdapter
import com.exp.clonefieldkonnect.adapter.CustomerretailerAdapter
import com.exp.clonefieldkonnect.adapter.OrderAddToCardAdapter
import com.exp.clonefieldkonnect.connection.APIResultLitener
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.helper.Constant
import com.exp.clonefieldkonnect.helper.DialogClass
import com.exp.clonefieldkonnect.helper.StaticSharedpreference
import com.exp.clonefieldkonnect.model.DistriutorModel
import com.exp.clonefieldkonnect.model.GetDiscountLimitModel
import com.exp.clonefieldkonnect.model.InsertOrderRequestModel
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Response
import java.util.HashMap

class AddToCartActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var recyclerView: RecyclerView
    lateinit var edtsubmitremark: AutoCompleteTextView
    lateinit var edtDistributor: TextView
    lateinit var edtRetailer: TextView
    lateinit var cardSubmit: CardView
    lateinit var cardBack: CardView
    lateinit var linear_5gst: LinearLayout
    lateinit var linear_12gst: LinearLayout
    lateinit var linear_18gst: LinearLayout
    lateinit var linear_28gst: LinearLayout
    lateinit var linear_ebd: LinearLayout
    lateinit var linear_cluster: LinearLayout
    lateinit var linear_dealer: LinearLayout
    lateinit var linear_cashdiscount_pump: LinearLayout
    lateinit var linear_total_dis_pump: LinearLayout
    lateinit var linear_agri_std_discount: LinearLayout
    lateinit var linear_agri_total_dis: LinearLayout
    lateinit var linear_distributor: LinearLayout
    lateinit var linear_Frieght: LinearLayout
    lateinit var linear_Doddiscount: LinearLayout
    lateinit var linear_Specialdiscount: LinearLayout
    lateinit var linear_distributionmargindiscount: LinearLayout
    lateinit var linear_cashdiscount: LinearLayout
    lateinit var linear_special: LinearLayout
    lateinit var linear_ebd_dis: LinearLayout
    lateinit var linear_total_dis: LinearLayout
    lateinit var linear_extradiscount: LinearLayout
    lateinit var titlepayment: LinearLayout
    var retailerArr: ArrayList<DistriutorModel.Datum> = arrayListOf()

    var customerId = ""
    var customertype = ""
    var ordercustomertype = ""
    var customertypein = ""
    private var page = 1
    private var pageSize = "500"
    private var parent_search = ""
    private var page_distri = 1
    private var pageSize_distri = "500"
    private var distri_search = ""
    private var lastPosition = -1
    private var isLoading = false
    var Discount_Limit = 0

    companion object {
        lateinit var tvTotal: TextView
        lateinit var tvGSTTotal: TextView
        lateinit var tvAmount: TextView
        lateinit var tv_5gstamt: TextView
        lateinit var tv_12gstamt: TextView
        lateinit var tv_18gstamt: TextView
        lateinit var tv_28gstamt: TextView
        lateinit var tv_ebd_amt: TextView
        lateinit var tv_stdfilter: TextView
        lateinit var tv_cluster_amt: TextView
        lateinit var tv_clusterfilter: TextView
        lateinit var tv_dealerfilter: TextView
        lateinit var tv_dealer_amt: TextView
        lateinit var tv_distributorfilter: TextView
        lateinit var tv_distributor_amt: TextView
        lateinit var tv_Frieghtfilter: TextView
        lateinit var tv_Frieght_amt: TextView
        lateinit var tv_Specialfilter: TextView
        lateinit var tv_Special_amt: TextView
        lateinit var tv_ebdfilter: TextView
        lateinit var tv_ebd_dis_amt: TextView
        lateinit var tv_dodvalue: TextView
        lateinit var tv_dod_amt: TextView
        lateinit var tv_specialvalue: TextView
        lateinit var tv_special_amt: TextView
        lateinit var tv_marginvalue: TextView
        lateinit var tv_margin_amt: TextView
        lateinit var tv_extravalue: TextView
        lateinit var tv_extra_amt: TextView
        lateinit var tv_cashvalue: TextView
        lateinit var tv_cash_amt: TextView
        lateinit var tv_disvalue: TextView
        lateinit var tv_dis_amt: TextView
        lateinit var tv_cashvalue_pump: TextView
        lateinit var tv_cash_amt_pump: TextView
        lateinit var tv_disvalue_pump: TextView
        lateinit var tv_dis_amt_pump: TextView
        lateinit var tv_std_agrivalue: TextView
        lateinit var tv_agri_dis_amt: TextView
        lateinit var tv_agri_totalvalue: TextView
        lateinit var tv_agri_total_amt: TextView
        lateinit var tv_remaining_pay: TextView
        lateinit var edt_advance_pay: EditText
    }

    var distributorArr: ArrayList<DistriutorModel.Datum> = arrayListOf();
    var diss_id = ""
    var disPos = -1
    var storePos = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_to_cart)

        initViews()
    }

    private fun initViews() {
        tvTotal = findViewById(R.id.tvTotal)
        tvGSTTotal = findViewById(R.id.tvGSTTotal)
        tvAmount = findViewById(R.id.tvAmount)

        recyclerView = findViewById(R.id.recyclerView)
        edtsubmitremark = findViewById(R.id.edtsubmitremark)
        edtDistributor = findViewById(R.id.edtDistributor)
        edtRetailer = findViewById(R.id.edtRetailer)
        cardSubmit = findViewById(R.id.cardSubmit)
        cardBack = findViewById(R.id.cardBack)

        linear_12gst = findViewById(R.id.linear_12gst)
        tv_12gstamt = findViewById(R.id.tv_12gstamt)
        linear_5gst = findViewById(R.id.linear_5gst)
        tv_5gstamt = findViewById(R.id.tv_5gstamt)
        linear_18gst = findViewById(R.id.linear_18gst)
        tv_18gstamt = findViewById(R.id.tv_18gstamt)
        linear_28gst = findViewById(R.id.linear_28gst)
        tv_28gstamt = findViewById(R.id.tv_28gstamt)

        linear_ebd = findViewById(R.id.linear_ebd)
        tv_ebd_amt = findViewById(R.id.tv_ebd_amt)
        tv_stdfilter = findViewById(R.id.tv_stdfilter)
        linear_cluster = findViewById(R.id.linear_cluster)
        tv_clusterfilter = findViewById(R.id.tv_clusterfilter)
        tv_cluster_amt = findViewById(R.id.tv_cluster_amt)
        linear_dealer = findViewById(R.id.linear_dealer)
        tv_dealerfilter = findViewById(R.id.tv_dealerfilter)
        tv_dealer_amt = findViewById(R.id.tv_dealer_amt)
        linear_distributor = findViewById(R.id.linear_distributor)
        tv_distributorfilter = findViewById(R.id.tv_distributorfilter)
        tv_distributor_amt = findViewById(R.id.tv_distributor_amt)
        linear_total_dis_pump = findViewById(R.id.linear_total_dis_pump)
        linear_agri_std_discount = findViewById(R.id.linear_agri_std_discount)
        tv_std_agrivalue = findViewById(R.id.tv_std_agrivalue)
        tv_agri_dis_amt = findViewById(R.id.tv_agri_dis_amt)
        linear_agri_total_dis = findViewById(R.id.linear_agri_total_dis)
        tv_agri_totalvalue = findViewById(R.id.tv_agri_totalvalue)
        tv_agri_total_amt = findViewById(R.id.tv_agri_total_amt)
        tv_disvalue_pump = findViewById(R.id.tv_disvalue_pump)
        tv_dis_amt_pump = findViewById(R.id.tv_dis_amt_pump)
        linear_Frieght = findViewById(R.id.linear_Frieght)
        linear_Doddiscount = findViewById(R.id.linear_Doddiscount)
        linear_cashdiscount_pump = findViewById(R.id.linear_cashdiscount_pump)
        tv_cashvalue_pump = findViewById(R.id.tv_cashvalue_pump)
        tv_cash_amt_pump = findViewById(R.id.tv_cash_amt_pump)
        linear_Specialdiscount = findViewById(R.id.linear_Specialdiscount)
        linear_distributionmargindiscount = findViewById(R.id.linear_distributionmargindiscount)
        linear_cashdiscount = findViewById(R.id.linear_cashdiscount)
        linear_extradiscount = findViewById(R.id.linear_extradiscount)
        tv_Frieghtfilter = findViewById(R.id.tv_Frieghtfilter)
        tv_Frieght_amt = findViewById(R.id.tv_Frieght_amt)
        linear_special = findViewById(R.id.linear_special)
        tv_Specialfilter = findViewById(R.id.tv_Specialfilter)
        tv_Special_amt = findViewById(R.id.tv_Special_amt)
        linear_ebd_dis = findViewById(R.id.linear_ebd_dis)
        tv_ebdfilter = findViewById(R.id.tv_ebdfilter)
        tv_ebd_dis_amt = findViewById(R.id.tv_ebd_dis_amt)
        tv_dodvalue = findViewById(R.id.tv_dodvalue)
        tv_specialvalue = findViewById(R.id.tv_specialvalue)
        tv_marginvalue = findViewById(R.id.tv_marginvalue)
        tv_cashvalue = findViewById(R.id.tv_cashvalue)
        linear_total_dis = findViewById(R.id.linear_total_dis)
        tv_disvalue = findViewById(R.id.tv_disvalue)
        tv_dod_amt = findViewById(R.id.tv_dod_amt)
        tv_special_amt = findViewById(R.id.tv_special_amt)
        tv_margin_amt = findViewById(R.id.tv_margin_amt)
        tv_extravalue = findViewById(R.id.tv_extravalue)
        tv_extra_amt = findViewById(R.id.tv_extra_amt)
        tv_cash_amt = findViewById(R.id.tv_cash_amt)
        tv_dis_amt = findViewById(R.id.tv_dis_amt)

        titlepayment = findViewById(R.id.titlepayment)
        edt_advance_pay = findViewById(R.id.edt_advance_pay)
        tv_remaining_pay = findViewById(R.id.tv_remaining_pay)

        recyclerView.layoutManager = GridLayoutManager(this, 1)

//        edtDistributor.setOnClickListener(this)

      /*  edtDistributor.setOnItemClickListener { adapterView, view, i, l ->
            for((index,value) in distributorArr!!.withIndex()){
                if(edtDistributor.text.toString()==value.name){
                    disPos = index
                }
            }
        }*/

        if (intent.getStringExtra("checkin") == "n") {
            edtRetailer.visibility = View.VISIBLE
            getRetailers(page,pageSize,parent_search)
            val categoryId = productArrAddToCart.get(0)?.categoryId
            if (categoryId!!.equals(4)){
                titlepayment.visibility = View.VISIBLE
            }
        }else if (intent.getStringExtra("checkin") == "y") {
            customertypein = StaticSharedpreference.getInfo(Constant.Customer_Type, this@AddToCartActivity).toString()
            if (customertypein.equals("Retailer") || customertypein.equals("Service Center")){
                edtDistributor.visibility = View.VISIBLE
            }else{
                edtDistributor.visibility = View.GONE
                ordercustomertype = customertypein
                setuprecycler()
            }
            val categoryId = productArrAddToCart.get(0)?.categoryId
            if (categoryId!!.equals(4)){
                titlepayment.visibility = View.VISIBLE
            }
        }
        edt_advance_pay.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let {
                    val totalValue = tvTotal.text.toString().toDoubleOrNull() ?: 0.0
                    val enteredValue = it.toString().toDoubleOrNull() ?: 0.0

                    if (enteredValue > totalValue) {
                        edt_advance_pay.text.clear()
                        tv_remaining_pay.text = ""
                        Toast.makeText(this@AddToCartActivity,"You cannot enter amount greater than $totalValue",Toast.LENGTH_SHORT).show()
                    } else {
                        val remainingPay = totalValue - enteredValue
                        tv_remaining_pay.text = String.format("%.2f", remainingPay)
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })




        edtRetailer.setOnClickListener {
            showretailerdropdown()
        }

        edtDistributor.setOnClickListener {
            showdistributoredropdown()
        }

        cardBack.setOnClickListener {
            onBackPressed()
        }

        cardSubmit.setOnClickListener(this)
        getDscountLimit()
        setuprecycler()
        getCustomerList(page_distri,pageSize_distri,distri_search)
    }

    private fun getDscountLimit() {
        if (!Utilities.isOnline(this@AddToCartActivity)) {
            return
        }

        val dialog = DialogClass.progressDialog(this@AddToCartActivity)

        val queryParams = HashMap<String, String>()

        ApiClient.getDiscountLimit(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this@AddToCartActivity).toString(),
            object : APIResultLitener<GetDiscountLimitModel> {
                @SuppressLint("SuspiciousIndentation")
                override fun onAPIResult(response: Response<GetDiscountLimitModel>?, errorMessage: String?) {
                    dialog.dismiss()

                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {

                            Discount_Limit = response.body()?.orderDiscountLimit!!.toInt()
                            setuprecycler()
                            println("response.body() ${Discount_Limit}")
                        } else {
                            // Handle other response codes
                        }
                    } else {
                        // Handle poor connection or other error
                        Toast.makeText(
                            this@AddToCartActivity,
                            resources.getString(R.string.poor_connection),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })
    }
    private lateinit var currentDialog: Dialog

    private fun showretailerdropdown() {
        currentDialog = Dialog(this)
        currentDialog.setContentView(R.layout.dialog_custom_retaier_dropdown)

        val searchInput = currentDialog.findViewById<EditText>(R.id.search_input)
        val searchIcon = currentDialog.findViewById<ImageView>(R.id.search_icon)
        val recyclerView = currentDialog.findViewById<RecyclerView>(R.id.recycler_dropdown)


        val adapter = CustomerretailerAdapter(retailerArr, currentDialog) { selectedItem ->
            val selectedCustomerId = selectedItem.customer_id
            val selectedCustomerName = selectedItem.name
            val selectedCustomertype = selectedItem.customer_type
            Log.d("SelectedItem", "ID: $selectedCustomerId, Name: $selectedCustomerName")
            edtRetailer.setText(selectedCustomerName)
            customerId = selectedCustomerId.toString()
            customertype = selectedCustomertype.toString()
            ordercustomertype = customertype
            setuprecycler()
            println("typeeee=="+customertype)
            if (customertype.equals("Retailer") || customertype.equals("Service Center")){
                edtDistributor.visibility = View.VISIBLE
            }else{
                customertype = ""
                edtDistributor.visibility = View.GONE
            }
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.scrollToPosition(lastPosition)


        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && totalItemCount <= firstVisibleItemPosition + visibleItemCount) {
                    page++
                    if (retailerArr.size >= 500) {
                        getRetailers(page, pageSize, "") // Call the API to load more items
                        lastPosition = firstVisibleItemPosition
                    }
                }
            }
        })

        searchIcon.setOnClickListener {
            val parent_search = searchInput.text.toString()
            page = 1 // Reset page to 1 for a new search
            getRetailers(page, pageSize, parent_search) // Call API with the search query
        }
        currentDialog.show()
    }

    private fun showdistributoredropdown() {
        currentDialog = Dialog(this)
        currentDialog.setContentView(R.layout.dialog_custom_retaier_dropdown)

        val searchInput = currentDialog.findViewById<EditText>(R.id.search_input)
        val searchIcon = currentDialog.findViewById<ImageView>(R.id.search_icon)
        val recyclerView = currentDialog.findViewById<RecyclerView>(R.id.recycler_dropdown)
        val dropdown_title = currentDialog.findViewById<TextView>(R.id.dropdown_title)
        dropdown_title.text = "Select Distributor"

        val adapter = CustomerDistributorAdapter(distributorArr, currentDialog) { selectedItem ->
            val selectedCustomerId = selectedItem.customer_id
            val selectedCustomerName = selectedItem.name
            edtDistributor.setText(selectedCustomerName)
            diss_id = selectedCustomerId.toString()
            Log.d("SelectedItem", "ID: $selectedCustomerId, Name: $selectedCustomerName")

        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.scrollToPosition(lastPosition)


        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && totalItemCount <= firstVisibleItemPosition + visibleItemCount) {
                    page_distri++
                    if (distributorArr.size >= 500) {
                        getCustomerList(page_distri, pageSize_distri, "") // Call the API to load more items
                        lastPosition = firstVisibleItemPosition
                    }
                }
            }
        })

        searchIcon.setOnClickListener {
            val parent_search = searchInput.text.toString()
            page_distri = 1 // Reset page to 1 for a new search
            getCustomerList(page_distri, pageSize_distri, parent_search) // Call API with the search query
        }

        currentDialog.show()

    }


    private fun setuprecycler() {
        val orderAddToCardAdapter = OrderAddToCardAdapter(this, linear_5gst, linear_12gst, linear_18gst, linear_28gst, ordercustomertype, linear_ebd, linear_cluster, linear_dealer, linear_distributor,
            linear_Frieght, linear_special, linear_ebd_dis, linear_Doddiscount, linear_Specialdiscount, linear_distributionmargindiscount, linear_cashdiscount, linear_total_dis,linear_extradiscount,linear_cashdiscount_pump,
            linear_total_dis_pump,linear_agri_std_discount,linear_agri_total_dis,Discount_Limit)
        recyclerView.adapter = orderAddToCardAdapter
    }


    override fun onClick(p0: View) {
        when (p0.id) {

            R.id.cardSubmit -> {
                if (customertype.equals("Retailer") || customertype.equals("Service Center")){
                        if (edtDistributor.text.toString() == "") {
                    Toast.makeText(
                        this,
                        "Please select distributor",
                        Toast.LENGTH_LONG
                    ).show()
                    return
                }
                } else if (intent.getStringExtra("checkin") == "n") {
                    if (edtRetailer.text.toString() == "") {
                        Toast.makeText(
                            this@AddToCartActivity,
                            "Please select Customer",
                            Toast.LENGTH_LONG
                        ).show()
                        return
                    }
                }else if (intent.getStringExtra("checkin") == "y") {
                    if (customertypein.equals("Retailer") || customertypein.equals("Service Center")) {
                        if (edtDistributor.text.toString() == "") {
                            Toast.makeText(
                                this@AddToCartActivity,
                                "Please select distributor",
                                Toast.LENGTH_LONG
                            ).show()
                            return
                        }
                    }
                }

                if (productArrAddToCart.size == 0) {
                    Toast.makeText(
                        this,
                        "Please add product",
                        Toast.LENGTH_LONG
                    ).show()
                } else if (edtsubmitremark.text.toString().isNullOrEmpty()){
                    Toast.makeText(this, "Please enter remark", Toast.LENGTH_LONG).show()
                } else{
                    insertOrder()
                }
            }

          /*  R.id.edtDistributor ->

                edtDistributor.showDropDown()*/

        }
    }

    private fun insertOrder() {
        if (!Utilities.isOnline(this)) {
            return
        }
        var dialog = DialogClass.progressDialog(this)

        var orderDetailsRequestModel = InsertOrderRequestModel()

        var requestArrLIst: ArrayList<InsertOrderRequestModel.Datum> = arrayListOf()


        if (intent.getStringExtra("checkin") == "n") {
            orderDetailsRequestModel.buyer_id = customerId
            if (customertype.equals("Retailer") || customertype.equals("Service Center")){
                orderDetailsRequestModel.seller_id = diss_id
            }else{
                orderDetailsRequestModel.seller_id = customerId
            }

        } else {
            orderDetailsRequestModel.buyer_id =
                StaticSharedpreference.getInfo(Constant.CHECKIN_CUST_ID, this).toString()

            if (customertypein.equals("Retailer") || customertypein.equals("Service Center")){
                orderDetailsRequestModel.seller_id = diss_id
            }else{
                orderDetailsRequestModel.seller_id = StaticSharedpreference.getInfo(Constant.CHECKIN_CUST_ID, this).toString()
            }

        }

        orderDetailsRequestModel.total_gst = tvGSTTotal.text.toString()
        orderDetailsRequestModel.sub_total = tvAmount.text.toString()
        orderDetailsRequestModel.grand_total = tvTotal.text.toString()
        orderDetailsRequestModel.beatScheduleId = beatScheduleId
        orderDetailsRequestModel.remark = edtsubmitremark.text.toString()
        orderDetailsRequestModel.gst5_amt = tv_5gstamt.text.toString()
        orderDetailsRequestModel.gst12_amt = tv_12gstamt.text.toString()
        orderDetailsRequestModel.gst18_amt = tv_18gstamt.text.toString()
        orderDetailsRequestModel.gst28_amt = tv_28gstamt.text.toString()


        if (ordercustomertype.equals("Dealer") || ordercustomertype.equals("Distributor")){
            orderDetailsRequestModel.schme_amount = tv_ebd_amt.text.toString()
            orderDetailsRequestModel.schme_val = tv_stdfilter.text.toString()
            orderDetailsRequestModel.ebd_discount = tv_ebdfilter.text.toString()
            orderDetailsRequestModel.ebd_amount = tv_ebd_dis_amt.text.toString()
            orderDetailsRequestModel.special_discount = tv_Specialfilter.text.toString()
            orderDetailsRequestModel.special_amount = tv_Special_amt.text.toString()
            orderDetailsRequestModel.cluster_discount = tv_clusterfilter.text.toString()
            orderDetailsRequestModel.cluster_amount = tv_cluster_amt.text.toString()
            orderDetailsRequestModel.deal_discount = tv_dealerfilter.text.toString()
            orderDetailsRequestModel.deal_amount = tv_dealer_amt.text.toString()
            orderDetailsRequestModel.distributor_discount = tv_distributorfilter.text.toString()
            orderDetailsRequestModel.distributor_amount = tv_distributor_amt.text.toString()
            orderDetailsRequestModel.frieght_discount = tv_Frieghtfilter.text.toString()
            orderDetailsRequestModel.frieght_amount = tv_Frieght_amt.text.toString()


            if (tv_cashvalue_pump.text.isNotEmpty()){
                orderDetailsRequestModel.cash_discount = tv_cashvalue_pump.text.toString()
                orderDetailsRequestModel.cash_amount = tv_cash_amt_pump.text.toString()
            }else{
                orderDetailsRequestModel.cash_discount = tv_cashvalue.text.toString()
                orderDetailsRequestModel.cash_amount = tv_cash_amt.text.toString()
            }
            if (tv_agri_totalvalue.text.isNotEmpty()){
                orderDetailsRequestModel.total_discount = tv_agri_totalvalue.text.toString()
                orderDetailsRequestModel.total_amount = tv_agri_total_amt.text.toString()
                orderDetailsRequestModel.advance = edt_advance_pay.text.toString()
            }else{
                orderDetailsRequestModel.total_discount = tv_disvalue_pump.text.toString()
                orderDetailsRequestModel.total_amount = tv_dis_amt_pump.text.toString()
            }


            orderDetailsRequestModel.dod_discount = tv_dodvalue.text.toString()
            orderDetailsRequestModel.dod_discount_amount = tv_dod_amt.text.toString()
            orderDetailsRequestModel.special_distribution_discount = tv_specialvalue.text.toString()
            orderDetailsRequestModel.special_distribution_discount_amount = tv_special_amt.text.toString()
            orderDetailsRequestModel.distribution_margin_discount = tv_marginvalue.text.toString()
            orderDetailsRequestModel.distribution_margin_discount_amount = tv_margin_amt.text.toString()
            orderDetailsRequestModel.fan_extra_discount = tv_extravalue.text.toString()
            orderDetailsRequestModel.fan_extra_discount_amount = tv_extra_amt.text.toString()
            orderDetailsRequestModel.total_fan_discount = tv_disvalue.text.toString()
            orderDetailsRequestModel.total_fan_discount_amount = tv_dis_amt.text.toString()

            orderDetailsRequestModel.agri_standard_discount = tv_std_agrivalue.text.toString()
            orderDetailsRequestModel.agri_standard_discount_amount = tv_agri_dis_amt.text.toString()

            println("orderrrrr=="+tv_std_agrivalue.text.toString()+"<<"+tv_agri_dis_amt.text.toString()+"<<"+
                    tv_agri_totalvalue.text.toString()+"<<"+ tv_agri_total_amt.text.toString())

            val categoryId = productArrAddToCart.get(0)?.categoryId


            if (ordercustomertype.equals("Distributor") && categoryId!!.equals(4)){
                for (value in productArrAddToCart) {
                    val requestModel = InsertOrderRequestModel().Datum()
                    requestModel.product_id = value!!.id.toString()
                    requestModel.quantity = value.quantity.toString()
                    requestModel.price = ((value.price?.toDouble() ?: 0.0) / 1.06).toString()
                    requestModel.line_total = (value!!.quantity.toString().toDouble() *  value!!.product_ebd_amount.toString().toDouble()).toString()
                    requestModel.ebd_amount = value.product_ebd_amount.toString()
                    requestArrLIst.add(requestModel)
                }
            }else{
                for (value in productArrAddToCart) {
                    val requestModel = InsertOrderRequestModel().Datum()
                    requestModel.product_id = value!!.id.toString()
                    requestModel.quantity = value.quantity.toString()
                    requestModel.price = value.price.toString()
                    requestModel.line_total = (value!!.quantity.toString().toDouble() *  value!!.product_ebd_amount.toString().toDouble()).toString()
                    requestModel.ebd_amount = value.product_ebd_amount.toString()
                    requestArrLIst.add(requestModel)
                }
            }



        } else{
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

            for (value in productArrAddToCart) {
                val requestModel = InsertOrderRequestModel().Datum()
                requestModel.product_id = value!!.id.toString()
                requestModel.quantity = value.quantity.toString()
                requestModel.price = value.price.toString()
                requestModel.line_total = (value.quantity.toDouble() * value.amount.toDouble()).toString()
                requestModel.ebd_amount = value.product_ebd_amount.toString()
                requestArrLIst.add(requestModel)
//                println("ABhiiiiii=="+requestModel.price+"<<"+requestModel.line_total+"<<"+requestModel.ebd_amount )
            }
        }
        orderDetailsRequestModel.orderdetail = requestArrLIst

        ApiClient.insertOrder(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this).toString(),
            orderDetailsRequestModel,
            object : APIResultLitener<JsonObject> {
                override fun onAPIResult(response: Response<JsonObject>?, errorMessage: String?) {

                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {

                            dialog.dismiss()
                            productArrAddToCart.clear()
                            alertDialog("Success", "" + response.body()!!.get("message").asString)

                        } else {
                            dialog.dismiss()

                            var jsonObject: JSONObject? = null
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                println("Errorooror=="+jsonObject.getString("message"))

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    this@AddToCartActivity,
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
            this
        )

    }

    private fun getCustomerList(page_distri: Int, pageSize_distri: String, distri_search: String) {
        if (!Utilities.isOnline(this)) {
            return
        }
        var dialog = DialogClass.progressDialog(this)

        val queryParams = HashMap<String, String>()
        queryParams["pageSize"] = pageSize_distri
        queryParams["page"] = page_distri.toString()
        queryParams["search"] = distri_search

        ApiClient.getDistributors(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this).toString(),
            queryParams,
            object :
                APIResultLitener<DistriutorModel> {
                override fun onAPIResult(
                    response: Response<DistriutorModel>?,
                    errorMessage: String?
                ) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {

                            if (page_distri == 1) {
                                distributorArr.clear() // Clear the previous results for the new search
                            }
                            distributorArr.addAll(response.body()!!.data!!)

                            if (::currentDialog.isInitialized && currentDialog.isShowing) {
                                distributorArr?.let {
                                    (currentDialog.findViewById<RecyclerView>(R.id.recycler_dropdown).adapter as CustomerDistributorAdapter)
                                        .updateList(it)
                                }
                            }

                            /*distributorArr = response.body()!!.data

                            val statesName = arrayOfNulls<String>(response.body()!!.data!!.size)

                            for (i in 0 until response.body()!!.data!!.size) {
                                statesName[i] = response.body()!!.data!![i].name
                            }

                            val aa = ArrayAdapter(this@AddToCartActivity, android.R.layout.simple_list_item_1, statesName)
                            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            edtDistributor.setAdapter(aa)

                            dialog.dismiss()*/


                        } else {
                            dialog.dismiss()

                            var jsonObject: JSONObject? = null
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    this@AddToCartActivity,
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
            }, this
        )
    }

    private fun getRetailers(page: Int, pageSize: String, parent_search: String) {

        if (!Utilities.isOnline(this@AddToCartActivity)) {
            return
        }
        var dialog = DialogClass.progressDialog(this@AddToCartActivity)

        val queryParams = HashMap<String, String>()
        queryParams["pageSize"] = pageSize
        queryParams["page"] = page.toString()
        queryParams["search"] = parent_search

        ApiClient.getRetailers(
            StaticSharedpreference.getInfo(
                Constant.ACCESS_TOKEN,
                this@AddToCartActivity
            ).toString(), queryParams, object : APIResultLitener<DistriutorModel> {
                override fun onAPIResult(
                    response: Response<DistriutorModel>?,
                    errorMessage: String?
                ) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {

                               if (page == 1) {
                                   retailerArr.clear() // Clear the previous results for the new search
                               }
                               retailerArr.addAll(response.body()!!.data!!)

                            if (::currentDialog.isInitialized && currentDialog.isShowing) {
                                retailerArr?.let {
                                    (currentDialog.findViewById<RecyclerView>(R.id.recycler_dropdown).adapter as CustomerretailerAdapter)
                                        .updateList(it)
                                }
                            }


                           /* retailerArr = response.body()!!.data

                            val disName = arrayOfNulls<String>(retailerArr!!.size)

                            for (i in retailerArr!!.indices) {

                                disName[i] = retailerArr!![i].name

                            }

                            val aa = ArrayAdapter(
                                this@AddToCartActivity,
                                android.R.layout.simple_list_item_1,
                                disName
                            )
                            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            edtRetailer.setAdapter(aa)*/

                        }
                    } else {
                        Toast.makeText(
                            this@AddToCartActivity,
                            resources.getString(R.string.poor_connection),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        )
    }



    fun alertDialog(
        title: String,
        description: String
    ) {

        val builder = AlertDialog.Builder(this)

        builder.setMessage(description)
            .setCancelable(false)
            .setPositiveButton("OK") { dialog, id ->

                if (intent.getStringExtra("checkin") == "n") {
                    MainActivity.tabPosition = 0
                    startActivity(Intent(this@AddToCartActivity, MainActivity::class.java))
                    finishAffinity()
                } else {
                    isBack = true
                    finish()
                }
            }

        val alert = builder.create()
        alert.setCanceledOnTouchOutside(false)
        alert.setCancelable(false)
        alert.setTitle(title)
        alert.show()
    }


}