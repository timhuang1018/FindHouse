package com.chingkai56.findhouse.ui

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreferenceCompat
import com.chingkai56.findhouse.R
import com.chingkai56.findhouse.config.HouseKeyWord
import com.chingkai56.findhouse.work.FetchHousesWork
import timber.log.Timber

/**
 * Created by timhuang on 2021/2/5.
 **/

class ConfigFragment():PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = HouseKeyWord.ApplicationName

        setPreferencesFromResource(R.xml.config_preference,rootKey)

        //this is for blog example(方式2), see TODO add link
//        demo()
    }

    //Demo how to dynamically create preference
    private fun demo(){
        val root = preferenceManager.createPreferenceScreen(requireContext())
        val child = SwitchPreferenceCompat(requireContext())
        child.title = getString(R.string.text_fetch_data)
        child.key = getString(R.string.key_fetch_data)
        root.addPreference(child)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListener()
    }

    private fun setupListener() {


        findPreference<SwitchPreferenceCompat>(getString(R.string.key_notify_new))?.let {

            Timber.d("key:${it.key},isChecked:${it.isChecked}")
        }

        //if fetch data get set to true
        //display dialog to remind fetch start immediately
        //if set to false
        findPreference<SwitchPreferenceCompat>(getString(R.string.key_fetch_data))?.let { fetchDataPref->

            fetchDataPref.setOnPreferenceChangeListener(object :Preference.OnPreferenceChangeListener{
                override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
                    Timber.e("onPreferenceChange called newValue:$newValue")
                    if(newValue==true){
                        remindDialog()
                        FetchHousesWork.startWork(requireContext())
                    }

                    if(newValue==false){
                        FetchHousesWork.cancelWork(requireContext())
                    }
                    //any case not proceed above
                    return true
                }

            })

        }

    }

    //there is no reset since the preference is stored,this method is for reminding
    private fun remindDialog() {
        AlertDialog.Builder(requireContext())
                .setTitle("記得查看App")
                .setMessage("背景獲取資料有可能因資源不足而被系統取消，常查看App會保持不被中斷。")
                .setPositiveButton("我知道了",object :DialogInterface.OnClickListener{
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        dialog?.dismiss()
                    }
                })
                .show()
    }
}