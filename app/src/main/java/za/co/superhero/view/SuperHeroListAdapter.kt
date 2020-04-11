package za.co.superhero.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_superhero.view.*
import za.co.superhero.R
import za.co.superhero.databinding.ItemSuperheroBinding
import za.co.superhero.model.SuperHero

class SuperHeroListAdapter(val superHeroList: ArrayList<SuperHero>) : RecyclerView.Adapter<SuperHeroListAdapter.superheroViewHolder>(),
    SuperHeroClickListener {

    fun updatesuperheroList(newsuperherosList: List<SuperHero>) {
        superHeroList.clear()
        superHeroList.addAll(newsuperherosList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): superheroViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<ItemSuperheroBinding>(inflater, R.layout.item_superhero, parent, false)
        return superheroViewHolder(view)
    }

    override fun getItemCount() = superHeroList.size

    override fun onBindViewHolder(holder: superheroViewHolder, position: Int) {
        holder.view.superhero = superHeroList[position]
        holder.view.listener = this
    }

    override fun onSuperHeroClicked(v: View) {
        val uuid = v.superheroId.text.toString().toInt()
        val action = ListFragmentDirections.actionListFragmentToDetailFragment()
        action.superHeroUuid = uuid
        Navigation.findNavController(v).navigate(action)
    }

    class superheroViewHolder(var view: ItemSuperheroBinding) : RecyclerView.ViewHolder(view.root)
}