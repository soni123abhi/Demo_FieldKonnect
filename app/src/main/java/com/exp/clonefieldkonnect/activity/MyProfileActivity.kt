package com.exp.clonefieldkonnect.activity

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
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
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
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


class MyProfileActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var cardUpdate: CardView
    lateinit var cardBack: CardView
    lateinit var edtFirstName: TextInputEditText
    lateinit var edtMobile: TextInputEditText
    lateinit var edtEmail: TextInputEditText
     lateinit var relativeCamera: RelativeLayout
    val INTENTCAMERA = 4
    val INTENTGALLERY = 5
    lateinit var cameraFile: File
     var imageFile: String=""

    lateinit var imgProfile: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)

        initViews()
    }

    private fun initViews() {
        cardBack = findViewById(R.id.cardBack)
        relativeCamera = findViewById(R.id.relativeCamera)
        imgProfile = findViewById(R.id.imgProfile)
        edtFirstName = findViewById(R.id.edtFirstName)
        cardUpdate = findViewById(R.id.cardUpdate)
        edtMobile = findViewById(R.id.edtMobile)
        edtEmail = findViewById(R.id.edtEmail)

        cardBack.setOnClickListener {
            onBackPressed()
        }

        edtFirstName.setText(StaticSharedpreference.getInfo(Constant.FIRM_NAME,this))
        edtMobile.setText(StaticSharedpreference.getInfo(Constant.MOBILE,this))
        edtEmail.setText(StaticSharedpreference.getInfo(Constant.EMAIL,this))

        Glide.with(this)
            .load(ApiClient.BASE_IMAGE_URL+StaticSharedpreference.getInfo(Constant.PROFILE_IMAGE,this))
            .into(imgProfile)

        cardUpdate.setOnClickListener(this)
        relativeCamera.setOnClickListener(this)
        imgProfile.setOnClickListener(this)

    }

    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.imgProfile -> {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ),

                    1
                )
            }
            R.id.relativeCamera ->
                imgProfile.performClick()
            R.id.cardUpdate -> {

                if(imageFile.equals("")){
                        Toast.makeText(this@MyProfileActivity,"Please select image",Toast.LENGTH_LONG).show()
                }else{
                    updateProfile()
                }
            }

        }
    }



    private fun updateProfile() {

        if (!Utilities.isOnline(this@MyProfileActivity)) {
            return
        }
        var dialog = DialogClass.progressDialog(this@MyProfileActivity)


        var fileToUploadList: MultipartBody.Part?


        if (imageFile.equals("")) {
            val reqbodyFile = RequestBody.create(MediaType.parse("text"), "")
            fileToUploadList = MultipartBody.Part.createFormData("image", "")
        } else {

            var file: File? = File(imageFile)
            try {
                file = Compressor(this).compressToFile(file)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            val reqbodyFileD: RequestBody =
                RequestBody.create(MediaType.parse("image/*"), file)
            val fileName = "image"
            fileToUploadList =
                MultipartBody.Part.createFormData(fileName, file!!.name, reqbodyFileD)
        }

        ApiClient.updateProfile(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this).toString(),
            fileToUploadList,
            object : APIResultLitener<JsonObject> {
                override fun onAPIResult(response: Response<JsonObject>?, errorMessage: String?) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {

                            DialogClass.alertDialog("Success","profile update successfully",this@MyProfileActivity,false)
                             StaticSharedpreference.saveInfo(Constant.PROFILE_IMAGE,response.body()!!.get("profile_image").asString,this@MyProfileActivity)

                        } else {

                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                if (response.code() == 401){
                                    Toast.makeText(this@MyProfileActivity,jsonObject.getString("message"), Toast.LENGTH_LONG).show()
                                    StaticSharedpreference.deleteSharedPreference(this@MyProfileActivity)
                                    startActivity(Intent(this@MyProfileActivity, LoginActivity::class.java))
                                    this@MyProfileActivity.finishAffinity()
                                    println("Erroorr=="+jsonObject.getString("message"))
                                }else{
                                    DialogClass.alertDialog(
                                        jsonObject.getString("status"),
                                        jsonObject.getString("message"),
                                        this@MyProfileActivity
                                        ,
                                        false
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
        } else if (requestCode == INTENTGALLERY) {

            if (resultCode == Activity.RESULT_OK) {

                val selectedImageUri: Uri = data!!.data!!

                val tempPath = Utilities.getPathFromUri(selectedImageUri, this@MyProfileActivity)


                Glide.with(this)
                    .load(data.data)
                    .into(imgProfile)

                imageFile = tempPath


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


    private fun selectImage() {
        val items = arrayOf<CharSequence>("Camera", "Gallery", "Cancel")
        val builder = AlertDialog.Builder(this@MyProfileActivity)
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
    }}


