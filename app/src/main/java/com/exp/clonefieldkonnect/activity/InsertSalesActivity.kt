package com.exp.clonefieldkonnect.activity

//import gun0912.tedimagepicker.builder.TedImagePicker
import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.adapter.ImageAdapter
import com.exp.clonefieldkonnect.connection.APIResultLitener
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.helper.Constant
import com.exp.clonefieldkonnect.helper.DialogClass
import com.exp.clonefieldkonnect.helper.StaticSharedpreference
import com.exp.clonefieldkonnect.model.DistriutorModel
import com.exp.clonefieldkonnect.model.InsertSalesRequestModel
import com.exp.import.Utilities
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.JsonObject
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response
import java.io.File

class InsertSalesActivity : AppCompatActivity(), View.OnClickListener {
    private var mResults = java.util.ArrayList<String>()
     private lateinit var recyclerView: RecyclerView
    private lateinit var edtDistributor: AutoCompleteTextView
    private lateinit var edtCustomer: AutoCompleteTextView
    private lateinit var edtCustomerMain: TextInputLayout
    private lateinit var edtDate: TextInputEditText
    private lateinit var edtAmount: TextInputEditText
    private lateinit var edtNumber: TextInputEditText
    private lateinit var cardSubmit: CardView
    private lateinit var cardBack: CardView
    private lateinit var cardSelect: CardView
    private var distributorPos: Int = -1
    private val REQUEST_CODE = 123
    private var selectedUriList: MutableList<Uri>? = null
    private var finalPath: ArrayList<String> = arrayListOf()
     var imageAdapter: ImageAdapter?  =null
    var distributorArr: List<DistriutorModel.Datum>? = arrayListOf();
    var retailerArr: List<DistriutorModel.Datum>? = arrayListOf();
    var customerId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert_sales)
        initViews()
    }

    private fun initViews() {
        cardSelect = findViewById(R.id.cardSelect)
        cardBack = findViewById(R.id.cardBack)
        edtDate = findViewById(R.id.edtDate)
        edtDistributor = findViewById(R.id.edtDistributor)
        cardSubmit = findViewById(R.id.cardSubmit)
        edtAmount = findViewById(R.id.edtAmount)
        edtNumber = findViewById(R.id.edtNumber)
        recyclerView = findViewById(R.id.recyclerView)
        edtCustomer = findViewById(R.id.edtCustomer)
        edtCustomerMain = findViewById(R.id.edtCustomerMain)
        getDistributors()

        if (intent.getStringExtra("checkin") == "n") {
            edtCustomerMain.visibility = View.VISIBLE
            getRetailers()
        }

        edtDistributor.setOnItemClickListener { adapterView, view, i, l ->

            distributorPos = i
        }

        edtCustomer.setOnClickListener {
            edtCustomer.showDropDown()
        }

        edtCustomer.setOnItemClickListener { adapterView, view, i, l ->
            customerId = retailerArr!![i].customer_id.toString()
        }

        edtDistributor.setOnClickListener(this)
        edtDate.setOnClickListener(this)
        cardSubmit.setOnClickListener(this)
        cardBack.setOnClickListener(this)
        cardSelect.setOnClickListener(this)
    }


    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.edtDistributor -> {
                edtDistributor.showDropDown()
            }
            R.id.cardBack -> {
               onBackPressed()
            }
            R.id.edtDate -> {
//                Utilities.datePicker(edtDate, this@InsertSalesActivity)
            }
            R.id.cardSelect -> {
                ActivityCompat.requestPermissions(this@InsertSalesActivity,
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ),

                    1
                )
            }
            R.id.cardSubmit -> {

                if (intent.getStringExtra("checkin") == "n") {
                    if(edtCustomer.text.toString()=="") {
                        Toast.makeText(this@InsertSalesActivity, "Please select Customer", Toast.LENGTH_LONG).show()
                        return
                    }
                }

                if (edtDistributor.text.toString() == "") {
                    Toast.makeText(this@InsertSalesActivity, "Please select distributor", Toast.LENGTH_LONG)
                        .show()
                } else if (edtNumber.text.toString() == "") {
                    Toast.makeText(this@InsertSalesActivity, "Please enter Amount", Toast.LENGTH_LONG).show()
                } else if (edtDate.text.toString() == "") {
                    Toast.makeText(this@InsertSalesActivity, "Please select Date", Toast.LENGTH_LONG).show()
                } else if (edtAmount.text.toString() == "") {
                    Toast.makeText(this@InsertSalesActivity, "Please enter Amount", Toast.LENGTH_LONG).show()
                } else {
                    insertSales()
                }
            }
        }
    }

    private fun getDistributors() {

        if (!Utilities.isOnline(this@InsertSalesActivity)) {
            return
        }
        var dialog = DialogClass.progressDialog(this@InsertSalesActivity)

        val queryParams = HashMap<String, String>()

        ApiClient.getDistributors(
            StaticSharedpreference.getInfo(
                Constant.ACCESS_TOKEN,
                this@InsertSalesActivity
            ).toString(), queryParams, object : APIResultLitener<DistriutorModel> {
                override fun onAPIResult(
                    response: Response<DistriutorModel>?,
                    errorMessage: String?
                ) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {

                            distributorArr = response.body()!!.data

                            val disName = arrayOfNulls<String>(distributorArr!!.size)

                            for (i in distributorArr!!.indices) {

                                disName[i] =
                                    distributorArr!![i].name

                            }


                            val aa = ArrayAdapter(
                                this@InsertSalesActivity,
                                android.R.layout.simple_list_item_1,
                                disName
                            )
                            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            //Setting the ArrayAdapter data on the Spinner
                            edtDistributor.setAdapter(aa)


                        }
                    } else {
                        Toast.makeText(
                            this@InsertSalesActivity,
                            resources.getString(R.string.poor_connection),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }, this@InsertSalesActivity
        )
    }

    lateinit var buyer : RequestBody

    private fun insertSales() {

        if (!Utilities.isOnline(this@InsertSalesActivity)) {
            return
        }
        var dialog = DialogClass.progressDialog(this@InsertSalesActivity)

        val queryParams = java.util.HashMap<String, String>()

        var orderDetailsRequestModel: InsertSalesRequestModel = InsertSalesRequestModel()


        var orderDetailsArrList: java.util.ArrayList<InsertSalesRequestModel.Datum> = arrayListOf()

        var orderDetailsModel = InsertSalesRequestModel().Datum()

        orderDetailsModel.images = "image"

        orderDetailsArrList.add(orderDetailsModel)

        orderDetailsRequestModel.data = orderDetailsArrList
        orderDetailsRequestModel.grand_total = edtAmount.text.toString()

        orderDetailsRequestModel.buyer_id =
            StaticSharedpreference.getInfo(Constant.USERID, this@InsertSalesActivity)
        orderDetailsRequestModel.invoice_date = edtDate.text.toString()
        orderDetailsRequestModel.invoice_no = edtNumber.text.toString()
        orderDetailsRequestModel.seller_id = distributorArr!![distributorPos].customer_id.toString()

        val parts: ArrayList<MultipartBody.Part> =
            ArrayList()
        if(imageAdapter!=null){
            val arrInt=    imageAdapter!!.deletePos()

            for(value in arrInt){
                finalPath!!.removeAt(value)

            }
        }

        for (i in 0 until finalPath.size) {
            var fileToUploadList: MultipartBody.Part?
            val file = File(finalPath[i])
            val reqbodyFileD: RequestBody =
                RequestBody.create(MediaType.parse("image/*"), file)
            val fileName = "image_" + (i + 1)
            fileToUploadList =
                MultipartBody.Part.createFormData(fileName, file.name, reqbodyFileD)
            parts.add(fileToUploadList)
        }

        val amount = RequestBody.create(
            MediaType.parse("text"),
            "" + edtAmount.text.toString()
        )

        val number = RequestBody.create(
            MediaType.parse("text"),
            "" + edtNumber.text.toString()
        )

        val seller = RequestBody.create(
            MediaType.parse("text"),
            "" + distributorArr!![distributorPos].customer_id.toString()
        )

        if (intent.getStringExtra("checkin") == "y") {

             buyer = RequestBody.create(
                MediaType.parse("text"),
                "" + StaticSharedpreference.getInfo(
                    Constant.CHECKIN_CUST_ID,
                    this@InsertSalesActivity
                )
            )

        }else{
            buyer = RequestBody.create(
                MediaType.parse("text"),
                "" +customerId
            )
        }

        val date = RequestBody.create(
            MediaType.parse("text"),
            "" + edtDate.text.toString()
        )

        ApiClient.insertSales(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this@InsertSalesActivity).toString(),
            buyer, seller, number, date, amount, parts,
            object : APIResultLitener<JsonObject> {
                override fun onAPIResult(response: Response<JsonObject>?, errorMessage: String?) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {

                            DialogClass.alertDialog(
                                "Success",
                                "" + response.body()!!.get("message").asString,
                                this@InsertSalesActivity,
                                true
                            )

                            if(selectedUriList!=null) {
                                selectedUriList!!.clear()
                            }
                        } else {

                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    this@InsertSalesActivity,
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

    private fun getRetailers() {

        if (!Utilities.isOnline(this@InsertSalesActivity)) {
            return
        }
        var dialog = DialogClass.progressDialog(this@InsertSalesActivity)

        val queryParams = HashMap<String, String>()

        ApiClient.getRetailers(
            StaticSharedpreference.getInfo(
                Constant.ACCESS_TOKEN,
                this@InsertSalesActivity
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
                                this@InsertSalesActivity,
                                android.R.layout.simple_list_item_1,
                                disName
                            )
                            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            //Setting the ArrayAdapter data on the Spinner
                            edtCustomer.setAdapter(aa)

                        }
                    } else {
                        Toast.makeText(
                            this@InsertSalesActivity,
                            resources.getString(R.string.poor_connection),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        )
    }


    var mediaPath: String? = ""


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
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


                    if(imageAdapter!=null){
                     val arrInt=    imageAdapter!!.deletePos()

                        for(value in arrInt){
                            selectedUriList!!.removeAt(value)

                        }

                    }

                   /* TedImagePicker.with(this@InsertSalesActivity)
                        //.mediaType(MediaType.IMAGE)
                        //.scrollIndicatorDateFormat("YYYYMMDD")
                        //.buttonGravity(ButtonGravity.BOTTOM)
                        .errorListener { message -> Log.d("ted", "message: $message") }
                        .selectedUri(selectedUriList)
                        .max(5, "You have selected 5 Images")
                        .startMultiImage { list: List<Uri> ->
                            showMultiImage(list.toMutableList())
                        }*/

                } else {
                    // Permission Denied
                    Toast.makeText(this@InsertSalesActivity, "Some Permission is Denied", Toast.LENGTH_SHORT)
                        .show()
                }

            }
        }// other 'case' lines to check for other
        // permissions this app might request
    }


    private var fileImagge = java.util.ArrayList<File>()
    private fun showMultiImage(uriList: MutableList<Uri>) {
        this.selectedUriList = uriList
        finalPath.clear()

        lifecycleScope.launch {
            for (value in uriList) {
                val file = File(value.path ?: continue)

                // ✅ Use new Compressor API
                val compressedImageFile = Compressor.compress(this@InsertSalesActivity, file) {
                    resolution(200, 200)   // replaces setMaxHeight / setMaxWidth
                    quality(90)            // replaces setQuality
                    format(Bitmap.CompressFormat.JPEG)
                }

                finalPath.add(compressedImageFile.path)
            }

            val layoutManager = GridLayoutManager(this@InsertSalesActivity, 3)
            recyclerView.layoutManager = layoutManager
            recyclerView.setNestedScrollingEnabled(false)
            imageAdapter = ImageAdapter(finalPath)
            recyclerView.adapter = imageAdapter
        }
    }}