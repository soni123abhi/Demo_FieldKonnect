package com.exp.clonefieldkonnect.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.Html
import android.text.Spanned
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.adapter.LeadActivityLeadAdapter
import com.exp.clonefieldkonnect.connection.APIResultLitener
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.helper.Constant
import com.exp.clonefieldkonnect.helper.DialogClass
import com.exp.clonefieldkonnect.helper.GPSTracker
import com.exp.clonefieldkonnect.helper.StaticSharedpreference
import com.exp.clonefieldkonnect.model.AttendanceSubmitModel
import com.exp.clonefieldkonnect.model.LeadContactModel
import com.exp.clonefieldkonnect.model.LeadDetailModel
import com.exp.clonefieldkonnect.model.LeadStatusSourceModel
import com.exp.clonefieldkonnect.model.TaskDropdownModel
import com.exp.import.Utilities
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Response

class LeadDetailActivity : AppCompatActivity(),LeadActivityLeadAdapter.OnEmailClick{
    lateinit var cardBack_activity: CardView
    lateinit var carddddd_editt: CardView
    lateinit var ic_logo_name: TextView
    lateinit var tv_firm_name: TextView
    lateinit var tv_name: TextView
    lateinit var tv_url: TextView
    lateinit var tv_mob: TextView
    lateinit var tv_email: TextView
    lateinit var tv_loc: TextView
    lateinit var tv_pin: TextView
    lateinit var tvuser_notee: TextView
    lateinit var tv_lead_date: TextView
    lateinit var tv_action_date: TextView
    lateinit var tv_converted_date: TextView
    lateinit var tv_check_in: TextView
    lateinit var tv_checkout: TextView
    lateinit var notification_count: TextView
    lateinit var edtleadtype: AutoCompleteTextView
    lateinit var edtleadsource: AutoCompleteTextView
    lateinit var relll_check_in: RelativeLayout
    lateinit var relll_check_out: RelativeLayout
    lateinit var img_note: ImageView
    lateinit var img_task: ImageView
    lateinit var img_opportunity: ImageView
    lateinit var img_activity: ImageView

    private val REQUEST_CHECK_SETTINGS = 0x1
    private val REQUEST_VISIT_REPORT = 114
    private var mGoogleApiClient: GoogleApiClient? = null
    var latitude: String? = null
    var longitude: String? = null

    private var isLoading = false
    private var lead_response : LeadDetailModel.Data? = null
    private var lead_activity_response : ArrayList<LeadDetailModel.NotesTasks> = ArrayList()

    var leadcreatestatuslist: ArrayList<LeadStatusSourceModel.Status> = ArrayList()
    var leadcreatesourcelist: ArrayList<LeadStatusSourceModel.Source> = ArrayList()
    var leaduserlist: ArrayList<TaskDropdownModel.Users> = ArrayList()
    var leadprioritylist: ArrayList<TaskDropdownModel.Priorities> = ArrayList()
    var leadstatuslist: ArrayList<TaskDropdownModel.Status> = ArrayList()
    var leadcontactlist: ArrayList<LeadContactModel.Contacts> = ArrayList()
    var leadoppstatuslist: ArrayList<LeadContactModel.OpportunityStatuses> = ArrayList()

    var leadcontactname : ArrayList<String> = ArrayList()
    var leadcontactid : ArrayList<String> = ArrayList()
    var leadoppstatusname : ArrayList<String> = ArrayList()
    var leadoppstatusid : ArrayList<String> = ArrayList()
    var leadusersname : ArrayList<String> = ArrayList()
    var leadusersid : ArrayList<String> = ArrayList()
    var leadpriorityname : ArrayList<String> = ArrayList()
    var leadpriorityid : ArrayList<String> = ArrayList()
    var leadstatusname : ArrayList<String> = ArrayList()
    var leadstatusid : ArrayList<String> = ArrayList()
    var leadcreatestatusname : ArrayList<String> = ArrayList()
    var leadcreatestatusid : ArrayList<String> = ArrayList()
    var leadcreatesourcename : ArrayList<String> = ArrayList()
    var leadcreatesourceid : ArrayList<String> = ArrayList()



