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
import com.eazzyapps.test.databinding.FragmentDetailsBinding
import com.eazzyapps.test.ui.viewmodels.DetailsViewModel
import com.eazzyapps.test.ui.viewmodels.MainViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class DetailsFragment : Fragment() {

    private val sharedVm: MainViewModel by sharedViewModel()

    private val vm: DetailsViewModel by viewModel { parametersOf(sharedVm.selectedRepo) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        (requireActivity() as? AppCompatActivity)?.apply {
            title = "Details"
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        lifecycleScope.launchWhenResumed {
            vm.errorFlow.collect {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }

        return FragmentDetailsBinding.inflate(inflater, container, false)
            .apply { this.viewModel = vm }
            .root
    }

    override fun onResume() {
        super.onResume()
        vm.onResume()
    }

    override fun onPause() {
        super.onPause()
        vm.onPause()
    }

    companion object {
        fun newInstance() = DetailsFragment()
    }

}