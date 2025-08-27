package com.exp.clonefieldkonnect.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.adapter.NotificationAdapter
import com.exp.clonefieldkonnect.connection.APIResultLitener
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.helper.Constant
import com.exp.clonefieldkonnect.helper.DialogClass
import com.exp.clonefieldkonnect.helper.StaticSharedpreference
import com.exp.clonefieldkonnect.model.AttendanceSubmitModel
import com.exp.clonefieldkonnect.model.NotificationModel
import com.exp.import.Utilities
import org.json.JSONObject
import retrofit2.Response

class NotificationActivity : AppCompatActivity(),NotificationAdapter.OnEmailClick {
    lateinit var cardBack_activity: CardView
    lateinit var recyclerView_notification: RecyclerView

    private var lastPosition = -1
    private var isLoading = false
    private var page = 1
    private var pageSize = "30"
    var page_count : String = ""

    var notificationlist: ArrayList<NotificationModel.Data> = ArrayList()
    var notificationlist2: ArrayList<NotificationModel.Data> = ArrayList()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        initViews()

    }

    private fun initViews() {
        cardBack_activity = findViewById(R.id.cardBack)
        recyclerView_notification = findViewById(R.id.recyclerView_notification)


        getnotification(page)

        recyclerView_notification.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && totalItemCount <= firstVisibleItemPosition + visibleItemCount) {
                    page++
                    if (notificationlist.size == 30){
                        getnotification(page)
                        lastPosition = firstVisibleItemPosition
                    }
                }
            }
        })


        cardBack_activity.setOnClickListener {
            handelbackpress()
        }

    }

    private fun getnotification(page: Int) {

        isLoading = true

        if (!Utilities.isOnline(this)) {
            isLoading = false
            return
        }

        var dialog = DialogClass.progressDialog(this)
        val queryParams = HashMap<String, String>()
        queryParams["pageSize"] = pageSize
        queryParams["page"] = page.toString()

        println("queryParams=="+queryParams)
        ApiClient.getnotification(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this).toString(),
            queryParams,
            object : APIResultLitener<NotificationModel> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onAPIResult(response: Response<NotificationModel>?, errorMessage: String?) {
                    dialog.dismiss()
                    page_count = ""
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {
                            notificationlist2.clear()

                            if (page ==1)
                                notificationlist.clear()

                            println("DDDDDDDDD=="+response.body()!!.data!![0].id)
                            println("DDDDDDDDD=="+response.body()!!.data!![0].title)

                            notificationlist.addAll(response.body()!!.data)
                            notificationlist2.addAll(response.body()!!.data)

                            setuprecyclernotificationlist()
                        } else {

                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    this@NotificationActivity, false
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        isLoading = false
                    }
                    else {
                        Toast.makeText(this@NotificationActivity, resources.getString(R.string.poor_connection), Toast.LENGTH_LONG).show()
                    }
                }
            })
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun setuprecyclernotificationlist() {
        val mLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView_notification.layoutManager = mLayoutManager
        val useractivityAdapter = NotificationAdapter(this, notificationlist, this)
        recyclerView_notification.adapter = useractivityAdapter
        recyclerView_notification.scrollToPosition(lastPosition)
        useractivityAdapter.notifyDataSetChanged()
    }

    private fun handelbackpress() {
        startActivity(Intent(this@NotificationActivity,LeadActivity::class.java))
    }

    override fun onClicknotification(id: Int?, model: String?) {
        println("nottiii_iddd=="+id+"<<"+model)
        callreadapi(id,model)
    }

    private fun callreadapi(id: Int?, model: String?) {

        if (!Utilities.isOnline(this@NotificationActivity)) {
            return
        }
        var dialog = DialogClass.progressDialog(this@NotificationActivity)
        val queryParams = java.util.HashMap<String, String>()
        queryParams["id"] = id.toString()

        ApiClient.callreadnotification(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this@NotificationActivity).toString(),
            queryParams,
            object : APIResultLitener<AttendanceSubmitModel> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onAPIResult(
                    response: Response<AttendanceSubmitModel>?,
                    errorMessage: String?
                ) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {
                            Toast.makeText(this@NotificationActivity, response.body()!!.message, Toast.LENGTH_LONG).show()
                            page = 1
                            if (model.equals("opportunity")){
                                startActivity(Intent(this@NotificationActivity,OpportunityViewActivity::class.java))
                            }else if (model.equals("lead")){
                                startActivity(Intent(this@NotificationActivity,LeadActivity::class.java))
                            }
                            else if (model.equals("task")){
                                var intent = Intent(this@NotificationActivity, TaskActivity::class.java)
                                intent.putExtra("notification_tag",model)
                                startActivity(intent)
                            }
                            else if (model.equals("task_management")){
                                var intent = Intent(this@NotificationActivity, TaskActivity::class.java)
                                intent.putExtra("notification_tag",model)
                                startActivity(intent)
                            }else{
                                startActivity(Intent(this@NotificationActivity,LeadActivity::class.java))
                            }
                        } else {
                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    this@NotificationActivity, false
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            })
    }
}