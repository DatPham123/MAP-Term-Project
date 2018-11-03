package edu.uco.dpham9.datprobertmtermproject

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_trainee_home.*
import kotlinx.android.synthetic.main.app_bar_trainee_home.*
import kotlinx.android.synthetic.main.content_trainee_home.*

const val REQ_CODE_VIDEO = 1
const val EXTRA_EXERCISE_ID = "exercise_id"

class TraineeHomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener
{

    var mAuth: FirebaseAuth? = null
    var db: FirebaseFirestore? = null

    private var myExercises = ArrayList<TraineeExercise>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trainee_home)
        setSupportActionBar(toolbar)

        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        val currentUser = mAuth?.currentUser

        if(currentUser != null){
            val headerView = nav_view.getHeaderView(0)
            val emailView = headerView.findViewById<TextView>(R.id.id_nav_email)
            emailView.text = currentUser.email
        }

        fab.setOnClickListener { view ->
            //Launch Add Trainee Exercise
            val i = Intent(this, AddTraineeExercise::class.java)
            startActivity(i)
        }

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        //linear layout: vertical
        id_trainee_recyclerView.layoutManager = LinearLayoutManager(this)
        id_trainee_recyclerView.adapter = TraineeExerciseListAdapter(this, myExercises)

        for(i in 0..15)
        {
            myExercises.add(
                TraineeExercise(
                    "Exercise$i", "Description$i",
                    "vidUrl$i", 5, mAuth?.currentUser!!.uid
                )
            )
        }

        //update adapter data
        id_trainee_recyclerView.adapter?.notifyDataSetChanged()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
            logOut()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.trainee_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_find_exercise -> {
                // Handle the camera action
            }
            R.id.nav_my_exercise -> {

            }
            R.id.nav_my_trainer -> {

            }
            R.id.nav_manage_account -> {

            }
            R.id.nav_logout -> {
                logOut()
                finish()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun logOut()
    {
        mAuth?.signOut()
        val i = Intent(this, MainActivity::class.java)
        Toast.makeText(this, R.string.err_logout, Toast.LENGTH_SHORT).show()
        startActivity(i)
    }
}
