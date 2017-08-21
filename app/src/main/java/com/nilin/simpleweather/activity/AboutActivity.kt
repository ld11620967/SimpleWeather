package com.nilin.simpleweather.activity

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem

import com.nilin.simpleweather.R
import kotlinx.android.synthetic.main.activity_about.*


/**
 * Created by nilin on 2017/7/23.
 */
class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        //返回按钮
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        if(Build.VERSION.SDK_INT>=21){
            getSupportActionBar()!!.setElevation(0f);
        }
        project_home_btn.setOnClickListener {
            val uri = Uri.parse("http://www.apkbus.com/myspaceblog-901770.html")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
    }

    //返回按钮点击事件
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

}
