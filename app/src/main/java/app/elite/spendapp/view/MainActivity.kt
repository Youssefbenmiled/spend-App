package app.elite.spendapp.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.elite.spendapp.R
import app.elite.spendapp.adapter.TransactionAdapter
import app.elite.spendapp.data.TransactionDao
import app.elite.spendapp.data.TransactionDatabase
import app.elite.spendapp.data.TransactionEntity
import app.elite.spendapp.databinding.ActivityMainBinding
import app.elite.spendapp.databinding.UsernameDialogBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: TransactionDatabase
    private lateinit var dao: TransactionDao
    private lateinit var transactionAdapter: TransactionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 1 step create the binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // 2 step set the toolbar as default system toolbar
        setSupportActionBar(binding.toolbar)
        // Optional remove the toolbar title
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        // add navigation to other intent
        binding.navigateAction.setOnClickListener {
            val intent = Intent(this, AddTransactionActivity::class.java)
            startActivityForResult(intent, 3000)
        }
        transactionAdapter = TransactionAdapter()

        binding.mlayout.welcomeText.text = getString(R.string.welcome, "ananymous")

        binding.mlayout.root.setOnClickListener {
            createDialog()
        }

        loadData()

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = transactionAdapter
        }

        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.DOWN or ItemTouchHelper.UP,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT

        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }


            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val item = transactionAdapter.currentList[position]
                GlobalScope.launch {
                    dao.deleteOne(item)
                    val list = dao.getAll()
                    transactionAdapter.submitList(list)
                    runOnUiThread {
                       animationCheck(list!!)
                    }

                }
            }
        }
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.recycler)
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 3000) {
            loadData()

        }
    }

    private fun loadData() {
        database = TransactionDatabase.getInstance(this)
        dao = database.getTranscationDao()

        var list: List<TransactionEntity>?

        /*runOnUiThread(
        object : Runnable {
            override fun run() {
            }
        }
        )
      */
        GlobalScope.launch {
            list = dao.getAll()
            transactionAdapter.submitList(list)
            runOnUiThread {
                animationCheck(list!!)

            }
        }


    }

    private fun createDialog() {
        val dialog = UsernameDialogBinding.inflate(layoutInflater)
        val dialogBuilder = MaterialAlertDialogBuilder(this)
            .setView(dialog.root)
            .setCancelable(false)
            .create()

        dialogBuilder.setOnShowListener {
            dialog.addName.setOnClickListener {
                if (dialog.name.text.toString().isEmpty()) {
                    dialog.name.error = "Required Field"
                } else {
                    binding.mlayout.welcomeText.text = getString(R.string.welcome, dialog.name.text.toString())
                    dialogBuilder.dismiss()
                }
            }
        }
        dialogBuilder.show()
    }

    // 3 step link menu with toolbar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    // 4 step add menu item action
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.mode_type) {
            // change the theme from dark to light and vis-versa
            if (item.isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            item.isChecked = !item.isChecked
        }
        return true
    }


    private fun animationCheck(list:List<TransactionEntity>) {
        if (list!!.size == 0) {
            binding.viewAnimation.visibility = View.VISIBLE
        } else {
            binding.viewAnimation.visibility = View.GONE
        }
    }
}