package com.exp.import


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.ConnectivityManager
import android.net.Uri
import android.provider.OpenableColumns
import android.provider.Settings
import android.util.Log
import android.util.Patterns
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.exp.clonefieldkonnect.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.NetworkInterface
import java.text.SimpleDateFormat
import java.util.*


object Utilities {
     const val FILE_PROVIDER_AUTHORITY = "com.abridge.gajra.provider"

    fun isOnline(activity: Activity): Boolean {
        val cm = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        if (netInfo != null && netInfo.isConnectedOrConnecting) {
            return true
        }
        Toast.makeText(activity, activity.getString(R.string.error_no_internet), Toast.LENGTH_SHORT)
            .show()
        return false
    }

    fun isDeveloperOptionsEnabled(context: Context): Boolean {
        return Settings.Global.getInt(
            context.contentResolver,
            Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0
        ) != 0
    }

    fun isVpnActive(): Boolean {
        try {
            val networkInterfaces = NetworkInterface.getNetworkInterfaces()
            while (networkInterfaces.hasMoreElements()) {
                val networkInterface = networkInterfaces.nextElement()
                if (!networkInterface.isUp || networkInterface.interfaceAddresses.isEmpty()) continue

                val name = networkInterface.name
                if (name.equals("tun0", ignoreCase = true) || name.equals("ppp0", ignoreCase = true)) {
                    return true // VPN detected
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    @SuppressLint("MissingPermission")
    fun isFakeLocationDetected(activity: Activity, callback: (Boolean) -> Unit) {
        val fusedLocationClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(activity)

        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.w("SecurityValidator", "Location permission not granted")
            callback(false)
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null && location.isFromMockProvider) {
                Log.d("SecurityValidator", "Fake location detected.")
                callback(true)
            } else {
                callback(false)
            }
        }.addOnFailureListener {
            Log.e("SecurityValidator", "Location fetch failed: ${it.message}")
            callback(false)
        }
    }



    fun showExitDialog(
        context: Context,
        message: String = "Developer options must be turned off to use this app."
    ) {
        if (context is Activity && context.isFinishing) return

        val dialog = AlertDialog.Builder(context)
            .setTitle("Access Restricted")
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton("Exit") { _, _ ->
                if (context is Activity) {
                    context.finishAffinity()
                }
            }
            .create()

        dialog.setOnShowListener {
            val exitButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            exitButton?.setTextColor(ContextCompat.getColor(context, R.color.colur_light_blue))
        }

        dialog.show()
    }



    fun isValidEmail(address: String?): Boolean {
        return if (address != null || address != "") {
            Patterns.EMAIL_ADDRESS.matcher(address).matches()
        } else {
            false
        }
    }

    fun datePicker(textInputEditText: TextView, context: Context) {


        val c = Calendar.getInstance()
        val mYear = c.get(Calendar.YEAR) // current year
        val mMonth = c.get(Calendar.MONTH) // current month
        val mDay = c.get(Calendar.DAY_OF_MONTH) // current day
        // date picker dialog
        val datePickerDialog = DatePickerDialog(
            context,
            DatePickerDialog.OnDateSetListener { view, year, monthInt, dayOfMonth ->
                // set day of month , month and year value in the edit text
                val month = monthInt + 1

                val day: String
                val monthStr: String
                if (dayOfMonth < 10) {
                    day = "0$dayOfMonth"
                } else {
                    day = dayOfMonth.toString() + ""
                }

                if (month < 10) {
                    monthStr = "0$month"
                } else {
                    monthStr = month.toString() + ""
                }

                textInputEditText.setText("$day-$monthStr-$year")
            }, mYear, mMonth, mDay
        )
//        datePickerDialog.datePicker.maxDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }

    fun datePicker2(textInputEditText: TextView, context: Context) {


        val c = Calendar.getInstance()
        val mYear = c.get(Calendar.YEAR) // current year
        val mMonth = c.get(Calendar.MONTH) // current month
        val mDay = c.get(Calendar.DAY_OF_MONTH) // current day
        // date picker dialog
        val datePickerDialog = DatePickerDialog(
            context,
            DatePickerDialog.OnDateSetListener { view, year, monthInt, dayOfMonth ->
                // set day of month , month and year value in the edit text
                val month = monthInt + 1

                val day: String
                val monthStr: String
                if (dayOfMonth < 10) {
                    day = "0$dayOfMonth"
                } else {
                    day = dayOfMonth.toString() + ""
                }

                if (month < 10) {
                    monthStr = "0$month"
                } else {
                    monthStr = month.toString() + ""
                }

                textInputEditText.setText("$day-$monthStr-$year")
            }, mYear, mMonth, mDay
        )
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }

    fun datePickerFuture(textInputEditText: EditText, context: Context) {

        val c = Calendar.getInstance()
        val mYear = c.get(Calendar.YEAR) // current year
        val mMonth = c.get(Calendar.MONTH) // current month
        val mDay = c.get(Calendar.DAY_OF_MONTH) // current day
        // date picker dialog
        val datePickerDialog = DatePickerDialog(
            context,
            DatePickerDialog.OnDateSetListener { view, year, monthInt, dayOfMonth ->
                // set day of month , month and year value in the edit text
                val month = monthInt + 1

                val day: String
                val monthStr: String
                if (dayOfMonth < 10) {
                    day = "0$dayOfMonth"
                } else {
                    day = dayOfMonth.toString() + ""
                }

                if (month < 10) {
                    monthStr = "0$month"
                } else {
                    monthStr = month.toString() + ""
                }

                textInputEditText.setText("$year-$monthStr-$day")
            }, mYear, mMonth, mDay
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }

    fun datePickerconditional(
        textInputEditText: TextView,
        toDate: String,
        fromDate: String,
        isFrom: Boolean,
        context: Activity
    ) {
        val c = Calendar.getInstance()
        val mYear = c.get(Calendar.YEAR) // current year
        val mMonth = c.get(Calendar.MONTH) // current month
        val mDay = c.get(Calendar.DAY_OF_MONTH) // current day

        // date picker dialog
        val datePickerDialog = DatePickerDialog(
            context,
            DatePickerDialog.OnDateSetListener { _, year, monthInt, dayOfMonth ->
                // set day of month, month, and year value in the edit text
                val month = monthInt + 1

                val day: String = if (dayOfMonth < 10) {
                    "0$dayOfMonth"
                } else {
                    dayOfMonth.toString()
                }

                val monthStr: String = if (month < 10) {
                    "0$month"
                } else {
                    month.toString()
                }

                val selectedDate = "$day-$monthStr-$year"
                val sdf = SimpleDateFormat("dd-MM-yyyy")
                val selectedDateF = sdf.parse(selectedDate)

                if (isFrom && toDate.isNotEmpty()) {
                    val dateFTo = sdf.parse(toDate)
                    if (selectedDateF != null && selectedDateF.compareTo(dateFTo) > 0) {
                        alertDialog("From Date is Before or equal to To Date", context)
                        return@OnDateSetListener
                    }
                } else if (!isFrom && fromDate.isNotEmpty()) {
                    val dateFFrom = sdf.parse(fromDate)
                    if (selectedDateF != null && selectedDateF.compareTo(dateFFrom) < 0) {
                        alertDialog("To Date is equal or after From Date", context)
                        return@OnDateSetListener
                    }
                }

                textInputEditText.text = selectedDate
            }, mYear, mMonth, mDay
        )

        datePickerDialog.show()
    }


    fun datePickerFuture2(textInputEditText: TextView, context: Context) {


        val c = Calendar.getInstance()
        val mYear = c.get(Calendar.YEAR) // current year
        val mMonth = c.get(Calendar.MONTH) // current month
        val mDay = c.get(Calendar.DAY_OF_MONTH) // current day
        // date picker dialog
        val datePickerDialog = DatePickerDialog(
            context,
            DatePickerDialog.OnDateSetListener { view, year, monthInt, dayOfMonth ->
                // set day of month , month and year value in the edit text
                val month = monthInt + 1

                val day: String
                val monthStr: String
                if (dayOfMonth < 10) {
                    day = "0$dayOfMonth"
                } else {
                    day = dayOfMonth.toString() + ""
                }

                if (month < 10) {
                    monthStr = "0$month"
                } else {
                    monthStr = month.toString() + ""
                }
                textInputEditText.setText("$day-$monthStr-$year")
            }, mDay, mMonth, mYear
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }

    fun datePickercurrentlastday(textInputEditText: TextView, context: Context) {
        val c = Calendar.getInstance()
        val mYear = c.get(Calendar.YEAR) // current year
        val mMonth = c.get(Calendar.MONTH) // current month
        val mDay = c.get(Calendar.DAY_OF_MONTH) // current day

        val datePickerDialog = DatePickerDialog(
            context,
            DatePickerDialog.OnDateSetListener { _, year, monthInt, dayOfMonth ->
                val month = monthInt + 1

                val day: String = if (dayOfMonth < 10) {
                    "0$dayOfMonth"
                } else {
                    dayOfMonth.toString()
                }

                val monthStr: String = if (month < 10) {
                    "0$month"
                } else {
                    month.toString()
                }
                textInputEditText.setText("$day-$monthStr-$year")
            }, mYear, mMonth, mDay
        )

        c.add(Calendar.DAY_OF_MONTH, -1)
        datePickerDialog.datePicker.minDate = c.timeInMillis
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    fun datePicker(
        textInputEditText: TextView,
        toDate: String,
        fromDate: String,
        isFrom: Boolean,
        context: Activity
    ) {


        val c = Calendar.getInstance()
        val mYear = c.get(Calendar.YEAR) // current year
        val mMonth = c.get(Calendar.MONTH) // current month
        val mDay = c.get(Calendar.DAY_OF_MONTH) // current day
        // date picker dialog
        val datePickerDialog = DatePickerDialog(
            context,
            DatePickerDialog.OnDateSetListener { view, year, monthInt, dayOfMonth ->
                // set day of month , month and year value in the edit text
                val month = monthInt + 1

                val day: String
                val monthStr: String
                if (dayOfMonth < 10) {
                    day = "0$dayOfMonth"
                } else {
                    day = dayOfMonth.toString() + ""
                }

                if (month < 10) {
                    monthStr = "0$month"
                } else {
                    monthStr = month.toString() + ""
                }


                var selectedDate = "$day-$monthStr-$year"
                val sdf =
                    SimpleDateFormat("dd-MM-yyyy")
                val selectedDateF = sdf.parse(selectedDate)

                if (isFrom && toDate != "") {

                    val dateFTo = sdf.parse(toDate)

                    if (selectedDateF.compareTo(dateFTo) > 0) {
                        alertDialog("From Date is Before or equal to To Date", context)
                        return@OnDateSetListener
                    }

                } else if (!isFrom && fromDate != "") {
                    val dateFFrom = sdf.parse(fromDate)

                    Log.v("akram", "compare " + selectedDateF.compareTo(dateFFrom))

                    if (selectedDateF.compareTo(dateFFrom) < 0) {
                        alertDialog("To Date is equal or after From Date", context)
                        return@OnDateSetListener
                    }
                }

                textInputEditText.setText("$day-$monthStr-$year")
            }, mYear, mMonth, mDay
        )

        datePickerDialog.datePicker.maxDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }

    fun show24HourTimePicker(context: Context, editText: EditText) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            context,
            { _, selectedHour, selectedMinute ->
                val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                editText.setText(formattedTime)
            },
            hour,
            minute,
            true // true for 24-hour format
        )

        timePickerDialog.show()
    }

    fun convertDateFormat(inputDate: String): String {
        val inputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        try {
            val date: Date = inputFormat.parse(inputDate) ?: return ""
            return outputFormat.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ""
    }

    fun alertDialog(

        description: String,
        activity: Activity
    ) {

        val builder = AlertDialog.Builder(activity)

        builder.setMessage(description)
            .setCancelable(false)
            .setPositiveButton("OK") { dialog, id ->

            }

        val alert = builder.create()
        //Setting the title manually
        alert.show()
    }

    fun getCurrentDate() :String{
        val c = Calendar.getInstance().time
        println("Current time => $c")

        val df = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return df.format(c)
    }

     fun getPathFromUri(uri: Uri, activity: Activity): String {
        val returnUri: Uri = uri!!
        val returnCursor: Cursor =
            activity.getContentResolver().query(returnUri, null, null, null, null)!!

        val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        val sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE)
        returnCursor.moveToFirst()
        val name = returnCursor.getString(nameIndex)
        val size = java.lang.Long.toString(returnCursor.getLong(sizeIndex))
        val file = File(activity.getCacheDir(), name)
        try {
            val inputStream: InputStream = uri?.let {
                activity.getContentResolver().openInputStream(
                    it
                )
            }!!
            val outputStream = FileOutputStream(file)
            var read = 0
            val maxBufferSize = 1 * 1024 * 1024
            val bytesAvailable = inputStream.available()

            //int bufferSize = 1024;
            val bufferSize = Math.min(bytesAvailable, maxBufferSize)
            val buffers = ByteArray(bufferSize)
            while (inputStream.read(buffers).also { read = it } != -1) {
                outputStream.write(buffers, 0, read)
            }
            Log.e("File Size", "Size " + file.length())
            inputStream.close()
            outputStream.close()

            Log.e("File Path", "Path " + file.path)
            Log.e("File Size", "Size " + file.length())
        } catch (e: Exception) {
            Log.e("Exception", e.message!!)
        }
        return file.path

    }
    fun timePicker(context: Context, editText: EditText) {
        // Get Current Time
        // Get Current Time
        val c = Calendar.getInstance()
        var mHour = c[Calendar.HOUR_OF_DAY]
        var mMinute = c[Calendar.MINUTE]

        // Launch Time Picker Dialog
        // Launch Time Picker Dialog
        val timePickerDialog = TimePickerDialog(
            context,
            OnTimeSetListener { view, hourOfDay, minute ->
                val value = if (minute < 10) {
                    "${hourOfDay}:0${minute}"
                } else {
                    "${hourOfDay}:${minute}"
                }
                editText.setText(value)
                // the picked timings by user
/*
                when {
                    hourOfDay == 0 -> {


                    }
                    hourOfDay > 12 -> {
                        val value = if (minute < 10) {
                            "${hourOfDay - 12}:0${minute} pm"
                        } else {
                            "${hourOfDay - 12}:${minute} pm"
                        }
                        editText.setText(value)
                    }
                    hourOfDay == 12 -> {
                        val value =   if (minute < 10) {
                            "${hourOfDay}:0${minute} pm"
                        } else {
                            "${hourOfDay}:${minute} pm"
                        }
                        editText.setText(value)
                    }
                    else -> {
                        val value =  if (minute < 10) {
                            "${hourOfDay}:0${minute} am"
                        } else {
                            "${hourOfDay}:${minute} am"
                        }
                        editText.setText(value)
                    }

                }
*/

            },
            mHour,
            mMinute,
            true
        )
        timePickerDialog.show()
    }

    @Throws(IOException::class)
    fun createTempImageFile(context: Context): File? {
        val timeStamp = SimpleDateFormat(
            "yyyyMMdd_HHmmss",
            Locale.getDefault()
        ).format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = context.externalCacheDir
        return File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",  /* suffix */
            storageDir /* directory */
        )
    }

/*
    fun timePicker(context: Context,editText: EditText) {
        // Get Current Time
        // Get Current Time
        val c = Calendar.getInstance()
        var mHour = c[Calendar.HOUR_OF_DAY]
        var mMinute = c[Calendar.MINUTE]

        // Launch Time Picker Dialog

        // Launch Time Picker Dialog
        val timePickerDialog = TimePickerDialog(
            context,
            OnTimeSetListener { view, hourOfDay, minute ->

                // the picked timings by user
                when {
                    hourOfDay == 0 -> {
                      val value =  if (minute < 10) {
                            "${hourOfDay + 12}:0${minute} am"
                        } else {
                            "${hourOfDay + 12}:${minute} am"
                        }
                        editText.setText(value)
                    }
                    hourOfDay > 12 -> {
                        val value = if (minute < 10) {
                            "${hourOfDay - 12}:0${minute} pm"
                        } else {
                            "${hourOfDay - 12}:${minute} pm"
                        }
                        editText.setText(value)
                    }
                    hourOfDay == 12 -> {
                        val value =   if (minute < 10) {
                            "${hourOfDay}:0${minute} pm"
                        } else {
                            "${hourOfDay}:${minute} pm"
                        }
                        editText.setText(value)
                    }
                    else -> {
                        val value =  if (minute < 10) {
                            "${hourOfDay}:0${minute} am"
                        } else {
                            "${hourOfDay}:${minute} am"
                        }
                        editText.setText(value)
                    }

                }

            },
            mHour,
            mMinute,
            false
        )
        timePickerDialog.show()
    }
*/
}