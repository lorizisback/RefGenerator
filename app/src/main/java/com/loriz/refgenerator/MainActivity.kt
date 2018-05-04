package com.loriz.refgenerator

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.util.AttributeSet
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import android.R.menu
import android.content.Intent
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import android.R.attr.label
import android.content.ClipData
import android.content.ClipboardManager
import com.loriz.refgenerator.utils.getFirstLinkInText
import com.loriz.refgenerator.utils.sendTextViaShare


/**
 * Created by loriz on 26/03/18.
 */


class MainActivity : AppCompatActivity() {

    var intentUrl: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {

        setContentView(R.layout.activity_main)

        handleCreationByIntent()

        setupView()

        super.onCreate(savedInstanceState)
    }

    private fun handleCreationByIntent() {

        // Get intent, action and MIME type
        val intent = intent
        val action = intent.action
        val type = intent.type

        if (Intent.ACTION_SEND == action && type != null) {
            if ("text/plain" == type) {
                intentUrl = getFirstLinkInText(intent.getStringExtra(Intent.EXTRA_TEXT))
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.activity_main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            R.id.referral_entry -> {
                startActivity(Intent(this, ReferralManager::class.java))
            }
        }

        return super.onOptionsItemSelected(item)
    }


    private fun setupView() {

        url_label.setText("URL")

        url_edittext.setHint("Inserisci l'url")

        if (intentUrl != null) {
            url_edittext.setText(intentUrl)
        }

        referral_spinner_label.setText("Scegli il referral:")

        touch_blocker.setOnClickListener {
            Toast.makeText(this, "Aggiungi almeno un referral dal menu", Toast.LENGTH_SHORT).show()
        }

        generate_button.setText("Genera")
        generate_button.setOnClickListener {

            if (referral_spinner.count != 0) {


                var link = getSharedPreferences("SP", Context.MODE_PRIVATE).getString(referral_spinner.selectedItem.toString(), "-1")

                if (link != "-1") {
                    url_result.visibility = View.VISIBLE
                    copy_icon.visibility = View.VISIBLE
                    share_icon.visibility = View.VISIBLE

                    url_result.setText(url_edittext.text.toString() + link)
                    url_edittext.setText("")

                } else {
                    url_result.visibility = View.INVISIBLE
                    copy_icon.visibility = View.INVISIBLE
                    share_icon.visibility = View.INVISIBLE
                    Toast.makeText(this, "Referral non trovato!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Referral non trovato!", Toast.LENGTH_SHORT).show()
            }

        }

        copy_icon.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("RefGenerator", url_result.text.toString())
            clipboard.setPrimaryClip(clip)

            Toast.makeText(this, "Copiato negli appunti!", Toast.LENGTH_SHORT).show()

        }

        share_icon.setOnClickListener { sendTextViaShare(this, url_result.text.toString()) }

    }

    private fun refreshSpinner() {

        touch_blocker.visibility = View.VISIBLE
        touch_blocker.isClickable = true

        var refList = getSharedPreferences("SP", Context.MODE_PRIVATE).all.keys

        if (refList.size != 0) {
            touch_blocker.visibility = View.GONE
            val dataAdapter = ArrayAdapter(this,
                    android.R.layout.simple_spinner_item, refList.toList())
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            referral_spinner.setAdapter(dataAdapter)
        }

    }

    override fun onResume() {

        refreshSpinner()

        super.onResume()
    }


}