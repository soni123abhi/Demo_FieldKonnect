package com.exp.clonefieldkonnect.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.exp.import.Utilities
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.connection.APIResultLitener
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.helper.Constant
import com.exp.clonefieldkonnect.helper.DialogClass
import com.exp.clonefieldkonnect.helper.StaticSharedpreference
import com.exp.clonefieldkonnect.model.DistriutorModel
import com.exp.clonefieldkonnect.model.VisitTypeModel
import com.bumptech.glide.Glide
import com.exp.clonefieldkonnect.model.DraftReportModel
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class VisitReportActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var edtType: AutoCompleteTextView
    private lateinit var edtTitle: EditText
    private lateinit var edtDesc: EditText
    private lateinit var edtCustomerMain: TextInputLayout
    private lateinit var edtCustomer: AutoCompleteTextView
    private lateinit var cardSubmit: CardView
    private lateinit var cardBack: CardView
    private lateinit var imgProfile: ImageView
    private lateinit var cardImage: RelativeLayout
    var typeId = ""
    var retailerArr: List<DistriutorModel.Datum>? = arrayListOf();
    var customerId = ""
    val INTENTCAMERA = 4
    val INTENTGALLERY = 5
    lateinit var cameraFile: File
    var imageFile: String = ""
    var isLead: Boolean = false
    private var isLoading = false

    var visitArr: ArrayList<VisitTypeModel> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visit_report)
        initViews()
    }

    private fun initViews() {
        cardImage = findViewById(R.id.cardImage)
        edtCustomerMain = findViewById(R.id.edtCustomerMain)
        imgProfile = findViewById(R.id.imgProfile)
        edtCustomer = findViewById(R.id.edtCustomer)
        cardSubmit = findViewById(R.id.cardSubmit)
        edtType = findViewById(R.id.edtType)
        edtTitle = findViewById(R.id.edtTitle)
        edtDesc = findViewById(R.id.edtDesc)
        cardBack = findViewById(R.id.cardBack)

        isLead = intent.getBooleanExtra("isLead", false)

        if (intent.getStringExtra("checkin") == "n") {
            edtCustomerMain.visibility = View.VISIBLE
            cardImage.visibility = View.GONE
            getRetailers()
        }

        getdraftreport(edtDesc)
        getVisitTypes()

        edtType.setOnItemClickListener { adapterView, view, i, l ->
            typeId = visitArr[i].typeId.toString()
        }
        edtCustomer.setOnClickListener {
            edtCustomer.showDropDown()
        }

        edtCustomer.setOnItemClickListener { adapterView, view, i, l ->

            customerId = retailerArr!![i].customer_id.toString()
        }

        cardSubmit.setOnClickListener(this)
        edtType.setOnClickListener(this)
        cardBack.setOnClickListener(this)
        imgProfile.setOnClickListener(this)
    }

    private fun getdraftreport(edtDesc: EditText) {
        isLoading = true
        if (!Utilities.isOnline(this@VisitReportActivity)) {
            isLoading = false
            return
        }
        var dialog = DialogClass.progressDialog(this@VisitReportActivity)
        val queryParams = java.util.HashMap<String, String>()
        queryParams["checkin_id"] = StaticSharedpreference.getInfo(Constant.CHECKIN_ID,this@VisitReportActivity).toString()

        ApiClient.getdraftreport(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this@VisitReportActivity).toString(),
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
                                    this@VisitReportActivity, false
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        isLoading = false
                    }
                    else {
                        Toast.makeText(this@VisitReportActivity, resources.getString(R.string.data_not_found), Toast.LENGTH_LONG).show()
                    }
                }
            })    }



    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.edtType -> {
                edtType.showDropDown()
            }
            R.id.imgProfile -> {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.CAMERA
                    ),

                    1
                )
            }

            R.id.cardBack -> {
                onBackPressed()
            }

            R.id.cardSubmit -> {

                if (intent.getStringExtra("checkin") == "n") {

                    if (edtCustomer.text.toString() == "") {
                        Toast.makeText(
                            this@VisitReportActivity,
                            "Please select Customer",
                            Toast.LENGTH_LONG
                        ).show()
                        return
                    }
                }

                if (edtType.text.toString() == "") {
                    Toast.makeText(
                        this@VisitReportActivity,
                        "Please select Report type",
                        Toast.LENGTH_LONG
                    )
                        .show()
                } else if (edtDesc.text.toString() == "") {
                    Toast.makeText(
                        this@VisitReportActivity,
                        "Please enter Description",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    var intent = Intent()
                    intent.putExtra("type_id",typeId)
                    intent.putExtra("desc",edtDesc.text.toString())
                    setResult(RESULT_OK, intent)
                    finish()
                    //submitVisitReports()
                }
            }
        }
    }

    private fun getVisitTypes() {

        if (!Utilities.isOnline(this@VisitReportActivity)) {
            return
        }
        var dialog = DialogClass.progressDialog(this@VisitReportActivity)

        val queryParams = HashMap<String, String>()

        ApiClient.getVisitTypes(
            StaticSharedpreference.getInfo(
                Constant.ACCESS_TOKEN,
                this@VisitReportActivity
            ).toString(), queryParams, object : APIResultLitener<JsonObject> {
                override fun onAPIResult(
                    response: Response<JsonObject>?,
                    errorMessage: String?
                ) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {


                            val gson = Gson()
                            val listType = object :
                                TypeToken<ArrayList<VisitTypeModel>>() {}.type

                            visitArr = gson.fromJson<ArrayList<VisitTypeModel>>(
                                response.body()!!.get("data").asJsonArray,
                                listType
                            )


                            val disName = arrayOfNulls<String>(visitArr.size)

                            for (i in visitArr.indices) {

                                disName[i] =
                                    visitArr[i].typeName

                            }


                            val aa = ArrayAdapter(
                                this@VisitReportActivity,
                                android.R.layout.simple_list_item_1,
                                disName
                            )
                            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            //Setting the ArrayAdapter data on the Spinner
                            edtType.setAdapter(aa)

                        }
                    } else {
                        Toast.makeText(
                            this@VisitReportActivity,
                            resources.getString(R.string.poor_connection),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }, this@VisitReportActivity
        )
    }

    private fun getRetailers() {

        if (!Utilities.isOnline(this@VisitReportActivity)) {
            return
        }
        var dialog = DialogClass.progressDialog(this@VisitReportActivity)

        val queryParams = HashMap<String, String>()

        ApiClient.getRetailers(
            StaticSharedpreference.getInfo(
                Constant.ACCESS_TOKEN,
                this@VisitReportActivity
            ).toString(), queryParams, object : APIResultLitener<DistriutorModel> {
                override fun onAPIResult(
                    response: Response<DistriutorModel>?,
                    errorMessage: String?
                ) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {

                            retailerArr = response.body()!!.data

                            val disName = arrayOfNulls<String>(retailerArr!!.size)

                            for (i in retailerArr!!.indices) {

                                disName[i] =
                                    retailerArr!![i].name

                            }

                            val aa = ArrayAdapter(
                                this@VisitReportActivity,
                                android.R.layout.simple_list_item_1,
                                disName
                            )
                            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            //Setting the ArrayAdapter data on the Spinner
                            edtCustomer.setAdapter(aa)

                        }
                    } else {
                        Toast.makeText(
                            this@VisitReportActivity,
                            resources.getString(R.string.poor_connection),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        )
    }

    private fun submitVisitReports() {

        if (!Utilities.isOnline(this@VisitReportActivity)) {
            return
        }

        var dialog = DialogClass.progressDialog(this@VisitReportActivity)

        val queryParams = java.util.HashMap<String, String>()

        val checkin_id: RequestBody
        val customer_id: RequestBody
        val lead_id: RequestBody
        if (intent.getStringExtra("checkin") == "y") {

            checkin_id = RequestBody.create(
                MediaType.parse("text"),
                "" + StaticSharedpreference.getInfo(Constant.CHECKIN_ID, this@VisitReportActivity)
                    .toString()
            )

            if (isLead) {
                lead_id = RequestBody.create(
                    MediaType.parse("text"),
                    "" + StaticSharedpreference.getInfo(
                        Constant.CHECKIN_CUST_ID,
                        this@VisitReportActivity
                    )
                        .toString()
                )

                customer_id = RequestBody.create(
                    MediaType.parse("text"),
                    ""
                )
            } else {
                customer_id = RequestBody.create(
                    MediaType.parse("text"),
                    "" + StaticSharedpreference.getInfo(
                        Constant.CHECKIN_CUST_ID,
                        this@VisitReportActivity
                    )
                        .toString()
                )

                lead_id = RequestBody.create(
                    MediaType.parse("text"),
                    ""

                )
            }
            /*   queryParams["checkin_id"] =
                   StaticSharedpreference.getInfo(Constant.CHECKIN_ID, this@VisitReportActivity)
                       .toString()
               queryParams["customer_id"] =
                   StaticSharedpreference.getInfo(Constant.CHECKIN_CUST_ID, this@VisitReportActivity)
                       .toString()*/
        } else {

            lead_id = RequestBody.create(
                MediaType.parse("text"),
                ""

            )

            customer_id = RequestBody.create(
                MediaType.parse("text"),
                customerId
            )

            checkin_id = RequestBody.create(
                MediaType.parse("text"),
                ""
            )

        }

//        var fileToUploadList: MultipartBody.Part?
//
//
//        if (imageFile.equals("")) {
//            val reqbodyFile = RequestBody.create(MediaType.parse("text"), "")
//            fileToUploadList = MultipartBody.Part.createFormData("image", "")
//        } else {
//
//            var file: File? = File(imageFile)
//            try {
//                file = Compressor(this).compressToFile(file)
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//
//            val reqbodyFileD: RequestBody =
//                RequestBody.create(MediaType.parse("image/*"), file)
//            val fileName = "image"
//            fileToUploadList =
//                MultipartBody.Part.createFormData(fileName, file!!.name, reqbodyFileD)
//        }

        val visit_type_id = RequestBody.create(
            MediaType.parse("text"),
            "" + typeId.toString()
        )
        val report_title = RequestBody.create(
            MediaType.parse("text"),
            ""
        )

        val description = RequestBody.create(
            MediaType.parse("text"),
            "" + edtDesc.text.toString()
        )


        /* queryParams["visit_type_id"] = typeId.toString()
         queryParams["report_title"] = edtTitle.text.toString()
         queryParams["description"] = edtDesc.text.toString()
 */
        ApiClient.submitVisitReports(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this@VisitReportActivity)
                .toString(),
            customer_id,
            checkin_id,
            visit_type_id,
            report_title,
            description,
            lead_id,
            object : APIResultLitener<JsonObject> {
                override fun onAPIResult(response: Response<JsonObject>?, errorMessage: String?) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {

                            StaticSharedpreference.saveInfo(
                                Constant.REPORT_SUBMIT,
                                "1",
                                this@VisitReportActivity
                            )
                            Toast.makeText(
                                this@VisitReportActivity,
                                "Report Submitted",
                                Toast.LENGTH_LONG
                            ).show()

                            var intent = Intent()
                            setResult(RESULT_OK, intent)
                            finish()
                            /*  DialogClass.alertDialog(
                                  "Success",
                                  "" + response.body()!!.get("message").asString,
                                  this@VisitReportActivity,
                                  true
                              )
  */
                        } else {

                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    this@VisitReportActivity,
                                    false
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    } else {
                        //  dialog.dismiss()

                    }
                }
            })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == INTENTCAMERA) {

            if (resultCode == Activity.RESULT_OK) {
                val path = cameraFile

                //  compressImage(path)

                //  var bitmap = getBitmap(path.path)

                //imageFile = Compressor(this@MyProfileActivity).compressToFile(path)
                imageFile = path.path

                Glide.with(this)
                    .load(path.path)
                    .into(imgProfile)


            }
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
                // Initial

                perms[Manifest.permission.CAMERA] = PackageManager.PERMISSION_GRANTED

                // Fill with results
                for (i in permissions.indices)
                    perms[permissions[i]] = grantResults[i]
                // Check for ACCESS_FINE_LOCATION
                if (perms[Manifest.permission.CAMERA] == PackageManager.PERMISSION_GRANTED
                ) {
                    // All Permissions Granted
                    openCamera()
                } else {
                    // Permission Denied
                    Toast.makeText(this, "Some Permission is Denied", Toast.LENGTH_SHORT)
                        .show()
                }
            }

        }// other 'case' lines to check for other
        // permissions this app might request
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
}
