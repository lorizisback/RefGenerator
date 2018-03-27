package com.loriz.refgenerator

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.content.Intent
import android.widget.Toast
import com.loriz.refgenerator.utils.sendTextViaShare


/**
 * Created by loriz on 26/03/18.
 */


class FastShareActivity : AppCompatActivity() {

    lateinit var intentUrl: String
    lateinit var outUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {

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

            when (getSharedPreferences("SP", Context.MODE_PRIVATE).all.keys.size) {
                0 -> {
                    Toast.makeText(this, "Nessun referral da aggiungere, impostalo nell'app!", Toast.LENGTH_SHORT).show()
                }
                1 -> {
                    outUrl = intentUrl + (getSharedPreferences("SP", Context.MODE_PRIVATE).all.values.first() as String)
                    sendTextViaShare(this, outUrl)
                }
                else -> {
                    Toast.makeText(this, "Non ancora supportato!", Toast.LENGTH_SHORT).show()
                }
            }

            finish()

        }
    }

}