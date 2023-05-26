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
<jsp:useBean id="address" scope="page" class="deu.cse.spring_webmail.model.AddressManager" />
<%
            address.setUserid((String) session.getAttribute("userid"));
%>
-->

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>주소록 추가</title>
        <link type="text/css" rel="stylesheet" href="css/main_style.css" />
        <script>
            <c:if test="${!empty msg}">
                alert("${msg}");
            </c:if>
            </script>
    </head>
    <body>
        <%@include file="../header.jspf"%>

        <div id="sidebar">
            <jsp:include page="../sidebar_menu.jsp" />
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
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="row" items="${dataRows}">
                        <tr>
                            <td id = "name">${row.name}</td>
                            <td id = "email"><a href="write_mail?sender=${row.email}">${row.email}</a></td>
                            <td id = "phone">${row.phone}</td>
                            <td><a href="address_update?name=${row.name}&email=${row.email}&phone=${row.phone}"">수정</a></td>
                            <td><a href="delete.do?name=${row.name}&email=${row.email}&phone=${row.phone}" onclick="return confirm('삭제하시겠습니까?');">삭제</a></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

            <br><br><!-- comment -->

            <a href="address_insert">주소록 등록</a>
        </div>
        <%@include file="../footer.jspf"%>
    </body>
</html>