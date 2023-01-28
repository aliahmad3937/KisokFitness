package com.codecoy.fitnesskiodkapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment

import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.codecoy.fitnesskiodkapp.MyApp

import com.codecoy.fitnesskiodkapp.R
import com.codecoy.fitnesskiodkapp.databinding.FragmentNavigationScreenBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NavigationScreen : Fragment() {
    private lateinit var binding: FragmentNavigationScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNavigationScreenBinding.inflate(inflater, container, false)
        val navHostFragment =
           childFragmentManager.findFragmentById(R.id.nav_host_fragment_navigation) as NavHostFragment
       MyApp.bottomNavController = navHostFragment.navController
       MyApp.bottomNav = binding.bottomNav

        binding.bottomNav.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home2 -> MyApp.bottomNavController.navigate(R.id.home2)
                    R.id.classes -> MyApp.bottomNavController.navigate(R.id.classes)
                    R.id.equipment -> MyApp.bottomNavController.navigate(R.id.equipment)

            }
            true
        }





        return binding.root
    }

//
//    val bottomNavBar by lazy {
//        binding.bottomNav
//    }  private fun setUpNavigation() {
//       // val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
//        val navHostFragment =
//           childFragmentManager.findFragmentById(R.id.nav_host_fragment_navigation) as NavHostFragment
//       MyApp.navController = navHostFragment.navController
//        NavigationUI.setupWithNavController(
//            bottomNavBar,
//            MyApp.navController)
//
//
//              MyApp.navController.addOnDestinationChangedListener { _, destination, _ ->
//                when (destination.id) {
//                    R.id.home2 -> MyApp.showToast(requireContext(),"home")
//                    R.id.classes -> MyApp.showToast(requireContext(),"classes")
//                    R.id.equipment -> MyApp.showToast(requireContext(),"equipment")
//                }
//            }
////        val navController = findNavController()
////        navController.setGraph(R.navigation.nav_graph_bottom)
//////        navController.navigate(R.id.action_global_profileTwoFragment)
////
////        NavigationUI.setupWithNavController(
////            binding.bottomNav,
////         navController
////        )
//    }


}