/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telzar.tools;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author boris
 */
public class JsonTool {
    public static void GenerateJSON(PreparedStatement s, Writer o) throws SQLException, IOException{
        GenerateJSON(s, o, null);
    }
  
    public static void GenerateJSON(Statement s, Writer o,String query, String rowPrefix) throws SQLException, IOException{
        ResultSet r = s.executeQuery(query);
        GenerateJSON(r, o, rowPrefix);
        r.close();
    }
    
    public static void GenerateJSON(PreparedStatement s, Writer o, String rowPrefix) throws SQLException, IOException{
        ResultSet r = s.executeQuery();
        GenerateJSON(r,o,rowPrefix);
        r.close();
    }
    
    public static void GenerateJSON(ResultSet r, Writer out, String rowPrefix) throws SQLException, IOException{
        ResultSetMetaData m = r.getMetaData();
        PrintWriter o = new PrintWriter( new BufferedWriter(out) ,true);
        String[] columns = new String[m.getColumnCount()];
        for(int i=0; i< columns.length; i++){
            columns[i] = m.getColumnName(i+1).toLowerCase();
        }
        
        boolean isNotFirstRow = false;
        while(r.next()) {
            if(isNotFirstRow){
                o.write(",\n");
            }else{
                isNotFirstRow = true;
            }
            
            for(int i=0; i<columns.length; i++){
                
                if(i == 0) {
                    if(rowPrefix != null){
                        o.write(rowPrefix);
                    }
                    o.write("{");
                }else{
                    o.write(", ");
                }
                
                o.write('\'');
                o.write(columns[i]);
                o.write("':'");
                String value = r.getString(columns[i]);
                if (value != null)
                    o.write(value);
                
                o.write('\'');
            }
            o.write(" }");
            
        }
        o.flush();
        r.close();
    }
    
}
