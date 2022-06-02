package com.farmaciagaby.fragments

import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
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

class QuotationsHistoryFragment : Fragment() {

    private lateinit var binding: FragmentQuotationsHistoryBinding
    private val gson = GsonBuilder().create()
    private val viewModel: QuotationsViewModel by viewModels()
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
        // Show action bar and set title
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.title = resources.getString(R.string.action_bar_quotation_history)
        actionBar?.show()

        // Disable action bar back button
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeButtonEnabled(false)

        // Set up quotations history adapter
        binding.rvQuotations.layoutManager = LinearLayoutManager(context);

        // Get products from Firestore
        viewModel.getAllQuotation().observe(requireActivity(), Observer { quotationsList ->
            mQuotationList = quotationsList
            mAdapter = QuotationsHistoryAdapter(mQuotationList, QuotationsHistoryAdapter.OnClickListener { quotation, view ->
                val arg = gson.toJson(quotation)
                val action = QuotationsHistoryFragmentDirections.actionMainToQuotationDetails(arg)
                Navigation.findNavController(view).navigate(action)
            })
            binding.rvQuotations.adapter = mAdapter
        })

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