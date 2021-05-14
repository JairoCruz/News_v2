package com.example.news2

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.selection.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.news2.adapters.MyItemDetailsLookup
import com.example.news2.adapters.SourceAdapter
import com.example.news2.databinding.FragmentSourceBinding
import com.example.news2.model.SourceDomain
import com.example.news2.viewmodels.SourceViewModel
import com.example.news2.viewmodels.viewmodelfactory.SourceViewModelFactory


class SourceFragment : Fragment() {

    var tracker: SelectionTracker<Long>? = null


    // Variables
    private val TAG = SourceFragment::class.java.simpleName
    private val BUNDLETAG = "BUNDLETAG"

    private val viewModel: SourceViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProvider(this, SourceViewModelFactory(activity.application))
                .get(SourceViewModel::class.java)
    }

    private var viewModelAdapter: SourceAdapter? = null


    var actionMode: ActionMode? = null


    private val actionModelCallback = object : ActionMode.Callback {
        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
            return when (item?.itemId) {
                R.id.add -> {
                    mode?.finish()
                    true
                }
                else -> false
            }
        }

        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            val inflater: MenuInflater = mode!!.menuInflater
            inflater.inflate(R.menu.menu_action, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            return false
        }

        override fun onDestroyActionMode(mode: ActionMode?) {
            actionMode = null
            tracker?.clearSelection()
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        // this is for save selection tracker in route screen
        tracker?.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)

    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        // this is for get selection tracker in route screen
        tracker?.onRestoreInstanceState(savedInstanceState)


    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.sourceList.observe(viewLifecycleOwner, Observer<List<SourceDomain>> {
            it?.let {
                viewModelAdapter?.addHeaderAndSubmitList(it)
            }
        })
    }


    private lateinit var binding: FragmentSourceBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        // Inflate the layout for this fragment
        viewModel.eventNetworkError.observe(viewLifecycleOwner, Observer<Boolean> { isNetworkError ->
            if (isNetworkError) onNetworkError()
        })


        // val binding: FragmentSourceBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_source, container, false)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_source, container, false)

        binding.sourceSize = "Sources available"
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewModelAdapter = SourceAdapter()

        binding.root.findViewById<RecyclerView>(R.id.recycler_view).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewModelAdapter

            tracker = SelectionTracker.Builder<Long>(
                    "mySelection",
                    this,
                    StableIdKeyProvider(this),
                   // MyItemKeyProvider(this),
                    MyItemDetailsLookup(this),
                    StorageStrategy.createLongStorage()
            ).withSelectionPredicate(
                    SelectionPredicates.createSelectAnything()
            ).build()

            tracker?.addObserver(
                    object : SelectionTracker.SelectionObserver<Long>() {
                        override fun onSelectionRestored() {
                            super.onSelectionRestored()
                            val items: Int? = tracker?.selection!!.size()
                            if (items != 0) {
                                when (actionMode) {
                                    null -> {
                                        actionMode = activity?.startActionMode(actionModelCallback)?.apply {
                                            title = "Selected ${items.toString()}"
                                        }
                                    }
                                    else -> {
                                        actionMode?.apply {
                                            title = "Selected ${items.toString()}"
                                        }
                                        false
                                    }
                                }

                            }
                        }

                        override fun onSelectionChanged() {
                            super.onSelectionChanged()
                            val items: Int? = tracker?.selection!!.size()
                            Log.e(TAG, "Size Selection: $items")
                            if (items != 0) {
                                when (actionMode) {
                                    null -> {

                                        actionMode = activity?.startActionMode(actionModelCallback)?.apply {
                                            title = "Selected ${items.toString()}"
                                        }

                                    }
                                    else -> {

                                        actionMode?.apply {
                                            title = "Selected ${items.toString()}"
                                        }
                                        false
                                    }
                                }
                            } else {
                                actionMode?.finish()
                            }
                        }


                    }

            )

            // Log.e(TAG, "aca3: ${tracker?.let { "hola3" }}")
        }

        binding.fabShowSources.setOnClickListener {
            showListDialogSources(it)
        }



        viewModelAdapter?.tracker = tracker



        return binding.root


    }

    private fun showListDialogSources(view: View) {
        Log.e(TAG, "Click FAb")
        // showDialogList()

    }

    private fun showDialogList() {
        val builder: AlertDialog.Builder = activity.let {
            AlertDialog.Builder(it)
        }

        builder.setMessage("me muestro").setTitle("saludos")

        val dialog: AlertDialog? = builder.create()

        dialog?.show()
    }

    private fun onNetworkError() {
        if (!viewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            viewModel.onNetworkErrorShown()
        }
    }


}

class MyItemKeyProvider(private val recyclerView: RecyclerView) : ItemKeyProvider<Long>(ItemKeyProvider.SCOPE_MAPPED) {
    override fun getKey(position: Int): Long? {
        return recyclerView.adapter?.getItemId(position)
    }

    override fun getPosition(key: Long): Int {
        val viewHolder = recyclerView.findViewHolderForItemId(key)
        return viewHolder?.layoutPosition ?: RecyclerView.NO_POSITION
    }

}
