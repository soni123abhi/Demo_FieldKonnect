package com.exp.clonefieldkonnect.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.bumptech.glide.Glide
import com.devstune.searchablemultiselectspinner.SearchableItem
import com.devstune.searchablemultiselectspinner.SearchableMultiSelectSpinner
import com.devstune.searchablemultiselectspinner.SelectionCompleteListener
import com.exp.clonefieldkonnect.activity.AttendanceActivity
import com.exp.clonefieldkonnect.activity.MainActivity
import com.exp.clonefieldkonnect.connection.APIResultLitener
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.helper.Constant
import com.exp.clonefieldkonnect.helper.DialogClass
import com.exp.clonefieldkonnect.helper.GPSTracker
import com.exp.clonefieldkonnect.helper.StaticSharedpreference
import com.exp.clonefieldkonnect.model.*
import com.exp.clonefieldkonnect.worker.LocationForegroundService
import com.exp.clonefieldkonnect.worker.WorkerLocation
import com.exp.import.Utilities
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.google.common.util.concurrent.ListenableFuture
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit


class AttendanceFragment : Fragment(), View.OnClickListener {
    private var items: MutableList<SearchableItem> = ArrayList()

    lateinit var activityLocal: Activity
    private lateinit var rootView: View
    private lateinit var cardPunchIn: CardView
    private lateinit var cardPunchOut: CardView
    val INTENTCAMERA = 4
    private val REQUEST_CHECK_SETTINGS = 0x1
    private var mGoogleApiClient: GoogleApiClient? = null
    lateinit var cameraFile: File
    var punchInOrOut: String = "in"
    var imagePath: String = ""
    lateinit var latitude: String
    lateinit var longitude: String
    lateinit var cardBurger: CardView
    lateinit var cardPunchInMain: CardView
    lateinit var cardPunchOutMain: CardView
    lateinit var cardSubmit: CardView
    lateinit var tvPunchOutDate: TextView
    lateinit var tvPunchOutTime: TextView
    lateinit var tvPunchIn: TextView
    lateinit var imgPunchOut: ImageView
    lateinit var tvPunchInDate: TextView
    lateinit var tvPunchInTime: TextView
    lateinit var tvBeatTitle: TextView
    lateinit var imgPunchIn: ImageView
    lateinit var relativeMainPunch: LinearLayout
    lateinit var linearPlan: LinearLayout
    lateinit var imgCamera: ImageView
    lateinit var tvObjective: TextView
    lateinit var tvType: TextView
    lateinit var tvCity: TextView
    lateinit var tvObjectiveS: TextView
    lateinit var tvTypeS: TextView
    lateinit var tvCityS: TextView
    lateinit var tvDateS: TextView
    lateinit var spinnerPlan: AutoCompleteTextView
    lateinit var spinnerCity: AutoCompleteTextView
    lateinit var spinnerBeat: AutoCompleteTextView
    lateinit var linearSelectedPlan: LinearLayout
    var todayTourId: String = ""
    var selectedcityid: String = ""
    var schedulebeatid: String = ""
    lateinit var actualTourId: String
    var tags = "workLocation"
    var tagCancel = "workLocationCancel"
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var searchText = ""
    var working_type = ""
    var punchin_flag_send = ""
    var tourArr: ArrayList<TourModel> = arrayListOf()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(com.exp.clonefieldkonnect.R.layout.fragment_attendance, container, false)
        activityLocal = activity as AttendanceActivity

