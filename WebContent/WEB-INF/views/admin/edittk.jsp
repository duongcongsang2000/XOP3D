<%@ page pageEncoding="utf-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>3D SHOP</title>
<base href="${pageContext.servletContext.contextPath}"/>
<style>
<%@ include file="/resources/css/reset.css"%>
<%@ include file="/resources/css/editpr.css"%>
</style>
</head>
<body>
	<div class="header">
        <div class="container-header">
            <div class="left-header">
                <img src=" ${pageContext.request.contextPath}/resources/img/logo/logo-sub.png" alt="">
            </div>
            <div class="right-header">
                <ul>
                	<li><a href="../3D_SHOP/home/index.htm">Trang Chủ</a></li>
                    <li><a href="../3D_SHOP/admin/product.htm">Quản lý sản phẩm</a></li>
                    <li><a href="../3D_SHOP/admin/donhang.htm">Quản lý đơn hàng</a></li>
                    <li><a href="../3D_SHOP/admin/taikhoan.htm">Quản lý tài khoản</a></li>
                </ul>
            </div>
        </div>
    </div>
    <div class="main">
        <div class="main-left">
            <h1>Chỉnh Sửa Tài Khoản</h1>
            <form class="sign-up-form" action="${pageContext.request.contextPath}/admin/edittk.htm" method="POST">
           		<input style="display: none" type="text" value="${tk.username}" name="taikhoan" required>
                <div class="input-fm">
                    <label><b>Tài khoản</b></label>
                    <input style="width: 310px;height: 30px" type="text" value="${tk.username}" name="username" required>
                </div>
                <div class="input-fm">
                    <label><b>Mật khẩu</b></label>
                    <input style="width: 310px;height: 30px"  type="text" value="${tk.password}" name="password" required>
                </div>
                 <div class="input-fm">
                    <label><b>Họ và tên</b></label>
                    <input style="width: 310px;height: 30px"  type="text" value="${tk.fullname}" name="fullname" required>
                </div>
		        <div class = "type">  	
                <label ><b>Giới tính:</b></label>
                	<input type="radio" checked="checked" name = "gender" value="1">Nam
					<input type="radio" name = "gender" value="0">Nữ
				</div>
				<div class="input-fm">
                    <label for="birthday"><b>Ngày Sinh :</b></label>
                     <input style="width: 310px;height: 30px"  type="text" value="${tk.birthday}" name="birthday" required>
                </div>
                <div class="input-fm">
                    <label for="email"><b>Email :</b></label>
                    <input style="width: 310px;height: 30px"  type="text" value="${tk.email}" name="email" required>
                </div>
                <div class="input-fm">
                    <label for="phone"><b>Số Điện Thoại :</b></label>
                    <input style="width: 310px;height: 30px"  type="text" value="${tk.phone}" name="phone" required>
                </div>
                <div class="input-fm">
                    <label for="address"><b>Địa Chỉ :</b></label>
                    <input style="width: 310px;height: 30px"  type="text" value="${tk.address}" name="address" required>
                </div>   
                <button type="submit">Sửa</button>

            </form>
        </div>
    </div>
</body>
</html>