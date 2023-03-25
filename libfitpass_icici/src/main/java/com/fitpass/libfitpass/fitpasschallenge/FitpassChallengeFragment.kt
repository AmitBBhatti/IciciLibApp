package com.fitpass.libfitpass.fitpasschallenge

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fitpass.libfitpass.R
import com.fitpass.libfitpass.base.constants.ConfigConstants
import com.fitpass.libfitpass.base.customview.CustomToastView
import com.fitpass.libfitpass.base.http_client.CustomLoader
import com.fitpass.libfitpass.base.utilities.FitpassPrefrenceUtil
import com.fitpass.libfitpass.base.utilities.Util
import com.fitpass.libfitpass.fitpasschallenge.fitpasschallengeadapters.AllChallengesListAdapter
import com.fitpass.libfitpass.fitpasschallenge.fitpasschallengemodels.Challenge
import com.fitpass.libfitpass.fitpasschallenge.fitpasschallengemodels.UpcomingChallenge
import com.fitpass.libfitpass.fitpasschallenge.listeners.FitpasssFragmentInteractionListener
import com.fitpass.libfitpass.home.http_client.ApiConstants
import com.fitpass.libfitpass.home.http_client.CommonRepository
import com.fitpass.libfitpass.home.http_client.HandleResponseListeners
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Response

