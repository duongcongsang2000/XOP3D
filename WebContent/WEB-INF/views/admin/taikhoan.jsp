<%@ page pageEncoding="utf-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>3D SHOP</title>
<base href="${pageContext.servletContext.contextPath }" />
<style>
<%@ include file="/resources/css/reset.css"%>
<%@ include file="/resources/css/manager-pr.css"%>
</style>
</head>
<body>
	    <div class="header">
        <div class="container-header">
            <div class="left-header">
                <img src=" 3D_SHOP/resources/img/logo/logo-sub.png" alt="">
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
            <h1>${tb}</h1>
            <form class="add-pr" action="3D_SHOP/admin/grtaikhoan.htm"  method="post">
            <h1>Thêm Tài Khoản</h1>
            	<div class="input-fm">
                    <label for="username"><b>Tài khoản :</b></label>
                    <input name ="username">
                </div>
                <div class="input-fm">
                    <label for="password"><b>Mật khẩu :</b></label>
                    <input name ="password">
                </div>
                <div class="input-fm">
                    <label for="fullname"><b>Họ và tên :</b></label>
                    <input name ="fullname">
                </div>
                
                <div class = "type">  	
                <label ><b>Giới tính:</b></label>
                	<input type="radio" name = "gender" value="1">Nam
					<input type="radio" name = "gender" value="0">Nữ
				</div>
				<div class="input-fm">
                    <label for="birthday"><b>Ngày Sinh :</b></label>
                    <input name ="birthday">
                </div>
                <div class="input-fm">
                    <label for="email"><b>Email :</b></label>
                    <input name ="email">
                </div>
                <div class="input-fm">
                    <label for="phone"><b>Số Điện Thoại :</b></label>
                    <input name ="phone">
                </div>
                <div class="input-fm">
                    <label for="address"><b>Địa Chỉ :</b></label>
                    <input name ="address">
                </div>
				<div class="bt">
					<button>Thêm tài khoản</button>	
				</div>
            </form>
        </div>
        <div class="main-right">
            <table>
                <tr>
                	<th style="width: 120px;height: 30px" >Tài khoản</th>
                    <th>Mật khẩu</th>
                    <th>Họ và Tên</th>
                    <th>Giới tính</th>
                    <th>Ngày sinh</th>
                    <th>Email</th>
                    <th>Số ĐT</th>
                    <th>Địa Chỉ</th>
                    <th></th>
                    <th></th>
                </tr>
                <c:forEach var="a" items="${listtk}">
                		<tr style="align-items: center;">              	
                		<td style="width: 120px;height: 30px" >${a.username }</td>
                		<td>${a.password}</td>
                		<td>${a.fullname}</td>
                		<c:if test="${a.gender==1}">
                			<td>Nam</td>
                		</c:if>
                		<c:if test="${a.gender==0}">
                			<td>Nữ</td>
                		</c:if>
                		<td>${a.birthday}</td>
                		<td>${a.email}</td>
                		<td>${a.phone}</td>
                		<td>${a.address}</td>
                		<td><a title="Sửa sản phẩm" href="${pageContext.request.contextPath}/admin/edittk.htm?taikhoan=${a.username}" >Sửa</a></td>
                		<td><a href="${pageContext.request.contextPath}/admin/deletetk.htm?taikhoan=${a.username}" >Xóa</a> </td>
                </tr>
                </c:forEach>
            </table>
        </div>
    </div>
</body>
</html>