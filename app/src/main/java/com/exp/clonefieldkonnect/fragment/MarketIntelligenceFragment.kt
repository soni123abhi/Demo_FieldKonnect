package com.exp.clonefieldkonnect.fragment

import FieldData
import FillterModel
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.activity.CameraCustomActivity
import com.exp.clonefieldkonnect.activity.LoginActivity
import com.exp.clonefieldkonnect.activity.MainActivity
import com.exp.clonefieldkonnect.connection.APIResultLitener
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.helper.Constant
import com.exp.clonefieldkonnect.helper.DialogClass
import com.exp.clonefieldkonnect.helper.StaticSharedpreference
import com.exp.clonefieldkonnect.model.*
import com.exp.import.Utilities
import com.google.gson.JsonObject
import id.zelory.compressor.Compressor
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.util.HashMap


class MarketIntelligenceFragment(var relativeHome: RelativeLayout,var three_dot: CardView) : Fragment() {
    lateinit var activityLocal: Activity
    private lateinit var rootView: View
    private lateinit var edtState: AutoCompleteTextView
    private lateinit var cardshowww: CardView
    private lateinit var container: LinearLayout
    lateinit var img: RelativeLayout
    lateinit var imgProfile: ImageView
    var cameraResultLauncher: ActivityResultLauncher<Intent>? = null
    var photoFile: File? = null

    private var isLoading = false
    var statelist : ArrayList<StateModel.Datum> = ArrayList()
    var statelistname : ArrayList<String> = ArrayList()
    var statelistid : ArrayList<String> = ArrayList()


    private val formData = mutableMapOf<String, String>()


    var selectedstate_id = ""
    val INTENTCAMERA = 4
    val INTENTGALLERY = 5
    lateinit var cameraFile: File
    var outletImgFile: File? = null
    companion object {
        var arrList: ArrayList<EnquiryModel.Datum> = arrayListOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_market_intelligence, container, false)
        activityLocal = activity as MainActivity

        initViews()

        cameraResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val takenImage = BitmapFactory.decodeFile(photoFile!!.absolutePath)
                imgProfile.setImageBitmap(takenImage)
            } else {
                Toast.makeText(activityLocal, "Error taking picture", Toast.LENGTH_SHORT).show()
            }
        }
        return rootView
    }

    private fun initViews() {
        three_dot.visibility=View.GONE
        edtState = rootView.findViewById(R.id.edtState)
        img = rootView.findViewById(R.id.img)
        imgProfile = rootView.findViewById(R.id.imgProfile)
        cardshowww = rootView.findViewById(R.id.cardshowww)
        container = rootView.findViewById(R.id.dynamicContainer)



        var divisoin_id = StaticSharedpreference.getInfo(Constant.DIVISION_ID, activityLocal).toString()
        println("divisoin_id=="+divisoin_id)

        img.setOnClickListener {
            selectImage()
        }
        cardshowww.setOnClickListener {
            submitmarketintelligence()
        }

        getfillterdata()
        getstatedata()

    }

    private fun submitmarketintelligence() {

        isLoading = true

        if (!Utilities.isOnline(activityLocal)) {
            isLoading = false
            return
        }

        var dialog = DialogClass.progressDialog(activityLocal)
        val queryParams = HashMap<String, String>()


        val jsonFormData = getFormDataAsJson()
        val requestBodyJson = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonFormData.toString())

        println("jsonFormDatajsonFormData=="+jsonFormData)

        var fileToUploadOther = if (outletImgFile == null)
            MultipartBody.Part.createFormData("servey_image", "")
        else {
            val reqbodyFileD: RequestBody = RequestBody.create(MediaType.parse("image/*"), outletImgFile!!)
            val fileName = "servey_image"
            MultipartBody.Part.createFormData(fileName, outletImgFile!!.name, reqbodyFileD)
        }

        println("DDDD=="+jsonFormData+"<<"+fileToUploadOther+"<<"+requestBodyJson)


        ApiClient.marketingintelligence(StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, activityLocal).toString(),
            queryParams,requestBodyJson,fileToUploadOther,
            object : APIResultLitener<JsonObject> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onAPIResult(
                    response: Response<JsonObject>?,
                    errorMessage: String?
                ) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {
                            dialog.dismiss()
                            responsemessage(response.body()!!.get("message").asString)
                            startActivity(Intent(activityLocal, MainActivity::class.java))
                        } else {
                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())
                                if (response.code() == 401){
                                    Toast.makeText(activityLocal,jsonObject.getString("message"), Toast.LENGTH_LONG).show()
                                    StaticSharedpreference.deleteSharedPreference(activityLocal)
                                    startActivity(Intent(activityLocal, LoginActivity::class.java))
                                    activityLocal.finishAffinity()
                                    println("Erroorr=="+jsonObject.getString("message"))
                                }else{
                                    DialogClass.alertDialog(
                                        jsonObject.getString("status"),
                                        jsonObject.getString("message"),
                                        activityLocal, false
                                    )
                                }

                            } catch (e: Exception) {
                                println("Errrororo="+e.toString())
                                e.printStackTrace()
                            }
                        }
                    }else{
                        println("Errrororo="+response!!.message())
                    }
                }
            })
    }


    private fun getFormDataAsJson(): JSONObject {
        val jsonObject = JSONObject()

        for ((key, value) in formData) {
            jsonObject.put(key, value)
        }
        jsonObject.put("state_id", selectedstate_id)


        return jsonObject
    }




    private fun responsemessage(msg: String) {
        Toast.makeText(activityLocal,msg,Toast.LENGTH_SHORT).show()
    }

    private fun getfillterdata() {

        isLoading = true

        if (!Utilities.isOnline(activityLocal)) {
            isLoading = false
            return
        }
        var dialog = DialogClass.progressDialog(activityLocal)
        val queryParams = HashMap<String, String>()
        println("TTTT=="+StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, activityLocal).toString())
        ApiClient.getfillterList(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, activityLocal).toString(), queryParams,
            object : APIResultLitener<FillterModel> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onAPIResult(response: Response<FillterModel>?, errorMessage: String?) {
                    dialog.dismiss()

                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {
                            val dataList = response.body()?.data ?: emptyList()

                            println("dataList==="+dataList)

                            generateDynamicFields(dataList)
                           

                        } else {
                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                if (response.code() == 401){
                                    Toast.makeText(activityLocal,jsonObject.getString("message"), Toast.LENGTH_LONG).show()
                                    StaticSharedpreference.deleteSharedPreference(activityLocal)
                                    startActivity(Intent(activityLocal, LoginActivity::class.java))
                                    activityLocal.finishAffinity()
                                    println("Erroorr=="+jsonObject.getString("message"))
                                }else{
                                    DialogClass.alertDialog(
                                        jsonObject.getString("status"),
                                        jsonObject.getString("message"),
                                        activityLocal, false
                                    )
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        isLoading = false
                    }
                    else {
                        Toast.makeText(activityLocal, resources.getString(R.string.poor_connection), Toast.LENGTH_LONG).show()
                    }
                }
            })
    }

    private fun generateDynamicFields(dataList: List<FieldData>) {
        container.removeAllViews()
        formData.clear()

        for (field in dataList) {
            // Create TextView for field_name
            val textView = TextView(activityLocal).apply {
                id = View.generateViewId()
                text = field.field_name
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
                setTypeface(ResourcesCompat.getFont(activityLocal, R.font.inter_regular), Typeface.BOLD)
                setTextColor(ContextCompat.getColor(activityLocal, R.color.black))
                setPadding(dpToPx(10), dpToPx(5), dpToPx(10), dpToPx(5))
                setSingleLine(true)
            }

            val textViewParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(dpToPx(0), 0, dpToPx(0), dpToPx(2))
            }
            container.addView(textView, textViewParams)

            // Define LayoutParams for Input Fields
            val inputFieldParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dpToPx(40) // Default height
            ).apply {
                setMargins(dpToPx(5), 0, dpToPx(5), dpToPx(10))
            }

            if (field.field_type == "Input") {
                // Determine inputType based on input_type value
                val inputTypeValue = when (field.input_type?.lowercase()) {
                    "number" -> InputType.TYPE_CLASS_NUMBER
                    else -> InputType.TYPE_CLASS_TEXT
                }
                // Create EditText
                val editText = EditText(activityLocal).apply {
                    id = View.generateViewId()
                    setBackgroundResource(R.drawable.edittext_shape_new)
                    hint = "Enter ${field.field_name}"
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
                    setTypeface(ResourcesCompat.getFont(activityLocal, R.font.inter_regular), Typeface.NORMAL)
                    setPadding(dpToPx(10), 0, dpToPx(10), 0)
                    setTextColor(Color.parseColor("#000000"))
                    setHintTextColor(ContextCompat.getColor(activityLocal, R.color.hintColor))
                    inputType = inputTypeValue // Set inputType based on field.input_type
                }

                // Store input text when user types
                editText.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        formData[field.key] = s.toString()
                    }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                })

                container.addView(editText, inputFieldParams)
            }


            else if (field.field_type == "Select" && field.fields_data.isNotEmpty()) {
                val autoCompleteTextView = AutoCompleteTextView(activityLocal).apply {
                    id = View.generateViewId()
                    setBackgroundResource(R.drawable.edittext_shape_new)
                    hint = "Select ${field.field_name}"
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
                    setTypeface(ResourcesCompat.getFont(activityLocal, R.font.inter_regular), Typeface.NORMAL)
                    setSingleLine(true)
                    setPadding(dpToPx(10), 0, dpToPx(10), 0)
                    setTextColor(Color.parseColor("#000000"))
                    setHintTextColor(ContextCompat.getColor(activityLocal, R.color.hintColor))
                    setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_spinner, 0)
                    isClickable = true
                    isFocusable = false
                    isFocusableInTouchMode = false
                    inputType = InputType.TYPE_NULL
                    keyListener = null
                }

                // Populate dropdown values
                val fieldValues = field.fields_data.map { it.value }
                val adapter = ArrayAdapter(activityLocal, R.layout.custom_dropdown_item, fieldValues)
                autoCompleteTextView.setAdapter(adapter)

                // Create an EditText (Initially Hidden)
                val othersEditText = EditText(activityLocal).apply {
                    id = View.generateViewId()
                    setBackgroundResource(R.drawable.edittext_shape_new)
                    hint = "Enter ${field.field_name}"
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
                    setTypeface(ResourcesCompat.getFont(activityLocal, R.font.inter_regular), Typeface.NORMAL)
                    setPadding(dpToPx(10), 0, dpToPx(10), 0)
                    setTextColor(Color.parseColor("#000000"))
                    setHintTextColor(ContextCompat.getColor(activityLocal, R.color.hintColor))
                    inputType = InputType.TYPE_CLASS_TEXT
                    visibility = View.GONE  // Initially Hidden
                }

                // Show dropdown when clicked
                autoCompleteTextView.setOnClickListener {
                    autoCompleteTextView.showDropDown()
                }

                // Handle selection
                autoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
                    val selectedValue = fieldValues[position]
                    autoCompleteTextView.setText(selectedValue, false) // Set selected value
                    if (selectedValue.equals("Others", ignoreCase = true)) {
                        othersEditText.visibility = View.VISIBLE
                        othersEditText.requestFocus()
                        formData.remove(field.key) // Remove previous value if exists
                    } else {
                        othersEditText.visibility = View.GONE
                        formData[field.key] = selectedValue // Store selected value
                    }
                }

                // Capture input when user types in "Others" EditText
                othersEditText.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        if (othersEditText.visibility == View.VISIBLE) {
                            formData[field.key] = s.toString() // Store user input instead of selected value
                        }
                    }
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                })

                // Add views to the container
                container.addView(autoCompleteTextView, inputFieldParams)
                container.addView(othersEditText, inputFieldParams)
            }
        }
    }

    // Helper function to convert dp to pixels
    private fun dpToPx(dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), activityLocal.resources.displayMetrics
        ).toInt()
    }



    private fun getstatedata() {
        isLoading = true

        if (!Utilities.isOnline(activityLocal)) {
            isLoading = false
            return
        }
        var dialog = DialogClass.progressDialog(activityLocal)
        val queryParams = HashMap<String, String>()

        ApiClient.getStateList(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, activityLocal).toString(), queryParams,
            object : APIResultLitener<StateModel> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onAPIResult(response: Response<StateModel>?, errorMessage: String?) {
                    dialog.dismiss()

                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {

                            statelist.clear()

                            statelist.addAll(response.body()!!.data)

                            for (item in statelist) {
                                val name = item.stateName.toString()
                                val id = item.stateId.toString()

                                if (!statelistname.contains(name)) {
                                    statelistname.add(name)
                                    statelistid.add(id)
                                }
                            }
                            edtState.setOnClickListener {
                                spinnerstate()
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
                        isLoading = false
                    }
                    else {
                        Toast.makeText(activityLocal, resources.getString(R.string.poor_connection), Toast.LENGTH_LONG).show()
                    }
                }
            })
    }


    private fun spinnerstate() {
        val builder = AlertDialog.Builder(activityLocal)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_alert_dialog2, null)
        builder.setView(dialogView)

        val searchEditText = dialogView.findViewById<EditText>(R.id.searchEditText)
        val listView = dialogView.findViewById<ListView>(R.id.listView)
        val button = dialogView.findViewById<Button>(R.id.button)

        val colorsArray = statelistname.map { it.toString() }.toTypedArray()
        val adapter = ArrayAdapter(activityLocal, android.R.layout.simple_list_item_1, colorsArray)
        listView.adapter = adapter

        builder.setTitle("Select State")

        val dialog = builder.create()

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {
                adapter.filter.filter(charSequence)
            }

            override fun afterTextChanged(editable: Editable) {}
        })
        button.setOnClickListener {
            edtState.setText("")
            selectedstate_id = ""
            dialog.dismiss()
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedPosition = statelistname.indexOf(adapter.getItem(position).toString())
            if (selectedPosition != -1) {
                val selectedParentId = statelistid[selectedPosition].toString()
                val selectedParentName = statelistname[selectedPosition].toString()

                edtState.setText(selectedParentName)
                selectedstate_id = selectedParentId

                println("Abhi=id=$selectedstate_id")


                dialog.dismiss()
            }
        }

        dialog.show() // Show the dialog
    }



    override fun onResume() {
        super.onResume()
        requireView().isFocusableInTouchMode = true
        requireView().requestFocus()
        requireView().setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                three_dot.visibility=View.VISIBLE
                relativeHome.performClick()
                return@OnKeyListener true
            }
            false
        })
    }

    var imageFile: String = ""
    var base64: String = ""
    var outletImg: String = ""


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == INTENTCAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    //   val path =    cameraFile
                    var path: File = data!!.getSerializableExtra("image") as File
                    Log.v("akram", "path " + arrList.size)
                    //  compressImage(path)
                    cameraFile = path
                    //  var bitmap = getBitmap(path.path)

                    //imageFile = Compressor(this@MyProfileActivity).compressToFile(path)
                    imageFile = path.path

                    var compressedImageFile: File? = null
                    try {
                        compressedImageFile =
                            Compressor(activityLocal).setMaxHeight(200)
                                .setMaxWidth(200)
                                .setQuality(90).compressToFile(path)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                    base64 = ""

                    outletImgFile = compressedImageFile
                    Glide.with(this)
                        .load(path.path)
                        .into(imgProfile)
                    outletImg = base64

                } catch (e: Exception) {

                    Log.v("akram", "tri inside")
                }
            }
        } else if (requestCode == INTENTGALLERY) {

            if (resultCode == Activity.RESULT_OK) {

                val selectedImageUri: Uri = data!!.data!!

                val tempPath =
                    Utilities.getPathFromUri(selectedImageUri, activityLocal)

                val file = File(tempPath)


                var compressedImageFile: File? = null
                try {
                    compressedImageFile =
                        Compressor(activityLocal).setMaxHeight(200).setMaxWidth(200)
                            .setQuality(90).compressToFile(file)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                base64 = ""

                outletImgFile = compressedImageFile
                Glide.with(this)
                    .load(data.data)
                    .into(imgProfile)
                outletImg = base64

                imageFile = tempPath

            }
        }
    }


    private fun selectImage() {
        val items = arrayOf<CharSequence>("Camera", "Gallery", "Cancel")
        val builder = AlertDialog.Builder(activityLocal)
        builder.setTitle("Select!")
        builder.setItems(items) { dialog, item ->
            when {
                items[item] == "Camera" -> {
                    //openCamera()
                    //launchCamera()
                    var intent = Intent(activityLocal, CameraCustomActivity::class.java);
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


}