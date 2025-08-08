package com.exp.clonefieldkonnect.fragment

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.FileProvider
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exp.import.Utilities
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.activity.MainActivity
import com.exp.clonefieldkonnect.activity.PaymentReceivedActivity
import com.exp.clonefieldkonnect.adapter.CustomerSearchAdapter
import com.exp.clonefieldkonnect.adapter.ImageAdapter
import com.exp.clonefieldkonnect.adapter.UnpaidInvoiceAdapter
import com.exp.clonefieldkonnect.connection.APIResultLitener
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.helper.Constant
import com.exp.clonefieldkonnect.helper.DialogClass
import com.exp.clonefieldkonnect.helper.StaticSharedpreference
import com.exp.clonefieldkonnect.model.DistriutorModel
import com.exp.clonefieldkonnect.model.PaymentReceivedSalesArrRequestModel
import com.exp.clonefieldkonnect.model.UnpaidInvoiceModel
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.google.gson.JsonObject
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
import kotlin.collections.HashMap

class PaymentReceivedFragment : Fragment(), View.OnClickListener {

    lateinit var activityLocal: Activity
    private var mResults = java.util.ArrayList<String>()
    private lateinit var rootView: View
    private lateinit var recyclerView: RecyclerView
    private lateinit var edtCustomer: AutoCompleteTextView
    private lateinit var edtDate: TextInputEditText
    private lateinit var edtReference: TextInputEditText
    private lateinit var edtMode: AutoCompleteTextView
    private lateinit var edtBankName: TextInputEditText
    private lateinit var edtDescription: TextInputEditText
    private lateinit var tvInvoicePayment: TextView
    private lateinit var tvCustomerAdvance: TextView
    private lateinit var cardSubmit: CardView
    private lateinit var cardBurger: CardView
    private lateinit var cardInvoicePayment: CardView
    private lateinit var cardCustomerAdvance: CardView
    private lateinit var tlCustomer: TextInputLayout
    private lateinit var linearTop: LinearLayout
    private lateinit var cardImage: CardView
    private lateinit var img: ImageView
    private var distributorPos: Int = -1
    private val REQUEST_CODE = 123
    private var selectedUriList: MutableList<Uri>? = null
    private var finalPath: ArrayList<String> = arrayListOf()
    var imageAdapter: ImageAdapter? = null
    var distributorArr: ArrayList<DistriutorModel.Datum>? = arrayListOf();
    val INTENTCAMERA = 4
    val INTENTGALLERY = 5
    lateinit var cameraFile: File

    companion object {
        lateinit var edtAmount: TextInputEditText
    }

