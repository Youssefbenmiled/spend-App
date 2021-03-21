package app.elite.spendapp.view

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import app.elite.spendapp.R
import app.elite.spendapp.data.TransactionEntity
import app.elite.spendapp.data.TransactionDatabase
import app.elite.spendapp.databinding.ActivityAddTransactionBinding

import app.elite.spendapp.listTag
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AddTransactionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAddTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = ArrayAdapter(
            this,
            R.layout.type_item,
            R.id.type_content,
            resources.getStringArray(R.array.type)
        )

        val tagAdapter = ArrayAdapter(
            this,
            R.layout.type_item,
            R.id.type_content,
            listTag
        )

        binding.apply {
            typeInput.setAdapter(adapter)
            tagInput.setAdapter(tagAdapter)
            dateInput.apply {
                isClickable = true
                isFocusable = false
                isFocusableInTouchMode = false
            }
            dateInput.setOnClickListener {
                val picker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select Date")
                    .build()

                picker.addOnPositiveButtonClickListener {
                    Log.e("TAG", "onCreate: $it")
                    val simpleDate = SimpleDateFormat("dd/MM/y", Locale.getDefault())
                    dateInput.setText(simpleDate.format(Date(it)))
                    //binding.dateInput.setText(picker.headerText)
                }

                picker.show(supportFragmentManager, "TAG")
            }

            saveData.setOnClickListener {

                val database = TransactionDatabase.getInstance(this@AddTransactionActivity)
                val dao = database.getTranscationDao()
                val title: String = inputTitle.text.toString()
                val amount: String = amountInput.text.toString()
                val type: String = typeInput.text.toString()
                val tag: String = tagInput.text.toString()
                val date: String = dateInput.text.toString()
                val note: String = inputNote.text.toString()
              if(verif(listOf(inputTitle,amountInput,typeInput,tagInput,dateInput,inputNote))){
                  GlobalScope.launch {
                      dao.insertOne(
                          TransactionEntity(
                              title = title,
                              amount = amount.toInt(),
                              type = type,
                              tag = tag,
                              date = date,
                              note = note
                          )
                      )
                  }
                  clearAll(listOf(inputTitle,amountInput,typeInput,tagInput,dateInput,inputNote))
              }





            }
        }
    }

    private fun clearAll(list : List<EditText>) {
        for (item in list)
        {
            item.text.clear()
        }


    }

    private fun toInteger(s: String) {
        try {
            val value = s.toInt()
            println("The value is $value")
        } catch (ex: NumberFormatException) {
            println("The given string is non-numeric")
        }
    }
    
    private fun verif(list : List<EditText>):Boolean{

        for (item in list)
        {
            if(item.text.toString().isEmpty()){
                item.error="Required Field"
                return false
            }
        }

        return true;

        
    }


}