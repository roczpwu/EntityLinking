<%@ page import="com.roc.Config.Environment" %><%--
  Created by IntelliJ IDEA.
  User: rocwu
  Date: 16/8/14
  Time: 下午10:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%if (Environment.isDebug) { %>
<link href="/statics/bootstrapvalidator/css/bootstrapValidator.css" rel="stylesheet" type="text/css"/>
<%} else {%>
<link href="/statics/bootstrapvalidator/css/bootstrapValidator.min.css" rel="stylesheet" type="text/css"/>
<%}%>
