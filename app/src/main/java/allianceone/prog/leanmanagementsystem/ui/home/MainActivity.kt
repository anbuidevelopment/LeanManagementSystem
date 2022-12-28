package allianceone.prog.leanmanagementsystem.ui.home

import allianceone.prog.leanmanagementsystem.R
import allianceone.prog.leanmanagementsystem.databinding.ActivityMainBinding
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val firstFragment = SqlFormFragment()

        setCurrentFragment(firstFragment)

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_home -> setCurrentFragment(firstFragment)
                R.id.menu_notification -> setCurrentFragment(ListNotificationFragment())
                R.id.menu_staff_notification -> setCurrentFragment(StaffListNotificationFragment())
            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container_view, fragment)
            commit()
        }
}