        working_type = arguments?.getString("working_type_").toString()
        punchin_flag_send = arguments?.getString("punchin_flag").toString()
        println("punchin_flag 2 ${punchin_flag_send}")
        initViews()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO,
                    Manifest.permission.READ_MEDIA_AUDIO,
                ),
                2
            )
        }else{
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO,
                    Manifest.permission.READ_MEDIA_AUDIO,
                ),
                3
            )
        }
        return rootView
    }

    private fun initViews() {
        linearSelectedPlan = rootView.findViewById(com.exp.clonefieldkonnect.R.id.linearSelectedPlan)
        spinnerCity = rootView.findViewById(com.exp.clonefieldkonnect.R.id.spinnerCity)
        imgPunchIn = rootView.findViewById(com.exp.clonefieldkonnect.R.id.imgPunchIn)
        tvPunchInTime = rootView.findViewById(com.exp.clonefieldkonnect.R.id.tvPunchInTime)
        tvPunchInDate = rootView.findViewById(com.exp.clonefieldkonnect.R.id.tvPunchInDate)
        imgPunchOut = rootView.findViewById(com.exp.clonefieldkonnect.R.id.imgPunchOut)
        tvPunchOutDate = rootView.findViewById(com.exp.clonefieldkonnect.R.id.tvPunchOutDate)
        tvPunchOutTime = rootView.findViewById(com.exp.clonefieldkonnect.R.id.tvPunchOutTime)
        cardPunchInMain = rootView.findViewById(com.exp.clonefieldkonnect.R.id.cardPunchInMain)
        cardPunchOutMain = rootView.findViewById(com.exp.clonefieldkonnect.R.id.cardPunchOutMain)
        cardBurger = rootView.findViewById(com.exp.clonefieldkonnect.R.id.cardBurger)
        relativeMainPunch = rootView.findViewById(com.exp.clonefieldkonnect.R.id.relativeMainPunch)
        imgCamera = rootView.findViewById(com.exp.clonefieldkonnect.R.id.imgCamera)
        cardSubmit = rootView.findViewById(com.exp.clonefieldkonnect.R.id.cardSubmit)
        tvPunchIn = rootView.findViewById(com.exp.clonefieldkonnect.R.id.tvPunchIn)
        spinnerPlan = rootView.findViewById(com.exp.clonefieldkonnect.R.id.spinnerPlan)
        linearPlan = rootView.findViewById(com.exp.clonefieldkonnect.R.id.linearPlan)
        spinnerBeat = rootView.findViewById(com.exp.clonefieldkonnect.R.id.spinnerBeat)

        tvObjective = rootView.findViewById(com.exp.clonefieldkonnect.R.id.tvObjective)
        tvType = rootView.findViewById(com.exp.clonefieldkonnect.R.id.tvType)
        tvCity = rootView.findViewById(com.exp.clonefieldkonnect.R.id.tvCity)

        tvObjectiveS = rootView.findViewById(com.exp.clonefieldkonnect.R.id.tvObjectiveS)
        tvTypeS = rootView.findViewById(com.exp.clonefieldkonnect.R.id.tvTypeS)
        tvCityS = rootView.findViewById(com.exp.clonefieldkonnect.R.id.tvCityS)
        tvDateS = rootView.findViewById(com.exp.clonefieldkonnect.R.id.tvDateS)

        if (punchin_flag_send.equals("true")){
            getPunchin()
        }else{
            getPunchin2()
        }

        spinnerPlan.setOnClickListener {
            spinnerPlan.showDropDown()
        }

        spinnerPlan.setOnItemClickListener { adapterView, view, i, l ->
            if(workTypeArr.get(i).city) {
                spinnerCity.visibility = View.VISIBLE
            }  else{
                spinnerCity.visibility = View.GONE
            }
            if(workTypeArr.get(i).is_beat && StaticSharedpreference.getBoolean(Constant.beatUser, activityLocal)){
                spinnerBeat.visibility = View.VISIBLE
            }else {
                spinnerBeat.visibility = View.GONE
            }
        }

        imgCamera.setOnClickListener(this)
        cardSubmit.setOnClickListener(this)
        cardBurger.setOnClickListener(this)
    }

    override fun onClick(p0: View) {
        when (p0.id) {
            com.exp.clonefieldkonnect.R.id.imgCamera -> {
                imgCamera.isClickable = false
                Handler().postDelayed(
                    Runnable
                    {
                        imgCamera.isClickable = true
                    }, 4 * 1000
                )

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    requestPermissions(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.READ_MEDIA_IMAGES,
                            Manifest.permission.READ_MEDIA_VIDEO,
                            Manifest.permission.READ_MEDIA_AUDIO,
                            ),
                        2
                    )
                }else{
                    requestPermissions(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.READ_MEDIA_IMAGES,
                            Manifest.permission.READ_MEDIA_VIDEO,
                            Manifest.permission.READ_MEDIA_AUDIO,
                        ),
                        3
                    )
                }
            }

            com.exp.clonefieldkonnect.R.id.cardSubmit -> {

                if (punchInOrOut == "in") {

                    if(todayTourId=="" && !StaticSharedpreference.getBoolean(Constant.todayBeatSchedule, activityLocal)
                        && !StaticSharedpreference.getBoolean(Constant.beatUser, activityLocal)){
                        Toast.makeText(activityLocal, "Please Make Today's Tour Plan", Toast.LENGTH_LONG).show()
                        return
                    }
                    else if(spinnerPlan.text.toString()=="" && !StaticSharedpreference.getBoolean(Constant.todayBeatSchedule, activityLocal)){
                        Toast.makeText(activityLocal, "Please select Type", Toast.LENGTH_LONG).show()
                        return
                    }
                    else if (spinnerPlan.text.toString() != "Holiday" && spinnerPlan.text.toString() != "Leave" && !StaticSharedpreference.getBoolean(Constant.todayBeatSchedule, activityLocal)) {
                        if (selectedcityid == "") {
                            Toast.makeText(activityLocal, "Please Select City", Toast.LENGTH_LONG).show()
                            return
                        }
                    }
                }

                /* if (punchInOrOut=="in"&&imagePath == "") {
                     Toast.makeText(activityLocal, "Please take image", Toast.LENGTH_LONG).show()
                 } else {*/

                /*   if (working_type!= null){
                       punchInOrOut = "in"
                   }*/
                if (punchInOrOut == "in") {
                    checkInDialog(activityLocal, "Punch In")
                }else if (punchin_flag_send.equals("true")) {
                    userPunchout(summary)
                }else{
                    userPunchout(summary)
                }
                /* }*/
            }

            com.exp.clonefieldkonnect.R.id.cardBurger -> {
                MainActivity.drawerLayout.openDrawer(GravityCompat.START)
            }
        }
    }


    fun checkInDialog(activity: Activity, tv: String) {

        //Inflate the dialog with custom view
        val mDialogView = LayoutInflater.from(activity).inflate(com.exp.clonefieldkonnect.R.layout.dialog_checkin_summary, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(activity)
            .setView(mDialogView)
            .setTitle("")
        //show dialog
        val mAlertDialog = mBuilder.show()

        var edtCheckIn: EditText = mDialogView.findViewById(com.exp.clonefieldkonnect.R.id.edtCheckIn)
        var tvDetails: TextView = mDialogView.findViewById(com.exp.clonefieldkonnect.R.id.tvDetails)
        var tvBtn: TextView = mDialogView.findViewById(com.exp.clonefieldkonnect.R.id.tvBtn)
        var cardOK: CardView = mDialogView.findViewById(com.exp.clonefieldkonnect.R.id.cardOK)

        tvDetails.text = "$tv Summary"
        tvBtn.text = "$tv"

        cardOK.setOnClickListener {
            //dismiss dialog
            mAlertDialog.dismiss()
            if (edtCheckIn.text.toString() != "") {
                if (tv == "Punch In") {
                    punchIn(edtCheckIn.text.toString())
                } else {
                    summary = edtCheckIn.text.toString()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        requestPermissions(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            ),
                            2
                        )
                    }else{
                        requestPermissions(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            ),
                            3
                        )
                    }
                }
            } else {
                Toast.makeText(activityLocal, "Please enter $tv summary", Toast.LENGTH_LONG).show()
            }
        }

        mAlertDialog.setCancelable(false)
        mAlertDialog.setCanceledOnTouchOutside(false)
        //  mBuilder.se
        // mBuilder.setCanceledOnTouchOutside(false)
        //cancel button click of custom layout
    }

    private fun getPunchin() {

        if (!Utilities.isOnline(activityLocal)) {
            return
        }
        var dialog = DialogClass.progressDialog(activityLocal)
        val queryParams = HashMap<String, String>()
        queryParams["page"] = "1"
        queryParams["pageSize"] = "1"

        ApiClient.getPunchin(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, activityLocal).toString(),
            queryParams,
            object : APIResultLitener<JsonObject> {
                override fun onAPIResult(response: Response<JsonObject>?, errorMessage: String?) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {
                            val c = Calendar.getInstance().time

                            val df = SimpleDateFormat("yyyy-MM-dd")
                            val formattedDate = df.format(c)
                            val gson = Gson()
                            val listType = object : TypeToken<ArrayList<LastPunchInModel>>() {}.type

                            var punchInArr = gson.fromJson<ArrayList<LastPunchInModel>>(response.body()!!.get("data").asJsonArray, listType)

                            if (punchInArr.isNotEmpty()) {
                                if (punchInArr.get(0).getPunchinDate() != "" && punchInArr.get(0).getPunchinDate() == formattedDate) {

                                    linearPlan.visibility = View.GONE
                                    cardPunchInMain.visibility = View.VISIBLE
                                    Glide.with(activityLocal)
                                        .load(ApiClient.BASE_IMAGE_URL+punchInArr.get(0).getPunchinImage())
                                        .into(imgPunchIn)

                                    tvPunchInDate.text = punchInArr.get(0).getPunchinDate()
                                    tvPunchInTime.text = punchInArr.get(0).getPunchinTime()

                                    println("punchInAr ${punchInArr.get(0).getWrokingType()}")
                                    if (punchInArr.get(0).getPunchoutDate() != "" ) {
                                        punchInOrOut = ""
                                        relativeMainPunch.visibility = View.GONE
                                        cardPunchOutMain.visibility = View.VISIBLE
                                        imgPunchOut.visibility = View.GONE
/*
                                        Glide.with(activityLocal)
                                            .load(ApiClient.BASE_IMAGE_URL+punchInArr.get(0).getPunchinImage())
                                            .into(imgPunchOut)
*/

                                        imgCamera.visibility = View.GONE
                                        tvPunchOutDate.text = punchInArr.get(0).getPunchinDate()
                                        tvPunchOutTime.text = punchInArr.get(0).getPunchinTime()
                                    }
                                    else {
                                        punchInOrOut = "out"
                                        tvPunchIn.text = "Punch Out"
                                        imgCamera.visibility = View.GONE
                                        StaticSharedpreference.saveInfo(Constant.PUNCHIN_ID, punchInArr.get(0).getPunchinId().toString(), activityLocal)
                                    }
                                } else {
                                    println("checkkkk=todayBeatSchedule="+StaticSharedpreference.getBoolean(Constant.todayBeatSchedule, activityLocal))
                                    println("checkkkk=beatUser="+StaticSharedpreference.getBoolean(Constant.beatUser, activityLocal).toString())
                                    userCityList("")

                                    if (StaticSharedpreference.getBoolean(Constant.todayBeatSchedule, activityLocal)){
                                        spinnerPlan.visibility = View.GONE
                                        getbeatschedule()
                                    }
                                    else{
                                        spinnerPlan.visibility = View.VISIBLE
                                        upcommingTourProgramme()
                                    }
                                    linearPlan.visibility = View.VISIBLE
                                }

                            } else {
                                if (StaticSharedpreference.getBoolean(Constant.todayBeatSchedule, activityLocal)){
                                    spinnerPlan.visibility = View.GONE
                                    getbeatschedule()
                                }
                                else{
                                    spinnerPlan.visibility = View.VISIBLE
                                    upcommingTourProgramme()
                                }
                                userCityList("")
                                linearPlan.visibility = View.VISIBLE
                                cardPunchInMain.visibility = View.GONE
                                cardPunchOutMain.visibility = View.GONE
                            }
                        } else {

                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    activityLocal, false
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            })
    }

    private fun getPunchin2() {

        if (!Utilities.isOnline(activityLocal)) {
            return
        }
        var dialog = DialogClass.progressDialog(activityLocal)
        val queryParams = HashMap<String, String>()
        queryParams["page"] = "1"
        queryParams["pageSize"] = "1"

        ApiClient.getPunchin(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, activityLocal).toString(),
            queryParams,
            object : APIResultLitener<JsonObject> {
                override fun onAPIResult(response: Response<JsonObject>?, errorMessage: String?) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {
                            val c = Calendar.getInstance().time

                            val df = SimpleDateFormat("yyyy-MM-dd")
                            val formattedDate = df.format(c)
                            val gson = Gson()
                            val listType = object : TypeToken<ArrayList<LastPunchInModel>>() {}.type

                            var punchInArr = gson.fromJson<ArrayList<LastPunchInModel>>(response.body()!!.get("data").asJsonArray, listType)

                            if (punchInArr.isNotEmpty()) {
                                if (punchInArr.get(0).getWrokingType() != "" && punchInArr.get(0).getPunchinDate() == formattedDate){
                                    if (StaticSharedpreference.getBoolean(Constant.todayBeatSchedule, activityLocal)){
                                        spinnerPlan.visibility = View.GONE
                                        getbeatschedule()
                                    }
                                    else{
                                        spinnerPlan.visibility = View.GONE
                                        upcommingTourProgramme()
                                    }
                                    spinnerPlan.setText(punchInArr.get(0).getWrokingType())
                                    spinnerCity.visibility = View.VISIBLE
                                    linearPlan.visibility = View.VISIBLE
                                    cardPunchInMain.visibility = View.GONE
                                    cardPunchOutMain.visibility = View.GONE
                                    userCityList("")
                                }
                                else if (punchInArr.get(0).getPunchinDate() != "" && punchInArr.get(0).getPunchinDate() == formattedDate) {

                                    linearPlan.visibility = View.GONE
                                    cardPunchInMain.visibility = View.VISIBLE
                                    Glide.with(activityLocal)
                                        .load(ApiClient.BASE_IMAGE_URL+punchInArr.get(0).getPunchinImage())
                                        .into(imgPunchIn)

                                    tvPunchInDate.text = punchInArr.get(0).getPunchinDate()
                                    tvPunchInTime.text = punchInArr.get(0).getPunchinTime()

                                    println("punchInAr ${punchInArr.get(0).getWrokingType()}")
                                    if (punchInArr.get(0).getPunchoutDate() != "" ) {
                                        punchInOrOut = ""
                                        relativeMainPunch.visibility = View.GONE
                                        cardPunchOutMain.visibility = View.VISIBLE
                                        imgPunchOut.visibility = View.GONE
/*
                                        Glide.with(activityLocal)
                                            .load(ApiClient.BASE_IMAGE_URL+punchInArr.get(0).getPunchinImage())
                                            .into(imgPunchOut)
*/

                                        imgCamera.visibility = View.GONE
                                        tvPunchOutDate.text = punchInArr.get(0).getPunchinDate()
                                        tvPunchOutTime.text = punchInArr.get(0).getPunchinTime()
                                    }
                                    else {
                                        punchInOrOut = "out"
                                        tvPunchIn.text = "Punch Out"
                                        imgCamera.visibility = View.GONE
                                        StaticSharedpreference.saveInfo(Constant.PUNCHIN_ID, punchInArr.get(0).getPunchinId().toString(), activityLocal)
                                    }
                                } else {
                                    println("checkkkk=todayBeatSchedule="+StaticSharedpreference.getBoolean(Constant.todayBeatSchedule, activityLocal))
                                    println("checkkkk=beatUser="+StaticSharedpreference.getBoolean(Constant.beatUser, activityLocal).toString())
                                    userCityList("")

                                    if (StaticSharedpreference.getBoolean(Constant.todayBeatSchedule, activityLocal)){
                                        spinnerPlan.visibility = View.GONE
                                        getbeatschedule()
                                    }
                                    else{
                                        spinnerPlan.visibility = View.VISIBLE
                                        upcommingTourProgramme()
                                    }
                                    linearPlan.visibility = View.VISIBLE
                                }

                            } else {
                                if (StaticSharedpreference.getBoolean(Constant.todayBeatSchedule, activityLocal)){
                                    spinnerPlan.visibility = View.GONE
                                    getbeatschedule()
                                }
                                else{
                                    spinnerPlan.visibility = View.VISIBLE
                                    upcommingTourProgramme()
                                }
                                userCityList("")
                                linearPlan.visibility = View.VISIBLE
                                cardPunchInMain.visibility = View.GONE
                                cardPunchOutMain.visibility = View.GONE
                            }

                        } else {

                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    activityLocal, false
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            })
    }

    private fun getbeatschedule() {
        if (!Utilities.isOnline(activityLocal)) {
            return
        }
        var dialog = DialogClass.progressDialog(activityLocal)
        val queryParams = HashMap<String, String>()

        ApiClient.beatschedule(StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, activityLocal).toString(), queryParams,
            object : APIResultLitener<BeatScheduleModel> {
                override fun onAPIResult(response: Response<BeatScheduleModel>?, errorMessage: String?) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {

                            println("Beattttt=="+response.body()!!.data!![0].beatDate)

                            if (response.body()!!.data.isNotEmpty()){
                                tvType.text = response.body()!!.data!![0].beatDate

                                val beatNames = StringBuilder()
                                val beatdis = StringBuilder()
                                val cityidd = StringBuilder()
                                val beatidd = StringBuilder()

                                for (value in response.body()!!.data) {
                                    value.beats?.let { beat ->
                                        beatNames.append(beat.beatName).append(", ")
                                    }
                                    value.beats?.let { beat ->
                                        beatdis.append(beat.description).append(", ")
                                    }
                                    value.beats?.let { beat ->
                                        cityidd.append(beat.cityId).append(", ")
                                    }
                                    value.beats?.let { beat ->
                                        beatidd.append(beat.id).append(", ")
                                    }
                                }

                                if (beatNames.isNotEmpty()) {
                                    beatNames.setLength(beatNames.length - 2)
                                }
                                if (beatdis.isNotEmpty()) {
                                    beatdis.setLength(beatdis.length - 2)
                                }
                                if (cityidd.isNotEmpty()) {
                                    cityidd.setLength(cityidd.length - 2)
                                }
                                if (beatidd.isNotEmpty()) {
                                    beatidd.setLength(beatidd.length - 2)
                                }
                                tvCity.text = beatNames.toString()
                                tvObjective.text = beatdis.toString()
                                selectedcityid = cityidd.toString()
                                schedulebeatid = beatidd.toString()
                            }
                            getWorkType()
                        } else {

                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    activityLocal, false
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            })
    }

    private fun upcommingTourProgramme() {

        if (!Utilities.isOnline(activityLocal)) {
            return
        }
        var dialog = DialogClass.progressDialog(activityLocal)
        val queryParams = HashMap<String, String>()
        queryParams["filter"] = ""

        ApiClient.upcommingTourProgramme(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, activityLocal).toString(),
            queryParams,
            object : APIResultLitener<JsonObject> {
                override fun onAPIResult(response: Response<JsonObject>?, errorMessage: String?) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {

                            val gson = Gson()
                            val listType = object :
                                TypeToken<ArrayList<TourModel>>() {}.type

                            tourArr = gson.fromJson<ArrayList<TourModel>>(
                                response.body()!!.get("data").asJsonArray,
                                listType
                            )

                            var todayCity = ""
                            var arrStr: ArrayList<String> = arrayListOf()
                            for (value in tourArr) {
                                if (value.date == Utilities.getCurrentDate()) {
                                    // if (value.programmeDate=="2022-08-30"){
                                    todayTourId = value.id.toString()
                                    tvObjective.text = value.objectives
                                    tvType.text = value.town
                                    tvCity.text = value.date

                                    arrStr.add(value.objectives!!)
                                }

//                                val aa = ArrayAdapter(
//                                    activityLocal,
//                                    android.R.layout.simple_list_item_1,
//                                    arrStr
//                                )
//                                aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//                                spinnerPlan.setAdapter(aa)

                                /* if (todayCity != "0" && todayCity != "" && todayCity != "null")
                                     getBeatList(todayCity)*/
                            }
                            getWorkType()

                        } else {

                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    activityLocal, false
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            })
    }

    var beatArr: java.util.ArrayList<BeatModel> = arrayListOf()
    var workTypeArr: java.util.ArrayList<WorkTypeModel> = arrayListOf()
    var userCityArr: java.util.ArrayList<UserCityModel> = arrayListOf()
    private fun getBeatList(cityId: String) {

        if (!Utilities.isOnline(activityLocal)) {
            return
        }

        val dialog = DialogClass.progressDialog(activityLocal)

        val queryParams = HashMap<String, String>()
        queryParams["city_id"] = cityId

        ApiClient.getBeatDropdownList(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, activityLocal).toString(),
            queryParams,
            object : APIResultLitener<JsonObject> {
                override fun onAPIResult(response: Response<JsonObject>?, errorMessage: String?) {
                    dialog.dismiss()

                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {

                            val gson = Gson()
                            val listType = object :
                                TypeToken<java.util.ArrayList<BeatModel>>() {}.type

                            beatArr = gson.fromJson<java.util.ArrayList<BeatModel>>(
                                response.body()!!.get("data").asJsonArray,
                                listType
                            )

                            if (beatArr.size !=0){
                                spinnerBeat.visibility = View.VISIBLE
                            }else{
                                spinnerBeat.visibility = View.GONE
                            }
                            spinnerBeat.setText("")
                            spinnerBeat.setOnClickListener {
                                spinnerBeat()
                            }
                        } else {

                            val jsonObject: JSONObject

                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())
                                if ( jsonObject.getString("message").equals("No Record Found")){
                                    spinnerBeat.visibility = View.GONE
                                }
                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    activityLocal,
                                    false
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    } else {
                        //  dialog.dismiss()
                        Toast.makeText(
                            activityLocal,
                            resources.getString(com.exp.clonefieldkonnect.R.string.poor_connection),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })
    }
    private fun getWorkType() {

        if (!Utilities.isOnline(activityLocal)) {
            return
        }

        val dialog = DialogClass.progressDialog(activityLocal)

        val queryParams = HashMap<String, String>()

        ApiClient.getWorkType(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, activityLocal).toString(),
            queryParams,
            object : APIResultLitener<JsonObject> {
                @SuppressLint("SuspiciousIndentation")
                override fun onAPIResult(response: Response<JsonObject>?, errorMessage: String?) {
                    dialog.dismiss()

                    if (response != null && errorMessage == null) {
                        if (response.code() == 200) {
                            val gson = Gson()
                            val listType = object :
                                TypeToken<java.util.ArrayList<WorkTypeModel>>() {}.type

                            workTypeArr  = gson.fromJson<java.util.ArrayList<WorkTypeModel>>(
                                response.body()!!.get("data").asJsonArray,
                                listType
                            )

                            var arrStr : ArrayList<String> = arrayListOf()

                            for(value in workTypeArr){
                                arrStr.add(value.type)
                            }
                            println("type="+arrStr)

                            val aa = ArrayAdapter(activityLocal, android.R.layout.simple_list_item_1, arrStr)
                            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            spinnerPlan.setAdapter(aa)
                        } else {

                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    activityLocal,
                                    false
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    } else {
                        //  dialog.dismiss()
                        Toast.makeText(
                            activityLocal,
                            resources.getString(com.exp.clonefieldkonnect.R.string.poor_connection),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })
    }
    private fun userCityList(searchText : String) {
        if (!Utilities.isOnline(activityLocal)) {
            return
        }

        val dialog = DialogClass.progressDialog(activityLocal)

        val queryParams = HashMap<String, String>()

        queryParams["cityname"] = searchText
        println("searchText ${searchText}")
        ApiClient.userCityList(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, activityLocal).toString(),
            queryParams,
            object : APIResultLitener<JsonObject> {
                override fun onAPIResult(response: Response<JsonObject>?, errorMessage: String?) {
                    dialog.dismiss()

                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {

                            val gson = Gson()
                            val listType = object :
                                TypeToken<java.util.ArrayList<UserCityModel>>() {}.type

                            userCityArr  = gson.fromJson<java.util.ArrayList<UserCityModel>>(
                                response.body()!!.get("data").asJsonArray,
                                listType
                            )
                            println("userCityArr="+userCityArr)

                            spinnerCity.setOnClickListener {
                                spinnerCity1()
                            }
                        } else {

                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    activityLocal,
                                    false
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                        }
                    } else {
                        //  dialog.dismiss()
                        Toast.makeText(activityLocal, resources.getString(com.exp.clonefieldkonnect.R.string.poor_connection), Toast.LENGTH_LONG).show()
                    }
                }
            })
    }

    private fun spinnerCity1() {
        items.clear()
        for (item in userCityArr) {
            items.add(item.city_name.toString(), item.id.toString(), item.isSelected)
        }

        SearchableMultiSelectSpinner.show(requireContext(), "Select City", "Done", items, object :
            SelectionCompleteListener {
            override fun onCompleteSelection(selectedItems: ArrayList<SearchableItem>) {
                Log.e("data", selectedItems.toString())
                val selectedCity = selectedItems.map { it.text }
                val selectedCityText = selectedCity.joinToString(",") // Use your preferred delimiter
                spinnerCity.setText(selectedCityText)

                val selectedCodes = selectedItems.map { it.code }
                selectedcityid = selectedCodes.joinToString(",")
                println("cityyy=selectedCodes=$selectedItems")
                println("cityyy=codesString=$selectedcityid")
                getBeatList(selectedcityid)
            }
        })
    }
    var tourId: String = ""

    private fun punchIn(summary: String) {

        if (!Utilities.isOnline(activityLocal)) {
            return
        }
        var dialog = DialogClass.progressDialog(activityLocal)

//        var fileToUploadList: MultipartBody.Part?
//        var file: File? = File(imagePath)
//        try {
//            file = Compressor(activityLocal).compressToFile(file)
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//
//        val reqbodyFileD: RequestBody =
//            RequestBody.create(MediaType.parse("image/*"), file)
//        val fileName = "image"
//        fileToUploadList =
//            MultipartBody.Part.createFormData(fileName, file!!.name, reqbodyFileD)

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
        val tour = RequestBody.create(
            MediaType.parse("text"),
            todayTourId
        )

        var type : RequestBody
        if (StaticSharedpreference.getBoolean(Constant.todayBeatSchedule, activityLocal)){
            type = RequestBody.create(
                MediaType.parse("text"),
                "Tour"
            )
        }else{
            type = RequestBody.create(
                MediaType.parse("text"),
                spinnerPlan.text.toString()
            )
        }
        var beatStr = ""

//        if (StaticSharedpreference.getBoolean(Constant.todayBeatSchedule, activityLocal)){
//            beatStr = schedulebeatid
//        }else{
//            for (value in beatArr) {
//                if (value.isChecked) {
//                    beatStr = if (beatStr == "")
//                        value.beatId.toString()
//                    else
//                        beatStr+","+value.beatId.toString()
//                }
//            }
//        }

        for (value in beatArr) {
            if (value.isChecked) {
                beatStr = if (beatStr == "")
                    value.beatId.toString()
                else
                    beatStr+","+value.beatId.toString()
            }
        }

        val city = RequestBody.create(
            MediaType.parse("text"),
            selectedcityid
        )
        val beat = RequestBody.create(
            MediaType.parse("text"),
            beatStr
        )
        println("Token=="+selectedcityid+"<<"+beatStr+"<<"+type+"<<"+latitude+"<<"+longitude)

        ApiClient.userPunchin(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, activityLocal).toString(),
            lat, long, address, summary, tour, beat,city,type,
            object : APIResultLitener<JsonObject> {
                override fun onAPIResult(response: Response<JsonObject>?, errorMessage: String?) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {
                            punchInOrOut = "out"
                            tvPunchIn.text = "Punch OUT"
                            cardPunchInMain.visibility = View.VISIBLE

                            StaticSharedpreference.saveInfo(Constant.PUNCHIN_ID, response.body()!!.get("punchin_id").asString, activityLocal)
                            StaticSharedpreference.saveInfo(Constant.IS_PUNCH_IN, "true", activityLocal)

                            tvPunchInTime.text = response.body()!!
                                .get("punchin").asJsonObject.get("punchin_time").asString
                            tvPunchInDate.text = response.body()!!
                                .get("punchin").asJsonObject.get("punchin_date").asString

                            Glide.with(activityLocal)
                                .load("")
                                .into(imgCamera)
                            Glide.with(activityLocal)
                                .load(ApiClient.BASE_IMAGE_URL+
                                        response.body()!!.get("punchin").asJsonObject.get("punchin_image").asString)
                                .into(imgPunchIn)
                            Toast.makeText(activityLocal, "Attendance add successfully", Toast.LENGTH_LONG).show()

                            val intent = Intent(context!!, LocationForegroundService::class.java)
                            ContextCompat.startForegroundService(context!!, intent)

                            try {
                                if (isWorkScheduled(tags)) {
                                    WorkManager.getInstance().cancelAllWorkByTag(tagCancel);
                                    Log.v("akram", "running or queqe")
                                } else {
                                    Log.v("akram", "start worker")
                                    setUpWorker()
                                }
                            } catch (e: Exception) {
                            }
                            startActivity(Intent(activityLocal, MainActivity::class.java))
                            activityLocal.finishAffinity()

                        } else {
                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    activityLocal,
                                    false
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    } else {
                        DialogClass.alertDialog("error", errorMessage.toString(), activityLocal, false)
                    }
                }
            })
    }

    private fun userPunchout(summary: String) {

        if (!Utilities.isOnline(activityLocal)) {
            return
        }

        var fileToUploadList: MultipartBody.Part?

//        var file: File? = File(imagePath)
//        try {
//            file = Compressor(activityLocal).compressToFile(file)
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//
//        val reqbodyFileD: RequestBody =
//            RequestBody.create(MediaType.parse("image/*"), file)
//        val fileName = "image"
//        fileToUploadList =
//            MultipartBody.Part.createFormData(fileName, file!!.name, reqbodyFileD)
//

        val punchInId = RequestBody.create(
            MediaType.parse("text"),
            "" + StaticSharedpreference.getInfo(Constant.PUNCHIN_ID, activityLocal)
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
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, activityLocal).toString(),
            punchInId, lat, long, address, summary,
            object : APIResultLitener<JsonObject> {
                override fun onAPIResult(response: Response<JsonObject>?, errorMessage: String?) {
                    try {
                        dialog!!.dismiss()
                    } catch (e: Exception) {
                    }
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {

                            try {
                                WorkManager.getInstance().cancelAllWorkByTag(tagCancel);
                            } catch (e: Exception) {
                            }

                            punchInOrOut = ""
                            relativeMainPunch.visibility = View.GONE
                            cardPunchOutMain.visibility = View.VISIBLE

                            tvPunchOutTime.text = response.body()!!
                                .get("punchout").asJsonObject.get("punchout_time").asString
                            tvPunchOutDate.text = response.body()!!
                                .get("punchout").asJsonObject.get("punchout_date").asString

                            Glide.with(activityLocal)
                                .load(ApiClient.BASE_IMAGE_URL+
                                        response.body()!!
                                            .get("punchout").asJsonObject.get("punchout_image").asString
                                )
                                .into(imgPunchOut)

                            StaticSharedpreference.saveInfo(Constant.IS_PUNCH_OUT, "true", activityLocal)

                            val intent = Intent(context!!, LocationForegroundService::class.java)
                            context!!.stopService(intent)

                            startActivity(Intent(activityLocal, MainActivity::class.java))
                            activityLocal.finishAffinity()
                        } else {
                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    activityLocal,
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                val requiredPermissions = listOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_AUDIO,
                    Manifest.permission.READ_MEDIA_VIDEO)
                when (requestCode) {
                    2, 3 -> handlePermissionsResult(permissions, grantResults, requiredPermissions)
                    // Add more cases if needed for other request codes
                }
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                val requiredPermissions = listOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                when (requestCode) {
                    2, 3 -> handlePermissionsResult(permissions, grantResults, requiredPermissions)
                    // Add more cases if needed for other request codes
                }
            }
            else -> {
                val requiredPermissions = listOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                when (requestCode) {
                    2, 3 -> handlePermissionsResult(permissions, grantResults, requiredPermissions)
                    // Add more cases if needed for other request codes
                }
            }
        }
    }
    private fun handlePermissionsResult(
        permissions: Array<String>,
        grantResults: IntArray,
        requiredPermissions: List<String>
    ) {
        val perms = HashMap<String, Int>().apply {
            // Initialize with PERMISSION_GRANTED
            requiredPermissions.forEach { this[it] = PackageManager.PERMISSION_GRANTED }
        }

        for (i in permissions.indices) {
            perms[permissions[i]] = grantResults[i]
        }

        val allPermissionsGranted = requiredPermissions.all { perms[it] == PackageManager.PERMISSION_GRANTED }

        if (allPermissionsGranted) {
            // All Permissions Granted
            // insertDummyContact();
            // openCamera()
            initGoogleAPIClient()
            showSettingDialog()
        } else {
            // Permission Denied
            Toast.makeText(activity, "Some Permission is Denied", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == INTENTCAMERA) {

            if (resultCode == Activity.RESULT_OK) {
                var path:File =   data!!.getSerializableExtra("image") as File
                imagePath = path.absolutePath
                //punchIn()
                imgCamera.setImageDrawable(null)
                Glide.with(activityLocal)
                    .load(imagePath)
                    .into(imgCamera)
            }
        } else if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
                gettingLocation()
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

                    try {
                        status.startResolutionForResult(activityLocal, REQUEST_CHECK_SETTINGS)
                    } catch (e: IntentSender.SendIntentException) {
                        e.printStackTrace()
                        // Ignore the error.
                    }
                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                }
            }
        }
    }

    lateinit var gpsTracker: GPSTracker
    var summary = ""
    var isCallDialog = false
    var dialog : Dialog? = null
    private fun gettingLocation() {
        if(punchInOrOut=="out"&&!isCallDialog){
            isCallDialog = true
        }
        Handler().postDelayed({
            gpsTracker = GPSTracker(activityLocal)
            gpsTracker.getLongitude()

            if (gpsTracker.getLatitude() == 0.0) {
                gettingLocation()
            } else {
                latitude = gpsTracker.getLatitude().toString()
                longitude = gpsTracker.getLongitude().toString()
            }
        }, 500)
    }

    /* Initiate Google API Client  */
    private fun initGoogleAPIClient() {
        //Without Google API Client Auto Location Dialog will not work
        mGoogleApiClient = GoogleApiClient.Builder(activityLocal)
            .addApi(LocationServices.API)
            .build()
        mGoogleApiClient!!.connect()
    }

    private fun openCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(activityLocal!!.packageManager) != null) {
            // Create the File where the photo should go
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                cameraFile = photoFile
                /*  Uri photoURI = FileProvider.getUriForFile(activity,
                                "com.jain.parwar.provider",
                                photoFile);*/
                val photoURI = FileProvider.getUriForFile(
                    activityLocal!!,
                    activityLocal!!.packageName + ".provider", photoFile
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, INTENTCAMERA)
            }
        }
    }

    @Throws(IOException::class)
    fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES)
        try {
            // Make sure the Pictures directory exists.
            storageDir.mkdirs()
        } catch (e: Exception) {

        }
        return File.createTempFile(
            imageFileName, /* prefix */
            ".jpg", /* suffix */
            storageDir      /* directory */
        )
    }

    private fun spinnerBeat(){
        val builder = AlertDialog.Builder(activityLocal)
        // String array for alert dialog multi choice items
        // Boolean array for initial selected items

        val colorsArray = arrayOfNulls<String>(beatArr.size)
        val checkedColorsArray = BooleanArray(beatArr.size)

        for((index,value) in beatArr.withIndex()){
            colorsArray[index] = value.beatName!!
            checkedColorsArray[index] = value.isChecked
        }
        // Convert the color array to list
        val colorsList = Arrays.asList(*colorsArray)
        //setTitle
        builder.setTitle("Select Beat")
        //set multichoice
        builder.setMultiChoiceItems(colorsArray, checkedColorsArray) { dialog, which, isChecked ->
            // Update the current focused item's checked status
            checkedColorsArray[which] = isChecked
            // Get the current focused item
            val currentItem = colorsList[which]
            // Notify the current action
        }
        // Set the positive/yes button click listener
        builder.setPositiveButton("OK") { dialog, which ->
            // Do something when click positive button
            var strBeat = ""
            for (i in checkedColorsArray.indices) {
                val checked = checkedColorsArray[i]
                if (checked) {
                    if(strBeat==""){
                        strBeat = beatArr[i].beatName.toString()
                    } else{
                        strBeat += "," + beatArr[i].beatName.toString()
                    }
                    beatArr[i].isChecked = true
                }
            }
            spinnerBeat.setText(strBeat)
        }
        // Set the neutral/cancel button click listener
        builder.setNeutralButton("Cancel") { dialog, which ->
            // Do something when click the neutral button
        }
        val dialog = builder.create()
        // Display the alert dialog on interface
        dialog.show()

    }
    private fun setUpWorker() {
        val workerRequest = PeriodicWorkRequest.Builder(WorkerLocation::class.java, 15, TimeUnit.MINUTES)
            .addTag(tagCancel)
            .build()
        val instance = WorkManager.getInstance()
        instance.enqueueUniquePeriodicWork(tags, ExistingPeriodicWorkPolicy.KEEP, workerRequest)
    }
    private fun isWorkScheduled(tag: String): Boolean {
        val instance = WorkManager.getInstance()
        val statuses: ListenableFuture<List<WorkInfo>> = instance.getWorkInfosForUniqueWork(tag)
        return try {
            var running = false
            val workInfoList: List<WorkInfo> = statuses.get()
            for (workInfo in workInfoList) {
                val state = workInfo.state
                running = state == WorkInfo.State.RUNNING || state == WorkInfo.State.ENQUEUED
            }
            running
        } catch (e: ExecutionException) {
            e.printStackTrace()
            false
        } catch (e: InterruptedException) {
            e.printStackTrace()
            false
        }
    }
}

fun MutableList<SearchableItem>.add(cityName: String?, id: String?, selected: Boolean) {
    add(SearchableItem(cityName.toString(), id ?: "" ))
}
