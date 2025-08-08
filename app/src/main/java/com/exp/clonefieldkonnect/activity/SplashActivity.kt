package com.exp.clonefieldkonnect.activity

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.connection.APIResultLitener
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.helper.Constant
import com.exp.clonefieldkonnect.helper.DialogClass
import com.exp.clonefieldkonnect.helper.MyApplication
import com.exp.clonefieldkonnect.helper.SecurityValidator
import com.exp.clonefieldkonnect.helper.StaticSharedpreference
import com.exp.clonefieldkonnect.model.*
import com.exp.import.Utilities
import org.json.JSONObject
import retrofit2.Response
import java.util.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        /*SecurityValidator.isFakeLocationDetected(this) { isFake ->
            if (isFake) {
                Utilities.showExitDialog(
                    context = this,
                    message = "Fake location detected. Please disable mock location apps."
                )
            } else {
                Log.d("SecurityValidator", "Location is clean (not fake).")
            }
        }
        if (Utilities.isDeveloperOptionsEnabled(this)) {
            Utilities.showExitDialog(this,
                "Developer options are enabled on your device. Please disable them to continue using this app.")
        }else if (Utilities.isVpnActive()) {
            Utilities.showExitDialog(
                this,
                message = "VPN connection detected. Please disable VPN to use the app."
            )
        }else{*/
            Handler().postDelayed({
                checkForUpdates()
            },  1000)
//        }

    }

    fun checkForUpdates() {
        val packageInfo = packageManager.getPackageInfo(packageName, 0)
        val currentVersion = packageInfo.versionName


        println("currentVersion="+currentVersion)

        ApiClient.getversion(
            object : APIResultLitener<VersionModel> {
                override fun onAPIResult(
                    response: Response<VersionModel>?,
                    errorMessage: String?
                ) {
                    if (response != null && errorMessage == null) {
                        if (response.code() == 200) {
                            println("appVersionappVersion="+response!!.body()!!.data!!.appVersion)
                            var latestVersion = response.body()!!.data!!.appVersion
                            if (latestVersion != null && latestVersion != currentVersion) {
                                showUpdateDialog(latestVersion)
                            } else {
                                navigateToAppropriateActivity()
                            }
                        }
                    } else {
                        Toast.makeText(this@SplashActivity, resources.getString(R.string.poor_connection), Toast.LENGTH_LONG).show()
                    }
                }
            }
        )
    }

    private fun navigateToAppropriateActivity() {
        if(StaticSharedpreference.getInfo(Constant.USERID,this@SplashActivity)=="") {
            StaticSharedpreference.saveInfo(Constant.TabPosition, "0",this).toString()
            val intent = Intent(this@SplashActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        } else{
            getUserSataus()
        }
    }

    private fun getUserSataus() {
        if (!Utilities.isOnline(this@SplashActivity)) {
            return
        }
        ApiClient.getUserSataus(StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this@SplashActivity).toString(),
            object : APIResultLitener<UserActiveModel> {
                override fun onAPIResult(response: Response<UserActiveModel>?, errorMessage: String?) {
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {
                            println("DATAA=="+response.body()!!.userStatus)
                            if (response.body()!!.status== "success"){
                                if (response.body()!!.userStatus == "Y"){
                                    StaticSharedpreference.saveInfo(Constant.TabPosition, "0",this@SplashActivity).toString()
                                    val intent = Intent(this@SplashActivity, MainActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }else{
                                    calllogoutapi()
                                    StaticSharedpreference.deleteSharedPreference(this@SplashActivity)
                                    startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                                    finishAffinity()
                                }
                            }
                        } else {

                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                if (response.code() == 401){
                                    Toast.makeText(this@SplashActivity,jsonObject.getString("message"), Toast.LENGTH_LONG).show()
                                    StaticSharedpreference.deleteSharedPreference(this@SplashActivity)
                                    startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                                    this@SplashActivity.finishAffinity()
                                    println("Erroorr=="+jsonObject.getString("message"))
                                }else{
                                    DialogClass.alertDialog(
                                        jsonObject.getString("status"),
                                        jsonObject.getString("message"),
                                        this@SplashActivity, false
                                    )
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }else{
                        StaticSharedpreference.saveInfo(Constant.TabPosition, "0",this@SplashActivity).toString()
                        val intent = Intent(this@SplashActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            })
    }

    private fun calllogoutapi() {
        if (!Utilities.isOnline(this)) {
            return
        }
        var dialog = DialogClass.progressDialog(this)
        val queryParams = HashMap<String, String>()
        ApiClient.getlogout(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this).toString(), queryParams,
            object : APIResultLitener<AttendanceSubmitModel> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onAPIResult(response: Response<AttendanceSubmitModel>?, errorMessage: String?) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {

                            Toast.makeText(this@SplashActivity,response.body()!!.message, Toast.LENGTH_LONG).show()

                        } else {

                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    this@SplashActivity, false
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                    else {
                        Toast.makeText(this@SplashActivity,"No Record Found", Toast.LENGTH_LONG).show()
                    }
                }
            })
    }


    fun showUpdateDialog(latestVersion: String) {
        AlertDialog.Builder(this)
            .setTitle("Update Available")
            .setMessage("Please update your app to the latest version ($latestVersion).")
            .setPositiveButton("Update") { _, _ ->
                // Redirect the user to the Google Play Store to update the app
                val appPackageName = packageName
                try {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
                } catch (e: ActivityNotFoundException) {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
                }
            }
            .setCancelable(false)
            .show()
    }
}