/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.spring_webmail.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author User
 */

@Slf4j
public class AddressManager {
    private String mysqlServerIp;
    private String mysqlServerPort;
    private String userName;
    private String password;
    private String jdbcDriver;
    
    @Setter @Getter private String userid;

    public AddressManager() {
        log.debug("AddrBookManager(): mysqlServerIp={}, jdbcDriver={}", mysqlServerIp, jdbcDriver);
    }

    public AddressManager(String mysqlServerIp, String mysqlServerPort, String userName, String password, String jdbcDriver) {
        this.mysqlServerIp = mysqlServerIp;
        this.mysqlServerPort = mysqlServerPort;
        this.userName = userName;
        this.password = password;
        this.jdbcDriver = jdbcDriver;
        log.debug("AddrBookManager(): mysqlServerIp= {}. jdbcDriver= {}", mysqlServerIp, jdbcDriver);
    }

    public List<Address> getAllRows() {
        List<Address> dataList = new ArrayList<>();
        final String JDBC_URL = String.format("jdbc:mysql://%s:%s/webmail?serverTimezone=Asia/Seoul", this.mysqlServerIp, this.mysqlServerPort);

        log.debug("JDBC_URL={}, mysqlServerIp={}, jdbcDriver={}", JDBC_URL, mysqlServerIp, jdbcDriver);

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            Class.forName(jdbcDriver);
            conn = DriverManager.getConnection(JDBC_URL, userName, password);
            String sql = "SELECT email, name, phone FROM addrbook WHERE userid = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userid);
            rs = pstmt.executeQuery(sql);

            while (rs.next()) {
                String email = rs.getString("email");
                String name = rs.getString("name");
                String phone = rs.getString("phone");
                dataList.add(new Address(email, name, phone));
            }
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (Exception ex) {
            log.error("오류가 발생했습니다. (발생 오류: {})", ex.getMessage());
        }
        return dataList;
    }


    public void addRow(String email, String name, String phone) {
        final String JDBC_URL = String.format("jdbc:mysql://%s:%s/webmail?serverTimezone=Asia/Seoul",mysqlServerIp, mysqlServerPort);
        log.debug("JDBC_URL ={}", JDBC_URL);

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            Class.forName(jdbcDriver);
            conn = DriverManager.getConnection(JDBC_URL, this.userName, this.password);
            String sql = "INSERT into addrbook VALUES(?,?,?,?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            pstmt.setString(2, name);
            pstmt.setString(3, phone);
            pstmt.setString(4, userid);

            pstmt.executeUpdate();
            if (pstmt != null) {
                pstmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (Exception ex) {
            log.error("오류가 발생했씁니다. (발생 오류: {})", ex.getMessage());
        }
    }
    
    public void removeRow(String email, String name, String phone) {
        final String JDBC_URL = String.format("jdbc:mysql://%s:%s/webmail?serverTimezone=Asia/Seoul",mysqlServerIp, mysqlServerPort);
        log.debug("JDBC_URL ={}", JDBC_URL);

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            Class.forName(jdbcDriver);
            conn = DriverManager.getConnection(JDBC_URL, this.userName, this.password);
            String sql = "Delete from addrbook WHERE email = ? and name = ? and phone = ? and userid = ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            pstmt.setString(2, name);
            pstmt.setString(3, phone);
            pstmt.setString(4, userid);

            pstmt.executeUpdate();
            if (pstmt != null) {
                pstmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (Exception ex) {
            log.error("오류가 발생했씁니다. (발생 오류: {})", ex.getMessage());
        }
    }
}
