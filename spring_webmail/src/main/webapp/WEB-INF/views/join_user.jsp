<%-- 
    Document   : join_user
    Created on : 2023. 5. 26., 오전 2:28:33
    Author     : JaeHyuk
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="deu.cse.spring_webmail.control.CommandType" %>

<!DOCTYPE html>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>회원 가입</title>
        <link type="text/css" rel="stylesheet" href="css/main_style.css" />
    </head>
    <body>
        <jsp:include page="header.jspf" />

        <div id="sidebar">
            <jsp:include page="sidebar_join_previous_menu.jsp" />
        </div>

        <div id="main">
            회원가입할 사용자 ID와 암호를 입력해 주시기 바랍니다. <br> <br>

            <form name="AddUser" action="add_user.do" method="POST">
                <table border="0" align="left">
                    <tr>
                        <td>사용자 ID</td>
                        <td> <input type="text" name="id" value="" size="20" />  </td>
                    </tr>
                    <tr>
                        <td>암호 </td>
                        <td> <input type="password" name="password" value="" /> </td>
                        <td> <input type="hidden" name="joinOrAdmin" value="join" /> </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <input type="submit" value="회원 가입" name="register" />
                            <input type="reset" value="초기화" name="reset" />
                        </td>
                    </tr>
                </table>

            </form>
        </div>

        <jsp:include page="footer.jspf" />
    </body>
</html>

