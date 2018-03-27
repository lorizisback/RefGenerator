package com.loriz.refgenerator.utils

import java.nio.file.Files.size
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Patterns


fun extractLinks(text: String): ArrayList<String> {
    val links = ArrayList<String>()
    val m = Patterns.WEB_URL.matcher(text)
    while (m.find()) {
        val url = m.group()
        links.add(url)
    }

    return links
}

fun getFirstLinkInText(text: String) : String? {
    with(extractLinks(text)) {
        return if (isNotEmpty()) {
           first()
        } else {
            null
        }
    }
}

fun sendTextViaShare(context: Context, output: String) {

    val sendIntent = Intent()
    sendIntent.action = Intent.ACTION_SEND
    sendIntent.putExtra(Intent.EXTRA_TEXT, output)
    sendIntent.type = "text/plain"
    context.startActivity(Intent.createChooser(sendIntent, "Referral aggiunto! Invia tramite..."))

}