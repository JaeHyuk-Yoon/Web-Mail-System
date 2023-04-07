<%--
    Document   : index.jsp
    Author     : jongmin
    Description:
        본 프로젝트의 목적은 SMTP/POP3를 사용하는 메일서버에 웹메일 인터페이스를 제공하여
        웹메일 시스템으로 사용할 수 있게 해주는데 있다.
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="cse.maven_webmail.control.CommandType"%>

<!DOCTYPE html>

<%
            if (session.isNew()) {
                session.setAttribute("host", application.getInitParameter("host"));   // should be modified if you change the POP3 server
                session.setAttribute("debug", "false");
                //session.setAttribute("pageno", "1");
                //session.setMaxInactiveInterval(session.getMaxInactiveInterval() * 2);
            }
%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>로그인 화면</title>
        <link type="text/css" rel="stylesheet" href="css/main_style.css" />
    </head>

    <body>
        <%-- <jsp:include page="header.jspf" /> --%>
        <%@include file="header.jspf"%>


        <div id="login_form">
            <form method="POST" action="Login.do?menu=<%= CommandType.LOGIN %>">
                사용자: <input type="text" name="userid" size="20"> <br />
                암&nbsp;&nbsp;&nbsp;호: <input type="password" name="passwd" size="20"> <br /> <br />
                <input type="submit" value="로그인" name="B1">&nbsp;&nbsp;&nbsp;
                <input type="reset" value="다시 입력" name="B2">
            </form>
        </div>


        <%@include file="footer.jspf"%>
        <%-- <jsp:include page="footer.jspf" /> --%>

    </body>
</html>
