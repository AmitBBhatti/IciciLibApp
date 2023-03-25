package com.fitpass.libfitpass.base.CustomAdapters

import android.content.Context
import android.util.Log
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.fitpass.libfitpass.home.adapters.*
import com.fitpass.libfitpass.home.icicimenumodel.Workoutlist
import com.fitpass.libfitpass.scanqrcode.Adapers.FitpassScanListAdapter
import com.fitpass.libfitpass.scanqrcode.models.Workout
import com.fitpass.libfitpass.scanqrcode.viewmodel.ScanViewModel
import com.fitpass.libfitpass.home.listeners.FitpassHomeListener
import com.fitpass.libfitpass.home.listeners.FitpassiciciHomeListener
import com.fitpass.libfitpass.home.icicimenumodel.List
import com.fitpass.libfitpass.home.models.MacrosDetail
import com.fitpass.libfitpass.home.models.Product
import com.fitpass.libfitpass.home.models.SliderActivity
import com.fitpass.libfitpass.home.viewmodel.HomeViewModel
import com.fitpass.libfitpass.scanqrcode.listeners.FitpassScanListener


@BindingAdapter("menuItems","menuViewModel","menufitpassHomeListener")
fun bindproductItemsActivity(recyclerView: RecyclerView, list: MutableLiveData<ArrayList<Product>>?, homeViewModel: HomeViewModel,fitpassHomeListener: FitpassHomeListener) {
    val adapter = getProductItemsAdapter(recyclerView,homeViewModel,fitpassHomeListener)
    if (list!=null&&list!!.value!=null)
    {
        adapter.updateItems(list,)
    }
}
private fun getProductItemsAdapter(recyclerView: RecyclerView, homeViewModel: HomeViewModel,fitpassHomeListener: FitpassHomeListener): MenuAdapter {
    return if (recyclerView.adapter != null) {
        recyclerView.adapter as MenuAdapter
    } else {
        val bindableRecyclerAdapter = MenuAdapter(homeViewModel,fitpassHomeListener)
        recyclerView.layoutManager= LinearLayoutManager(recyclerView.context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = bindableRecyclerAdapter
        bindableRecyclerAdapter
    }
}




@BindingAdapter("upcomingItems","upcomingviewModel","fitpassHomeListener")
fun bindUpcomingIItemsActivity(vp: ViewPager, list: MutableLiveData<ArrayList<SliderActivity>>?, homeViewModel: HomeViewModel,fitpassHomeListener: FitpassHomeListener) {
    val adapter = getUpcomingIItemsAdapter(vp,homeViewModel,fitpassHomeListener)
   Log.d("upcomingItems","upcomingItems")
    if (list!=null&&list!!.value!=null)
    {
        adapter.updateItems(list)

    }
}
private fun getUpcomingIItemsAdapter(vp: ViewPager, homeViewModel: HomeViewModel,fitpassHomeListener: FitpassHomeListener): UpcomingAdapter {
    return if (vp.adapter != null) {
        vp.adapter as UpcomingAdapter
    } else {
        val bindableRecyclerAdapter = UpcomingAdapter(vp.context,homeViewModel,fitpassHomeListener)
        vp.adapter = bindableRecyclerAdapter
        bindableRecyclerAdapter
    }
}

@BindingAdapter("macroItems","macroViewmodel")
fun bindMacroItemsActivity(recyclerView: RecyclerView, list: MutableLiveData<ArrayList<MacrosDetail>>?, homeViewModel: HomeViewModel) {
    val adapter = getMaroItemsAdapter(recyclerView,homeViewModel)
    if (list!=null&&list!!.value!=null)
    {
        adapter.updateItems(list)
    }
}
private fun getMaroItemsAdapter(recyclerView: RecyclerView, homeViewModel: HomeViewModel): Macro_Adapter {
    return if (recyclerView.adapter != null) {
        recyclerView.adapter as Macro_Adapter
    } else {
        val bindableRecyclerAdapter = Macro_Adapter(homeViewModel)
        recyclerView.layoutManager=  GridLayoutManager(recyclerView.context,2)
        recyclerView.adapter = bindableRecyclerAdapter
        bindableRecyclerAdapter
    }
}

@BindingAdapter("scanItems","scanViewmodel","fitpassScanListener")
fun bindScanItemsActivity(recyclerView: RecyclerView, list: MutableLiveData<ArrayList<Workout>>?, scanViewModel: ScanViewModel, fitpassScanListener: FitpassScanListener) {
    val adapter = getScanItemsAdapter(recyclerView,scanViewModel,fitpassScanListener)
    if (list!=null&&list!!.value!=null)
    {
        adapter.updateItems(list)
    }
}
private fun getScanItemsAdapter(recyclerView: RecyclerView, scanViewModel: ScanViewModel, fitpassScanListener:FitpassScanListener): FitpassScanListAdapter {
    return if (recyclerView.adapter != null) {
        recyclerView.adapter as FitpassScanListAdapter
    } else {
        val bindableRecyclerAdapter = FitpassScanListAdapter(scanViewModel,fitpassScanListener)
        recyclerView.layoutManager= LinearLayoutManager(recyclerView.context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = bindableRecyclerAdapter
        bindableRecyclerAdapter
    }
}


/*icici adapter*/

@BindingAdapter("icicimenuItems","icicimenuListener")
fun bindproductItemsActivity(recyclerView: RecyclerView,
                             list: MutableLiveData<ArrayList<com.fitpass.libfitpass.home.icicimenumodel.Product_List>>?,
                             fitpassHomeListener: FitpassiciciHomeListener
) {
    val adapter = getIciciProductItemsAdapter(recyclerView,fitpassHomeListener)
    if (list!=null&&list!!.value!=null)
    {
        adapter.updateItems(list,)
    }
}

private fun getIciciProductItemsAdapter(recyclerView: RecyclerView, fitpassHomeListener: FitpassiciciHomeListener): FitpassIciciMenuAdapter {
    return if (recyclerView.adapter != null) {
        recyclerView.adapter as FitpassIciciMenuAdapter
    } else {
        val bindableRecyclerAdapter = FitpassIciciMenuAdapter(fitpassHomeListener)
      //  recyclerView.layoutManager= LinearLayoutManager(recyclerView.context, LinearLayoutManager.VERTICAL, false)
        var linearLayoutManager = object : LinearLayoutManager(recyclerView.context,
            LinearLayoutManager.VERTICAL, false) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = bindableRecyclerAdapter
        bindableRecyclerAdapter
    }
}

@BindingAdapter("iciciWorkoutItems","iciciworkoutListener")
fun bindWorkoutItemsActivity(recyclerView: RecyclerView,
                             list: MutableLiveData<ArrayList<Workoutlist>>?,
                             fitpassHomeListener: FitpassiciciHomeListener
) {
    val adapter = getWorkoutProductItemsAdapter(recyclerView,fitpassHomeListener,recyclerView.context)
    if (list!=null&&list!!.value!=null)
    {
        adapter.updateItems(list,)
    }
}

private fun getWorkoutProductItemsAdapter(recyclerView: RecyclerView, fitpassHomeListener: FitpassiciciHomeListener,context: Context): FitpassIciciWorkoutAdapter {
    return if (recyclerView.adapter != null) {
        recyclerView.adapter as FitpassIciciWorkoutAdapter
    } else {
        val bindableRecyclerAdapter = FitpassIciciWorkoutAdapter(context,fitpassHomeListener)
       // recyclerView.layoutManager= LinearLayoutManager(recyclerView.context, LinearLayoutManager.VERTICAL, false)
        var linearLayoutManager = object : LinearLayoutManager(recyclerView.context,
            LinearLayoutManager.VERTICAL, false) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        recyclerView.layoutManager = linearLayoutManager

        recyclerView.adapter = bindableRecyclerAdapter
        bindableRecyclerAdapter
    }
}

@BindingAdapter("faqItems")
fun bindFaqItemsActivity(recyclerView: RecyclerView, list: MutableLiveData<ArrayList<List>>?) {
    val adapter = getFaqItemsAdapter(recyclerView)
    if (list!=null&&list!!.value!=null)
    {
        adapter.updateItems(list)
    }
}
private fun getFaqItemsAdapter(recyclerView: RecyclerView): FaqAdapter {
    return if (recyclerView.adapter != null) {
        recyclerView.adapter as FaqAdapter
    } else {
        val bindableRecyclerAdapter = FaqAdapter()
        recyclerView.layoutManager= LinearLayoutManager(recyclerView.context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = bindableRecyclerAdapter
        bindableRecyclerAdapter
    }
}