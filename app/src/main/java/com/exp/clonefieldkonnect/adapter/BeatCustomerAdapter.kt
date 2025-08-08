package com.exp.clonefieldkonnect.adapter


import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Handler
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.activity.CounterDashboardActivity
import com.exp.clonefieldkonnect.helper.Constant
import com.exp.clonefieldkonnect.helper.StaticSharedpreference
import com.exp.clonefieldkonnect.model.BeatCustomerModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.adapter_beat_customer.view.*
import kotlinx.android.synthetic.main.adapter_beat_customer.view.tvName


class BeatCustomerAdapter(
    val arr: ArrayList<BeatCustomerModel?>,
    val isLead: Boolean,
    var isToday: Boolean,
    var beatScheduleId: String
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var mcontext: Context

    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    fun addData(dataViews: List<BeatCustomerModel?>) {
        this.arr.addAll(dataViews)
        notifyDataSetChanged()
    }

    fun clear() {
        this.arr.clear()
        notifyDataSetChanged()
    }

    fun addLoadingView() {
        //add loading item
        Handler().post {
            arr.add(null)
            notifyItemInserted(arr.size - 1)
        }
    }

    fun removeLoadingView() {
        //Remove loading item
        if (arr.size != 0) {
            arr.removeAt(arr.size - 1)
            notifyItemRemoved(arr.size)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        mcontext = parent.context

        return if (viewType == Constant.VIEW_TYPE_ITEM) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_beat_customer, parent, false)
            ItemViewHolder(view)
        } else {
            val view =
                LayoutInflater.from(mcontext).inflate(R.layout.progress_loading, parent, false)
            LoadingViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder.itemViewType == Constant.VIEW_TYPE_ITEM) {

            holder.itemView.tvName.text = arr[position]!!.customerName
            holder.itemView.tvAddress.text = arr[position]!!.address
            holder.itemView.tvGrade.text = arr[position]!!.grade
            holder.itemView.tvStatus.text = arr[position]!!.visitStatus

            println("lattt=="+arr[position]!!.latitude+"<<"+arr[position]!!.longitude)

            if (arr[position]!!.latitude.equals("") && arr[position]!!.longitude.equals("")) {
//                holder.itemView.txt_loc.setTextColor(Color.parseColor("#D82323"))
                holder.itemView.cardloc.setBackgroundResource(R.color.bg_loc_red)
            }else{
//                holder.itemView.txt_loc.setTextColor(Color.parseColor("#008000"))
                holder.itemView.cardloc.setBackgroundResource(R.color.bg_loc_green)
            }
             val drawableResource = if (arr[position]!!.latitude.equals("") && arr[position]!!.longitude.equals("")) {
                 R.drawable.ic_location_customer
             } else {
                 R.drawable.ic_location_customer2
             }

             holder.itemView.txt_loc.setCompoundDrawablesWithIntrinsicBounds(drawableResource, 0, 0, 0)


            if (arr[position]!!.grade != null && arr[position]!!.grade != "") {
                holder.itemView.cardGrade.visibility = View.VISIBLE
                if (arr[position]!!.grade == "Grade A") {
                    holder.itemView.cardGrade.setCardBackgroundColor(Color.parseColor("#FFF4B9"))
                    holder.itemView.tvGrade.setTextColor(Color.parseColor("#CFA200"))
                } else if (arr[position]!!.grade == "Grade B") {
                    holder.itemView.cardGrade.setCardBackgroundColor(Color.parseColor("#E3E3E3"))
                    holder.itemView.tvGrade.setTextColor(Color.parseColor("#7E7E7E"))
                } else if (arr[position]!!.grade == "Grade C") {
                    holder.itemView.cardGrade.setCardBackgroundColor(Color.parseColor("#FFC398"))
                    holder.itemView.tvGrade.setTextColor(Color.parseColor("#B27244"))
                } else if (arr[position]!!.grade == "Grade D") {
                    holder.itemView.cardGrade.setCardBackgroundColor(Color.parseColor("#B8B8B8"))
                    holder.itemView.tvGrade.setTextColor(Color.parseColor("#494949"))
                }
            } else {
                holder.itemView.cardGrade.visibility = View.GONE
            }

            if (arr[position]!!.visitStatus != null && arr[position]!!.visitStatus != "") {
                holder.itemView.cardStatus.visibility = View.VISIBLE
                if (arr[position]!!.visitStatus == "Hot") {
                    holder.itemView.cardStatus.setCardBackgroundColor(Color.parseColor("#FFF2F2"))
                    holder.itemView.tvStatus.setTextColor(Color.parseColor("#FF0000"))
                } else if (arr[position]!!.visitStatus == "Cold") {
                    holder.itemView.cardStatus.setCardBackgroundColor(Color.parseColor("#F2F5FF"))
                    holder.itemView.tvStatus.setTextColor(Color.parseColor("#0057FF"))
                } else if (arr[position]!!.visitStatus == "Warm") {
                    holder.itemView.cardStatus.setCardBackgroundColor(Color.parseColor("#FFFBD6"))
                    holder.itemView.tvStatus.setTextColor(Color.parseColor("#EAC70F"))
                } else if (arr[position]!!.visitStatus == "Existing") {
                    holder.itemView.cardStatus.setCardBackgroundColor(Color.parseColor("#FFFBD6"))
                    holder.itemView.tvStatus.setTextColor(Color.parseColor("#EAC70F"))
                }
            } else {
                holder.itemView.cardStatus.visibility = View.GONE
            }

            println("visibility="+arr[position]!!.profile_image)
            Glide.with(mcontext).load(
               arr[position]!!.profile_image
            ).into(holder.itemView.img)

            holder.itemView.relativeMain.tag = position
            holder.itemView.relativeMain.setOnClickListener { view ->
                val pos = view.tag as Int

                StaticSharedpreference.saveInfo(
                    Constant.CHECKIN_CUST_ID,
                    arr[pos]!!.customerId.toString(),
                    mcontext
                )

                val intent = Intent(mcontext, CounterDashboardActivity::class.java)
                intent.putExtra("image", arr[pos]!!.profile_image.toString())
                intent.putExtra("customerName", arr[pos]!!.customerName.toString())
                intent.putExtra("isLead", isLead)
                intent.putExtra("checkInId", "")
                intent.putExtra("outstanding", arr[pos]!!.outstanding.toString())
                intent.putExtra("customerAddress", arr[pos]!!.address.toString())
                intent.putExtra("isToday", isToday)
                intent.putExtra("beat_schedule_id", beatScheduleId)
                mcontext.startActivity(intent)
            }
            holder.itemView.linearLocation.setTag(position)
            holder.itemView.linearLocation.setOnClickListener {
                val pos = it.tag as Int
                if (!TextUtils.isEmpty(arr[pos]!!.latitude) && !TextUtils.isEmpty(arr[pos]!!.longitude)) {
                    var lat = arr[pos]!!.latitude
                    var long = arr[pos]!!.longitude
                    var label = arr[pos]!!.address
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(
                            "geo:<" + lat.toString() + ">,<" + long.toString()
                                    + ">?q=<" + lat.toString() + ">,<" + long.toString()
                                    + ">(" + label.toString() + ")"
                        )
                    )
                    mcontext.startActivity(intent)
                }
            }
            holder.itemView.linearCall.setTag(position)
            holder.itemView.linearCall.setOnClickListener {
                val pos = it.tag as Int
                val intent = Intent(Intent.ACTION_DIAL)
                var mobile = arr[pos]!!.customerMobile
                intent.data = Uri.parse("tel:" + mobile)
                mcontext.startActivity(intent)
            }
            holder.itemView.linearWhatsApp.setTag(position)
            holder.itemView.linearWhatsApp.setOnClickListener {
                val pos = it.tag as Int
                openWhatsApp(arr[pos]!!.customerMobile!!, mcontext)
            }
            holder.itemView.linearMail.setTag(position)
            holder.itemView.linearMail.setOnClickListener {
                val pos = it.tag as Int
                if (!TextUtils.isEmpty(arr[pos]!!.email)) {
                    val to = arr[pos]!!.email
                    val intent = Intent(Intent.ACTION_SENDTO)
                    intent.putExtra(Intent.EXTRA_EMAIL, arrayOf<String>(to!!))
                    intent.putExtra(Intent.EXTRA_SUBJECT, "")
                    intent.putExtra(Intent.EXTRA_TEXT, "")

//need this to prompts email client only

//need this to prompts email client only
                    intent.type = "message/rfc822"

                    mcontext.startActivity(Intent.createChooser(intent, "Choose an Email client :"))
                }
            }

        }
    }

    private fun openWhatsApp(number: String, context: Context) {
        val installed = appInstalledOrNot("com.whatsapp",context)

        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data =
                Uri.parse("http://api.whatsapp.com/send?phone=$number")
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(
                context,
                "Whats app not installed on your device",
                Toast.LENGTH_SHORT
            ).show()

        }

    }


    //Create method appInstalledOrNot
    private fun appInstalledOrNot(url: String,context: Context): Boolean {
        val packageManager: PackageManager = context.getPackageManager()
        val app_installed: Boolean
        app_installed = try {
            packageManager.getPackageInfo(url, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
        return app_installed
    }
    override fun getItemViewType(position: Int): Int {
        return if (arr[position] == null) {
            Constant.VIEW_TYPE_LOADING
        } else {
            Constant.VIEW_TYPE_ITEM
        }
    }

    override fun getItemCount(): Int {
        return arr.size
    }

}