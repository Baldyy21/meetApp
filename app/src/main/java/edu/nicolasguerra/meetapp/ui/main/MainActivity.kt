package edu.nicolasguerra.meetapp.ui.main

import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import edu.nicolasguerra.meetapp.MyRoomApplication
import edu.nicolasguerra.meetapp.R
import edu.nicolasguerra.meetapp.data.MarkerDataSource
import edu.nicolasguerra.meetapp.data.MarkerRepository
import edu.nicolasguerra.meetapp.databinding.ActivityMainBinding
import edu.nicolasguerra.meetapp.ui.fragments.ListFragment
import edu.nicolasguerra.meetapp.ui.fragments.MapFragment
import edu.nicolasguerra.meetapp.utils.utils

class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding



    private val vm: MainViewModel by viewModels {
        val db = (application as MyRoomApplication).markerDB
        val dataSource= MarkerDataSource(db.markersDao())
        val repository= MarkerRepository(dataSource)
        MainViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val btnTab1: Button = binding.btnTab1
        val btnTab2: Button = binding.btnTab2

        btnTab1.setOnClickListener {
            loadFragment(MapFragment(vm))
        }

        btnTab2.setOnClickListener {
            loadFragment(ListFragment(vm))
        }

        if (savedInstanceState == null) {
            loadFragment(MapFragment(vm))
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }


}
