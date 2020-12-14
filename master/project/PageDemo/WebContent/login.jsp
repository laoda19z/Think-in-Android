<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style type="text/css">
	* {
		margin: 0px;
		}

	#content {
		margin: 150px auto;
		width: 100%;
		height: 460px;
		border: 1px transparent solid;
		background-color: #21D4FD;
		background-image: -moz-linear-gradient(243deg, #21D4FD 0%, #B721FF 100%); 
	}

	#box {
		margin: 50px auto;
		width: 30%;
		height: 360px;
		background-color: #fff;
		text-align: center;
		border-radius: 15px;
		border: 2px #fff solid;
		box-shadow: 10px 10px 5px #000000;
	}

	.title {
		line-height: 58px;
		margin-top: 20px;
		font-size: 36px;
		color: #000;
		height: 58px;
	}

	#box:hover {
		border: 2px #fff solid;
	}

	.input {
		margin-top: 20px;
	}

	input {
		margin-top: 5px;
		outline-style: none;
		border: 1px solid #ccc;
		border-radius: 3px;
		padding: 13px 14px;
		width: 70%;
		font-size: 14px;
		font-weight: 700;
		font-family: "Microsoft soft";
	}

	button {
		margin-top: 20px;
		border: none;
		color: #000;
		padding: 15px 32px;
		text-align: center;
		text-decoration: none;
		display: inline-block;
		font-size: 16px;
		border-radius: 15px;
		background-color: #CCCCCC;
			}
	button:hover{
		background-color: #B721FF;
		color: #fff;
	}
</style>
</head>
<body>

<div id="content">
	<div id="box">
		<div class="title">欢迎登录后台管理系统</div>
			<div class="input">
				<form action="j_security_check">
					用户名：<input type="text" id="username" name="j_username" /><br />
					密&nbsp;&nbsp;&nbsp;码：<input type="password" id="password" name="j_password" /><br />
					<input type="submit" value="确认登录" name="登录" />
				</form>
			</div>
	</div>
</div>
</body>
</html>