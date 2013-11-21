/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telzar.tools;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 *
 * @author boris
 */
public class JsonGenerator {
    private PreparedStatement _Statement    = null;
    private Writer _out                     = null;
    private String[] _columns               = null;
    private String _pref                     = null;
    
    public JsonGenerator(PreparedStatement statement, Writer out,String pref) throws SQLException {
        _Statement = statement;
        _out = out;
        _pref = pref;
        ResultSetMetaData m = _Statement.getMetaData();
        
        _columns = new String[m.getColumnCount()];
        
        for(int i=0; i<m.getColumnCount(); i++){
            _columns[i] = m.getColumnName(i+1);
        }
        
    }

    

    public void Generate() throws SQLException, IOException{
        ResultSet r = _Statement.executeQuery();
        boolean isNotFirstRow = false;
        while(r.next()) {
            
            if(isNotFirstRow){
                _out.write(",\n");
            }else{
                isNotFirstRow = true;
            }
            
            for(int i=0; i<_columns.length; i++){
                
                if(i == 0) {
                    if(_pref != null)
                        _out.write(_pref);
                    _out.write("{");
                }else{
                    _out.write(", ");
                }
                
                _out.write('\'');
                _out.write(_columns[i]);
                _out.write("':'");
                _out.write(r.getString(_columns[i]));
                _out.write('\'');
                //_out.flush();
            }
            _out.write(" }");
        }
        r.close();
    }
    
    
    
}
