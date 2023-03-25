package com.fitpass.libfitpass.fitpasschallenge.fitpasschallengeadapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fitpass.libfitpass.R
import com.fitpass.libfitpass.base.constants.FontIconConstant
import com.fitpass.libfitpass.base.http_client.ApiClient
import com.fitpass.libfitpass.base.utilities.FitpassPrefrenceUtil.USER_ID
import com.fitpass.libfitpass.base.utilities.FitpassPrefrenceUtil.getStringPrefs
import com.fitpass.libfitpass.base.utilities.Util
import com.fitpass.libfitpass.fitpasschallenge.fitpasschallengemodels.Challenge
import com.fitpass.libfitpass.fitpasschallenge.fitpasschallengemodels.UpcomingChallenge
import com.squareup.picasso.Picasso

class AllChallengesListAdapter(var mContext:Context, var mChallengeList:ArrayList<UpcomingChallenge>, var challengetype: Int,var mChallengeTyepList: ArrayList<Challenge>,var width : Int): RecyclerView.Adapter<AllChallengesListAdapter.ChallengeListHolder>()
{

    private var icon_inivted=0Xe9ed
    private var icon_own=0Xe9ec

    class ChallengeListHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var iv_banner = itemView.findViewById<ImageView>(R.id.iv_banner)
        var tv_days_of_challenge = itemView.findViewById<TextView>(R.id.tv_days_of_challenge)
        var fa_challenge_type = itemView.findViewById<TextView>(R.id.fa_challenge_type)
        var fa_invited = itemView.findViewById<TextView>(R.id.fa_invited)
        var btn_view = itemView.findViewById<Button>(R.id.btn_view)
        var fa_calendar = itemView.findViewById<TextView>(R.id.fa_calendar)
        var fa_members = itemView.findViewById<TextView>(R.id.fa_members)
        var tv_days = itemView.findViewById<TextView>(R.id.tv_days)
        var tv_members = itemView.findViewById<TextView>(R.id.tv_members)
        var tv_invited = itemView.findViewById<TextView>(R.id.tv_invited)
        var rl_banner = itemView.findViewById<RelativeLayout>(R.id.rl_banner)
        /*var tv_days_of_challenge = itemView.tv_days_of_challenge
        var fa_challenge_type = itemView.fa_challenge_type

        var fa_invited=itemView.fa_invited
        var btn_view=itemView.btn_view
        var fa_calendar=itemView.fa_calendar
        var fa_members=itemView.fa_members
        var tv_days=itemView.tv_days
        var tv_members=itemView.tv_members
        var tv_invited=itemView.tv_invited
        var rl_banner = itemView.rl_banner*/
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeListHolder
    {
        val view: View = LayoutInflater.from(mContext).inflate(R.layout.upcimg_past_ongoing_challenges, parent, false)
        return ChallengeListHolder(view)
    }
    override fun getItemCount(): Int
    {
        return mChallengeList.size
    }

    override fun onBindViewHolder(holder: ChallengeListHolder, position: Int) {
        var activity_holder = mChallengeList.get(position)
        if (activity_holder.challengeCreatedUserId.equals(
                getStringPrefs(
                    mContext,
                    USER_ID,
                    ""
                )
            )
        ) {
            //Util.setimage(holder.fa_invited, icon_own)
            holder.tv_invited.text = "own"

        } else {
            //Util.setimage(holder.fa_invited, icon_inivted)
            holder.tv_invited.text = "invited"
        }

        val params: RelativeLayout.LayoutParams =
            holder.rl_banner.layoutParams as RelativeLayout.LayoutParams
        params.height = (width / 1.5).toInt()
        params.width = width
        holder.rl_banner.layoutParams = params

        Log.e("ImageSize", "Image width=" + params.width + ", Image Height=" + params.height)



        //Util.setimage(holder.fa_calendar, FontIconConstant.icon_calendar)
       // Util.setimage(holder.fa_members, FontIconConstant.icon_amway_member)
       /* when (activity_holder.challengeType) {
            "1" -> Util.setimage(holder.fa_challenge_type, FontIconConstant.pushups)
            "2" -> Util.setimage(holder.fa_challenge_type, FontIconConstant.squats)
            "3" -> Util.setimage(holder.fa_challenge_type, FontIconConstant.plank)
            "4" -> Util.setimage(holder.fa_challenge_type, FontIconConstant.logameal)
            "5" -> Util.setimage(holder.fa_challenge_type, FontIconConstant.heartrate)
            "6" -> Util.setimage(holder.fa_challenge_type, FontIconConstant.sleep)
            "7" -> Util.setimage(holder.fa_challenge_type, FontIconConstant.running)
            // "8"->  Utils.setimage(holder.fa_challenge_type, Utils.daily_challenge)
            else -> Util.setimage(holder.fa_challenge_type, FontIconConstant.running)
        }*/

        holder.tv_days_of_challenge.text = activity_holder.challengeName
        holder.tv_days.text = activity_holder.challengeDaysMatrics

        Picasso.get().load(activity_holder.challengePhoto).resize(2048, 1600).onlyScaleDown()
            .into(holder.iv_banner);

        //Glide.with(mContext).load(activity_holder.challengePhoto).into(holder.iv_banner)

        holder.tv_members.text = activity_holder.numberOfMembers.toString() + " Members"
        holder.btn_view.setOnClickListener {
            var challengeData = Challenge()
            for (challenge in mChallengeTyepList) {
                if (mChallengeList.get(position).challengeType.equals(challenge.type_id)) {
                    challengeData = challenge
                    break
                }
            }
            Log.d("amwayChallengeId", activity_holder.amwayChallengeId)
            /* var intent= Intent(mContext,ChallengeDetails::class.java)
            intent.putExtra("challenge_id", activity_holder.amwayChallengeId)
            intent.putExtra("challengetype",challengetype)
            intent.putExtra("challengeData",challengeData)
            mContext.startActivity(intent)*/

            /*mContext.startActivity(Intent(mContext,ChallengeDetails::class.java).
            putExtra("challenge_id", activity_holder.amwayChallengeId).
            putExtra("challengetype",challengetype)
            .putExtra("challengeData",challengeData))*/
            /* if (mContext is TabContainerActivity) {
                (mContext as TabContainerActivity).overridePendingTransition(
                    R.anim.enter_in,
                    R.anim.enter_out
                )
            }
            else if (mContext is ChallengeActivity)
            {
                (mContext as ChallengeActivity).overridePendingTransition(R.anim.enter_in, R.anim.enter_out)
            }
        }*/

        }
    }


}