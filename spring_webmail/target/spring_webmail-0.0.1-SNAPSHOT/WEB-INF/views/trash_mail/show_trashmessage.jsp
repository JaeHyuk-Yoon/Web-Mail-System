<%--
    Document   : show_trashmessage.jsp
    Created on : 2023. 5. 17., 오전 4:11:29
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
            <jsp:include page="sidebar_trash_menu.jsp" />
        </div>

        <div id="msgBody">
            ${msg}
        </div>

        <%@include file="../footer.jspf"%>
    </body>
</html>