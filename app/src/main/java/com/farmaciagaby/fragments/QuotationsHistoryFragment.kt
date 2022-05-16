package com.farmaciagaby.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.farmaciagaby.R
import com.farmaciagaby.adapters.QuotationsHistoryAdapter
import com.farmaciagaby.databinding.FragmentQuotationsHistoryBinding
import com.farmaciagaby.models.Detalle
import com.farmaciagaby.models.Quotation
import com.farmaciagaby.models.QuotationDetail
import com.farmaciagaby.network.FirebaseHelper
import com.farmaciagaby.viewmodels.ProductViewModel
import com.farmaciagaby.viewmodels.QuotationsViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class QuotationsHistoryFragment : Fragment() {

    private lateinit var binding: FragmentQuotationsHistoryBinding
    private val viewModel: QuotationsViewModel by viewModels()

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
            val adapter = QuotationsHistoryAdapter(quotationsList)
            binding.rvQuotations.adapter = adapter
        })
//        val quotation = Quotation("Bendición y Fé", "30/04/2022");
//        val quotation2 = Quotation("Droguería La Esperanza", "03/03/2022");
//        val quotation3 = Quotation("Promeco", "02/02/2022");
//        val quotation4 = Quotation("Droguería Las Flores", "16/01/2022");
//        val quotation5 = Quotation("Gloria y Esperanza", "07/05/2022");
//        val quotation6 = Quotation("Bendición y Fé", "30/04/2022");
//        val quotation7 = Quotation("Droguería La Esperanza", "03/03/2022");
//        val quotation8 = Quotation("Promeco", "02/02/2022");
//        val quotation9 = Quotation("Droguería Las Flores", "16/01/2022");
//        val quotation10= Quotation("Gloria y Esperanza", "07/05/2022");
//
//        val quotationList = arrayListOf(quotation, quotation2, quotation3, quotation4, quotation5, quotation6, quotation7, quotation8, quotation9, quotation10)
//        val adapter = QuotationsHistoryAdapter(quotationList)
//        binding.rvQuotations.adapter = adapter

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