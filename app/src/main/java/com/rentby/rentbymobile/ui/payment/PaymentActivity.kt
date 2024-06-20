package com.rentby.rentbymobile.ui.payment

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback
import com.midtrans.sdk.corekit.core.MidtransSDK
import com.midtrans.sdk.corekit.core.UIKitCustomSetting
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme
import com.midtrans.sdk.corekit.models.snap.TransactionResult
import com.midtrans.sdk.uikit.SdkUIFlowBuilder
import com.rentby.rentbymobile.R
import com.rentby.rentbymobile.databinding.ActivityPaymentBinding
import com.rentby.rentbymobile.utils.midtransConfig

class PaymentActivity : AppCompatActivity(), TransactionFinishedCallback {
    private lateinit var binding: ActivityPaymentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initMidtransSdk()

        val snapTokenValue: String? = intent.getStringExtra(SNAP_TOKEN)
        MidtransSDK.getInstance().startPaymentUiFlow(this@PaymentActivity, snapTokenValue)
        finish()
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
                TransactionResult.STATUS_SUCCESS -> {
                    Toast.makeText(this, "Transaction Finished", Toast.LENGTH_LONG).show()
                }
                TransactionResult.STATUS_PENDING -> {
                    Toast.makeText(this, "Transaction Pending", Toast.LENGTH_LONG).show()
                }
                TransactionResult.STATUS_FAILED -> Toast.makeText(this, "Transaction Failed" + ". Message: " + result.response.statusMessage, Toast.LENGTH_LONG).show()
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
        const val SNAP_TOKEN = "snap_token"
    }
}