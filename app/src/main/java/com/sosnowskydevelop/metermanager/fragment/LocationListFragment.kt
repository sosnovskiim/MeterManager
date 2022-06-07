package com.sosnowskydevelop.metermanager.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sosnowskydevelop.metermanager.*
import com.sosnowskydevelop.metermanager.databinding.LocationListFragmentBinding
import com.sosnowskydevelop.metermanager.viewmodel.LocationViewModel
import com.sosnowskydevelop.metermanager.viewmodel.LocationViewModelFactory

class LocationListFragment : Fragment() {
    private lateinit var binding: LocationListFragmentBinding

    private lateinit var auth: FirebaseAuth

    private val locationViewModel: LocationViewModel by viewModels {
        LocationViewModelFactory((activity?.application as MetersApplication).locationRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        auth = Firebase.auth
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = LocationListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.locationRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        val locationAdapter = LocationListAdapter()
        binding.locationRecyclerview.adapter = locationAdapter

        locationViewModel.allLocations.observe(this, Observer { locations ->
            locations?.let { locationAdapter.submitList(it) }
        })

        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.title = resources.getString(R.string.locations_title)
        actionBar?.setDisplayHomeAsUpEnabled(false)

        if (Firebase.auth.currentUser == null) {
            createAndShowSavingDataDialog()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.location_list_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.location_menu_add -> {
                findNavController().navigate(LocationListFragmentDirections.actionLocationListFragmentToLocationDetailsFragment(0))
                true
            }
            R.id.location_menu_saving_data -> {
                startLoginActivity()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun createAndShowSavingDataDialog() {
        lateinit var dialog: AlertDialog
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(resources.getString(R.string.login_dialog_title))
        builder.setMessage(resources.getString(R.string.login_dialog_message))
        val dialogClickListener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> startLoginActivity()
                DialogInterface.BUTTON_NEGATIVE -> dialog.dismiss()
            }
        }
        builder.setPositiveButton(
            resources.getString(R.string.login_dialog_positive),
            dialogClickListener
        )
        builder.setNegativeButton(
            resources.getString(R.string.login_dialog_negative),
            dialogClickListener
        )
        dialog = builder.create()
        dialog.show()
    }

    private fun startLoginActivity() {
        startActivity(Intent(requireContext(), LoginActivity::class.java))
        requireActivity().finish()
    }
}
