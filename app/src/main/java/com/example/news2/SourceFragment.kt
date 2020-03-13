package com.example.news2

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.view.ActionMode
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StableIdKeyProvider
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.news2.adapters.MyItemDetailsLookup
import com.example.news2.adapters.SourceAdapter
import com.example.news2.database.entities.Source
import com.example.news2.databinding.FragmentSourceBinding
import com.example.news2.model.SourceDomain
import com.example.news2.viewmodels.SourceViewModel
import com.example.news2.viewmodels.viewmodelfactory.SourceViewModelFactory
import kotlinx.android.synthetic.main.fragment_source.*


class SourceFragment : Fragment() {

    var tracker: SelectionTracker<Long>? = null


    // Variables
    private val TAG = SourceFragment::class.java.simpleName

    private val viewModel: SourceViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProvider(this, SourceViewModelFactory(activity.application))
            .get(SourceViewModel::class.java)
    }

    private var viewModelAdapter: SourceAdapter? = null

    private var listSourcesDialog: List<SourceDomain> = emptyList()


    var actionMode: ActionMode? = null

    companion object {
        var isMultiselectOn = false
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.sourceList.observe(viewLifecycleOwner, Observer<List<SourceDomain>> {
            sources -> sources?.apply {
            viewModelAdapter?.sources = sources

        }
        })
    }


    private lateinit var binding: FragmentSourceBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        viewModel.eventNetworkError.observe(viewLifecycleOwner, Observer<Boolean> {
            isNetworkError -> if (isNetworkError) onNetworkError()
        })









        val binding: FragmentSourceBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_source, container, false)
        binding.name = "jairo"
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewModelAdapter = SourceAdapter(CheckBoxClick {
            //Log.e(TAG, "vaya: $it")
            if (it.isChecked){
                val s = Source(it.id,it.name,it.description,it.url,it.category,it.language,it.country, false)
                viewModel.updateSource(s)
            } else {
                val s = Source(it.id,it.name,it.description,it.url,it.category,it.language,it.country, true)
                viewModel.updateSource(s)
            }

        }/*, CardViewClick {
            Log.e(TAG, "vaya 2: $it")
        }*/)
        binding.root.findViewById<RecyclerView>(R.id.recycler_view).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewModelAdapter
            //Log.e(TAG, "aca1: ${recycler_view?.let { "hola1" }}")
            //Log.e(TAG, "aca2: ${this?.let { "hola2" }}")

            tracker =  SelectionTracker.Builder<Long> (
                "mySelection",
                this,
                StableIdKeyProvider(this),
                MyItemDetailsLookup(this),
                StorageStrategy.createLongStorage()
            ).withSelectionPredicate(
                SelectionPredicates.createSelectAnything()
            ).build()

            Log.e(TAG, "aca3: ${tracker?.let { "hola3" }}")
        }

        binding.fabShowSources.setOnClickListener {
            showListDialogSources(it)
        }



        viewModelAdapter?.tracker = tracker

        return binding.root


    }

    class CheckBoxClick(val block: (SourceDomain) -> Unit) {
        fun onClick(view: View, source: SourceDomain) {
            //Log.e("From click", "v: ${view.id}")
            view.findViewById<CheckBox>(R.id.checkBox).apply {
               // isChecked = true
            }
            return block(source)
        }
    }

    class CardViewClick(val isSelected: (Boolean) -> Unit) {

        fun isSelecF(select: Boolean) = isSelected(select)
        /*fun onClick(view: View, source: SourceDomain) {
            return block(source)
        }
        */
       /* fun onLongClick(view: View) {
            return true
        }
        */
    }

    private fun listSourceToCharSequence(list: List<SourceDomain>): Array<CharSequence> {
        val csSource: MutableList<CharSequence> = ArrayList()
        for (i in list.indices) {
            csSource.add((list[i].name))
        }
        return csSource.toTypedArray()
    }

    private fun showListDialogSources(view: View){
        Log.e(TAG, "Click FAb")
       // showDialogList()

    }

    private fun showDialogList(){
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
