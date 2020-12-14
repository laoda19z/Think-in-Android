<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath }" />
<!DOCTYPE html>
<html lang="en">
<head>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		
		<title>Pike Admin - Free Bootstrap 4 Admin Template</title>
		<meta name="description" content="Free Bootstrap 4 Admin Theme | Pike Admin">
		<!-- Favicon -->
		<link rel="shortcut icon" href="assets/images/favicon.ico">

		<!-- Bootstrap CSS -->
		<link href="assets/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
		
		<!-- Font Awesome CSS -->
		<link href="assets/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
		
		<!-- Custom CSS -->
		<link href="assets/css/style.css" rel="stylesheet" type="text/css" />
		
		<!-- BEGIN CSS for this page -->
		<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.16/css/dataTables.bootstrap4.min.css"/>
		<!-- END CSS for this page -->
		
</head>

<body class="adminbody">

<div id="main">

	<!-- top bar navigation -->
	<div class="headerbar">
		<!-- LOGO -->
        <div class="headerbar-left">
			<a href="index.html" class="logo"><img alt="Logo" src="headers/1.jpg" /> <span>乐动云</span></a>
        </div>
	</div>
	<!-- End Navigation -->
	
	<!-- Left Sidebar  -->
	<div class="left main-sidebar">	
		<div class="sidebar-inner leftscroll">
			<div id="sidebar-menu">
			<ul>
				<li class="submenu">
					<a class="OrdersServlet" href="index.jsp"><i class="fa fa-fw fa-bars"></i><span>用户</span> </a>
                </li>
				<li class="submenu">
                    <a href="ShowUserServlet"><i class="fa fa-fw fa-area-chart"></i><span>动态</span> </a>
                </li>
            </ul>
			</div>
		</div>
	</div>
	<!-- End Sidebar -->

	<div id="out">
	    
	    <br>
	</div>
    <div class="content-page">
		<!-- Start content -->
        <div class="content">
            <h2>用户</h2>	
            
           	    <div class="row">
           	    <c:forEach items="${page.list }" var="user">
		  <div id="in">
		    <span>用户id：${user.userId }</span><br>
		    <span>用户名：${user.username }</span><br>
		    <span>用户电话号：${user.phoneNum }</span><br>
		    <span>用户Email：${user.email }</span><br>
		    <span>用户真实姓名：${user.realname }</span><br>
		    <span>用户性别：${user.sex }</span><br>
		  </div>
	    </c:forEach>
	    <div id="page">
	                总共有${page.totalPageNum }页，共有${page.totalCount }个数据 <br>
	         <a href="showuser?page=1">首页</a>
	         <a href="showuser?page=${page.prePageNum }">上一页</a>
	         <a href="showuser?page=${page.nextPageNum }">下一页</a>
	         <a href="showuser?page=${page.totalPageNum }">末页</a>
	    </div>
	           	<div class="col-xs-12 col-sm-12 col-md-6 col-lg-6 col-xl-6">
					<div class="card mb-3">
						<div class="card-body">
						<c:forEach items="${orders }" var="order">
							<div class="alert alert-info" role="alert">
								蛋糕类型：${order.type }&nbsp&nbsp类型编号：${order.typeId }<br>
							</div>
							</c:forEach>
						</div>	
					</div>
				</div>
				<div class="col-xs-12 col-sm-12 col-md-6 col-lg-6 col-xl-6">
					<div class="card mb-3">
						<div class="card-body">
				
							<%-- <form class="alert alert-success" role="alert" action="${ctx }/AddOrderServlet">
								蛋糕类型：<input type="text" name="cakeType"><br>
								类型编号：<input type="text" name="cakeTypeId"><br>
								<input type="submit" value="确认增加">
							</form>
							
							<form class="alert alert-danger" role="alert" action="${ctx }/UpdateOrderServlet">
								类型编号：<input type="text" name="cakeTypeId"><br>
								请输入要对对应编号蛋糕类型修改后的信息：<br>
								蛋糕类型：<input type="text" name="cakeType"><br>
								<input type="submit" value="确认修改">
							</form>
							
							<form class="alert alert-warning" role="alert" action="${ctx }/DeleteOrderServlet">
								请根据左侧列表输入要删除的编号：<br>
								类型编号：<input type="text" name="cakeTypeId"><br>
								<input type="submit" value="确认删除">
							</form> --%>
						</div>
					</div>
				</div>
           	</div>
		</div>
		<!-- END content -->
    </div>
	<!-- END content-page -->
    
	<footer class="footer">
		<span class="text-right">
		Copyright <a target="_blank" href="#">Pike Admin</a>
		</span>
		<span class="float-right">
		More Templates <a href="http://www.cssmoban.com/" target="_blank" title="æ¨¡æ¿ä¹å®¶">蛋糕网站</a> - Collect from <a href="http://www.cssmoban.com/" title="ç½é¡µæ¨¡æ¿" target="_blank">联系方式</a>
		</span>
	</footer>
