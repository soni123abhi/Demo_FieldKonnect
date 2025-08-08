package com.exp.clonefieldkonnect.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import com.exp.import.Utilities
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.connection.APIResultLitener
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.helper.*
import com.bumptech.glide.Glide
import com.exp.clonefieldkonnect.model.AttendanceSubmitModel
import com.exp.clonefieldkonnect.model.CustomerActivityModel
import com.exp.clonefieldkonnect.model.DraftReportModel
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import retrofit2.Response
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class CounterDashboardActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var name: TextView
    lateinit var tvTitle: TextView
    lateinit var cardCheckIn: CardView
    lateinit var cardCheckOUt: CardView
    lateinit var cardSales: CardView
    lateinit var cardOrders: CardView
    lateinit var cardOrder: CardView
    lateinit var cardReport: CardView
    lateinit var cardBack: CardView
    lateinit var cardCoupon: CardView
    lateinit var cardProfile: CardView
    lateinit var cardPaymentReceived: CardView
    lateinit var cardview_activity: CardView
    lateinit var card_order: CardView
    lateinit var cardview_sarthi_points: CardView
    lateinit var cardConvert: CardView
    lateinit var cardSearch: RelativeLayout
    lateinit var relativeCI: RelativeLayout
    lateinit var relativeDashboard: RelativeLayout
    lateinit var cardFrom: LinearLayout
    lateinit var cardTo: LinearLayout
    lateinit var linearDashboardData: LinearLayout
    lateinit var linearCInfo: LinearLayout
    lateinit var view1: View
    lateinit var view2: View

    lateinit var linearOut: LinearLayout
    lateinit var linearReport: LinearLayout
    lateinit var linearOrder: LinearLayout
    lateinit var linearSales: LinearLayout
    lateinit var linearScan: LinearLayout
    lateinit var linearIn: LinearLayout
    lateinit var linearOutstanding: LinearLayout
    lateinit var carddraft: CardView
    lateinit var tvIn: TextView
    lateinit var tvReport: TextView
    lateinit var tvOrder: TextView
    lateinit var tvSales: TextView
    lateinit var tvScan: TextView
    lateinit var tvOut: TextView
    lateinit var tvAddress: TextView
    lateinit var tvOutstanding: TextView
    lateinit var tvName: TextView
    lateinit var tvFrom: TextView
    lateinit var tvTo: TextView
    lateinit var tvCheckIn: TextView
    lateinit var tvTotalOrderValue: TextView
    lateinit var tvOrderQty: TextView
    lateinit var tvAvgOrderValue: TextView
    lateinit var tvAvgOrderQty: TextView
    lateinit var tvLastVisit: TextView
    lateinit var tvLastCall: TextView

    lateinit var tvShopName: TextView
    lateinit var tvContactPerson: TextView
    lateinit var tvMobile: TextView
    lateinit var tvEmail: TextView
    lateinit var tvTown: TextView
    lateinit var tvBeat: TextView
    lateinit var tvAddressShop: TextView
    lateinit var tvPincode: TextView
    lateinit var tvType: TextView
    lateinit var tvGSTIN: TextView
    lateinit var tvPAN: TextView
    lateinit var tvAdhar: TextView
    lateinit var tvOther: TextView

    private val REQUEST_CHECK_SETTINGS = 0x1
    private val REQUEST_VISIT_REPORT = 114
    private var mGoogleApiClient: GoogleApiClient? = null
    lateinit var latitude: String
    lateinit var longitude: String
    private var isLoading = false


    companion object {
        var beatScheduleId: String = ""
    }

    lateinit var imgShop: ImageView
    lateinit var imgProfile: ImageView
    lateinit var imgProfile2: ImageView
    lateinit var tvViewAll: TextView
    lateinit var cardPointCollection: CardView
    lateinit var cardEdit: CardView
    lateinit var relativePointCollection: RelativeLayout
    var checkInOrOut = ""
    var isBackHome = false
    var isLead = false
    var customeractivityList : ArrayList<CustomerActivityModel> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_counter)
        initViews()
        getCustomerInfo()
    }

    private fun initViews() {
        imgProfile = findViewById(R.id.imgProfile)
        imgProfile2 = findViewById(R.id.imgProfile2)
        cardEdit = findViewById(R.id.cardEdit)
        relativePointCollection = findViewById(R.id.relativePointCollection)
        tvViewAll = findViewById(R.id.tvViewAll)
        cardPointCollection = findViewById(R.id.cardPointCollection)
        linearCInfo = findViewById(R.id.linearCInfo)
        linearDashboardData = findViewById(R.id.linearDashboardData)
        carddraft = findViewById(R.id.carddraft)
        view1 = findViewById(R.id.view1)
        view2 = findViewById(R.id.view2)
        relativeDashboard = findViewById(R.id.relativeDashboard)
        relativeCI = findViewById(R.id.relativeCI)
        cardSearch = findViewById(R.id.cardSearch)
        tvTotalOrderValue = findViewById(R.id.tvTotalOrderValue)
        tvOrderQty = findViewById(R.id.tvOrderQty)
        tvAvgOrderValue = findViewById(R.id.tvAvgOrderValue)
        tvAvgOrderQty = findViewById(R.id.tvAvgOrderQty)
        tvLastVisit = findViewById(R.id.tvLastVisit)
        tvLastCall = findViewById(R.id.tvLastCall)
        cardview_activity = findViewById(R.id.cardview_activity)
        card_order = findViewById(R.id.card_order)

        tvShopName = findViewById(R.id.tvShopName)
        tvContactPerson = findViewById(R.id.tvContactPerson)
        tvMobile = findViewById(R.id.tvMobile)
        tvEmail = findViewById(R.id.tvEmail)
        tvTown = findViewById(R.id.tvTown)
        tvBeat = findViewById(R.id.tvBeat)
        tvAddressShop = findViewById(R.id.tvAddressShop)
        tvPincode = findViewById(R.id.tvPincode)
        tvType = findViewById(R.id.tvType)
        tvGSTIN = findViewById(R.id.tvGSTIN)
        tvPAN = findViewById(R.id.tvPAN)
        tvAdhar = findViewById(R.id.tvAdhar)
        tvOther = findViewById(R.id.tvOther)

        tvCheckIn = findViewById(R.id.tvCheckIn)
        cardFrom = findViewById(R.id.cardFrom)
        cardTo = findViewById(R.id.cardTo)
        tvFrom = findViewById(R.id.tvFrom)
        tvTo = findViewById(R.id.tvTo)
        tvName = findViewById(R.id.tvName)
        imgShop = findViewById(R.id.imgShop)
        cardProfile = findViewById(R.id.cardProfile)
        tvOutstanding = findViewById(R.id.tvOutstanding)
        name = findViewById(R.id.name)
        tvTitle = findViewById(R.id.tvTitle)
        cardCheckIn = findViewById(R.id.cardCheckIn)
        cardCheckOUt = findViewById(R.id.cardCheckOUt)
        cardOrders = findViewById(R.id.cardOrders)
        cardOrder = findViewById(R.id.cardOrder)
        cardSales = findViewById(R.id.cardSales)
        cardReport = findViewById(R.id.cardReport)
        cardBack = findViewById(R.id.cardBack)
        cardCoupon = findViewById(R.id.cardCoupon)
        cardPaymentReceived = findViewById(R.id.cardPaymentReceived)
        cardConvert = findViewById(R.id.cardConvert)
        linearOutstanding = findViewById(R.id.linearOutstanding)

        linearOut = findViewById(R.id.linearOut)
        linearReport = findViewById(R.id.linearReport)
        linearOrder = findViewById(R.id.linearOrder)
        linearSales = findViewById(R.id.linearSales)
        linearScan = findViewById(R.id.linearScan)
        linearIn = findViewById(R.id.linearIn)
        tvIn = findViewById(R.id.tvIn)
        tvReport = findViewById(R.id.tvReport)
        tvOrder = findViewById(R.id.tvOrder)
        tvSales = findViewById(R.id.tvSales)
        tvScan = findViewById(R.id.tvScan)
        tvOut = findViewById(R.id.tvOut)
        tvAddress = findViewById(R.id.tvAddress)
        cardview_sarthi_points = findViewById(R.id.cardview_sarthi_points)

        tvName.text = intent.getStringExtra("customerName")
        name.text = intent.getStringExtra("customerName")
        tvAddress.text = intent.getStringExtra("customerAddress")
        // tvTitle.text = intent.getStringExtra("customerName")
        beatScheduleId = intent.getStringExtra("beat_schedule_id")!!

        Glide.with(this@CounterDashboardActivity).load(
            intent.getStringExtra("image")
        ).centerCrop().into(imgShop)

        println("imageee==11=="+intent.getStringExtra("image"))

        isLead = intent.getBooleanExtra("isLead", false)

        if (isLead) {

            cardConvert.visibility = View.VISIBLE
            linearOutstanding.visibility = View.GONE
            tvOutstanding.visibility = View.GONE
        } else {
            if (intent.getStringExtra("outstanding")
                    .toString() != "" && intent.getStringExtra("outstanding").toString() != "null"
            )
                tvOutstanding.text = "Outstanding : " + DecimalFormat("##.#").format(
                    intent.getStringExtra("outstanding").toString().toFloat()
                ).toString()
        }

        cardBack.setOnClickListener {
            onBackPressed()
        }

        relativeCI.setOnClickListener(this)
        relativeDashboard.setOnClickListener(this)
        cardSearch.setOnClickListener(this)
        cardCheckIn.setOnClickListener(this)
        cardCheckOUt.setOnClickListener(this)
        cardOrder.setOnClickListener(this)
        cardSales.setOnClickListener(this)
        cardPaymentReceived.setOnClickListener(this)
        cardConvert.setOnClickListener(this)
        tvOutstanding.setOnClickListener(this)
        cardProfile.setOnClickListener(this)
        cardFrom.setOnClickListener(this)
        cardTo.setOnClickListener(this)
        cardPointCollection.setOnClickListener(this)
        tvViewAll.setOnClickListener(this)
        cardEdit.setOnClickListener(this)
        cardview_activity.setOnClickListener(this)
        card_order.setOnClickListener(this)
        cardview_sarthi_points.setOnClickListener(this)
        carddraft.setOnClickListener(this)

        if (intent.getStringExtra("checkInId") == "") {
            checkOut()
        } else {
            checkIn()
        }
    }

    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.cardCheckIn -> {
                if (StaticSharedpreference.getInfo(Constant.IS_PUNCH_IN, this) == "false") {
                    Toast.makeText(
                        this,
                        "Before Checkin Please submit today attendance",
                        Toast.LENGTH_LONG
                    ).show()
                } else if (StaticSharedpreference.getInfo(Constant.IS_PUNCH_OUT, this) == "true") {
                    Toast.makeText(this, "You have completed your day", Toast.LENGTH_LONG).show()
                } else {
                    if (tvCheckIn.text.toString() == "Check In") {
                        carddraft.visibility = View.GONE
                        checkInOrOut = "in"
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            ),
                            1
                        )
                    } else {
                        checkInOrOut = "out"
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            ),
                            1
                        )
                    }
                }
            }
            R.id.carddraft ->{
                showpopup()
            }

            R.id.cardCheckOUt -> {
                if (StaticSharedpreference.getInfo(
                        Constant.REPORT_SUBMIT,
                        this@CounterDashboardActivity
                    ) == "1"
                ) {
                    checkInOrOut = "out"
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ),
                        1
                    )
                } else {
                    Toast.makeText(
                        this@CounterDashboardActivity,
                        "Please submitted visit report first",
                        Toast.LENGTH_LONG
                    ).show()
                }


            }
            R.id.cardOrder -> {
                if (isLead) {
                    Toast.makeText(
                        this@CounterDashboardActivity,
                        "You have to convert lead to customer before order",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    val intent = Intent(this@CounterDashboardActivity, ProductActivity::class.java)
                    intent.putExtra("checkin", "y")
                    intent.putExtra("beatScheduleId", beatScheduleId)
                    startActivity(intent)
                }
            }

            R.id.cardSales -> {
                if (isLead) {
                    Toast.makeText(
                        this@CounterDashboardActivity,
                        "You have to convert lead to customer before Sales",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    val intent =
                        Intent(this@CounterDashboardActivity, InsertSalesActivity::class.java)
                    intent.putExtra("checkin", "y")
                    startActivity(intent)
                }
            }

            R.id.cardCoupon -> {

            }

            R.id.tvOutstanding -> {
                val intent = Intent(this@CounterDashboardActivity, OutstandingPaymentActivity::class.java)
                startActivity(intent)
            }

            R.id.cardConvert -> {
                leadToCustomer()
            }
            R.id.cardPaymentReceived -> {
                val intent = Intent(this@CounterDashboardActivity, PaymentReceivedActivity::class.java)
                intent.putExtra("from", "beat")
                startActivity(intent)
            }
            R.id.cardProfile -> {
                val intent = Intent(this@CounterDashboardActivity, CustomerEditActivity::class.java)
                startActivity(intent)
            }
            R.id.cardPointCollection -> {
                val intent =
                    Intent(this@CounterDashboardActivity, AddPointCollectionActivity::class.java)
                startActivity(intent)
            }

            R.id.tvViewAll -> {
                val intent =
                    Intent(this@CounterDashboardActivity, PointCollectionActivity::class.java)
                startActivity(intent)
            }

            R.id.cardFrom -> {
                Utilities.datePicker(tvFrom, tvTo.text.toString(), "", true, this)
            }
            R.id.cardTo -> {
                Utilities.datePicker(tvTo, "", tvFrom.text.toString(), false, this)
            }
            R.id.cardSearch -> {
                val convertedDate = convertDateFormats(tvFrom.text.toString(),tvTo.text.toString())
                println("from=="+convertedDate.first+"To="+convertedDate.second)
                getCustomerInfo(convertedDate.first, convertedDate.second)
            }
            R.id.relativeCI -> {
                view1.visibility = View.GONE
                view2.visibility = View.VISIBLE
                linearDashboardData.visibility = View.GONE
                linearCInfo.visibility = View.VISIBLE
            }
            R.id.relativeDashboard -> {
                view2.visibility = View.GONE
                view1.visibility = View.VISIBLE
                linearDashboardData.visibility = View.VISIBLE
                linearCInfo.visibility = View.GONE
            }
            R.id.cardEdit -> {

                val intent = Intent(this@CounterDashboardActivity, UpdateCustomerActivity::class.java)
                intent.putExtra("customerId", StaticSharedpreference.getInfo(Constant.CHECKIN_CUST_ID, this@CounterDashboardActivity).toString())
                intent.putExtra("customerTypeId", customerTypeId.toString())
                startActivityForResult(intent,111)
            }
            R.id.cardview_activity -> {
                println("sizesizesizesize==" + customeractivityList.size)
                val intent = Intent(this@CounterDashboardActivity, CustomerHistoryActivity::class.java)
                intent.putExtra("activity_list", customeractivityList)
                startActivity(intent)
            }
            R.id.card_order -> {
                val intent = Intent(this@CounterDashboardActivity, MainActivity::class.java)
                intent.putExtra("CustomerVisit", "customer_order_his")
                intent.putExtra("customerId", StaticSharedpreference.getInfo(Constant.CHECKIN_CUST_ID, this@CounterDashboardActivity).toString())
                startActivity(intent)
            }
            R.id.cardview_sarthi_points -> {
//                Toast.makeText(this@CounterDashboardActivity,"Working on it..!!",Toast.LENGTH_SHORT).show()
                val intent = Intent(this@CounterDashboardActivity, SarthidetailActivity::class.java)
                intent.putExtra("customerId", StaticSharedpreference.getInfo(Constant.CHECKIN_CUST_ID, this@CounterDashboardActivity).toString())
                startActivity(intent)
            }
        }
    }

    private lateinit var alertDialog: androidx.appcompat.app.AlertDialog
    @SuppressLint("MissingInflatedId")
    private fun showpopup() {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.draft_popup_layout, null)
        builder.setCancelable(false)


        val cardbtn_submit: CardView = view.findViewById(R.id.cardbtn_submit)
        val edtDesc: EditText = view.findViewById(R.id.edtDesc)
        val img_close: ImageView = view.findViewById(R.id.img_close)

        getdraftreport(edtDesc)



        img_close.setOnClickListener {
            alertDialog.dismiss()
        }

        cardbtn_submit.setOnClickListener {
            if (edtDesc.text.isNullOrEmpty()) {
                Toast.makeText(this@CounterDashboardActivity,"Please Enter Report Description",Toast.LENGTH_SHORT).show()
            }else{
                println("Abhiiiiiii=="+StaticSharedpreference.getInfo(Constant.CHECKIN_ID,this@CounterDashboardActivity))
                submitdraftreport(edtDesc.text.toString(),alertDialog)
            }

        }

        builder.setView(view)

        alertDialog = builder.create()
        alertDialog.show()
    }

    private fun getdraftreport(edtDesc: EditText) {
        isLoading = true
        if (!Utilities.isOnline(this@CounterDashboardActivity)) {
            isLoading = false
            return
        }
        var dialog = DialogClass.progressDialog(this@CounterDashboardActivity)
        val queryParams = java.util.HashMap<String, String>()
        queryParams["checkin_id"] = StaticSharedpreference.getInfo(Constant.CHECKIN_ID,this@CounterDashboardActivity).toString()

        ApiClient.getdraftreport(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this@CounterDashboardActivity).toString(),
            queryParams,
            object : APIResultLitener<DraftReportModel> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onAPIResult(response: Response<DraftReportModel>?, errorMessage: String?) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {
                        if (response.code() == 200) {
                            response.body()?.data?.draftMsg?.let { draftMsg ->
                                if (draftMsg.isNotEmpty()) {
                                    edtDesc.setText(draftMsg)
                                }
                            }

                        } else {
                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    this@CounterDashboardActivity, false
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        isLoading = false
                    }
                    else {
                        Toast.makeText(this@CounterDashboardActivity, resources.getString(R.string.data_not_found), Toast.LENGTH_LONG).show()
                    }
                }
            })
    }


    private fun submitdraftreport(
        description: String,
        alertDialog: androidx.appcompat.app.AlertDialog
    ) {
        isLoading = true
        if (!Utilities.isOnline(this@CounterDashboardActivity)) {
            isLoading = false
            return
        }
        var dialog = DialogClass.progressDialog(this@CounterDashboardActivity)
        val queryParams = java.util.HashMap<String, String>()
        queryParams["checkin_id"] = StaticSharedpreference.getInfo(Constant.CHECKIN_ID,this@CounterDashboardActivity).toString()
        queryParams["draft_msg"] = description

        ApiClient.submitdraftreport(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this@CounterDashboardActivity).toString(),
            queryParams,
            object : APIResultLitener<AttendanceSubmitModel> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onAPIResult(response: Response<AttendanceSubmitModel>?, errorMessage: String?) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {
                        if (response.code() == 200) {
                            Toast.makeText(this@CounterDashboardActivity,response.body()!!.message,Toast.LENGTH_SHORT).show()
                            alertDialog.dismiss()
                        } else {
                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    this@CounterDashboardActivity, false
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        isLoading = false
                    }
                    else {
                        Toast.makeText(this@CounterDashboardActivity, resources.getString(R.string.data_not_found), Toast.LENGTH_LONG).show()
                    }
                }
            })
    }



    fun convertDateFormats(tvFrom: String, tvTo: String): Pair<String, String> {
        val inputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

        val tvFromParsed = inputFormat.parse(tvFrom)

        val tvToParsed = inputFormat.parse(tvTo)

        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val tvfrom = outputFormat.format(tvFromParsed)
        val tvTo = outputFormat.format(tvToParsed)

        return Pair(tvfrom,tvTo)
    }

    lateinit var dialog: Dialog
    private fun submitCheckin() {

        if (!Utilities.isOnline(this)) {
            return
        }

        val queryParams = HashMap<String, String>()

        if (isLead)
            queryParams["lead_id"] =
                StaticSharedpreference.getInfo(
                    Constant.CHECKIN_CUST_ID,
                    this@CounterDashboardActivity
                )
                    .toString()
        else
            queryParams["customer_id"] =
                StaticSharedpreference.getInfo(
                    Constant.CHECKIN_CUST_ID,
                    this@CounterDashboardActivity
                )
                    .toString()


        queryParams["checkin_latitude"] = latitude
        queryParams["checkin_longitude"] = longitude
        queryParams["beatScheduleId"] = beatScheduleId

        println("ABHI=="+latitude)
        println("ABHI==lonnn="+longitude)
        println("ABHI==lonnn="+StaticSharedpreference.getInfo(Constant.CHECKIN_CUST_ID, this@CounterDashboardActivity).toString())

        ApiClient.submitCheckin(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this).toString(),
            queryParams,
            object : APIResultLitener<JsonObject> {
                override fun onAPIResult(response: Response<JsonObject>?, errorMessage: String?) {

                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {
                            StaticSharedpreference.saveInfo(Constant.CHECKIN_ID, response.body()!!.get("checkin_id").asString, this@CounterDashboardActivity)

                            StaticSharedpreference.saveInfo(Constant.CHECKIN_IMAGE, intent.getStringExtra("image").toString(), this@CounterDashboardActivity)
                            StaticSharedpreference.saveInfo(Constant.REPORT_SUBMIT, "", this@CounterDashboardActivity)
                            StaticSharedpreference.saveInfo(Constant.Customer_Type, response.body()!!.get("customer_type").asString, this@CounterDashboardActivity)

                            checkIn()
                            isBackHome = true
                        } else {

                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    this@CounterDashboardActivity,
                                    false
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                        }
                    } else {
                        //  dialog.dismiss()
                        Toast.makeText(this@CounterDashboardActivity, resources.getString(R.string.poor_connection), Toast.LENGTH_LONG).show()
                    }
                }
            })
    }

    private fun submitCheckout( typeI:String, desc:String) {

        if (!Utilities.isOnline(this)) {
            return
        }

        val queryParams = HashMap<String, String>()

        queryParams["customer_id"] = StaticSharedpreference.getInfo(Constant.CHECKIN_CUST_ID, this@CounterDashboardActivity).toString()
        queryParams["checkout_latitude"] = latitude
        queryParams["visit_type_id"] = typeI
        queryParams["description"] = desc
        queryParams["checkout_longitude"] = longitude
        queryParams["checkin_id"] = StaticSharedpreference.getInfo(Constant.CHECKIN_ID, this@CounterDashboardActivity).toString()
       // queryParams["beatScheduleId"] = beatScheduleId.toString()

        println("ABHI=outtlat="+latitude)
        println("ABHI==outtlonnng="+longitude)

        ApiClient.submitCheckout(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this).toString(),
            queryParams,
            object : APIResultLitener<JsonObject> {
                override fun onAPIResult(response: Response<JsonObject>?, errorMessage: String?) {

                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {

                            Toast.makeText(this@CounterDashboardActivity, "Checkout Success", Toast.LENGTH_LONG).show()

                            StaticSharedpreference.saveInfo(Constant.REPORT_SUBMIT, "", this@CounterDashboardActivity)
                            StaticSharedpreference.saveInfo(
                                Constant.Customer_Type,
                                "",
                                this@CounterDashboardActivity
                            )


                            MainActivity.tabPosition = 0
                            startActivity(Intent(this@CounterDashboardActivity, MainActivity::class.java))
                            finishAffinity()

                        } else {

                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    this@CounterDashboardActivity,
                                    false
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                        }
                    } else {
                        //  dialog.dismiss()
                        Toast.makeText(
                            this@CounterDashboardActivity,
                            resources.getString(R.string.poor_connection),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })
    }

    private fun leadToCustomer() {

        if (!Utilities.isOnline(this)) {
            return
        }

        dialog = DialogClass.progressDialog(this)

        val queryParams = HashMap<String, String>()
        queryParams["lead_id"] =
            StaticSharedpreference.getInfo(Constant.CHECKIN_CUST_ID, this@CounterDashboardActivity)
                .toString()
        queryParams["beatScheduleId"] = beatScheduleId

        ApiClient.leadToCustomer(StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this)
            .toString(),
            queryParams, object : APIResultLitener<JsonObject> {
                override fun onAPIResult(response: Response<JsonObject>?, errorMessage: String?) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {

                            StaticSharedpreference.saveInfo(
                                Constant.CHECKIN_CUST_ID,
                                response.body()!!
                                    .get("data").asJsonObject.get("id").asInt.toString(),
                                this@CounterDashboardActivity
                            )
                            cardConvert.visibility = View.GONE
                            linearOutstanding.visibility = View.VISIBLE
                            isLead = false

                            checkOut()

                            StaticSharedpreference.saveInfo(
                                Constant.CHECKIN_ID,
                                "",
                                this@CounterDashboardActivity
                            )

                            StaticSharedpreference.saveInfo(
                                Constant.REPORT_SUBMIT,
                                "",
                                this@CounterDashboardActivity
                            )


                            Toast.makeText(
                                this@CounterDashboardActivity,
                                "Successfully Convert Lead to Customer",
                                Toast.LENGTH_LONG
                            ).show()

                        } else {

                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    this@CounterDashboardActivity,
                                    false
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                        }
                    } else {
                        Toast.makeText(
                            this@CounterDashboardActivity,
                            resources.getString(R.string.poor_connection),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })
    }

    var customerTypeId = 0

    private fun getCustomerInfo(fromDate: String = "", toDate: String = "") {
        if (!Utilities.isOnline(this)) {
            return
        }
        var dialog = DialogClass.progressDialog(this)
        val queryParams = java.util.HashMap<String, String>()
        queryParams["customer_id"] = StaticSharedpreference.getInfo(Constant.CHECKIN_CUST_ID, this).toString()
        queryParams["fromDate"] = fromDate
        queryParams["toDate"] = toDate

        println("DATEEEEEEEEE=="+StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this).toString())
        println("DATEEEEEEEEE=token="+StaticSharedpreference.getInfo(Constant.CHECKIN_CUST_ID, this).toString())

        ApiClient.getCustomerInfo(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this).toString(),
            queryParams,
            object :
                APIResultLitener<JsonObject> {
                override fun onAPIResult(response: Response<JsonObject>?, errorMessage: String?) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {

                            var json = response.body()!!.get("data").asJsonObject

                            val activityJsonArray = json.getAsJsonArray("activity")
                            val gson = Gson()
                            val listType = object : TypeToken<List<CustomerActivityModel>>() {}.type
                            val activityList: List<CustomerActivityModel> = gson.fromJson(activityJsonArray, listType)
                            customeractivityList.clear()
                            customeractivityList.addAll(activityList)


                            tvOutstanding.text = json.get("total_sales_value").asInt.toString()
                            tvTotalOrderValue.text = json.get("total_order_value").asInt.toString()
                            tvOrderQty.text = json.get("total_order_quantity").asInt.toString()
                            tvAvgOrderValue.text = json.get("avg_order_value").asString.toString()
                            tvAvgOrderQty.text = json.get("avg_order_quantity").asString.toString()
                            tvLastVisit.text = json.get("last_visited").asString.toString()
                            tvLastCall.text = json.get("last_order_date").asString.toString()


                            val sapCode = if (json.has("sap_code") && !json.get("sap_code").isJsonNull) {
                                json.get("sap_code").asString
                            } else {
                                ""
                            }
                            tvBeat.text = sapCode


                            println("total_order_value="+json.get("total_order_value").asInt.toString())
                            try {
                                tvShopName.text = json.get("name").asString.toString()
                                try {
                                    tvContactPerson.text = json.get("first_name").asString.toString() + " " + json.get("last_name").asString.toString()
                                } catch (e: Exception) {
                                }
                                tvMobile.text = json.get("mobile").asString.toString()
                                tvEmail.text = json.get("email").asString.toString()
                                tvAddressShop.text =
                                    json.get("customeraddress").asJsonObject.get("address1").asString.toString()
                                tvPincode.text =
                                    json.get("customeraddress").asJsonObject.get("zipcode").asString.toString()
                                tvGSTIN.text =
                                    json.get("customerdetails").asJsonObject.get("gstin_no").asString.toString()
                                tvPAN.text =
                                    json.get("customerdetails").asJsonObject.get("pan_no").asString.toString()
                                tvAdhar.text =
                                    json.get("customerdetails").asJsonObject.get("aadhar_no").asString.toString()
                                tvOther.text =
                                    json.get("customerdetails").asJsonObject.get("otherid_no").asString.toString()
                                tvType.text =
                                    json.get("customertypes").asJsonObject.get("customertype_name").asString.toString()


                                customerTypeId = json.get("customertypes").asJsonObject.get("id").asInt
                                if (json.get("customertypes").asJsonObject.get("id").asInt == 4) {
                                    relativePointCollection.visibility = View.VISIBLE
                                } else {
                                    relativePointCollection.visibility = View.GONE
                                }

                                tvTown.text =
                                    json.get("customeraddress").asJsonObject.get("cityname").asJsonObject.get(
                                        "city_name"
                                    ).asString.toString()

                                Glide.with(this@CounterDashboardActivity)
                                    .load(json.get("profile_image").asString)
                                    .into(imgProfile)

                                Glide.with(this@CounterDashboardActivity)
                                    .load(json.get("customerdetails").asJsonObject.get("visiting_card").asString)
                                    .into(imgProfile2)

                            } catch (e: Exception) {
                            }

                        } else {


                            var jsonObject: JSONObject? = null
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    this@CounterDashboardActivity,
                                    false
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                        }
                    } else {

                    }
                }
            })
    }


    private fun updateCustomerLocation() {

        if (!Utilities.isOnline(this)) {
            return
        }

        val queryParams = HashMap<String, String>()
        queryParams["customer_id"] =
            StaticSharedpreference.getInfo(Constant.CHECKIN_CUST_ID, this@CounterDashboardActivity)
                .toString()
        queryParams["beatScheduleId"] = beatScheduleId
        queryParams["latitude"] = latitude
        queryParams["longitude"] = longitude

        ApiClient.updateCustomerLocation(StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this)
            .toString(),
            queryParams, object : APIResultLitener<JsonObject> {
                override fun onAPIResult(response: Response<JsonObject>?, errorMessage: String?) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {
                            Toast.makeText(this@CounterDashboardActivity, "Location updated successfully", Toast.LENGTH_LONG).show()
                        } else {

                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    this@CounterDashboardActivity,
                                    false
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                        }
                    } else {
                        //  dialog.dismiss()
                        Toast.makeText(
                            this@CounterDashboardActivity,
                            resources.getString(R.string.poor_connection),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                val perms = java.util.HashMap<String, Int>()
                perms[Manifest.permission.ACCESS_FINE_LOCATION] = PackageManager.PERMISSION_GRANTED
                perms[Manifest.permission.ACCESS_COARSE_LOCATION] =
                    PackageManager.PERMISSION_GRANTED

                // Fill with results
                for (i in permissions.indices)
                    perms[permissions[i]] = grantResults[i]
                // Check for ACCESS_FINE_LOCATION
                if (perms[Manifest.permission.ACCESS_FINE_LOCATION] == PackageManager.PERMISSION_GRANTED
                    && perms[Manifest.permission.ACCESS_COARSE_LOCATION] == PackageManager.PERMISSION_GRANTED

                ) {
                    // All Permissions Granted
                    // insertDummyContact();
                    // openCamera()
                    initGoogleAPIClient()
                    showSettingDialog()

                } else {
                    // Permission Denied
                    Toast.makeText(this, "Some Permission is Denied", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }// other 'case' lines to check for other
        // permissions this app might request
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
                gettingLocation()
            }
        } else if (requestCode == REQUEST_VISIT_REPORT) {
            if (resultCode == Activity.RESULT_OK) {
              var typeId =  data!!.getStringExtra("type_id")
              var desc =  data!!.getStringExtra("desc")
                dialog = DialogClass.progressDialog(this)
                submitCheckout(typeId!!,desc!!)
            }
        }
        else if (requestCode == 111) {
            if (resultCode == Activity.RESULT_OK) {
              var updated =  data!!.getStringExtra("updated")
                if(updated=="1")
                     getCustomerInfo("","")
            }
        }
    }


    /* Show Location Access Dialog */
    private fun showSettingDialog() {
        val locationRequest = LocationRequest.create()
        locationRequest.priority =
            LocationRequest.PRIORITY_HIGH_ACCURACY//Setting priotity of Location request to high
        locationRequest.interval = (30 * 1000).toLong()
        locationRequest.fastestInterval =
            (5 * 1000).toLong()//5 sec Time interval for location update
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        builder.setAlwaysShow(true) //this is the key ingredient to show dialog always when GPS is off

        val result =
            LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient!!, builder.build())
        result.setResultCallback { result ->
            val status = result.status
            val state = result.locationSettingsStates
            when (status.statusCode) {
                LocationSettingsStatusCodes.SUCCESS ->

                    gettingLocation()
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->

                    // Location settings are not satisfied. But could be fixed by showing the user
                    // a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        status.startResolutionForResult(
                            this@CounterDashboardActivity,
                            REQUEST_CHECK_SETTINGS
                        )
                    } catch (e: IntentSender.SendIntentException) {
                        e.printStackTrace()
                        // Ignore the error.
                    }

                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                }
            }// All location settings are satisfied. The client can initialize location
            // requests here.
            // updateGPSStatus("GPS is Enabled in your device");
            // Location settings are not satisfied. However, we have no way to fix the
            // settings so we won't show the dialog.
        }
    }

    lateinit var gpsTracker: GPSTracker

    private fun gettingLocation() {
        dialog = DialogClass.progressDialog(this)

        Handler().postDelayed({
            gpsTracker = GPSTracker(this@CounterDashboardActivity)
            gpsTracker.getLongitude()

            if (gpsTracker.getLatitude() == 0.0) {
                gettingLocation()
            } else {
                latitude = gpsTracker.getLatitude().toString()
                longitude = gpsTracker.getLongitude().toString()
                if (checkInOrOut == "in")
                    submitCheckin()
                else if (checkInOrOut == "out") {
                    dialog.dismiss()
                    val intent = Intent(this, VisitReportActivity::class.java)
                    intent.putExtra("checkin", "y")
                    intent.putExtra("isLead", isLead)
                    startActivityForResult(intent, REQUEST_VISIT_REPORT)
                } else
                    alertDialog(
                        "Update Location",
                        "Do you want to update customer location?",
                        this@CounterDashboardActivity
                    )

            }
        }, 2000)
    }

    fun alertDialog(
        title: String,
        description: String,
        activity: Activity
    ) {

        val builder = AlertDialog.Builder(activity)

        builder.setMessage(description)
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                updateCustomerLocation()
            }
            .setNegativeButton("No") { dialog, id ->

            }

        val alert = builder.create()
        //Setting the title manually
        alert.setTitle(title)
        alert.show()
    }


    /* Initiate Google API Client  */
    private fun initGoogleAPIClient() {
        //Without Google API Client Auto Location Dialog will not work
        mGoogleApiClient = GoogleApiClient.Builder(this@CounterDashboardActivity)
            .addApi(LocationServices.API)
            .build()
        mGoogleApiClient!!.connect()
    }

    private fun checkIn() {
        /* cardCheckIn.setCardBackgroundColor(Color.parseColor("#9c9c9c"))
         cardCheckOUt.setCardBackgroundColor(Color.parseColor("#ffffff"))
         cardOrders.setCardBackgroundColor(Color.parseColor("#ffffff"))
         cardSales.setCardBackgroundColor(Color.parseColor("#ffffff"))
         cardReport.setCardBackgroundColor(Color.parseColor("#ffffff"))
         cardCoupon.setCardBackgroundColor(Color.parseColor("#ffffff"))*/

        linearIn.visibility = View.VISIBLE

        if (!isLead) {
            linearOrder.visibility = View.GONE
            linearSales.visibility = View.GONE
            linearScan.visibility = View.GONE
        } else {
            linearOrder.visibility = View.VISIBLE
            linearSales.visibility = View.VISIBLE
            linearScan.visibility = View.VISIBLE
        }

        linearOut.visibility = View.GONE
        linearReport.visibility = View.GONE

        tvIn.setTextColor(Color.parseColor("#70000000"))

        if (!isLead) {
            tvOrder.setTextColor(Color.parseColor("#000000"))
            tvSales.setTextColor(Color.parseColor("#000000"))
            tvScan.setTextColor(Color.parseColor("#000000"))
        } else {
            tvOrder.setTextColor(Color.parseColor("#70000000"))
            tvSales.setTextColor(Color.parseColor("#70000000"))
            tvScan.setTextColor(Color.parseColor("#70000000"))
        }
        tvReport.setTextColor(Color.parseColor("#000000"))
        tvOut.setTextColor(Color.parseColor("#000000"))

        cardOrder.setCardBackgroundColor(Color.parseColor("#FFF212"))
        cardPointCollection.setCardBackgroundColor(Color.parseColor("#FFF212"))
        tvCheckIn.text = "Check Out"
        carddraft.visibility = View.VISIBLE

        Glide.with(this@CounterDashboardActivity).load(
            StaticSharedpreference.getInfo(
                Constant.CHECKIN_IMAGE,
                this@CounterDashboardActivity
            )
        ).centerCrop().into(imgShop)

        println("imageee==check22=="+intent.getStringExtra("image"))
        println("imageee==checkin=="+StaticSharedpreference.getInfo(Constant.CHECKIN_IMAGE, this@CounterDashboardActivity))



        cardPointCollection.isClickable = true
        cardCheckOUt.isClickable = true
        cardSales.isClickable = true
        cardOrder.isClickable = true
        cardCoupon.isClickable = true
        cardReport.isClickable = true
    }

    private fun checkOut() {
        /* cardCheckIn.setCardBackgroundColor(Color.parseColor("#ffffff"))
         cardCheckOUt.setCardBackgroundColor(Color.parseColor("#626262"))
         cardSales.setCardBackgroundColor(Color.parseColor("#626262"))
         cardOrders.setCardBackgroundColor(Color.parseColor("#626262"))
         cardReport.setCardBackgroundColor(Color.parseColor("#626262"))
         cardCoupon.setCardBackgroundColor(Color.parseColor("#626262"))*/


        linearIn.visibility = View.GONE
        linearOrder.visibility = View.VISIBLE
        linearOut.visibility = View.VISIBLE
        linearSales.visibility = View.VISIBLE
        linearScan.visibility = View.VISIBLE
        linearReport.visibility = View.VISIBLE

        tvIn.setTextColor(Color.parseColor("#000000"))
        tvOrder.setTextColor(Color.parseColor("#70000000"))
        tvReport.setTextColor(Color.parseColor("#70000000"))
        tvSales.setTextColor(Color.parseColor("#70000000"))
        tvScan.setTextColor(Color.parseColor("#70000000"))
        tvOut.setTextColor(Color.parseColor("#70000000"))

        cardPointCollection.setCardBackgroundColor(Color.parseColor("#e7e7e7"))
        cardOrder.setCardBackgroundColor(Color.parseColor("#e7e7e7"))
        cardCheckIn.setCardBackgroundColor(Color.parseColor("#25B700"))


        cardCheckOUt.isClickable = false
        cardOrder.isClickable = false
        cardPointCollection.isClickable = false
        cardReport.isClickable = false
        cardSales.isClickable = false
        cardCoupon.isClickable = false
    }

    override fun onBackPressed() {

        if (isBackHome || intent.getStringExtra("checkInId") != "") {
            MainActivity.tabPosition = 0
            startActivity(Intent(this@CounterDashboardActivity, MainActivity::class.java))
            finishAffinity()

        } else
            super.onBackPressed()

    }


}
