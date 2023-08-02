package com.example.abithshiksha

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.onNavDestinationSelected
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.example.abithshiksha.databinding.ActivityMainBinding
import com.example.abithshiksha.helper.PrefManager
import com.example.abithshiksha.model.network.ApiConstants
import com.example.abithshiksha.model.repo.Outcome
import com.example.abithshiksha.view.activity.CartActivity
import com.example.abithshiksha.view.activity.NotificationActivity
import com.example.abithshiksha.view.activity.ProfileActivity
import com.example.abithshiksha.view_model.GetProfileViewModel
import com.google.android.material.navigation.NavigationView
import com.google.firebase.messaging.FirebaseMessaging
import com.thekhaeng.pushdownanim.PushDownAnim
import com.user.caregiver.gone
import com.user.caregiver.lightStatusBar
import com.user.caregiver.visible
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.google.android.gms.tasks.OnCompleteListener


class MainActivity : AppCompatActivity(){

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val mGetProfileViewModel: GetProfileViewModel by viewModel()
    private lateinit var accessToken: String
    private var CHANNEL_ID = "101"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lightStatusBar(this,R.color.white)

        //checkInternetConnection()

        //get token
        accessToken = "Bearer "+ PrefManager.getKeyAuthToken()

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.myCoursesFragment, R.id.coursesFragment
            ), drawerLayout
        )

        navController.addOnDestinationChangedListener {
                controller, destination, arguments ->
            binding.appBarMain.headerTv.text = navController.currentDestination?.label
        }

        navView.setupWithNavController(navController)

        binding.appBarMain.navBtn.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        val navigationView : NavigationView  = findViewById(R.id.nav_view)
        val headerView : View = navigationView.getHeaderView(0)
        val imageview : ImageView = headerView.findViewById(R.id.imageView)

        navigationView.setNavigationItemSelectedListener { menuItem ->
            val id = menuItem.itemId
            //it's possible to do more actions on several items, if there is a large amount of items I prefer switch(){case} instead of if()
            if (id == R.id.bolgFragment) {
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse("http://abhithsiksha.com/blog")
                startActivity(i)
            }
            //This is for maintaining the behavior of the Navigation view
            onNavDestinationSelected(menuItem, navController)
            //This is for closing the drawer after acting on it
            //drawer.closeDrawer(GravityCompat.START)
            binding.drawerLayout.closeDrawer(GravityCompat.START)

            true
        }

        imageview.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        binding.appBarMain.notificationBtn.setOnClickListener {
            val intent = Intent(this, NotificationActivity::class.java)
            startActivity(intent)
        }

        binding.appBarMain.cartImg.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }

        PushDownAnim.setPushDownAnimTo(binding.appBarMain.hcCheckoutBtn).setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }

        //notification
        createNotificationChannel()
        getToken()
        subscribeToTopic()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onResume() {
        super.onResume()
        val navigationView : NavigationView  = findViewById(R.id.nav_view)
        val headerView : View = navigationView.getHeaderView(0)
        val nameTv : TextView = headerView.findViewById(R.id.name_tv)
        val emailTv : TextView = headerView.findViewById(R.id.email_tv)
        val imageview : ImageView = headerView.findViewById(R.id.imageView)

        getProfile(nameTv,emailTv,imageview,accessToken)

    }

    private fun getProfile(
        nameTv: TextView,
        emailTv: TextView,
        imageView: ImageView,
        token: String
    ){
        binding.appBarMain.cartItemCountTv.gone()
        binding.appBarMain.proceedLayout.gone()
        mGetProfileViewModel.getProfile(token).observe(this) { outcome ->
            when (outcome) {
                is Outcome.Success -> {
                    if (outcome.data.status == 1) {

                        if(outcome.data.result.result.user_details!=null){
                            Glide.with(this)
                                .load(ApiConstants.PUBLIC_URL+outcome.data.result.result.user_details.image) // image url
                                .placeholder(R.color.color_grey) // any placeholder to load at start
                                .centerCrop()
                                .into(imageView)

                            nameTv.text = outcome.data.result.result.user_details.name
                            emailTv.text = outcome.data.result.result.user_details.email

                            if(outcome.data.result.result.cart_total_count != 0){
                                binding.appBarMain.cartItemCountTv.visible()
                                //binding.appBarMain.proceedLayout.visible()
                                binding.appBarMain.cartItemCountTv.text = outcome.data.result.result.cart_total_count.toString()
                                /*if(outcome.data.result.result.cart_total_count>1){
                                    binding.appBarMain.hcItemCountTv.text = outcome.data.result.result.cart_total_count.toString()+" Subjects"
                                }else{
                                    binding.appBarMain.hcItemCountTv.text = outcome.data.result.result.cart_total_count.toString()+" Subject"
                                }*/
                            }else{
                                binding.appBarMain.cartItemCountTv.gone()
                                binding.appBarMain.proceedLayout.gone()
                            }
                        }else{
                            Toast.makeText(this, outcome.data.result.message, Toast.LENGTH_SHORT)
                                .show()
                        }

                    } else {
                        Toast.makeText(this, outcome.data.result.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                is Outcome.Failure<*> -> {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT)
                        .show()
                    Log.i("statusMsg", outcome.e.message.toString())

                    outcome.e.printStackTrace()
                    Log.i("status", outcome.e.cause.toString())
                }
            }
        }
    }

    //notification subscribe
    private fun subscribeToTopic(){
        FirebaseMessaging.getInstance().subscribeToTopic("cloud")
            .addOnCompleteListener { task ->
                var msg = "Done"
                if (!task.isSuccessful) {
                    msg = "Failed"
                }
            }
    }

    //get token
    private fun getToken(){
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("Token", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            //val msg = getString(R.string.msg_token_fmt, token)
            Log.e("Token", token)
            //Toast.makeText(baseContext, token, Toast.LENGTH_SHORT).show()
        })
    }

    private fun createNotificationChannel() {

        NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_circle_notifications_24)
            .setContentTitle("textTitle")
            .setContentText("textContent")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "firebaseNotifChannel"
            val descriptionText = "this is a channel to receive firebase notification."
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

        }
    }
}