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

class SuperHeroListAdapter(val superHeroList: ArrayList<SuperHero>) : RecyclerView.Adapter<SuperHeroListAdapter.DogViewHolder>(),
    SuperHeroClickListener {

    fun updateDogList(newDogsList: List<SuperHero>) {
        superHeroList.clear()
        superHeroList.addAll(newDogsList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<ItemSuperheroBinding>(inflater, R.layout.item_superhero, parent, false)
        return DogViewHolder(view)
    }

    override fun getItemCount() = superHeroList.size

    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {
        holder.view.superhero = superHeroList[position]
        holder.view.listener = this
    }

    override fun onSuperHeroClicked(v: View) {
        val uuid = v.superheroId.text.toString().toInt()
        val action = ListFragmentDirections.actionListFragmentToDetailFragment()
        action.superHeroUuid = uuid
        Navigation.findNavController(v).navigate(action)
    }

    class DogViewHolder(var view: ItemSuperheroBinding) : RecyclerView.ViewHolder(view.root)
}