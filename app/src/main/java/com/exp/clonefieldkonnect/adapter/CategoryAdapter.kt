package com.exp.clonefieldkonnect.adapter


import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.model.CategoryModel
import com.bumptech.glide.Glide

class CategoryAdapter(var activity: Activity,var categoryArr : ArrayList<CategoryModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var mcontext: Context

     var oldSelectedPos = 0

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        mcontext = parent.context

        val v = LayoutInflater.from(mcontext).inflate(R.layout.adapter_category, parent, false)
        return StatementHandler(v)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

            val statementHandler = holder as StatementHandler


        statementHandler.tvCategoryName.text = categoryArr[position].categoryName

            Glide.with(mcontext).load(
                ApiClient.BASE_IMAGE_URL+categoryArr[position].categoryImage
            ).into(statementHandler.img)

        statementHandler.cardMain.tag = position
        statementHandler.cardMain.setOnClickListener { view ->
            val pos = view.tag as Int

            categoryArr[oldSelectedPos].selected = false
            categoryArr[pos].selected = true
            oldSelectedPos = pos
            notifyDataSetChanged()
//            (activity as ProductActivity).getSubCategoryData( categoryArr[position].id.toString())

        }

        if(categoryArr[position].selected){
          statementHandler.cardMain.setCardBackgroundColor(Color.parseColor("#395299"))
          statementHandler.tvCategoryName.setTextColor(Color.parseColor("#ffffff"))
        }else{
            statementHandler.cardMain.setCardBackgroundColor(Color.parseColor("#F7F7F9"))
            statementHandler.tvCategoryName.setTextColor(Color.parseColor("#000000"))
        }

    }


    private inner class StatementHandler internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvCategoryName: TextView = itemView.findViewById(R.id.tvCategoryName)
        var img: ImageView = itemView.findViewById(R.id.img)
        var linearMain: LinearLayout = itemView.findViewById(R.id.linearMain)
        var cardMain: CardView = itemView.findViewById(R.id.cardMain)

    }

    override fun getItemCount(): Int {
        return categoryArr.size
    }


}
