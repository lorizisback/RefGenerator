package com.loriz.refgenerator

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.Toast
import com.loriz.refgenerator.adapter.ReferralListAdapter
import kotlinx.android.synthetic.main.activity_referral_manager.*

/**
 * Created by loriz on 26/03/18.
 */

class ReferralManager : AppCompatActivity() {

    var entryList : LinkedHashSet<String> = linkedSetOf()

    override fun onCreate(savedInstanceState: Bundle?) {

        setContentView(R.layout.activity_referral_manager)

        setupView()

        super.onCreate(savedInstanceState)
    }

    private lateinit var layoutRefManager: LinearLayoutManager

    private lateinit var adapter: ReferralListAdapter

    private fun setupView() {

        refreshList()

        layoutRefManager = LinearLayoutManager(this)
        adapter = ReferralListAdapter(this@ReferralManager, entryList)

        with(added_link_list) {
            layoutManager = layoutRefManager as RecyclerView.LayoutManager
            adapter = ReferralListAdapter(this@ReferralManager, entryList)
        }

        refman_add_button.setOnClickListener {

            val name = refman_name_edittext.text.toString()
            val link = refman_link_edittext.text.toString()

            if (name.isNotBlank() && link.isNotBlank()) {
                addEntry(name, link)

                refman_name_edittext.setText("")
                refman_link_edittext.setText("")
                val view = this.currentFocus
                if (view != null) {
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm!!.hideSoftInputFromWindow(view.windowToken, 0)
                }
                Toast.makeText(this, getString(R.string.refman_referral_add_success), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, getString(R.string.refman_referral_add_error), Toast.LENGTH_SHORT).show()
            }

        }

    }

    private fun addEntry(name: String, link: String) : Boolean {
        try {
            getSharedPreferences("SP", Context.MODE_PRIVATE).edit().putString(name, link).commit()
            refreshList()
            entryList.add(name)
            added_link_list?.adapter = ReferralListAdapter(this@ReferralManager, entryList)
            return true
        } catch (e: Exception) {
            getSharedPreferences("SP", Context.MODE_PRIVATE).edit().remove(name).commit()
            if (entryList.contains(name)) entryList.remove(name)
            adapter.notifyDataSetChanged()
            return false
        }
    }

    private fun refreshList() {
        entryList.clear()
        entryList.addAll(getSharedPreferences("SP", Context.MODE_PRIVATE).all.keys.toList())
    }

}