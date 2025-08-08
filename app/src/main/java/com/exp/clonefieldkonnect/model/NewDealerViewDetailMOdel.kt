package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.SerializedName

 class NewDealerViewDetailMOdel {

     @SerializedName("status")
     var status: String? = null

     @SerializedName("message")
     var message: String? = null

     @SerializedName("data")
     var data: Data? = Data()


     class Data {

         @SerializedName("id")
         var id: Int? = null

         @SerializedName("branch")
         var branch: String? = null

         @SerializedName("created_by")
         var createdBy: String? = null

         @SerializedName("division")
         var division: String? = null

         @SerializedName("customertype")
         var customertype: String? = null

         @SerializedName("firm_name")
         var firmName: String? = null

         @SerializedName("contact_person")
         var contactPerson: String? = null

         @SerializedName("mobile_email")
         var mobileEmail: String? = null

         @SerializedName("district")
         var district: String? = null

         @SerializedName("city")
         var city: String? = null

         @SerializedName("place")
         var place: String? = null

         @SerializedName("appointment_date")
         var appointmentDate: String? = null

         @SerializedName("security_deposit")
         var securityDeposit: String? = null

         @SerializedName("gst_details")
         var gstDetails: String? = null

         @SerializedName("firm_type")
         var firmType: String? = null

         @SerializedName("payment_term")
         var paymentTerm: String? = null

         @SerializedName("credit_period")
         var creditPeriod: String? = null

         @SerializedName("present_annual_turnover")
         var presentAnnualTurnover: String? = null

         @SerializedName("credit_limit")
         var creditLimit: String? = null

         @SerializedName("motor_anticipated_business")
         var motorAnticipatedBusiness: String? = null

         @SerializedName("motor_next_year_business")
         var motorNextYearBusiness: String? = null

         @SerializedName("pump_anticipated_business")
         var pumpAnticipatedBusiness: String? = null

         @SerializedName("pump_next_year_business")
         var pumpNextYearBusiness: String? = null

         @SerializedName("F&A_anticipated_business")
         var FAAnticipatedBusiness: String? = null

         @SerializedName("F&A_next_year_business")
         var FANextYearBusiness: String? = null

         @SerializedName("lighting_anticipated_business")
         var lightingAnticipatedBusiness: String? = null

         @SerializedName("lighting_next_year_business")
         var lightingNextYearBusiness: String? = null

         @SerializedName("agri_anticipated_business")
         var agriAnticipatedBusiness: String? = null

         @SerializedName("agri_next_year_business")
         var agriNextYearBusiness: String? = null

         @SerializedName("solar_anticipated_business")
         var solarAnticipatedBusiness: String? = null

         @SerializedName("solar_next_year_business")
         var solarNextYearBusiness: String? = null

         @SerializedName("manufacture_company_1")
         var manufactureCompany1: String? = null

         @SerializedName("manufacture_product_1")
         var manufactureProduct1: String? = null

         @SerializedName("manufacture_business_1")
         var manufactureBusiness1: String? = null

         @SerializedName("manufacture_turn_over_1")
         var manufactureTurnOver1: String? = null

         @SerializedName("manufacture_company_2")
         var manufactureCompany2: String? = null

         @SerializedName("manufacture_product_2")
         var manufactureProduct2: String? = null

         @SerializedName("manufacture_business_2")
         var manufactureBusiness2: String? = null

         @SerializedName("manufacture_turn_over_2")
         var manufactureTurnOver2: String? = null

         @SerializedName("approval_status")
         var approvalStatus: Int? = null

         @SerializedName("bm_remark")
         var bm_remark: String? = null


     }
 }