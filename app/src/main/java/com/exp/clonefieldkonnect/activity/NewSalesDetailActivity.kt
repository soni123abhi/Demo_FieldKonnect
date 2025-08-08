package com.exp.clonefieldkonnect.activity

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.adapter.SalesDetailsAdapter
import com.exp.clonefieldkonnect.connection.APIResultLitener
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.helper.Constant
import com.exp.clonefieldkonnect.helper.DialogClass
import com.exp.clonefieldkonnect.helper.StaticSharedpreference
import com.exp.clonefieldkonnect.model.SalesDetailModel
import com.exp.import.Utilities
import retrofit2.Response

class NewSalesDetailActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var cardBack: CardView
    lateinit var cardtransport: CardView
    lateinit var tvBuyer: TextView
    lateinit var tvBuyeradd: TextView
    lateinit var tvSeller: TextView
    lateinit var tvSelleraddd: TextView
    lateinit var tvOrderNo: TextView
    lateinit var tvAmount: TextView
    lateinit var tvGSTTotal: TextView
    lateinit var tvTotal: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_sales_detail)

        initViews()
    }

    private fun initViews() {

        recyclerView = findViewById(R.id.recyclerView)
        cardBack = findViewById(R.id.cardBack)

        tvBuyer = findViewById(R.id.tvBuyer)
        tvBuyeradd = findViewById(R.id.tvBuyeradd)
        tvSeller = findViewById(R.id.tvSeller)
        tvSelleraddd = findViewById(R.id.tvSelleraddd)
        tvAmount = findViewById(R.id.tvAmount)
        tvGSTTotal = findViewById(R.id.tvGSTTotal)
        tvTotal = findViewById(R.id.tvTotal)
        tvOrderNo = findViewById(R.id.tvOrderNo)
        cardtransport = findViewById(R.id.cardtransport)

        recyclerView.layoutManager = GridLayoutManager(this, 1)


        cardBack.setOnClickListener {
            onBackPressed()
        }

        getsalesDetails()

    }

    private fun getsalesDetails() {
        if (!Utilities.isOnline(this)) {
            return
        }
        var dialog = DialogClass.progressDialog(this)
        val queryParams = HashMap<String, String>()
        queryParams["sales_id"] = intent.getIntExtra("id", 0).toString()
        println("IDDORRRR=="+intent.getIntExtra("id", 0).toString())
        ApiClient.getsalesDetails(StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this@NewSalesDetailActivity).toString(), queryParams, object :
            APIResultLitener<SalesDetailModel> {
            override fun onAPIResult(response: Response<SalesDetailModel>?, errorMessage: String?) {
                dialog.dismiss()
                if (response != null && errorMessage == null) {
                    if (response.code() == 200) {
                        tvGSTTotal.text = response.body()!!.data!!.totalGst
                        tvAmount.text = response.body()!!.data!!.subTotal
                        tvTotal.text = response.body()!!.data!!.grandTotal
                        tvBuyer.text = "Sales No :- "+response.body()!!.data!!.salesNo.toString()
                        tvBuyeradd.text = "Buyer :- "+response.body()!!.data!!.buyerName.toString()
                        tvSeller.text ="Seller :- "+response.body()!!.data!!.sellerName.toString()
                        tvSelleraddd.text ="Invoice No :- "+response.body()!!.data!!.invoiceNo.toString()
                        tvOrderNo.text ="Invoice Date :- "+response.body()!!.data!!.invoiceDate.toString()

                        cardtransport.visibility = View.VISIBLE

                        cardtransport.setOnClickListener {
                            showPopupDialog(response.body()!!.data!!)
                        }


//                        tvBuyeradd.text = "Buyer Add. :- "+response.body()!!.data!!.buyer_address!!.address1.toString()
//                        tvSelleraddd.text = "Seller Add. :- "+response.body()!!.data!!.seller_address!!.address1.toString()
//                        tvOrdercreatedby.text = "Created by :- "+response.body()!!.data!!.createdbyname!!.name.toString()

                        val orderDetails =   response.body()!!.data!!.saledetails
                        val orderAddToCardAdapter = SalesDetailsAdapter(this@NewSalesDetailActivity,orderDetails)
                        recyclerView.adapter = orderAddToCardAdapter
                    }
                } else {
                    println("errrroorro=="+response!!.body()!!.message)
                    println("errrroorro=="+errorMessage)
                    Toast.makeText(this@NewSalesDetailActivity, resources.getString(R.string.poor_connection), Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private lateinit var alertDialog: AlertDialog
    @SuppressLint("MissingInflatedId")
    private fun showPopupDialog(data: SalesDetailModel.Data) {

        val builder = AlertDialog.Builder(this@NewSalesDetailActivity)
        val inflater = this@NewSalesDetailActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.transport_detail_popup_layout, null)
        builder.setCancelable(false)


        val img_close: ImageView = view.findViewById(R.id.img_close)
        val tv_invoice_no: TextView = view.findViewById(R.id.tv_invoice_no)
        val tv_invoice_date: TextView = view.findViewById(R.id.tv_invoice_date)
        val tv_lr_no: TextView = view.findViewById(R.id.tv_lr_no)
        val tv_dis_date: TextView = view.findViewById(R.id.tv_dis_date)
        val tv_trans_detail: TextView = view.findViewById(R.id.tv_trans_detail)

        tv_invoice_no.text = data.invoiceNo
        tv_invoice_date.text = data.invoiceDate
        tv_lr_no.text = data.lr_no
        tv_dis_date.text = data.dispatch_date
        tv_trans_detail.text = data.transport_name


        img_close.setOnClickListener {
            alertDialog.dismiss()
        }

        builder.setView(view)

        alertDialog = builder.create()
        alertDialog.show()
    }
}