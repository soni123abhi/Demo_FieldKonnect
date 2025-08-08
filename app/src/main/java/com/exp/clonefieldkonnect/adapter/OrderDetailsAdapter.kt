package com.exp.clonefieldkonnect.adapter


import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.model.OrderDetailsModel
import java.text.DecimalFormat

class OrderDetailsAdapter(
    val context: Activity,
    val orderDetails: List<OrderDetailsModel.Data.Orderdetail>?,
    var buyerType: String?,
    var productCatId: Int?
) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var price_cust = 0.0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.adapter_order_details, parent, false)
        return StatementHandler(v)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val statementHandler = holder as StatementHandler

        if(orderDetails!!.size== (position+1)){
            statementHandler.view.visibility = View.GONE
        }else{
            statementHandler.view.visibility = View.VISIBLE
        }

        if (productCatId!!.equals(1)){
            statementHandler.tvName.text = orderDetails!![position]!!.productName
            statementHandler.tvQuantity.text = orderDetails!![position]!!.quantity.toString()
            statementHandler.tvDetails.text ="Model :-"+ orderDetails[position]!!.model_no.toString()
            statementHandler.tvGSTNew.text = "HP :- "+ orderDetails[position]!!.hp.toString()
            statementHandler.tvPrice.text = "Stage :- "+ orderDetails[position]!!.product_no.toString()
            statementHandler.tvspecification.text = "Phase :- "+ orderDetails[position]!!.phase.toString()
            if (buyerType.equals("Dealer") || buyerType.equals("Distributor")) {
                price_cust = (orderDetails?.get(position)?.quantity.toString().toDouble() * orderDetails?.get(position)?.ebd_amount.toString().toDouble())
                statementHandler.tvAmount.text = price_cust.toString()
                statementHandler.tvRate.text = orderDetails!![position]!!.ebd_amount.toString()
            } else {
                statementHandler.tvRate.text = orderDetails!![position]!!.price.toString()
                statementHandler.tvAmount.text = DecimalFormat("##.#").format(orderDetails?.get(position)?.lineTotal.toString().toFloat()).toString()
            }

        }else if (productCatId!!.equals(2)){
            statementHandler.tvName.text = orderDetails!![position]!!.productName
            statementHandler.tvQuantity.text = orderDetails!![position]!!.quantity.toString()
            statementHandler.tvDetails.text = "Model :-"+orderDetails!![position]!!.model_no.toString()
            statementHandler.tvGSTNew.text = "Brand Name :-"+orderDetails!![position]!!.brand_name.toString()
            if (buyerType.equals("Dealer") || buyerType.equals("Distributor")) {
                price_cust = (orderDetails?.get(position)?.quantity.toString().toDouble() * orderDetails?.get(position)?.ebd_amount.toString().toDouble())
                statementHandler.tvAmount.text = price_cust.toString()
                statementHandler.tvRate.text = orderDetails!![position]!!.ebd_amount.toString()
            } else {
                statementHandler.tvRate.text = orderDetails!![position]!!.price.toString()
                statementHandler.tvAmount.text = DecimalFormat("##.#").format(orderDetails?.get(position)?.lineTotal.toString().toFloat()).toString()
            }
        }else if (productCatId!!.equals(4)){
            statementHandler.tvQuantity.text = orderDetails!![position]!!.quantity.toString()
            statementHandler.tvDetails.text = "Stage :- "+ orderDetails[position]!!.product_no.toString()
            statementHandler.tvGSTNew.text = "kW :- "+ orderDetails[position]!!.part_no.toString()
            statementHandler.tvPrice.text = "HP :- "+ orderDetails[position]!!.hp.toString()
            statementHandler.tvspecification.text = "Suc*Del :- "+ orderDetails[position]!!.specification.toString()
            if (buyerType.equals("Distributor")) {
                price_cust = (orderDetails?.get(position)?.quantity.toString().toDouble() * orderDetails?.get(position)?.price.toString().toDouble())
                statementHandler.tvAmount.text = price_cust.toString()
                statementHandler.tvRate.text = orderDetails!![position]!!.price.toString()
            } else {
                statementHandler.tvRate.text = orderDetails!![position]!!.price.toString()
                statementHandler.tvAmount.text = DecimalFormat("##.#").format(orderDetails?.get(position)?.lineTotal.toString().toFloat()).toString()
            }
            statementHandler.tvName.text = orderDetails!![position]!!.productName
        }else{
            statementHandler.tvQuantity.text = orderDetails!![position]!!.quantity.toString()
            statementHandler.tvDetails.text = "Stage :- "+ orderDetails[position]!!.product_no.toString()
            statementHandler.tvGSTNew.text = "kW :- "+ orderDetails[position]!!.part_no.toString()
            statementHandler.tvPrice.text = "HP :- "+ orderDetails[position]!!.hp.toString()
            statementHandler.tvspecification.text = "Suc*Del :- "+ orderDetails[position]!!.specification.toString()

            if (buyerType.equals("Dealer") || buyerType.equals("Distributor")) {
                price_cust = (orderDetails?.get(position)?.quantity.toString().toDouble() * orderDetails?.get(position)?.ebd_amount.toString().toDouble())
                statementHandler.tvAmount.text = price_cust.toString()
                statementHandler.tvRate.text = orderDetails!![position]!!.ebd_amount.toString()
            } else {
                statementHandler.tvRate.text = orderDetails!![position]!!.price.toString()
                statementHandler.tvAmount.text = DecimalFormat("##.#").format(orderDetails?.get(position)?.lineTotal.toString().toFloat()).toString()
            }
            statementHandler.tvName.text = orderDetails!![position]!!.productName
        }
        /*if (buyerType.equals("Dealer") || buyerType.equals("Distributor")) {
            price_cust = (orderDetails?.get(position)?.quantity.toString().toDouble() * orderDetails?.get(position)?.ebd_amount.toString().toDouble())
            statementHandler.tvAmount.text = price_cust.toString()
            statementHandler.tvRate.text = orderDetails!![position]!!.ebd_amount.toString()
        } else {
            statementHandler.tvRate.text = orderDetails!![position]!!.price.toString()
            statementHandler.tvAmount.text = DecimalFormat("##.#").format(orderDetails?.get(position)?.lineTotal.toString().toFloat()).toString()
        }*/


    }


    override fun getItemCount(): Int {
        return orderDetails!!.size
    }

    private inner class StatementHandler internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvEAN: TextView = itemView.findViewById(R.id.tvEAN)
        var tvGSTNew: TextView = itemView.findViewById(R.id.tvGSTNew)
        var tvName: TextView = itemView.findViewById(R.id.tvName)
        var tvDetails: TextView = itemView.findViewById(R.id.tvDetails)
        var tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
        var tvspecification: TextView = itemView.findViewById(R.id.tvspecification)
        var tvGST: TextView = itemView.findViewById(R.id.tvGST)
        var tvQuantity: TextView = itemView.findViewById(R.id.tvQuantity)
        var tvRate: TextView = itemView.findViewById(R.id.tvRate)
        var tvTax: TextView = itemView.findViewById(R.id.tvTax)
        var tvAmount: TextView = itemView.findViewById(R.id.tvAmount)
        var imgDelete: ImageView = itemView.findViewById(R.id.imgDelete)
        var view: View = itemView.findViewById(R.id.view)
    }
}