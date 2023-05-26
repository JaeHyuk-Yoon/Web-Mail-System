<%-- 
    Document   : address_insert
    Created on : 2023. 5. 17., 오후 8:10:02
    Author     : User
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
        <title>주소록 수정 폼</title>
        <link type="text/css" rel="stylesheet" href="css/main_style.css" />
    </head>
    <body>
        <%@include file="../header.jspf"%>

        <div id="sidebar">
            <jsp:include page="../sidebar_menu.jsp" />
        </div>
        <div id="main">
            <h1>주소록 수정</h1>
            <hr />

            <form action="update.do" method="POST">
                <table border="0">
                    <tbody>
                        <tr>
                            <td></td>
                            <td>변경 전</td>
                            <td>변경 후</td>
                        </tr>
                        <tr>
                            <td>이름</td>
                            <td><p id="prename">${prename}</p></td>
                            <td><input type="text" name="nexname" size="20" /></td>
                        </tr>
                        <tr>
                            <td>이메일</td>
                            <td>${preemail}</td>
                            <td>${nexemail}</td>
                        </tr>
                        <tr>
                            <td>전화번호</td>
                            <td>${prephone}</td>
                            <td><input type="text" name="nexphone" value="" size="20" /></td>
                        </tr>
                        <tr>
                            <td colspan="3">
                                <center>
                                    <input type="submit" value="수정" /> <input type="reset" value="초기화"/>
                                </center>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </form>
        </div>
        <%@include file="../footer.jspf"%>
    </body>
</html>