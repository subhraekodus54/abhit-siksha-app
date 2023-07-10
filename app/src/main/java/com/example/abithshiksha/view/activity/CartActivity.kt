package com.example.abithshiksha.view.activity

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.abithshiksha.R
import com.example.abithshiksha.databinding.ActivityCart2Binding
import com.example.abithshiksha.helper.PrefManager
import com.example.abithshiksha.helper.click_listener.SubjectClickListener
import com.example.abithshiksha.model.pojo.get_carts.Cart
import com.example.abithshiksha.model.repo.Outcome
import com.example.abithshiksha.view.adapter.CartsAdapter
import com.example.abithshiksha.view_model.GetCartsViewModel
import com.example.abithshiksha.view_model.RemoveCartViewModel
import com.user.caregiver.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CartActivity : AppCompatActivity(), SubjectClickListener {
    private lateinit var binding: ActivityCart2Binding

    private val mGetCartsViewModel: GetCartsViewModel by viewModel()
    private val mRemoveCartViewModel: RemoveCartViewModel by viewModel()
    private lateinit var accessToken: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCart2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        lightStatusBar(this,R.color.white)

        //get token
        accessToken = "Bearer "+ PrefManager.getKeyAuthToken()
        binding.emptyCart.gone()
        binding.emptyCartHtv.gone()

        binding.backArrow.setOnClickListener {
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        if(isConnectedToInternet()){
            getCarts(accessToken)
        }else{
            Toast.makeText(this,"No internet connection.",Toast.LENGTH_LONG).show()
        }
    }

    private fun fillCartsRecycler(list: List<Cart>) {
        val gridLayoutManager = LinearLayoutManager(this)
        binding.cartRecycler.apply {
            layoutManager = gridLayoutManager
            setHasFixedSize(true)
            isFocusable = false
            adapter = CartsAdapter(list,this@CartActivity, this@CartActivity)
        }
    }

    private fun getCarts(
        token: String,
    ){
        binding.cartRecycler.gone()
        binding.cartShimmerView.visible()
        binding.cartShimmerView.startShimmer()
        mGetCartsViewModel.getCarts(token).observe(this) { outcome ->
            when (outcome) {
                is Outcome.Success -> {
                    if (outcome.data.status == 1) {
                        binding.cartShimmerView.stopShimmer()
                        binding.cartShimmerView.gone()
                        if(outcome.data.result.carts.isNotEmpty()){
                            binding.cartRecycler.visible()
                            binding.emptyCart.gone()
                            binding.emptyCartHtv.gone()
                            fillCartsRecycler(outcome.data.result.carts)
                        }else{
                            binding.emptyCart.visible()
                            binding.cartRecycler.gone()
                            binding.emptyCartHtv.visible()
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

    private fun removeCart(
        id: Int,
        token: String,
    ){
        val loader = this.loadingDialog()
        loader.show()
        mRemoveCartViewModel.removeCart(token,id).observe(this) { outcome ->
            loader.dismiss()
            when (outcome) {
                is Outcome.Success -> {
                    if (outcome.data.status == 1) {
                        Toast.makeText(this, outcome.data.result.message, Toast.LENGTH_SHORT)
                            .show()
                        getCarts(accessToken)
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

    private fun showRemoveDialog(id: Int){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Remove Cart")
        builder.setMessage("Do you want to remove the items from the cart ?")
        builder.setIcon(R.drawable.ic_baseline_warning_24)
        builder.setPositiveButton("Yes"){dialogInterface, which ->
            if(isConnectedToInternet()){
                removeCart(id,accessToken)
            }else{
                Toast.makeText(this,"No internet connection.",Toast.LENGTH_LONG).show()
            }
        }
        builder.setNegativeButton("No"){dialogInterface, which ->
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    override fun onClick(view: View, id: Int) {
        showRemoveDialog(id)
    }

}