package com.exp.clonefieldkonnect.activity

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.exp.clonefieldkonnect.R
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
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.default
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CustomerEditActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var edtFirmName: TextInputEditText
    private lateinit var edtFName: TextInputEditText
    private lateinit var edtLastName: TextInputEditText
    private lateinit var edtMobile: TextInputEditText
    private lateinit var edtEmail: TextInputEditText
    private lateinit var edtMarket: TextInputEditText
    private lateinit var cardSubmit: CardView
    private lateinit var cardBack: CardView
    private lateinit var checkbox: CheckBox
    private lateinit var edtAddress1: TextInputEditText
    private val REQUEST_CHECK_SETTINGS = 0x1
    private var mGoogleApiClient: GoogleApiClient? = null
    var latitude: String = ""
    var longitude: String = ""
    lateinit var spinnerBeat: AutoCompleteTextView

    lateinit var spinnerPin: TextInputEditText

    lateinit var edtGSTIN: TextInputEditText
    lateinit var edtAdhar: TextInputEditText
    lateinit var edtPAN: TextInputEditText
    lateinit var edtOther: TextInputEditText

    lateinit var imgProfile: ImageView

    lateinit var img: RelativeLayout
    lateinit var relativeKYCOpen: RelativeLayout
    lateinit var relativeSurveyOpen: RelativeLayout

    lateinit var imgGST: ImageView
    lateinit var imgAdhar: ImageView
    lateinit var imgPAN: ImageView
    lateinit var imgOther: ImageView
    lateinit var tvTitle: TextView

    lateinit var beatArr: ArrayList<BeatModel>
    lateinit var stateModel: List<StateModel.Datum>
    lateinit var districtModel: List<DistrictModel.Datum>
    lateinit var cityModel: List<CityModel.Datum>
    lateinit var pinModel: List<PinCodeModel.Datum>

    val INTENTCAMERA = 4
    val INTENTGALLERY = 5
    lateinit var cameraFile: File
    var beatPos: String = ""
    var statePos: String = ""
    var districtPos: String = ""
    var cityPos: String = ""
    var pinPos: String = ""
    var selectedImg = ""

    companion object {
        var arrList: ArrayList<EnquiryModel.Datum> = arrayListOf()
    }

    lateinit var rv: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_customer)
        arrList.clear()
        initViews()

    }


    private fun initViews() {
        rv = findViewById(R.id.rv)

        tvTitle = findViewById(R.id.tvTitle)
        relativeSurveyOpen = findViewById(R.id.relativeSurveyOpen)
        checkbox = findViewById(R.id.checkbox)
        spinnerBeat = findViewById(R.id.spinnerBeat)

        spinnerPin = findViewById(R.id.spinnerPin)
        edtAddress1 = findViewById(R.id.edtAddress1)
        cardBack = findViewById(R.id.cardBack)
        cardSubmit = findViewById(R.id.cardSubmit)
        edtFirmName = findViewById(R.id.edtFirmName)
        edtFName = findViewById(R.id.edtFName)
        edtLastName = findViewById(R.id.edtLastName)
        edtMobile = findViewById(R.id.edtMobile)
        edtEmail = findViewById(R.id.edtEmail)
        edtMarket = findViewById(R.id.edtMarket)
        img = findViewById(R.id.img)
        imgProfile = findViewById(R.id.imgProfile)


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

        imgGST.setOnClickListener(this)
        imgAdhar.setOnClickListener(this)
        imgPAN.setOnClickListener(this)
        imgOther.setOnClickListener(this)
        relativeSurveyOpen.setOnClickListener(this)

        relativeKYCOpen.setOnClickListener(this)

        tvTitle.text = "Edit Customer"

        edtMobile.isFocusable = false

        spinnerBeat.setOnClickListener {
           // spinnerBeat.showDropDown()
        }

        spinnerBeat.setOnItemClickListener { adapterView, view, i, l ->

            beatPos = beatArr[i].beatId.toString()
        }


        //getBeatList()
        getCustomerInfo()
        // getStateList()
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onClick(p0: View) {
        when (p0.id) {


            R.id.cardBack -> {
                onBackPressed()
            }

            R.id.relativeSurveyOpen -> {
                if (rv.visibility == View.VISIBLE) {
                    rv.visibility = View.GONE
                } else {
                    rv.visibility = View.VISIBLE
                }
            }

            R.id.img -> {
                selectedImg = "outlet"
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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
                }
            }

            R.id.imgGST -> {
                selectedImg = "gst"
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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
                }

            }

            R.id.imgAdhar -> {
                selectedImg = "adhar"
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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
                }

            }

            R.id.imgPAN -> {
                selectedImg = "pan"
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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
                }

            }

            R.id.imgOther -> {
                selectedImg = "other"
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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
                }

            }


            R.id.cardSubmit -> {


                if (edtFirmName.text.toString() == "") {
                    Toast.makeText(
                        this@CustomerEditActivity,
                        "Please enter Shop Name",
                        Toast.LENGTH_LONG
                    )
                        .show()
                    return
                } else if (edtFName.text.toString() == "") {
                    Toast.makeText(
                        this@CustomerEditActivity,
                        "Please enter Contact Person Name",
                        Toast.LENGTH_LONG
                    ).show()
                    return
                } else if (edtMobile.text.toString() == "") {
                    Toast.makeText(
                        this@CustomerEditActivity,
                        "Please enter Mobile",
                        Toast.LENGTH_LONG
                    ).show()
                    return
                }else if (edtAddress1.text.toString() == "") {
                    Toast.makeText(
                        this@CustomerEditActivity,
                        "Please enter Shop Address",
                        Toast.LENGTH_LONG
                    ).show()
                    return
                }

                if (checkbox.isChecked) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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

                            1
                        )
                    }


                } else {
                  //  storeCustomer()
                }

            }
        }
    }


