package com.exp.clonefieldkonnect.activity

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exp.import.Utilities
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.connection.APIResultLitener
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.helper.*
import com.exp.clonefieldkonnect.model.OrderListModel
import retrofit2.Response

class OrderListActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var cardBack: CardView
    private lateinit var tvNoDta: TextView
    private var pageSize = "10"
    private var index = 1
    var orderListArr: ArrayList<OrderListModel.Datum?> = arrayListOf();
    lateinit var scrollListener: RecyclerViewLoadMoreScroll
    lateinit var mLayoutManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_list)

        initViews()
    }

    private fun initViews() {
        tvNoDta = findViewById(R.id.tvNoDta)
        recyclerView = findViewById(R.id.recyclerView)
        cardBack = findViewById(R.id.cardBack)

        cardBack.setOnClickListener {
            onBackPressed()
        }

//        setAdapter()
        //setRVLayoutManager()
        setRVScrollListener()

        getOrderList(true)

    }

//    private fun setAdapter() {
//        statementAdapter = OrderListAdapter(orderListArr)
//        statementAdapter.notifyDataSetChanged()
//        recyclerView.adapter = statementAdapter
//    }

    private fun setRVScrollListener() {
        mLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = mLayoutManager
        recyclerView.setHasFixedSize(true)
        scrollListener = RecyclerViewLoadMoreScroll(mLayoutManager as LinearLayoutManager)
        scrollListener.setOnLoadMoreListener(object :
            OnLoadMoreListener {
            override fun onLoadMore() {
                index++
                getOrderList(false)
            }
        })
        recyclerView.addOnScrollListener(scrollListener)
    }

    lateinit var dialog: Dialog
    private fun getOrderList(isProgress: Boolean) {

        if (!Utilities.isOnline(this)) {
            return
        }
        if (isProgress)
            dialog = DialogClass.progressDialog(this)

        val queryParams = HashMap<String, String>()
        queryParams["page"] = index.toString()
        queryParams["pageSize"] = pageSize
        ApiClient.getOrderList(
            StaticSharedpreference.getInfo(
                Constant.ACCESS_TOKEN,
                this
            ).toString(), queryParams, object : APIResultLitener<OrderListModel> {
                override fun onAPIResult(
                    response: Response<OrderListModel>?,
                    errorMessage: String?
                ) {

                    if (isProgress)
                        dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {

                            tvNoDta.visibility = View.GONE
                            orderListArr = response.body()!!.data

                            if(index==1&&orderListArr.size==0)
                                tvNoDta.visibility = View.VISIBLE

                            if (orderListArr.size == pageSize.toInt()) {
                                scrollListener.setLoaded()
//                                stateme ntAdapter.addLoadingView()

                            } else {
                                index = 1
                                scrollListener.setNotLoaded()
                            }
//                            statementAdapter.removeLoadingView()
//                            statementAdapter.addData(orderListArr)

                        }
                    } else {
                        Toast.makeText(
                            this@OrderListActivity,
                            resources.getString(R.string.poor_connection),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }, this
        )
    }


}