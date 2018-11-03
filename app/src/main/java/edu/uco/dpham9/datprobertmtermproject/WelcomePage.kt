package edu.uco.dpham9.datprobertmtermproject

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class WelcomePage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome_page)


        val background = object:Thread(){
            override fun run() {
                try {
                    Thread.sleep(3000)
                    val home = Intent(baseContext, MainActivity::class.java)
                    startActivity(home)
                }catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }
        background.start()
    }
}
