package com.antoniomy.citypoi.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.antoniomy.citypoi.R
import com.antoniomy.citypoi.home.HomeDistrictFragment
import com.antoniomy.citypoi.viewmodel.PoisViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val poisViewModel: PoisViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Load Home fragment
        replaceFragment(HomeDistrictFragment(poisViewModel), supportFragmentManager)
    }

    //Disable automatic back button
    @Deprecated("Deprecated in Java", ReplaceWith("Toast.makeText(this, R.string.toast_no, Toast.LENGTH_LONG).show()",
            "android.widget.Toast", "com.antoniomy82.mycities.ui.R", "android.widget.Toast"))
    override fun onBackPressed() {
        Toast.makeText(this, R.string.toast_no, Toast.LENGTH_LONG).show()
    }
}