class FitpassChallengeFragment:Fragment(),View.OnClickListener,
    HandleResponseListeners {
    private  var noOngoing: String = ""
    private var noUpComing: String=""
    private var noPasts: String=""
    private var width=0
    private var user_can_create_challenge: Boolean?=false
    var pastChallengeList =ArrayList<UpcomingChallenge>();
    var upcomingChallengeList =ArrayList<UpcomingChallenge>();
    var onGoingChallengeList =ArrayList<UpcomingChallenge>();
    var challengeList =ArrayList<Challenge>();

    lateinit var fa_add_challenge: TextView

    lateinit var view_ongoing: View
    lateinit var tv_ongoing: TextView
    lateinit var tv_upcoming: TextView
    lateinit var tv_past: TextView

    lateinit var view_upcoming: View
    lateinit var view_past: View

    private lateinit var rl_ongoing: RelativeLayout
    private lateinit var rl_upcoming: RelativeLayout
    private lateinit var rl_past: RelativeLayout
    private lateinit var cl_challenge_fragment: ConstraintLayout
    private lateinit var rl_no_data: LinearLayout
    private lateinit var no_data_found: TextView
    private lateinit var iv_toolbar_challenges: ImageView


    private lateinit var rv_all_challenge_list: RecyclerView
    private var icon_add_circle=0Xe97b
    private lateinit var pBar_challenges: ProgressBar
    private lateinit var mContext: Context
    private var OnFragmentInteractionListener:FitpasssFragmentInteractionListener?=null
    private var commonRepository: CommonRepository?=null
    override fun onStart() {
        super.onStart()
        OnFragmentInteractionListener?.onFragmentLoadingStart(2,ConfigConstants.CHALLENGE)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_all_challenges_list, container, false)
        // requireActivity()!!.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        initView(view)

        commonRepository= CommonRepository(requireContext(),requireActivity())
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext=context

        if(mContext is FitpasssFragmentInteractionListener)
            OnFragmentInteractionListener =mContext as FitpasssFragmentInteractionListener
    }

    override fun onResume() {
        super.onResume()
        // pBar_challenges.visibility=View.VISIBLE
        var hashMap=HashMap<String?,String?>()
        hashMap["user_id"]= FitpassPrefrenceUtil.getStringPrefs(mContext, FitpassPrefrenceUtil.USER_ID, "")!!
        // SecureRetroClient.instance?.callFormUrlApi(SecureConst.BASE_URL + SecureConst.AMWAY_GET_CHALLENGE, hashMap, this,mContext)
        commonRepository!!.apiCallNew(ApiConstants.AMWAY_GET_CHALLENGE, Util.convertHashMapToJSON(hashMap), this, true,ApiConstants.BASET_URL, ConfigConstants.ENCRYPTION_DYNAMIC_IV);

    }

    private fun initView(view: View) {
        iv_toolbar_challenges =view.findViewById(R.id.iv_toolbar_challenges) as ImageView

        iv_toolbar_challenges.visibility=if(requireActivity().intent.hasExtra("start_challenge_profile")) View.VISIBLE else View.GONE
       /* if(mContext is ChallengeActivity)
        {
            iv_toolbar_challenges.visibility=View.VISIBLE
        }*/
        iv_toolbar_challenges.visibility=View.VISIBLE

        iv_toolbar_challenges.setOnClickListener {
           /* if (mContext is ChallengeActivity) {
                var challengeActivity = mContext as ChallengeActivity
                challengeActivity.finishTask()
            }*/
            var challengeActivity = mContext as FitpassChallengeActivity
            challengeActivity.finishTask()
        }
        pBar_challenges = view.findViewById(R.id.pBar_challenges) as ProgressBar
        setImageSizeOnScreen()
        fa_add_challenge =view.findViewById(R.id.fa_add_challenge) as TextView

        tv_ongoing =view.findViewById(R.id.tv_ongoing) as TextView
        tv_upcoming =view.findViewById(R.id.tv_upcoming) as TextView
        tv_past =view.findViewById(R.id.tv_past) as TextView

        view_ongoing =view.findViewById(R.id.view_ongoing) as View
        view_upcoming =view.findViewById(R.id.view_upcoming) as View
        view_past =view.findViewById(R.id.view_past) as View

        rl_ongoing = view.findViewById(R.id.rl_ongoing) as RelativeLayout
        rl_upcoming = view.findViewById(R.id.rl_upcoming) as RelativeLayout
        rl_past = view.findViewById(R.id.rl_past) as RelativeLayout
        rl_no_data=view.findViewById(R.id.rl_no_data) as LinearLayout
        no_data_found=view.findViewById(R.id.no_data_found) as TextView

        rv_all_challenge_list=view.findViewById(R.id.rv_all_challenge_list) as RecyclerView
        cl_challenge_fragment=view.findViewById(R.id.cl_challenge_fragment) as ConstraintLayout

       // Util.setimage(fa_add_challenge,icon_add_circle)
        rl_ongoing.setOnClickListener {
            tv_ongoing.setTextColor(mContext.resources.getColor(R.color.peacock_blue))
            tv_upcoming.setTextColor(mContext.resources.getColor(R.color.blue_grey1))
            tv_past.setTextColor(mContext.resources.getColor(R.color.blue_grey1))

            view_ongoing.visibility=View.VISIBLE
            view_upcoming.visibility=View.GONE
            view_past.visibility=View.GONE

            if(onGoingChallengeList.size==0){
                rl_no_data.visibility=View.VISIBLE
                rv_all_challenge_list.visibility=View.GONE
                no_data_found.text=noOngoing
            }else {
                rl_no_data.visibility=View.GONE
                rv_all_challenge_list.visibility=View.VISIBLE
                rv_all_challenge_list.adapter = activity?.let {
                    AllChallengesListAdapter(it, onGoingChallengeList,1,challengeList,width) }
            }
        }
        rl_upcoming.setOnClickListener {
            tv_ongoing.setTextColor(mContext.resources.getColor(R.color.blue_grey1))
            tv_upcoming.setTextColor(mContext.resources.getColor(R.color.peacock_blue))
            tv_past.setTextColor(mContext.resources.getColor(R.color.blue_grey1))

            view_ongoing.visibility=View.GONE
            view_upcoming.visibility=View.VISIBLE
            view_past.visibility=View.GONE
            if(upcomingChallengeList.size==0){
                rl_no_data.visibility=View.VISIBLE
                rv_all_challenge_list.visibility=View.GONE
                no_data_found.text=noUpComing

            }else {
                rl_no_data.visibility=View.GONE
                rv_all_challenge_list.visibility=View.VISIBLE
                rv_all_challenge_list.adapter =
                    activity?.let { AllChallengesListAdapter(it, upcomingChallengeList,2,challengeList,width) }
            }
        }

        rl_past.setOnClickListener {
            tv_ongoing.setTextColor(mContext.resources.getColor(R.color.blue_grey1))
            tv_upcoming.setTextColor(mContext.resources.getColor(R.color.blue_grey1))
            tv_past.setTextColor(mContext.resources.getColor(R.color.peacock_blue))

            view_ongoing.visibility=View.GONE
            view_upcoming.visibility=View.GONE
            view_past.visibility=View.VISIBLE
            if(pastChallengeList.size==0){
                rl_no_data.visibility=View.VISIBLE
                rv_all_challenge_list.visibility=View.GONE
                no_data_found.text=noPasts

            }else {
                rl_no_data.visibility=View.GONE
                rv_all_challenge_list.visibility=View.VISIBLE
                rv_all_challenge_list.adapter =
                    activity?.let { AllChallengesListAdapter(it, pastChallengeList,3,challengeList,width) }
            }
        }
        fa_add_challenge.setOnClickListener {
            call_challenge_list()
        }
    }


    private fun setImageSizeOnScreen() {
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        width = displayMetrics.widthPixels-40


    }
    fun call_challenge_list(){
      /*  startActivity(Intent(activity,ChallengeList::class.java).putExtra("array_challenge_type",challengeList))
        requireActivity().overridePendingTransition(R.anim.enter_in,R.anim.enter_out)*/
    }





    override fun onClick(v: View?) {

    }



    override fun handleSuccess(response1: Response<JsonObject?>, api: String?) {
        var jsonObject=response1.body()
        var json = JSONObject(response1.body().toString())
        var responseCode = json!!.optInt("code")
        var message = json.optString("message")

        if (responseCode == 200) {
            val user_can_create_challenge = jsonObject!!.get("user_can_create_challenge").asBoolean
            if (user_can_create_challenge)
                fa_add_challenge.visibility = View.VISIBLE
            pastChallengeList.clear()
            val past_challenge = Gson().fromJson(
                jsonObject.getAsJsonArray("past_challenge"),
                Array<UpcomingChallenge>::class.java
            ).asList()
            pastChallengeList.addAll(past_challenge)
            upcomingChallengeList.clear()
            val upcoming_challenge = Gson().fromJson(
                jsonObject.getAsJsonArray("upcoming_challenge"),
                Array<UpcomingChallenge>::class.java
            ).asList()
            upcomingChallengeList.addAll(upcoming_challenge)
            onGoingChallengeList.clear()
            val ongoing_challenge = Gson().fromJson(
                jsonObject.getAsJsonArray("ongoing_challenge"),
                Array<UpcomingChallenge>::class.java
            ).asList()
            onGoingChallengeList.addAll(ongoing_challenge)

            Log.e("Challenge", "Challenge count pastChallengeList=" + past_challenge.size + ", upcoming_challenge=" + upcoming_challenge.size + ", ongoing_challenge=" + ongoing_challenge)
            challengeList.clear()
            val challenge = Gson().fromJson(jsonObject.getAsJsonArray("challenge_type"), Array<Challenge>::class.java).asList()
            challengeList.addAll(challenge)
            if (upcomingChallengeList.size == 0) {
                rl_no_data.visibility = View.VISIBLE
                rv_all_challenge_list.visibility = View.GONE
                noUpComing = json.getString("no_upcoming_challenge_tex")
            }
            if (pastChallengeList.size == 0) {
                rl_no_data.visibility = View.VISIBLE
                rv_all_challenge_list.visibility = View.GONE
                noPasts = json.getString("no_past_challenge_text")
            }
            tv_ongoing.setTextColor(mContext.resources.getColor(R.color.peacock_blue))
            tv_upcoming.setTextColor(mContext.resources.getColor(R.color.blue_grey1))
            tv_past.setTextColor(mContext.resources.getColor(R.color.blue_grey1))
            view_ongoing.visibility = View.VISIBLE
            view_upcoming.visibility = View.GONE
            view_past.visibility = View.GONE
            rv_all_challenge_list.layoutManager = LinearLayoutManager(
                activity,
                LinearLayoutManager.VERTICAL, false
            )
            if (onGoingChallengeList.size == 0) {
                rl_no_data.visibility = View.VISIBLE
                rv_all_challenge_list.visibility = View.GONE
                noOngoing = json.getString("no_ongoing_challenge_text")
            } else {
                rl_no_data.visibility = View.GONE
                rv_all_challenge_list.visibility = View.VISIBLE

                rv_all_challenge_list.adapter =
                    activity?.let {
                        AllChallengesListAdapter(it, onGoingChallengeList, 1, challengeList, width)
                    }
            }
            fa_add_challenge.isEnabled = true
            OnFragmentInteractionListener?.onFragmentLoadComplete(2, ConfigConstants.CHALLENGE)
            CustomLoader.hideLoaderDialog(requireActivity());
        }else{
            pBar_challenges.visibility=View.GONE
           // Utils.createDialogerror(cl_challenge_fragment, message, activity, false)
            OnFragmentInteractionListener?.onFragmentLoadFail(2,ConfigConstants.CHALLENGE)
            CustomLoader.Companion.hideLoaderDialog(requireActivity());
        }
    }

    override fun handleErrorMessage(response: String?, api: String?) {
        pBar_challenges.visibility=View.GONE
        // Utils.createDialogerror(cl_challenge_fragment, response, activity, false)
        OnFragmentInteractionListener?.onFragmentLoadFail(2,ConfigConstants.CHALLENGE)
        CustomToastView.errorToasMessage(requireActivity(), requireContext(), response);
        CustomLoader.Companion.hideLoaderDialog(requireActivity());
    }


}