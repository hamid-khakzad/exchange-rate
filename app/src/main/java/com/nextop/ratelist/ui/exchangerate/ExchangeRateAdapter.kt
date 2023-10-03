package com.nextop.ratelist.ui.exchangerate

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nextop.ratelist.R
import com.nextop.ratelist.databinding.ItemCurrencyRateBinding
import com.nextop.ratelist.data.local.ExchangeRate
import com.nextop.ratelist.util.fractionFormat

class ExchangeRateAdapter(private val context: Context) :
    ListAdapter<ExchangeRate, ExchangeRateAdapter.ExchangeRateVh>(ExchangeRateDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExchangeRateVh {
        val binding =
            ItemCurrencyRateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExchangeRateVh(binding)
    }

    override fun onBindViewHolder(holder: ExchangeRateVh, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    override fun onBindViewHolder(
        holder: ExchangeRateVh,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()) {
            val bundle = payloads[0] as Bundle
            holder.update(bundle)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    inner class ExchangeRateVh(private val binding: ItemCurrencyRateBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
        }

        fun bind(exchangeRate: ExchangeRate) {
            binding.apply {
                pairImg.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        context.resources.getIdentifier(
                            exchangeRate.symbol.lowercase(), "drawable", context.packageName
                        )
                    )
                )

                binding.pairSymbolTv.text = exchangeRate.symbol.run {
                    "${this.substring(0, 3)}/${this.substring(3, 6)}"
                }

                binding.pairPriceTv.text = exchangeRate.price.fractionFormat(4)

            }
        }

        fun update(bundle: Bundle) {
            if (bundle.containsKey(ExchangeRateDiffCallback.PRICE_CHANGE_INDICATOR_KEY)) {
                val priceChangeStatus =
                    bundle.getInt(ExchangeRateDiffCallback.PRICE_CHANGE_INDICATOR_KEY)
                showPriceChangeIndicator(priceChangeStatus)
                changePriceTextColor(priceChangeStatus)
            }

            if (bundle.containsKey(ExchangeRateDiffCallback.PRICE_KEY)) {
                val price = bundle.getDouble(ExchangeRateDiffCallback.PRICE_KEY)
                binding.pairPriceTv.text = price.fractionFormat(4)
            }
        }

        private fun changePriceTextColor(priceChangeStatus: Int) {
            when  {
                priceChangeStatus > 0-> {
                    // Exchange rate increased
                    binding.pairPriceTv.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.price_dec_color
                        )
                    )
                }

                priceChangeStatus < 0-> {
                    // Exchange rate decreased
                    binding.pairPriceTv.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.price_inc_color
                        )
                    )
                }
                else -> {
                    // Exchange rate not changed
                    binding.pairPriceTv.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.price_default_color
                        )
                    )
                }
            }
        }

        private fun showPriceChangeIndicator(priceChangeStatus: Int) {
            when {
                priceChangeStatus > 0 -> {
                    // Exchange rate increased
                    binding.priceChangeIndicatorImg.setImageResource(R.drawable.ic_price_dec)
                    binding.priceChangeIndicatorImg.visibility = View.VISIBLE
                }

                priceChangeStatus < 0 -> {
                    // Exchange rate decreased
                    binding.priceChangeIndicatorImg.setImageResource(R.drawable.ic_price_inc)
                    binding.priceChangeIndicatorImg.visibility = View.VISIBLE
                }

                else->{
                    // Exchange rate not changed
                    binding.priceChangeIndicatorImg.visibility = View.INVISIBLE
                }
            }
        }


    }
}

