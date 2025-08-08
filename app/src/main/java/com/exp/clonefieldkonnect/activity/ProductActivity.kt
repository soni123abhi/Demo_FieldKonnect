package com.exp.clonefieldkonnect.activity

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devstune.searchablemultiselectspinner.SearchableItem
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.connection.APIResultLitener
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.helper.Constant
import com.exp.clonefieldkonnect.helper.DialogClass
import com.exp.clonefieldkonnect.helper.RecyclerViewLoadMoreScroll
import com.exp.clonefieldkonnect.helper.StaticSharedpreference
import com.exp.clonefieldkonnect.model.CategoryModel
import com.exp.clonefieldkonnect.model.ProductNewModel
import com.exp.clonefieldkonnect.model.SubCategoryModel
import com.exp.import.Utilities
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import retrofit2.Response
import java.util.*

class ProductActivity : AppCompatActivity(), View.OnClickListener  {
    private lateinit var edtSearchcategory: AutoCompleteTextView
    private lateinit var edtSearchsubcategory: AutoCompleteTextView
    private lateinit var edtSearchproduct: AutoCompleteTextView
    private lateinit var cardview_category_main: RelativeLayout
    private lateinit var cardview_sub_category_main: RelativeLayout
    private lateinit var img_category: ImageView
    private lateinit var img_sub_category: ImageView
    private lateinit var textview_category_name: TextView
    private lateinit var textview_subcategory_name: TextView
    private lateinit var tv_model: TextView
    private lateinit var tv_hp: TextView
    private lateinit var tv_stage: TextView
    private lateinit var tv_sucXdel: TextView
    private lateinit var tv_phase: TextView
    private lateinit var tv_kw: TextView
    private lateinit var tv_warrenty: TextView
    private lateinit var tv_lp: TextView
    private lateinit var tv_model_name: TextView
    private lateinit var tv_user_id: TextView
    private lateinit var tvCount: TextView
    private lateinit var rel_main_cart: RelativeLayout
    private lateinit var img_pro_detail: ImageView
    private lateinit var linearLayout_phase: LinearLayout
    private lateinit var linearLayout_stage: LinearLayout
    private lateinit var relative_pro_details: RelativeLayout
    private lateinit var cardAddToCart: CardView
    private lateinit var cardAddToCartMoreProduct: CardView
    private var productId = 0

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewCategory: RecyclerView
    private lateinit var recyclerViewSubCategory: RecyclerView
    private lateinit var cardBack: CardView
    private lateinit var imgSearch: ImageView
    private var pageSize = "10"
    private var index = 1
    var productDetail: ProductNewModel = ProductNewModel()

    lateinit var linearMinus: LinearLayout
    lateinit var linearAdd: LinearLayout

    companion object {
        var isBack = false
        var productArrAddToCart: ArrayList<ProductNewModel?> = arrayListOf();
        var productArr: ArrayList<ProductNewModel?> = arrayListOf();
    }

    var productNewArr: ArrayList<ProductNewModel> = arrayListOf();

