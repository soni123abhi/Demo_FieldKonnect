package com.exp.clonefieldkonnect.adapter


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.activity.ProductActivity.Companion.productArr
import com.exp.clonefieldkonnect.activity.ProductDetailsActivity
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.helper.Constant
import com.exp.clonefieldkonnect.model.ProductNewModel
import com.bumptech.glide.Glide

class ProductAdapter(var checkin: String) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var mcontext: Context

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    fun addData(dataViews: List<ProductNewModel?>) {
        productArr.addAll(dataViews)
        notifyDataSetChanged()
    }

    fun addLoadingView() {
        //add loading item
        Handler().post {
            productArr.add(null)
            notifyItemInserted(productArr.size - 1)
        }
    }

    fun removeLoadingView() {
        //Remove loading item
        if (productArr.size != 0) {
            productArr.removeAt(productArr.size - 1)
            notifyItemRemoved(productArr.size)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        mcontext = parent.context
        return if (viewType == Constant.VIEW_TYPE_ITEM) {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.adapter_product, parent, false)
            StatementHandler(view)
        } else {
            val view =
                LayoutInflater.from(mcontext).inflate(R.layout.progress_loading, parent, false)

            LoadingViewHolder(view)
        }
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder.itemViewType == Constant.VIEW_TYPE_ITEM) {
            val statementHandler = holder as StatementHandler

            try {
                statementHandler.tvProduct.text = ""+productArr[position]!!.productName.toString()
            } catch (e: Exception) {
                statementHandler.tvProduct.visibility = View.GONE
            }


            if(productArr[position]!!.part_no.toString()!=""&&productArr[position]!!.part_no.toString()!="null")
            statementHandler.tvOEPart.text = "Kw :- "+productArr[position]!!.part_no.toString()

            if(productArr[position]!!.product_no.toString()!=""&&productArr[position]!!.product_no.toString()!="null")
            statementHandler.tvGG.text ="Product stage :- "+ productArr[position]!!.product_no.toString()
            println("abhiii="+productArr[position]!!.product_ebd_amount.toString())

            if(productArr[position]!!.hp.toString()!=""&&productArr[position]!!.hp.toString()!="null")
            statementHandler.tvhp.text = "Hp :- "+productArr[position]!!.hp.toString()

            if(productArr[position]!!.specification.toString()!=""&&productArr[position]!!.specification.toString()!="null")
            statementHandler.tvSUC.text = "Suc*Del :- "+productArr[position]!!.specification.toString()

            Glide.with(mcontext).load(
                ApiClient.BASE_IMAGE_URL+productArr[position]!!.productImage
            ).into(statementHandler.img)

            statementHandler.relativeMain.tag = position

            statementHandler.relativeMain.setOnClickListener { view ->

                val pos= view.tag as Int
                var intent = Intent(mcontext,ProductDetailsActivity::class.java)
                intent.putExtra("id",productArr[pos]!!.id)
                intent.putExtra("checkin", checkin)
                intent.putExtra("checkin", checkin)
                mcontext.startActivity(intent)
            }

        }
    }


    private inner class StatementHandler internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvProduct: TextView = itemView.findViewById(R.id.tvProduct)
        var tvGG: TextView = itemView.findViewById(R.id.tvGG)
        var tvOEPart: TextView = itemView.findViewById(R.id.tvOEPart)
        var tvhp: TextView = itemView.findViewById(R.id.tvhp)
        var tvSUC: TextView = itemView.findViewById(R.id.tvSUC)
        var relativeMain: LinearLayout = itemView.findViewById(R.id.relativeMain)

        var img: ImageView = itemView.findViewById(R.id.img)

    }

    override fun getItemCount(): Int {
        return productArr.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (productArr[position] == null) {
            Constant.VIEW_TYPE_LOADING
        } else {
            Constant.VIEW_TYPE_ITEM
        }
    }
}