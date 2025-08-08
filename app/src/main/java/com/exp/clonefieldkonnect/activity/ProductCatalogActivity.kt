package com.exp.clonefieldkonnect.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.adapter.ProductCatalogAdapter
import com.exp.clonefieldkonnect.connection.APIResultLitener
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.model.VersionModel
import retrofit2.Response

class ProductCatalogActivity : AppCompatActivity() {

    lateinit var cardBack_pro : CardView
    lateinit var recyclerView_pro : RecyclerView
    var productcataloglist :ArrayList<VersionModel.Media> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_catalog)
        initViews()
    }

    private fun initViews() {
        recyclerView_pro = findViewById(R.id.recyclerView_pro)
        cardBack_pro = findViewById(R.id.cardBack_pro)

        getproductcatalog()

        cardBack_pro.setOnClickListener {
            handleBackPressed()
        }
    }

    private fun getproductcatalog() {
        ApiClient.getversion(
            object : APIResultLitener<VersionModel> {
                override fun onAPIResult(
                    response: Response<VersionModel>?,
                    errorMessage: String?
                ) {
                    if (response != null && errorMessage == null) {
                        if (response.code() == 200) {
                            productcataloglist.clear()
                            productcataloglist.addAll(response.body()!!.data!!.media)
                            println("productcataloglistproductcataloglist=="+productcataloglist.size)
                            setuprecyclerview()
                        }
                    } else {
                        Toast.makeText(this@ProductCatalogActivity, resources.getString(R.string.poor_connection), Toast.LENGTH_LONG).show()
                    }
                }
            }
        )

    }

    private fun setuprecyclerview() {
        var mLayoutManager = LinearLayoutManager(this@ProductCatalogActivity)
        recyclerView_pro.layoutManager = mLayoutManager
        val taskAdapter = ProductCatalogAdapter(this,productcataloglist)
        recyclerView_pro.adapter = taskAdapter
    }

    private fun handleBackPressed() {
        startActivity(Intent(this@ProductCatalogActivity, MainActivity::class.java))
    }
}