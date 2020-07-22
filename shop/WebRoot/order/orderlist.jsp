<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">
  <head>
  <base href="<%=basePath%>">
    <meta charset="utf-8">
    <title>购物商城管理系统</title>
    <meta content="IE=edge,chrome=1" http-equiv="X-UA-Compatible">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">

    <link rel="stylesheet" type="text/css" href="lib/bootstrap/css/bootstrap.css">
    
    <link rel="stylesheet" type="text/css" href="stylesheets/theme.css">
    <link rel="stylesheet" href="lib/font-awesome/css/font-awesome.css">

    <script src="lib/jquery-1.7.2.min.js" type="text/javascript"></script>

    <!-- Demo page code -->

    <style type="text/css">
        #line-chart {
            height:300px;
            width:800px;
            margin: 0px auto;
            margin-top: 1em;
        }
        .brand { font-family: georgia, serif; }
        .brand .first {
            color: #ccc;
            font-style: italic;
        }
        .brand .second {
            color: #fff;
            font-weight: bold;
        }
    </style>

    <!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
      <script src="lib/html5.js"></script>
    <![endif]-->

    <!-- Le fav and touch icons -->
    <link rel="shortcut icon" href="../assets/ico/favicon.ico">
    <link rel="apple-touch-icon-precomposed" sizes="144x144" href="../assets/ico/apple-touch-icon-144-precomposed.png">
    <link rel="apple-touch-icon-precomposed" sizes="114x114" href="../assets/ico/apple-touch-icon-114-precomposed.png">
    <link rel="apple-touch-icon-precomposed" sizes="72x72" href="../assets/ico/apple-touch-icon-72-precomposed.png">
    <link rel="apple-touch-icon-precomposed" href="../assets/ico/apple-touch-icon-57-precomposed.png">
  </head>

  <!--[if lt IE 7 ]> <body class="ie ie6"> <![endif]-->
  <!--[if IE 7 ]> <body class="ie ie7 "> <![endif]-->
  <!--[if IE 8 ]> <body class="ie ie8 "> <![endif]-->
  <!--[if IE 9 ]> <body class="ie ie9 "> <![endif]-->
  <!--[if (gt IE 9)|!(IE)]><!--> 
  <body class=""> 
  <!--<![endif]-->
    
    <%@ include file="../top.jsp" %>
    


    
    <%@ include file="../left.jsp" %>
    

	

    
    <div class="content">
        
        <div class="header">
            
            <h1 class="page-title">${title }</h1>
        </div>
        
                

        <div class="container-fluid">
            <div class="row-fluid">
<br/>
<form action="${url }" method="post">                    

     


账户：<input type="text" name="username"  value="${username}" class="input-xlarge"/>
订单号：<input type="text" name="orderid"  value="${orderid}" class="input-xlarge"/>
    	订单状态：
    	<select name="status">
    	<option value="">所有状态</option>
    	<option value="处理中" <c:if test="${status=='处理中' }">selected</c:if> >处理中</option>
    	<option value="已发货" <c:if test="${status=='已发货' }">selected</c:if> >已发货</option>
    	<option value="确认收货" <c:if test="${status=='确认收货' }">selected</c:if> >确认收货</option>
    	<option value="取消订单" <c:if test="${status=='取消订单' }">selected</c:if> >取消订单</option>

            <input type="submit" class="button button-small border-green"   value="查询" />
   
 

</form>

<div class="well">
    <table class="table">
      <thead>
        <tr>
        <th>账户</th>
          <th>订单号</th>

          <th>生成时间</th>
          <th>收货人姓名</th>
          <th>订单状态</th>
          <th>订单总价</th>
      
   
        
          <th >操作</th>
        </tr>
      </thead>
      <tbody>
      
      <c:forEach items="${list}" var="bean">
        <tr>
         <td> ${bean.user.username }</td>
          <td> ${bean.orderid }</td>
          <td>${bean.createtime }</td>
          <td> ${bean.name }</td>
          <td> ${bean.status }</td>
          <td> ￥${bean.totalprice}</td>
         
          
          <td>
           
        	 
       <c:if test="${bean.status=='处理中'}">
        <a href="${url2 }delete?id=${bean.id }" onclick="{if(confirm('确认发货?')){return true;}return false;}">发货</a>
        </c:if>
 &nbsp;&nbsp;&nbsp;
		<a href="${url2 }update3?id=${bean.id }">查看订单详情</a>
          </td>
        </tr>
        </c:forEach>
        
        
        
        
      </tbody>
    </table>
</div>
<div class="pagination">
    ${pagerinfo }
</div>





                    
                    
                    
            </div>
        </div>
    </div>
    


    <script src="lib/bootstrap/js/bootstrap.js"></script>
    <script type="text/javascript">
        $("[rel=tooltip]").tooltip();
        $(function() {
            $('.demo-cancel-click').click(function(){return false;});
        });
    </script>
    
  </body>
</html>
