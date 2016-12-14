<%@ page import="com.roc.Config.Environment" %><%--
  Created by IntelliJ IDEA.
  User: rocwu
  Date: 16/8/14
  Time: 下午10:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%if (Environment.isDebug) { %>
<script type="text/javascript" language="javascript" src="/statics/common/simditor/scripts/module.js"></script>
<script type="text/javascript" language="javascript" src="/statics/common/simditor/scripts/hotkeys.js"></script>
<script type="text/javascript" language="javascript" src="/statics/common/simditor/scripts/uploader.js"></script>
<script type="text/javascript" language="javascript" src="/statics/common/simditor/scripts/simditor.js"></script>
<%} else {%>
<script type="text/javascript" language="javascript" src="/statics/common/simditor/scripts/module.min.js"></script>
<script type="text/javascript" language="javascript" src="/statics/common/simditor/scripts/hotkeys.min.js"></script>
<script type="text/javascript" language="javascript" src="/statics/common/simditor/scripts/uploader.min.js"></script>
<script type="text/javascript" language="javascript" src="/statics/common/simditor/scripts/simditor.min.js"></script>
<%}%>
