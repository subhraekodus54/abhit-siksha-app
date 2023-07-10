package com.example.abithshiksha.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.abithshiksha.R
import com.example.abithshiksha.databinding.ActivityCartBinding
import com.example.abithshiksha.helper.PrefManager
import com.example.abithshiksha.model.pojo.get_cart_details.CartSubjectDetail
import com.example.abithshiksha.model.repo.Outcome
import com.example.abithshiksha.view.adapter.CartSubjectAdapter
import com.example.abithshiksha.view_model.GetCartDetailsViewModel
import com.example.abithshiksha.view_model.GetOrderIdViewModel
import com.example.abithshiksha.view_model.PaymentStatusViewModel
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import com.thekhaeng.pushdownanim.PushDownAnim
import com.user.caregiver.*
import org.json.JSONObject
import org.koin.androidx.viewmodel.ext.android.viewModel

class CartDetailsActivity : AppCompatActivity(), PaymentResultListener {
    private lateinit var binding: ActivityCartBinding
    private lateinit var accessToken: String
    private var id: Int = 0
    private var total_amount: Int = 0
    private var order_id = ""
    private var api_key = ""

    private val mGetCartDetailsViewModel: GetCartDetailsViewModel by viewModel()
    private val mGetOrderIdViewModel: GetOrderIdViewModel by viewModel()
    private val mPaymentStatusViewModel: PaymentStatusViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lightStatusBar(this,R.color.white)

        binding.mainLay.gone()
        binding.cartDetailsShimmerView.startShimmer()

        val extras = intent.extras
        if (extras != null) {
            val cart_id = intent?.getIntExtra("id",0)
            id = cart_id!!
        }

        //get token
        accessToken = "Bearer "+ PrefManager.getKeyAuthToken()

        binding.backArrow.setOnClickListener {
            finish()
        }
        PushDownAnim.setPushDownAnimTo(binding.proceedBtn).setOnClickListener {
            val amount = total_amount*100
            if(isConnectedToInternet()){
                if(amount.toString().toDouble() != 0.00){
                    getOrderId(accessToken,id,amount)
                }else{
                    Toast.makeText(this,"Sorry, Can not proceed.",Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(this,"No internet connection.",Toast.LENGTH_LONG).show()
            }
        }
        if(isConnectedToInternet()){
            getCartDetails(accessToken,id)
        }else{
            Toast.makeText(this,"No internet connection.",Toast.LENGTH_LONG).show()
        }
    }

    private fun fillCartRecyclerView(list: List<CartSubjectDetail>, board: String, standard: String) {
        val gridLayoutManager = GridLayoutManager(this, 2)
        binding.subjectRecycler.apply {
            layoutManager = gridLayoutManager
            setHasFixedSize(true)
            isFocusable = false
            adapter = CartSubjectAdapter(list,this@CartDetailsActivity,board,standard)
        }
    }

    private fun getCartDetails(
        token: String,
        cart_id: Int
    ){
        mGetCartDetailsViewModel.getCartDetails(token,cart_id).observe(this) { outcome ->
            when (outcome) {
                is Outcome.Success -> {
                    if (outcome.data.status == 1) {
                        binding.mainLay.visible()
                        binding.cartDetailsShimmerView.stopShimmer()
                        binding.cartDetailsShimmerView.gone()

                        fillCartRecyclerView(
                            outcome.data.result.carts.cart_subject_details,
                            outcome.data.result.carts.board,
                            outcome.data.result.carts.class_name
                        )

                        outcome.data.result.carts.total_amount?.let {
                            binding.coursePriceTv.text = "₹"+it.toString()
                            binding.totalTv.text = "₹"+it.toString()
                            total_amount = it
                        }

                    } else {
                        Toast.makeText(this, outcome.data.result.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                is Outcome.Failure<*> -> {
                    Toast.makeText(this, R.string.network_issue, Toast.LENGTH_SHORT).show()

                    Log.i("statusMsg", outcome.e.message.toString())

                    outcome.e.printStackTrace()
                    Log.i("status", outcome.e.cause.toString())
                }
            }
        }
    }

    private fun getOrderId(
        token: String,
        cart_id: Int,
        amount: Int
    ){
        val loader = this.loadingDialog(false,R.raw.processing)
        loader.show()
        mGetOrderIdViewModel.getOrderId(token,cart_id).observe(this) { outcome ->
            loader.dismiss()
            when (outcome) {
                is Outcome.Success -> {
                    if (outcome.data.status == 1) {
                        order_id = outcome.data.result.data.razorpayOrderId
                        api_key = outcome.data.result.data.razorpayKey
                        startPayment(order_id,api_key,amount.toString())
                    }else{
                        Toast.makeText(this, outcome.data.result.message, Toast.LENGTH_SHORT).show()
                    }
                }
                is Outcome.Failure<*> -> {
                    Toast.makeText(this, R.string.network_issue, Toast.LENGTH_SHORT).show()

                    Log.i("statusMsg", outcome.e.message.toString())

                    outcome.e.printStackTrace()
                    Log.i("status", outcome.e.cause.toString())
                }
            }
        }
    }

    private fun paymentStatus(
        cart_id: Int,
        razorpay_order_id: String,
        razorpay_payment_id: String,
        token: String,
    ){
        val loader = this.loadingDialog(false,R.raw.processing)
        loader.show()
        mPaymentStatusViewModel.paymentStatus(cart_id, razorpay_order_id, razorpay_payment_id, token).observe(this) { outcome ->
            loader.dismiss()
            when (outcome) {
                is Outcome.Success -> {
                    if (outcome.data.status == 1) {
                        Toast.makeText(this, outcome.data.result.message, Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, PaymentStatusActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    }else{
                        Toast.makeText(this, outcome.data.result.message, Toast.LENGTH_SHORT).show()
                    }
                }
                is Outcome.Failure<*> -> {
                    Toast.makeText(this, R.string.network_issue, Toast.LENGTH_SHORT).show()

                    Log.i("statusMsg", outcome.e.message.toString())

                    outcome.e.printStackTrace()
                    Log.i("status", outcome.e.cause.toString())
                }
            }
        }
    }

    private fun startPayment(orderId: String, apiKey: String, amount: String) {
        val checkout = Checkout()
        checkout.setKeyID(apiKey)
        checkout.setImage(R.mipmap.ic_logo)
        val activity: Activity = this
        try {
            val options = JSONObject()
            options.put("name", R.string.app_name)
            options.put("description", "Payment for Anything")
            options.put("order_id", orderId);//from response of step 3.
            options.put("send_sms_hash", true)
            options.put("allow_rotation", false)

            //You can omit the image option to fetch the image from dashboard
            options.put("currency", "INR")
            options.put("amount", amount)
            val preFill = JSONObject()
            preFill.put("email", " ")
            preFill.put("contact", " ")
            options.put("prefill", preFill)
            checkout.open(activity, options)
        } catch (e: Exception) {
            Toast.makeText(activity, "Error in payment: " + e.message, Toast.LENGTH_SHORT)
                .show()
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(p0: String?) {
        if (p0 != null) {
            paymentStatus(id,order_id,p0,accessToken)
        }
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(this, "Payment error: " + p1, Toast.LENGTH_LONG)
            .show()
    }
}