    private var lastPosition = -1
    var checkInOrOut :String = ""
    var selectedtaskstatus_id :String = ""
    var selectedtaskpriority_id :String = ""
    var selectedcontact_id :String = ""
    var selectedstatus_id :String = ""
    var selectedtaskuser_id :String = ""
    var selectedtype_id :String = ""
    var selectedsource_id :String = ""
    var pincode_id : String = ""
    var city_id : String = ""
    var state_id : String = ""
    var district_id : String = ""
    var lead_id :String = ""
    var lead_check_in :String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lead_detail)
        initViews()

    }

    private fun initViews() {
        cardBack_activity = findViewById(R.id.cardBack_detail)
        carddddd_editt = findViewById(R.id.carddddd_editt)
        ic_logo_name = findViewById(R.id.ic_logo_name)
        tv_firm_name = findViewById(R.id.tv_firm_name)
        tv_name = findViewById(R.id.tv_name)
        tv_url = findViewById(R.id.tv_url)
        tv_mob = findViewById(R.id.tv_mob)
        tv_email = findViewById(R.id.tv_email)
        tv_loc = findViewById(R.id.tv_loc)
        tv_pin = findViewById(R.id.tv_pin)
        edtleadtype = findViewById(R.id.edtleadtype)
        edtleadsource = findViewById(R.id.edtleadsource)
        tvuser_notee = findViewById(R.id.tvuser_notee)
        tv_lead_date = findViewById(R.id.tv_lead_date)
        tv_action_date = findViewById(R.id.tv_action_date)
        tv_converted_date = findViewById(R.id.tv_converted_date)
        relll_check_in = findViewById(R.id.relll_check_in)
        relll_check_out = findViewById(R.id.relll_check_out)
        tv_check_in = findViewById(R.id.tv_check_in)
        tv_checkout = findViewById(R.id.tv_checkout)
        img_note = findViewById(R.id.img_note)
        img_task = findViewById(R.id.img_task)
        img_opportunity = findViewById(R.id.img_opportunity)
        img_activity = findViewById(R.id.img_activity)
        notification_count = findViewById(R.id.notification_count)

        lead_id = intent.getStringExtra("lead_id").toString()

        lead_check_in = StaticSharedpreference.getInfo(Constant.Lead_check_in, this@LeadDetailActivity).toString()
        println("lead_check_in=="+lead_check_in)


        if (!lead_check_in.isNullOrEmpty()){
            relll_check_in.isClickable = false
            relll_check_in.setBackgroundColor(Color.parseColor("#8A8A8A"))
            tv_check_in.setTextColor(Color.parseColor("#FFFFFF"))
            val drawable = ContextCompat.getDrawable(this@LeadDetailActivity, R.drawable.ic_lead_checkin_yes)
            tv_check_in.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
        }


        if (lead_id.isNotEmpty()){
            getleaddetail(lead_id)
        }

        edtleadtype.setOnClickListener {
            spinnerstatus(edtleadtype)
        }

        getleadstatussource()
        gettaskdropdown()



        println("lead_idlead_id=="+lead_id)

        relll_check_in.setOnClickListener {
            if (lead_check_in.isNullOrEmpty()){
                relll_check_in.setBackgroundColor(Color.parseColor("#00ACD8"))
                tv_check_in.setTextColor(Color.parseColor("#FFFFFF"))
                val drawable = ContextCompat.getDrawable(this, R.drawable.ic_lead_checkin_yes)
                tv_check_in.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)

                relll_check_out.setBackgroundColor(Color.parseColor("#FFFFFF"))
                tv_checkout.setTextColor(Color.parseColor("#00ACD8"))
                val drawable2 = ContextCompat.getDrawable(this, R.drawable.ic_lead_checkout_not)
                tv_checkout.setCompoundDrawablesWithIntrinsicBounds(drawable2, null, null, null)
                checkInOrOut = "in"

                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    1
                )
            }
            else{
                responsemessage("Already Checked In!!")
            }


        }

        relll_check_out.setOnClickListener {
            if (lead_check_in.isNullOrEmpty()){
                responsemessage("Please Check In")
            }else{
                relll_check_out.setBackgroundColor(Color.parseColor("#00ACD8"))
                tv_checkout.setTextColor(Color.parseColor("#FFFFFF"))
                val drawable = ContextCompat.getDrawable(this, R.drawable.ic_lead_checkout_yes)
                tv_checkout.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)

//                relll_check_in.setBackgroundColor(Color.parseColor("#FFFFFF"))
//                tv_check_in.setTextColor(Color.parseColor("#00ACD8"))
//                val drawable2 = ContextCompat.getDrawable(this, R.drawable.ic_lead_checkin_not)
//                tv_check_in.setCompoundDrawablesWithIntrinsicBounds(drawable2, null, null, null)

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

        img_note.setOnClickListener {
            shownotepopup()
        }

        img_task.setOnClickListener {
            showtaskpopup()
        }

        img_opportunity.setOnClickListener {
            showopportunitypopup()
        }

        img_activity.setOnClickListener {
            showactivitypopup()
        }


        carddddd_editt.setOnClickListener {
            showeditleadpopup()
        }

        cardBack_activity.setOnClickListener {
            handelbackpress()
        }

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



    /* Show Location Access Dialog */
    private fun showSettingDialog() {
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY//Setting priotity of Location request to high
        locationRequest.interval = (30 * 1000).toLong()
        locationRequest.fastestInterval = (5 * 1000).toLong()//5 sec Time interval for location update
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        builder.setAlwaysShow(true) //this is the key ingredient to show dialog always when GPS is off

        val result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient!!, builder.build())
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
                            this@LeadDetailActivity,
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
//        dialog = DialogClass.progressDialog(this)

        Handler().postDelayed({
            gpsTracker = GPSTracker(this@LeadDetailActivity)
            gpsTracker.getLongitude()

            if (gpsTracker.getLatitude() == 0.0) {
                gettingLocation()
            } else {
                latitude = gpsTracker.getLatitude().toString()
                longitude = gpsTracker.getLongitude().toString()

                println("laaaaaaaaaa=="+latitude+"<<"+longitude+"<<"+lead_id)

                if (checkInOrOut == "in")
                    submitCheckin(latitude!!, longitude!!)
                else if (checkInOrOut == "out") {
                    showCheckoutDialog(latitude!!, longitude!!)
                }
            }
        }, 2000)
    }



    private lateinit var alertDialog2: AlertDialog

    private fun showCheckoutDialog(latitude: String, longitude: String) {
        val builder = AlertDialog.Builder(this)
        val inflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.dialog_checkin_summary, null)
        builder.setCancelable(false)

        val tvDetails = view.findViewById<TextView>(R.id.tvDetails)
        val editTextNote = view.findViewById<EditText>(R.id.edtCheckIn)
        val btnSubmit = view.findViewById<CardView>(R.id.cardOK)
        val tvBtn = view.findViewById<TextView>(R.id.tvBtn)

        tvDetails.text = "Lead Check Out Summary"
        tvBtn.text = "Check Out"

        btnSubmit.setOnClickListener {
            val note = editTextNote.text.toString().trim()
            if (note.isNotEmpty()) {
                submitCheckout(latitude, longitude, note)
                alertDialog2.dismiss()
            } else {
                Toast.makeText(this, "Note cannot be empty!", Toast.LENGTH_SHORT).show()
            }
        }

        builder.setView(view)
        alertDialog2 = builder.create()
        alertDialog2.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // Optional: make background transparent
        alertDialog2.show()
    }



    /* Initiate Google API Client  */
    private fun initGoogleAPIClient() {
        //Without Google API Client Auto Location Dialog will not work
        mGoogleApiClient = GoogleApiClient.Builder(this@LeadDetailActivity)
            .addApi(LocationServices.API)
            .build()
        mGoogleApiClient!!.connect()
    }


    private fun showactivitypopup() {
        val builder = AlertDialog.Builder(this)
        val inflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.popup_activity_lead_layout, null)
        builder.setCancelable(false)

        val img_close: ImageView = view.findViewById(R.id.img_close)
        val recyclerView_leadactivity: RecyclerView = view.findViewById(R.id.recyclerView_leadactivity)


        setuprecycleractivitylist(recyclerView_leadactivity,lead_activity_response)



        img_close.setOnClickListener {
            alertDialog.dismiss()
        }

        builder.setView(view)
        alertDialog = builder.create()
        alertDialog.show()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setuprecycleractivitylist(
        recyclerView_leadactivity: RecyclerView,
        lead_activity_response: ArrayList<LeadDetailModel.NotesTasks>
    ) {
        val mLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView_leadactivity.layoutManager = mLayoutManager
        val useractivityAdapter = LeadActivityLeadAdapter(this, lead_activity_response,this)
        recyclerView_leadactivity.adapter = useractivityAdapter
        recyclerView_leadactivity.isNestedScrollingEnabled = false
        recyclerView_leadactivity.scrollToPosition(lastPosition)
        useractivityAdapter.notifyDataSetChanged()
    }

    private fun showopportunitypopup() {
        val builder = AlertDialog.Builder(this)
        val inflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.popup_opportunity_lead_layout, null)
        builder.setCancelable(false)

        val img_close: ImageView = view.findViewById(R.id.img_close)
        val tv_confidence_value: TextView = view.findViewById(R.id.tv_confidence_value)
        val seekBar_confidence: SeekBar = view.findViewById(R.id.seekBar_confidence)
        val edtusername: AutoCompleteTextView = view.findViewById(R.id.edtusername)
        val edtamount: AutoCompleteTextView = view.findViewById(R.id.edtamount)
        val edtcontact: AutoCompleteTextView = view.findViewById(R.id.edtcontact)
        val edtnote: AutoCompleteTextView = view.findViewById(R.id.edtnote)
        val edtdate: AutoCompleteTextView = view.findViewById(R.id.edtdate)
        val edtstatus: AutoCompleteTextView = view.findViewById(R.id.edtstatus)
        val cardcancel: CardView = view.findViewById(R.id.cardcancel)
        val cardsave: CardView = view.findViewById(R.id.cardsave)

        getleadcontact()

        edtusername.setOnClickListener {
            spinnerusername(edtusername)
        }

        edtcontact.setOnClickListener {
            spinnercontact(edtcontact)
        }

        edtstatus.setOnClickListener {
            spinnerstatusss(edtstatus)
        }


        edtdate.setOnClickListener {
            Utilities.datePicker(edtdate,this@LeadDetailActivity)
        }

        seekBar_confidence.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tv_confidence_value.text = "$progress%"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })


        cardsave.setOnClickListener {
            var date = Utilities.convertDateFormat(edtdate.text.toString())

            if (edtamount.text.toString().isNullOrEmpty()){
                responsemessage("Please Enter Amount")
            }else if (selectedtaskuser_id.isNullOrEmpty()){
                responsemessage("Please Select User")
            }else if (selectedcontact_id.isNullOrEmpty()){
                responsemessage("Please Select Contact")
            }else if (edtnote.text.toString().isNullOrEmpty()){
                responsemessage("Please Enter Note")
            }else if (date.isNullOrEmpty()){
                responsemessage("Please Select Date")
            }else if (selectedstatus_id.isNullOrEmpty()){
                responsemessage("Please Select Status")
            }else{
                submitopportunity(lead_id,edtamount.text.toString(),tv_confidence_value.text.toString(),selectedtaskuser_id,
                    selectedcontact_id,edtnote.text.toString(),date,selectedstatus_id)
            }
        }


        img_close.setOnClickListener {
            alertDialog.dismiss()
        }

        cardcancel.setOnClickListener {
            alertDialog.dismiss()
        }

        builder.setView(view)
        alertDialog = builder.create()
        alertDialog.show()
    }


    private fun showtaskpopup() {
        val builder = AlertDialog.Builder(this)
        val inflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.popup_task_lead_layout, null)
        builder.setCancelable(false)

        val img_close: ImageView = view.findViewById(R.id.img_close)
        val edtleadusername: AutoCompleteTextView = view.findViewById(R.id.edtleadusername)
        val edtacccview: AutoCompleteTextView = view.findViewById(R.id.edtacccview)
        val edtpriority: AutoCompleteTextView = view.findViewById(R.id.edtpriority)
