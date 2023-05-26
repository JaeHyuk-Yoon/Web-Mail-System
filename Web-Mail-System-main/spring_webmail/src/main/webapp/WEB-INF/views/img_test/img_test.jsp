<%-- 
    Document   : img_test
    Created on : 2022. 6. 16., 오전 10:07:49
    Author     : Prof.Jong Min Lee
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>JSP Page</title>
        <%-- https://stackoverflow.com/questions/5614089/how-to-escape-and-in-facelets-el --%>
        <script>
            <c:if test="${!empty msg}">
            alert("${msg}");
            </c:if>
        </script>
    </head>
    <body>
        <h1>이미지 포함 테스트</h1>

        <p>
            이미지가 잘 포함되는지 테스트합니다.
        </p>

        <img src="${pageContext.request.contextPath}/get_image/ROS_mask_generator.png" width="60%" alt="ROS 마스크 생성">

    </body>
</html>
