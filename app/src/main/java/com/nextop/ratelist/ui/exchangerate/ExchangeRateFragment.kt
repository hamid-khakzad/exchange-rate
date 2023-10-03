package com.nextop.ratelist.ui.exchangerate

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.nextop.ratelist.R
import com.nextop.ratelist.databinding.FragmentCurrencyRateBinding
import com.nextop.ratelist.util.Resource
import com.nextop.ratelist.util.VerticalSpaceItemDecorator
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date

@AndroidEntryPoint
class ExchangeRateFragment : Fragment() {
    private var _binding: FragmentCurrencyRateBinding? = null
    private val binding get() = _binding!!
    private lateinit var exchangeRateAdapter: ExchangeRateAdapter;
    private val exchangeRateViewModel: ExchangeRateViewModel by viewModels()
    private val scheduleApiCallHandler = Handler(Looper.getMainLooper())
    private lateinit var scheduleApiCallRunnable: Runnable
    var dateFormatter = SimpleDateFormat("dd-MMMM-yyyy hh:mm");


    companion object {
        const val API_CALL_DELAY = 2 * 60 * 1000L
    }

    var isLoading = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCurrencyRateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Stop the API call cycle when the Exchange Rate fragment is destroyed
        scheduleApiCallHandler.removeCallbacks(scheduleApiCallRunnable)
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun setupUi() {
        exchangeRateAdapter = ExchangeRateAdapter(requireContext())
        binding.apply {
            currencyRatesRec.apply {
                currencyRatesRec.addItemDecoration(
                    VerticalSpaceItemDecorator(
                        resources.getDimension(
                            R.dimen.exchange_rate_item_vertical_spacing
                        ).toInt()
                    )
                )
                adapter = exchangeRateAdapter
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                setHasFixedSize(true)
            }
        }

        exchangeRateViewModel.exchangeRate.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    isLoading = false
                    it.data?.let { newsResponse ->
                        exchangeRateAdapter.submitList(newsResponse.rates)
                        setLatestUpdateDate()
                    }
                }

                is Resource.Error -> {
                    isLoading = true
                    it.message?.let { message ->
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    }
                }

                is Resource.Loading -> {

                }
            }
        })

        scheduleApiCallRunnable = Runnable {
            exchangeRateViewModel.getExchangeRate()
            scheduleApiCallHandler.postDelayed(scheduleApiCallRunnable, API_CALL_DELAY)
        }

        scheduleApiCallHandler.post(scheduleApiCallRunnable)

    }

    private fun setLatestUpdateDate() {
        binding.latestUpdateDateTv.text = dateFormatter.format(Date())
    }

}