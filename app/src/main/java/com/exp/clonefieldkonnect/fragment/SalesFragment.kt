package com.exp.clonefieldkonnect.fragment

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exp.import.Utilities
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.activity.MainActivity
import com.exp.clonefieldkonnect.adapter.SalesAdapter
import com.exp.clonefieldkonnect.connection.APIResultLitener
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.helper.*
import com.exp.clonefieldkonnect.model.SalesModel
import retrofit2.Response

class SalesFragment : Fragment() {
    lateinit var activityLocal: Activity

    private lateinit var rootView: View
    private lateinit var recyclerView: RecyclerView
    private lateinit var tvNoDta: TextView
    private lateinit var cardBurger: CardView
    private var pageSize = "10"
    private var index = 1
    var statementArr: ArrayList<SalesModel.Datum?> = arrayListOf();
    lateinit var statementAdapter: SalesAdapter
    lateinit var scrollListener: RecyclerViewLoadMoreScroll
    lateinit var mLayoutManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_sales, container, false)
        activityLocal = activity as MainActivity

        initViews()
        return rootView

    }

    private fun initViews() {
        recyclerView = rootView.findViewById(R.id.recyclerView)
        cardBurger = rootView.findViewById(R.id.cardBurger)
        tvNoDta = rootView.findViewById(R.id.tvNoDta)

        cardBurger.setOnClickListener {
            MainActivity.drawerLayout.openDrawer(GravityCompat.START)
        }

        setAdapter()
        //setRVLayoutManager()
        setRVScrollListener()

        getSales(true)

    }

    private fun setAdapter() {
        statementAdapter = SalesAdapter(statementArr)
        statementAdapter.notifyDataSetChanged()
        recyclerView.adapter = statementAdapter
    }

    private fun setRVScrollListener() {
        mLayoutManager = LinearLayoutManager(activityLocal)
        recyclerView.layoutManager = mLayoutManager
        recyclerView.setHasFixedSize(true)
        scrollListener = RecyclerViewLoadMoreScroll(mLayoutManager as LinearLayoutManager)
        scrollListener.setOnLoadMoreListener(object :
            OnLoadMoreListener {
            override fun onLoadMore() {
                index++
                getSales(false)
            }
        })
        recyclerView.addOnScrollListener(scrollListener)
    }

    lateinit var dialog: Dialog
    private fun getSales(isProgress: Boolean) {

        if (!Utilities.isOnline(activityLocal)) {
            return
        }
        if (isProgress)
            dialog = DialogClass.progressDialog(activityLocal)

        val queryParams = HashMap<String, String>()
        queryParams["page"] = index.toString()
        queryParams["pageSize"] = pageSize
        ApiClient.getSales(
            StaticSharedpreference.getInfo(
                Constant.ACCESS_TOKEN,
                activityLocal
            ).toString(), queryParams, object : APIResultLitener<SalesModel> {
                override fun onAPIResult(
                    response: Response<SalesModel>?,
                    errorMessage: String?
                ) {

                    if (isProgress)
                        dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {

                            tvNoDta.visibility = View.GONE
                            statementArr = response.body()!!.data

                            if(index==1&&statementArr.size==0)
                                tvNoDta.visibility = View.VISIBLE

                            if (statementArr.size == pageSize.toInt()) {
                                scrollListener.setLoaded()
                                statementAdapter.addLoadingView()

                            } else {
                                index = 1
                                scrollListener.setNotLoaded()
                            }
                            statementAdapter.removeLoadingView()
                            statementAdapter.addData(statementArr)

                        }
                    } else {
                        Toast.makeText(
                            activityLocal,
                            resources.getString(R.string.poor_connection),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }, activityLocal
        )
    }


}