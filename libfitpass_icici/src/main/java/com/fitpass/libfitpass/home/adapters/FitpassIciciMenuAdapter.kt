package com.fitpass.libfitpass.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

import com.fitpass.libfitpass.base.utilities.Util
import com.fitpass.libfitpass.databinding.FitpassIciciMenuRowBinding
import com.fitpass.libfitpass.home.icicimenumodel.Product_List
import com.fitpass.libfitpass.home.listeners.FitpassiciciHomeListener


class FitpassIciciMenuAdapter (val fitpassHomeListener: FitpassiciciHomeListener): RecyclerView.Adapter<FitpassIciciMenuAdapter.ViewHolder>() {
    val list=  MutableLiveData<ArrayList<Product_List>>()
    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }
    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: FitpassIciciMenuRowBinding = FitpassIciciMenuRowBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }
    override fun getItemCount(): Int {
        if(list!!.value!=null){
            return list!!.value!!.size
        }else{
            return 0
        }
    }
    override  fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.product=list.value!!.get(position)

        holder.binding.llicon.background= Util.drawCircle("#"+list!!.value!!.get(position).bgcolor)

        holder.binding.llDetail.setOnClickListener {
            if(!list!!.value!!.get(position).redircet_url.isNullOrEmpty()){
                fitpassHomeListener.onMenuClick(list!!.value!!.get(position))
            }

        }

    }
    class ViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.getRoot()) {
        var binding:   FitpassIciciMenuRowBinding

        init {
            this.binding = binding as FitpassIciciMenuRowBinding
        }
    }
    fun updateItems(items: MutableLiveData<ArrayList<Product_List>>?) {
        if (items!!.value!=null&&items!!.value!!.size>0) {
            list.value = (items!!.value ?: emptyList()) as ArrayList<Product_List>?
            notifyDataSetChanged()
        }
    }
}