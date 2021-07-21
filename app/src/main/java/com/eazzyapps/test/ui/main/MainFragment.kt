package com.eazzyapps.test.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.eazzyapps.test.R
import com.eazzyapps.test.databinding.FragmentMainBinding
import com.eazzyapps.test.ui.viewmodels.MainViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val vm: MainViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (requireActivity() as? AppCompatActivity)?.apply {
            title = "Repositories list"
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }
        return FragmentMainBinding.inflate(inflater, container, false)
            .apply { this.viewModel = vm }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenResumed {
            vm.clickFlow.filter { it }.collect {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, DetailsFragment.newInstance())
                    .addToBackStack(null)
                    .commit()
            }
        }
        lifecycleScope.launchWhenResumed {
            vm.errorFlow.collect {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }
    }
}