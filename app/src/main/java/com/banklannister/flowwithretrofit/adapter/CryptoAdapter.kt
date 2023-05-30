package com.banklannister.flowwithretrofit.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.banklannister.flowwithretrofit.R
import com.banklannister.flowwithretrofit.databinding.ItemBinding
import com.banklannister.flowwithretrofit.response.ResponseCoinsMarkets
import com.banklannister.flowwithretrofit.utils.Constants.animationDuration
import com.banklannister.flowwithretrofit.utils.roundToTwoDecimal
import com.banklannister.flowwithretrofit.utils.toDoubleFloatPairs
import javax.inject.Inject

class CryptoAdapter @Inject constructor() : RecyclerView.Adapter<CryptoAdapter.MyViewHolder>() {

    private lateinit var binding: ItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemBinding.inflate(inflater, parent, false)
        return MyViewHolder()
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
        holder.setIsRecyclable(false)
    }

    inner class MyViewHolder : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: ResponseCoinsMarkets.ResponseCoinsMarketsItem) {
            binding.apply {
                tvName.text = item.id
                tvPrice.text = "€${item.currentPrice?.roundToTwoDecimal()}"
                tvSymbol.text = item.symbol?.uppercase()
                imgCrypto.load(item.image) {
                    crossfade(true)
                    crossfade(500)
                    placeholder(R.drawable.round_currency_bitcoin_24)
                        .error(R.drawable.round_currency_bitcoin_24)
                }
                lineChart.gradientFillColors = intArrayOf(
                    Color.parseColor("#2a9085"),
                    Color.TRANSPARENT
                )

                lineChart.animation.duration = animationDuration
                val listData = item.sparklineIn7d?.price.toDoubleFloatPairs()
                lineChart.animate(listData)

                root.setOnClickListener {
                    onItemClickListener?.let {
                        it(item)
                    }
                }

            }

        }
    }

    private var onItemClickListener: ((ResponseCoinsMarkets.ResponseCoinsMarketsItem) -> Unit)? =
        null

    fun setOnItemClickListener(listener: (ResponseCoinsMarkets.ResponseCoinsMarketsItem) -> Unit) {
        onItemClickListener = listener
    }

    private val differCallback =
        object : DiffUtil.ItemCallback<ResponseCoinsMarkets.ResponseCoinsMarketsItem>() {
            override fun areItemsTheSame(
                oldItem: ResponseCoinsMarkets.ResponseCoinsMarketsItem,
                newItem: ResponseCoinsMarkets.ResponseCoinsMarketsItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ResponseCoinsMarkets.ResponseCoinsMarketsItem,
                newItem: ResponseCoinsMarkets.ResponseCoinsMarketsItem
            ): Boolean {
                return oldItem == newItem
            }

        }

    val differ = AsyncListDiffer(this, differCallback)

}