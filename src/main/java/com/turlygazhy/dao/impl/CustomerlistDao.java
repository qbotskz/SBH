package com.turlygazhy.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerlistDao {

    private final Connection connection;

    public CustomerlistDao(Connection connection) {
        this.connection = connection;
    }


    public void insert(long CHAT_ID, String USER_NAME, String SURE_NAME) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("INSERT INTO PUBLIC.CUSTOMERLIST (ID,CHAT_ID,USER_NAME,SURE_NAME) VALUES (DEFAULT, ?, ?, ?)");
        ps.setLong(1, CHAT_ID);
        ps.setString(2, USER_NAME);
        ps.setString(3, SURE_NAME);
        ps.execute();
    }


   /* public List<String> getArkhivTaskText() throws SQLException {

        PreparedStatement ps = connection.prepareStatement("SELECT * FROM CUSTOMERLIST");
        ps.execute();
        int i = 0;
        List<String> list = new ArrayList<>();
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            i++;
            String user = rs.getString("USER_NAME") +" " + rs.getString("SURE_NAME")
            list.add(i," ");
        }

        return list;
    }*/

    public List<String> get() throws SQLException {
        List<String> stringList = new ArrayList<>();
        String string;
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM CUSTOMERLIST");
        ps.execute();
        ResultSet resultSet = ps.getResultSet();
        while (resultSet.next()) {
            string = new String();
            string = resultSet.getString(2) + " " + resultSet.getString(3);
            stringList.add(string);
        }
        return stringList;
    }

    public List<Map<String, Object>> getList() throws SQLException {
        List<Map<String, Object>> list = new ArrayList<>();

        PreparedStatement ps = connection.prepareStatement("SELECT * FROM CUSTOMERLIST");
        ps.execute();
        ResultSet resultSet = ps.getResultSet();
        while (resultSet.next()) {
            Map<String, Object> map = new HashMap<>();
            map.put("user_name", resultSet.getString(2));
            map.put("sure_name", resultSet.getString(3));
            map.put("chat_id", resultSet.getLong(4));
            list.add(map);

        }
        return list;
    }
}
