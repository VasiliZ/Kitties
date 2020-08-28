package com.github.rtyvZ.kitties.common

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.github.rtyvZ.kitties.R
import com.github.rtyvZ.kitties.ui.favoriteCats.FavoriteCatsFragment
import com.github.rtyvZ.kitties.ui.imageCats.RandomCatsFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private var currentSelectedItem = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navigation.setOnNavigationItemSelectedListener { item ->
            if (item.itemId == currentSelectedItem) {
                return@setOnNavigationItemSelectedListener false
            }

            when (item.itemId) {

                R.id.list_kitties -> replaceFragment(RandomCatsFragment())

                R.id.favorite_cats -> replaceFragment(FavoriteCatsFragment())

                else -> {
                }
            }

            currentSelectedItem = item.itemId

            return@setOnNavigationItemSelectedListener true
        }
        navigation.selectedItemId = R.id.list_kitties
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.content_container, fragment)
        transaction.addToBackStack(fragment::class.java.canonicalName)
        transaction.commit()
    }
}