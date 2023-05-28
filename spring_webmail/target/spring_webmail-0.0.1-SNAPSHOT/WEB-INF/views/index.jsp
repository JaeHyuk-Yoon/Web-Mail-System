<%-- 
    Document   : index
    Created on : 2022. 6. 10., 오후 2:19:43
    Author     : skylo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="deu.cse.spring_webmail.control.CommandType"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html> <!-- HTML5를 의미함 -->

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>로그인 화면</title>
        <link type="text/css" rel="stylesheet" href="css/main_style.css" />
        <script>
            <c:if test="${!empty msg}">
            alert("${msg}");
            </c:if>
        </script>
    </head>
    <body>
        <%@include file="header.jspf"%>


        <div id="login_form">
            <form method="POST" action="login.do?menu=<%= CommandType.LOGIN %>">
                사용자: <input type="text" name="userid" size="20" autofocus> <br />
                암&nbsp;&nbsp;&nbsp;호: <input type="password" name="passwd" size="20"> <br /> <br />
                <input type="submit" value="로그인" name="B1">&nbsp;&nbsp;&nbsp;
                <input type="reset" value="다시 입력" name="B2">&nbsp;&nbsp;&nbsp;
                <a href="join_user">회원 가입</a>
            </form>
        </div>


        <%@include file="footer.jspf"%>
    </body>
</html>