    val modeList = arrayListOf<String>("Cash", "Bank Transfer", "Cheque", "Credit Card", "UPI")
    var arrUnpaidInvoice: ArrayList<UnpaidInvoiceModel.Datum> = arrayListOf()
    var paymentType = "Invoice payment"
    var from:String=""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_payment_received, container, false)


        initViews()
        return rootView

    }

    private fun initViews() {
        linearTop = rootView.findViewById(R.id.linearTop)
        tlCustomer = rootView.findViewById(R.id.tlCustomer)
        cardImage = rootView.findViewById(R.id.cardImage)
        img = rootView.findViewById(R.id.img)
        cardCustomerAdvance = rootView.findViewById(R.id.cardCustomerAdvance)
        cardInvoicePayment = rootView.findViewById(R.id.cardInvoicePayment)
        tvCustomerAdvance = rootView.findViewById(R.id.tvCustomerAdvance)
        tvInvoicePayment = rootView.findViewById(R.id.tvInvoicePayment)
        edtDescription = rootView.findViewById(R.id.edtDescription)
        edtBankName = rootView.findViewById(R.id.edtBankName)
        edtReference = rootView.findViewById(R.id.edtReference)
        edtMode = rootView.findViewById(R.id.edtMode)
        cardBurger = rootView.findViewById(R.id.cardBurger)
        edtDate = rootView.findViewById(R.id.edtPaymentDate)
        edtCustomer = rootView.findViewById(R.id.edtCustomer)
        cardSubmit = rootView.findViewById(R.id.cardSubmit)
        edtAmount = rootView.findViewById(R.id.edtAmount)
        recyclerView = rootView.findViewById(R.id.recyclerView)

         from = requireArguments().getString("from").toString()
        if(from=="beat"){
            activityLocal = activity as PaymentReceivedActivity

            linearTop.visibility = View.GONE
            tlCustomer.visibility = View.GONE
            getUnpaidInvoice(StaticSharedpreference.getInfo(
                Constant.CHECKIN_CUST_ID,
                activityLocal
            )
                .toString())
        }else if(from=="dashboard"){
            activityLocal = activity as PaymentReceivedActivity

            linearTop.visibility = View.GONE
            getRetailers()
        }else{
            activityLocal = activity as MainActivity

            getRetailers()
        }


        var mLayoutManager =
            LinearLayoutManager(activityLocal)
        recyclerView.layoutManager = mLayoutManager

        edtCustomer.setOnItemClickListener { adapterView, view, i, l ->
            distributorPos = i

            autoCompleteTextViewProduct_onItemClick(adapterView, view, i, l)

        }

        edtAmount.isEnabled = false

        val aa = ArrayAdapter(
            activityLocal,
            android.R.layout.simple_list_item_1,
            modeList
        )
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        //Setting the ArrayAdapter data on the Spinner
        edtMode.setAdapter(aa)

        edtCustomer.setOnTouchListener { view, motionEvent ->

            edtCustomer.showDropDown()

            false

        }

        cardSubmit.setOnClickListener {

            if(from=="menu"||from=="dashboard") {
                if (edtCustomer.text.toString() == "") {
                    Toast.makeText(activityLocal, "Please select Customer", Toast.LENGTH_LONG)
                        .show()
                    return@setOnClickListener
                }
            }

            if (edtDate.text.toString() == "") {
                Toast.makeText(activityLocal, "Please select Date", Toast.LENGTH_LONG).show()
            } else if (edtMode.text.toString() == "") {
                Toast.makeText(activityLocal, "Please Select Payment Mode", Toast.LENGTH_LONG)
                    .show()
            } else if (paymentType == "Customer Advance" && edtAmount.text.toString() == "") {
                Toast.makeText(activityLocal, "Please Enter Amount", Toast.LENGTH_LONG).show()
            } else if (edtReference.text.toString() == "") {
                Toast.makeText(activityLocal, "Please Enter Reference No.", Toast.LENGTH_LONG)
                    .show()
            } else if (edtBankName.text.toString() == "") {
                Toast.makeText(activityLocal, "Please Enter Bank Name.", Toast.LENGTH_LONG).show()
            } else if (edtDescription.text.toString() == "") {
                Toast.makeText(activityLocal, "Please Enter Description.", Toast.LENGTH_LONG).show()
            } else {

                if (paymentType == "Invoice payment") {
                    for (value in arrUnpaidInvoice) {

                        if (value.enterAmount != "" && value.enterAmount.toInt() > value.amountUnpaid!!) {

                            Toast.makeText(
                                activityLocal,
                                "Enter Amount is Greater than Due Amount for Invoice Number ${value.invoiceNo}",
                                Toast.LENGTH_LONG
                            ).show()

                            return@setOnClickListener
                        }

                    }
                }

                if (edtAmount.text.toString() == "" || edtAmount.text.toString() == "0") {
                    Toast.makeText(
                        activityLocal,
                        "Enter Amount for at least one Invoice",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    paymentReceived()
                }
            }
        }

        edtMode.setOnClickListener(this)
        edtDate.setOnClickListener(this)
        cardBurger.setOnClickListener(this)
        cardCustomerAdvance.setOnClickListener(this)
        cardInvoicePayment.setOnClickListener(this)
        cardImage.setOnClickListener(this)

        recyclerView.isNestedScrollingEnabled = false;
    }

    private fun autoCompleteTextViewProduct_onItemClick(
        adapterView: AdapterView<*>,
        view: View,
        position: Int,
        id: Long
    ) {
        val product: DistriutorModel.Datum =
            adapterView.getItemAtPosition(position) as DistriutorModel.Datum
        edtCustomer.setText(product.name)

        getUnpaidInvoice(product.customer_id!!.toString())
    }


    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.edtCustomer -> {
                // edtCustomer.showDropDown()
            }
            R.id.edtMode -> {
                edtMode.showDropDown()
            }

            R.id.cardBurger -> {
                MainActivity.drawerLayout.openDrawer(GravityCompat.START)
            }

            R.id.cardCustomerAdvance -> {
                paymentType = "Customer Advance"
                arrUnpaidInvoice.clear()
                recyclerView.visibility = View.GONE
                edtAmount.isEnabled = true

                cardCustomerAdvance.setCardBackgroundColor(Color.parseColor("#685BD6"))
                cardInvoicePayment.setCardBackgroundColor(Color.parseColor("#ffffff"))
                tvCustomerAdvance.setTextColor(Color.parseColor("#ffffff"))
                tvInvoicePayment.setTextColor(Color.parseColor("#685BD6"))
            }

            R.id.cardInvoicePayment -> {
                paymentType = "Invoice payment"
                recyclerView.visibility = View.VISIBLE
                edtAmount.isEnabled = false
                cardCustomerAdvance.setCardBackgroundColor(Color.parseColor("#ffffff"))
                cardInvoicePayment.setCardBackgroundColor(Color.parseColor("#685BD6"))
                tvCustomerAdvance.setTextColor(Color.parseColor("#685BD6"))
                tvInvoicePayment.setTextColor(Color.parseColor("#ffffff"))
            }
            R.id.edtPaymentDate -> {
             }
            R.id.cardImage -> {
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
            }
        }
    }

    private fun getRetailers() {

        if (!Utilities.isOnline(activityLocal)) {
            return
        }
        var dialog = DialogClass.progressDialog(activityLocal)

        val queryParams = HashMap<String, String>()

        ApiClient.getRetailers(
            StaticSharedpreference.getInfo(
                Constant.ACCESS_TOKEN,
                activityLocal
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
                                    distributorArr!![i].name.toString()
                            }

                            val aa = ArrayAdapter(
                                activityLocal,
                                android.R.layout.simple_list_item_1,
                                disName
                            )
                            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            //Setting the ArrayAdapter data on the Spinner

                            edtCustomer.setAdapter(
                                CustomerSearchAdapter(
                                    activityLocal,
                                    distributorArr
                                )
                            )


                        }
                    } else {
                        Toast.makeText(
                            activityLocal,
                            resources.getString(R.string.poor_connection),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        )
    }

    private fun getUnpaidInvoice(id: String) {

        if (!Utilities.isOnline(activityLocal)) {
            return
        }

        var dialog = DialogClass.progressDialog(activityLocal)

        val queryParams = HashMap<String, String>()
        queryParams["customer_id"] = id
        ApiClient.getUnpaidInvoice(
            StaticSharedpreference.getInfo(
                Constant.ACCESS_TOKEN,
                activityLocal
            ).toString(), queryParams, object : APIResultLitener<UnpaidInvoiceModel> {
                override fun onAPIResult(
                    response: Response<UnpaidInvoiceModel>?,
                    errorMessage: String?
                ) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {

                            arrUnpaidInvoice = response.body()!!.data!!

                            var categoryAdapter = UnpaidInvoiceAdapter(arrUnpaidInvoice)
                            categoryAdapter.notifyDataSetChanged()
                            recyclerView.adapter = categoryAdapter

                            /* distributorArr = response.body()!!.data

                             val disName = arrayOfNulls<String>(distributorArr!!.size)

                             for (i in distributorArr!!.indices) {

                                 disName[i] =
                                     distributorArr!![i].name.toString()

                             }


                             val aa = ArrayAdapter(
                                 activityLocal,
                                 android.R.layout.simple_list_item_1,
                                 disName
                             )
                             aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                             //Setting the ArrayAdapter data on the Spinner
                             edtCustomer.setAdapter(aa)

 */
                        }
                    } else {
                        Toast.makeText(
                            activityLocal,
                            resources.getString(R.string.poor_connection),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        )
    }


    private fun paymentReceived() {

        if (!Utilities.isOnline(activityLocal)) {
            return
        }
        var dialog = DialogClass.progressDialog(activityLocal)

        val paymentReceivedSalesArrRequestModel = PaymentReceivedSalesArrRequestModel()
        var salesArrList: java.util.ArrayList<PaymentReceivedSalesArrRequestModel> =
            arrayListOf()

        for (value in arrUnpaidInvoice) {
            if (value.enterAmount != "") {
                paymentReceivedSalesArrRequestModel.amount = value.enterAmount
                paymentReceivedSalesArrRequestModel.invoice_no = value.invoiceNo
                paymentReceivedSalesArrRequestModel.sales_id = value.id.toString()

                salesArrList.add(paymentReceivedSalesArrRequestModel)
            }
        }
       // paymentReceivedSalesArrRequestModel.detail = salesArrList

        val json = Gson().toJson(salesArrList)
        val jsonArray = RequestBody.create(
            MediaType.parse("text"),
            "" + json
        )

        val paymentDate = RequestBody.create(
            MediaType.parse("text"),
            "" + edtDate.text.toString()
        )

        val paymentTypeRB = RequestBody.create(
            MediaType.parse("text"),
            "" + paymentType
        )
        val customerId = RequestBody.create(
            MediaType.parse("text"),
            "" + StaticSharedpreference.getInfo(
                Constant.CHECKIN_CUST_ID,
                activityLocal
            )
                .toString()
        )

        val paymentMode = RequestBody.create(
            MediaType.parse("text"),
            "" + edtMode.text.toString()
        )

        val amount = RequestBody.create(
            MediaType.parse("text"),
            "" + edtAmount.text.toString()
        )
        val reference = RequestBody.create(
            MediaType.parse("text"),
            "" + edtReference.text.toString()
        )

        val bankName = RequestBody.create(
            MediaType.parse("text"),
            "" + edtBankName.text.toString()
        )

        val description = RequestBody.create(
            MediaType.parse("text"),
            "" + edtDescription.text.toString()
        )

        var fileToUploadList: MultipartBody.Part?


        if (imageFile.equals("")) {
            val reqbodyFile = RequestBody.create(MediaType.parse("text"), "")
            fileToUploadList = MultipartBody.Part.createFormData("image", "")
        } else {

            var file: File? = File(imageFile)
            try {
                file = Compressor(activityLocal).compressToFile(file)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            val reqbodyFileD: RequestBody =
                RequestBody.create(MediaType.parse("image/*"), file)
            val fileName = "image"
            fileToUploadList =
                MultipartBody.Part.createFormData(fileName, file!!.name, reqbodyFileD)
        }

        ApiClient.paymentReceived(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, activityLocal).toString(),

            jsonArray,paymentDate,paymentTypeRB,customerId,paymentMode,amount,reference,bankName,description,fileToUploadList,
            object : APIResultLitener<JsonObject> {
                override fun onAPIResult(response: Response<JsonObject>?, errorMessage: String?) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {

                            alertDialog("success", response.body()!!.get("message").asString, activityLocal, false)

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

                    }
                }
            })
    }

    /*
        private fun paymentReceived() {

            if (!Utilities.isOnline(activityLocal)) {
                return
            }
            var dialog = DialogClass.progressDialog(activityLocal)

            val queryParams = java.util.HashMap<String, String>()

            var orderDetailsRequestModel: PaymentReceivedRequestModel = PaymentReceivedRequestModel()


            var orderDetailsArrList: java.util.ArrayList<PaymentReceivedRequestModel.Datum> = arrayListOf()

            var orderDetailsModel = PaymentReceivedRequestModel().Datum()

            for (value in arrUnpaidInvoice) {

                if(value.enterAmount!="") {

                    orderDetailsModel.amount= value.enterAmount
                    orderDetailsModel.invoice_no= value.invoiceNo
                    orderDetailsModel.sales_id= value.id.toString()

                    orderDetailsArrList.add(orderDetailsModel)
                }
            }

            orderDetailsRequestModel.detail = orderDetailsArrList


            orderDetailsRequestModel.payment_type = paymentType

            orderDetailsRequestModel.customer_id =
                distributorArr!![distributorPos].customer_id!!.toString()
            orderDetailsRequestModel.payment_date = edtDate.text.toString()
            orderDetailsRequestModel.payment_mode = edtMode.text.toString()
            orderDetailsRequestModel.amount = edtAmount.text.toString()
            orderDetailsRequestModel.reference_no = edtReference.text.toString()
            orderDetailsRequestModel.bank_name = edtBankName.text.toString()
            orderDetailsRequestModel.description = edtDescription.text.toString()

            ApiClient.paymentReceived(
                StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, activityLocal).toString(),
                orderDetailsRequestModel,
                object : APIResultLitener<JsonObject> {
                    override fun onAPIResult(response: Response<JsonObject>?, errorMessage: String?) {
                        dialog.dismiss()
                        if (response != null && errorMessage == null) {

                            if (response.code() == 200) {

                                alertDialog("success",response.body()!!.get("message").asString,activityLocal,false)

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

                        }
                    }
                })
        }
    */
    fun alertDialog(
        title: String,
        description: String,
        activity: Activity,
        isFinishActivity: Boolean
    ) {

        val builder = AlertDialog.Builder(activity)

        builder.setMessage(description)
            .setCancelable(false)
            .setPositiveButton("OK") { dialog, id ->

                if(from=="menu"||from=="dashboard") {
                    MainActivity.tabPosition = 0
                    startActivity(Intent(activityLocal, MainActivity::class.java))
                    activityLocal.finishAffinity()
                }else{
                    activityLocal.finish()
                }
            }

        val alert = builder.create()
        //Setting the title manually
        alert.setTitle(title)
        alert.show()
    }


    var mediaPath: String? = ""
    var imageFile: String = ""

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == INTENTCAMERA) {

            if (resultCode == Activity.RESULT_OK) {
                val path = cameraFile

                //  compressImage(path)

                //  var bitmap = getBitmap(path.path)

                //imageFile = Compressor(this@MyProfileActivity).compressToFile(path)
                img.visibility = View.VISIBLE
                imageFile = path.path

                Glide.with(this)
                    .load(path.path)
                    .into(img)


            }
        } else if (requestCode == INTENTGALLERY) {

            if (resultCode == Activity.RESULT_OK) {

                val selectedImageUri: Uri = data!!.data!!

                val tempPath = Utilities.getPathFromUri(selectedImageUri, activityLocal)

                img.visibility = View.VISIBLE
                Glide.with(this)
                    .load(data.data)
                    .into(img)

                imageFile = tempPath


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
        val perms = java.util.HashMap<String, Int>().apply {
            // Initialize with PERMISSION_GRANTED
            requiredPermissions.forEach { this[it] = PackageManager.PERMISSION_GRANTED }
        }

        for (i in permissions.indices) {
            perms[permissions[i]] = grantResults[i]
        }

        val allPermissionsGranted = requiredPermissions.all { perms[it] == PackageManager.PERMISSION_GRANTED }

        if (allPermissionsGranted) {

            if (requestCode == 1){
                selectImage()
            }
        } else {
            // Permission Denied
            Toast.makeText(context, "Some Permission is Denied", Toast.LENGTH_SHORT).show()
        }
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
            Environment.DIRECTORY_PICTURES
        )

        try {
            // Make sure the Pictures directory exists.
            storageDir.mkdirs()
        } catch (e: Exception) {

        }

// Save a file: path for use with ACTION_VIEW intents
        return File.createTempFile(
            imageFileName, /* prefix */
            ".jpg", /* suffix */
            storageDir      /* directory */
        )
    }

    private fun selectImage() {
        val items = arrayOf<CharSequence>("Camera", "Gallery", "Cancel")
        val builder = AlertDialog.Builder(activityLocal)
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

}