    var categoryArr: ArrayList<CategoryModel> = arrayListOf();
    var subCategoryArr: ArrayList<SubCategoryModel> = arrayListOf();
    private var items: MutableList<SearchableItem> = ArrayList()
    var product_name : ArrayList<String> = ArrayList()
    var product_id : ArrayList<String> = ArrayList()
    var subcatgory_name : ArrayList<String> = ArrayList()
    var subcatgory_id : ArrayList<String> = ArrayList()
    var subcatgory_image : ArrayList<String> = ArrayList()
    lateinit var edtCount: EditText
    var category_id = ""
    var selectedsubcategory_id = ""
    var selectedproduct_id = ""
    private var quantityCount = 1

//    lateinit var productAdapter: ProductAdapter
//    lateinit var categoryAdapter: CategoryAdapter
//    lateinit var subCategoryAdapter: SubCategoryAdapter
    lateinit var scrollListener: RecyclerViewLoadMoreScroll
    lateinit var scrollListenerHorizontal: RecyclerViewLoadMoreScroll
    lateinit var mLayoutManager: RecyclerView.LayoutManager
    lateinit var mSubLayoutManager: RecyclerView.LayoutManager
    var isCallMethod = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
        productArrAddToCart.clear()
        productArr.clear()
        initViews()
    }

    private fun initViews() {
        recyclerView = findViewById(R.id.recyclerView)
        cardBack = findViewById(R.id.cardBack)
        recyclerViewCategory = findViewById(R.id.recyclerViewCategory)
        recyclerViewSubCategory = findViewById(R.id.recyclerViewSubCategory)
        imgSearch = findViewById(R.id.imgSearch)

        edtSearchcategory = findViewById(R.id.edtSearchcategory)
        edtSearchsubcategory = findViewById(R.id.edtSearchsubcategory)
        edtSearchproduct = findViewById(R.id.edtSearchproduct)
        cardview_category_main = findViewById(R.id.cardview_category_main)
        cardview_sub_category_main = findViewById(R.id.cardview_sub_category_main)
        img_category = findViewById(R.id.img_category)
        img_sub_category = findViewById(R.id.img_sub_category)
        textview_category_name = findViewById(R.id.textview_category_name)
        textview_subcategory_name = findViewById(R.id.textview_subcategory_name)
        tv_model_name = findViewById(R.id.tv_model_name)

        edtSearchcategory.setOnClickListener(this)
        edtSearchsubcategory.setOnClickListener(this)
        edtSearchproduct.setOnClickListener(this)
        linearMinus = findViewById(R.id.linearMinus)
        linearAdd = findViewById(R.id.linearAdd)
        edtCount = findViewById(R.id.edtCount)
        tv_model = findViewById(R.id.tv_model)
        tv_hp = findViewById(R.id.tv_hp)
        tv_stage = findViewById(R.id.tv_stage)
        tv_sucXdel = findViewById(R.id.tv_sucXdel)
        tv_phase = findViewById(R.id.tv_phase)
        tv_kw = findViewById(R.id.tv_kw)
        tv_warrenty = findViewById(R.id.tv_warrenty)
        tv_lp = findViewById(R.id.tv_lp)
        tvCount = findViewById(R.id.tvCount)
        img_pro_detail = findViewById(R.id.img_pro_detail)
        linearLayout_phase = findViewById(R.id.linearLayout_phase)
        linearLayout_stage = findViewById(R.id.linearLayout_stage)
        relative_pro_details = findViewById(R.id.relative_pro_details)
        tv_user_id = findViewById(R.id.tv_user_id)
        cardAddToCart = findViewById(R.id.cardAddToCart)
        cardAddToCartMoreProduct = findViewById(R.id.cardAddToCartMoreProduct)
        rel_main_cart = findViewById(R.id.rel_main_cart)

//        setAdapter()
//        setCategoryAdapter()
//        setSubCategoryAdapter()
//        setRVScrollListener()
//
        getCategoryData()

        edtCount.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if (edtCount.text.toString() != "")
                    quantityCount = edtCount.text.toString().toInt()

                if (edtCount.text.toString() == "0") {
                    edtCount.setText("")
                    Toast.makeText(
                        this@ProductActivity,
                        "Enter at least one quantity",
                        Toast.LENGTH_LONG
                    ).show()
                }

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
            }
        })

        linearAdd.setOnClickListener {

            quantityCount++

            edtCount.setText(quantityCount.toString())
        }

        linearMinus.setOnClickListener {

            if (quantityCount > 1)
                quantityCount--

            edtCount.setText(quantityCount.toString())

        }

        rel_main_cart.setOnClickListener {
            if (productArrAddToCart.size == 0)
                Toast.makeText(this, "Please add product", Toast.LENGTH_LONG).show()
            else {
                val intent1 = Intent(this@ProductActivity, AddToCartActivity::class.java)
                intent1.putExtra("checkin", intent.getStringExtra("checkin"))
                startActivity(intent1)
            }

        }
        cardAddToCartMoreProduct.setOnClickListener {
            if (tvCount.text.toString() == "0") {
                Toast.makeText(
                    this@ProductActivity,
                    "Please add product",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
                edtSearchsubcategory.setText("Select Category")
                edtSearchproduct.setText("Select Product")
                cardview_sub_category_main.visibility = View.GONE
                relative_pro_details.visibility = View.GONE
                edtCount.setText("1")
                quantityCount = 1

        }
        cardAddToCart.setOnClickListener {

            var oldQuantity = -1
            var oldProductPos = -1

            if (edtCount.text.toString() == "") {
                Toast.makeText(
                    this@ProductActivity,
                    "Please enter Quantity",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            var priceInt = 0

            var singleDiscount = 0.0
            var singlePriceWithDis = priceInt - singleDiscount

            for ((index, value) in productArrAddToCart.withIndex()) {

                if (value!!.id == productId) {
                    oldQuantity = value.quantity
                    oldProductPos = index
                    break
                }
            }
            val edittext = edtSearchproduct.text.toString().trim()
            if (edittext != "Select Product" && edittext != "" ){
                if (oldQuantity == -1) {
                    productDetail.quantity= edtCount.text.toString().toInt()
                      productDetail.amount = tv_lp.text.toString()
                    productArrAddToCart.add(productDetail)
                    Toast.makeText(this@ProductActivity, "Product Added into Cart", Toast.LENGTH_LONG).show()

                    tvCount.text = productArrAddToCart.size.toString()


                } else {

                    productArrAddToCart[oldProductPos]!!.quantity  = edtCount.text.toString().toInt()
                    Toast.makeText(this@ProductActivity, "Product Quantity updated", Toast.LENGTH_LONG).show()
                }

            }else{
                Toast.makeText(this@ProductActivity, "Please add product", Toast.LENGTH_LONG).show()
            }
        }



        cardBack.setOnClickListener {
            showCancelDialog()
        }

        imgSearch.setOnClickListener {
            val intent1 = Intent(this@ProductActivity, ProductSearchActivity::class.java)
            intent1.putExtra("checkin",intent.getStringExtra("checkin"))
            startActivity(intent1)
        }



        edtSearchcategory.setOnTouchListener { view, motionEvent ->
            edtSearchcategory.showDropDown()
            false
        }
        edtSearchcategory.setOnItemClickListener { adapterView, view, i, l ->
            for(value in categoryArr!!){
                if(edtSearchcategory.text.toString()==value.categoryName){
                    category_id = value.id.toString()
                    if (category_id.isNotEmpty()){
                        cardview_category_main.visibility = View.VISIBLE
                        textview_category_name.text = value.categoryName.toString()

                        var image_cat = ApiClient.BASE_IMAGE_URL+categoryArr[i].categoryImage
                        if (image_cat.isNullOrEmpty()){
                        }else{
                            Glide.with(this@ProductActivity).load(image_cat).into(img_category)
                        }
                        getSubCategoryData(category_id)
                        edtSearchcategory.isEnabled = false
                    }
                }
            }
        }



        if (isCallMethod) {
            index = 1
        } else
            isCallMethod = true


        var timer: Timer? = null

    }

    private fun showCancelDialog() {
        val dialogView: View = LayoutInflater.from(this).inflate(R.layout.dialog_cancel, null)
        val dialog = Dialog(this)
        dialog.setContentView(dialogView)
        dialog.setCancelable(false)

        val btnYes: Button = dialogView.findViewById(R.id.btn_cancel_yes)
        val btnNo: Button = dialogView.findViewById(R.id.btn_cancel_no)

        btnYes.setOnClickListener {
            onBackPressed()
        }

        btnNo.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    lateinit var dialog: Dialog

    fun getProductList(
        isProgress: Boolean,
        search: String,
        isIndexOne: Boolean,
        subCategory_id: String
    ) {
        if (!Utilities.isOnline(this@ProductActivity)) {
            return
        }

        if (isProgress)
            dialog = DialogClass.progressDialog(this@ProductActivity)

        if (isIndexOne)
            index = 1

        val queryParams = HashMap<String, String>()
//        queryParams["page"] = index.toString()
//        queryParams["pageSize"] = pageSize
//        queryParams["search"] = search
        queryParams["subcategory_id"] = subCategory_id

        ApiClient.getProductList(

            StaticSharedpreference.getInfo(
                Constant.ACCESS_TOKEN,
                this@ProductActivity
            ).toString(), queryParams, object : APIResultLitener<JsonObject> {
                override fun onAPIResult(
                    response: Response<JsonObject>?,
                    errorMessage: String?
                ) {
                    if (isProgress) {
                        productArr.clear()
                        dialog.dismiss()
                    }
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {


                            var productArrLocal: ArrayList<ProductNewModel> = arrayListOf();
                            val gson = Gson()

                            productArrLocal.clear()
                            product_name.clear()
                            product_id.clear()

                            val listTypePro = object :
                                TypeToken<java.util.ArrayList<ProductNewModel>>() {}.type

                            productArrLocal = gson.fromJson<java.util.ArrayList<ProductNewModel>>(
                                response.body()!!.get("data").asJsonArray,

                                listTypePro
                            )


                            for (item in productArrLocal) {
                                val name = item.productName.toString()
                                val id = item.id.toString()

                                if (!product_name.contains(name)) {
                                    product_name.add(name)
                                    product_id.add(id)
                                }
                            }

                            edtSearchproduct.setOnClickListener {
                                spinnerproduct()
                            }
                        }
                    } else {
                        Toast.makeText(
                            this@ProductActivity,
                            resources.getString(R.string.poor_connection),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }, this@ProductActivity
        )
    }



    private fun getCategoryData() {

        if (!Utilities.isOnline(this@ProductActivity)) {
            return
        }
        dialog = DialogClass.progressDialog(this@ProductActivity)
        val queryParams = HashMap<String, String>()


//        queryParams["page"] = "1"
//        queryParams["pageSize"] = "30"
        ApiClient.getCategoryData(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this@ProductActivity).toString(),
            queryParams,
            object : APIResultLitener<JsonObject> {
                override fun onAPIResult(response: Response<JsonObject>?, errorMessage: String?) {

                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {

                            val gson = Gson()
                            val listType = object :
                                TypeToken<java.util.ArrayList<CategoryModel>>() {}.type

                            categoryArr = gson.fromJson<java.util.ArrayList<CategoryModel>>(
                                response.body()!!.get("data").asJsonArray,
                                listType
                            )

                            val disName = arrayOfNulls<String>(categoryArr!!.size)

                            for (i in categoryArr!!.indices) {

                                disName[i] = categoryArr!![i].categoryName

                            }
                            val aa = ArrayAdapter(this@ProductActivity, android.R.layout.simple_list_item_1, disName)
                            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            edtSearchcategory.setAdapter(aa)

                        } else {

                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    this@ProductActivity,
                                    false
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                        }
                    } else {
                        Toast.makeText(
                            this@ProductActivity,
                            resources.getString(R.string.poor_connection),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })
    }

    fun getSubCategoryData(categoryID: String) {

        if (!Utilities.isOnline(this@ProductActivity)) {
            return
        }
        dialog = DialogClass.progressDialog(this@ProductActivity)
        val queryParams = HashMap<String, String>()

//        queryParams["page"] = "1"
//        queryParams["pageSize"] = "30"
        queryParams["category_id"] = categoryID

        ApiClient.getSubCategoryData(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this@ProductActivity).toString(),
            queryParams,
            object : APIResultLitener<JsonObject> {
                override fun onAPIResult(response: Response<JsonObject>?, errorMessage: String?) {

                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {

                            val gson = Gson()


                            val listTypeSub = object :
                                TypeToken<java.util.ArrayList<SubCategoryModel>>() {}.type

                            subCategoryArr = gson.fromJson<java.util.ArrayList<SubCategoryModel>>(
                                response.body()!!.get("data").asJsonArray,
                                listTypeSub
                            )
                            for (item in subCategoryArr) {
                                val name = item.subcategoryName.toString()
                                val id = item.id.toString()
                                val image = item.subcategoryImage.toString()

                                if (!subcatgory_name.contains(name)) {
                                    subcatgory_name.add(name)
                                    subcatgory_id.add(id)
                                    subcatgory_image.add(image)
                                }
                            }

                            edtSearchsubcategory.setOnClickListener {
                                edtSearchsubcategory.setText("Select Category")
                                edtSearchproduct.setText("Select Product")
                                cardview_sub_category_main.visibility = View.GONE
                                relative_pro_details.visibility = View.GONE
                                edtCount.setText("1")
                                quantityCount = 1
                                spinnersubcategory()
                            }

                        } else {

                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    this@ProductActivity,
                                    false
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                        }
                    } else {
                        //  dialog.dismiss()
                        Toast.makeText(
                            this@ProductActivity,
                            resources.getString(R.string.poor_connection),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }, this@ProductActivity
        )
    }

    private fun spinnerproduct() {
        val builder = android.app.AlertDialog.Builder(this@ProductActivity)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_alert_dialog2, null)
        builder.setView(dialogView)

        val searchEditText = dialogView.findViewById<EditText>(R.id.searchEditText)
        val listView = dialogView.findViewById<ListView>(R.id.listView)
        val button = dialogView.findViewById<Button>(R.id.button)

        val colorsArray = product_name.map { it.toString() }.toTypedArray()
        val adapter = ArrayAdapter(this@ProductActivity, android.R.layout.simple_list_item_1, colorsArray)
        listView.adapter = adapter

        builder.setTitle("Select Product")

        val dialog = builder.create()

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {
                adapter.filter.filter(charSequence)
            }

            override fun afterTextChanged(editable: Editable) {}
        })
        button.setOnClickListener {
            edtSearchproduct.setText("")
            selectedproduct_id = ""
            dialog.dismiss()
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedPosition = product_name.indexOf(adapter.getItem(position).toString())
            if (selectedPosition != -1) {

                val selectedParentId = product_id[selectedPosition].toString()
                val selectedParentName = product_name[selectedPosition].toString()
                edtSearchproduct.setText(selectedParentName.toString())
                selectedproduct_id = selectedParentId
                productId = selectedParentId.toString().toInt()
                getProductDetailsValue(selectedParentId)
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    private fun getProductDetailsValue(selectedParentId  :String) {

        if (!Utilities.isOnline(this)) {
            return
        }

        var dialog = DialogClass.progressDialog(this)

        val queryParams = java.util.HashMap<String, String>()
        queryParams["product_id"] = selectedParentId
        ApiClient.getProductDetails(
            StaticSharedpreference.getInfo(
                Constant.ACCESS_TOKEN,
                this@ProductActivity
            ).toString(), queryParams, object : APIResultLitener<JsonObject> {
                override fun onAPIResult(
                    response: Response<JsonObject>?,
                    errorMessage: String?
                ) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {
                            relative_pro_details.visibility =View.VISIBLE
                            val gson = Gson()
                            val listType = object :
                                TypeToken<ProductNewModel>() {}.type

                            productDetail = gson.fromJson<ProductNewModel>(
                                response.body()!!.get("data").asJsonObject,
                                listType
                            )

                            Glide.with(this@ProductActivity)
                                .load(productDetail.productImage)
                                .into(img_pro_detail)

                            val edittext = edtSearchcategory.text.toString().trim()

                            tv_model.text = productDetail.model_no

                            tv_stage.text = productDetail.stage
                            tv_sucXdel.text = productDetail.specification
                            tv_phase.text = productDetail.phase
                            tv_kw.text = productDetail.part_no
                            tv_warrenty.text = productDetail.expiry_interval_preiod + productDetail.expiry_interval
                            tv_lp.text = productDetail.mrp
                            tv_model_name.text = productDetail.productName


                            if (edittext.equals("Fan appliances & lightings", ignoreCase = true)) {
                                linearLayout_stage.visibility = View.GONE
                                linearLayout_phase.visibility = View.GONE
                                tv_user_id.text = "Brand Name"
                                tv_hp.text = productDetail.brandName

                            } else {
                                linearLayout_stage.visibility = View.VISIBLE
                                linearLayout_phase.visibility = View.VISIBLE
                                tv_user_id.text = "HP"
                                tv_hp.text = productDetail.hp
                            }


                        }
                    } else {
                        relative_pro_details.visibility =View.GONE
                        Toast.makeText(
                            this@ProductActivity,
                            resources.getString(R.string.poor_connection),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        )
    }



    private fun spinnersubcategory() {
        val builder = android.app.AlertDialog.Builder(this@ProductActivity)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_alert_dialog2, null)
        builder.setView(dialogView)

        val searchEditText = dialogView.findViewById<EditText>(R.id.searchEditText)
        val listView = dialogView.findViewById<ListView>(R.id.listView)
        val button = dialogView.findViewById<Button>(R.id.button)

        val colorsArray = subcatgory_name.map { it.toString() }.toTypedArray()
        val adapter = ArrayAdapter(this@ProductActivity, android.R.layout.simple_list_item_1, colorsArray)
        listView.adapter = adapter

        builder.setTitle("Select Sub-Category")

        val dialog = builder.create()

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {
                adapter.filter.filter(charSequence)
            }

            override fun afterTextChanged(editable: Editable) {}
        })
        button.setOnClickListener {
            edtSearchsubcategory.setText("")
            selectedsubcategory_id = ""
            dialog.dismiss()
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedPosition = subcatgory_name.indexOf(adapter.getItem(position).toString())
            if (selectedPosition != -1) {
                val selectedParentId = subcatgory_id[selectedPosition].toString()
                val selectedParentName = subcatgory_name[selectedPosition].toString()
                val selectedParentimage = subcatgory_image[selectedPosition].toString()

                edtSearchsubcategory.setText(selectedParentName)
                selectedsubcategory_id = selectedParentId
                cardview_sub_category_main.visibility = View.VISIBLE

                Glide.with(this@ProductActivity).load(selectedParentimage).into(img_sub_category)
                textview_subcategory_name.text = selectedParentName

                getProductList(true, "", true, selectedsubcategory_id)

                dialog.dismiss()
            }
        }
        dialog.show()
    }


    override fun onResume() {
        super.onResume()

//        productAdapter.notifyDataSetChanged()

    }

    override fun onClick(p0: View) {
        when (p0.id)
        {
            R.id.edtSearchcategory ->
                edtSearchcategory.showDropDown()

            R.id.edtSearchsubcategory ->
                edtSearchsubcategory.showDropDown()

        }
    }

}