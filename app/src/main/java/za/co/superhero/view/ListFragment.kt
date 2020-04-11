package za.co.superhero.view

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_list.*
import za.co.superhero.R
import za.co.superhero.util.ShakeEventManager
import za.co.superhero.viewmodel.ListViewModel


class ListFragment : Fragment(), ShakeEventManager.ShakeListener {

    private lateinit var viewModel: ListViewModel
    private val superherosListAdapter = SuperHeroListAdapter(arrayListOf())
    private var sd: ShakeEventManager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        setHasOptionsMenu(true)
        sd = ShakeEventManager()
        sd!!.setListener(this)
        sd!!.init(this.requireContext())

        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)
        viewModel.refresh()

        superHeroList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = superherosListAdapter
        }

        refreshLayout.setOnRefreshListener {
            refreshByPassCache()
        }

        observeViewModel()
    }

    fun refreshByPassCache()
    {
        superHeroList.visibility = View.GONE
        listError.visibility = View.GONE
        loadingView.visibility = View.VISIBLE
        viewModel.refreshBypassCache()
        refreshLayout.isRefreshing = false
    }

    fun observeViewModel() {
        viewModel.superHeros.observe(this, Observer { superheros ->
            superheros?.let {
                superHeroList.visibility = View.VISIBLE
                superherosListAdapter.updatesuperheroList(superheros)
            }
        })

        viewModel.superHeroLoadError.observe(this, Observer { isError ->
            isError?.let {
                listError.visibility = if(it) View.VISIBLE else View.GONE
            }
        })

        viewModel.loading.observe(this, Observer { isLoading ->
            isLoading?.let {
                loadingView.visibility = if(it) View.VISIBLE else View.GONE
                if(it) {
                    listError.visibility = View.GONE
                    superHeroList.visibility = View.GONE
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
            super.onCreateOptionsMenu(menu, inflater)
            inflater.inflate(R.menu.list_menu, menu)
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            when(item.itemId) {
                R.id.actionDelete -> {
                    view?.let {
                            AlertDialog.Builder(it.context)
                                .setTitle("Are you sure you want to delete all superheros?")
                                .setPositiveButton("Yes") { _, _ -> viewModel.ClearDatabase() }
                                .setNegativeButton("No") { _, _ ->  }
                                .create().show()
                    }
                }
            }

            return super.onOptionsItemSelected(item)
        }

    override fun onShake() {
        refreshByPassCache()
    }


   override fun onResume() {
        super.onResume()
        sd!!.register()
    }


    override fun onPause() {
        super.onPause()
        sd!!.deregister()
    }
}