//        val edtstatus: AutoCompleteTextView = view.findViewById(R.id.edtstatus)
        val edtdate: AutoCompleteTextView = view.findViewById(R.id.edtdate)
        val edttime: AutoCompleteTextView = view.findViewById(R.id.edttime)
        val cardcancel: CardView = view.findViewById(R.id.cardcancel)
        val cardsave: CardView = view.findViewById(R.id.cardsave)

        edtleadusername.setOnClickListener {
            spinnerusername(edtleadusername)
        }

        edtpriority.setOnClickListener {
            spinnerpriority(edtpriority)
        }

        /*edtstatus.setOnClickListener {
            spinnertaskstatus(edtstatus)
        }*/
        edtdate.setOnClickListener {
            Utilities.datePicker(edtdate,this@LeadDetailActivity)
        }
        edttime.setOnClickListener {
            Utilities.show24HourTimePicker(this@LeadDetailActivity,edttime)
        }

        cardsave.setOnClickListener {
            var date = Utilities.convertDateFormat(edtdate.text.toString())
            val time = edttime.text.toString().trim()

            println("abhiiiiiii=="+date+"<<"+time)


            if (selectedtaskuser_id.isNullOrEmpty()){
                responsemessage("Please Select User")
            }else if (edtacccview.text.isNullOrEmpty()){
                responsemessage("Please Enter Description")
            }else if (selectedtaskpriority_id.isNullOrEmpty()){
                responsemessage("Please Select Priority")
            }
            /*else if (selectedtaskstatus_id.isNullOrEmpty()){
                responsemessage("Please Select Status")
            }*/
            else if (date.isNullOrEmpty()){
                responsemessage("Please Select Date")
            }else{
                submittask(
                    lead_id,
                    selectedtaskuser_id,
                    edtacccview.text.toString(),
                    selectedtaskpriority_id,
                    date,
                    time,
                    ""
                )
            }
        }


        img_close.setOnClickListener {
            alertDialog.dismiss()
        }
        cardcancel.setOnClickListener {
            alertDialog.dismiss()
        }

        builder.setView(view)
        alertDialog = builder.create()
        alertDialog.show()
    }



    private fun spinnertaskstatus(edtstatus: AutoCompleteTextView) {
        val builder = android.app.AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_alert_dialog2, null)
        builder.setView(dialogView)

        val searchEditText = dialogView.findViewById<EditText>(R.id.searchEditText)
        val listView = dialogView.findViewById<ListView>(R.id.listView)
        val button = dialogView.findViewById<Button>(R.id.button)

        val colorsArray = leadstatusname.map { it.toString() }.toTypedArray()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, colorsArray)
        listView.adapter = adapter

        builder.setTitle("Select Status")

        val dialog = builder.create()

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {
                adapter.filter.filter(charSequence)
            }

            override fun afterTextChanged(editable: Editable) {}
        })
        button.setOnClickListener {
            edtstatus.setText("")
            selectedtaskstatus_id = ""
            dialog.dismiss()
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedPosition = leadstatusname.indexOf(adapter.getItem(position).toString())
            if (selectedPosition != -1) {
                val selectedParentId = leadstatusid[selectedPosition].toString()
                val selectedParentName = leadstatusname[selectedPosition].toString()

                edtstatus.setText(selectedParentName)
                selectedtaskstatus_id = selectedParentId

                println("Abhi=id=$selectedtaskstatus_id")


                dialog.dismiss()
            }
        }

        dialog.show() // Show the dialog
    }



    private fun spinnercontact(edtcontact: AutoCompleteTextView) {
        val builder = android.app.AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_alert_dialog2, null)
        builder.setView(dialogView)

        val searchEditText = dialogView.findViewById<EditText>(R.id.searchEditText)
        val listView = dialogView.findViewById<ListView>(R.id.listView)
        val button = dialogView.findViewById<Button>(R.id.button)

        val colorsArray = leadcontactname.map { it.toString() }.toTypedArray()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, colorsArray)
        listView.adapter = adapter

        builder.setTitle("Select Contact")

        val dialog = builder.create()

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {
                adapter.filter.filter(charSequence)
            }

            override fun afterTextChanged(editable: Editable) {}
        })
        button.setOnClickListener {
            edtcontact.setText("")
            selectedcontact_id = ""
            dialog.dismiss()
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedPosition = leadcontactname.indexOf(adapter.getItem(position).toString())
            if (selectedPosition != -1) {
                val selectedParentId = leadcontactid[selectedPosition].toString()
                val selectedParentName = leadcontactname[selectedPosition].toString()

                edtcontact.setText(selectedParentName)
                selectedcontact_id = selectedParentId

                println("Abhi=id=$selectedcontact_id")


                dialog.dismiss()
            }
        }

        dialog.show() // Show the dialog
    }


    private fun spinnerstatusss(edtstatus: AutoCompleteTextView) {
        val builder = android.app.AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_alert_dialog2, null)
        builder.setView(dialogView)

        val searchEditText = dialogView.findViewById<EditText>(R.id.searchEditText)
        val listView = dialogView.findViewById<ListView>(R.id.listView)
        val button = dialogView.findViewById<Button>(R.id.button)

        val colorsArray = leadoppstatusname.map { it.toString() }.toTypedArray()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, colorsArray)
        listView.adapter = adapter

        builder.setTitle("Select Status")

        val dialog = builder.create()

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {
                adapter.filter.filter(charSequence)
            }

            override fun afterTextChanged(editable: Editable) {}
        })
        button.setOnClickListener {
            edtstatus.setText("")
            selectedstatus_id = ""
            dialog.dismiss()
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedPosition = leadoppstatusname.indexOf(adapter.getItem(position).toString())
            if (selectedPosition != -1) {
                val selectedParentId = leadoppstatusid[selectedPosition].toString()
                val selectedParentName = leadoppstatusname[selectedPosition].toString()

                edtstatus.setText(selectedParentName)
                selectedstatus_id = selectedParentId

                println("Abhi=id=$selectedstatus_id")


                dialog.dismiss()
            }
        }

        dialog.show() // Show the dialog
    }


    private fun spinnerpriority(edtpriority: AutoCompleteTextView) {
        val builder = android.app.AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_alert_dialog2, null)
        builder.setView(dialogView)

        val searchEditText = dialogView.findViewById<EditText>(R.id.searchEditText)
        val listView = dialogView.findViewById<ListView>(R.id.listView)
        val button = dialogView.findViewById<Button>(R.id.button)

        val colorsArray = leadpriorityname.map { it.toString() }.toTypedArray()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, colorsArray)
        listView.adapter = adapter

        builder.setTitle("Select Priority")

        val dialog = builder.create()

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {
                adapter.filter.filter(charSequence)
            }

            override fun afterTextChanged(editable: Editable) {}
        })
        button.setOnClickListener {
            edtpriority.setText("")
            selectedtaskpriority_id = ""
            dialog.dismiss()
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedPosition = leadpriorityname.indexOf(adapter.getItem(position).toString())
            if (selectedPosition != -1) {
                val selectedParentId = leadpriorityid[selectedPosition].toString()
                val selectedParentName = leadpriorityname[selectedPosition].toString()

                edtpriority.setText(selectedParentName)
                selectedtaskpriority_id = selectedParentId

                println("Abhi=id=$selectedtaskpriority_id")


                dialog.dismiss()
            }
        }

        dialog.show() // Show the dialog
    }



    private fun spinnerusername(edtleadusername: AutoCompleteTextView) {
        val builder = android.app.AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_alert_dialog2, null)
        builder.setView(dialogView)

        val searchEditText = dialogView.findViewById<EditText>(R.id.searchEditText)
        val listView = dialogView.findViewById<ListView>(R.id.listView)
        val button = dialogView.findViewById<Button>(R.id.button)

        val colorsArray = leadusersname.map { it.toString() }.toTypedArray()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, colorsArray)
        listView.adapter = adapter

        builder.setTitle("Select Username")

        val dialog = builder.create()

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {
                adapter.filter.filter(charSequence)
            }

            override fun afterTextChanged(editable: Editable) {}
        })
        button.setOnClickListener {
            edtleadusername.setText("")
            selectedtaskuser_id = ""
            dialog.dismiss()
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedPosition = leadusersname.indexOf(adapter.getItem(position).toString())
            if (selectedPosition != -1) {
                val selectedParentId = leadusersid[selectedPosition].toString()
                val selectedParentName = leadusersname[selectedPosition].toString()

                edtleadusername.setText(selectedParentName)
                selectedtaskuser_id = selectedParentId

                println("Abhi=id=$selectedtaskuser_id")


                dialog.dismiss()
            }
        }

        dialog.show() // Show the dialog
    }



    private fun shownotepopup() {
        val builder = AlertDialog.Builder(this)
        val inflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.popup_note_lead_layout, null)
        builder.setCancelable(false)

        val img_close: ImageView = view.findViewById(R.id.img_close)
        val edtnote: AutoCompleteTextView = view.findViewById(R.id.edtnote)
        val cardSubmit: CardView = view.findViewById(R.id.cardSubmit)

        cardSubmit.setOnClickListener {
            if (edtnote.text.toString().isNullOrEmpty()){
                responsemessage("Please Enter Note")
            }else{
                submitnote(edtnote.text.toString(), "","")
            }
        }


        img_close.setOnClickListener {
            alertDialog.dismiss()
        }

        builder.setView(view)
        alertDialog = builder.create()
        alertDialog.show()

    }


    @SuppressLint("MissingInflatedId")
    private lateinit var alertDialog: AlertDialog

    @SuppressLint("MissingInflatedId")
    private fun showeditleadpopup() {

        val builder = AlertDialog.Builder(this)
        val inflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.popup_edit_lead_layout, null)
        builder.setCancelable(false)

        val img_close: ImageView = view.findViewById(R.id.img_close)
        val edtleadtype: AutoCompleteTextView = view.findViewById(R.id.edtleadtype)
        val edtleadassign: AutoCompleteTextView = view.findViewById(R.id.edtleadassign)
        val edtleadsource: AutoCompleteTextView = view.findViewById(R.id.edtleadsource)
        val edtfirmname: AutoCompleteTextView = view.findViewById(R.id.edtfirmname)
        val edtcustomername: AutoCompleteTextView = view.findViewById(R.id.edtcustomername)
        val edtmobno: AutoCompleteTextView = view.findViewById(R.id.edtmobno)
        val edtemail: AutoCompleteTextView = view.findViewById(R.id.edtemail)
        val edtadd: AutoCompleteTextView = view.findViewById(R.id.edtadd)
        val edtwebsite: AutoCompleteTextView = view.findViewById(R.id.edtwebsite)
        val edtnote: AutoCompleteTextView = view.findViewById(R.id.edtnote)
        val cardSubmit: CardView = view.findViewById(R.id.cardSubmit)
        val spinnerPin: EditText = view.findViewById(R.id.spinnerPin)
        val edtState: EditText = view.findViewById(R.id.edtState)
        val edtCity: EditText = view.findViewById(R.id.edtCity)
        val edtdistric: EditText = view.findViewById(R.id.edtdistric)

        edtleadtype.visibility = View.GONE
        edtleadsource.visibility = View.GONE


        println("leadreeeee==="+lead_response)
