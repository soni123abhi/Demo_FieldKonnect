package com.exp.clonefieldkonnect.connection

import FillterModel
import com.exp.clonefieldkonnect.model.AttendanceDetailModel
import com.exp.clonefieldkonnect.model.AttendanceSubmitModel
import com.exp.clonefieldkonnect.model.BeatScheduleModel
import com.exp.clonefieldkonnect.model.CityModel
import com.exp.clonefieldkonnect.model.ClusterOrderListModel
import com.exp.clonefieldkonnect.model.CustomerParentModel
import com.exp.clonefieldkonnect.model.DealerApprovalListModel
import com.exp.clonefieldkonnect.model.DealerMonthlySalesReport
import com.exp.clonefieldkonnect.model.DealerSalesReportModel
import com.exp.clonefieldkonnect.model.DealergrowthModel
import com.exp.clonefieldkonnect.model.DistrictModel
import com.exp.clonefieldkonnect.model.DistriutorModel
import com.exp.clonefieldkonnect.model.DraftReportModel
import com.exp.clonefieldkonnect.model.EnquiryModel
import com.exp.clonefieldkonnect.model.ExpenseApprovalModel
import com.exp.clonefieldkonnect.model.ExpenseApprovalSubmitModel
import com.exp.clonefieldkonnect.model.ExpenseTypeModel
import com.exp.clonefieldkonnect.model.GetDiscountLimitModel
import com.exp.clonefieldkonnect.model.InsertOrderRequestModel
import com.exp.clonefieldkonnect.model.LeadContactModel
import com.exp.clonefieldkonnect.model.LeadDetailModel
import com.exp.clonefieldkonnect.model.LeadModel
import com.exp.clonefieldkonnect.model.LeadStatusSourceModel
import com.exp.clonefieldkonnect.model.LeadTaskModel
import com.exp.clonefieldkonnect.model.LeaveBalanceModel
import com.exp.clonefieldkonnect.model.LocationRequestModel
import com.exp.clonefieldkonnect.model.MSPFilterDataModel
import com.exp.clonefieldkonnect.model.MspActivityTypeModel
import com.exp.clonefieldkonnect.model.MspTabledataModel
import com.exp.clonefieldkonnect.model.NewDealerViewDetailMOdel
import com.exp.clonefieldkonnect.model.NotificationModel
import com.exp.clonefieldkonnect.model.OportunityDetailModel
import com.exp.clonefieldkonnect.model.OrderDetailsModel
import com.exp.clonefieldkonnect.model.OrderListModel
import com.exp.clonefieldkonnect.model.OrderPdfModel
import com.exp.clonefieldkonnect.model.OrderUpdateModel
import com.exp.clonefieldkonnect.model.OutstandingModel
import com.exp.clonefieldkonnect.model.ParticallyorderDetailsRequestModel
import com.exp.clonefieldkonnect.model.PinCodeModel
import com.exp.clonefieldkonnect.model.PointCollectionRequest
import com.exp.clonefieldkonnect.model.PrimaryFilterListModel
import com.exp.clonefieldkonnect.model.PrimarySchemeModel
import com.exp.clonefieldkonnect.model.PrimarySchemeTableModel
import com.exp.clonefieldkonnect.model.ProductDetailModel
import com.exp.clonefieldkonnect.model.ReportcountModel
import com.exp.clonefieldkonnect.model.SalesDetailModel
import com.exp.clonefieldkonnect.model.SalesModel
import com.exp.clonefieldkonnect.model.SalessListModel
import com.exp.clonefieldkonnect.model.SarthiPointsModel
import com.exp.clonefieldkonnect.model.SpecialDiscountModel
import com.exp.clonefieldkonnect.model.StateModel
import com.exp.clonefieldkonnect.model.StoreCustomerRequestModel
import com.exp.clonefieldkonnect.model.TaskDropdownModel
import com.exp.clonefieldkonnect.model.UnpaidInvoiceModel
import com.exp.clonefieldkonnect.model.UserActiveModel
import com.exp.clonefieldkonnect.model.UserActivityListModel
import com.exp.clonefieldkonnect.model.UserAttendanceListModel
import com.exp.clonefieldkonnect.model.UserDataModel
import com.exp.clonefieldkonnect.model.UserExpenseDetailModel
import com.exp.clonefieldkonnect.model.UserExpenseListModel
import com.exp.clonefieldkonnect.model.UserTourListModel
import com.exp.clonefieldkonnect.model.VersionModel
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface BaseApiService {
    @Headers("Content-Type: application/json")
    @POST("login")
    fun login(@Body queryParams: Map<String, String?>): Call<JsonObject>

    @Headers("Content-Type: application/json")
    @POST("submitCheckin")
    fun submitCheckin(
        @Header("Authorization") authorization: String?,
        @Body queryParams: Map<String, String?>
    ): Call<JsonObject>

    @Headers("Content-Type: application/json")
    @POST("submitCheckout")
    fun submitCheckout(
        @Header("Authorization") authorization: String?,
        @Body queryParams: Map<String, String?>
    ): Call<JsonObject>

    @Headers("Content-Type: application/json")
    @POST("storeCustomer")
    fun storeCustomer(
        @Header("Authorization") authorization: String?,
        @Body queryParams: StoreCustomerRequestModel
    ): Call<JsonObject>

    @Headers("Content-Type: application/json")
    @POST("updateLiveLocation")
    fun updateLiveLocation(
        @Header("Authorization") authorization: String?,
        @Body queryParams: LocationRequestModel
    ): Call<JsonObject>


    @Headers("Content-Type: application/json")
    @POST("pointsCollection")
    fun pointsCollection(
        @Header("Authorization") authorization: String?, @Body queryParams: PointCollectionRequest
    ): Call<JsonObject>


    @Multipart
    @POST("updateCustomerProfile")
    fun updateCustomerProfile(
        @Header("Authorization") authorization: String?,
        @Part("name") name: RequestBody?,
        @Part("full_name") full_name: RequestBody?,
        @Part("mobile") mobile: RequestBody?,
        @Part("contact_number") alternate_no: RequestBody?,
        @Part("parent_id") parent_id: RequestBody?,
        @Part("email") email: RequestBody?,
        @Part("address1") address1: RequestBody?,
        @Part("latitude") latitude: RequestBody?,
        @Part("longitude") longitude: RequestBody?,
        @Part("beat_id") beat_id: RequestBody?,
        @Part("gstin_no") gstin_no: RequestBody?,
        @Part("pan_no") pan_no: RequestBody?,
        @Part("aadhar_no") aadhar_no: RequestBody?,
        @Part("otherid_no") otherid_no: RequestBody?,
        @Part("zipcode") zipcode: RequestBody?,
        @Part("landmark") landmark: RequestBody?,
        @Part("customertype") customertype: RequestBody?,
        @Part("status_type") status_type: RequestBody?,
        @Part("grade") grade: RequestBody?,
        @Part("customer_id") customer_id: RequestBody?,
        @Part("customer_type") customerType: RequestBody?,
        @Part("address_id") addressId: RequestBody?,
        @Part("pincode_id") pincode_id: RequestBody?,
        @Part("city_id") city_id: RequestBody?,
        @Part("district_id") district_id: RequestBody?,
        @Part("state_id") state_id: RequestBody?,
        @Part("country_id") country_id: RequestBody?,
        @Part("survey") survey: RequestBody?,
        @Part("dealing") dealing: RequestBody?,
        @Part gstImg: MultipartBody.Part,
        @Part pan_image: MultipartBody.Part?,
        @Part aadhar_image: MultipartBody.Part?,
        @Part other_image: MultipartBody.Part?,
        @Part visiting_card: MultipartBody.Part?,
        @Part image: MultipartBody.Part?,

        ): Call<JsonObject>

    @Headers("Content-Type: application/json")
    @POST("getTaskInfo")
    fun getTaskInfo(
        @Header("Authorization") authorization: String?, @Body queryParams: Map<String, String?>
    ): Call<JsonObject>

    @Headers("Content-Type: application/json")
    @POST("mobileNumberExists")
    fun mobileNumberExists(
        @Header("Authorization") authorization: String?, @Body queryParams: Map<String, String?>
    ): Call<JsonObject>

    @Headers("Content-Type: application/json")
    @POST("gstNumberExists")
    fun gstNumberExists(
        @Header("Authorization") authorization: String?, @Body queryParams: Map<String, String?>
    ): Call<JsonObject>

    @Headers("Content-Type: application/json")
    @POST("emailExists")
    fun emailExists(
        @Header("Authorization") authorization: String?, @Body queryParams: Map<String, String?>
    ): Call<JsonObject>

    @Headers("Content-Type: application/json")
    @POST("taskMarkComplite")
    fun taskMarkComplite(
        @Header("Authorization") authorization: String?, @Body queryParams: Map<String, String?>
    ): Call<JsonObject>

    @GET("getCheckin")
    abstract fun getCheckin(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>
    ): Call<JsonObject>
    @Headers("Accept: application/json")
    @GET("logout")
    abstract fun logout(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>
    ): Call<JsonObject>
    @Headers("Accept: application/json")
    @GET("dashboard")
    abstract fun dashboard(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>
    ): Call<JsonObject>

    @GET("getPunchin")
    abstract fun getPunchin(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>
    ): Call<JsonObject>
    @Headers("Accept: application/json")
    @GET("getUserSataus")
    abstract fun getUserSataus(
        @Header("Authorization") authorization: String?,
    ): Call<UserActiveModel>

    @GET("getBeatList")
    abstract fun getBeatList(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>
    ): Call<JsonObject>

    @Headers("Accept: application/json")
    @GET("getBeatDropdownList")
    abstract fun getBeatDropdownList(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>
    ): Call<JsonObject>

    @GET("getCustomerTypeList")
    abstract fun getcustomertype(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>
    ): Call<JsonObject>

    @GET("getCategoryData")
    abstract fun getCategoryData(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>
    ): Call<JsonObject>

    @GET("getBeatCustomers")
    abstract fun getBeatCustomers(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>
    ): Call<JsonObject>


    @Multipart
    @POST("userPunchin")
    fun userPunchin(
        @Header("Authorization") authorization: String?,
        @Part("punchin_longitude") punchin_longitude: RequestBody?,
        @Part("punchin_latitude") punchin_latitude: RequestBody?,
        @Part("punchin_address") punchin_address: RequestBody?,
        @Part("punchin_summary") summary: RequestBody?,
        @Part("tourid") tourid: RequestBody?,
        @Part("beats") beats: RequestBody?,
        @Part("city") city: RequestBody?,
        @Part("type") type: RequestBody?,
    ): Call<JsonObject>

    @Multipart
    @POST("storeCustomer")
    fun storeCustomer(
        @Header("Authorization") authorization: String?,
        @Part("name") name: RequestBody?,
        @Part("full_name") full_name: RequestBody?,
        @Part("mobile") mobile: RequestBody?,
        @Part("contact_number") contact_number: RequestBody?,
        @Part("parent_id") parent_id: RequestBody?,
        @Part("email") email: RequestBody?,
        @Part("address1") address1: RequestBody?,
        @Part("latitude") latitude: RequestBody?,
        @Part("longitude") longitude: RequestBody?,
        @Part("beat_id") beat_id: RequestBody?,
        @Part("gstin_no") gstin_no: RequestBody?,
        @Part("pan_no") pan_no: RequestBody?,
        @Part("aadhar_no") aadhar_no: RequestBody?,
        @Part("otherid_no") otherid_no: RequestBody?,
        @Part("zipcode") zipcode: RequestBody?,
        @Part("landmark") landmark: RequestBody?,
        @Part("customertype") customertype: RequestBody?,
        @Part("status_type") status_type: RequestBody?,
        @Part("grade") grade: RequestBody?,
        @Part("survey") survey: RequestBody?,
        @Part("dealing") dealing: RequestBody?,
        @Part gstImg: MultipartBody.Part,
        @Part pan_image: MultipartBody.Part?,
        @Part aadhar_image: MultipartBody.Part?,
        @Part other_image: MultipartBody.Part?,
        @Part visiting_card: MultipartBody.Part?,
        @Part image: MultipartBody.Part?,
        @Part bank_passbook: MultipartBody.Part?,
    ): Call<JsonObject>


    @GET("getDistributors")
    abstract fun getDistributors(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>
    ): Call<DistriutorModel>

    @GET("getVisitTypes")
    abstract fun getVisitTypes(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>
    ): Call<JsonObject>

    @Multipart
    @POST("insertSales")
    fun insertSales(
        @Header("Authorization") authorization: String?,
        @Part("buyer_id") buyer_id: RequestBody?,
        @Part("seller_id") seller_id: RequestBody?,
        @Part("invoice_no") invoice_no: RequestBody?,
        @Part("invoice_date") invoice_date: RequestBody?,
        @Part("grand_total") grand_total: RequestBody?,
        @Part files: List<MultipartBody.Part?>?
    ): Call<JsonObject>

    @Multipart
    @POST("userPunchout")
    fun userPunchout(
        @Header("Authorization") authorization: String?,
        @Part("punchin_id") punchin_id: RequestBody?,
        @Part("punchout_longitude") punchout_longitude: RequestBody?,
        @Part("punchout_latitude") punchout_latitude: RequestBody?,
        @Part("punchout_address") punchout_address: RequestBody?,
        @Part("punchout_summary") summary: RequestBody?
    ): Call<JsonObject>

    @Headers("Content-Type: application/json")
    @POST("insertOrder")
    fun insertOrder(
        @Header("Authorization") authorization: String?,
        @Body queryParams: InsertOrderRequestModel
    ): Call<JsonObject>

    @Headers("Content-Type: application/json")
    @POST("submitPartiallyDispatched")
    fun particallydispatchOrder(
        @Header("Authorization") authorization: String?,
        @Body queryParams: ParticallyorderDetailsRequestModel
    ): Call<JsonObject>

    @Headers("Content-Type: application/json")
    @POST("updateCustomerLocation")
    fun updateCustomerLocation(
        @Header("Authorization") authorization: String?,
        @Body queryParams: Map<String, String?>
    ): Call<JsonObject>

    @Headers("Content-Type: application/json")
    @POST("leadToCustomer")
    fun leadToCustomer(
        @Header("Authorization") authorization: String?,
        @Body queryParams: Map<String, String?>
    ): Call<JsonObject>

    @Headers("Content-Type: application/json")
    @POST("createNewTask")
    fun createNewTask(
        @Header("Authorization") authorization: String?,
        @Body queryParams: Map<String, String?>
    ): Call<JsonObject>

    @Headers("Content-Type: application/json")
    @POST("requestReport")
    fun requestReport(
        @Header("Authorization") authorization: String?,
        @Body queryParams: Map<String, String?>
    ): Call<JsonObject>


    @Multipart
    @POST("paymentReceived")
    fun paymentReceived(
        @Header("Authorization") authorization: String?,
        @Part("detail") detail: RequestBody?,
        @Part("payment_date") payment_date: RequestBody?,
        @Part("payment_type") payment_type: RequestBody?,
        @Part("customer_id") customer_id: RequestBody?,
        @Part("payment_mode") payment_mode: RequestBody?,
        @Part("amount") amount: RequestBody?,
        @Part("reference_no") reference_no: RequestBody?,
        @Part("bank_name") bank_name: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part files: MultipartBody.Part?
    ): Call<JsonObject>


    @Multipart
    @POST("submitVisitReports")
    fun submitVisitReports(
        @Header("Authorization") authorization: String?,
        @Part("customer_id") customer_id: RequestBody?,
        @Part("checkin_id") checkin_id: RequestBody?,
        @Part("visit_type_id") visit_type_id: RequestBody?,
        @Part("report_title") report_title: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part("lead_id") lead_id: RequestBody?
    ): Call<JsonObject>


    @GET("getProductList")
    abstract fun getProductList(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>
    ): Call<JsonObject>

    @GET("getSubCategoryData")
    abstract fun getSubCategoryData(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>
    ): Call<JsonObject>

    @GET("getOrderDetails")
    abstract fun getOrderDetails(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>
    ): Call<OrderDetailsModel>

    @GET("getSalesDetails")
    abstract fun getsalesDetails(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>
    ): Call<SalesDetailModel>

    @POST("updateClusterOrder")
    abstract fun updateorder(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>
    ): Call<OrderUpdateModel>

    @GET("getOrderPfd")
    abstract fun getorderpdf(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>
    ): Call<OrderPdfModel>

    @GET("get-field-connet-version")
    abstract fun getversion(
    ): Call<VersionModel>

    @GET("getSales")
    abstract fun getSales(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>
    ): Call<SalesModel>
    @Headers("Accept: application/json")
    @GET("getOrderList")
    abstract fun getOrderList(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>
    ): Call<OrderListModel>

    @GET("getSales")
    abstract fun getsalesssList(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>
    ): Call<SalessListModel>

    @GET("getClusterOrderList")
    abstract fun getclusterOrderList(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>
    ): Call<ClusterOrderListModel>

    @GET("getSpecialOrderList")
    abstract fun getspecialOrderList(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>
    ): Call<SpecialDiscountModel>

    @GET("getSalesDetails")
    abstract fun getSalesDetails(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>
    ): Call<JsonObject>

    @GET("getSurveyQuestions")
    abstract fun getSurveyQuestions(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>
    ): Call<EnquiryModel>

    @POST("getCustomerInfo")
    abstract fun getCustomerInfo(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>
    ): Call<JsonObject>

    @GET("getUnpaidInvoice")
    abstract fun getUnpaidInvoice(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>
    ): Call<UnpaidInvoiceModel>

    @GET("getPaymentList")
    abstract fun getPaymentList(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>
    ): Call<OutstandingModel>

    @GET("getRetailers")
    abstract fun getRetailers(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>
    ): Call<DistriutorModel>

    @POST("getRetailers")
    abstract fun getRetailersSearch(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>,
        @Query("city_id[]") citi_idList: ArrayList<String>,
        @Query("branch_id[]") branch_idList: ArrayList<String>,
        ): Call<JsonObject>

    @POST("user/msp_activity")
    abstract fun submitmsp(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>,
        @Query("cities[]") searchcity: ArrayList<String>,
        @Query("customers[]") customerIds: ArrayList<String>,
        ): Call<AttendanceSubmitModel>

    @GET("getPincodeList")
    abstract fun getPincodeList(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>
    ): Call<PinCodeModel>

    @GET("getCityList")
    abstract fun getCityList(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>
    ): Call<CityModel>
    @Headers("Accept: application/json")
    @GET("getMarketIntelligencesField")
    abstract fun getfillterList(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>
    ): Call<FillterModel>

    @GET("getStateList")
    abstract fun getStateList(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>
    ): Call<StateModel>


    @GET("getDistrictList")
    abstract fun getDistrictList(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>
    ): Call<DistrictModel>

    @GET("getPincodeInfo")
    abstract fun getPincodeInfo(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>
    ): Call<JsonObject>

    @GET("getProductDetails")
    abstract fun getProductDetails(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>
    ): Call<JsonObject>

    @GET("getPaymentInfo")
    abstract fun getPaymentInfo(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>
    ): Call<ProductDetailModel>

    @GET("getUpcomingTasks")
    abstract fun getUpcomingTasks(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>
    ): Call<JsonObject>

    @GET("getCollectedPoints")
    abstract fun getCollectedPoints(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>
    ): Call<JsonObject>

    @GET("upcommingTourProgramme")
    abstract fun upcommingTourProgramme(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>
    ): Call<JsonObject>

    @GET("getTodaySchedul")
    abstract fun getbeatschedule(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>
    ): Call<BeatScheduleModel>

    @GET("getReportType")
    abstract fun getReportType(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>
    ): Call<JsonObject>

    @Headers("Accept: application/json")
    @GET("reporting/users")
    abstract fun getuseractivitylist(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>,
        @Query("search_branches[]") search_branches: ArrayList<String>
    ): Call<UserActivityListModel>


    @GET("leads")
    abstract fun getlead(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>,
    ): Call<LeadModel>

    @GET("getLeadTasks")
    abstract fun getleadtaskk(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>,
    ): Call<LeadTaskModel>

    @GET("getAllLeadNotifications")
    abstract fun getnotificationn(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>,
    ): Call<NotificationModel>

    @GET("getAllOpportunities")
    abstract fun getopportunity(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>,
    ): Call<OportunityDetailModel>

    @GET("leadDetails")
    abstract fun getleaddetailsss(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>,
    ): Call<LeadDetailModel>

    @GET("getTaskDropdowns")
    abstract fun getleadtaskdropdownn(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>,
    ): Call<TaskDropdownModel>

    @GET("getLeadContacts")
    abstract fun getleadcontacttt(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>,
    ): Call<LeadContactModel>

    @GET("getLeadStatusSource")
    abstract fun getleadstatussource(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>,
    ): Call<LeadStatusSourceModel>

    @GET("primary-sales")
    abstract fun getdealersales(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>,
    ): Call<DealerSalesReportModel>

    @GET("getDealerGrowth")
    abstract fun getdealergrowth(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>,
    ): Call<DealergrowthModel>

    @GET("getprimary-scheme-filter")
    abstract fun getprimaryfilterlist(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>,
    ): Call<PrimaryFilterListModel>

    @GET("user/msp-activity-filter")
    abstract fun getmspfilterlist(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>,
    ): Call<MSPFilterDataModel>

    @GET("user/msp-activity-counts")
    abstract fun getmsptablelist(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>,
    ): Call<MspTabledataModel>

    @GET("getPrimarySchemes")
    abstract fun getprimaryschemelist(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>,
    ): Call<PrimarySchemeModel>

    @GET("getPrimarySchemeData")
    abstract fun getprimaryschemeshowdata(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>,
    ): Call<PrimarySchemeTableModel>

    @GET("monthly-sales?")
    abstract fun getmonthlysales(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>,
    ): Call<DealerMonthlySalesReport>

    @GET("allExpenseListing")
    abstract fun getexpenseaprovelist(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>,
        @Query("search_branches[]") search_branches: ArrayList<String>
    ): Call<ExpenseApprovalModel>

    @GET("getappointments")
    abstract fun getdealerapprovallist(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>,
        @Query("branch[]") search_branches: ArrayList<String>
    ): Call<DealerApprovalListModel>

    @GET("getappointmentsDetails")
    abstract fun getdealerviewdetail(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>,
    ): Call<NewDealerViewDetailMOdel>

    @POST("approveAppointment")
    abstract fun approverdealearappointment(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>,
    ): Call<AttendanceSubmitModel>

    @POST("addbmremark")
    abstract fun saveappointmentremark(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>,
    ): Call<AttendanceSubmitModel>

    @POST("addCheckinDraft")
    abstract fun submitdraftreport(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>,
    ): Call<AttendanceSubmitModel>

    @GET("getCheckinDraft")
    abstract fun getdraftreport(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>,
    ): Call<DraftReportModel>

    @GET("tour/userlist")
    abstract fun getusertourlist(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>,
        @Query("search_branches[]") search_branches: ArrayList<String>
    ): Call<UserTourListModel>

    @GET("expenseListing")
    abstract fun getexpenselist(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>,
    ): Call<UserExpenseListModel>

    @Headers("Accept: application/json")
    @GET("pendingCounts")
    abstract fun getreportcount(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>,
    ): Call<ReportcountModel>

    @GET("logout")
    abstract fun calllogout(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>,
    ): Call<AttendanceSubmitModel>

    @POST("expenseDetails")
    abstract fun getexpensedetail(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>,
    ): Call<UserExpenseDetailModel>

    @POST("getExpensesType")
    abstract fun getexpensetypelist(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>,
    ): Call<ExpenseTypeModel>

    @GET("getAllUserPunchInOut")
    abstract fun getuserattendancelist(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>,
        @Query("search_branches[]") search_branches: ArrayList<String>
    ): Call<UserAttendanceListModel>
    @Headers("Accept: application/json")
    @GET("getSarthiPoints")
    abstract fun getsarthipoint(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>,
    ): Call<SarthiPointsModel>

    @GET("showAttendance")
    abstract fun getuserattendancedetail(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>,
    ): Call<AttendanceDetailModel>

    @GET("attendance/changeStatus")
    abstract fun userattendancesubmit(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>,
    ): Call<AttendanceSubmitModel>

    @POST("customer/deleteOrder")
    abstract fun cancelorder(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>,
    ): Call<AttendanceSubmitModel>

    @POST("submitFullyDispatched")
    abstract fun fullydispatchorder(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>,
    ): Call<AttendanceSubmitModel>

    @POST("approveExpense")
    abstract fun expenseapprovalsubmit(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>,
    ): Call<ExpenseApprovalSubmitModel>

    @POST("rejectExpense")
    abstract fun expenserejectionsubmit(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>,
    ): Call<ExpenseApprovalSubmitModel>

    @POST("addLeaves")
    abstract fun userleavesubmit(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>,
    ): Call<AttendanceSubmitModel>
    @Headers("Accept: application/json")
    @GET("getLeaveBalance")
    abstract fun userleavebal(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>,
    ): Call<LeaveBalanceModel>

    @POST("tour/add")
    abstract fun createtoursubmit(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>,
        @Query("date[]") tourCreateDate: ArrayList<String>,
        @Query("town[]") tourCreateCity: ArrayList<String>,
        @Query("objectives[]") tourCreateObjective: ArrayList<String>,
    ): Call<AttendanceSubmitModel>

    @Multipart
    @POST("createExpense")
    abstract fun Expensesubmit(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>,
        @Part file: List<MultipartBody.Part>,
    ): Call<AttendanceSubmitModel>

    @Multipart
    @POST("updateExpense")
    abstract fun Expenseupdate(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>,
        @Part file: List<MultipartBody.Part>,
        @Query("image_id[]") edit_select_imagepath_remove: ArrayList<String>,
    ): Call<AttendanceSubmitModel>
    @Headers("Accept: application/json")
    @Multipart
    @POST("MarketIntelligenceStore")
    fun marketinteeee(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>,
        @Part("data") jsonFormData: RequestBody,
        @Part servey_image: MultipartBody.Part?
    ): Call<JsonObject>



    @POST("tour/edit")
    abstract fun submittourapproval(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>,
        @Query("tour_id[]") touridarray: ArrayList<String>,
        @Query("date[]") datearray: ArrayList<String>,
        @Query("town[]") townarray: ArrayList<String>,
        @Query("objectives[]") objectivearray: ArrayList<String>,
        @Query("status[]") statusArray: ArrayList<Int>,
    ): Call<AttendanceSubmitModel>

    @POST("leadCreate")
    abstract fun submitcreatelead(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>,
    ): Call<AttendanceSubmitModel>

    @POST("changeTaskStatus")
    abstract fun submittaskstatus(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>,
    ): Call<AttendanceSubmitModel>

    @POST("readNotification")
    abstract fun callreadnotificationn(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>,
    ): Call<AttendanceSubmitModel>

    @POST("addNote")
    abstract fun submitleadnote(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>,
    ): Call<AttendanceSubmitModel>

    @POST("updateLeadStatus")
    abstract fun updateleadtypeee(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>,
    ): Call<AttendanceSubmitModel>

    @POST("addLeadopportunity")
    abstract fun submitleadopportunityyy(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>,
    ): Call<AttendanceSubmitModel>

    @POST("leadSubmitCheckin")
    abstract fun submitleadcheckin(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>,
    ): Call<AttendanceSubmitModel>

    @POST("leadSubmitCheckout")
    abstract fun submitleadcheckout(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>,
    ): Call<AttendanceSubmitModel>

    @POST("deleteOpportunity")
    abstract fun deleteleadopportunityyy(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>,
    ): Call<AttendanceSubmitModel>

    @POST("addleadTask")
    abstract fun submitleadtask(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>,
    ): Call<AttendanceSubmitModel>


    @Headers("Accept: application/json")
    @GET("user/activity")
    abstract fun getuseractivitydetail(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>
    ): Call<JsonObject>
    @Headers("Accept: application/json")
    @GET("getUserDashboardData")
    abstract fun getUserData(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>,
        @Query("tamonth[]") selectedmonth: ArrayList<String>
    ): Call<UserDataModel>

    @GET("getNotification")
    abstract fun getNotification(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>
    ): Call<JsonObject>
    @Headers("Accept: application/json")
    @GET("userCityList")
    abstract fun userCityList(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>
    ): Call<JsonObject>

    @GET("user/msp_activity")
    abstract fun usermsptypeList(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>
    ): Call<MspActivityTypeModel>

    @GET("tour/show")
    abstract fun tourviewdetails(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>
    ): Call<JsonObject>

    @GET("getDevision")
    abstract fun customerdivisionList(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>
    ): Call<JsonObject>

    @GET("getRetailerList")
    abstract fun customerparentList(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>
    ): Call<CustomerParentModel>
    @Headers("Accept: application/json")
    @GET("getOrderDiscountLimit")
    abstract fun getDiscountLimit(
        @Header("Authorization") authorization: String?,
    ): Call<GetDiscountLimitModel>


    @GET("getWorkType")
    abstract fun getWorkType(
        @Header("Authorization") authorization: String?,
        @QueryMap queryParams: Map<String, String>
    ): Call<JsonObject>
    @Headers("Accept: application/json")
    @Multipart
    @POST("updateProfile")
    fun updateProfile(
        @Header("Authorization") authorization: String?,
        @Part files: MultipartBody.Part
    ): Call<JsonObject>

}