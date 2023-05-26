<%-- 
    Document   : health
    Created on : 2023. 5. 10., 오후 6:43:36
    Author     : 손진제
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Health Check</title>
</head>
<body>
  <h1>Health Check</h1>
  <pre>${health.status()}</pre>
  <a href="/webmail/admin_menu">이전 메뉴</a>
</body>
</html>