package com.loriz.refgenerator

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_referral_manager.*

/**
 * Created by loriz on 26/03/18.
 */

class ReferralManager : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        setContentView(R.layout.activity_referral_manager)

        setupView()

        super.onCreate(savedInstanceState)
    }

    private fun setupView() {

        refman_add_button.setOnClickListener {

            val name = refman_name_edittext.text.toString()
            val link = refman_link_edittext.text.toString()

            if (name.isNotBlank() && link.isNotBlank()) {
                getSharedPreferences("SP", Context.MODE_PRIVATE).edit().putString(name, link).apply()

                refman_name_edittext.setText("")
                refman_link_edittext.setText("")

                Toast.makeText(this, getString(R.string.refman_referral_add_success), Toast.LENGTH_SHORT).show()
            } else {

                Toast.makeText(this, getString(R.string.refman_referral_add_error), Toast.LENGTH_SHORT).show()
            }

        }

    }

}