/*
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            1 -> {
                val perms = java.util.HashMap<String, Int>()
                // Initial
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

            2 -> {
                val perms = java.util.HashMap<String, Int>()
                // Initial
                perms[Manifest.permission.READ_EXTERNAL_STORAGE] = PackageManager.PERMISSION_GRANTED
                perms[Manifest.permission.WRITE_EXTERNAL_STORAGE] =
                    PackageManager.PERMISSION_GRANTED
                perms[Manifest.permission.CAMERA] = PackageManager.PERMISSION_GRANTED

                // Fill with results
                for (i in permissions.indices)
                    perms[permissions[i]] = grantResults[i]
                // Check for ACCESS_FINE_LOCATION
                if (perms[Manifest.permission.READ_EXTERNAL_STORAGE] == PackageManager.PERMISSION_GRANTED
                    && perms[Manifest.permission.WRITE_EXTERNAL_STORAGE] == PackageManager.PERMISSION_GRANTED
                    && perms[Manifest.permission.CAMERA] == PackageManager.PERMISSION_GRANTED
                ) {
                    // All Permissions Granted
                    selectImage()
                } else {
                    // Permission Denied
                    Toast.makeText(this, "Some Permission is Denied", Toast.LENGTH_SHORT)
                        .show()
                }
            }

        }// other 'case' lines to check for other
        // permissions this app might request
    }
*/

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
        val perms = java.util.HashMap<String, Int>().apply {
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
    var adharImg: String = ""
    var panImg: String = ""
    var otherImg: String = ""
    var passbookImg: String = ""

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == INTENTCAMERA && resultCode == Activity.RESULT_OK) {
            val path = cameraFile
            imageFile = path.path

            lifecycleScope.launch {
                try {
                    val compressedFile = Compressor.compress(this@CustomerEditActivity, path) {
                        default(
                            width = 100,
                            height = 100,
                            format = Bitmap.CompressFormat.JPEG,
                            quality = 50
                        )
                    }

                    val compressedImageBitmap =
                        BitmapFactory.decodeFile(compressedFile.absolutePath)

                    compressedImageBitmap?.let { bitmap ->
                        val byteArrayOutputStream = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream)
                        val byteArray = byteArrayOutputStream.toByteArray()
                        base64 = Base64.encodeToString(byteArray, Base64.DEFAULT)

                        when (selectedImg) {
                            "outlet" -> {
                                Glide.with(this@CustomerEditActivity).load(path.path).into(imgProfile)
                                outletImg = base64
                            }
                            "gst" -> {
                                Glide.with(this@CustomerEditActivity).load(path.path).into(imgGST)
                                gstImg = base64
                            }
                            "adhar" -> {
                                Glide.with(this@CustomerEditActivity).load(path.path).into(imgAdhar)
                                adharImg = base64
                            }
                            "pan" -> {
                                Glide.with(this@CustomerEditActivity).load(path.path).into(imgPAN)
                                panImg = base64
                            }
                            "other" -> {
                                Glide.with(this@CustomerEditActivity).load(path.path).into(imgOther)
                                otherImg = base64
                            }
                        }
                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

        }
        else if (requestCode == INTENTGALLERY && resultCode == Activity.RESULT_OK) {
            val selectedImageUri: Uri = data?.data ?: return
            val tempPath = Utilities.getPathFromUri(selectedImageUri, this@CustomerEditActivity)
            val file = File(tempPath)

            lifecycleScope.launch {
                try {
                    val compressedFile = Compressor.compress(this@CustomerEditActivity, file) {
                        default(width = 100, height = 100, format = Bitmap.CompressFormat.JPEG, quality = 50)
                    }

                    val compressedImageBitmap =
                        BitmapFactory.decodeFile(compressedFile.absolutePath)

                    compressedImageBitmap?.let { bitmap ->
                        val byteArrayOutputStream = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream)
                        val byteArray = byteArrayOutputStream.toByteArray()
                        base64 = Base64.encodeToString(byteArray, Base64.DEFAULT)

                        when (selectedImg) {
                            "outlet" -> {
                                Glide.with(this@CustomerEditActivity).load(data.data).into(imgProfile)
                                outletImg = base64
                            }
                            "gst" -> {
                                Glide.with(this@CustomerEditActivity).load(data.data).into(imgGST)
                                gstImg = base64
                            }
                            "adhar" -> {
                                Glide.with(this@CustomerEditActivity).load(data.data).into(imgAdhar)
                                adharImg = base64
                            }
                            "pan" -> {
                                Glide.with(this@CustomerEditActivity).load(data.data).into(imgPAN)
                                panImg = base64
                            }
                            "other" -> {
                                Glide.with(this@CustomerEditActivity).load(data.data).into(imgOther)
                                otherImg = base64
                            }
                        }
                    }

                    imageFile = tempPath
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        else if (requestCode == REQUEST_CHECK_SETTINGS && resultCode == Activity.RESULT_OK) {
            gettingLocation()
        }
    }


    /*
        private fun storeCustomer() {

            if (!Utilities.isOnline(this@CustomerEditActivity)) {
                return
            }

            var dialog = DialogClass.progressDialog(this@CustomerEditActivity)

            var storeCustomerRequestModel = StoreCustomerRequestModel()

            storeCustomerRequestModel.name = edtFirmName.text.toString()
            storeCustomerRequestModel.full_name = edtFName.text.toString()
          //  storeCustomerRequestModel.first_name = edtFName.text.toString()
          //  storeCustomerRequestModel.last_name = edtFName.text.toString()
            storeCustomerRequestModel.mobile = edtMobile.text.toString()
            storeCustomerRequestModel.email = edtEmail.text.toString()
            storeCustomerRequestModel.address1 = edtAddress1.text.toString()
            storeCustomerRequestModel.latitude = latitude
            storeCustomerRequestModel.longitude = longitude
          //  storeCustomerRequestModel.beat_id = beatPos
            storeCustomerRequestModel.gstin_no = edtGSTIN.text.toString()
            storeCustomerRequestModel.pan_no = edtPAN.text.toString()
            storeCustomerRequestModel.aadhar_no = edtAdhar.text.toString()
            storeCustomerRequestModel.otherid_no = edtOther.text.toString()
            storeCustomerRequestModel.gstin_image = gstImg
            storeCustomerRequestModel.pan_image = panImg
            storeCustomerRequestModel.aadhar_image = adharImg
            storeCustomerRequestModel.other_image = otherImg
            storeCustomerRequestModel.zipcode = spinnerPin.text.toString()
            storeCustomerRequestModel.image = outletImg

            storeCustomerRequestModel.bank_passbook = passbookImg
            storeCustomerRequestModel.landmark = edtMarket.text.toString()
            storeCustomerRequestModel.customer_id = StaticSharedpreference.getInfo(Constant.CHECKIN_CUST_ID, this).toString()
            storeCustomerRequestModel.address_id = addressId


            ApiClient.updateCustomerProfile(
                StaticSharedpreference.getInfo(
                    Constant.ACCESS_TOKEN,
                    this@CustomerEditActivity
                ).toString(), storeCustomerRequestModel, object : APIResultLitener<JsonObject> {
                    override fun onAPIResult(
                        response: Response<JsonObject>?,
                        errorMessage: String?
                    ) {
                        dialog.dismiss()
                        if (response != null && errorMessage == null) {

                            if (response.code() == 200) {

                                Toast.makeText(
                                    this@CustomerEditActivity,
                                    "Customer updated successfully",
                                    Toast.LENGTH_LONG
                                ).show()
                                finish()

                            }
                        } else {
                            Toast.makeText(
                                this@CustomerEditActivity,
                                resources.getString(R.string.poor_connection),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            )
        }
    */

    var addressId= ""


    private fun getCustomerInfo() {
        if (!Utilities.isOnline(this)) {
            return
        }
        var dialog = DialogClass.progressDialog(this)

        val queryParams = java.util.HashMap<String, String>()
        println("customer_id=="+StaticSharedpreference.getInfo(Constant.CHECKIN_CUST_ID, this).toString())
        queryParams["customer_id"] = StaticSharedpreference.getInfo(Constant.CHECKIN_CUST_ID, this).toString()
        ApiClient.getCustomerInfo(StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this).toString(),
            queryParams,
            object :
                APIResultLitener<JsonObject> {
                override fun onAPIResult(
                    response: Response<JsonObject>?,
                    errorMessage: String?
                ) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        getSurveyQuestions()
                        if (response.code() == 200) {


                            var json = response.body()!!.get("data").asJsonObject
                            edtFirmName.setText(json.get("name").asString)
                            edtFName.setText(json.get("first_name").asString + " " + json.get("last_name").asString)
                            edtMobile.setText(json.get("mobile").asString)

                            if (!json.get("email").isJsonNull)
                                edtEmail.setText(json.get("email").asString)

                         if (!json.get("customeraddress").asJsonObject.get("landmark").isJsonNull)
                                edtMarket.setText(json.get("customeraddress").asJsonObject.get("landmark").asString)

                         if (!json.get("customeraddress").asJsonObject.get("address1").isJsonNull)
                                edtAddress1.setText(json.get("customeraddress").asJsonObject.get("address1").asString)

                           if (!json.get("customeraddress").asJsonObject.get("zipcode").isJsonNull)
                                spinnerPin.setText(json.get("customeraddress").asJsonObject.get("zipcode").asString)

                            if (!json.get("customeraddress").asJsonObject.get("id").isJsonNull)
                                addressId=  json.get("customeraddress").asJsonObject.get("id").asString




                         var jsonCustomerDetails  = json.get("customerdetails").asJsonObject

                            if (!jsonCustomerDetails.get("gstin_no").isJsonNull)
                                edtGSTIN.setText(jsonCustomerDetails.get("gstin_no").asString)

                            if (!jsonCustomerDetails.get("pan_no").isJsonNull)
                                edtPAN.setText(jsonCustomerDetails.get("pan_no").asString)

                            if (!jsonCustomerDetails.get("aadhar_no").isJsonNull)
                                edtAdhar.setText(jsonCustomerDetails.get("aadhar_no").asString)

                             if (!jsonCustomerDetails.get("otherid_no").isJsonNull)
                                edtOther.setText(jsonCustomerDetails.get("otherid_no").asString)

                            Glide.with(this@CustomerEditActivity)
                                .load(json.get("profile_image").asString)
                                .into(imgProfile)


                            var jsonCustomerDoc  = json.get("customerdocuments").asJsonArray

                           for ((index,value) in jsonCustomerDoc.withIndex()){

                               if(jsonCustomerDoc[index].asJsonObject.get("document_name").asString=="aadhar"){
                                   Glide.with(this@CustomerEditActivity)
                                       .load(jsonCustomerDoc[index].asJsonObject.get("file_path").asString)
                                       .into(imgAdhar)

                               }else if(jsonCustomerDoc[index].asJsonObject.get("document_name").asString=="other"){
                                   Glide.with(this@CustomerEditActivity)
                                       .load(jsonCustomerDoc[index].asJsonObject.get("file_path").asString)
                                       .into(imgOther)

                               }else if(jsonCustomerDoc[index].asJsonObject.get("document_name").asString=="gstin"){
                                   Glide.with(this@CustomerEditActivity)
                                       .load(jsonCustomerDoc[index].asJsonObject.get("file_path").asString)
                                       .into(imgGST)

                               }else if(jsonCustomerDoc[index].asJsonObject.get("document_name").asString=="pan"){
                                   Glide.with(this@CustomerEditActivity)
                                       .load(jsonCustomerDoc[index].asJsonObject.get("file_path").asString)
                                       .into(imgPAN)

                               }
                           }

                        } else {


                            var jsonObject: JSONObject? = null
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    this@CustomerEditActivity,
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


    private fun selectImage() {
        val items = arrayOf<CharSequence>("Camera", "Gallery", "Cancel")
        val builder = AlertDialog.Builder(this@CustomerEditActivity)
        builder.setTitle("Select!")
        builder.setItems(items) { dialog, item ->
            when {
                items[item] == "Camera" -> {
                    openCamera()
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
                            this@CustomerEditActivity,
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
            gpsTracker = GPSTracker(this@CustomerEditActivity)
            gpsTracker.getLongitude()

            if (gpsTracker.getLatitude() == 0.0) {
                gettingLocation()
            } else {
                latitude = gpsTracker.getLatitude().toString()
                longitude = gpsTracker.getLongitude().toString()
                //storeCustomer()

            }
        }, 2000)
    }

    /* Initiate Google API Client  */
    private fun initGoogleAPIClient() {
        //Without Google API Client Auto Location Dialog will not work
        mGoogleApiClient = GoogleApiClient.Builder(this@CustomerEditActivity)
            .addApi(LocationServices.API)
            .build()
        mGoogleApiClient!!.connect()
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


        ApiClient.getBeatList(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this).toString(),
            queryParams,
            object : APIResultLitener<JsonObject> {
                override fun onAPIResult(response: Response<JsonObject>?, errorMessage: String?) {


                    dialog.dismiss()
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
                                this@CustomerEditActivity,
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

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    this@CustomerEditActivity,
                                    false
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                        }
                    } else {
                        //  dialog.dismiss()
                        Toast.makeText(
                            this@CustomerEditActivity,
                            resources.getString(R.string.poor_connection),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })
    }

    private fun getSurveyQuestions() {
        if (!Utilities.isOnline(this)) {
            return
        }
        var dialog = DialogClass.progressDialog(this)

        val queryParams = java.util.HashMap<String, String>()

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

                            StoreCustomerActivity.arrList = response.body()!!.data

                            var mLayoutManager = LinearLayoutManager(this@CustomerEditActivity)
                            rv.layoutManager = mLayoutManager
                            rv.setNestedScrollingEnabled(false);
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
                                    this@CustomerEditActivity,
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


}