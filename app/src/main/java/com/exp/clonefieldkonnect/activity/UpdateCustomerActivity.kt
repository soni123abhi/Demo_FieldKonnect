package com.exp.clonefieldkonnect.activity

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.activity.StoreCustomerActivity.Companion.arrList
import com.exp.clonefieldkonnect.adapter.EnquiryInfoAdapter
import com.exp.clonefieldkonnect.connection.APIResultLitener
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.helper.Constant
import com.exp.clonefieldkonnect.helper.DialogClass
import com.exp.clonefieldkonnect.helper.GPSTracker
import com.exp.clonefieldkonnect.helper.StaticSharedpreference
import com.exp.clonefieldkonnect.model.*
import com.exp.import.Utilities
import com.bumptech.glide.Glide
import com.exp.clonefieldkonnect.adapter.CustomerParentAdapter
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import id.zelory.compressor.Compressor
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class UpdateCustomerActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var edtFirmName: EditText
    private lateinit var edtFName: EditText
    private lateinit var edtLastName: EditText
    private lateinit var edtMobile: EditText
    private lateinit var edtalternateMobile: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtMarket: EditText
    private lateinit var cardSubmit: CardView
    private lateinit var cardBack: CardView
    private lateinit var edtAddress1: EditText
    private val REQUEST_CHECK_SETTINGS = 0x1
    private var mGoogleApiClient: GoogleApiClient? = null
    var latitude: String = ""
    var longitude: String = ""
    var parent_id: String = ""
    lateinit var spinnerBeat: AutoCompleteTextView
    var gstImgFile: File? = null
    var adharImgFile: File? = null
    var otherImgFile: File? = null
    var panImgFile: File? = null
    var outletImgFile: File? = null
    var visitingCardImgFile: File? = null
    lateinit var spinnerCustomerType: AutoCompleteTextView
    lateinit var spinnerparentType: AutoCompleteTextView
    lateinit var spinnerGrade: AutoCompleteTextView
    lateinit var spinnerStatus: AutoCompleteTextView
    lateinit var spinnerPin: EditText
    lateinit var edtState: EditText
    lateinit var edtCity: EditText

    lateinit var edtGSTIN: EditText
    lateinit var edtAdhar: EditText
    lateinit var edtPAN: EditText
    lateinit var edtOther: EditText

    lateinit var imgProfile: ImageView
    lateinit var imgProfile2: ImageView

    lateinit var img: RelativeLayout
    lateinit var img2: RelativeLayout
    lateinit var relativeKYCOpen: RelativeLayout
    lateinit var relativeSurveyOpen: RelativeLayout
    lateinit var linearKYCBox: LinearLayout

    lateinit var imgGST: ImageView
    lateinit var imgAdhar: ImageView
    lateinit var imgPAN: ImageView
    lateinit var imgOther: ImageView
    lateinit var imgArrowSurvey: ImageView
    lateinit var imgArrowKYC: ImageView

    lateinit var beatArr: ArrayList<BeatModel>
    lateinit var stateModel: List<StateModel.Datum>
    lateinit var districtModel: List<DistrictModel.Datum>
    lateinit var cityModel: List<CityModel.Datum>
    lateinit var pinModel: List<PinCodeModel.Datum>
    var dataArray: JsonArray = JsonArray()
    var CustomerParentArr: ArrayList<CustomerParentModel.Data> = arrayListOf()
    private var items: MutableList<com.devstune.searchablemultiselectspinner.SearchableItem> = ArrayList()
    var arrparent : ArrayList<String> = arrayListOf()
    var arrparentid : ArrayList<String> = arrayListOf()
    var selectedParentIds : ArrayList<String> = arrayListOf()

    private lateinit var checkbox: CheckBox
    val INTENTCAMERA = 4
    val INTENTGALLERY = 5
    lateinit var cameraFile: File
    var beatPos: String = ""
    var customerTypePos: String = ""
    var customerId: String = ""
    var statePos: String = ""
    var districtPos: String = ""
    var cityPos: String = ""
    var pinPos: String = ""
    var countryPos: String = ""
    var selectedImg = ""
    var customerTypeArr: ArrayList<CustomerTypeModel?> = arrayListOf();
    var tata = ""
    var otherStore = ""
    var tractor = ""
    var mm = ""
    var layland = ""
    private var lastPosition = -1
    private var isLoading = false
    private var page = 1
    private var pageSize = "100"
    private var parent_search = ""

    var arrGrade = arrayListOf<String>(
        "Grade A", "Grade B", "Grade C", "Grade D"

    )
    var arrStatus = arrayListOf<String>(
        "Hot", "Warm", "Cold", "Existing"

    )


    lateinit var chTATA_MAV: CheckBox
    lateinit var chTATA_HCV: CheckBox
    lateinit var chTATA_LCV: CheckBox
    lateinit var chTATA_Other: CheckBox

    lateinit var chLEYLAND_MAV: CheckBox
    lateinit var chLEYLAND_HCV: CheckBox
    lateinit var chLEYLAND_Other: CheckBox

    lateinit var chMM_HCV: CheckBox
    lateinit var chMM_LCV: CheckBox
    lateinit var chMM_Other: CheckBox

    lateinit var chTractor_YES: CheckBox

    lateinit var chOther_MAV: CheckBox
    lateinit var chOther_HCV: CheckBox
    lateinit var chOther_LCV: CheckBox
    lateinit var chOther_LMV: CheckBox
    lateinit var chOther_Other: CheckBox

    lateinit var linearTATA: LinearLayout
    lateinit var linearLeyland: LinearLayout
    lateinit var linearMM: LinearLayout
    lateinit var linearTractor: LinearLayout
    lateinit var linearOther: LinearLayout

    lateinit var tvTATA: TextView
    lateinit var tvLeyland: TextView
    lateinit var tvMM: TextView
    lateinit var tvTractor: TextView
    lateinit var tvOther: TextView

    lateinit var rv: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_customer)
        arrList.clear()
        initViews()
    }

    private fun initViews() {
        chTATA_MAV = findViewById(R.id.chTATA_MAV)
        chTATA_HCV = findViewById(R.id.chTATA_HCV)
        chTATA_LCV = findViewById(R.id.chTATA_LCV)
        chTATA_Other = findViewById(R.id.chTATA_Other)

        chLEYLAND_MAV = findViewById(R.id.chLEYLAND_MAV)
        chLEYLAND_HCV = findViewById(R.id.chLEYLAND_HCV)
        chLEYLAND_Other = findViewById(R.id.chLEYLAND_Other)

        chMM_HCV = findViewById(R.id.chMM_HCV)
        chMM_LCV = findViewById(R.id.chMM_LCV)
        chMM_Other = findViewById(R.id.chMM_Other)

        chTractor_YES = findViewById(R.id.chTractor_YES)

        chOther_MAV = findViewById(R.id.chOther_MAV)
        chOther_HCV = findViewById(R.id.chOther_HCV)
        chOther_LCV = findViewById(R.id.chOther_LCV)
        chOther_LMV = findViewById(R.id.chOther_LMV)
        chOther_Other = findViewById(R.id.chOther_Other)
        linearTATA = findViewById(R.id.linearTATA)
        linearLeyland = findViewById(R.id.linearLeyland)
        linearMM = findViewById(R.id.linearMM)
        linearTractor = findViewById(R.id.linearTractor)
        linearOther = findViewById(R.id.linearOther)
        tvTATA = findViewById(R.id.tvTATA)
        tvLeyland = findViewById(R.id.tvLeyland)
        tvMM = findViewById(R.id.tvMM)
        tvTractor = findViewById(R.id.tvTractor)
        tvOther = findViewById(R.id.tvOther)

        rv = findViewById(R.id.rv)
        edtCity = findViewById(R.id.edtCity)
        edtState = findViewById(R.id.edtState)

        spinnerStatus = findViewById(R.id.spinnerStatus)
        spinnerGrade = findViewById(R.id.spinnerGrade)
        linearKYCBox = findViewById(R.id.linearKYCBox)
        spinnerCustomerType = findViewById(R.id.spinnerCustomerType)
        spinnerparentType = findViewById(R.id.spinnerparentType)
        relativeSurveyOpen = findViewById(R.id.relativeSurveyOpen)
        spinnerBeat = findViewById(R.id.spinnerBeat)
        checkbox = findViewById(R.id.checkbox)
        spinnerPin = findViewById(R.id.spinnerPin)
        edtAddress1 = findViewById(R.id.edtAddress1)

        cardBack = findViewById(R.id.cardBack)
        cardSubmit = findViewById(R.id.cardSubmit)
        edtFirmName = findViewById(R.id.edtFirmName)
        edtFName = findViewById(R.id.edtFName)
        edtLastName = findViewById(R.id.edtLastName)
        edtMobile = findViewById(R.id.edtMobile)
        edtalternateMobile = findViewById(R.id.edtalternateMobile)
        edtEmail = findViewById(R.id.edtEmail)
        edtMarket = findViewById(R.id.edtMarket)
        img = findViewById(R.id.img)
        imgProfile = findViewById(R.id.imgProfile)
        img2 = findViewById(R.id.img2)
        imgProfile2 = findViewById(R.id.imgProfile2)
        imgArrowSurvey = findViewById(R.id.imgArrowSurvey)
        imgArrowKYC = findViewById(R.id.imgArrowKYC)


        edtOther = findViewById(R.id.edtOther)
        edtPAN = findViewById(R.id.edtPAN)
        edtAdhar = findViewById(R.id.edtAdhar)
        edtGSTIN = findViewById(R.id.edtGSTIN)

        relativeKYCOpen = findViewById(R.id.relativeKYCOpen)

        imgGST = findViewById(R.id.imgGST)
        imgAdhar = findViewById(R.id.imgAdhar)
        imgPAN = findViewById(R.id.imgPAN)
        imgOther = findViewById(R.id.imgOther)


        cardSubmit.setOnClickListener(this)
        cardBack.setOnClickListener(this)
        img.setOnClickListener(this)
        img2.setOnClickListener(this)

        imgGST.setOnClickListener(this)
        imgAdhar.setOnClickListener(this)
        imgPAN.setOnClickListener(this)
        imgOther.setOnClickListener(this)
        relativeSurveyOpen.setOnClickListener(this)
        spinnerCustomerType.setOnClickListener(this)
        spinnerparentType.setOnClickListener(this)
        tvTATA.setOnClickListener(this)
        tvLeyland.setOnClickListener(this)
        tvMM.setOnClickListener(this)
        tvTractor.setOnClickListener(this)
        tvOther.setOnClickListener(this)


        relativeKYCOpen.setOnClickListener(this)

        spinnerBeat.setOnClickListener {
            spinnerBeat.showDropDown()
        }
        customerTypePos = intent.getStringExtra("customerTypeId").toString()
        customerId = intent.getStringExtra("customerId").toString()

        spinnerStatus.setOnClickListener {
            spinnerStatus.showDropDown()
        }
        spinnerGrade.setOnClickListener {
            spinnerGrade.showDropDown()
        }

        spinnerBeat.setOnItemClickListener { adapterView, view, i, l ->

            beatPos = beatArr[i].beatId.toString()
        }

        spinnerCustomerType.setOnItemClickListener { adapterView, view, i, l ->

            customerTypePos = customerTypeArr[i]!!.customertype.toString()

            getSurveyQuestions(customerTypePos)
        }

        spinnerPin.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if (spinnerPin.text.toString().length == 6) {
                    getPincodeInfo(spinnerPin.text.toString())
                } else if (spinnerPin.text.toString().length < 6) {
                    edtState.setText("")
                    edtCity.setText("")
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

        getBeatList()
        getparentlist(page, pageSize, parent_search)


        spinnerparentType.setOnClickListener{
            showparentdropdown(CustomerParentArr)
        }

        // getStateList()
    }

    private fun getparentlist(page: Int, pageSize: String, parent_search: String) {
        isLoading = true

        if (!Utilities.isOnline(this)) {
            isLoading = false
            return
        }
        val dialog = DialogClass.progressDialog(this)
        val queryParams = HashMap<String, String>()
        queryParams["pageSize"] = pageSize
        queryParams["page"] = page.toString()
        queryParams["search"] = parent_search

        ApiClient.getParentlist(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this).toString(),
            queryParams,
            object : APIResultLitener<CustomerParentModel> {
                override fun onAPIResult(response: Response<CustomerParentModel>?, errorMessage: String?) {
                    dialog.dismiss()

                    if (response != null && errorMessage == null) {
                        if (response.code() == 200) {
                            if (page == 1) {
                                CustomerParentArr.clear() // Clear the previous results for the new search
                            }
                            CustomerParentArr.addAll(response.body()!!.data)
                            println("CustomerParentArr=" + page+"<<"+parent_search)

                            if (::currentDialog.isInitialized && currentDialog.isShowing) {
                                (currentDialog.findViewById<RecyclerView>(R.id.recycler_dropdown).adapter as CustomerParentAdapter)
                                    .updateList(CustomerParentArr)
                            }
                        } else {
                            Toast.makeText(this@UpdateCustomerActivity,response.message(),Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@UpdateCustomerActivity, resources.getString(R.string.poor_connection), Toast.LENGTH_LONG).show()
                    }
                    isLoading = false
                }
            })
    }
    private lateinit var currentDialog: Dialog

    private fun showparentdropdown(customerParentArr: ArrayList<CustomerParentModel.Data>) {
        currentDialog = Dialog(this) // Initialize the dialog
        currentDialog.setContentView(R.layout.dialog_custom_parent_dropdown)

        val searchInput = currentDialog.findViewById<EditText>(R.id.search_input)
        val searchIcon = currentDialog.findViewById<ImageView>(R.id.search_icon)
        val recyclerView = currentDialog.findViewById<RecyclerView>(R.id.recycler_dropdown)
        val doneButton = currentDialog.findViewById<Button>(R.id.btn_done)

        val adapter = CustomerParentAdapter(customerParentArr) { selectedItems ->
            Log.d("SelectedItems", selectedItems.joinToString { it.name ?: "Unknown" })
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
                    if (CustomerParentArr.size >= 100) {
                        getparentlist(page, pageSize, "") // Call the API to load more items
                        lastPosition = firstVisibleItemPosition
                    }
                }
            }
        })

        searchIcon.setOnClickListener {
            val parent_search = searchInput.text.toString()
            page = 1 // Reset page to 1 for a new search
            getparentlist(page, pageSize, parent_search) // Call API with the search query
        }

        doneButton.setOnClickListener {
            val selectedItems = adapter.getSelectedItems()
            Log.d("DoneClicked", "Selected Items: ${selectedItems.joinToString { it.name ?: "Unknown" }}")

            val selectedCodes = selectedItems.map { it.name ?: "Unknown" }
            val selectedCityText = selectedCodes.joinToString(",")
            spinnerparentType.setText(selectedCityText)

            val selectedIds = selectedItems.map { it.id.toString() }
            parent_id = selectedIds.joinToString(",")
            println("DoneClickedparent_id== $parent_id")
            page = 1
            getparentlist(page, pageSize, "")
            currentDialog.dismiss()
        }

        currentDialog.show()
    }

    private fun spinnerParent() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_alert_dialog, null)
        builder.setView(dialogView)

        val searchEditText = dialogView.findViewById<EditText>(R.id.searchEditText)
        val listView = dialogView.findViewById<ListView>(R.id.listView)

        val colorsArray = arrparent.map { it.toString() }.toTypedArray()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, colorsArray)
        listView.adapter = adapter

        val selectedItemId = arrparentid.indexOf(selectedParentIds.firstOrNull())

        builder.setTitle("Select Parent")

        val dialog = builder.create()

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {
                adapter.filter.filter(charSequence)
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedPosition = arrparent.indexOf(adapter.getItem(position).toString())
            if (selectedPosition != -1) {
                val selectedParentId = arrparentid[selectedPosition].toString()
                val selectedParentName = arrparent[selectedPosition].toString()

                spinnerparentType.setText(selectedParentName)
                selectedParentIds.clear()
                selectedParentIds.add(selectedParentId)
                parent_id = selectedParentId

                println("Abhi=mname=$selectedParentName")
                println("Abhi=id=$selectedParentId")
                parent_id = selectedParentId

                dialog.dismiss()
            }
        }

        builder.setNeutralButton("Cancel") { _, _ ->
            // Handle cancel action if needed
        }
        dialog.show()
    }

    /*private fun spinnerParent1() {
        items.clear()
        for (item in CustomerParentArr) {
            items.add(item.name.toString(), item.id.toString(), item.isSelected)
        }

        SearchableMultiSelectSpinner.show(this, "Select Parent Customer", "Done", items, object :
            SelectionCompleteListener {
            override fun onCompleteSelection(selectedItems: ArrayList<SearchableItem>) {
                Log.e("data", selectedItems.toString())
                val selectedCity = selectedItems.map { it.text }
                val selectedCityText = selectedCity.joinToString(",") // Use your preferred delimiter
                spinnerparentType.setText(selectedCityText)

                val selectedCodes = selectedItems.map { it.code }
                parent_id = selectedCodes.joinToString(",")
                println("cityyy=selectedCodes=$selectedItems")
                println("cityyy=codesString=$parent_id")
            }
        })
    }*/


    override fun onClick(p0: View) {
        when (p0.id) {

            R.id.cardBack -> {
                onBackPressed()
            }

            R.id.relativeKYCOpen -> {
                if (linearKYCBox.visibility == View.VISIBLE) {
                    linearKYCBox.visibility = View.GONE
                    val a: Animation = AnimationUtils.loadAnimation(
                        this@UpdateCustomerActivity,
                        R.anim.rotate2
                    )
                    imgArrowKYC.startAnimation(a)
                    rv.visibility = View.GONE
                } else {
                    linearKYCBox.visibility = View.VISIBLE
                    val a: Animation = AnimationUtils.loadAnimation(
                        this@UpdateCustomerActivity,
                        R.anim.rotate
                    )
                    a.fillAfter = true
                    imgArrowKYC.startAnimation(a)
                }
            }

            R.id.tvTATA -> {
                if (linearTATA.visibility == View.VISIBLE) {
                    linearTATA.visibility = View.GONE

                } else {
                    linearTATA.visibility = View.VISIBLE
                    linearLeyland.visibility = View.GONE
                    linearMM.visibility = View.GONE
                    linearTractor.visibility = View.GONE
                    linearOther.visibility = View.GONE

                }
            }

            R.id.tvLeyland -> {
                if (linearLeyland.visibility == View.VISIBLE) {
                    linearLeyland.visibility = View.GONE
                } else {
                    linearTATA.visibility = View.GONE
                    linearLeyland.visibility = View.VISIBLE
                    linearMM.visibility = View.GONE
                    linearTractor.visibility = View.GONE
                    linearOther.visibility = View.GONE

                }
            }

            R.id.tvMM -> {
                if (linearMM.visibility == View.VISIBLE) {
                    linearMM.visibility = View.GONE
                } else {
                    linearTATA.visibility = View.GONE
                    linearLeyland.visibility = View.GONE
                    linearMM.visibility = View.VISIBLE
                    linearTractor.visibility = View.GONE
                    linearOther.visibility = View.GONE

                }
            }

            R.id.tvTractor -> {
                if (linearTractor.visibility == View.VISIBLE) {
                    linearTractor.visibility = View.GONE
                } else {
                    linearTATA.visibility = View.GONE
                    linearLeyland.visibility = View.GONE
                    linearMM.visibility = View.GONE
                    linearTractor.visibility = View.VISIBLE
                    linearOther.visibility = View.GONE

                }
            }

            R.id.tvOther -> {
                if (linearOther.visibility == View.VISIBLE) {
                    linearOther.visibility = View.GONE
                } else {
                    linearTATA.visibility = View.GONE
                    linearLeyland.visibility = View.GONE
                    linearMM.visibility = View.GONE
                    linearTractor.visibility = View.GONE
                    linearOther.visibility = View.VISIBLE

                }
            }

            R.id.relativeSurveyOpen -> {
                if (rv.visibility == View.VISIBLE) {
                    val a: Animation = AnimationUtils.loadAnimation(
                        this@UpdateCustomerActivity,
                        R.anim.rotate2
                    )
                    imgArrowSurvey.startAnimation(a)
                    rv.visibility = View.GONE
                } else {
                    val a: Animation = AnimationUtils.loadAnimation(
                        this@UpdateCustomerActivity,
                        R.anim.rotate
                    )
                    a.fillAfter = true
                    imgArrowSurvey.startAnimation(a)
                    rv.visibility = View.VISIBLE
                }
            }

            R.id.img -> {
                selectedImg = "outlet"
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(

                        arrayOf(
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.READ_MEDIA_IMAGES,
                            Manifest.permission.READ_MEDIA_VIDEO,
                            Manifest.permission.READ_MEDIA_AUDIO,

                            ),

                        2
                    )
                }
            }

            R.id.img2 -> {
                selectedImg = "card"
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(

                        arrayOf(
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.READ_MEDIA_IMAGES,
                            Manifest.permission.READ_MEDIA_VIDEO,
                            Manifest.permission.READ_MEDIA_AUDIO,


                            ),

                        2
                    )
                }
            }

            R.id.imgGST -> {
                selectedImg = "gst"
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(

                        arrayOf(
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.READ_MEDIA_IMAGES,
                            Manifest.permission.READ_MEDIA_VIDEO,
                            Manifest.permission.READ_MEDIA_AUDIO,


                            ),

                        2
                    )
                }
            }

            R.id.imgAdhar -> {
                selectedImg = "adhar"
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(

                        arrayOf(
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.READ_MEDIA_IMAGES,
                            Manifest.permission.READ_MEDIA_VIDEO,
                            Manifest.permission.READ_MEDIA_AUDIO,


                            ),

                        2
                    )
                }
            }

            R.id.imgPAN -> {
                selectedImg = "pan"
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(

                        arrayOf(
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.READ_MEDIA_IMAGES,
                            Manifest.permission.READ_MEDIA_VIDEO,
                            Manifest.permission.READ_MEDIA_AUDIO,


                            ),

                        2
                    )
                }
            }

            R.id.imgOther -> {
                selectedImg = "other"
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(

                        arrayOf(
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.READ_MEDIA_IMAGES,
                            Manifest.permission.READ_MEDIA_VIDEO,
                            Manifest.permission.READ_MEDIA_AUDIO,


                            ),

                        2
                    )
                }
            }


            R.id.spinnerCustomerType -> {
                //  spinnerCustomerType.showDropDown()
            }

            R.id.cardSubmit -> {


                if (spinnerCustomerType.text.toString() == "") {
                    Toast.makeText(
                        this@UpdateCustomerActivity,
                        "Please select Customer Type",
                        Toast.LENGTH_LONG
                    )
                        .show()
                    return
                } else if (edtFirmName.text.toString() == "") {
                    Toast.makeText(
                        this@UpdateCustomerActivity,
                        "Please enter Shop Name",
                        Toast.LENGTH_LONG
                    )
                        .show()
                    return
                } else if (edtFName.text.toString() == "") {
                    Toast.makeText(
                        this@UpdateCustomerActivity,
                        "Please enter Contact Person Name",
                        Toast.LENGTH_LONG
                    ).show()
                    return
                } else if (edtMobile.text.toString() == "") {
                    Toast.makeText(
                        this@UpdateCustomerActivity,
                        "Please enter Mobile",
                        Toast.LENGTH_LONG
                    ).show()
                    return
                } else if (edtMobile.text.toString().length < 10) {
                    Toast.makeText(
                        this@UpdateCustomerActivity,
                        "Please enter valid Mobile",
                        Toast.LENGTH_LONG
                    ).show()
                    return
                } else if (edtMarket.text.toString() == "") {
                    Toast.makeText(
                        this@UpdateCustomerActivity,
                        "Please enter Market/Area/Town Name",
                        Toast.LENGTH_LONG
                    ).show()
                    return
                } else if (edtAddress1.text.toString() == "") {
                    Toast.makeText(
                        this@UpdateCustomerActivity,
                        "Please enter Shop Address",
                        Toast.LENGTH_LONG
                    ).show()
                    return
                } else if (spinnerPin.text.toString() == "") {
                    Toast.makeText(
                        this@UpdateCustomerActivity,
                        "Please enter pin code",
                        Toast.LENGTH_LONG
                    ).show()
                    return
                } else if (spinnerPin.text.toString().length < 6) {
                    Toast.makeText(
                        this@UpdateCustomerActivity,
                        "Please enter valid pin code",
                        Toast.LENGTH_LONG
                    ).show()
                    return
                }

/*
                for ((index, value) in arrList.withIndex()) {

                    if (value.isRequired == "1" && value.value == "") {

                        Toast.makeText(this@UpdateCustomerActivity, "Please add details for ${value.fieldName}", Toast.LENGTH_LONG).show()
                        return
                    }
                }
*/

                tata = ""
                layland = ""
                mm = ""
                tractor = ""
                otherStore = ""
                if (chTATA_MAV.isChecked)
                    tata = if (tata == "") "MAV" else ",MAV"
                if (chTATA_HCV.isChecked)
                    tata += if (tata == "") "HCV" else ",HCV"
                if (chTATA_LCV.isChecked)
                    tata += if (tata == "") "LCV" else ",LCV"
                if (chTATA_MAV.isChecked)
                    tata += if (tata == "") "Other" else ",Other"


                if (chLEYLAND_MAV.isChecked)
                    layland += if (layland == "") "MAV" else ",MAV"
                if (chLEYLAND_HCV.isChecked)
                    layland += if (layland == "") "HCV" else ",HCV"
                if (chLEYLAND_Other.isChecked)
                    layland += if (layland == "") "Other" else ",Other"


                if (chMM_HCV.isChecked)
                    mm += if (mm == "") "LMV" else ",LMV"
                if (chMM_LCV.isChecked)
                    mm += if (mm == "") "LCV" else ",LCV"
                if (chMM_Other.isChecked)
                    mm += if (mm == "") "Other" else ",Other"

                if (chTractor_YES.isChecked)
                    tractor = "1"

                if (chOther_MAV.isChecked)
                    otherStore += if (otherStore == "") "MAV" else ",MAV"
                if (chOther_HCV.isChecked)
                    otherStore += if (otherStore == "") "HCV" else ",HCV"
                if (chOther_LCV.isChecked)
                    otherStore += if (otherStore == "") "LCV" else ",LCV"
                if (chOther_LMV.isChecked)
                    otherStore += if (otherStore == "") "LMV" else ",LMV"
                if (chOther_Other.isChecked)
                    otherStore += if (otherStore == "") "Other" else ",Other"

/*
                if (tata == "" && layland == "" && mm == "" && tractor == "" && otherStore == "") {
                    Toast.makeText(
                        this@UpdateCustomerActivity,
                        "Please select in Dealing",
                        Toast.LENGTH_LONG
                    ).show()
                    return
                }
*/

                if (checkbox.isChecked) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(
                            arrayOf(
                                Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.READ_MEDIA_IMAGES,
                                Manifest.permission.READ_MEDIA_VIDEO,
                                Manifest.permission.READ_MEDIA_AUDIO,


                                ),
                            1
                        )
                    }
                } else {
                    storeCustomer()
                }
            }
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                val requiredPermissions = listOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_AUDIO,
                    Manifest.permission.READ_MEDIA_VIDEO)
                handlePermissionsResult(permissions, grantResults, requiredPermissions,requestCode)
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                val requiredPermissions = listOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                handlePermissionsResult(permissions, grantResults, requiredPermissions,requestCode)
            }
            else -> {
                val requiredPermissions = listOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                handlePermissionsResult(permissions, grantResults, requiredPermissions,requestCode)
            }
        }




    }

    private fun handlePermissionsResult(permissions: Array<String>, grantResults: IntArray, requiredPermissions: List<String>,requestCode :Int) {
        val perms = HashMap<String, Int>().apply {
            // Initialize with PERMISSION_GRANTED
            requiredPermissions.forEach { this[it] = PackageManager.PERMISSION_GRANTED }
        }

        for (i in permissions.indices) {
            perms[permissions[i]] = grantResults[i]
        }

        val allPermissionsGranted = requiredPermissions.all { perms[it] == PackageManager.PERMISSION_GRANTED }

        if (allPermissionsGranted) {

            if (requestCode == 2){
                selectImage()
            }else if (requestCode == 1){
                initGoogleAPIClient()
                showSettingDialog()
            }

        } else {
            // Permission Denied
            Toast.makeText(this, "Some Permission is Denied", Toast.LENGTH_SHORT).show()
        }
    }



    var imageFile: String = ""
    var base64: String = ""
    var gstImg: String = ""
    var outletImg: String = ""
    var visitingCard: String = ""
    var adharImg: String = ""
    var panImg: String = ""
    var otherImg: String = ""
    var cardImg: String = ""
    var passbookImg: String = ""

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == INTENTCAMERA) {

            if (resultCode == Activity.RESULT_OK) {
                var path: File = data!!.getSerializableExtra("image") as File
                //  compressImage(path)

                //  var bitmap = getBitmap(path.path)

                //imageFile = Compressor(this@MyProfileActivity).compressToFile(path)
                imageFile = path.path

                var compressedImageFile: File? = null
               /* try {
                    compressedImageFile =
                        Compressor(this@UpdateCustomerActivity).setMaxHeight(100).setMaxWidth(100)
                            .setQuality(50).compressToFile(path)
                } catch (e: IOException) {
                    e.printStackTrace()
                }*/
                try {
                    compressedImageFile =
                        Compressor(this@UpdateCustomerActivity).setMaxHeight(200)
                            .setMaxWidth(200)
                            .setQuality(90).compressToFile(path)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                println("compressedImageFile=imageFile="+imageFile)
                println("compressedImageFile="+compressedImageFile)


                base64 = ""

                if (selectedImg == "outlet") {
                    outletImgFile = compressedImageFile
                    Glide.with(this)
                        .load(path.path)
                        .into(imgProfile)
                    outletImg = base64
                } else if (selectedImg == "card") {
                    visitingCardImgFile = compressedImageFile
                    Glide.with(this)
                        .load(path.path)
                        .into(imgProfile2)
                    visitingCard = base64
                } else if (selectedImg == "gst") {
                    gstImgFile = compressedImageFile
                    Glide.with(this)
                        .load(path.path)
                        .into(imgGST)
                    gstImg = base64
                } else if (selectedImg == "adhar") {
                    adharImgFile = compressedImageFile
                    Glide.with(this)
                        .load(path.path)
                        .into(imgAdhar)
                    adharImg = base64
                } else if (selectedImg == "pan") {
                    panImgFile = compressedImageFile
                    Glide.with(this)
                        .load(path.path)
                        .into(imgPAN)
                    panImg = base64
                } else if (selectedImg == "other") {
                    otherImgFile = compressedImageFile
                    Glide.with(this)
                        .load(path.path)
                        .into(imgOther)
                    otherImg = base64
                }


            }
        } else if (requestCode == INTENTGALLERY) {
            if (resultCode == Activity.RESULT_OK) {

                val selectedImageUri: Uri = data!!.data!!

                val tempPath =
                    Utilities.getPathFromUri(selectedImageUri, this@UpdateCustomerActivity)

                val file = File(tempPath)


                var compressedImageFile: File? = null
                try {
                    compressedImageFile =
                        Compressor(this@UpdateCustomerActivity).setMaxHeight(200).setMaxWidth(200)
                            .setQuality(90).compressToFile(file)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                base64 = ""

                if (selectedImg == "outlet") {
                    outletImgFile = compressedImageFile
                    Glide.with(this)
                        .load(data.data)
                        .into(imgProfile)
                    outletImg = base64
                } else if (selectedImg == "gst") {
                    gstImgFile = compressedImageFile
                    Glide.with(this)
                        .load(data.data)
                        .into(imgGST)
                    gstImg = base64
                } else if (selectedImg == "adhar") {
                    adharImgFile = compressedImageFile
                    Glide.with(this)
                        .load(data.data)
                        .into(imgAdhar)
                    adharImg = base64
                } else if (selectedImg == "pan") {
                    panImgFile = compressedImageFile
                    Glide.with(this)
                        .load(data.data)
                        .into(imgPAN)
                    panImg = base64
                } else if (selectedImg == "other") {
                    otherImgFile = compressedImageFile
                    Glide.with(this)
                        .load(data.data)
                        .into(imgOther)
                    otherImg = base64
                } else if (selectedImg == "card") {
                    visitingCardImgFile = compressedImageFile
                    Glide.with(this)
                        .load(data.data)
                        .into(imgProfile2)
                    cardImg = base64
                }

                imageFile = tempPath


            }


/*
            if (resultCode == Activity.RESULT_OK) {

                val selectedImageUri: Uri = data!!.data!!

                val tempPath =
                    Utilities.getPathFromUri(selectedImageUri, this@UpdateCustomerActivity)

                val file = File(tempPath)

                var compressedImageBitmap: Bitmap? = null
                try {
                    compressedImageBitmap =
                        Compressor(this@UpdateCustomerActivity).setMaxHeight(100).setMaxWidth(100)
                            .setQuality(50).compressToBitmap(file)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                val byteArrayOutputStream = ByteArrayOutputStream()
                compressedImageBitmap!!.compress(
                    Bitmap.CompressFormat.PNG,
                    50,
                    byteArrayOutputStream
                )

                val byteArray = byteArrayOutputStream.toByteArray()

                base64 = Base64.encodeToString(byteArray, Base64.DEFAULT)

                if (selectedImg == "outlet") {
                    Glide.with(this)
                        .load(data.data)
                        .into(imgProfile)
                    outletImg = base64
                } else if (selectedImg == "gst") {
                    Glide.with(this)
                        .load(data.data)
                        .into(imgGST)
                    gstImg = base64
                } else if (selectedImg == "adhar") {
                    Glide.with(this)
                        .load(data.data)
                        .into(imgAdhar)
                    adharImg = base64
                } else if (selectedImg == "pan") {
                    Glide.with(this)
                        .load(data.data)
                        .into(imgPAN)
                    panImg = base64
                } else if (selectedImg == "other") {
                    Glide.with(this)
                        .load(data.data)
                        .into(imgOther)
                    otherImg = base64
                } else if (selectedImg == "card") {
                    Glide.with(this)
                        .load(data.data)
                        .into(imgProfile2)
                    cardImg = base64
                }

                imageFile = tempPath


            }
*/
        } else if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
                gettingLocation()
            }
        }
    }


    private fun getSurveyQuestions(customerType: String = "") {
        if (!Utilities.isOnline(this)) {
            return
        }

        val queryParams = java.util.HashMap<String, String>()
        queryParams["customer_type"] = customerType
        ApiClient.getSurveyQuestions(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this).toString(),
            queryParams,
            object :
                APIResultLitener<EnquiryModel> {
                override fun onAPIResult(
                    response: Response<EnquiryModel>?,
                    errorMessage: String?
                ) {

                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {
                            dialog.dismiss()

                            arrList = response.body()!!.data
                            getCustomerInfo()
                        } else {
                            dialog.dismiss()

                            var jsonObject: JSONObject? = null
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    this@UpdateCustomerActivity,
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

    private fun getPincodeInfo(pincode: String) {
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

                                pinPos = response.body()!!
                                    .get("data").asJsonObject.get("id").asInt.toString()

                                cityPos = response.body()!!
                                    .get("data").asJsonObject.get("city_id").asInt.toString()

                                statePos = response.body()!!
                                    .get("data").asJsonObject.get("state_id").asInt.toString()

                                districtPos = response.body()!!
                                    .get("data").asJsonObject.get("district_id").asInt.toString()

                                countryPos = response.body()!!
                                    .get("data").asJsonObject.get("country_id").asInt.toString()


                                edtState.setText(state)
                                edtCity.setText(city)
                            } catch (e: Exception) {
                                Toast.makeText(
                                    this@UpdateCustomerActivity,
                                    "Please enter correct pin",
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                        } else {
                            dialog.dismiss()

                            var jsonObject: JSONObject? = null
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    this@UpdateCustomerActivity,
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

    private fun getCustomerInfo() {
        if (!Utilities.isOnline(this)) {
            return
        }
        var dialog = DialogClass.progressDialog(this)
        println("IDDD="+StaticSharedpreference.getInfo(Constant.CHECKIN_CUST_ID, this).toString())
        val queryParams = java.util.HashMap<String, String>()
        queryParams["customer_id"] = StaticSharedpreference.getInfo(Constant.CHECKIN_CUST_ID, this).toString()
        ApiClient.getCustomerInfo(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this).toString(),
            queryParams,
            object :
                APIResultLitener<JsonObject> {
                override fun onAPIResult(
                    response: Response<JsonObject>?,
                    errorMessage: String?
                ) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {

                            var json = response.body()!!.get("data").asJsonObject

                            try {
                                edtFirmName.setText(json.get("name").asString.toString())

                                try {
                                    edtFName.setText(json.get("first_name").asString.toString() + " " + json.get("last_name").asString.toString())
                                } catch (e: Exception) {
                                }

                                spinnerCustomerType.setText(json.get("customertypes").asJsonObject.get("customertype_name").asString.toString())
                                if (json.get("customertypes").asJsonObject.get("customertype_name").asString.toString() == "Retailer"){
                                    spinnerparentType.visibility = View.VISIBLE
                                    if (StaticSharedpreference.getBoolean(Constant.todayBeatSchedule, this@UpdateCustomerActivity)){
                                        spinnerBeat.visibility = View.VISIBLE
                                    }else if (StaticSharedpreference.getBoolean(Constant.beatUser, this@UpdateCustomerActivity)){
                                        spinnerBeat.visibility = View.VISIBLE
                                    }else{
                                        spinnerBeat.visibility = View.GONE
                                    }                                    
                                    if (json.get("parent_name").asString.toString() != ""){
                                        spinnerparentType.setText(json.get("parent_name").asString.toString())
                                    }
                                    if (json.get("beat_name").asString.toString() != ""){
                                        spinnerBeat.visibility = View.VISIBLE
                                        spinnerBeat.setText(json.get("beat_name").asString.toString())
                                    }
                                }else{
                                    spinnerparentType.visibility = View.GONE
                                    try {
                                        val parent_id2 = json.get("parent_id").asString.toString()
                                        if (!TextUtils.isEmpty(parent_id2)){
                                            parent_id = parent_id2
                                        }
                                    }catch (e: Exception){
                                    }
                                }
                                try {
                                    var mobileStr = json.get("mobile").asString.toString()
                                    val mobile: String = mobileStr.substring(mobileStr.length - 10)
                                    if (!TextUtils.isEmpty(mobileStr)){
                                        edtMobile.setText(mobile)
                                    }
                                }catch (e: Exception){
                                }

                                try {
                                    val contactnumber = json.get("contact_number").asString.toString()
                                    if (!TextUtils.isEmpty(contactnumber)){
                                        edtalternateMobile.setText(contactnumber)
                                    }
                                }catch (e: Exception){
                                }

                                try {
                                    val Email_ID = json.get("email").asString.toString()
                                    if (!TextUtils.isEmpty(Email_ID)){
                                        edtEmail.setText(Email_ID)
                                    }
                                }catch (e: Exception){
                                }

                                try {
                                    val market = json.get("customeraddress").asJsonObject.get("landmark").asString.toString()
                                    if (!TextUtils.isEmpty(market)){
                                        edtMarket.setText(market)
                                    }
                                }catch (e: Exception){
                                }
                                try {
                                    val shop_add = json.get("customeraddress").asJsonObject.get("address1").asString.toString()
                                    if (!TextUtils.isEmpty(shop_add)){
                                        edtAddress1.setText(shop_add)
                                    }
                                }catch (e: Exception){
                                }
                                try {
                                    val pin = json.get("customeraddress").asJsonObject.get("zipcode").asString.toString()
                                    if (!TextUtils.isEmpty(pin)){
                                        spinnerPin.setText(pin)
                                    }
                                }catch (e: Exception){
                                }
                                try {
                                    val state = json.get("customeraddress").asJsonObject.get("statename").asJsonObject.get("state_name").asString.toString()
                                    if (!TextUtils.isEmpty(state)){
                                        edtState.setText(state)
                                    }
                                }catch (e: Exception){
                                }
                                try {
                                    val state_city = json.get("customeraddress").asJsonObject.get("cityname").asJsonObject.get("city_name").asString.toString()
                                    if (!TextUtils.isEmpty(state_city)){
                                        edtCity.setText(state_city)
                                    }
                                }catch (e: Exception){
                                }
                                try {
                                    val grade = json.get("customerdetails").asJsonObject.get("grade").asString.toString()
                                    if (!TextUtils.isEmpty(grade)){
                                        spinnerGrade.setText(grade)
                                    }
                                }catch (e: Exception){
                                }
                                try {
                                    val status = json.get("customerdetails").asJsonObject.get("visit_status").asString.toString()
                                    if (!TextUtils.isEmpty(status)){
                                        spinnerStatus.setText(status)
                                    }
                                }catch (e: Exception){
                                }
                                try {
                                    val imageoutletUrl = json.get("profile_image").asString
                                    if (!TextUtils.isEmpty(imageoutletUrl)){
                                        Glide.with(this@UpdateCustomerActivity)
                                            .load(imageoutletUrl)
                                            .into(imgProfile)
                                    }
                                }catch (e: Exception){
                                }
                                try {
                                    val imagevisitUrl = json.get("customerdetails").asJsonObject.get("visiting_card").asString
                                    if (!TextUtils.isEmpty(imagevisitUrl)){
                                        Glide.with(this@UpdateCustomerActivity)
                                            .load(imagevisitUrl)
                                            .into(imgProfile2)
                                    }
                                }catch (e: Exception){
                                }
                                try {
                                    val gst_text = json.get("customerdetails").asJsonObject.get("gstin_no").asString.toString()
                                    if (!TextUtils.isEmpty(gst_text)){
                                        edtGSTIN.setText(gst_text)
                                    }
                                }catch (e: Exception){
                                }
                                try {
                                    val pan_text = json.get("customerdetails").asJsonObject.get("pan_no").asString.toString()
                                    if (!TextUtils.isEmpty(pan_text)){
                                        edtPAN.setText(pan_text)
                                    }
                                }catch (e: Exception){
                                }
                                try {
                                    val adhar_text = json.get("customerdetails").asJsonObject.get("aadhar_no").asString.toString()
                                    if (!TextUtils.isEmpty(adhar_text)){
                                        edtAdhar.setText(adhar_text)
                                    }
                                }catch (e: Exception){
                                }
                                try {
                                    val other_text = json.get("customerdetails").asJsonObject.get("otherid_no").asString.toString()
                                    if (!TextUtils.isEmpty(other_text)){
                                        edtOther.setText(other_text)
                                    }
                                }catch (e: Exception){
                                }




                                var jsonArr: JsonArray = json.get("customerdocuments").asJsonArray
                                for ((index, value) in jsonArr.withIndex()) {
                                    var jsonO = jsonArr.get(index).asJsonObject
                                    if (jsonO.get("document_name").asString == "aadhar") {
                                        Glide.with(this@UpdateCustomerActivity)
                                            .load(jsonO.get("file_path").asString)
                                            .into(imgAdhar)
                                    } else if (jsonO.get("document_name").asString == "pan") {
                                        Glide.with(this@UpdateCustomerActivity)
                                            .load(jsonO.get("file_path").asString)
                                            .into(imgPAN)
                                        println("Abhii=pan="+jsonO.get("file_path").asString)
                                    } else if (jsonO.get("document_name").asString == "gstin") {
                                        Glide.with(this@UpdateCustomerActivity)
                                            .load(jsonO.get("file_path").asString)
                                            .into(imgGST)
                                        println("Abhii=gst="+jsonO.get("file_path").asString)
                                    } else if (jsonO.get("document_name").asString == "other") {
                                        Glide.with(this@UpdateCustomerActivity)
                                            .load(jsonO.get("file_path").asString)
                                            .into(imgOther)
                                    }
                                }
//                                spinnerBeat.setText(json.get("beat_name").asString.toString())

                                if (!json.get("customeraddress").asJsonObject.get("id").isJsonNull)
                                    addressId = json.get("customeraddress").asJsonObject.get("id").asString

                                longitude = json.get("longitude")?.asString?.toString() ?: ""
                                latitude = json.get("latitude").asString?.toString() ?: ""

                            } catch (e: Exception) {
                                println("jsonexception ${e.toString()}")
                            }

                            try {
                                cityPos = json.get("customeraddress").asJsonObject.get("city_id").asInt.toString()
                            } catch (e: Exception) {
                            }

                            try {
                                statePos =
                                    json.get("customeraddress").asJsonObject.get("state_id").asInt.toString()
                            } catch (e: Exception) {
                            }

                            try {
                                districtPos =
                                    json.get("customeraddress").asJsonObject.get("district_id").asInt.toString()
                            } catch (e: Exception) {
                            }

                            try {
                                pinPos = json.get("customeraddress").asJsonObject.get("pincode_id").asInt.toString()
                            } catch (e: Exception) {
                            }

                            try {
                                countryPos =
                                    json.get("customeraddress").asJsonObject.get("country_id").asInt.toString()
                            } catch (e: Exception) {
                            }

                            try {
                                beatPos =
                                    json.get("beat_id").asInt.toString()
                            } catch (e: Exception) {
                            }


                            val aa = ArrayAdapter(
                                this@UpdateCustomerActivity,
                                android.R.layout.simple_list_item_1,
                                arrGrade
                            )
                            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            spinnerGrade.setAdapter(aa)

                            val aa2 = ArrayAdapter(
                                this@UpdateCustomerActivity,
                                android.R.layout.simple_list_item_1,
                                arrStatus
                            )
                            aa2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            spinnerStatus.setAdapter(aa2)

                            val disName = arrayOfNulls<String>(beatArr!!.size)

                            for (i in beatArr!!.indices) {

                                disName[i] =
                                    beatArr!![i].beatName

                            }


                            val aaBeat = ArrayAdapter(
                                this@UpdateCustomerActivity,
                                android.R.layout.simple_list_item_1,
                                disName
                            )
                            aaBeat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            //Setting the ArrayAdapter data on the Spinner
                            spinnerBeat.setAdapter(aaBeat)


                            var jsonArra = json.get("surveys").asJsonArray

                            for ((index, value) in arrList.withIndex()) {
                                for (valueJson in jsonArra) {
                                    var jsonObject = valueJson.asJsonObject;
                                    if (value.field_id == jsonObject.get("field_id").asInt) {
                                        arrList[index].responseValue = jsonObject.get("value").asString
                                    }
                                }
                            }

                            rv.setNestedScrollingEnabled(false)
                            var mLayoutManager = LinearLayoutManager(this@UpdateCustomerActivity)
                            rv.layoutManager = mLayoutManager
                            rv.setNestedScrollingEnabled(false);
                            val enquiryInfoAdapter = EnquiryInfoAdapter()
                            rv.adapter = enquiryInfoAdapter

                            var jsonArraDeal = json.get("customerdeals").asJsonArray

                            for (value in jsonArraDeal) {
                                var jsonObject = value.asJsonObject;
                                if (jsonObject.get("types")!!.asString == "TATA") {
                                    chTATA_MAV.isChecked =
                                        jsonObject.get("mav")!!.asInt == 1

                                    chTATA_HCV.isChecked = jsonObject.get("hcv")!!.asInt == 1

                                    chTATA_LCV.isChecked = jsonObject.get("lcv")!!.asInt == 1

                                    chTATA_Other.isChecked = jsonObject.get("other")!!.asInt == 1

                                } else if (jsonObject.get("types")!!.asString == "LEYLAND") {

                                    chLEYLAND_MAV.isChecked = jsonObject.get("mav")!!.asInt == 1
                                    chLEYLAND_HCV.isChecked = jsonObject.get("hcv")!!.asInt == 1
                                    chLEYLAND_Other.isChecked = jsonObject.get("other")!!.asInt == 1

                                } else if (jsonObject.get("types")!!.asString == "M&M") {

                                    chMM_HCV.isChecked = jsonObject.get("lmv")!!.asInt == 1
                                    chMM_LCV.isChecked = jsonObject.get("lcv")!!.asInt == 1
                                    chMM_Other.isChecked = jsonObject.get("other")!!.asInt == 1

                                } else if (jsonObject.get("types")!!.asString == "Tractor") {

                                    if (jsonObject.get("tractor")!!.asInt == 1) {
                                        chTractor_YES.isChecked = true

                                    }

                                } else if (jsonObject.get("types")!!.asString == "Other") {
                                    chOther_MAV.isChecked = jsonObject.get("mav")!!.asInt == 1
                                    chOther_HCV.isChecked = jsonObject.get("hcv")!!.asInt == 1
                                    chOther_LCV.isChecked = jsonObject.get("lcv")!!.asInt == 1
                                    chOther_LMV.isChecked = jsonObject.get("lmv")!!.asInt == 1
                                    chOther_Other.isChecked = jsonObject.get("other")!!.asInt == 1
                                }
                            }
                        } else {


                            var jsonObject: JSONObject? = null
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    this@UpdateCustomerActivity,
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

    var addressId = ""
    private fun storeCustomer() {

        if (!Utilities.isOnline(this@UpdateCustomerActivity)) {
            return
        }

        var dialog = DialogClass.progressDialog(this@UpdateCustomerActivity)

        val name = RequestBody.create(
            MediaType.parse("text"),
            edtFirmName.text.toString()
        )

        val fullName = RequestBody.create(
            MediaType.parse("text"),
            edtFName.text.toString()
        )

        val mobile = RequestBody.create(
            MediaType.parse("text"),
            edtMobile.text.toString()
        )
        val alternate_no = RequestBody.create(
            MediaType.parse("text"),
            edtalternateMobile.text.toString()
        )
        val parent_id = RequestBody.create(
            MediaType.parse("text"),
            parent_id
        )

        val email = RequestBody.create(
            MediaType.parse("text"),
            edtEmail.text.toString()
        )

        val address1 = RequestBody.create(
            MediaType.parse("text"),
            edtAddress1.text.toString()
        )

        val latitudeReq = RequestBody.create(
            MediaType.parse("text"),
            latitude
        )

        val longitudeReq = RequestBody.create(
            MediaType.parse("text"),
            longitude
        )
        println("whileeesubmitttt=="+latitude+"<<<"+longitude)

        val beat_id = RequestBody.create(
            MediaType.parse("text"),
            beatPos
        )

        val gst = RequestBody.create(
            MediaType.parse("text"),
            edtGSTIN.text.toString()
        )

        val pan = RequestBody.create(
            MediaType.parse("text"),
            edtPAN.text.toString()
        )

        val adhar = RequestBody.create(
            MediaType.parse("text"),
            edtAdhar.text.toString()
        )

        val otherNo = RequestBody.create(
            MediaType.parse("text"),
            edtOther.text.toString()
        )
        val zipcode = RequestBody.create(
            MediaType.parse("text"),
            spinnerPin.text.toString()
        )
        val landmark = RequestBody.create(
            MediaType.parse("text"),
            edtMarket.text.toString()
        )
        val customertype = RequestBody.create(
            MediaType.parse("text"),
            customerTypePos
        )
        val statusType = RequestBody.create(
            MediaType.parse("text"),
            spinnerStatus.text.toString()
        )

        val grade = RequestBody.create(
            MediaType.parse("text"),
            spinnerGrade.text.toString()
        )

        val customer_id = RequestBody.create(
            MediaType.parse("text"),
            StaticSharedpreference.getInfo(Constant.CHECKIN_CUST_ID, this).toString()
        )

        val address_id = RequestBody.create(
            MediaType.parse("text"),
            addressId
        )

        val pincodeIdReq = RequestBody.create(
            MediaType.parse("text"),
            pinPos + ""
        )

        val cityIdReq = RequestBody.create(
            MediaType.parse("text"),
            cityPos + ""
        )

        val stateIdReq = RequestBody.create(
            MediaType.parse("text"),
            statePos + ""
        )

        val countryIdReq = RequestBody.create(
            MediaType.parse("text"),
            countryPos
        )

        val districtIdReq = RequestBody.create(
            MediaType.parse("text"),
            districtPos
        )

        var fileToUploadOther = if (otherImgFile == null)
            MultipartBody.Part.createFormData("image", "")
        else {
            val reqbodyFileD: RequestBody = RequestBody.create(MediaType.parse("image/*"), otherImgFile!!)
            val fileName = "other_image"
            MultipartBody.Part.createFormData(fileName, otherImgFile!!.name, reqbodyFileD)
        }

        var fileToUploadGST = if (gstImgFile == null)
            MultipartBody.Part.createFormData("image", "")
        else {
            val reqbodyFileD: RequestBody =
                RequestBody.create(MediaType.parse("image/*"), gstImgFile!!)
            val fileName = "gstin_image"
            MultipartBody.Part.createFormData(fileName, gstImgFile!!.name, reqbodyFileD)
        }

        var fileToUploadPAN = if (panImgFile == null)
            MultipartBody.Part.createFormData("image", "")
        else {
            val reqbodyFileD: RequestBody =
                RequestBody.create(MediaType.parse("image/*"), panImgFile!!)
            val fileName = "pan_image"
            MultipartBody.Part.createFormData(fileName, panImgFile!!.name, reqbodyFileD)
        }

        var fileToUploadAdhar = if (adharImgFile == null)
            MultipartBody.Part.createFormData("image", "")
        else {
            val reqbodyFileD: RequestBody =
                RequestBody.create(MediaType.parse("image/*"), adharImgFile!!)
            val fileName = "aadhar_image"
            MultipartBody.Part.createFormData(fileName, adharImgFile!!.name, reqbodyFileD)
        }

        var fileToUploadOutet = if (outletImgFile == null)
            MultipartBody.Part.createFormData("image", "")
        else {
            val reqbodyFileD: RequestBody =
                RequestBody.create(MediaType.parse("image/*"), outletImgFile!!)
            val fileName = "image"
            MultipartBody.Part.createFormData(fileName, outletImgFile!!.name, reqbodyFileD)
        }

        var fileToUploadCard = if (visitingCardImgFile == null)
            MultipartBody.Part.createFormData("image", "")
        else {
            val reqbodyFileD: RequestBody =
                RequestBody.create(MediaType.parse("image/*"), visitingCardImgFile!!)
            val fileName = "visiting_card"
            MultipartBody.Part.createFormData(fileName, visitingCardImgFile!!.name, reqbodyFileD)
        }

        var arrSurvey: ArrayList<StoreCustomerRequestModel.Survey> = arrayListOf()
        for ((index, value) in arrList.withIndex()) {
            var storeCustomerSurveyRequestModel = StoreCustomerRequestModel().Survey()
            storeCustomerSurveyRequestModel.field_id = value.field_id.toString()
            storeCustomerSurveyRequestModel.value = value.value.toString()
            arrSurvey.add(storeCustomerSurveyRequestModel)
        }

        val stringToPost = Gson().toJson(arrSurvey)
        val surveyArr = RequestBody.create(
            MediaType.parse("text"),
            "" + stringToPost
        )

        var mav = if (tata.contains("MAV")) "1" else "0"
        var hcv = if (tata.contains("HCV")) "1" else "0"
        var lcv = if (tata.contains("LCV")) "1" else "0"
        var other = if (tata.contains("Other")) "1" else "0"

        var jsonArray = JsonArray()

        var jsonObject = JsonObject()
        jsonObject.addProperty("types", "TATA")
        jsonObject.addProperty("mav", mav)
        jsonObject.addProperty("hcv", hcv)
        jsonObject.addProperty("lcv", lcv)
        jsonObject.addProperty("other", other)
        jsonArray.add(jsonObject)

        mav = if (layland.contains("MAV")) "1" else "0"
        hcv = if (layland.contains("HCV")) "1" else "0"
        other = if (layland.contains("Other")) "1" else "0"

        var jsonObject2 = JsonObject()
        jsonObject2.addProperty("types", "LEYLAND")
        jsonObject2.addProperty("mav", mav)
        jsonObject2.addProperty("hcv", hcv)
        jsonObject2.addProperty("other", other)
        jsonArray.add(jsonObject2)

        hcv = if (mm.contains("LMV")) "1" else "0"
        lcv = if (mm.contains("LCV")) "1" else "0"
        other = if (mm.contains("Other")) "1" else "0"

        var jsonObject3 = JsonObject()
        jsonObject3.addProperty("types", "M&M")
        jsonObject3.addProperty("lmv", hcv)
        jsonObject3.addProperty("lcv", lcv)
        jsonObject3.addProperty("other", other)
        jsonArray.add(jsonObject3)

        if (tractor == "")
            tractor = "0"

        var jsonObject4 = JsonObject()
        jsonObject4.addProperty("types", "Tractor")
        jsonObject4.addProperty("tractor", tractor)
        jsonArray.add(jsonObject4)

        mav = if (otherStore.contains("MAV")) "1" else "0"
        hcv = if (otherStore.contains("HCV")) "1" else "0"
        lcv = if (otherStore.contains("LCV")) "1" else "0"
        val lmv = if (otherStore.contains("LMV")) "1" else "0"
        other = if (otherStore.contains("Other")) "1" else "0"

        var jsonObject5 = JsonObject()
        jsonObject5.addProperty("types", "Other")
        jsonObject5.addProperty("mav", mav)
        jsonObject5.addProperty("hcv", hcv)
        jsonObject5.addProperty("lcv", lcv)
        jsonObject5.addProperty("lmv", lmv)
        jsonObject5.addProperty("other", other)
        jsonArray.add(jsonObject5)

        val stringToPostDealing = Gson().toJson(jsonArray)
        val surveyArrDealing = RequestBody.create(
            MediaType.parse("text"),
            "" + stringToPostDealing
        )

        ApiClient.updateCustomerProfile(
            StaticSharedpreference.getInfo(
                Constant.ACCESS_TOKEN,
                this@UpdateCustomerActivity
            ).toString(),
            name,
            fullName,
            mobile,
            alternate_no,
            parent_id,
            email,
            address1,
            latitudeReq,
            longitudeReq,
            beat_id,
            gst,
            pan,
            adhar,
            otherNo,
            zipcode,
            landmark,
            customertype,
            statusType,
            grade,
            customer_id,
            address_id,
            pincodeIdReq,
            cityIdReq,
            districtIdReq,
            stateIdReq,
            countryIdReq,
            surveyArr,
            surveyArrDealing,
            fileToUploadGST,
            fileToUploadPAN,
            fileToUploadAdhar,
            fileToUploadOther,
            fileToUploadCard,
            fileToUploadOutet,
            object : APIResultLitener<JsonObject> {
                override fun onAPIResult(
                    response: Response<JsonObject>?,
                    errorMessage: String?
                ) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {

                            Toast.makeText(
                                this@UpdateCustomerActivity,
                                "Customer updated successfully",
                                Toast.LENGTH_LONG
                            ).show()
                            var intent = Intent()
                            intent.putExtra("updated", "1")
                            setResult(RESULT_OK, intent)
                            finish()

                        } else {

                            var jsonObject: JSONObject? = null

                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    "error",
                                    jsonObject.getJSONArray("message")[0].toString(),
                                    this@UpdateCustomerActivity,
                                    false
                                )

                            } catch (e: java.lang.Exception) {

                            }
                        }
                    } else {
                        Toast.makeText(
                            this@UpdateCustomerActivity,
                            resources.getString(R.string.poor_connection),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        )
    }

    lateinit var dialog: Dialog


    private fun getBeatList() {

        if (!Utilities.isOnline(this)) {
            return
        }

        dialog = DialogClass.progressDialog(this)

        val queryParams = HashMap<String, String>()
        queryParams["page"] = ""
        queryParams["pageSize"] = ""
        queryParams["beatDate"] = ""


        ApiClient.getBeatDropdownList(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this).toString(),
            queryParams,
            object : APIResultLitener<JsonObject> {
                override fun onAPIResult(response: Response<JsonObject>?, errorMessage: String?) {
                    // getcustomertype()
                    getSurveyQuestions(customerTypePos)
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {

                            val gson = Gson()
                            val listType = object :
                                TypeToken<ArrayList<BeatModel>>() {}.type

                            beatArr = gson.fromJson<ArrayList<BeatModel>>(
                                response.body()!!.get("data").asJsonArray,
                                listType
                            )


                        } else {

                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    this@UpdateCustomerActivity,
                                    false
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                        }
                    } else {
                        //  dialog.dismiss()
                        Toast.makeText(
                            this@UpdateCustomerActivity,
                            resources.getString(R.string.poor_connection),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })
    }


    private fun selectImage() {
        val items = arrayOf<CharSequence>("Camera", "Gallery", "Cancel")
        val builder = AlertDialog.Builder(this@UpdateCustomerActivity)
        builder.setTitle("Select!")
        builder.setItems(items) { dialog, item ->
            when {
                items[item] == "Camera" -> {
                    var intent = Intent(this, CameraCustomActivity::class.java);
                    intent.putExtra("camera", "1")
                    startActivityForResult(intent, INTENTCAMERA)
                    // openCamera()
                }
                items[item] == "Gallery" -> {
                    openGallery()
                }
                items[item] == "Cancel" -> dialog.dismiss()
            }
        }
        builder.show()
    }


    private fun openGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(
                intent,
                getString(R.string.please_select_image)
            ), INTENTGALLERY
        )
    }


    private fun openCamera() {

        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(packageManager) != null) {
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
                    this,
                    packageName + ".provider", photoFile
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
            Environment.DIRECTORY_PICTURES
        )

// Save a file: path for use with ACTION_VIEW intents
        return File.createTempFile(
            imageFileName, /* prefix */
            ".jpg", /* suffix */
            storageDir      /* directory */
        )
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
                            this@UpdateCustomerActivity,
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

        Handler().postDelayed({
            gpsTracker = GPSTracker(this@UpdateCustomerActivity)
            gpsTracker.getLongitude()

            if (gpsTracker.getLatitude() == 0.0) {
                gettingLocation()
            } else {
                latitude = gpsTracker.getLatitude().toString()
                longitude = gpsTracker.getLongitude().toString()
                storeCustomer()

            }
        }, 2000)
    }

    /* Initiate Google API Client  */
    private fun initGoogleAPIClient() {
        //Without Google API Client Auto Location Dialog will not work
        mGoogleApiClient = GoogleApiClient.Builder(this@UpdateCustomerActivity)
            .addApi(LocationServices.API)
            .build()
        mGoogleApiClient!!.connect()
    }


}