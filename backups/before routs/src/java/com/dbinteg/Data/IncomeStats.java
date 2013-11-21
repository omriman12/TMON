package com.dbinteg.Data;

import java.sql.ResultSet;

public class IncomeStats {

    public String trunk_owner ;
    public String country_name ;
    public String country_desc ;
    public String call_duration ;
    public String acd ;
    public String asr ;
    public String calls ;
    
    public IncomeStats (ResultSet rs){
        try{
            this.trunk_owner = rs.getString("trunk_owner");
            this.country_name = rs.getString("country_name");
            this.country_desc = rs.getString("country_desc"); 
            this.call_duration = rs.getString("call_duration") == null ? "0" : rs.getString("call_duration");
            this.acd = rs.getString("acd") == null ? "0" : rs.getString("acd");
            this.asr = rs.getString("asr") == null ? "0" : rs.getString("asr");
            this.calls = rs.getString("calls");
        }catch(Exception e){
            
        }
    }
    
    public void setTrunkOwner(String trunk_owner){
        this.trunk_owner = trunk_owner;
    }
    
    public String getTrunkOwner(){
        return this.trunk_owner;
    }
    
    public void setCountryName(String country_name){
        this.country_name = country_name;
    }
    
    public String getCountryName(){
        return this.country_name;
    }
    
    public void setCountryDescription(String country_desc){
        this.country_desc = country_desc;
    }
    
    public String getCountryDescription(){
        return this.country_desc;
    }
    
    public void setCallDuration(String call_duration){
        this.call_duration = call_duration;
    }
    
    public String getCallDuration(){
        return this.call_duration;
    }
    
    public void setAcd(String acd){
        this.acd = acd;
    }
    
    public String getAcd(){
        return this.acd;
    }
    
    public void setAsr(String asr){
        this.asr = asr;
    }
    
    public String getAsr(){
        return this.asr;
    }
    
    public void setCalls(String calls){
        this.calls = calls;
    }
    
    public String getCalls(){
        return this.calls;
    }
}

