package com.exp.clonefieldkonnect.activity

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devstune.searchablemultiselectspinner.SearchableItem
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.adapter.CustomerParentAdapter
import com.exp.clonefieldkonnect.adapter.EnquiryInfoAdapter
import com.exp.clonefieldkonnect.connection.APIResultLitener
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.helper.Constant
import com.exp.clonefieldkonnect.helper.DialogClass
import com.exp.clonefieldkonnect.helper.GPSTracker
import com.exp.clonefieldkonnect.helper.StaticSharedpreference
import com.exp.clonefieldkonnect.model.*
import com.exp.import.Utilities
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
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class StoreCustomerActivity : AppCompatActivity(), View.OnClickListener {
    private var items: MutableList<com.devstune.searchablemultiselectspinner.SearchableItem> = ArrayList()

    val APP_TAG = "MyCustomApp"
    val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034
    var photoFileName = "photo.jpg"
    var photoFile: File? = null
    var cameraResultLauncher: ActivityResultLauncher<Intent>? = null

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

    lateinit var spinnerCustomerType: AutoCompleteTextView
//    lateinit var spinnerdivisionType: AutoCompleteTextView
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
    lateinit var linearMainDealing: LinearLayout

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

    lateinit var imgGST: ImageView
    lateinit var imgAdhar: ImageView
    lateinit var imgPAN: ImageView
    lateinit var imgOther: ImageView
    lateinit var imgArrowSurvey: ImageView
    lateinit var imgArrowKYC: ImageView
    lateinit var relativeDealingOpen: RelativeLayout
    lateinit var imgArrowDealing: ImageView

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

    lateinit var beatArr: ArrayList<BeatModel>
    lateinit var stateModel: List<StateModel.Datum>
    lateinit var districtModel: List<DistrictModel.Datum>
    lateinit var cityModel: List<CityModel.Datum>
    lateinit var pinModel: List<PinCodeModel.Datum>
    private lateinit var checkbox: CheckBox
    val INTENTCAMERA = 4
    val INTENTGALLERY = 5
    private var lastPosition = -1
    private var isLoading = false
    private var page = 1
    private var pageSize = "100"
    private var parent_search = ""

    lateinit var cameraFile: File
    var beatPos: String = ""
    var customerTypePos: String = ""
    var statePos: String = ""
    var districtPos: String = ""
    var cityPos: String = ""
    var pinPos: String = ""
    var selectedImg = ""
    var customerTypeArr: ArrayList<CustomerTypeModel?> = arrayListOf();
    var CustomerDivisionArr: java.util.ArrayList<CustomerDivisionModel> = arrayListOf()
    var CustomerParentArr: java.util.ArrayList<CustomerParentModel.Data> = arrayListOf()
    var CustomerParentArr2: java.util.ArrayList<CustomerParentModel.Data> = arrayListOf()
    var arrdivisionStr : ArrayList<String> = arrayListOf()
    var dataArray: JsonArray = JsonArray()
    var selectedDivisionIds : ArrayList<String> = arrayListOf()
    var arrdivisionid : ArrayList<String> = arrayListOf()
    var arrparent : ArrayList<String> = arrayListOf()
    var arrparentid : ArrayList<String> = arrayListOf()
    var selectedParentIds : ArrayList<String> = arrayListOf()
    private lateinit var typeName: Array<String?>
    private lateinit var typeID: Array<String?>
    companion object {
        var arrList: ArrayList<EnquiryModel.Datum> = arrayListOf()
    }

    var arrGrade = arrayListOf<String>(
        "Grade A", "Grade B", "Grade C", "Grade D"

    )
    var arrStatus = arrayListOf<String>(
        "Hot", "Warm", "Cold", "Existing"
    )


    lateinit var rv: RecyclerView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_customer)
        Log.v("akram", "arrlist " + arrList.size)
        arrList.clear()
        initViews()

        cameraResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == RESULT_OK) {
                    // by this point we have the camera photo on disk
                    val takenImage = BitmapFactory.decodeFile(photoFile!!.absolutePath)
                    // RESIZE BITMAP, see section below
                    // Load the taken image into a preview
                    // val ivPreview = ivPostImage
                    imgProfile.setImageBitmap(takenImage)
                } else { // Result was a failure
                    Toast.makeText(this, "Error taking picture", Toast.LENGTH_SHORT).show()
                }
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initViews() {
        chTATA_MAV = findViewById(R.id.chTATA_MAV)
        chTATA_HCV = findViewById(R.id.chTATA_HCV)
        chTATA_LCV = findViewById(R.id.chTATA_LCV)
        chTATA_Other = findViewById(R.id.chTATA_Other)

        chLEYLAND_MAV = findViewById(R.id.chLEYLAND_MAV)
        chLEYLAND_HCV = findViewById(R.id.chLEYLAND_HCV)
        chLEYLAND_Other = findViewById(R.id.chLEYLAND_Other)
        relativeDealingOpen = findViewById(R.id.relativeDealingOpen)
        imgArrowDealing = findViewById(R.id.imgArrowDealing)

        chMM_HCV = findViewById(R.id.chMM_HCV)
        chMM_LCV = findViewById(R.id.chMM_LCV)
        chMM_Other = findViewById(R.id.chMM_Other)

        chTractor_YES = findViewById(R.id.chTractor_YES)

        chOther_MAV = findViewById(R.id.chOther_MAV)
        chOther_HCV = findViewById(R.id.chOther_HCV)
        chOther_LCV = findViewById(R.id.chOther_LCV)
        chOther_LMV = findViewById(R.id.chOther_LMV)
        chOther_Other = findViewById(R.id.chOther_Other)

        rv = findViewById(R.id.rv)
        edtCity = findViewById(R.id.edtCity)
        edtState = findViewById(R.id.edtState)
        linearMainDealing = findViewById(R.id.linearMainDealing)
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

        spinnerStatus = findViewById(R.id.spinnerStatus)
        spinnerGrade = findViewById(R.id.spinnerGrade)
        linearKYCBox = findViewById(R.id.linearKYCBox)
        spinnerCustomerType = findViewById(R.id.spinnerCustomerType)
//        spinnerdivisionType = findViewById(R.id.spinnerdivisionType)
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

        //  chTractor_No.isChecked = true

        cardSubmit.setOnClickListener(this)
        cardBack.setOnClickListener(this)
        img.setOnClickListener(this)
        img2.setOnClickListener(this)

        imgGST.setOnClickListener(this)
        imgAdhar.setOnClickListener(this)
        imgPAN.setOnClickListener(this)
        imgOther.setOnClickListener(this)
        relativeSurveyOpen.setOnClickListener(this)
        relativeDealingOpen.setOnClickListener(this)
        spinnerCustomerType.setOnClickListener(this)
//        spinnerdivisionType.setOnClickListener(this)
        spinnerparentType.setOnClickListener(this)
        tvTATA.setOnClickListener(this)
        tvLeyland.setOnClickListener(this)
        tvMM.setOnClickListener(this)
        tvTractor.setOnClickListener(this)
        tvOther.setOnClickListener(this)

        relativeKYCOpen.setOnClickListener(this)

        println("punchhhhhhhh"+StaticSharedpreference.getInfo(Constant.todayBeatSchedule, this))




//        getcustomertype()
        getBeatList()
//        getdivisionlist()
        getparentlist(page, pageSize, parent_search)

        spinnerparentType.setOnClickListener{
            showparentdropdown(CustomerParentArr)
        }

        spinnerBeat.setOnClickListener {
            spinnerBeat.showDropDown()
        }

        val aa = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            arrGrade
        )
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGrade.setAdapter(aa)

        val aa2 = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            arrStatus
        )
        aa2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerStatus.setAdapter(aa2)

        spinnerStatus.setOnClickListener {
            spinnerStatus.showDropDown()
        }
        spinnerGrade.setOnClickListener {
            spinnerGrade.showDropDown()
        }

        spinnerBeat.setOnItemClickListener { adapterView, view, i, l ->

            for (value in beatArr) {
                if (value.beatName == spinnerBeat.text.toString()) {
                    beatPos = value.beatId.toString()
                }
            }
            Log.v("abhi", "beat pos " + beatPos)
            // beatPos = beatArr[i].beatId.toString()
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

     /*   edtMobile.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                var mobileText = s.toString()
                if (mobileText.startsWith("+91") && mobileText.length > 3) {
                    mobileText = mobileText.substring(3)
                }
                mobileText = mobileText.replace(" ", "")

                // Set the modified text back to the EditText and move the cursor to the end
                edtMobile.setText(mobileText)
                edtMobile.setSelection(mobileText.length)
            }
        })*/



        edtMobile.setOnFocusChangeListener { view, b ->
//            val inputText = "+917987062148"
            val inputText = edtMobile.text.toString()
            var mobileText = inputText
            if (mobileText.startsWith("+91")) {
                mobileText = mobileText.substring(3).trim()
            }
            mobileText = mobileText.replace(" ", "")
            edtMobile.setText(mobileText)
            edtMobile.setSelection(mobileText.length) // Move the cursor to the end
             if (!b) {
                 if (edtMobile.text.toString() != "" && edtMobile.text.toString().length == 10) {
                     mobileExist()
                 }
             }
         }

        edtGSTIN.setOnFocusChangeListener { view, b ->
            Log.v("abhi", "bbbbb " + b)
            if (!b) {
                if (edtGSTIN.text.toString().length < 15){
                    edtGSTIN.setError("GST No. is not valid")
                }else  {
                    GSTExist()
                }
            }
        }

        edtEmail.setOnFocusChangeListener { view, b ->
            Log.v("abhi", "bbbbb " + b)
            if (!b) {
                if (edtEmail.text.toString() != "" && Utilities.isValidEmail(edtEmail.text.toString())) {
                    emailExists()
                }
            }
        }


        spinnerCustomerType.setOnItemClickListener { parent, view, position, id ->
            val selectedType = typeName[position].toString()

            customerTypePos = typeID[position].toString()

            getSurveyQuestions(customerTypePos)
            if (selectedType.equals("Retailer", ignoreCase = true)) {
                spinnerparentType.visibility = View.VISIBLE
                if (StaticSharedpreference.getBoolean(Constant.todayBeatSchedule, this)){
                    spinnerBeat.visibility = View.VISIBLE
                }else if (StaticSharedpreference.getBoolean(Constant.beatUser, this)){
                    spinnerBeat.visibility = View.VISIBLE
                }else{
                    spinnerBeat.visibility = View.GONE
                }
            }else{
                spinnerparentType.visibility = View.GONE
                spinnerBeat.visibility = View.GONE
            }
            println("customerTypePos"+customerTypePos +selectedType)
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
                            Toast.makeText(this@StoreCustomerActivity,response.message(),Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@StoreCustomerActivity, resources.getString(R.string.poor_connection), Toast.LENGTH_LONG).show()
                    }
                    isLoading = false
                }
            }
        )
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

