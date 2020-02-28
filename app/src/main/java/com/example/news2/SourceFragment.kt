package com.example.news2

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.news2.adapters.SourceAdapter
import com.example.news2.databinding.FragmentSourceBinding
import com.example.news2.model.SourceDomain
import com.example.news2.viewmodels.SourceViewModel
import com.example.news2.viewmodels.viewmodelfactory.SourceViewModelFactory


class SourceFragment : Fragment() {

    private val viewModel: SourceViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProvider(this, SourceViewModelFactory(activity.application))
            .get(SourceViewModel::class.java)
    }

    private var viewModelAdapter: SourceAdapter? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.sourceList.observe(viewLifecycleOwner, Observer<List<SourceDomain>> {
            sources -> sources?.apply {
            viewModelAdapter?.sources = sources
            // Log.e("Fragment", "valores: $this")
        }
        })
    }


    private lateinit var binding: FragmentSourceBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        viewModel.eventNetworkError.observe(viewLifecycleOwner, Observer<Boolean> {
            isNetworkError -> if (isNetworkError) onNetworkError()
        })


        // Example the ViewBinding
        //binding = FragmentSourceBinding.inflate(inflater)
        //binding.plainName.text = "Luz"
       // binding.plainLastName.text = "Cruz"
        //return binding.root
        // End viewBinding

        val binding: FragmentSourceBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_source, container, false)
        binding.name = "jairo"
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        viewModelAdapter = SourceAdapter()
        binding.root.findViewById<RecyclerView>(R.id.recycler_view).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewModelAdapter
        }
        return binding.root

    }

    private fun onNetworkError() {
        if (!viewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            viewModel.onNetworkErrorShown()
        }
    }
}