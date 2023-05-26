<%--
    Document   : sidebar_sent_menu
    Created on : 2023. 4. 17., 오전 2:35:24
    Author     : pcb
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <br> <br>

        <span style="color: indigo">
            <strong>사용자: <%= session.getAttribute("userid") %> </strong>
        </span> <br> <br>
       
        <p><a href="sent_mail"> 이전 메뉴로 </a></p>
    </body>
</html>
