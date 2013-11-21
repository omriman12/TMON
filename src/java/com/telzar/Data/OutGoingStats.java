package com.telzar.Data;

import java.sql.ResultSet;

public class OutGoingStats {

    public String country_name ;
    public String country_code ;
    public String country_desc ;
    public String call_duration_h ;
    public String call_duration_m ;
    public String priority ;
    public String operator_prefix ;
    public String acd_h ;
    public String acd_m ;
    public String asr_h ;
    public String asr_m ;
    public String calls_h ;
    public String calls_m ;
    
    public OutGoingStats (ResultSet rs){
        try{
            this.country_name = rs.getString("country_name");
            this.country_code = rs.getString("country_code");
            //this.country_desc = rs.getString("country_desc");
            this.call_duration_h = rs.getString("call_duration_h");
            this.call_duration_m = rs.getString("call_duration_m");
            this.priority = rs.getString("priority");
            this.operator_prefix = rs.getString("operator_prefix");
            this.acd_h = rs.getString("acd_h");
            this.acd_m = rs.getString("acd_m");
            this.asr_h = rs.getString("asr_h");
            this.asr_m = rs.getString("asr_m");
            this.calls_h = rs.getString("calls_h");
            this.calls_m = rs.getString("calls_m");
        }catch(Exception e){
            
        }
    }
    
    public void setCountryName(String country_name){
        this.country_name = country_name;
    }
    
    public String getCountryName(){
        return this.country_name;
    }
    
    public void setCountryCode(String country_code){
        this.country_code = country_code;
    }
    
    public String getCountryCode(){
        return this.country_code;
    }
    
    public void setCountryDescription(String country_desc){
        this.country_desc = country_desc;
    }
    
    public String getCountryDescription(){
        return this.country_desc;
    }
    
    public void setOperatorPrefix(String country_code){
        this.operator_prefix = country_code;
    }
    
    public String getOperatorPrefix(){
        return this.operator_prefix;
    }
    
    public void setCallDurationHour(String call_duration_h){
        this.call_duration_h = call_duration_h;
    }
    
    public String getCallDurationHour(){
        return this.call_duration_h;
    }
    
    public void setCallDurationMinute(String call_duration_m){
        this.call_duration_m = call_duration_m;
    }
    
    public String getCallDurationMinute(){
        return this.call_duration_m;
    }
    
    public void setPriority(String priority){
        this.priority = priority;
    }
    
    public String getPriority(){
        return this.priority;
    }
    
    public void setAcdHour(String acd_h){
        this.acd_h = acd_h;
    }
    
    public String getAcdHour(){
        return this.acd_h;
    }
    
    public void setAcdMinute(String acd_m){
        this.acd_m = acd_m;
    }
    
    public String getAcdMinute(){
        return this.acd_m;
    }
    
    public void setAsrHour(String asr_h){
        this.asr_h = asr_h;
    }
    
    public String getAsrHour(){
        return this.asr_h;
    }
    
    public void setAsrMinute(String asr_m){
        this.asr_m = asr_m;
    }
    
    public String getAsrMinute(){
        return this.asr_m;
    }
    
    public void setCallsHour(String calls_h){
        this.calls_h = calls_h;
    }
    
    public String getCallsHour(){
        return this.calls_h;
    }
    
    public void setCallsMinute(String calls_m){
        this.calls_m = calls_m;
    }
    
    public String getCallsMinute(){
        return this.calls_m;
    }
    
}