//        edtleadtype.setText(lead_response!!.status.toString())
//        edtleadsource.setText(lead_response!!.leadSource.toString())

        edtleadassign.setText(lead_response!!.assign_user_name.toString())
        edtfirmname.setText(lead_response!!.companyName.toString())
        edtcustomername.setText(lead_response!!.contactName.toString())
        edtwebsite.setText(lead_response!!.website.toString())
        edtmobno.setText(lead_response!!.phoneNumber.toString())
        edtemail.setText(lead_response!!.email.toString())
        edtadd.setText(lead_response!!.address.toString())
        spinnerPin.setText(lead_response!!.pincode.toString())
        edtnote.setText(lead_response!!.note.toString())
        edtCity.setText(lead_response!!.city.toString())
        edtState.setText(lead_response!!.state.toString())
        edtdistric.setText(lead_response!!.district.toString())

        selectedsource_id = lead_response!!.leadSource.toString()
        selectedtype_id = lead_response!!.status_id.toString()
        pincode_id = lead_response!!.pincode_id.toString()
        city_id  = lead_response!!.city_id.toString()
        state_id  = lead_response!!.state_id.toString()
        district_id = lead_response!!.district_id.toString()
        selectedtaskuser_id = lead_response!!.assign_user_id.toString()



        cardSubmit.setOnClickListener {
            println("AAAAAAA=="+selectedtype_id+"<<"+edtfirmname.text.toString()+"<<"+edtcustomername.text.toString()+"<<"+
                    edtmobno.text.toString()+"<<"+selectedsource_id)
            if (selectedtype_id.isNullOrEmpty()){
                responsemessage("Please Select Lead Type")
            }else if (edtfirmname.text.isNullOrEmpty()){
                responsemessage("Please Enter Firm Name")
            }else if (edtcustomername.text.isNullOrEmpty()){
                responsemessage("Please Enter Customer Name")
            }else if (edtmobno.text.isNullOrEmpty()){
                responsemessage("Please Enter Mobile No.")
            }else if (selectedsource_id.isNullOrEmpty()){
                responsemessage("Please Select Lead Source")
            }else{
                submitedit(selectedtype_id,edtfirmname.text.toString(),edtcustomername.text.toString(),
                    edtmobno.text.toString(),edtemail.text.toString(),edtadd.text.toString(),
                    pincode_id,state_id,city_id,district_id,selectedsource_id,edtnote.text.toString(),edtwebsite.text.toString(),
                    alertDialog,lead_id,selectedtaskuser_id)
            }
        }


        edtleadassign.setOnClickListener {
            spinnerusername(edtleadassign)
        }

       /* edtleadtype.setOnClickListener {
            spinnerstatus(edtleadtype)
        }
        edtleadsource.setOnClickListener {
            spinnersource(edtleadsource)
        }*/

        spinnerPin.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if (spinnerPin.text.toString().length == 6) {
                    getPincodeInfo(spinnerPin.text.toString(),edtState,edtCity,edtdistric)
                } else if (spinnerPin.text.toString().length < 6) {
                    edtState.setText("")
                    edtCity.setText("")
                    edtdistric.setText("")
                    pincode_id = ""
                    city_id  = ""
                    state_id  = ""
                    district_id = ""
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
            }
        })



        img_close.setOnClickListener {
            alertDialog.dismiss()
        }

        builder.setView(view)
        alertDialog = builder.create()
        alertDialog.show()

    }

    private fun submittask(
        leadId: String,
        selectedtaskuserId: String,
        description: String,
        selectedtaskpriorityId: String,
        date: String,
        time: String,
        task_id: String
    ) {

        if (!Utilities.isOnline(this@LeadDetailActivity)) {
            return
        }
        var dialog = DialogClass.progressDialog(this@LeadDetailActivity)
        val queryParams = java.util.HashMap<String, String>()
        queryParams["lead_id"] = leadId
        queryParams["assigned_to"] = selectedtaskuserId
        queryParams["description"] = description
        queryParams["priority"] = selectedtaskpriorityId
        queryParams["date"] = date
        queryParams["time"] = time
        queryParams["task_id"] = task_id


        ApiClient.submitleadtask(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this@LeadDetailActivity).toString(),
            queryParams,
            object : APIResultLitener<AttendanceSubmitModel> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onAPIResult(
                    response: Response<AttendanceSubmitModel>?,
                    errorMessage: String?
                ) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {
                            getleaddetail(lead_id)
                            Toast.makeText(this@LeadDetailActivity, response.body()!!.message, Toast.LENGTH_LONG).show()
                            alertDialog.dismiss()
                        } else {
                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    this@LeadDetailActivity, false
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            })
    }


    private fun updatelead(selectedtypeId: String, leadId: String) {
        if (!Utilities.isOnline(this@LeadDetailActivity)) {
            return
        }
        var dialog = DialogClass.progressDialog(this@LeadDetailActivity)
        val queryParams = java.util.HashMap<String, String>()
        queryParams["lead_id"] = leadId
        queryParams["status"] = selectedtypeId

        ApiClient.updateleadtype(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this@LeadDetailActivity).toString(),
            queryParams,
            object : APIResultLitener<AttendanceSubmitModel> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onAPIResult(
                    response: Response<AttendanceSubmitModel>?,
                    errorMessage: String?
                ) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {
                            Toast.makeText(this@LeadDetailActivity, response.body()!!.message, Toast.LENGTH_LONG).show()
                            getleaddetail(this@LeadDetailActivity.lead_id)
                        } else {
                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    this@LeadDetailActivity, false
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            })
    }

    private fun submitnote(note: String, id: String, lead_id: String) {
        if (!Utilities.isOnline(this@LeadDetailActivity)) {
            return
        }
        var dialog = DialogClass.progressDialog(this@LeadDetailActivity)
        val queryParams = java.util.HashMap<String, String>()
        queryParams["lead_id"] = this.lead_id
        queryParams["note"] = note
        queryParams["note_id"] = id

        ApiClient.submitleadnote(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this@LeadDetailActivity).toString(),
            queryParams,
            object : APIResultLitener<AttendanceSubmitModel> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onAPIResult(
                    response: Response<AttendanceSubmitModel>?,
                    errorMessage: String?
                ) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {
                            Toast.makeText(this@LeadDetailActivity, response.body()!!.message, Toast.LENGTH_LONG).show()
                            getleaddetail(this@LeadDetailActivity.lead_id)
                            alertDialog.dismiss()
                        } else {
                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    this@LeadDetailActivity, false
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            })
    }

    private fun submitCheckin(latitude: String, longitude: String) {
        if (!Utilities.isOnline(this@LeadDetailActivity)) {
            return
        }
        var dialog = DialogClass.progressDialog(this@LeadDetailActivity)
        val queryParams = java.util.HashMap<String, String>()
        queryParams["lead_id"] = lead_id
        queryParams["checkin_latitude"] = latitude
        queryParams["checkin_longitude"] = longitude


        ApiClient.submitleadcheckin(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this@LeadDetailActivity).toString(),
            queryParams,
            object : APIResultLitener<AttendanceSubmitModel> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onAPIResult(
                    response: Response<AttendanceSubmitModel>?,
                    errorMessage: String?
                ) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {
                            Toast.makeText(this@LeadDetailActivity, response.body()!!.message, Toast.LENGTH_LONG).show()
                            StaticSharedpreference.saveInfo(Constant.Lead_check_in,response.body()!!.checkin_id.toString(),this@LeadDetailActivity)
                            StaticSharedpreference.saveInfo(Constant.Lead_check_in_leadID,lead_id,this@LeadDetailActivity)

                            lead_check_in = response.body()!!.checkin_id.toString()

                            relll_check_in.setBackgroundColor(Color.parseColor("#8A8A8A"))
                            tv_check_in.setTextColor(Color.parseColor("#FFFFFF"))
                            val drawable = ContextCompat.getDrawable(this@LeadDetailActivity, R.drawable.ic_lead_checkin_yes)
                            tv_check_in.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
                            relll_check_in.isClickable = false


                        } else {
                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    this@LeadDetailActivity, false
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            })
    }

    private fun submitCheckout(latitude: String, longitude: String, note: String) {
        if (!Utilities.isOnline(this@LeadDetailActivity)) {
            return
        }
        var dialog = DialogClass.progressDialog(this@LeadDetailActivity)
        val queryParams = java.util.HashMap<String, String>()
        queryParams["checkin_id"] = StaticSharedpreference.getInfo(Constant.Lead_check_in, this@LeadDetailActivity).toString()
        queryParams["checkout_latitude"] = latitude
        queryParams["checkout_longitude"] = longitude
        queryParams["description"] = note
        queryParams["lead_id"] = lead_id

        println("abbbbbbbbbbbbbb==="+queryParams)


        ApiClient.submitleadcheckout(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this@LeadDetailActivity).toString(),
            queryParams,
            object : APIResultLitener<AttendanceSubmitModel> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onAPIResult(
                    response: Response<AttendanceSubmitModel>?,
                    errorMessage: String?
                ) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {
                            Toast.makeText(this@LeadDetailActivity, response.body()!!.message, Toast.LENGTH_LONG).show()
                            StaticSharedpreference.saveInfo(Constant.Lead_check_in,"",this@LeadDetailActivity)
                            StaticSharedpreference.saveInfo(Constant.Lead_check_in_leadID,"",this@LeadDetailActivity)
                            lead_check_in = ""
                            relll_check_in.setBackgroundColor(Color.parseColor("#FFFFFF"))
                            tv_check_in.setTextColor(Color.parseColor("#00ACD8"))
                            val drawable2 = ContextCompat.getDrawable(this@LeadDetailActivity, R.drawable.ic_lead_checkin_not)
                            tv_check_in.setCompoundDrawablesWithIntrinsicBounds(drawable2, null, null, null)

                            relll_check_out.setBackgroundColor(Color.parseColor("#FFFFFF"))
                            tv_checkout.setTextColor(Color.parseColor("#00ACD8"))
                            val drawable3 = ContextCompat.getDrawable(this@LeadDetailActivity, R.drawable.ic_lead_checkout_not)
                            tv_checkout.setCompoundDrawablesWithIntrinsicBounds(drawable3, null, null, null)

                        } else {
                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())
                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    this@LeadDetailActivity, false
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            })
    }

    private fun submitopportunity(
        leadId: String,
        edtamount: String,
        confidence: String,
        selectedtaskuserid: String,
        selectedcontactId: String,
        note: String,
        date: String,
        selectedstatusId: String)
    {
        if (!Utilities.isOnline(this@LeadDetailActivity)) {
            return
        }
        var dialog = DialogClass.progressDialog(this@LeadDetailActivity)
        val queryParams = java.util.HashMap<String, String>()
        queryParams["lead_id"] = leadId
        queryParams["amount"] = edtamount
        queryParams["confidence"] = confidence
        queryParams["assigned_to"] = selectedtaskuserid
        queryParams["lead_contact_id"] = selectedcontactId
        queryParams["note"] = note
        queryParams["estimated_close_date"] = date
        queryParams["status"] = selectedstatusId


        ApiClient.submitleadopportunity(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this@LeadDetailActivity).toString(),
            queryParams,
            object : APIResultLitener<AttendanceSubmitModel> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onAPIResult(
                    response: Response<AttendanceSubmitModel>?,
                    errorMessage: String?
                ) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {
                            Toast.makeText(this@LeadDetailActivity, response.body()!!.message, Toast.LENGTH_LONG).show()
                            getleaddetail(lead_id)
                            alertDialog.dismiss()
                        } else {
                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    this@LeadDetailActivity, false
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            })
    }

    private fun submitedit(
        selectedtypeId: String,
        edtfirmname: String,
        edtcustomername: String,
        edtmobno: String,
        edtemail: String,
        edtadd: String,
        pincodeId: String,
        stateId: String,
        cityId: String,
        districtId: String,
        selectedsourceId: String,
        edtnote: String,
        edtwebsite: String,
        alertDialog: AlertDialog,
        lead_id: String,
        selectedtaskuser_id: String
    ) {
        if (!Utilities.isOnline(this@LeadDetailActivity)) {
            return
        }
        var dialog = DialogClass.progressDialog(this@LeadDetailActivity)
        val queryParams = java.util.HashMap<String, String>()
        queryParams["lead_id"] = lead_id
        queryParams["status"] = selectedtypeId
        queryParams["company_name"] = edtfirmname
        queryParams["contact_name"] = edtcustomername
        queryParams["phone_number"] = edtmobno
        queryParams["email"] = edtemail
        queryParams["address"] = edtadd
        queryParams["pincode_id"] = pincodeId
        queryParams["state_id"] = stateId
        queryParams["city_id"] = cityId
        queryParams["district_id"] = districtId
        queryParams["website"] = edtwebsite
        queryParams["lead_source"] = selectedsourceId
        queryParams["note"] = edtnote
        queryParams["assign_to"] = selectedtaskuser_id

        ApiClient.submitcreatelead(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this@LeadDetailActivity).toString(),
            queryParams,
            object : APIResultLitener<AttendanceSubmitModel> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onAPIResult(
                    response: Response<AttendanceSubmitModel>?,
                    errorMessage: String?
                ) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {
                            Toast.makeText(this@LeadDetailActivity, response.body()!!.message, Toast.LENGTH_LONG).show()
                            getleaddetail(lead_id)
                            alertDialog.dismiss()
                        } else {
                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    this@LeadDetailActivity, false
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            })
    }

    private fun responsemessage(s: String) {
        Toast.makeText(this@LeadDetailActivity,s,Toast.LENGTH_SHORT).show()
    }

    lateinit var dialog: Dialog
    private fun getPincodeInfo(
        pincode: String,
        edtState: EditText,
        edtCity: EditText,
        edtdistric: EditText
    ) {
        if (!Utilities.isOnline(this)) {
            return
        }
        dialog = DialogClass.progressDialog(this)
        val queryParams = java.util.HashMap<String, String>()
        queryParams["pincode"] = pincode
        ApiClient.getPincodeInfo(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this).toString(),
            queryParams,
            object :
                APIResultLitener<JsonObject> {
                override fun onAPIResult(
                    response: Response<JsonObject>?,
                    errorMessage: String?
                ) {

                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {
                            dialog.dismiss()

                            try {
                                var city = response.body()!!
                                    .get("data").asJsonObject.get("city_name").asString
                                var state = response.body()!!
                                    .get("data").asJsonObject.get("state_name").asString
                                var distric = response.body()!!
                                    .get("data").asJsonObject.get("district_name").asString

                                pincode_id = response.body()!!
                                    .get("data").asJsonObject.get("id").asString
                                city_id = response.body()!!
                                    .get("data").asJsonObject.get("city_id").asString
                                state_id = response.body()!!
                                    .get("data").asJsonObject.get("state_id").asString
                                district_id = response.body()!!
                                    .get("data").asJsonObject.get("district_id").asString

                                edtState.setText(state)
                                edtCity.setText(city)
                                edtdistric.setText(distric)
                            } catch (e: Exception) {
                                Toast.makeText(this@LeadDetailActivity, "Please enter correct pin", Toast.LENGTH_LONG).show()
                            }

                        } else {
                            dialog.dismiss()

                            var jsonObject: JSONObject? = null
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    this@LeadDetailActivity,
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
            })
    }

    private fun spinnersource(edtleadsource: AutoCompleteTextView) {
        val builder = android.app.AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_alert_dialog2, null)
        builder.setView(dialogView)

        val searchEditText = dialogView.findViewById<EditText>(R.id.searchEditText)
        val listView = dialogView.findViewById<ListView>(R.id.listView)
        val button = dialogView.findViewById<Button>(R.id.button)

        val colorsArray = leadcreatesourcename.map { it.toString() }.toTypedArray()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, colorsArray)
        listView.adapter = adapter

        builder.setTitle("Select Source")

        val dialog = builder.create()

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {
                adapter.filter.filter(charSequence)
            }

            override fun afterTextChanged(editable: Editable) {}
        })
        button.setOnClickListener {
            edtleadsource.setText("")
            selectedsource_id = ""
            dialog.dismiss()
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedPosition = leadcreatesourcename.indexOf(adapter.getItem(position).toString())
            if (selectedPosition != -1) {
                val selectedParentId = leadcreatesourceid[selectedPosition].toString()
                val selectedParentName = leadcreatesourcename[selectedPosition].toString()

                edtleadsource.setText(selectedParentName)
                selectedsource_id = selectedParentId

                println("Abhi=id=$selectedsource_id")


                dialog.dismiss()
            }
        }

        dialog.show() // Show the dialog
    }


    private fun spinnerstatus(edtleadtype: AutoCompleteTextView) {
        val builder = android.app.AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_alert_dialog2, null)
        builder.setView(dialogView)

        val searchEditText = dialogView.findViewById<EditText>(R.id.searchEditText)
        val listView = dialogView.findViewById<ListView>(R.id.listView)
        val button = dialogView.findViewById<Button>(R.id.button)

        val colorsArray = leadcreatestatusname.map { it.toString() }.toTypedArray()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, colorsArray)
        listView.adapter = adapter

        builder.setTitle("Select Type")

        val dialog = builder.create()

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {
                adapter.filter.filter(charSequence)
            }

            override fun afterTextChanged(editable: Editable) {}
        })
        button.setOnClickListener {
//            edtleadtype.setText("")
//            selectedtype_id = ""
            dialog.dismiss()
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedPosition = leadcreatestatusname.indexOf(adapter.getItem(position).toString())
            if (selectedPosition != -1) {
                val selectedParentId = leadcreatestatusid[selectedPosition].toString()
                val selectedParentName = leadcreatestatusname[selectedPosition].toString()

                edtleadtype.setText(selectedParentName)
                selectedtype_id = selectedParentId

                println("Abhi=id=$selectedtype_id")

                updatelead(selectedtype_id,lead_id)


                dialog.dismiss()
            }
        }

        dialog.show() // Show the dialog
    }



    private fun getleadstatussource() {
        isLoading = true

        if (!Utilities.isOnline(this)) {
            isLoading = false
            return
        }
        var dialog = DialogClass.progressDialog(this)
        val queryParams = HashMap<String, String>()

        ApiClient.getleadstatussource(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this).toString(),
            queryParams,
            object : APIResultLitener<LeadStatusSourceModel> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onAPIResult(response: Response<LeadStatusSourceModel>?, errorMessage: String?) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {
                            leadcreatestatuslist.clear()
                            leadcreatesourcelist.clear()


                            leadcreatestatuslist.addAll(response.body()!!.data!!.status)
                            leadcreatesourcelist.addAll(response.body()!!.data!!.source)

                            for (item in leadcreatestatuslist) {
                                val name = item.displayName.toString()
                                val id = item.id.toString()

                                if (!leadcreatestatusname.contains(name)) {
                                    leadcreatestatusname.add(name)
                                    leadcreatestatusid.add(id)
                                }
                            }

                            for (item in leadcreatesourcelist) {
                                val name = item.value.toString()
                                val id = item.key.toString()

                                if (!leadcreatesourcename.contains(name)) {
                                    leadcreatesourcename.add(name)
                                    leadcreatesourceid.add(id)
                                }
                            }


                        } else {

                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    this@LeadDetailActivity, false
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        isLoading = false
                    }
                    else {
                        Toast.makeText(this@LeadDetailActivity, resources.getString(R.string.poor_connection), Toast.LENGTH_LONG).show()
                    }
                }
            })
    }


    private fun gettaskdropdown() {
        isLoading = true

        if (!Utilities.isOnline(this)) {
            isLoading = false
            return
        }
        var dialog = DialogClass.progressDialog(this)
        val queryParams = HashMap<String, String>()

        println("queryParams=="+queryParams)
        ApiClient.getleadtaskdropdown(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this).toString(),
            queryParams,
            object : APIResultLitener<TaskDropdownModel> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onAPIResult(response: Response<TaskDropdownModel>?, errorMessage: String?) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {
                            leaduserlist.clear()
                            leadprioritylist.clear()
                            leadstatuslist.clear()


                            leaduserlist.addAll(response.body()!!.data!!.users)
                            leadprioritylist.addAll(response.body()!!.data!!.priorities)
                            leadstatuslist.addAll(response.body()!!.data!!.status)

                            for (item in leaduserlist) {
                                val name = item.name.toString()
                                val id = item.id.toString()

                                if (!leadusersname.contains(name)) {
                                    leadusersname.add(name)
                                    leadusersid.add(id)
                                }
                            }

                            for (item in leadprioritylist) {
                                val name = item.name.toString()
                                val id = item.id.toString()

                                if (!leadpriorityname.contains(name)) {
                                    leadpriorityname.add(name)
                                    leadpriorityid.add(id)
                                }
                            }
                            for (item in leadstatuslist) {
                                val name = item.name.toString()
                                val id = item.id.toString()

                                if (!leadstatusname.contains(name)) {
                                    leadstatusname.add(name)
                                    leadstatusid.add(id)
                                }
                            }


                        }
                        else {
                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    this@LeadDetailActivity, false
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        isLoading = false
                    }
                    else {
                        Toast.makeText(this@LeadDetailActivity, resources.getString(R.string.poor_connection), Toast.LENGTH_LONG).show()
                    }
                }
            })
    }


    private fun getleadcontact() {
        isLoading = true

        if (!Utilities.isOnline(this)) {
            isLoading = false
            return
        }
        var dialog = DialogClass.progressDialog(this)
        val queryParams = HashMap<String, String>()
        queryParams["lead_id"] = lead_id

        println("queryParams=="+queryParams)
        ApiClient.getleadcontact(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this).toString(),
            queryParams,
            object : APIResultLitener<LeadContactModel> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onAPIResult(response: Response<LeadContactModel>?, errorMessage: String?) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {
                            leadcontactlist.clear()
                            leadoppstatuslist.clear()


                            leadcontactlist.addAll(response.body()!!.data!!.contacts)
                            leadoppstatuslist.addAll(response.body()!!.data!!.opportunityStatuses)

                            for (item in leadcontactlist) {
                                val name = item.name.toString()
                                val id = item.id.toString()

                                if (!leadcontactname.contains(name)) {
                                    leadcontactname.add(name)
                                    leadcontactid.add(id)
                                }
                            }

                            for (item in leadoppstatuslist) {
                                val name = item.statusName.toString()
                                val id = item.id.toString()

                                if (!leadoppstatusname.contains(name)) {
                                    leadoppstatusname.add(name)
                                    leadoppstatusid.add(id)
                                }
                            }

                        }
                        else {
                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    this@LeadDetailActivity, false
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        isLoading = false
                    }
                    else {
                        Toast.makeText(this@LeadDetailActivity, resources.getString(R.string.poor_connection), Toast.LENGTH_LONG).show()
                    }
                }
            })
    }


    private fun getleaddetail(leadId: String) {
        isLoading = true

        if (!Utilities.isOnline(this)) {
            isLoading = false
            return
        }
        var dialog = DialogClass.progressDialog(this)
        val queryParams = HashMap<String, String>()
        queryParams["lead_id"] = leadId

        println("queryParams=="+queryParams)
        ApiClient.getleaddetail(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this).toString(),
            queryParams,
            object : APIResultLitener<LeadDetailModel> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onAPIResult(response: Response<LeadDetailModel>?, errorMessage: String?) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {
                            lead_activity_response.clear()

                            setupdata(response.body()!!.data)
                            lead_response = response!!.body()!!.data
                            lead_activity_response.addAll(response!!.body()!!.notesTasks)
                            notification_count.text = response.body()!!.notification_count.toString()
                        } else {

                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    this@LeadDetailActivity, false
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        isLoading = false
                    }
                    else {
                        Toast.makeText(this@LeadDetailActivity, resources.getString(R.string.poor_connection), Toast.LENGTH_LONG).show()
                    }
                }
            })
    }

    private fun setupdata(data: LeadDetailModel.Data?) {

        if (!data!!.companyName.isNullOrEmpty()){
            val firstLetter = data.companyName!!.trim().firstOrNull()?.uppercaseChar() ?: ""
            ic_logo_name.text = firstLetter.toString()
        }
        if (!data!!.companyName.isNullOrEmpty()){
            tv_firm_name.text = data!!.companyName.toString()
        }
        if (!data!!.contactName.isNullOrEmpty()){
            tv_name.text = data!!.contactName.toString()
        }
        if (!data!!.website.isNullOrEmpty()){
            tv_url.text = data!!.website.toString()
        }
        if (!data!!.phoneNumber.isNullOrEmpty()){
            tv_mob.text = data!!.phoneNumber.toString()
        }
        if (!data!!.email.isNullOrEmpty()){
            tv_email.text = data!!.email.toString()
        }
        if (!data!!.address.isNullOrEmpty()){
            tv_loc.text = data!!.address.toString()
        }
        if (!data!!.pincode.isNullOrEmpty()){
            tv_pin.text = data!!.pincode.toString()
        }
        if (!data!!.status.isNullOrEmpty()){
            edtleadtype.setText(data!!.status.toString())
        }
        if (!data!!.leadSource.isNullOrEmpty()){
            edtleadsource.setText(data!!.leadSource.toString())
        }
        if (!data!!.note.isNullOrEmpty()){
            val spanned: Spanned = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(data!!.note ?: "", Html.FROM_HTML_MODE_COMPACT)
            } else {
                Html.fromHtml(data!!.note ?: "")
            }
            tvuser_notee.text = spanned
        }
        if (!data!!.leadGenerationDate.isNullOrEmpty()){
            tv_lead_date.setText(data!!.leadGenerationDate.toString())
        }
        if (!data!!.conversion_date.isNullOrEmpty()){
            tv_converted_date.setText(data!!.conversion_date.toString())
        }
        if (!data!!.updatedAt.isNullOrEmpty()){
            tv_action_date.setText(data!!.updatedAt.toString())
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        handelbackpress()
    }

    private fun handelbackpress() {
        println("lead_check_inlead_check_in=="+lead_check_in)
        if (!lead_check_in.isNullOrEmpty()){
            var intent = Intent(this@LeadDetailActivity,MainActivity::class.java)
            startActivity(intent)
        }else{
            var intent = Intent(this@LeadDetailActivity,LeadActivity::class.java)
            startActivity(intent)
        }
    }

    private fun shownotepopupedit(notesTasks: LeadDetailModel.NotesTasks) {
        val builder = AlertDialog.Builder(this)
        val inflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.popup_note_lead_layout, null)
        builder.setCancelable(false)

        val img_close: ImageView = view.findViewById(R.id.img_close)
        val edtnote: AutoCompleteTextView = view.findViewById(R.id.edtnote)
        val cardSubmit: CardView = view.findViewById(R.id.cardSubmit)


        if (notesTasks!= null){
            edtnote.setText(notesTasks!!.note.toString())
        }

        cardSubmit.setOnClickListener {
            if (edtnote.text.toString().isNullOrEmpty()){
                responsemessage("Please Enter Note")
            }else{
                submitnote(edtnote.text.toString(),notesTasks!!.id.toString(),notesTasks!!.leadId.toString())
            }
        }


        img_close.setOnClickListener {
            alertDialog.dismiss()
        }

        builder.setView(view)
        alertDialog = builder.create()
        alertDialog.show()

    }

    override fun onClickactivityedit(id: Int?, type: String, notesTasks: LeadDetailModel.NotesTasks) {
        println("EDIII==="+id+"<<"+type+"<<"+notesTasks)
        if (type.equals("note")){
            alertDialog.dismiss()
            shownotepopupedit(notesTasks)
        }else if (type.equals("task")){
            alertDialog.dismiss()
            showtaskpopupedit(notesTasks)
        }else{
            alertDialog.dismiss()
        }
    }

    private fun showtaskpopupedit(notesTasks: LeadDetailModel.NotesTasks) {
        val builder = AlertDialog.Builder(this)
        val inflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.popup_task_lead_layout, null)
        builder.setCancelable(false)

        val img_close: ImageView = view.findViewById(R.id.img_close)
        val edtleadusername: AutoCompleteTextView = view.findViewById(R.id.edtleadusername)
        val edtacccview: AutoCompleteTextView = view.findViewById(R.id.edtacccview)
        val edtpriority: AutoCompleteTextView = view.findViewById(R.id.edtpriority)
