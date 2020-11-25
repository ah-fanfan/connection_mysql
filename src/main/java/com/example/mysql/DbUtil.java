package com.example.mysql;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author :GongFan
 * @ClassName : DbUtil.java
 * @Description :TODO
 * @createTime :2020年11月22日 15:10:00
 */
public class DbUtil {

    private static String username = "root";

    private static String password = "root";

    private PreparedStatement ps = null;

    private ResultSet resultSet = null;

    private Connection connection = null;

    private static String url = "jdbc:mysql://localhost:3306/gongfan?useUnicode=true&characterEncoding=utf-8";


    static {
        try {//加载MySql的驱动类
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("找不到驱动程序类 ，加载驱动失败！");
            e.printStackTrace();
        }
    }


    public Connection getConnection() {
        try {
            connection = DriverManager.getConnection(url, username, password);
            return connection;
        } catch (SQLException se) {
            System.out.println("数据库连接失败！");
            se.printStackTrace();
            return null;
        }
    }


    public List<Map<String, Object>> query(String sql, Object... objs) {
        List<Map<String, Object>> list = new ArrayList<>();
        connection = getConnection();
        try {
            ps = connection.prepareStatement(sql);
            for (int i = 0; i < objs.length; i++) {
                ps.setObject(i + 1, objs);
            }
            resultSet = ps.executeQuery();
            ResultSetMetaData rsmd = resultSet.getMetaData();
            while (resultSet.next()) {
                Map<String, Object> map = new HashMap<>();
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    String key = rsmd.getColumnName(i);
                    Object value = resultSet.getObject(key);
                    map.put(key, value);
                }
                list.add(map);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return list;
    }


    public void close() {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }



    public static  void main(String []args){
        DbUtil db=new DbUtil();
        List<Map<String,Object>> list=db.query("select * from user");
        System.out.println(list);
    }


}
