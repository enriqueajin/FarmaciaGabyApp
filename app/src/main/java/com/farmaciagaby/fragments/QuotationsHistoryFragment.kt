package com.farmaciagaby.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.farmaciagaby.R
import com.farmaciagaby.adapters.QuotationsHistoryAdapter
import com.farmaciagaby.databinding.FragmentQuotationsHistoryBinding
import com.farmaciagaby.models.Quotation

class QuotationsHistoryFragment : Fragment() {

    private lateinit var binding: FragmentQuotationsHistoryBinding

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

    private fun setData() {
        // Show action bar
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.show()

        // Disable action bar back button
        actionBar?.setDisplayHomeAsUpEnabled(false)
        actionBar?.setHomeButtonEnabled(false)

        // Set up quotations history adapter
        binding.rvQuotations.layoutManager = LinearLayoutManager(context);

        val quotation = Quotation("Bendición y Fé", "30/04/2022");
        val quotation2 = Quotation("Droguería La Esperanza", "03/03/2022");
        val quotation3 = Quotation("Promeco", "02/02/2022");
        val quotation4 = Quotation("Droguería Las Flores", "16/01/2022");
        val quotation5 = Quotation("Gloria y Esperanza", "07/05/2022");
        val quotation6 = Quotation("Bendición y Fé", "30/04/2022");
        val quotation7 = Quotation("Droguería La Esperanza", "03/03/2022");
        val quotation8 = Quotation("Promeco", "02/02/2022");
        val quotation9 = Quotation("Droguería Las Flores", "16/01/2022");
        val quotation10= Quotation("Gloria y Esperanza", "07/05/2022");

        val quotationList = arrayListOf(quotation, quotation2, quotation3, quotation4, quotation5, quotation6, quotation7, quotation8, quotation9, quotation10)
        val adapter = QuotationsHistoryAdapter(quotationList)
        binding.rvQuotations.adapter = adapter
    }
}