/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.spring_webmail.model;

import deu.cse.spring_webmail.entity.AddrBook;
import deu.cse.spring_webmail.repository.AddrBookRepository;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author User
 */
@Slf4j
@NoArgsConstructor        // 기본 생성자 생성
@AllArgsConstructor
@Service
public class AddressManager {

    private String mysqlServerIp;
    private String mysqlServerPort;
    private String userName;
    private String password;
    private String jdbcDriver;

    @Setter
    @Getter
    private String userid;
    
    @Autowired
    AddrBookRepository addrBookRepository;

//    public AddressManager() {
//        log.debug("AddrBookManager(): mysqlServerIp={}, jdbcDriver={}", mysqlServerIp, jdbcDriver);
//    }

    public AddressManager(String mysqlServerIp, String mysqlServerPort, String userName, String password, String jdbcDriver) {
        this.mysqlServerIp = mysqlServerIp;
        this.mysqlServerPort = mysqlServerPort;
        this.userName = userName;
        this.password = password;
        this.jdbcDriver = jdbcDriver;
        log.debug("AddrBookManager(): mysqlServerIp= {}. jdbcDriver= {}", mysqlServerIp, jdbcDriver);
    }

    public List<AddrBook> getAllRows() {
//        List<AddrBook> dataList = new ArrayList<>();
//        final String JDBC_URL = String.format("jdbc:mysql://%s:%s/webmail?serverTimezone=Asia/Seoul", this.mysqlServerIp, this.mysqlServerPort);
//
//        log.debug("JDBC_URL={}, mysqlServerIp={}, jdbcDriver={}", JDBC_URL, mysqlServerIp, jdbcDriver);
//
//        Connection conn = null;
//        PreparedStatement pstmt = null;
//        ResultSet rs = null;

        try {
//            Class.forName(jdbcDriver);
//            conn = DriverManager.getConnection(JDBC_URL, userName, password);
//            String sql = "SELECT email, name, phone FROM addrbook WHERE userid = ?";
//            pstmt = conn.prepareStatement(sql);
//            pstmt.setString(1, userid);
//            log.debug(pstmt.toString());
//            rs = pstmt.executeQuery();
//
//            while (rs.next()) {
//                String email = rs.getString("email");
//                String name = rs.getString("name");
//                String phone = rs.getString("phone");
//                dataList.add(new Address(email, name, phone));
//            }
//            if (rs != null) {
//                rs.close();
//            }
//            if (pstmt != null) {
//                pstmt.close();
//            }
//            if (conn != null) {
//                conn.close();
//            }
            List<AddrBook> dataList = addrBookRepository.findByUserId(userid);
            return dataList;
        } catch (Exception ex) {
            log.error("오류가 발생했습니다. (발생 오류: {})", ex.getMessage());
        }
        return null;
    }

    public void addRow(String email, String name, String phone) {
//        final String JDBC_URL = String.format("jdbc:mysql://%s:%s/webmail?serverTimezone=Asia/Seoul", mysqlServerIp, mysqlServerPort);
//        log.debug("JDBC_URL ={}", JDBC_URL);
//
//        Connection conn = null;
//        PreparedStatement pstmt = null;

        try {
//            Class.forName(jdbcDriver);
//            conn = DriverManager.getConnection(JDBC_URL, this.userName, this.password);
//            String sql = "INSERT into addrbook VALUES(?,?,?,?)";
//            pstmt = conn.prepareStatement(sql);
//            pstmt.setString(1, email);
//            pstmt.setString(2, name);
//            pstmt.setString(3, phone);
//            pstmt.setString(4, userid);
//            log.debug(pstmt.toString());
//            pstmt.executeUpdate();
//            
//            if (pstmt != null) {
//                pstmt.close();
//            }
//            if (conn != null) {
//                conn.close();
//            }
            AddrBook addrbook = new AddrBook();
            addrbook.setEmail(email);
            addrbook.setName(name);
            addrbook.setPhone(phone);
            addrbook.setUserId(userid);
            addrBookRepository.save(addrbook);
        } catch (Exception ex) {
            log.error("오류가 발생했씁니다. (발생 오류: {})", ex.getMessage());
        }
    }

    public void changeRow(String preemail, String prename, String prephone, String nexemail, String nexname, String nexphone) {
//        final String JDBC_URL = String.format("jdbc:mysql://%s:%s/webmail?serverTimezone=Asia/Seoul", mysqlServerIp, mysqlServerPort);
//        log.debug("JDBC_URL ={}", JDBC_URL);
//
//        Connection conn = null;
//        PreparedStatement pstmt = null;

        try {
//            Class.forName(jdbcDriver);
//            conn = DriverManager.getConnection(JDBC_URL, this.userName, this.password);
//            String sql = "UPDATE addrbook SET email = ?, name = ?, phone = ? WHERE email=? and name=? and phone=? and userid=?";
//            pstmt = conn.prepareStatement(sql);
//            pstmt.setString(1, nexemail);
//            pstmt.setString(2, nexname);
//            pstmt.setString(3, nexphone);
//            pstmt.setString(4, preemail);
//            pstmt.setString(5, prename);
//            pstmt.setString(6, prephone);
//            pstmt.setString(7, userid);
//            log.debug(pstmt.toString());
//            pstmt.executeUpdate();
//            
//            if (pstmt != null) {
//                pstmt.close();
//            }
//            if (conn != null) {
//                conn.close();
//            }
        
            if(preemail.equals(nexemail)) {
                AddrBook data = addrBookRepository.findByEmailAndNameAndPhoneAndUserId(preemail, prename, prephone, userid);
                data.setName(nexname);
                data.setPhone(nexphone);
                addrBookRepository.save(data);
            }
            else {
                AddrBook data = addrBookRepository.findByEmailAndNameAndPhoneAndUserId(preemail, prename, prephone, userid);
                addrBookRepository.delete(data);
                data.setEmail(nexemail);
                data.setName(nexname);
                data.setPhone(nexphone);
                addrBookRepository.save(data);
            }
            
        } catch (Exception ex) {
            log.error("오류가 발생했씁니다. (발생 오류: {})", ex.getMessage());
        }
    }

    public void removeRow(String email, String name, String phone) {
//        final String JDBC_URL = String.format("jdbc:mysql://%s:%s/webmail?serverTimezone=Asia/Seoul", mysqlServerIp, mysqlServerPort);
//        log.debug("JDBC_URL ={}", JDBC_URL);
//        log.debug("email ={}, name={}, phone={}", email, name, phone);
//        Connection conn = null;
//        PreparedStatement pstmt = null;

        try {
//            Class.forName(jdbcDriver);
//            conn = DriverManager.getConnection(JDBC_URL, this.userName, this.password);
//            String sql = "Delete from addrbook WHERE email = ? and name = ? and phone = ? and userid = ?";
//            pstmt = conn.prepareStatement(sql);
//            pstmt.setString(1, email);
//            pstmt.setString(2, name);
//            pstmt.setString(3, phone);
//            pstmt.setString(4, userid);
//            log.debug(pstmt.toString());
//            pstmt.executeUpdate();
//            
//            if (pstmt != null) {
//                pstmt.close();
//            }
//            if (conn != null) {
//                conn.close();
//            }
            AddrBook deleteTarget = new AddrBook();
            deleteTarget.setEmail(email);
            deleteTarget.setName(name);
            deleteTarget.setPhone(phone);
            addrBookRepository.save(deleteTarget);
        } catch (Exception ex) {
            log.error("오류가 발생했씁니다. (발생 오류: {})", ex.getMessage());
        }
    }
}
