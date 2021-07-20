package com.eazzyapps.test.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.eazzyapps.test.R
import com.eazzyapps.test.databinding.FragmentMainBinding
import com.eazzyapps.test.ui.viewmodels.MainViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val vm: MainViewModel by sharedViewModel()

    private val disposables = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (requireActivity() as? AppCompatActivity)?.apply {
            title = "Repositories list"
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }
        return FragmentMainBinding.inflate(inflater, container, false)
            .apply { this.viewModel = vm }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        disposables.add(
            vm.itemClicks
                .subscribeBy {
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.container, DetailsFragment.newInstance())
                        .addToBackStack(null)
                        .commit()
                }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }
}