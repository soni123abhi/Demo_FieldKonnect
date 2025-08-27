package com.exp.clonefieldkonnect.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.exp.import.Utilities
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.connection.APIResultLitener
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.fragment.*
import com.exp.clonefieldkonnect.helper.Constant
import com.exp.clonefieldkonnect.helper.DialogClass
import com.exp.clonefieldkonnect.helper.StaticSharedpreference
import com.exp.clonefieldkonnect.worker.WorkerLocation
import com.bumptech.glide.Glide
import com.exp.clonefieldkonnect.model.*
import com.exp.clonefieldkonnect.worker.LocationForegroundService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.navigation.NavigationView
import com.google.gson.JsonObject
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), View.OnClickListener,
    DashBoardNewDesingFragment.PunchInDetails {

    lateinit var linearTop: CardView
    lateinit var relativeProfile: RelativeLayout
    lateinit var relativeOrderHistory: RelativeLayout
    lateinit var relativeLogout: RelativeLayout
    lateinit var relativemspActivity: RelativeLayout
    lateinit var imgHome: ImageView

    lateinit var imgState: ImageView
    lateinit var imgBeat: ImageView
    lateinit var imgAtten: ImageView

    lateinit var imgCStatement: ImageView
    lateinit var imgCBeat: ImageView
    lateinit var imgCLead: ImageView
    lateinit var imgmarkettt: ImageView
    lateinit var imgCHome: ImageView
    lateinit var imgLeads: ImageView
    lateinit var imgmarket: ImageView
    var summary = ""

    lateinit var tvState: TextView
    lateinit var latitude: String
    lateinit var longitude: String
    lateinit var tvBeat: TextView
    lateinit var tvAtten: TextView
    lateinit var tvmarket: TextView
    lateinit var tvHome: TextView
    lateinit var tvLeads: TextView
    lateinit var tvPaymentRec: TextView
    lateinit var tvMobile: TextView
    lateinit var tvName: TextView
    lateinit var fragment: AttendanceFragment
    lateinit var relativeHome: RelativeLayout
    lateinit var relativeLeads: RelativeLayout
    lateinit var relativemarketing: RelativeLayout
    lateinit var relativeBeats: RelativeLayout
    lateinit var relativeStatement: RelativeLayout
    lateinit var imgProfileHeader: CircleImageView
    lateinit var imgProfile: CircleImageView
    lateinit var tvNameHeader: TextView
    lateinit var tvPunch: TextView
    lateinit var tvCardPunch: TextView
    lateinit var tvVersion: TextView
    lateinit var cardPunch: CardView
    lateinit var cardBack: CardView
    lateinit var cardBurger: CardView
    lateinit var cardleave: CardView
    lateinit var linearCustomer: LinearLayout
    lateinit var linearmarketing: LinearLayout
    lateinit var linearProfile: LinearLayout
    lateinit var relativeReport: LinearLayout
    lateinit var relativecatelog: LinearLayout
    lateinit var relativebeat: LinearLayout
    var  customerVisitFlag  :String = ""
    var  customer_id  :String = ""
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private val permissionId = 2
    var comb_off_ball = ""
    var working_type_send= ""
    var punchin_flag_send = ""
    var notification_module = ""

    //    lateinit var relativeNotification: LinearLayout
    lateinit var nav_view: NavigationView
    lateinit var three_dot: CardView

    companion object {
        lateinit var drawerLayout: DrawerLayout
        var tabPosition = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLocation()
        customerVisitFlag = intent.getStringExtra("CustomerVisit") ?: "default_value"
        customer_id = intent.getStringExtra("customerId") ?: ""

//        println("Received module in onNewIntentoncreateeee:"+ intent.getStringExtra("module"))

        notification_module = StaticSharedpreference.getInfo(Constant.NOTIFICATION_TYPE,this@MainActivity).toString()
        println("Received module in onNewIntentoncreateeee:"+notification_module)

        if (notification_module.isNotEmpty()){
            if (notification_module.equals("opportunity")){
                StaticSharedpreference.saveInfo(Constant.NOTIFICATION_TYPE,"",this@MainActivity)
                startActivity(Intent(this@MainActivity,OpportunityViewActivity::class.java))
            }else if (notification_module.equals("lead")){
                StaticSharedpreference.saveInfo(Constant.NOTIFICATION_TYPE,"",this@MainActivity)
                startActivity(Intent(this@MainActivity,LeadActivity::class.java))
            }else if (notification_module.equals("task")){
                StaticSharedpreference.saveInfo(Constant.NOTIFICATION_TYPE,"",this@MainActivity)

                var intent = Intent(this, TaskActivity::class.java)
                intent.putExtra("notification_tag",notification_module)
                startActivity(intent)

            }else if (notification_module.equals("task_management")){
                StaticSharedpreference.saveInfo(Constant.NOTIFICATION_TYPE,"",this@MainActivity)

                var intent = Intent(this, TaskActivity::class.java)
                intent.putExtra("notification_tag",notification_module)
                startActivity(intent)

            }
        }



        println("customerVisitFlag=="+customerVisitFlag+"<<"+ tabPosition)



        if (customerVisitFlag == "CustomerVisit"){
            println("customerVisitFlag=1="+customerVisitFlag+"<<"+ tabPosition)
            linearTop.visibility = View.VISIBLE
            if (tabPosition != 1)
                goToFragment(CustomerFragment(relativeHome, three_dot), true)
            cardBack.visibility = View.VISIBLE
            cardPunch.visibility = View.GONE
            tvPunch.visibility = View.GONE
            cardBurger.visibility = View.GONE
            cardleave.visibility = View.GONE
            allInActive()
            imgCLead.visibility = View.VISIBLE
            tvAtten.setTextColor(Color.parseColor("#00aadb"))
            imgAtten.setImageDrawable(resources.getDrawable(R.drawable.ic_lead))
            imgAtten.setColorFilter(ContextCompat.getColor(this@MainActivity, R.color.colur_dark_blue))
            tabPosition = 1
        }else if(customerVisitFlag == "Tour Plan"){
            println("customerVisitFlag=2="+customerVisitFlag+"<<"+ tabPosition)
            if (tabPosition != 2)
                goToFragment(ReportFragment(linearTop, tabPosition,customerVisitFlag), false)
            allInActive()
            cardBurger.visibility = View.GONE
            cardBack.visibility = View.VISIBLE
            cardPunch.visibility = View.GONE
            tvPunch.visibility = View.GONE
            cardleave.visibility = View.GONE

        }else if(customerVisitFlag == "Expense"){
            println("customerVisitFlag=3="+customerVisitFlag+"<<"+ tabPosition)
            if (tabPosition != 2)
                goToFragment(ReportFragment(linearTop, tabPosition,customerVisitFlag), false)
            allInActive()
            cardBurger.visibility = View.GONE
            cardBack.visibility = View.VISIBLE
            cardPunch.visibility = View.GONE
            tvPunch.visibility = View.GONE
            cardleave.visibility = View.GONE

        }else if(customerVisitFlag == "MSP Activity"){
            println("customerVisitFlag=4="+customerVisitFlag+"<<"+ tabPosition)
            if (tabPosition != 2)
                goToFragment(ReportFragment(linearTop, tabPosition,customerVisitFlag), false)
            allInActive()
            cardBurger.visibility = View.GONE
            cardBack.visibility = View.VISIBLE
            cardPunch.visibility = View.GONE
            tvPunch.visibility = View.GONE
            cardleave.visibility = View.GONE

        }else if (customerVisitFlag == "customer_order_his"){
            println("customerVisitFlag=5="+customerVisitFlag+"<<"+ tabPosition)
//            linearTop.visibility = View.VISIBLE
            println("AKAKKAKAKA==$11")
            if (tabPosition != 3)
                println("AKAKKAKAKA==$22")
            goToFragment(OrderListFragment(relativeHome, three_dot,customer_id), true)
            cardBack.visibility = View.VISIBLE
            cardPunch.visibility = View.GONE
            tvPunch.visibility = View.GONE
            cardBurger.visibility = View.GONE
            cardleave.visibility = View.GONE
            allInActive()
            imgCStatement.visibility = View.VISIBLE
            tvState.setTextColor(Color.parseColor("#00aadb"))
            imgState.setImageDrawable(resources.getDrawable(R.drawable.ic_statement_new))
            imgState.setColorFilter(ContextCompat.getColor(this@MainActivity, R.color.colur_dark_blue))
            tabPosition = 3
        }
    }


    private fun getLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    val location: Location? = task.result
                    if (location != null) {
//                        val geocoder = Geocoder(this, Locale.getDefault())
//                        val list: List<Address> = geocoder.getFromLocation(location.latitude, location.longitude, 1)!!
                        latitude = location.latitude.toString()
                        longitude = location.longitude.toString()
                        println("location.latitude ${location.latitude},${location.longitude}")
                    }
                }
            } else {
                Toast.makeText(this, "Please turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }
    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }
    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }
    private fun requestPermissions() {
        val permissions = mutableListOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_PHONE_NUMBERS,
            Manifest.permission.CALL_PHONE
            )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.POST_NOTIFICATIONS)
        }

        ActivityCompat.requestPermissions(
            this,
            permissions.toTypedArray(),
            permissionId
        )
    }
    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == permissionId) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLocation()
            }
        }
    }


    private fun initViews() {
        linearTop = findViewById(R.id.linearTop)
        three_dot = findViewById(R.id.three_dot)
        nav_view = findViewById(R.id.nav_view)
//        relativeNotification = findViewById(R.id.relativeNotification)
        relativeReport = findViewById(R.id.relativeReport)
        relativecatelog = findViewById(R.id.relativecatelog)
        relativebeat = findViewById(R.id.relativebeat)
        linearProfile = findViewById(R.id.linearProfile)
        tvVersion = findViewById(R.id.tvVersion)
        linearCustomer = findViewById(R.id.linearCustomer)
        linearmarketing = findViewById(R.id.linearmarketing)
        cardBurger = findViewById(R.id.cardBurger)
        cardBack = findViewById(R.id.cardBack)
        cardPunch = findViewById(R.id.cardPunch)
        cardleave = findViewById(R.id.cardleave)
        tvCardPunch = findViewById(R.id.tvCardPunch)
        tvPunch = findViewById(R.id.tvPunch)
        tvNameHeader = findViewById(R.id.tvNameHeader)
        imgProfileHeader = findViewById(R.id.imgProfileHeader)
        imgProfile = findViewById(R.id.imgProfile)
        imgCStatement = findViewById(R.id.imgCStatement)
        imgCBeat = findViewById(R.id.imgCBeat)
        imgCLead = findViewById(R.id.imgCLead)
        imgmarkettt = findViewById(R.id.imgmarkettt)
        imgCHome = findViewById(R.id.imgCHome)
        relativeStatement = findViewById(R.id.relativeStatement)
        relativeBeats = findViewById(R.id.relativeBeats)
        relativeLeads = findViewById(R.id.relativeLeads)
        relativemarketing = findViewById(R.id.relativemarketing)
        relativeHome = findViewById(R.id.relativeHome)
        imgState = findViewById(R.id.imgState)
        imgBeat = findViewById(R.id.imgBeat)
        imgAtten = findViewById(R.id.imgLeads)
        imgmarket = findViewById(R.id.imgmarket)
        tvState = findViewById(R.id.tvState)
        tvBeat = findViewById(R.id.tvBeat)
        tvAtten = findViewById(R.id.tvLeads)
        tvmarket = findViewById(R.id.tvmarket)
        tvHome = findViewById(R.id.tvHome)
        tvMobile = findViewById(R.id.tvMobile)
        tvName = findViewById(R.id.tvName)
        drawerLayout = findViewById(R.id.drawerLayout)
        relativeProfile = findViewById(R.id.relativeProfile)
        relativeOrderHistory = findViewById(R.id.relativeOrderHistory)
        imgHome = findViewById(R.id.imgHome)
        relativeLogout = findViewById(R.id.relativeLogout)
        relativemspActivity = findViewById(R.id.relativemspActivity)
        fragment = AttendanceFragment()

        relativeReport.setOnClickListener(this)
        relativecatelog.setOnClickListener(this)
        relativebeat.setOnClickListener(this)
        relativeProfile.setOnClickListener(this)
        relativeOrderHistory.setOnClickListener(this)
        relativeLogout.setOnClickListener(this)
        relativemspActivity.setOnClickListener(this)
        relativeHome.setOnClickListener(this)
        relativeLeads.setOnClickListener(this)
        relativemarketing.setOnClickListener(this)
        relativeBeats.setOnClickListener(this)
        relativeStatement.setOnClickListener(this)
        cardPunch.setOnClickListener(this)
        cardBack.setOnClickListener(this)
        linearCustomer.setOnClickListener(this)
        linearmarketing.setOnClickListener(this)
        cardBurger.setOnClickListener(this)
        linearProfile.setOnClickListener(this)
        cardleave.setOnClickListener(this)
//        relativeNotification.setOnClickListener(this)

        var divisoin_id = StaticSharedpreference.getInfo(Constant.DIVISION_ID, this@MainActivity).toString()
        println("divisoin_id=="+divisoin_id)

//        if (divisoin_id.equals("13")){
//        if (divisoin_id.equals("10") || divisoin_id.equals("18")){
            relativemarketing.visibility = View.VISIBLE
            relativeLeads.visibility = View.GONE
//        }else{
//            relativemarketing.visibility = View.GONE
//            relativeLeads.visibility = View.VISIBLE
//        }


        getUserSataus()

        tvMobile.text = StaticSharedpreference.getInfo(Constant.MOBILE, this)
        tvNameHeader.text = StaticSharedpreference.getInfo(Constant.FIRM_NAME, this@MainActivity)

        Glide.with(this).load(ApiClient.BASE_IMAGE_URL+StaticSharedpreference.getInfo(Constant.PROFILE_IMAGE, this@MainActivity)).into(imgProfileHeader)
        Glide.with(this).load(ApiClient.BASE_IMAGE_URL+StaticSharedpreference.getInfo(Constant.PROFILE_IMAGE, this@MainActivity)).into(imgProfile)


        val tabpostion = StaticSharedpreference.getInfo(Constant.TabPosition, this).toString()
        tabPosition = if (tabpostion.isNotEmpty()) {
            tabpostion.toInt()
        } else {
            0
        }
        println("tabpostiontabpostion"+tabPosition)
//        println("punchhhhhhhh"+StaticSharedpreference.getInfo(Constant.IS_PUNCH_IN, this).toString())

        if (StaticSharedpreference.getInfo(Constant.IS_PUNCH_IN, this) == "true") {
            if (StaticSharedpreference.getBoolean(Constant.todayBeatSchedule, this)){
                relativebeat.visibility = View.VISIBLE
            }else if (StaticSharedpreference.getBoolean(Constant.beatUser, this)){
                relativebeat.visibility = View.VISIBLE
            }else{
                relativebeat.visibility = View.GONE
            }
        } else {
            relativebeat.visibility = View.GONE
        }


        println("customerVisitFlag=tbbb1="+customerVisitFlag+"<<"+ tabPosition)

        if (tabPosition == 0) {
            cardBack.visibility = View.GONE
            println("AKAKKAKAKA==$33")
            cardPunch.visibility = View.VISIBLE
            cardleave.visibility = View.VISIBLE
            tvPunch.visibility = View.VISIBLE
            cardBurger.visibility = View.VISIBLE
            var dashBoardNewFragment: DashBoardNewDesingFragment = DashBoardNewDesingFragment()
            dashBoardNewFragment.punchInDetails(this)
            goToFragment(dashBoardNewFragment, false)
        } else if (tabPosition == 2) {
            cardBack.visibility = View.VISIBLE
            cardPunch.visibility = View.GONE
            tvPunch.visibility = View.GONE
            cardBurger.visibility = View.GONE
            cardleave.visibility = View.GONE
            allInActive()
            tvBeat.setTextColor(Color.parseColor("#000000"))
            imgBeat.setImageDrawable(resources.getDrawable(R.drawable.ic_beat_new))
            imgBeat.setColorFilter(ContextCompat.getColor(this@MainActivity, R.color.colur_dark_blue))
//            startActivity(Intent(this@MainActivity, ReportActivity::class.java))
            var reportFragment: ReportFragment = ReportFragment(linearTop, tabPosition,customerVisitFlag)
            goToFragment(reportFragment,false)
        }else if (tabPosition == 3){
            customer_id = ""
            goToFragment(OrderListFragment(relativeHome, three_dot, customer_id), false)
            cardBack.visibility = View.VISIBLE
            cardPunch.visibility = View.GONE
            tvPunch.visibility = View.GONE
            cardBurger.visibility = View.GONE
            cardleave.visibility = View.GONE
            allInActive()
            imgCStatement.visibility = View.VISIBLE
            tvState.setTextColor(Color.parseColor("#00aadb"))
            imgState.setImageDrawable(resources.getDrawable(R.drawable.ic_statement_new))
            imgState.setColorFilter(ContextCompat.getColor(this@MainActivity, R.color.colur_dark_blue))
            tabPosition = 3
        }
        home()

        three_dot.visibility=View.VISIBLE
        three_dot.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }



    private fun getUserSataus() {
        if (!Utilities.isOnline(this@MainActivity)) {
            return
        }
        ApiClient.getUserSataus(StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this@MainActivity).toString(),
            object : APIResultLitener<UserActiveModel> {
                override fun onAPIResult(response: Response<UserActiveModel>?, errorMessage: String?) {
                    if (response != null && errorMessage == null) {
                        if (response.code() == 200) {
                            println("DATAA=="+response.body()!!.userStatus)
                            if (response.body()!!.status== "success"){
                                if (response.body()!!.userStatus == "Y"){
                                }else{
                                    calllogoutapi()
                                    StaticSharedpreference.deleteSharedPreference(this@MainActivity)
                                    startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                                    finishAffinity()
                                }
                            }
                        } else {

                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())
                                if (response.code() == 401){
                                    Toast.makeText(this@MainActivity,jsonObject.getString("message"), Toast.LENGTH_LONG).show()
                                    StaticSharedpreference.deleteSharedPreference(this@MainActivity)
                                    startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                                    this@MainActivity.finishAffinity()
                                    println("Erroorr=="+jsonObject.getString("message"))
                                }else{
                                    DialogClass.alertDialog(
                                        jsonObject.getString("status"),
                                        jsonObject.getString("message"),
                                        this@MainActivity, false
                                    )
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            })
    }


    fun home(){
        tvHome.setTextColor(Color.parseColor("#00aadb"))
        imgHome.setImageDrawable(resources.getDrawable(R.drawable.ic_home_new))
        imgHome.setColorFilter(ContextCompat.getColor(this@MainActivity, R.color.colur_dark_blue))
    }

    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.relativeBeats -> {
//                customerVisitFlag = "default_value"

//                println("Abhiiii=report="+customerVisitFlag+"<<"+ tabPosition)

                if (tabPosition != 2)
//                    goToFragment(ReportFragment(linearTop, tabPosition,customerVisitFlag), false)
                startActivity(Intent(this@MainActivity, TaskActivity::class.java))
                allInActive()
                cardBurger.visibility = View.GONE
                cardBack.visibility = View.VISIBLE
                cardPunch.visibility = View.GONE
                tvPunch.visibility = View.GONE
                cardleave.visibility = View.GONE

                imgCBeat.visibility = View.VISIBLE
                tvBeat.setTextColor(Color.parseColor("#00aadb"))
                imgBeat.setImageDrawable(resources.getDrawable(R.drawable.ic_beat_new))
                imgBeat.setColorFilter(ContextCompat.getColor(this@MainActivity, R.color.colur_dark_blue))
                tabPosition = 2
            }

            R.id.relativeHome -> {
                if (customerVisitFlag.equals("default_value")){
                    linearTop.visibility = View.VISIBLE
                    three_dot.visibility = View.VISIBLE
                    var dashBoardNewFragment = DashBoardNewDesingFragment()
                    dashBoardNewFragment.punchInDetails(this)
                    if (tabPosition != 0) {
                        goToFragment(dashBoardNewFragment, true)
                        cardBurger.visibility = View.VISIBLE
                        cardBack.visibility = View.GONE
                        println("AKAKKAKAKA==$44")
                        cardPunch.visibility = View.VISIBLE
                        cardleave.visibility = View.VISIBLE
                        allInActive()
                        imgCHome.visibility = View.VISIBLE
                        tvHome.setTextColor(Color.parseColor("#00aadb"))
                        imgHome.setImageDrawable(resources.getDrawable(R.drawable.ic_home_new))
                        imgHome.setColorFilter(ContextCompat.getColor(this@MainActivity, R.color.colur_dark_blue))
                        tabPosition = 0
                    }
                }else{
                    startActivity(Intent(this@MainActivity,MainActivity::class.java))
                }
            }
            R.id.cardBack -> {
                three_dot.visibility=View.VISIBLE
                relativeHome.performClick()
                if (customerVisitFlag == "customer_order_his"){
                    StaticSharedpreference.saveInfo("Order_his","1234",this@MainActivity)
                }
            }
            R.id.linearCustomer -> {
                linearTop.visibility = View.VISIBLE
                if (tabPosition != 1)
                    goToFragment(CustomerFragment(relativeHome, three_dot), true)
                cardBack.visibility = View.VISIBLE
                cardPunch.visibility = View.GONE
                tvPunch.visibility = View.GONE
                cardBurger.visibility = View.GONE
                cardleave.visibility = View.GONE
                allInActive()
                imgCLead.visibility = View.VISIBLE
                tvAtten.setTextColor(Color.parseColor("#00aadb"))
                imgAtten.setImageDrawable(resources.getDrawable(R.drawable.ic_lead))
                imgAtten.setColorFilter(ContextCompat.getColor(this@MainActivity, R.color.colur_dark_blue))
                tabPosition = 1
            }

            R.id.linearmarketing -> {
                println("Abhiiii=mm="+customerVisitFlag+"<<"+ tabPosition)

                linearTop.visibility = View.VISIBLE
                if (tabPosition != 1)
                    goToFragment(MarketIntelligenceFragment(relativeHome, three_dot), true)
                if (customerVisitFlag == "CustomerVisit"){
                    goToFragment(MarketIntelligenceFragment(relativeHome, three_dot), true)
                }
                cardBack.visibility = View.VISIBLE
                cardPunch.visibility = View.GONE
                tvPunch.visibility = View.GONE
                cardBurger.visibility = View.GONE
                cardleave.visibility = View.GONE
                allInActive()
                imgmarkettt.visibility = View.VISIBLE
                tvmarket.setTextColor(Color.parseColor("#00aadb"))
                imgmarket.setImageDrawable(resources.getDrawable(R.drawable.ic_lead))
                imgmarket.setColorFilter(ContextCompat.getColor(this@MainActivity, R.color.colur_dark_blue))
                tabPosition = 1
            }

            R.id.relativeStatement -> {
                linearTop.visibility = View.VISIBLE
                if (tabPosition != 3)
                    customer_id = ""
                    goToFragment(OrderListFragment(relativeHome, three_dot, customer_id), false)
                cardBack.visibility = View.VISIBLE
                cardPunch.visibility = View.GONE
                tvPunch.visibility = View.GONE
                cardBurger.visibility = View.GONE
                cardleave.visibility = View.GONE
                allInActive()
                imgCStatement.visibility = View.VISIBLE
                tvState.setTextColor(Color.parseColor("#00aadb"))
                imgState.setImageDrawable(resources.getDrawable(R.drawable.ic_statement_new))
                imgState.setColorFilter(ContextCompat.getColor(this@MainActivity, R.color.colur_dark_blue))
                tabPosition = 3
            }
            R.id.cardPunch -> {
                startActivity(Intent(this, AttendanceActivity::class.java)
                    .putExtra("working_type_",working_type_send)
                    .putExtra("punchin_flag_",punchin_flag_send))
            }
            R.id.linearProfile -> {
                drawerLayout.openDrawer(GravityCompat.START)
            }
            R.id.relativeLogout -> {
                calllogoutapi()
                StaticSharedpreference.deleteSharedPreference(this@MainActivity)
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finishAffinity()
            }
            R.id.relativemspActivity -> {
                drawerLayout.closeDrawer(GravityCompat.START)
                customerVisitFlag = "MSP Activity"
                if (tabPosition != 2)
                    goToFragment(ReportFragment(linearTop, tabPosition,customerVisitFlag), false)
                allInActive()
                cardBurger.visibility = View.GONE
                cardBack.visibility = View.VISIBLE
                cardPunch.visibility = View.GONE
                tvPunch.visibility = View.GONE
                cardleave.visibility = View.GONE

                imgCBeat.visibility = View.VISIBLE
                tvBeat.setTextColor(Color.parseColor("#00aadb"))
                imgBeat.setImageDrawable(resources.getDrawable(R.drawable.ic_beat_new))
                imgBeat.setColorFilter(ContextCompat.getColor(this@MainActivity, R.color.colur_dark_blue))
                tabPosition = 2
//                calllogoutapi()
//                StaticSharedpreference.deleteSharedPreference(this@MainActivity)
//                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
//                finishAffinity()
            }
            R.id.relativeOrderHistory -> {
                drawerLayout.closeDrawer(GravityCompat.START)
                relativeStatement.performClick()
            }
            R.id.relativeReport -> {
                drawerLayout.closeDrawer(GravityCompat.START)
                customerVisitFlag = "default_value"
                tabPosition = 2
                goToFragment(ReportFragment(linearTop, tabPosition,customerVisitFlag), false)
                allInActive()
                cardBurger.visibility = View.GONE
                cardBack.visibility = View.VISIBLE
                cardPunch.visibility = View.GONE
                tvPunch.visibility = View.GONE
                cardleave.visibility = View.GONE

//                imgCBeat.visibility = View.VISIBLE
//                tvBeat.setTextColor(Color.parseColor("#00aadb"))
//                imgBeat.setImageDrawable(resources.getDrawable(R.drawable.ic_beat_new))
//                imgBeat.setColorFilter(ContextCompat.getColor(this@MainActivity, R.color.colur_dark_blue))
//                tabPosition = 2
            }
            R.id.relativecatelog -> {
//                Toast.makeText(this@MainActivity,"Working on it..!!",Toast.LENGTH_SHORT).show()
                drawerLayout.closeDrawer(GravityCompat.START)
                startActivity(Intent(this@MainActivity, ProductCatalogActivity::class.java))
            }
            R.id.relativebeat -> {
                drawerLayout.closeDrawer(GravityCompat.START)
                goToFragment(BeatFragment(relativeHome, three_dot), true)
                cardBack.visibility = View.VISIBLE
                cardPunch.visibility = View.GONE
                tvPunch.visibility = View.GONE
                cardBurger.visibility = View.GONE
                cardleave.visibility = View.GONE
                allInActive()
                imgCLead.visibility = View.VISIBLE
                tvAtten.setTextColor(Color.parseColor("#00aadb"))
                imgAtten.setImageDrawable(resources.getDrawable(R.drawable.ic_lead))
                imgAtten.setColorFilter(ContextCompat.getColor(this@MainActivity, R.color.colur_dark_blue))
                tabPosition = 1
            }
            R.id.cardleave ->{
                showleavepopupdialog()
            }
          /*  R.id.relativeNotification -> {
                drawerLayout.closeDrawer(GravityCompat.START)
//                startActivity(Intent(this@MainActivity, NotificationActivity::class.java))
            }*/

        }
    }


    private fun calllogoutapi() {
        if (!Utilities.isOnline(this)) {
            return
        }
        var dialog = DialogClass.progressDialog(this)
        val queryParams = HashMap<String, String>()
        ApiClient.getlogout(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this).toString(), queryParams,
            object : APIResultLitener<AttendanceSubmitModel> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onAPIResult(response: Response<AttendanceSubmitModel>?, errorMessage: String?) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {

                            Toast.makeText(this@MainActivity,response.body()!!.message, Toast.LENGTH_LONG).show()

                        } else {

                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    this@MainActivity, false
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                    else {
                        Toast.makeText(this@MainActivity,"No Record Found", Toast.LENGTH_LONG).show()
                    }
                }
            })
    }




    @SuppressLint("MissingInflatedId")
    private lateinit var alertDialog: androidx.appcompat.app.AlertDialog
    @SuppressLint("MissingInflatedId")
    private fun showleavepopupdialog() {

        val builder = androidx.appcompat.app.AlertDialog.Builder(this@MainActivity)
        val inflater = this@MainActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.popup_leave_layout, null)
        builder.setCancelable(false)

        val leaveTypes = listOf("First Half Leave", "Second Half Leave","Full Day Leave")
        val leavebalTypes = listOf("Leave Balance", "Comp-off Balance")
        var start_date = ""
        var end_date = ""
        var leave_type = ""
        var bal_type = ""


        val cardbtn_submit: CardView = view.findViewById(R.id.cardbtn_submit)
        val cardFrom: LinearLayout = view.findViewById(R.id.cardFrom)
        val tvFrom: TextView = view.findViewById(R.id.tvFrom)
        val cardTo: LinearLayout = view.findViewById(R.id.cardTo)
        val tvTo: TextView = view.findViewById(R.id.tvTo)
        val img_close: ImageView = view.findViewById(R.id.img_close)
        val edtselectleave: AutoCompleteTextView = view.findViewById(R.id.edtselectleave)
        val edtselectleavebal: AutoCompleteTextView = view.findViewById(R.id.edtselectleavebal)
        val edt_leaveremark: EditText = view.findViewById(R.id.edt_leaveremark)
        val tv_leave_bal: TextView = view.findViewById(R.id.tv_leave_bal)
        val tv_comb_bal: TextView = view.findViewById(R.id.tv_comb_bal)

        getleavebal(tv_leave_bal,tv_comb_bal)

        cardFrom.setOnClickListener {
            Utilities.datePickerFuture2(tvFrom,this@MainActivity)
        }
        cardTo.setOnClickListener {
            Utilities.datePickerFuture2(tvTo,this@MainActivity)
        }
        edtselectleave.setOnClickListener {
            edtselectleave.showDropDown()
            val aa = ArrayAdapter(this@MainActivity, android.R.layout.simple_list_item_1, leaveTypes)
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            edtselectleave.setAdapter(aa)
        }
        edtselectleave.setOnItemClickListener { adapterView, view, i, l ->
            println("from=="+leaveTypes.get(i))
            leave_type = leaveTypes.get(i)
        }

        edtselectleavebal.setOnClickListener {
            edtselectleavebal.showDropDown()
            val aa = ArrayAdapter(this@MainActivity, android.R.layout.simple_list_item_1, leavebalTypes)
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            edtselectleavebal.setAdapter(aa)
        }
        edtselectleavebal.setOnItemClickListener { adapterView, view, i, l ->
            val selectedItem = leavebalTypes[i]
            if (selectedItem == "Comp-off Balance" && comb_off_ball == "0") {
                Toast.makeText(this@MainActivity, "Comb-off Balance is 0. You are not allowed to select comb-off type.", Toast.LENGTH_SHORT).show()
                edtselectleavebal.setText("")
            } else {
                bal_type = selectedItem
                println("from== $selectedItem")
                println("from== $comb_off_ball")
            }
        }

        cardbtn_submit.setOnClickListener {
            start_date = Utilities.convertDateFormat(tvFrom.text.toString())
            end_date = Utilities.convertDateFormat(tvTo.text.toString())
            println("from=="+start_date+"To="+end_date+"<<"+edt_leaveremark.text.toString())
            if (start_date.isNullOrEmpty()){
                responsemessage("Please select start date")
            }else if (end_date.isNullOrEmpty()){
                responsemessage("Please select end date")
            }else if (bal_type.isNullOrEmpty()){
                responsemessage("Please select Balance type")
            }else if (leave_type.isNullOrEmpty()){
                responsemessage("Please select leave type")
            }else if (edt_leaveremark.text.toString().isNullOrEmpty()){
                responsemessage("Please enter remark")
            }else{
                submitleave(start_date,end_date,leave_type,bal_type,edt_leaveremark.text.toString(),alertDialog)
            }
        }

        img_close.setOnClickListener {
            alertDialog.dismiss()
        }

        builder.setView(view)
        alertDialog = builder.create()
        alertDialog.show()
    }

    private fun getleavebal(tv_leave_bal: TextView, tv_comb_bal: TextView) {
        /*if (!Utilities.isOnline(this)) {
            return
        }
        var dialog = DialogClass.progressDialog(this)*/

        val queryParams = HashMap<String, String>()
//        queryParams["user_id"] = StaticSharedpreference.getInfo(Constant.USERID,this@MainActivity).toString()

        ApiClient.userleavebal(StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this)
            .toString(),queryParams, object : APIResultLitener<LeaveBalanceModel> {
            override fun onAPIResult(response: Response<LeaveBalanceModel>?, errorMessage: String?) {
//                    dialog.dismiss()
                if (response != null && errorMessage == null) {

                    if (response.code() == 200) {
//                            Toast.makeText(this@MainActivity, response.body()!!.message, Toast.LENGTH_LONG).show()
                        tv_leave_bal.text = "Leave Balance : "+response.body()!!.data!!.leaveBalance.toString()
                        tv_comb_bal.text = "Comp-Off Balance : "+response.body()!!.data!!.combOff.toString()
                        comb_off_ball = response.body()!!.data!!.combOff.toString()
                    } else {

                        val jsonObject: JSONObject
                        try {
                            jsonObject = JSONObject(response.errorBody()!!.string())
                            if (response.code() == 401){
                                Toast.makeText(this@MainActivity,jsonObject.getString("message"), Toast.LENGTH_LONG).show()
                                StaticSharedpreference.deleteSharedPreference(this@MainActivity)
                                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                                this@MainActivity.finishAffinity()
                                println("Erroorr=="+jsonObject.getString("message"))
                            }else{
                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    this@MainActivity,
                                    false)
                            }

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                    }
                } else {
                    //  dialog.dismiss()
                    Toast.makeText(this@MainActivity, resources.getString(R.string.poor_connection), Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun responsemessage(message: String) {
        Toast.makeText(this@MainActivity,message,Toast.LENGTH_SHORT).show()
    }


    private fun submitleave(
        startDate: String,
        endDate: String,
        leaveType: String,
        bal_type: String,
        remark: String,
        alertDialog: androidx.appcompat.app.AlertDialog
    ) {

        if (!Utilities.isOnline(this)) {
            return
        }
        var dialog = DialogClass.progressDialog(this)


        val queryParams = HashMap<String, String>()
        queryParams["user_id"] = StaticSharedpreference.getInfo(Constant.USERID,this@MainActivity).toString()
        queryParams["from_date"] = startDate
        queryParams["to_date"] = endDate
        queryParams["type"] = leaveType
        queryParams["bal_type"] = bal_type
        queryParams["reason"] = remark

        ApiClient.userleavesubmit(StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this)
            .toString(),
            queryParams, object : APIResultLitener<AttendanceSubmitModel> {
                override fun onAPIResult(response: Response<AttendanceSubmitModel>?, errorMessage: String?) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {
                            Toast.makeText(this@MainActivity, response.body()!!.message, Toast.LENGTH_LONG).show()
//                            alertDialog.dismiss()
                            startActivity(Intent(this@MainActivity,MainActivity::class.java))
                        } else {
                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())
                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    this@MainActivity,
                                    false
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                        }
                    } else {
                        //  dialog.dismiss()
                        Toast.makeText(
                            this@MainActivity,
                            resources.getString(R.string.poor_connection),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })
    }




    fun allInActive() {
        tvState.setTextColor(Color.parseColor("#828282"))
        tvAtten.setTextColor(Color.parseColor("#828282"))
        tvmarket.setTextColor(Color.parseColor("#828282"))
        tvHome.setTextColor(Color.parseColor("#828282"))
        tvBeat.setTextColor(Color.parseColor("#828282"))

        imgCBeat.visibility = View.INVISIBLE
        imgCHome.visibility = View.INVISIBLE
        imgCLead.visibility = View.INVISIBLE
        imgmarkettt.visibility = View.INVISIBLE
        imgCStatement.visibility = View.INVISIBLE

        imgBeat.setImageDrawable(resources.getDrawable(R.drawable.ic_beat_new_unselect))
        imgAtten.setImageDrawable(resources.getDrawable(R.drawable.ic_lead_unselect))
        imgmarket.setImageDrawable(resources.getDrawable(R.drawable.ic_lead_unselect))
        imgHome.setImageDrawable(resources.getDrawable(R.drawable.ic_home_new_unselect))
        imgState.setImageDrawable(resources.getDrawable(R.drawable.ic_statement_new_unselect))

        imgBeat.setColorFilter(ContextCompat.getColor(this@MainActivity, R.color.grey_light2))
        imgAtten.setColorFilter(ContextCompat.getColor(this@MainActivity, R.color.grey_light2))
        imgmarket.setColorFilter(ContextCompat.getColor(this@MainActivity, R.color.grey_light2))
        imgHome.setColorFilter(ContextCompat.getColor(this@MainActivity, R.color.grey_light2))
        imgState.setColorFilter(ContextCompat.getColor(this@MainActivity, R.color.grey_light2))
    }

    fun alertDialog(

        activity: Activity

    ) {

        val builder = AlertDialog.Builder(activity)

        builder.setMessage("Do you want to logout?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                logout()
            }
            .setNegativeButton("No") { dialogInterface, i ->


            }

        val alert = builder.create()
        //Setting the title manually
        alert.setTitle("Logout")
        alert.show()
    }

    fun logout() {

        if (!Utilities.isOnline(this@MainActivity)) {
            return
        }
        var dialog = DialogClass.progressDialog(this@MainActivity)
        val queryParams = HashMap<String, String>()

        ApiClient.logout(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this).toString(),
            queryParams,
            object : APIResultLitener<JsonObject> {
                override fun onAPIResult(response: Response<JsonObject>?, errorMessage: String?) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {
                            tabPosition = 0
                            StaticSharedpreference.deleteSharedPreference(this@MainActivity)
                            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                            finishAffinity()
                        } else {

                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                if (response.code() == 401){
                                    Toast.makeText(this@MainActivity,jsonObject.getString("message"), Toast.LENGTH_LONG).show()
                                    StaticSharedpreference.deleteSharedPreference(this@MainActivity)
                                    startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                                    this@MainActivity.finishAffinity()
                                    println("Erroorr=="+jsonObject.getString("message"))
                                }else{
                                    DialogClass.alertDialog(
                                        jsonObject.getString("status"),
                                        jsonObject.getString("message"),
                                        this@MainActivity, false
                                    )
                                }

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            })
    }


    private fun goToFragment(fragment: Fragment, isProduct: Boolean) {

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        if (isProduct)
            fragmentTransaction.replace(R.id.layout, fragment, "orderProduct")
        else
            fragmentTransaction.replace(R.id.layout, fragment, "Fragment")
        fragmentTransaction.commit()
    }

    override fun onResume() {
        super.onResume()
        tvName.text = StaticSharedpreference.getInfo(Constant.FIRM_NAME, this)
        getUserSataus()
        getLocation()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        fragment.onActivityResult(requestCode, resultCode, data)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onPunchInDetails(punchIn: Boolean, punchOut: Boolean, working_type : String,punchin_flag: Boolean,punchin_id:String) {
        println("ABHII=="+punchIn+"<<"+punchOut+"working_type ${working_type},  ${punchin_flag}")
        println("punchin_flag 3 ${punchin_flag}")
        working_type_send = working_type
        punchin_flag_send = punchin_flag.toString()
        if (working_type != "" && punchin_flag.equals(false)){
            if (working_type.equals("First Half Leave")){
                val referenceTime = LocalTime.of(14, 0)

                // Get the current time
                val currentTime = LocalTime.now()
                // Check if the current time is 14:00 or greater
                if (currentTime >= referenceTime) {
                    // Format the current time for display
                    tvPunch.visibility = View.GONE
                    cardPunch.visibility = View.VISIBLE
                    tvCardPunch.text = "Punch In"
                } else {
                    tvPunch.visibility = View.GONE
                    cardPunch.visibility = View.GONE
                    tvCardPunch.text = "Punch In"
                }
            }
            else if (working_type.equals("Second Half Leave")){
                val referenceTime = LocalTime.of(14, 0)

                // Get the current time
                val currentTime = LocalTime.now()

                // Check if the current time is 14:00 or greater
                if (currentTime >= referenceTime) {
                    // Format the current time for display
                    val formatter = DateTimeFormatter.ofPattern("HH:mm")
                    val formattedTime = currentTime.format(formatter)
                    userPunchout(summary,punchIn,punchOut,punchin_id)
                } else {

                }


            }
            else if (working_type.equals("Full Day Leave")){
                tvPunch.text = "End Day"
                cardPunch.visibility = View.GONE
                tvPunch.visibility = View.VISIBLE
                val intent = Intent(this, LocationForegroundService::class.java)
                this.stopService(intent)
            }
        }
        else{
            if (punchIn && punchOut) {
                tvPunch.text = "End Day"
                cardPunch.visibility = View.GONE
                tvPunch.visibility = View.VISIBLE
                val intent = Intent(this, LocationForegroundService::class.java)
                this.stopService(intent)
            }
            else if (punchIn) {
                if (working_type.equals("First Half Leave") || working_type.equals("Second Half Leave") || working_type.equals("Full Day Leave") && punchin_flag.equals(true)){
                    if (working_type.equals("First Half Leave")){
                        val referenceTime = LocalTime.of(14, 0)

                        // Get the current time
                        val currentTime = LocalTime.now()
                        // Check if the current time is 14:00 or greater
                        if (currentTime >= referenceTime) {
                            // Format the current time for display
                            tvPunch.visibility = View.GONE
                            cardPunch.visibility = View.VISIBLE
                            tvCardPunch.text = "Punch In"
                        } else {
                            tvPunch.visibility = View.GONE
                            cardPunch.visibility = View.GONE
                            tvCardPunch.text = "Punch In"
                        }
                    }
                    else if (working_type.equals("Second Half Leave")){
                        val referenceTime = LocalTime.of(14, 0)

                        // Get the current time
                        val currentTime = LocalTime.now()

                        // Check if the current time is 14:00 or greater
                        if (currentTime >= referenceTime) {
                            // Format the current time for display
                            val formatter = DateTimeFormatter.ofPattern("HH:mm")
                            val formattedTime = currentTime.format(formatter)
                            userPunchout(summary,punchIn,punchOut,punchin_id)
                            startActivity(Intent(this@MainActivity,MainActivity::class.java))
                        } else {

                        }
                    }
                    else if (working_type.equals("Full Day Leave")){
                        tvPunch.text = "End Day"
                        cardPunch.visibility = View.GONE
                        tvPunch.visibility = View.VISIBLE
                        val intent = Intent(this, LocationForegroundService::class.java)
                        this.stopService(intent)
                    }
                }else{
                    println("AKAKKAKAKA==$55"+"<<"+customerVisitFlag+"<<"+tabPosition)
                    if (customerVisitFlag == "CustomerVisit" && tabPosition != 0){
                        tvPunch.visibility = View.GONE
                        cardPunch.visibility = View.GONE
                        three_dot.visibility = View.GONE
                    }else if (customerVisitFlag == "default_value" || customerVisitFlag == "MSP Activity" || customerVisitFlag == "Expense"
                        || customerVisitFlag == "CustomerVisit"|| customerVisitFlag == "Tour Plan"){
                        tvCardPunch.text = "Punch Out"
                        tvPunch.visibility = View.VISIBLE
                        cardPunch.visibility = View.VISIBLE
                        three_dot.visibility = View.VISIBLE
                    }
                    else{
                        tvPunch.visibility = View.GONE
                        cardPunch.visibility = View.GONE
                        three_dot.visibility = View.GONE
                    }
                }
            }
            else if (!punchIn && !punchOut) {
                var flag_his = StaticSharedpreference.getInfo("Order_his",this@MainActivity)
                println("AKAKKAKAKA==$666"+"<<"+flag_his+"<<"+customerVisitFlag+"<<"+tabPosition)

                if (flag_his == "1234"){
                    cardPunch.visibility = View.VISIBLE
                    StaticSharedpreference.saveInfo("Order_his","",this@MainActivity)
                }else if (customerVisitFlag == "customer_order_his"){
                    cardPunch.visibility = View.GONE
                }else{
                    if (customerVisitFlag == "CustomerVisit" && tabPosition!= 0){
                        cardPunch.visibility = View.GONE
                    }else{
                        tvPunch.visibility = View.GONE
                        cardPunch.visibility = View.VISIBLE
                        tvCardPunch.text = "Punch In"
                    }
                }
            }

        }
    }

    private fun userPunchout(summary: String, punchIn: Boolean, punchOut: Boolean, punchin_id: String) {

        if (!Utilities.isOnline(this)) {
            return
        }


        var fileToUploadList: MultipartBody.Part?


        val punchInId = RequestBody.create(
            MediaType.parse("text"),
            "" + punchin_id
        )

        val lat = RequestBody.create(
            MediaType.parse("text"),
            "" + latitude
        )

        val long = RequestBody.create(
            MediaType.parse("text"),
            "" + longitude
        )

        val address = RequestBody.create(
            MediaType.parse("text"),
            ""
        )

        val summary = RequestBody.create(
            MediaType.parse("text"),
            summary
        )

        ApiClient.userPunchout(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this).toString(),
            punchInId, lat, long, address, summary,
            object : APIResultLitener<JsonObject> {
                override fun onAPIResult(response: Response<JsonObject>?, errorMessage: String?) {
                    try {
                        //   dialog!!.dismiss()
                    } catch (e: Exception) {
                    }
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {
                            if (punchIn && punchOut) {
                                tvPunch.text = "End Day"
                                cardPunch.visibility = View.GONE
                                tvPunch.visibility = View.VISIBLE
                                val intent = Intent(this@MainActivity, LocationForegroundService::class.java)
                                this@MainActivity.stopService(intent)
                            } else if (punchIn) {
                                if (customerVisitFlag == "default_value"){
                                    tvCardPunch.text = "Punch Out"
                                    tvPunch.visibility = View.VISIBLE
                                    cardPunch.visibility = View.VISIBLE
                                    println("AKAKKAKAKA==$55")
                                    three_dot.visibility = View.VISIBLE
                                }else{
                                    tvPunch.visibility = View.GONE
                                    cardPunch.visibility = View.GONE
                                    three_dot.visibility = View.GONE
                                }
                            }else if (!punchIn && !punchOut) {
                                println("AKAKKAKAKA==$66")
                                var flag_his = StaticSharedpreference.getInfo("Order_his",this@MainActivity)

                                if (flag_his == "1234"){
                                    cardPunch.visibility = View.VISIBLE
                                    StaticSharedpreference.saveInfo("Order_his","",this@MainActivity)
                                }else if (customerVisitFlag == "customer_order_his"){
                                    cardPunch.visibility = View.GONE
                                }else{
                                    if (customerVisitFlag == "CustomerVisit"){
                                        cardPunch.visibility = View.GONE
                                    }else{
                                        tvPunch.visibility = View.GONE
                                        cardPunch.visibility = View.VISIBLE
                                        tvCardPunch.text = "Punch In"
                                    }
                                }
                            }


                        } else {

                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    this@MainActivity,
                                    false
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            })
    }





    private fun setUpWorker(){
        val constraint = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val workerRequest = PeriodicWorkRequest.Builder(WorkerLocation::class.java,15, TimeUnit.MINUTES)
            .setConstraints(constraint)
            .build()
        WorkManager.getInstance().enqueue(workerRequest)
    }

}