/*
    private fun spinnerParent1() {
            items.clear()
//            for (item in CustomerParentArr) {
//                items.add(item.name.toString(), item.id.toString(), item.isSelected)
//            }

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
        }
*/



        private fun GSTExist() {
        if (!Utilities.isOnline(this)) {
            return
        }

        val queryParams = java.util.HashMap<String, String>()
        queryParams["gstnumber"] = edtGSTIN.text.toString()
        ApiClient.gstNumberExists(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this).toString(),
            queryParams,
            object :
                APIResultLitener<JsonObject> {
                override fun onAPIResult(
                    response: Response<JsonObject>?, errorMessage: String?) {

                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {
                            if (response.body()!!.get("status").asString == "error") {
                                edtGSTIN.setError("GST No. is already exist")
                            }

                        } else {
                            dialog.dismiss()

                            var jsonObject: JSONObject? = null
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    this@StoreCustomerActivity,
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

    private fun getdivisionlist() {
        if (!Utilities.isOnline(this)) {
            return
        }
        val dialog = DialogClass.progressDialog(this)
        val queryParams = HashMap<String, String>()

        ApiClient.getdivisionlist(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this).toString(),
            queryParams,
            object : APIResultLitener<JsonObject> {
                override fun onAPIResult(response: Response<JsonObject>?, errorMessage: String?) {
                    dialog.dismiss()

                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {
                            val gson = Gson()
                            val listType = object :
                                TypeToken<java.util.ArrayList<CustomerDivisionModel>>() {}.type

                            CustomerDivisionArr  = gson.fromJson<java.util.ArrayList<CustomerDivisionModel>>(
                                response.body()!!.get("data").asJsonArray,
                                listType
                            )
                            val dataArray = response.body()!!.get("data").asJsonArray
//                            var arrdivisionStr : ArrayList<String> = arrayListOf()
                            for (element in dataArray) {
                                val dataObject = element.asJsonObject
                                // Assuming CustomerDivisionModel has a setTitle() method
                                val title = gson.fromJson(dataObject.get("title"), String::class.java)
                                arrdivisionStr.add(title)
                                val id = gson.fromJson(dataObject.get("id"), String::class.java)
                                arrdivisionid.add(id)
                                println("divisionnn=111="+arrdivisionStr.size)
                            }
//                            spinnerdivisionType.setOnClickListener {
//                                spinnerdivision()
//                            }

                            println("divisionnn=="+arrdivisionStr)
                        } else {

                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())
                                println("jsonObjectjsonObject ${jsonObject}")

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    this@StoreCustomerActivity,
                                    false
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                                println()
                            }

                        }
                    } else {
                        //  dialog.dismiss()
                        Toast.makeText(
                            this@StoreCustomerActivity,
                            resources.getString(R.string.poor_connection),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })

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

    private fun spinnerdivision() {
        val builder = AlertDialog.Builder(this)

        val colorsArray = arrdivisionStr.toTypedArray()

        val checkedColorsArray = BooleanArray(arrdivisionStr.size) { false }

        for (i in arrdivisionStr.indices) {
            if (selectedDivisionIds.contains(arrdivisionid[i])) {
                checkedColorsArray[i] = true
            }
        }

        val colorsList = colorsArray.toList()
        builder.setTitle("Select Division")

        builder.setMultiChoiceItems(colorsArray, checkedColorsArray) { dialog, which, isChecked ->
            checkedColorsArray[which] = isChecked
        }

        builder.setPositiveButton("OK") { dialog, which ->
            var strdivisionid = ""
            var strdivisionName = ""

            for (i in checkedColorsArray.indices) {
                val checked = checkedColorsArray[i]

                if (checked) {
                    if (strdivisionName.isEmpty()) {
                        strdivisionName = arrdivisionStr[i].toString()
                        strdivisionid = arrdivisionid[i].toString()
                    } else {
                        strdivisionName += "," + arrdivisionStr[i].toString()
                        strdivisionid += "," + arrdivisionid[i].toString()
                    }
                }
            }

//            spinnerdivisionType.setText(strdivisionName)

            selectedDivisionIds.clear()
            selectedDivisionIds.addAll(strdivisionid.split(","))


            println("strdivisionNamestrdivisionName=$strdivisionName")
            println("strdivisionNamestrdivisionid=id=$strdivisionid")
        }
        builder.setNeutralButton("Cancel") { dialog, which ->
        }

        val dialog = builder.create()
        dialog.show()
    }



    @RequiresApi(Build.VERSION_CODES.M)
    override fun onClick(p0: View) {
        when (p0.id) {

            R.id.cardBack -> {
                onBackPressed()
            }

            R.id.relativeKYCOpen -> {
                if (linearKYCBox.visibility == View.VISIBLE) {
                    linearKYCBox.visibility = View.GONE
                    val a: Animation = AnimationUtils.loadAnimation(
                        this@StoreCustomerActivity,
                        R.anim.rotate2
                    )
                    imgArrowKYC.startAnimation(a)
                    rv.visibility = View.GONE
                } else {
                    linearKYCBox.visibility = View.VISIBLE
                    val a: Animation = AnimationUtils.loadAnimation(
                        this@StoreCustomerActivity,
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
                        this@StoreCustomerActivity,
                        R.anim.rotate2
                    )
                    imgArrowSurvey.startAnimation(a)
                    rv.visibility = View.GONE
                } else {
                    val a: Animation = AnimationUtils.loadAnimation(
                        this@StoreCustomerActivity,
                        R.anim.rotate
                    )
                    a.fillAfter = true
                    imgArrowSurvey.startAnimation(a)
                    rv.visibility = View.VISIBLE
                }
            }

            R.id.relativeDealingOpen -> {
                if (linearMainDealing.visibility == View.VISIBLE) {
                    val a: Animation = AnimationUtils.loadAnimation(
                        this@StoreCustomerActivity,
                        R.anim.rotate2
                    )
                    imgArrowDealing.startAnimation(a)
                    linearMainDealing.visibility = View.GONE
                } else {
                    val a: Animation = AnimationUtils.loadAnimation(
                        this@StoreCustomerActivity,
                        R.anim.rotate
                    )
                    a.fillAfter = true
                    imgArrowDealing.startAnimation(a)
                    linearMainDealing.visibility = View.VISIBLE
                }
            }

            R.id.img -> {
                selectedImg = "outlet"
                /*  ActivityCompat.requestPermissions(
                      this,
                      arrayOf(
                          Manifest.permission.CAMERA,
                          Manifest.permission.WRITE_EXTERNAL_STORAGE,
                          Manifest.permission.READ_EXTERNAL_STORAGE,
                          Manifest.permission.READ_MEDIA_IMAGES,
                          Manifest.permission.READ_MEDIA_AUDIO,
                          Manifest.permission.READ_MEDIA_VIDEO),

                      2
                  )*/
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
                /* ActivityCompat.requestPermissions(
                     this,
                     arrayOf(
                         Manifest.permission.CAMERA,
                         Manifest.permission.WRITE_EXTERNAL_STORAGE,
                         Manifest.permission.READ_EXTERNAL_STORAGE
                     ),

                     2
                 )*/
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
                /*ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ),

                    2
                )*/
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
                /*ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ),

                    2
                )*/
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

            R.id.imgOther -> {
                selectedImg = "other"
                /*     ActivityCompat.requestPermissions(
                         this,
                         arrayOf(
                             Manifest.permission.CAMERA,
                             Manifest.permission.WRITE_EXTERNAL_STORAGE,
                             Manifest.permission.READ_EXTERNAL_STORAGE
                         ),

                         2
                     )*/
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


            R.id.spinnerCustomerType -> {
                spinnerCustomerType.showDropDown()
            }

            R.id.cardSubmit -> {

                if (spinnerCustomerType.text.toString() == "") {
                    Toast.makeText(
                        this@StoreCustomerActivity,
                        "Please select Customer Type",
                        Toast.LENGTH_LONG
                    )
                        .show()
                    return
                } else if (edtFirmName.text.toString() == "") {
                    Toast.makeText(
                        this@StoreCustomerActivity,
                        "Please enter Shop Name",
                        Toast.LENGTH_LONG
                    )
                        .show()
                    return
                } else if (edtFName.text.toString() == "") {
                    Toast.makeText(
                        this@StoreCustomerActivity,
                        "Please enter Contact Person Name",
                        Toast.LENGTH_LONG
                    ).show()
                    return
                } else if (edtMobile.text.toString() == "") {
                    Toast.makeText(
                        this@StoreCustomerActivity,
                        "Please enter Mobile",
                        Toast.LENGTH_LONG
                    ).show()
                    return
                } else if (edtMobile.text.toString().length < 10) {
                    Toast.makeText(
                        this@StoreCustomerActivity,
                        "Please enter valid Mobile",
                        Toast.LENGTH_LONG
                    ).show()
                    return
                } else if (edtMarket.text.toString() == "") {
                    Toast.makeText(
                        this@StoreCustomerActivity,
                        "Please enter Market/Area/Town Name",
                        Toast.LENGTH_LONG
                    ).show()
                    return
                }
               /* else if (beatPos == "") {
                    Toast.makeText(
                        this@StoreCustomerActivity,
                        "Please select Beat",
                        Toast.LENGTH_LONG
                    ).show()
                    return
                } */
                else if (edtAddress1.text.toString() == "") {
                    Toast.makeText(
                        this@StoreCustomerActivity,
                        "Please enter Shop Address",
                        Toast.LENGTH_LONG
                    ).show()
                    return
                } else if (spinnerPin.text.toString() == "") {
                    Toast.makeText(
                        this@StoreCustomerActivity,
                        "Please enter pin code",
                        Toast.LENGTH_LONG
                    ).show()
                    return
                } else if (spinnerPin.text.toString().length < 6) {
                    Toast.makeText(
                        this@StoreCustomerActivity,
                        "Please enter valid pin code",
                        Toast.LENGTH_LONG
                    ).show()
                    return
                } else if (spinnerGrade.text.toString()== "") {
                    Toast.makeText(
                        this@StoreCustomerActivity,
                        "Please enter Grade",
                        Toast.LENGTH_LONG
                    ).show()
                    return
                } else if (spinnerStatus.text.toString()== "") {
                    Toast.makeText(
                        this@StoreCustomerActivity,
                        "Please enter Status",
                        Toast.LENGTH_LONG
                    ).show()
                    return
                }


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

                /*if (tata == "" && layland == "" && mm == "" && tractor == "" && otherStore == "") {
                    Toast.makeText(
                        this@StoreCustomerActivity,
                        "Please select in Dealing",
                        Toast.LENGTH_LONG
                    ).show()
                    return
                }*/


                if (outletImgFile == null) {
                    Toast.makeText(
                        this@StoreCustomerActivity,
                        "Please add Outlet Image",
                        Toast.LENGTH_LONG
                    ).show()
                    return
                }


                for ((index, value) in arrList.withIndex()) {

                    if (value.isRequired == "1" && value.value == "") {

                        Toast.makeText(
                            this@StoreCustomerActivity,
                            "Please add details for ${value.fieldName}",
                            Toast.LENGTH_LONG
                        ).show()
                        return
                    }
                }
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                    1
                )


/*
                if (checkbox.isChecked) {
                    Toast.makeText(this@StoreCustomerActivity, "Checkboxxx clickedd", Toast.LENGTH_LONG).show()
                    ActivityCompat.requestPermissions(
                        this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                        1
                    )
                } else {
                    storeCustomer()
                    Toast.makeText(this@StoreCustomerActivity, "Checkboxxx NOTT clickedd", Toast.LENGTH_LONG).show()
                }
*/
            }
        }
    }

    var tata = ""
    var otherStore = ""
    var tractor = ""
    var mm = ""
    var layland = ""


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

    var gstImgFile: File? = null
    var adharImgFile: File? = null
    var otherImgFile: File? = null
    var panImgFile: File? = null
    var outletImgFile: File? = null
    var visitingCardImgFile: File? = null

    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == INTENTCAMERA && resultCode == Activity.RESULT_OK) {
            try {
                val path: File = data?.getSerializableExtra("image") as File
                cameraFile = path
                imageFile = path.path

                lifecycleScope.launch {
                    try {
                        val compressedImageFile = Compressor.compress(this@StoreCustomerActivity, path)

                        handleSelectedImage(compressedImageFile, path.path)

                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }

            } catch (e: Exception) {
                Log.v("StoreCustomerActivity", "Camera error: ${e.localizedMessage}")
            }
        }
        else if (requestCode == INTENTGALLERY && resultCode == Activity.RESULT_OK) {

            val selectedImageUri: Uri = data?.data ?: return
            val tempPath = Utilities.getPathFromUri(selectedImageUri, this@StoreCustomerActivity)
            val file = File(tempPath)

            lifecycleScope.launch {
                try {
                    val compressedImageFile = Compressor.compress(this@StoreCustomerActivity, file)

                    handleSelectedImage(compressedImageFile, tempPath)

                    imageFile = tempPath
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        } else if (requestCode == REQUEST_CHECK_SETTINGS && resultCode == Activity.RESULT_OK) {
            gettingLocation()
        }
    }


    private fun handleSelectedImage(compressedImageFile: File, originalPath: String) {
        base64 = ""

        when (selectedImg) {
            "outlet" -> {
                outletImgFile = compressedImageFile
                Glide.with(this).load(originalPath).into(imgProfile)
                outletImg = base64
            }
            "gst" -> {
                gstImgFile = compressedImageFile
                Glide.with(this).load(originalPath).into(imgGST)
                gstImg = base64
            }
            "adhar" -> {
                adharImgFile = compressedImageFile
                Glide.with(this).load(originalPath).into(imgAdhar)
                adharImg = base64
            }
            "pan" -> {
                panImgFile = compressedImageFile
                Glide.with(this).load(originalPath).into(imgPAN)
                panImg = base64
            }
            "other" -> {
                otherImgFile = compressedImageFile
                Glide.with(this).load(originalPath).into(imgOther)
                otherImg = base64
            }
            "card" -> {
                visitingCardImgFile = compressedImageFile
                Glide.with(this).load(originalPath).into(imgProfile2)
                cardImg = base64
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
                            rv.setNestedScrollingEnabled(false)
                            var mLayoutManager = LinearLayoutManager(this@StoreCustomerActivity)
                            rv.layoutManager = mLayoutManager
                            rv.setNestedScrollingEnabled(false)
                            val enquiryInfoAdapter = EnquiryInfoAdapter()
                            rv.adapter = enquiryInfoAdapter

                        } else {
                            dialog.dismiss()

                            var jsonObject: JSONObject? = null
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    this@StoreCustomerActivity,
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

    private fun emailExists() {
        if (!Utilities.isOnline(this)) {
            return
        }

        val queryParams = java.util.HashMap<String, String>()
        queryParams["email"] = edtEmail.text.toString()
        ApiClient.emailExists(
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

                            if (response.body()!!.get("status").asString == "error") {
                                edtEmail.setError("Email is already exist")
                            }

                        } else {
                            dialog.dismiss()

                            var jsonObject: JSONObject? = null
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    this@StoreCustomerActivity,
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

    private fun mobileExist() {
        if (!Utilities.isOnline(this)) {
            return
        }

        val queryParams = java.util.HashMap<String, String>()
        queryParams["mobile"] = edtMobile.text.toString()
        ApiClient.mobileNumberExists(
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
                            if (response.body()!!.get("status").asString == "error") {
                                edtMobile.setError("Contact No. is already exist")
                            }

                        } else {
                            dialog.dismiss()

                            var jsonObject: JSONObject? = null
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    this@StoreCustomerActivity,
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

                                edtState.setText(state)
                                edtCity.setText(city)
                            } catch (e: Exception) {
                                Toast.makeText(
                                    this@StoreCustomerActivity,
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
                                    this@StoreCustomerActivity,
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


    private fun selectImage() {
        val items = arrayOf<CharSequence>("Camera", "Gallery", "Cancel")
        val builder = AlertDialog.Builder(this@StoreCustomerActivity)
        builder.setTitle("Select!")
        builder.setItems(items) { dialog, item ->
            when {
                items[item] == "Camera" -> {
                    //openCamera()
                    //launchCamera()
                    var intent = Intent(this, CameraCustomActivity::class.java);
                    intent.putExtra("camera", "1")
                    startActivityForResult(intent, INTENTCAMERA)
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
            Intent.createChooser(intent, getString(R.string.please_select_image)), INTENTGALLERY
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
                            this@StoreCustomerActivity,
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
            gpsTracker = GPSTracker(this@StoreCustomerActivity)
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
        mGoogleApiClient = GoogleApiClient.Builder(this@StoreCustomerActivity)
            .addApi(LocationServices.API)
            .build()
        mGoogleApiClient!!.connect()
    }

    private fun storeCustomer() {

        if (!Utilities.isOnline(this@StoreCustomerActivity)) {
            return
        }
        var dialog = DialogClass.progressDialog(this@StoreCustomerActivity)

        println("edtFirmName ${edtFirmName.text.toString()}")
        val name = RequestBody.create(
            MediaType.parse("text"),
            edtFirmName.text.toString()
        )

        println("edtFName ${edtFName.text.toString()}")
        val fullName = RequestBody.create(
            MediaType.parse("text"),
            edtFName.text.toString()
        )

        println("edtMobile ${edtMobile.text.toString()}")
        val mobile = RequestBody.create(
            MediaType.parse("text"),
            edtMobile.text.toString()
        )

        println("edtalternateMobile ${edtalternateMobile.text.toString()}")
        val alternatemobile = RequestBody.create(
            MediaType.parse("text"),
            edtalternateMobile.text.toString()
        )

        println("parent_id ${parent_id}")
        val parent_id = RequestBody.create(
            MediaType.parse("text"),
            parent_id
        )

        println("edtEmail ${edtEmail.text.toString()}")
        val email = RequestBody.create(
            MediaType.parse("text"),
            edtEmail.text.toString()
        )

        println("edtAddress1 ${edtAddress1.text.toString()}")
        val address1 = RequestBody.create(
            MediaType.parse("text"),
            edtAddress1.text.toString()
        )

        println("latitude ${latitude}")
        val latitude = RequestBody.create(
            MediaType.parse("text"),
            latitude
        )

        println("longitude ${longitude}")
        val longitude = RequestBody.create(
            MediaType.parse("text"),
            longitude
        )

        println("beatPos ${beatPos}")
        val beatId = RequestBody.create(
            MediaType.parse("text"),
            beatPos
        )

        println("GSTNOGSTNO ${edtGSTIN.text.toString()}")
        val gstin_no = RequestBody.create(
            MediaType.parse("text"),
            edtGSTIN.text.toString()
        )

        println("edtPAN ${edtPAN.text.toString()}")
        val pan_no = RequestBody.create(
            MediaType.parse("text"),
            edtPAN.text.toString()
        )

        println("edtAdhar ${edtAdhar.text.toString()}")
        val aadhar_no = RequestBody.create(
            MediaType.parse("text"),
            edtAdhar.text.toString()
        )

        println("edtOther ${edtOther.text.toString()}")
        val otherid_no = RequestBody.create(
            MediaType.parse("text"),
            edtOther.text.toString()
        )

        println("spinnerPin ${spinnerPin.text.toString()}")
        val zipcode = RequestBody.create(
            MediaType.parse("text"),
            spinnerPin.text.toString()
        )

        println("edtMarket ${edtMarket.text.toString()}")
        val landmark = RequestBody.create(
            MediaType.parse("text"),
            edtMarket.text.toString()
        )

        println("customerTypePoscustomerTypePos="+customerTypePos)
        val customertype = RequestBody.create(
            MediaType.parse("text"),
            customerTypePos
        )

        println("spinnerGrade="+spinnerStatus.text.toString())
        val statusType = RequestBody.create(
            MediaType.parse("text"),
            spinnerStatus.text.toString()
        )
        println("spinnerGrade="+spinnerGrade.text.toString())
        val grade = RequestBody.create(
            MediaType.parse("text"),
            spinnerGrade.text.toString()
        )

        var fileToUploadOther = if (otherImgFile == null)
            MultipartBody.Part.createFormData("image", "")
        else {
            val reqbodyFileD: RequestBody =
                RequestBody.create(MediaType.parse("image/*"), otherImgFile!!)
            val fileName = "other_image"
            MultipartBody.Part.createFormData(fileName, otherImgFile!!.name, reqbodyFileD)
        }

        var fileToUploadGST = if (gstImgFile == null)
            MultipartBody.Part.createFormData("image", "")
        else {
            val reqbodyFileD: RequestBody = RequestBody.create(MediaType.parse("image/*"), gstImgFile!!)
            val fileName = "gstin_image"
            MultipartBody.Part.createFormData(fileName, gstImgFile!!.name, reqbodyFileD)
        }
//        println("reqbodyFileDreqbodyFileD="+RequestBody.create(MediaType.parse("image/*"), gstImgFile!!))

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

        var fileToUploadpassbook = MultipartBody.Part.createFormData("image", "")

        var arrSurvey: ArrayList<StoreCustomerRequestModel.Survey> = arrayListOf()
        for ((index, value) in arrList.withIndex()) {
            var storeCustomerSurveyRequestModel = StoreCustomerRequestModel().Survey()
            storeCustomerSurveyRequestModel.field_id = value.field_id.toString()
            storeCustomerSurveyRequestModel.value = value.value.toString()
            arrSurvey.add(storeCustomerSurveyRequestModel)
        }

        println("surveyArr=="+arrSurvey)
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
        println("IMGARREEEE="+fileToUploadOutet)
        println("print value "+StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this@StoreCustomerActivity).toString()
                +"name ${name}" +"fullName ${fullName}"+"mobile ${mobile}"+"alternatemobile ${alternatemobile}"+"parent_id ${parent_id}"
                +"email ${email}"+"address1 ${address1}"+"latitude ${latitude}"+"longitude ${longitude}"+"beatId ${beatId}"+
                "gstin_no ${gstin_no}"+"pan_no ${pan_no}"+"aadhar_no ${aadhar_no}"+"otherid_no ${otherid_no}"+"zipcode ${zipcode}"
                +"landmark ${landmark}"+"customertype ${customertype}"+"statusType ${statusType}"
                +"grade ${grade}"+"surveyArr ${surveyArr}"+"surveyArrDealing ${surveyArrDealing}"+
                "fileToUploadGST ${fileToUploadGST}"+"fileToUploadPAN ${fileToUploadPAN}"+
                "fileToUploadAdhar ${fileToUploadAdhar}"+"fileToUploadOther ${fileToUploadOther}"+"fileToUploadCard ${fileToUploadCard}"
                +"fileToUploadOutet ${fileToUploadOutet}"+"fileToUploadpassbook ${fileToUploadpassbook}")

        ApiClient.storeCustomer(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this@StoreCustomerActivity).toString(),
            name, fullName, mobile,
            alternatemobile, parent_id, email,
            address1, latitude, longitude, beatId, gstin_no, pan_no, aadhar_no, otherid_no, zipcode, landmark, customertype,
            statusType, grade, surveyArr, surveyArrDealing, fileToUploadGST, fileToUploadPAN, fileToUploadAdhar,
            fileToUploadOther, fileToUploadCard, fileToUploadOutet, fileToUploadpassbook,
            object : APIResultLitener<JsonObject> {
                override fun onAPIResult(
                    response: Response<JsonObject>?,
                    errorMessage: String?
                ) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {

                            Toast.makeText(
                                this@StoreCustomerActivity,
                                "Customer added successfully",
                                Toast.LENGTH_LONG
                            ).show()
                            finish()

                        } else {
                            println("Error=="+response.message())
                            var jsonObject: JSONObject? = null

                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    "error",
                                    jsonObject.getJSONArray("message")[0].toString(),
                                    this@StoreCustomerActivity,
                                    false
                                )

                            } catch (e: java.lang.Exception) {

                            }
                        }
                    } else {
                        Toast.makeText(
                            this@StoreCustomerActivity,
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
                    getcustomertype()

                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {

                            val gson = Gson()
                            val listType = object :
                                TypeToken<ArrayList<BeatModel>>() {}.type

                            beatArr = gson.fromJson<ArrayList<BeatModel>>(
                                response.body()!!.get("data").asJsonArray,
                                listType
                            )
                            val disName = arrayOfNulls<String>(beatArr!!.size)

                            for (i in beatArr!!.indices) {

                                disName[i] =
                                    beatArr!![i].beatName

                            }


                            val aa = ArrayAdapter(
                                this@StoreCustomerActivity,
                                android.R.layout.simple_list_item_1,
                                disName
                            )
                            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            //Setting the ArrayAdapter data on the Spinner
                            spinnerBeat.setAdapter(aa)


                        } else {

                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())
                                if (response.code() == 401){
                                    Toast.makeText(this@StoreCustomerActivity,jsonObject.getString("message"), Toast.LENGTH_LONG).show()
                                    StaticSharedpreference.deleteSharedPreference(this@StoreCustomerActivity)
                                    startActivity(Intent(this@StoreCustomerActivity, LoginActivity::class.java))
                                    this@StoreCustomerActivity.finishAffinity()
                                    println("Erroorr=="+jsonObject.getString("message"))
                                }else{
                                    DialogClass.alertDialog(
                                        jsonObject.getString("status"),
                                        jsonObject.getString("message"),
                                        this@StoreCustomerActivity,
                                        false
                                    )
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                        }
                    } else {
                        //  dialog.dismiss()
                        Toast.makeText(
                            this@StoreCustomerActivity,
                            resources.getString(R.string.poor_connection),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })
    }

    private fun getcustomertype() {

        if (!Utilities.isOnline(this)) {
            return
        }


        val queryParams = HashMap<String, String>()


        ApiClient.getcustomertype(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this).toString(),
            queryParams,
            object : APIResultLitener<JsonObject> {
                override fun onAPIResult(response: Response<JsonObject>?, errorMessage: String?) {


                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {

                            val gson = Gson()
                            val listType = object :
                                TypeToken<ArrayList<CustomerTypeModel>>() {}.type

                            customerTypeArr = gson.fromJson<ArrayList<CustomerTypeModel?>>(
                                response.body()!!.get("data").asJsonArray,
                                listType
                            )

                            typeName = arrayOfNulls<String>(customerTypeArr.size)
                            typeID = arrayOfNulls<String>(customerTypeArr.size)
                            /* if (customerTypeArr.isNotEmpty()) {
                                 customerTypePos = customerTypeArr[0]!!.customertype.toString()
                                 spinnerCustomerType.setText(customerTypeArr[0]!!.customertype_name)
                             }*/
                            for (i in customerTypeArr.indices) {

                                typeName[i] = customerTypeArr[i]!!.customertype_name
                                typeID[i] = customerTypeArr[i]!!.customertype.toString()

                            }
                            println("typeNametypeName ${customerTypePos}")
                            val aa = ArrayAdapter(this@StoreCustomerActivity, android.R.layout.simple_list_item_1, typeName)
                            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            //Setting the ArrayAdapter data on the Spinner
                            spinnerCustomerType.setAdapter(aa)





                            if (customerTypeArr.isEmpty())
                                getSurveyQuestions()
                            else
                                getSurveyQuestions(customerType = customerTypeArr[0]!!.customertype.toString())

                        } else {

                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    this@StoreCustomerActivity,
                                    false
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                        }
                    } else {
                        //  dialog.dismiss()
                        Toast.makeText(this@StoreCustomerActivity, resources.getString(R.string.poor_connection), Toast.LENGTH_LONG).show()
                    }
                }
            })
    }


    fun onLaunchCamera() {
        // create Intent to take a picture and return control to the calling application
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName)

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        if (photoFile != null) {
            val fileProvider: Uri =
                FileProvider.getUriForFile(this, packageName + ".provider", photoFile!!)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.
            if (intent.resolveActivity(packageManager) != null) {
                // Start the image capture intent to take photo

                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
            }
        }
    }


    // Returns the File for a photo stored on disk given the fileName
    fun getPhotoFileUri(fileName: String): File {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        val mediaStorageDir =
            File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG)

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(APP_TAG, "failed to create directory")
        }

        // Return the file target for the photo based on filename
        return File(mediaStorageDir.path + File.separator + fileName)
    }


    private fun launchCamera() {

        // Create the capture image intent
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            // Create the temporary File where the photo should go
            var photoFile: File? = null
            try {
                photoFile = Utilities.createTempImageFile(this)
            } catch (ex: IOException) {
                // Error occurred while creating the File
                ex.printStackTrace()
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                cameraFile = photoFile
                Log.v("akram", "camera file " + cameraFile)
                // Get the path of the temporary file
                //  mTempPhotoPath = photoFile.absolutePath

                // Get the content URI for the image file
                val photoURI = FileProvider.getUriForFile(
                    this,
                    Utilities.FILE_PROVIDER_AUTHORITY,
                    photoFile
                )

                // Add the URI so the camera can store the image
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)

                // Launch the camera activity
                startActivityForResult(takePictureIntent, INTENTCAMERA)
            }
        }
    }


}

private fun MutableList<SearchableItem>.add(name: String?, id: String?, selected: Boolean) {
    add(SearchableItem(name.toString(), id ?: "" ))
}

