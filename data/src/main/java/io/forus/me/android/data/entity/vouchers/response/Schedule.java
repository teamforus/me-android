package io.forus.me.android.data.entity.vouchers.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Schedule {

@SerializedName("id")
@Expose
private long id;
@SerializedName("office_id")
@Expose
private long officeId;
@SerializedName("week_day")
@Expose
private long weekDay;
@SerializedName("start_time")
@Expose
private String startTime;
@SerializedName("end_time")
@Expose
private String endTime;

/**
* No args constructor for use in serialization
* 
*/
public Schedule() {
}

/**
* 
* @param startTime
* @param id
* @param officeId
* @param weekDay
* @param endTime
*/
public Schedule(long id, long officeId, long weekDay, String startTime, String endTime) {
super();
this.id = id;
this.officeId = officeId;
this.weekDay = weekDay;
this.startTime = startTime;
this.endTime = endTime;
}

public long getId() {
return id;
}

public void setId(long id) {
this.id = id;
}

public long getOfficeId() {
return officeId;
}

public void setOfficeId(long officeId) {
this.officeId = officeId;
}

public long getWeekDay() {
return weekDay;
}

public void setWeekDay(long weekDay) {
this.weekDay = weekDay;
}

public String getStartTime() {
return startTime;
}

public void setStartTime(String startTime) {
this.startTime = startTime;
}

public String getEndTime() {
return endTime;
}

public void setEndTime(String endTime) {
this.endTime = endTime;
}

}