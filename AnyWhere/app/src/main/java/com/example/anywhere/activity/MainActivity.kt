package com.example.anywhere.activity

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.anywhere.R
import com.example.anywhere.fragments.ListFragment
import com.example.anywhere.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private var listLayout: ViewGroup? = null
    private var contentLayout: ViewGroup? = null
    private lateinit var viewModel: MainViewModel
    var isTablet = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.button).setOnClickListener {
            contentLayout = findViewById(R.id.frameLayout_content)
            if (contentLayout != null) {
                isTablet = true
            }
            viewModel.setIsTab(isTablet)
            listLayout = findViewById(R.id.frameLayout_list)
            if (listLayout != null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayout_list, ListFragment())
                    .addToBackStack(ListFragment::class.simpleName)
                    .commit()
            }
        }
    }

    override fun onCreateView(
        parent: View?,
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View? {
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        return super.onCreateView(parent, name, context, attrs)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStackImmediate()
        } else super.onBackPressed()
    }
}