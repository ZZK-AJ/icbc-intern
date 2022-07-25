package com.icbcintern.prepaycard.contract.dao;

import java.sql.*;

/**
 * description:数据库访问接口
 * @author: He Yihui
 * @create: 2022-07-21 15:50
 **/
public class WalletsDao {
    private static final String url;

    private static final String username;

    private static final String password;

    static {
        url = "jdbc:mysql://localhost2:3306/icbc?useSSL=false&useServerPrepStmts=true";
        username = "root";
        password = "root";
    }

    public static long QueryBalanceById(long wallet_id) {
        Connection conn = getConnect();
        PreparedStatement pstmt = null;
        String sql = "select balance from wallets where wallet_id = ?";
        long balance = -1;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, wallet_id);
            ResultSet res = pstmt.executeQuery();
            if (res.next()){
                balance = res.getLong("balance");
            }

            conn.commit();//提交数据
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.rollback();//回滚数据
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                conn.close();
                assert pstmt != null;
                pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return balance;
    }
    public static int Transfer(long from_id,long to_id,long money) {
        Connection conn = getConnect();
        PreparedStatement pstmt = null;
        String sql = "select balance from wallets where wallet_id = ?";
        String sql_update = "update wallets set balance = ? where wallet_id =?";
        int finish = 0;

        try {
            //查询转帐方余额
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, from_id);
            ResultSet res = pstmt.executeQuery();
            long fromBalance = 0;
            if (res.next()){
                fromBalance = res.getLong("balance");
            }
            if (fromBalance<money){
                throw new Exception("账户余额不足");
            }
            //查询收款方
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, to_id);
            res = pstmt.executeQuery();
            long toBalance = 0;
            if (res.next()){
                toBalance = res.getLong("balance");
            }else{
                throw new Exception("转帐方不存在");
            }

            //执行转账
            pstmt = conn.prepareStatement(sql_update);
            pstmt.setLong(1,fromBalance-money);
            pstmt.setLong(2, from_id);
            pstmt.executeUpdate();

            pstmt = conn.prepareStatement(sql_update);
            pstmt.setLong(1,toBalance+money);
            pstmt.setLong(2, to_id);
            pstmt.executeUpdate();

            conn.commit();//提交数据
            finish=1;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.rollback();//回滚数据
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                conn.close();
                assert pstmt != null;
                pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return finish;
    }

    private static Connection getConnect(){
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, username, password);
            //JDBC默认使用自动提交模式
            conn.setAutoCommit(false);//关闭自动提交
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return conn;
    }

}