//        val edtstatus: AutoCompleteTextView = view.findViewById(R.id.edtstatus)
        val edtdate: AutoCompleteTextView = view.findViewById(R.id.edtdate)
        val edttime: AutoCompleteTextView = view.findViewById(R.id.edttime)
        val cardcancel: CardView = view.findViewById(R.id.cardcancel)
        val cardsave: CardView = view.findViewById(R.id.cardsave)

        if (notesTasks != null){
            edtleadusername.setText(notesTasks.assignUse!!.name!!.toString())
            edtacccview.setText(notesTasks.description!!.toString())
            edtpriority.setText(notesTasks.priority!!.toString())
//            edtstatus.setText(notesTasks.status!!.toString())
            edtdate.setText(notesTasks.date!!.toString())
            edttime.setText(notesTasks.time!!.toString())

            selectedtaskuser_id = notesTasks.assignedTo!!.toString()
            selectedtaskpriority_id = notesTasks.priority!!.toString()
//            selectedtaskstatus_id = notesTasks.status!!.toString()
        }

        edtleadusername.setOnClickListener {
            spinnerusername(edtleadusername)
        }

        edtpriority.setOnClickListener {
            spinnerpriority(edtpriority)
        }

        /*edtstatus.setOnClickListener {
            spinnertaskstatus(edtstatus)
        }*/
        edtdate.setOnClickListener {
            Utilities.datePicker(edtdate,this@LeadDetailActivity)
        }
        edttime.setOnClickListener {
            Utilities.show24HourTimePicker(this@LeadDetailActivity,edttime)
        }

        cardsave.setOnClickListener {
            var date = Utilities.convertDateFormat(edtdate.text.toString())
            val time = edttime.text.toString().trim()

            println("abhiiiiiii=="+date+"<<"+time)


            if (selectedtaskuser_id.isNullOrEmpty()){
                responsemessage("Please Select User")
            }else if (edtacccview.text.isNullOrEmpty()){
                responsemessage("Please Enter Description")
            }else if (selectedtaskpriority_id.isNullOrEmpty()){
                responsemessage("Please Select Priority")
            }/*else if (selectedtaskstatus_id.isNullOrEmpty()){
                responsemessage("Please Select Status")
            }*/else if (date.isNullOrEmpty()){
                responsemessage("Please Select Date")
            }else{
                submittask(lead_id,selectedtaskuser_id,edtacccview.text.toString(),selectedtaskpriority_id,
                    date,time,notesTasks!!.id.toString())
            }
        }


        img_close.setOnClickListener {
            alertDialog.dismiss()
        }
        cardcancel.setOnClickListener {
            alertDialog.dismiss()
        }

        builder.setView(view)
        alertDialog = builder.create()
        alertDialog.show()
    }
}