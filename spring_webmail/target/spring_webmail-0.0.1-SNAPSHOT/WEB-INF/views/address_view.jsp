<%-- 
    Document   : address
    Created on : 2023. 5. 17., 오후 5:29:37
    Author     : User
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>

<!-- 제어기에서 처리하면 로직 관련 소스 코드 제거 가능!
<jsp:useBean id="pop3" scope="page" class="deu.cse.spring_webmail.model.Pop3Agent" />
<%
            pop3.setHost((String) session.getAttribute("host"));
            pop3.setUserid((String) session.getAttribute("userid"));
            pop3.setPassword((String) session.getAttribute("password"));
%>
-->

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>주소록 추가</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/my_style.css">
        <script>
            <c:if test="${!empty msg}">
                alert("${msg}");
            </c:if>
            </script>
    </head>
    <body>
        <%@include file="header.jspf"%>

        <div id="sidebar">
            <jsp:include page="sidebar_menu.jsp" />
        </div>
        <div id="main">
            <h1>주소록</h1>
            <hr/><!-- comment -->
            <table border="1">
                <thead>
                    <tr>
                        <th>이름</th>
                        <th>이메일</th>
                        <th>전화번호</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="row" items="${dataRows}">
                        <tr>
                            <td>${row.name}</td>
                            <td>${row.email}</td>
                            <td>${row.phone}</td>
                            <td><a href="">삭제</a></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

            <br><br><!-- comment -->

            <a href="${pageContext.request.contextPath}/address_insert">주소록 추가</a>
        </div>
        <%@include file="footer.jspf"%>
    </body>
</html>