<%@ page import="com.roc.Config.Environment" %><%--
  Created by IntelliJ IDEA.
  User: rocwu
  Date: 16/8/14
  Time: 下午10:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%if (Environment.isDebug) { %>
<script type="text/javascript" language="javascript" src="/statics/bootstrapvalidator/js/bootstrapValidator.js"></script>
<%} else {%>
<script type="text/javascript" language="javascript" src="/statics/bootstrapvalidator/js/bootstrapValidator.min.js.js"></script>
<%}%>
<!--获取url参数-->
<script type="text/javascript" language="javascript">
    function GetRequest() {
        var url = location.search; //获取url中"?"符后的字串
        var theRequest = {};
        if (url.indexOf("?") != -1) {
            var str = url.substr(1);
            var strs = str.split("&");
            for(var i = 0; i < strs.length; i ++) {
                theRequest[strs[i].split("=")[0]]=unescape(strs[i].split("=")[1]);
            }
        }
        return theRequest;
    }
</script>
