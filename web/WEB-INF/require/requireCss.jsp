<%@ page import="com.roc.Config.Environment" %>
<%--
  User: rocwu
  Date: 16/7/17
  Time: 下午5:34
  Desc: 引入必要的jcss文件.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%if (Environment.isDebug) { %>
<link href="/statics/bootstrap/css/bootstrap.css" rel="stylesheet" type="text/css"/>
<link href="/statics/require/css/font-awesome/css/font-awesome.css" rel="stylesheet" type="text/css"/>
<%} else {%>
<link href="/statics/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
<link href="/statics/require/css/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
<%}%>
<link href="/statics/require/css/site.css" rel="stylesheet" type="text/css"/>

