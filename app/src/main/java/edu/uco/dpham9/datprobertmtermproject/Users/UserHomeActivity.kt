package edu.uco.dpham9.datprobertmtermproject.Users

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import edu.uco.dpham9.datprobertmtermproject.*
import edu.uco.dpham9.datprobertmtermproject.Model.TraineeExercise
import edu.uco.dpham9.datprobertmtermproject.Model.TraineeExerciseListAdapter
import edu.uco.dpham9.datprobertmtermproject.Model.UserAuth
import kotlinx.android.synthetic.main.activity_user_home.*
import kotlinx.android.synthetic.main.app_bar_user_home.*
import kotlinx.android.synthetic.main.content_user_home.*

const val REQ_CODE_VIDEO = 1
const val REQ_CODE_ADD_EX = 2
const val REQ_CODE_EDIT_EX = 3
const val REQ_CODE_RATING = 4
const val EXTRA_EXERCISE = "trainee_exercise"

const val TAG = "local"

class TraineeHomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener
{

    private var mAuth: FirebaseAuth? = null
    private var db: FirebaseFirestore? = null

    private var myExercises = ArrayList<TraineeExercise>()
    private var myTrainees = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_home)
        setSupportActionBar(toolbar)

        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val currentUser = mAuth?.currentUser

        db?.collection("Users")
            ?.whereEqualTo("id", currentUser!!.uid)
            ?.get()
            ?.addOnCompleteListener {
                if(it.isSuccessful)
                {
                    val isTrainee = it.result!!.documents[0]!!.toObject(UserAuth::class.java)!!.isTrainee
                    initNavMenuDisplay(isTrainee)
                    initRecyclerView(isTrainee)
                }
            }
            ?.addOnFailureListener {
                Toast.makeText(this, it.toString(), Toast.LENGTH_LONG).show()
                finish()
            }

        fab.setOnClickListener { view ->
            //Launch Add Trainee Exercise
            val i = Intent(this, AddTraineeExercise::class.java)
            startActivityForResult(i, REQ_CODE_ADD_EX)
        }

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    private fun initRecyclerView(isTrainee: Boolean)
    {
        id_user_recyclerView.layoutManager = LinearLayoutManager(this)
        if(isTrainee)
        {
            db?.collection("TraineeExercises/${mAuth?.currentUser?.email}/MyExercises")
                ?.get()
                ?.addOnSuccessListener {
                    myExercises.clear()
                    for (docSnapShot in it)
                    {
                        val exercise = docSnapShot.toObject(TraineeExercise::class.java)
                        myExercises.add(exercise)
                    }
                    id_user_recyclerView.adapter = TraineeExerciseListAdapter(this, myExercises)
                    id_user_recyclerView.adapter?.notifyDataSetChanged()
                }
                ?.addOnFailureListener { ex: Exception ->
                    Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show()
                }
        }
        else
        {
            /******UNDER CONSTRUCTION*********/
            id_user_recyclerView.adapter = TraineeExerciseListAdapter(this, myExercises)
            /******UNDER CONSTRUCTION********/
        }
    }

    private fun initNavMenuDisplay(isTrainee: Boolean)
    {
        if (isTrainee)
        {
            displayTraineeNavMenu()
        }
        else
            displayTrainerNavMenu()

        val headerView = nav_view.getHeaderView(0)
        val emailView = headerView.findViewById<TextView>(R.id.id_nav_email)
        emailView.text = this.mAuth?.currentUser!!.email
    }

    private fun displayTraineeNavMenu()
    {
        val headerView = nav_view.getHeaderView(0)
        val userType = headerView.findViewById<TextView>(R.id.id_userType)
        userType.text = getString(R.string.label_traineeType)
        val nav = findViewById<NavigationView>(R.id.nav_view).menu
        nav.findItem(R.id.nav_find_exercise).isVisible = true
        nav.findItem(R.id.nav_manage_account).isVisible = true
        nav.findItem(R.id.nav_my_trainer).isVisible = true
        //nav.findItem(R.id.nav_my_exercise).isVisible = false
        fab.show()
    }

    private fun displayTrainerNavMenu()
    {
        val headerView = nav_view.getHeaderView(0)
        val userType = headerView.findViewById<TextView>(R.id.id_userType)
        userType.text = getString(R.string.label_trainerType)
        val nav = findViewById<NavigationView>(R.id.nav_view).menu
        nav.findItem(R.id.nav_find_exercise).isVisible = true
        nav.findItem(R.id.nav_manage_account).isVisible = true
        nav.findItem(R.id.nav_my_trainer).isVisible = false
        //nav.findItem(R.id.nav_my_exercise).isVisible = false
        fab.hide()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        if(resultCode != Activity.RESULT_OK)
            return
        if(requestCode == REQ_CODE_ADD_EX || requestCode == REQ_CODE_EDIT_EX || requestCode == REQ_CODE_RATING)
        {
            initRecyclerView(true)
        }

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
        menuInflater.inflate(R.menu.user_home, menu)
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
//                val i = Intent(this, FindExercise::class.java)
//                startActivity(i)
            }
//            R.id.nav_my_exercise -> {
//
//            }
            R.id.nav_my_trainer -> {

            }
            R.id.nav_manage_account -> {
                val i = Intent(this, ManageAccount::class.java)
                startActivity(i)
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
        Toast.makeText(this, R.string.err_signOut, Toast.LENGTH_SHORT).show()
        startActivity(i)
    }
}
