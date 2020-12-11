package activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.rohit.bookhub.*
import fragment.AboutAppFragment
import fragment.DashboardFragment
import fragment.FavouritesFragment
import fragment.ProfileFragment

class MainActivity : AppCompatActivity() {

    lateinit var drawerLayout: DrawerLayout
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var toolbar: Toolbar
    lateinit var frameLayout: FrameLayout
    lateinit var navigationView: NavigationView

    var previousMenuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawerLayout)
        coordinatorLayout = findViewById(R.id.coordinatorLayout)
        toolbar = findViewById(R.id.toolbar)
        frameLayout = findViewById(R.id.frame)
        navigationView = findViewById(R.id.navigationView)


        setUpToolbar()

        openDashboard()

        var actionBarDrawerToggle = ActionBarDrawerToggle(
            this@MainActivity,
            drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        navigationView.setNavigationItemSelectedListener {

            if (previousMenuItem != null){
                previousMenuItem?.isChecked = false
            }

            it.isCheckable = true
            it.isChecked = true
            previousMenuItem = it
            when(it.itemId){
                R.id.dashboard -> {
//                    Toast.makeText(this@MainActivity, "Clicked on dashboard", Toast.LENGTH_LONG).show()
//                    supportFragmentManager.beginTransaction()
//                        .replace(R.id.frame,DashboardFragment())
////                        .addToBackStack("Dashboard")
//                        .commit()
//                        supportActionBar?.title = "Dashboard"

                        openDashboard()
                        drawerLayout.closeDrawers()
                }
                R.id.favourites -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, FavouritesFragment())
//                        .addToBackStack("Favourites")
                        .commit()
                    supportActionBar?.title = "Favourites"

                    drawerLayout.closeDrawers()
//                    Toast.makeText(this@MainActivity, "Clicked on favourites", Toast.LENGTH_LONG).show()
                }
                R.id.profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, ProfileFragment())
//                        .addToBackStack("Profile")
                        .commit()
                    supportActionBar?.title = "Profile"

                    drawerLayout.closeDrawers()
//                    Toast.makeText(this@MainActivity, "Clicked on profile", Toast.LENGTH_LONG).show()
                }
                R.id.aboutApp -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, AboutAppFragment())
//                        .addToBackStack("About App")
                        .commit()
                    supportActionBar?.title = "About App"

                    drawerLayout.closeDrawers()
//                    Toast.makeText(this@MainActivity, "Clicked on About App", Toast.LENGTH_LONG).show()
                }
            }
            return@setNavigationItemSelectedListener true
        }
    }
        fun setUpToolbar(){
            setSupportActionBar(toolbar)
            supportActionBar?.title = "Toolbar Title"
            supportActionBar?.setHomeButtonEnabled(true)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START)
        }

        return super.onOptionsItemSelected(item)
    }

    fun openDashboard(){
        val fragment = DashboardFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame, fragment)
        transaction.commit()
        supportActionBar?.title = "Dashboard"
        navigationView.setCheckedItem(R.id.dashboard)
    }

    override fun onBackPressed() {
        val frag = supportFragmentManager.findFragmentById(R.id.frame)

        when(frag){
            !is DashboardFragment -> openDashboard()

            else -> super.onBackPressed()
        }
    }
}