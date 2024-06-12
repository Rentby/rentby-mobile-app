package com.rentby.rentbymobile.ui.order

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback
import com.midtrans.sdk.corekit.core.MidtransSDK
import com.midtrans.sdk.corekit.core.TransactionRequest
import com.midtrans.sdk.corekit.core.UIKitCustomSetting
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme
import com.midtrans.sdk.corekit.models.CustomerDetails
import com.midtrans.sdk.corekit.models.ItemDetails
import com.midtrans.sdk.corekit.models.snap.Gopay
import com.midtrans.sdk.corekit.models.snap.Shopeepay
import com.midtrans.sdk.corekit.models.snap.TransactionResult
import com.midtrans.sdk.uikit.SdkUIFlowBuilder
import com.rentby.rentbymobile.R
import com.rentby.rentbymobile.databinding.ActivityOrderDetailBinding
import com.rentby.rentbymobile.utils.midtransConfig
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

class OrderDetailActivity : AppCompatActivity(), TransactionFinishedCallback {
    private lateinit var binding: ActivityOrderDetailBinding
    private val viewModel: OrderDetailViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityOrderDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val orderId = intent.getStringExtra(ORDER_ID) ?: ""
        viewModel.getOrderDetail(orderId)

        setupView()
        setupAction()
        initMidtransSdk()
    }

    private fun setupAction(){
        binding.arrowLeft.setOnClickListener {
            finish()
        }

        binding.buttonPayNow.setOnClickListener {
            val snapTokenValue: String = "4ab8ed5e-f62f-4ec6-a46b-e004424e2c0b"
            MidtransSDK.getInstance().startPaymentUiFlow(this@OrderDetailActivity, snapTokenValue)
        }
    }

    private fun setupView(){
        viewModel.orderDetail.observe(this) { order ->
            order?.let {
                val rentStartDate = formatDate(order.rentStart)
                val rentEndDate = formatDate(order.rentEnd)
                val rentDate = "$rentStartDate - $rentEndDate"

                val rentPricePerDay = order.rentPrice.toInt()
                val formattedRentPricePerDay = NumberFormat.getNumberInstance(Locale("id", "ID")).format(rentPricePerDay)
                val orderPrice = "${order.rentDuration} x $formattedRentPricePerDay"

                val formattedTotalRentPrice = "Rp" + NumberFormat.getNumberInstance(Locale("id", "ID")).format(order.rentTotal) // total harga rental
                val formattedDeposit = "Rp" + NumberFormat.getNumberInstance(Locale("id", "ID")).format(order.deposit)
                val formattedServiceFee = "Rp" + NumberFormat.getNumberInstance(Locale("id", "ID")).format(order.serviceFee.toInt())


                val totalAmount = order.rentTotal + order.serviceFee + order.deposit
                val orderTotal = "Rp" + NumberFormat.getNumberInstance(Locale("id", "ID")).format(totalAmount)

                val lateCharge = "${order.lateDuration} x $formattedRentPricePerDay = "
                val formattedTotalCharge = "Rp" + NumberFormat.getNumberInstance(Locale("id", "ID")).format(order.lateCharge)

                val status = when (order.status) {
                    1 -> {
                        binding.layoutPickUpTime.visibility = View.GONE
                        binding.layoutReturnTime.visibility = View.GONE
                        binding.layoutPickUpLocation.visibility = View.GONE
                        binding.layoutReview.visibility = View.GONE
                        "Waiting for Payment"
                    }
                    2 -> {
                        binding.layoutPickUpTime.visibility = View.GONE
                        binding.layoutReturnTime.visibility = View.GONE
                        binding.layoutReview.visibility = View.GONE
                        binding.buttonPayNow.visibility = View.GONE
                        "Order Ready for Pickup"
                    }
                    3 -> {
                        binding.layoutReturnTime.visibility = View.GONE
                        binding.layoutReview.visibility = View.GONE
                        binding.buttonPayNow.visibility = View.GONE
                        "Order is in Renting Date"
                    }
                    4 -> {
                        binding.layoutReturnTime.visibility = View.GONE
                        binding.layoutReview.visibility = View.GONE
                        binding.buttonPayNow.visibility = View.GONE
                        binding.cardLateCharge.visibility = View.VISIBLE
                        "Order Needs to be Returned"
                    }
                    5 -> {
                        binding.buttonPayNow.visibility = View.GONE
                        if(order.isRated){ binding.layoutReview.visibility = View.GONE }
                        "Order Finished"
                    }
                    else -> "Unknown Status"
                }

                // Update UI with order details
                binding.tvOrderId.text = order.id
                binding.tvOrderStatus.text = status
                binding.tvOrderDate.text = order.orderTime
                binding.tvPickUpDatetime.text = order.pickupTime
                binding.tvReturnDatetime.text = order.returnTime
                binding.tvPayment.text = order.payment

                binding.tvSellerName.text = order.sellerName
                binding.tvProductName.text = order.productName
                binding.tvRentDaterange.text = rentDate
                binding.tvOrderPrice.text = orderPrice

                binding.tvPickUpLocation.text = order.pickupLocation

                binding.tvPriceTotal.text = formattedTotalRentPrice
                binding.tvServiceFee.text = formattedServiceFee
                binding.tvDeposit.text = formattedDeposit
                binding.tvOrderTotal.text = orderTotal

                binding.tvCharge.text = lateCharge
                binding.tvTotalCharge.text = formattedTotalCharge

                order.image?.let {
                    binding.imageProduct.setImageResource(it)
                }
            }
        }
    }
    private fun formatDate(dateString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        return outputFormat.format(date)
    }

    private fun initTransactionRequest(): TransactionRequest {
        // Create new Transaction Request
        val transactionRequestNew = TransactionRequest(System.currentTimeMillis().toString() + "", 36500.0)
        transactionRequestNew.customerDetails = initCustomerDetails()
        transactionRequestNew.gopay = Gopay("mysamplesdk:://midtrans")
        transactionRequestNew.shopeepay = Shopeepay("mysamplesdk:://midtrans")

        val itemDetails1 = ItemDetails("ITEM_ID_1", 36500.0, 1, "ITEM_NAME_1")
        val itemDetailsList = ArrayList<ItemDetails>()
        itemDetailsList.add(itemDetails1)
        transactionRequestNew.itemDetails = itemDetailsList
        return transactionRequestNew
    }

    private fun initCustomerDetails(): CustomerDetails {
        //define customer detail (mandatory for coreflow)
        val mCustomerDetails = CustomerDetails()
        mCustomerDetails.phone = "085310102020"
        mCustomerDetails.firstName = "user fullname"
        mCustomerDetails.email = "mail@mail.com"
        mCustomerDetails.customerIdentifier = "mail@mail.com"
        return mCustomerDetails
    }

    private fun initMidtransSdk() {
        val clientKey: String = midtransConfig.MERCHANT_CLIENT_KEY
        val baseUrl: String = midtransConfig.MERCHANT_BASE_CHECKOUT_URL
        val sdkUIFlowBuilder: SdkUIFlowBuilder = SdkUIFlowBuilder.init()
            .setClientKey(clientKey) // client_key is mandatory
            .setContext(this) // context is mandatory
            .setTransactionFinishedCallback(this) // set transaction finish callback (sdk callback)
            .setMerchantBaseUrl(baseUrl) //set merchant url
            .enableLog(true) // enable sdk log
            .setColorTheme(CustomColorTheme("#FFE51255", "#B61548", "#FFE51255")) // will replace theme on snap theme on MAP
            .setLanguage("en")
        sdkUIFlowBuilder.buildSDK()
        uiKitCustomSetting()
    }

    private fun uiKitCustomSetting() {
        val uIKitCustomSetting = UIKitCustomSetting()
        uIKitCustomSetting.setSaveCardChecked(true)
        MidtransSDK.getInstance().setUiKitCustomSetting(uIKitCustomSetting)
    }

    override fun onTransactionFinished(result: TransactionResult) {
        if (result.response != null) {
            when (result.status) {
                TransactionResult.STATUS_SUCCESS -> Toast.makeText(this, "Transaction Finished. ID: " + result.response.transactionId, Toast.LENGTH_LONG).show()
                TransactionResult.STATUS_PENDING -> Toast.makeText(this, "Transaction Pending. ID: " + result.response.transactionId, Toast.LENGTH_LONG).show()
                TransactionResult.STATUS_FAILED -> Toast.makeText(this, "Transaction Failed. ID: " + result.response.transactionId.toString() + ". Message: " + result.response.statusMessage, Toast.LENGTH_LONG).show()
            }
        } else if (result.isTransactionCanceled) {
            Toast.makeText(this, "Transaction Canceled", Toast.LENGTH_LONG).show()
        } else {
            if (result.status.equals(TransactionResult.STATUS_INVALID, true)) {
                Toast.makeText(this, "Transaction Invalid", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Transaction Finished with failure.", Toast.LENGTH_LONG).show()
            }
        }
    }

    companion object {
        const val ORDER_ID = "order_id"
    }
}