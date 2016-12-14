<%@ page import="com.roc.Config.Environment" %>
<%--
  User: rocwu
  Date: 16/7/17
  Time: 下午5:16
  Desc: 引入必要的js文件.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%if (Environment.isDebug) { %>
<script type="text/javascript" language="javascript" src="/statics/require/js/jquery.js"></script>
<script type="text/javascript" language="javascript" src="/statics/bootstrap/js/bootstrap.js"></script>
<%} else {%>
<script type="text/javascript" language="javascript" src="/statics/require/js/jquery.min.js"></script>
<script type="text/javascript" language="javascript" src="/statics/bootstrap/js/bootstrap.min.js"></script>
<%}%>
<script type="text/javascript" language="javascript" src="/statics/require/js/validation.js"></script>
