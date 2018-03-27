package com.loriz.refgenerator.adapter

import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.loriz.refgenerator.R


class ReferralListAdapter(val context: Context, refList: LinkedHashSet<String>) : RecyclerView.Adapter<ReferralListAdapter.ReferralViewHolder>() {

    var referralList : ArrayList<String> = arrayListOf()

    init {
        referralList.addAll(refList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReferralViewHolder {
        val holder = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_referral, parent, false)
        return ReferralViewHolder(holder)
    }


    override fun onBindViewHolder(holder: ReferralViewHolder, position: Int) {

        holder.title.text = referralList[position]

        holder.content.text =  context.getSharedPreferences("SP", Context.MODE_PRIVATE).getString(referralList[position], "")

        holder.deleteButton.setOnClickListener {
            AlertDialog.Builder(context)
                    .setMessage("Vuoi cancellare il referral \"${referralList[position]}\"?")
                    .setPositiveButton("Si", { dialog, _ ->
                        removeEntry(context, referralList[position])
                        notifyDataSetChanged()
                        dialog.dismiss()
                    })
                    .setNegativeButton("No", { dialog, _ ->
                        dialog.dismiss()
                    }).show()
        }

    }

    override fun getItemCount(): Int {
        return referralList.size
    }

    private fun removeEntry(context: Context, name: String) {
        context.getSharedPreferences("SP", Context.MODE_PRIVATE).edit().remove(name).commit()
        referralList.remove(name)
    }

    open class ReferralViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById(R.id.viewholder_referral_entry_title)
        var content: TextView = view.findViewById(R.id.viewholder_referral_entry_content)
        var deleteButton: ImageView = view.findViewById(R.id.viewholder_referral_entry_delete_button)
    }


}