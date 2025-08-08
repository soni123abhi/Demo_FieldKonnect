package com.exp.clonefieldkonnect.activity

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.exp.import.Utilities
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.connection.APIResultLitener
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.helper.Constant
import com.exp.clonefieldkonnect.helper.DialogClass
import com.exp.clonefieldkonnect.helper.StaticSharedpreference
import com.exp.clonefieldkonnect.services.MyJobScheduler
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Response
import java.net.NetworkInterface
import java.util.UUID

class LoginActivity : AppCompatActivity(),View.OnClickListener {
    lateinit var cardLogin: CardView
    lateinit var edtPassword: TextInputEditText
    var tv:TextView?= null
    lateinit var edtUserName: TextInputEditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
      //  scheduleJob()
        initViews()
        //setUpWorker()
    }

    private fun initViews() {
        cardLogin = findViewById(R.id.cardLogin)
        edtPassword = findViewById(R.id.edtPassword)
        edtUserName = findViewById(R.id.edtUserName)

        cardLogin.setOnClickListener(this)
    }

    override fun onClick(p0: View) {
        when(p0.id){
             R.id.cardLogin->{
                 if(edtUserName.text.toString()==""){
                     Toast.makeText(this@LoginActivity,"Please enter username",Toast.LENGTH_LONG).show()
                 }else  if(edtPassword.text.toString()==""){
                     Toast.makeText(this@LoginActivity,"Please enter password",Toast.LENGTH_LONG).show()
                 }else{
                     val manufacturer = Build.MANUFACTURER
                     val model = Build.MODEL
                     val deviceName = if (model.startsWith(manufacturer)) {
                         model.capitalize()
                     } else {
                         "${manufacturer.capitalize()} $model"
                     }
                     var uniqueIddd  = getUniqueDeviceId(this@LoginActivity)
                     println("uniqueIddd=="+uniqueIddd)

                     val packageInfo = packageManager.getPackageInfo(packageName, 0)
                     val currentVersion = packageInfo.versionName
                     login(deviceName,currentVersion,uniqueIddd)
                 }
            }
        }
    }

    fun getUniqueDeviceId(context: LoginActivity): String {
        val macAddress = getMacAddress()
        val androidId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        val uniqueId = UUID.nameUUIDFromBytes("$macAddress-$androidId".toByteArray()).toString()
        return uniqueId
    }

    private fun getMacAddress(): String {
        try {
            val all = NetworkInterface.getNetworkInterfaces()
            while (all.hasMoreElements()) {
                val networkInterface = all.nextElement()
                val mac = networkInterface.hardwareAddress ?: continue
                return mac.joinToString(":") { "%02X".format(it) }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return "02:00:00:00:00:00"
    }

    private fun login(deviceName: String, currentVersion: String, uniqueIddd: String) {

        if (!Utilities.isOnline(this)) {
            return
        }
        var dialog = DialogClass.progressDialog(this)

        val queryParams = HashMap<String, String>()
        queryParams["username"] = edtUserName.text.toString()
        queryParams["password"] = edtPassword.text.toString()
        queryParams["device_name"] = deviceName
        queryParams["app_version"] = currentVersion
        queryParams["device_token"] = StaticSharedpreference.getInfoUnclear("fcmToken",this@LoginActivity).toString()
        queryParams["device_type"] = "android"
        queryParams["unique_id"] = uniqueIddd
        println("queryParams="+queryParams)
        ApiClient.login(queryParams, object : APIResultLitener<JsonObject> {
            override fun onAPIResult(response: Response<JsonObject>?, errorMessage: String?) {
                dialog.dismiss()
                if (response != null && errorMessage == null) {

                    if (response.code() == 200) {
                        val json = response.body()!!.get("userinfo").asJsonObject
                        StaticSharedpreference.saveInfo(Constant.USERID,json.get("id").asInt.toString(),this@LoginActivity)
                        StaticSharedpreference.saveInfo(Constant.EMAIL,json.get("email").asString.toString(),this@LoginActivity)
                        StaticSharedpreference.saveInfo(Constant.MOBILE,json.get("mobile").asString.toString(),this@LoginActivity)
                        StaticSharedpreference.saveInfo(Constant.PAYROLL_ID,json.get("payroll_id").asInt.toString(),this@LoginActivity)
                        StaticSharedpreference.saveInfo(Constant.FIRM_NAME,json.get("name").asString.toString(),this@LoginActivity)
                        StaticSharedpreference.saveInfo(Constant.DIVISION_ID,json.get("dividion_id").asInt.toString(),this@LoginActivity)
                        StaticSharedpreference.saveInfo(Constant.USERID,json.get("id").asInt.toString(),this@LoginActivity)
                        StaticSharedpreference.saveInfo(Constant.ACCESS_TOKEN,"Bearer "+json.get("access_token").asString.toString(),this@LoginActivity)
                        StaticSharedpreference.saveInfo(Constant.PROFILE_IMAGE,json.get("profile_image").asString.toString(),this@LoginActivity)
                        StaticSharedpreference.saveInfo(Constant.ROLL_ID,json.get("roles").asJsonArray.toString(),this@LoginActivity)
//                        StaticSharedpreference.saveBoolean(Constant.todayBeatSchedule, false, this@LoginActivity)
//                        StaticSharedpreference.saveBoolean(Constant.beatUser, false, this@LoginActivity)

                        // StaticSharedpreference.saveInfo(Constant.ACCESS_TOKEN,"Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIxIiwianRpIjoiMGE5NDgxNzY0M2Y3OTRlYjY4NTkyM2JlOGI3MTEwMjNkNjA0MjA3ZWMzODI4NTNlMmIyZDVlMDliYzU2ZjQwYzE1MTVjNTBjN2RiZmI4ZTEiLCJpYXQiOjE1OTAxNTMwNTYsIm5iZiI6MTU5MDE1MzA1NiwiZXhwIjoxNjIxNjg5MDU2LCJzdWIiOiIxIiwic2NvcGVzIjpbXX0.j1ajxMunZS_7KvHYaiIcE35Jvb1GjBhVcFaVFB-KSwcjPx1UaLw5tM6PIB4mE4BW2MujBKN5xKcbidLqupyI1Qk9tfKBoLwgGW9BwEACfCzMMJADbHkJaRJFFe0QqQVmjnxloM8UlSkihF0BCwweEby4yC9YbHFZyq6Bp5qzHYnmP3W8fU2LT89sG7CPdF7V9t_usAI_35qPeCRgqoTqr9IUQI6ewYJhXKczjHsxznsST6QATPirerlNhkBJ10eAaNsLnL8TEXJbNE6ptqj1xeWOyoeBJd7daP__WFml0kXcROgeegcM9fio-ZN0ccJdDTDZ4kO3Lmj-5J_GKDlnqkwHN2YokO6bcldLANwfuE49NVa3yaSHvQgg4PIDNW05I08k7GxKRRvm5q3acHd4Ysg2fhpl18UzA51HmvLhRcO4G22xgJdat2Fpr3Ww0ZTrpsnHBZEyp7QNuztjt1iYjrz8YqmqXCkGfNAgpziQKIJQSOA9usmobShnJs48KHJeCNMGgc6t0Aq7iN4ZtTfAs9oy10i4bP3GZOF_s8a7elalE5uqt927erd7mMslkKy-i5xfy5arJrfXaGGa0ulWaSfpU6elzbM6ufnBRX7zqa2g07m4s2HSkSEIzzVYRx-4SURJmSjGA2sXJpBHAtHb6HYo-1cwfdS6lsuSGK-YFbc",this@LoginActivity)
//                        println("rolesrolesrolesroles"+StaticSharedpreference.getInfo(Constant.ROLL_ID,this@LoginActivity))
                        startActivity(Intent(this@LoginActivity,MainActivity::class.java))
                        finishAffinity()
                    } else {
                        val jsonObject: JSONObject
                        try {
                            jsonObject = JSONObject(response.errorBody()!!.string())
                            DialogClass.alertDialog(
                                jsonObject.getString("status"),
                                jsonObject.getString("message"),
                                this@LoginActivity,
                                false
                            )
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                } else {
                    //  dialog.dismiss()
                    Toast.makeText(this@LoginActivity, resources.getString(R.string.poor_connection), Toast.LENGTH_LONG).show()
                }
            }
        }, this@LoginActivity)
    }
    val JOB_ID = 1001;
    val REFRESH_INTERVAL  : Long = 5 * 1000 // 15 minutes
    //    val REFRESH_INTERVAL  : Long = 1 * 60 * 1000 // 15 minutes
    private fun scheduleJob() {
        val jobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
        val componentName = ComponentName(this, MyJobScheduler::class.java)
        val jobInfo = JobInfo.Builder(JOB_ID, componentName)
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
            .setMinimumLatency(REFRESH_INTERVAL)
            .setPersisted(true)
            .setRequiresCharging(false)
            .build()
        jobScheduler.schedule(jobInfo)
    }
}