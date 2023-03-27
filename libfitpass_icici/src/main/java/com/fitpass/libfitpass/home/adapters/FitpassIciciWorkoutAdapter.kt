package com.fitpass.libfitpass.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.fitpass.libfitpass.R

import com.fitpass.libfitpass.base.utilities.Util
import com.fitpass.libfitpass.databinding.FitpassWorkoutRowBinding
import com.fitpass.libfitpass.home.icicimenumodel.Product_List
import com.fitpass.libfitpass.home.icicimenumodel.Workoutlist
import com.fitpass.libfitpass.home.listeners.FitpassiciciHomeListener


class FitpassIciciWorkoutAdapter (val context: Context,val fitpassHomeListener: FitpassiciciHomeListener): RecyclerView.Adapter<FitpassIciciWorkoutAdapter.ViewHolder>() {
    val list=  MutableLiveData<ArrayList<Workoutlist>>()
    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }
    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: FitpassWorkoutRowBinding = FitpassWorkoutRowBinding.inflate(layoutInflater, parent, false)
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
        holder.binding.workout=list.value!!.get(position)

       Glide.with(context).load(list.value!!.get(position).studio_logo).into(holder.binding.ivLogo)
        var data=list.value!!.get(position)
        holder.binding.tvDate.visibility=View.VISIBLE
        holder.binding.tvStatus.visibility=View.VISIBLE
            if (data.workout_status.equals("3")) {
                holder.binding.tvStatus.setTextColor(context.resources.getColor(R.color.green3))
               // holder.binding.tvDate.visibility=View.VISIBLE
                if (data.urc_updated_time != null) {
                    if (data.urc_updated_time > 0) {
                        holder.binding.tvDate.setText(Util.convertMiliesToDD_MM_HH_MMDateTime2(data.urc_updated_time.toString(), true))
                    }
                }
                else {
                    holder.binding.tvDate.setText(Util.convertMiliesToDD_MM_HH_MMDateTime2(data.start_time.toString(), true))
                }
            }
            else if (data.workout_status.equals("1")) {
                holder.binding.tvStatus.setTextColor(context.resources.getColor(R.color.orangedark))
                holder.binding.tvDate.setText(Util.convertMiliesToDD_MM_HH_MMDateTime2(data.start_time.toString(), true))
            }
            else {
                holder.binding.tvStatus.visibility=View.GONE
                holder.binding.tvDate.setText(Util.convertMiliesToDD_MM_HH_MMDateTime2(data.start_time.toString(), true))

            }


        holder.binding.llDetail.setOnClickListener {
            if (data.workout_status.equals("1")) {
               fitpassHomeListener.workOutClick(data)
           }

        }

    }
    class ViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.getRoot()) {
        var binding:   FitpassWorkoutRowBinding

        init {
            this.binding = binding as FitpassWorkoutRowBinding
        }
    }
    fun updateItems(items: MutableLiveData<ArrayList<Workoutlist>>?) {
        if (items!!.value!=null&&items!!.value!!.size>0) {
            list.value = (items!!.value ?: emptyList()) as ArrayList<Workoutlist>?
            notifyDataSetChanged()
        }
    }
}