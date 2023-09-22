package com.antoniomy.citypoi

import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.antoniomy.citypoi.navigation.CitiesNavigation
import com.antoniomy.citypoi.viewmodel.PoisViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val poisViewModel: PoisViewModel by viewModels()
    @Inject
    lateinit var citiesNavigation : CitiesNavigation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Load Home fragment
        //replaceFragment(DistrictsFragment(poisViewModel), supportFragmentManager)
        citiesNavigation.goToList(poisViewModel, supportFragmentManager)

        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Toast.makeText(applicationContext, R.string.toast_no, Toast.LENGTH_LONG).show()
            }
        })
    }
}