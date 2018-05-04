package com.loriz.refgenerator

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.content.Intent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.loriz.refgenerator.utils.sendTextViaShare
import kotlinx.android.synthetic.main.activity_fast_referral.*
import kotlinx.android.synthetic.main.activity_main.*


/**
 * Created by loriz on 26/03/18.
 */


class FastShareActivity : AppCompatActivity() {

    lateinit var intentUrl: String
    lateinit var outUrl: String
    var referralMap: HashMap<String, Any> = hashMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {

        referralMap = getSharedPreferences("SP", Context.MODE_PRIVATE).all as HashMap<String, Any>

        handleCreationByIntent()

        handleUrlPreparation()

        super.onCreate(savedInstanceState)
    }


    private fun handleCreationByIntent() {

        // Get intent, action and MIME type
        val intent = intent
        val action = intent.action
        val type = intent.type

        if (Intent.ACTION_SEND == action && type != null) {
            if ("text/plain" == type) {
                intentUrl = intent.getStringExtra(Intent.EXTRA_TEXT)
            }
        }
    }

    private fun handleUrlPreparation() {
        if (intentUrl.isNotBlank()) {

            when (referralMap.keys.size) {
                0 -> {
                    Toast.makeText(this, "Nessun referral da aggiungere, impostalo nell'app!", Toast.LENGTH_SHORT).show()
                    finish()
                }
                1 -> {
                    outUrl = intentUrl + (referralMap.values.first() as String)
                    sendTextViaShare(this, outUrl)
                    finish()
                }
                else -> {
                    prepareFastReferralUI()
                }
            }

        }
    }

    private fun prepareFastReferralUI() {
        setContentView(R.layout.activity_fast_referral)

        val dataAdapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, referralMap.keys.toList())
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        fast_referral_spinner.setAdapter(dataAdapter)

        fast_referral_generate_button.setOnClickListener {
            with(referralMap[fast_referral_spinner.selectedItem as String]) {
                sendTextViaShare(this@FastShareActivity, intentUrl + this)
                finish()
            }
        }


    }

}