</div>
<!-- END main -->

<script src="assets/js/modernizr.min.js"></script>
<script src="assets/js/jquery.min.js"></script>
<script src="assets/js/moment.min.js"></script>
		
<script src="assets/js/popper.min.js"></script>
<script src="assets/js/bootstrap.min.js"></script>

<script src="assets/js/detect.js"></script>
<script src="assets/js/fastclick.js"></script>
<script src="assets/js/jquery.blockUI.js"></script>
<script src="assets/js/jquery.nicescroll.js"></script>

<!-- App js -->
<script src="assets/js/pikeadmin.js"></script>

<!-- BEGIN Java Script for this page -->
	<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.4.0/Chart.min.js"></script>
	<script src="https://cdn.datatables.net/1.10.16/js/jquery.dataTables.min.js"></script>
	<script src="https://cdn.datatables.net/1.10.16/js/dataTables.bootstrap4.min.js"></script>

	<!-- Counter-Up-->
	<script src="assets/plugins/waypoints/lib/jquery.waypoints.min.js"></script>
	<script src="assets/plugins/counterup/jquery.counterup.min.js"></script>			

	<script>
		$(document).ready(function() {
			// data-tables
			$('#example1').DataTable();
					
			// counter-up
			$('.counter').counterUp({
				delay: 10,
				time: 600
			});
		} );		
	</script>
	
	<script>
	var ctx1 = document.getElementById("lineChart").getContext('2d');
	var lineChart = new Chart(ctx1, {
		type: 'bar',
		data: {
			labels: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"],
			datasets: [{
					label: 'Dataset 1',
					backgroundColor: '#3EB9DC',
					data: [10, 14, 6, 7, 13, 9, 13, 16, 11, 8, 12, 9] 
				}, {
					label: 'Dataset 2',
					backgroundColor: '#EBEFF3',
					data: [12, 14, 6, 7, 13, 6, 13, 16, 10, 8, 11, 12]
				}]
				
		},
		options: {
						tooltips: {
							mode: 'index',
							intersect: false
						},
						responsive: true,
						scales: {
							xAxes: [{
								stacked: true,
							}],
							yAxes: [{
								stacked: true
							}]
						}
					}
	});


	var ctx2 = document.getElementById("pieChart").getContext('2d');
	var pieChart = new Chart(ctx2, {
		type: 'pie',
		data: {
				datasets: [{
					data: [12, 19, 3, 5, 2, 3],
					backgroundColor: [
						'rgba(255,99,132,1)',
						'rgba(54, 162, 235, 1)',
						'rgba(255, 206, 86, 1)',
						'rgba(75, 192, 192, 1)',
						'rgba(153, 102, 255, 1)',
						'rgba(255, 159, 64, 1)'
					],
					label: 'Dataset 1'
				}],
				labels: [
					"Red",
					"Orange",
					"Yellow",
					"Green",
					"Blue"
				]
			},
			options: {
				responsive: true
			}

	});


	var ctx3 = document.getElementById("doughnutChart").getContext('2d');
	var doughnutChart = new Chart(ctx3, {
		type: 'doughnut',
		data: {
				datasets: [{
					data: [12, 19, 3, 5, 2, 3],
					backgroundColor: [
						'rgba(255,99,132,1)',
						'rgba(54, 162, 235, 1)',
						'rgba(255, 206, 86, 1)',
						'rgba(75, 192, 192, 1)',
						'rgba(153, 102, 255, 1)',
						'rgba(255, 159, 64, 1)'
					],
					label: 'Dataset 1'
				}],
				labels: [
					"Red",
					"Orange",
					"Yellow",
					"Green",
					"Blue"
				]
			},
			options: {
				responsive: true
			}

	});
	</script>
<!-- END Java Script for this page -->

</body>
</html>