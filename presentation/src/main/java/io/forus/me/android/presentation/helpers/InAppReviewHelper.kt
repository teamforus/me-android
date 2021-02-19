package io.forus.me.android.presentation.helpers

import android.util.Log

object InAppReviewHelper {

    val timesForReview: IntArray = intArrayOf(3, 7, 12, 30, 60)

    public fun resetTotalCountAppLaunch(){
        SharedPref.write(SharedPref.APP_REVIEW_TOTAL_COUNT_APP_LAUNCHES,0)
    }

    public fun incrementTotalLaunchCount(){
        var launchAppCount = SharedPref.read(SharedPref.APP_REVIEW_TOTAL_COUNT_APP_LAUNCHES,0)
        launchAppCount++
        SharedPref.write(SharedPref.APP_REVIEW_TOTAL_COUNT_APP_LAUNCHES,launchAppCount)
        Log.d("inAppRev","launchAppCount=$launchAppCount")
    }

    public fun writeCurrentAppVersion(buildVersion: String){
        SharedPref.write(SharedPref.APP_REVIEW_LAST_BUILD_VERSION,buildVersion)
    }



    public fun getLastRateAppVersion(): String?{
       return  SharedPref.read(SharedPref.APP_REVIEW_LAST_BUILD_VERSION,"")
    }

    public fun incrementLastLaunchCount(){
        var launchAppCount = SharedPref.read(SharedPref.APP_REVIEW_LAST_REQUEST_TIME,0)
        launchAppCount++
        SharedPref.write(SharedPref.APP_REVIEW_LAST_REQUEST_TIME,launchAppCount)
        Log.d("inAppRev","launchAppCount=$launchAppCount")
    }

    public fun getTimesFromLastRate(): Int{
        return  SharedPref.read(SharedPref.APP_REVIEW_LAST_REQUEST_TIME,0)
    }

    public fun resetTimesFromLastRate(){
        SharedPref.write(SharedPref.APP_REVIEW_LAST_REQUEST_TIME,0)
    }

    public fun incrementReviewsCount(){
        var reviewsCount = SharedPref.read(SharedPref.APP_REVIEW_COUNT_REQUESTS,0)
        reviewsCount++
        SharedPref.write(SharedPref.APP_REVIEW_COUNT_REQUESTS,reviewsCount)
    }

    public fun canLaunchInAppReviewDialog(): Boolean{

        var launchAppCount = SharedPref.read(SharedPref.APP_REVIEW_TOTAL_COUNT_APP_LAUNCHES,0)

        val maximumPeriod = timesForReview.max()?:60

        if(launchAppCount <= maximumPeriod) {

            return timesForReview.contains(launchAppCount)

        }/*else{



            //return getTimesFromLastRate() ==
        }*/

        return false


    }

}