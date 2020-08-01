<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">

    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css"
          integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
    <title>Registration</title>
</head>
<%@include file="topNavigationMenu.jsp" %>
<body class="text-center">
<div class="container">
    <div class="row justify-content-center align-items-center" style="height:100vh">
        <div class="col-3 card card-body">
            <% if (request.getAttribute("userAlreadyExists") != null) {
            %>
            <jsp:text>User already exists</jsp:text>
            <% } %>
            <form method="post" autocomplete="off">
                <div class="form-group">
                    <input type="email" class="form-control" name="email" placeholder="some@some.com">
                </div>
                <div class="form-group">
                    <input type="text" class="form-control" name="username" placeholder="Login">
                </div>
                <div class="form-group">
                    <input type="password" class="form-control" name="password" placeholder="Password">
                </div>
                <button type="submit" class="btn btn-primary">Register</button>
            </form>
        </div>
    </div>
</div>
</body>
<%@include file="bottomNavigationMenu.jsp" %>
</html>