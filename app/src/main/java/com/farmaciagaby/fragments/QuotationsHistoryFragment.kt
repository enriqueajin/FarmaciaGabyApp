package com.farmaciagaby.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.farmaciagaby.R
import com.farmaciagaby.adapters.QuotationsHistoryAdapter
import com.farmaciagaby.databinding.FragmentQuotationsHistoryBinding
import com.farmaciagaby.models.Detalle
import com.farmaciagaby.network.FirebaseHelper
import com.farmaciagaby.viewmodels.QuotationsViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.GsonBuilder
import kotlinx.coroutines.launch

class QuotationsHistoryFragment : BaseFragment() {

    private lateinit var binding: FragmentQuotationsHistoryBinding
    private val gson = GsonBuilder().create()
    private val quotationsViewModel: QuotationsViewModel by viewModels()
    private lateinit var mAdapter: QuotationsHistoryAdapter
    private var mQuotationList = mutableListOf<Detalle>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQuotationsHistoryBinding.inflate(inflater, container, false);
        setData();
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Handle back button in current Fragment
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showSignOutDialog()
            }
        })
    }

    private fun setData() {
        // Get user uid
        val sharedPref = activity?.getSharedPreferences(
            resources.getString(R.string.preference_file_key),
            Context.MODE_PRIVATE
        )
        val userUid = sharedPref?.getString(
            getString(R.string.uid_key),
            "uid"
        )

        Log.d("TAG", "user uid from main is: $userUid")

        // Show action bar and set title
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.title = resources.getString(R.string.action_bar_quotation_history)
        actionBar?.show()

        // Disable action bar back button
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeButtonEnabled(false)

        // Set up quotations history adapter
        binding.rvQuotations.layoutManager = LinearLayoutManager(context);

        showLoadingDialog()

        viewLifecycleOwner.lifecycleScope.launch {

            // Get products from Firestore
            quotationsViewModel.getAllQuotation()
            quotationsViewModel.allQuotationsData.observe(requireActivity()) { quotationsList ->
                Log.d("TAG", "setData: observing with coroutines $quotationsList")
                mQuotationList = quotationsList
                mAdapter = QuotationsHistoryAdapter(mQuotationList, QuotationsHistoryAdapter.OnClickListener { quotation, view ->
                    val arg = gson.toJson(quotation)
                    val action = QuotationsHistoryFragmentDirections.actionMainToQuotationDetails(arg)
                    Navigation.findNavController(view).navigate(action)
                })
                binding.rvQuotations.adapter = mAdapter
                hideLoadingDialog()
            }
        }

        binding.fabNewQuotation.setOnClickListener { view ->
            Navigation.findNavController(view).navigate(R.id.action_quotationsHistoryFragment_to_main_graph)
        }
    }

    private fun showSignOutDialog() {
        val dialog = MaterialAlertDialogBuilder(requireActivity())
            .setTitle("Cerrar sesión")
            .setMessage("¿Está seguro que desea cerrar sesión?")
            .setPositiveButton("Aceptar") { dialogInterface, i ->
                FirebaseHelper.loggedOut()
                Navigation.findNavController(binding.root).navigate(R.id.action_main_to_login_activity)
                requireActivity().finish()
            }
            .setNegativeButton("Cancelar") { dialogInterface, i -> }
            .show()
    }
}