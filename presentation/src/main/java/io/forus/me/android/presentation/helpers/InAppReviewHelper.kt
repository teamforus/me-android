package io.forus.me.android.presentation.helpers

import android.util.Log

object InAppReviewHelper {

    val timesForReview: IntArray = intArrayOf(1,3,5)//(3, 7, 12, 30, 60)

    fun getMaxPeriod(): Int{
        return timesForReview.max()?:3//60
    }

    fun resetAllRevData(){
        SharedPref.write(SharedPref.APP_REVIEW_TOTAL_COUNT_APP_LAUNCHES,0)
        SharedPref.write(SharedPref.APP_REVIEW_LAST_REQUEST_TIME,0)
        SharedPref.write(SharedPref.APP_REVIEW_COUNT_REQUESTS,0)
        SharedPref.write(SharedPref.APP_REVIEW_LAST_BUILD_VERSION,"")
    }


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

    public fun getReviewsCount(): Int{
        return SharedPref.read(SharedPref.APP_REVIEW_COUNT_REQUESTS,0)
    }

    public fun canLaunchInAppReviewDialog(): Boolean{

        val launchAppCount = SharedPref.read(SharedPref.APP_REVIEW_TOTAL_COUNT_APP_LAUNCHES,0)

        Log.d("inAppRev", " launchAppCount=$launchAppCount  getTimesFromLastRate()=${getTimesFromLastRate()}  getMaxPeriod()=${getMaxPeriod()}")


        if(launchAppCount < getMaxPeriod() && getReviewsCount() == 0) {

            return timesForReview.contains(launchAppCount)

        }else{
            return getTimesFromLastRate() == getMaxPeriod()
        }




    }

}