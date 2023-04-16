<%-- 
    Document   : show_sentmessage
    Created on : 2023. 4. 10., 오전 3:57:07
    Author     : pcb
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>메일 보기 화면</title>
        <link type="text/css" rel="stylesheet" href="css/main_style.css" />
    </head>
    <body>
        <%@include file="../header.jspf"%>

        <div id="sidebar">
            <jsp:include page="sidebar_sent_menu.jsp" />
        </div>

        <div id="msgBody">
            ${msg}
        </div>

        <%@include file="../footer.jspf"%>
    </body>
</html>