package app.elite.spendapp.adapter

import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.elite.spendapp.data.TransactionEntity
import app.elite.spendapp.databinding.ItemTransactionLayoutBinding
import java.util.*

class TransactionAdapter :
    ListAdapter<TransactionEntity, TransactionAdapter.TransactionViewholder>(TransactionDiffUtils()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewholder {
        val binding = ItemTransactionLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TransactionViewholder(binding)
    }

    override fun onBindViewHolder(holder: TransactionViewholder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class TransactionViewholder(val binding: ItemTransactionLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(transactionEntity: TransactionEntity) {
            with(binding) {
                transactionTitle.text = transactionEntity.title
                transactionAmount.text = transactionEntity.amount.toString()
                transactionCategory.text = transactionEntity.type
                transactionDate.text = transactionEntity.date
            }
            binding.root.setOnClickListener {
                Log.d("TAG","clicked")

            }

        }
    }
}

class TransactionDiffUtils : DiffUtil.ItemCallback<TransactionEntity>() {

    override fun areItemsTheSame(
        oldItem: TransactionEntity,
        newItem: TransactionEntity
    ) = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: TransactionEntity,
        newItem: TransactionEntity
    ) = oldItem == newItem
}