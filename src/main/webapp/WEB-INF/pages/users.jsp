<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="com.ivolodin.entities.Role" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">

    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css"
          integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
    <title>Users</title>
</head>
<%@include file="parts/topNavigationMenu.jsp" %>
<body>
<div class="container">
    <table class="table">
        <thead>
        <tr>
            <th scope="col">Id</th>
            <th scope="col">Email</th>
            <th scope="col">Nickname</th>
            <th scope="col">Name</th>
            <th scope="col">Surname</th>
            <th scope="col">Roles</th>

        </tr>
        </thead>
        <tbody>
        <c:forEach items="${userList}" var="user">
            <tr>
                <td>${user.id}</td>
                <td>${user.email}</td>
                <td>${user.userName}</td>
                <td>${user.name}</td>
                <td>${user.surname}</td>
                <td>
                    <form method="post">
                        <input type="hidden" name="userId" value="${user.id}">
                        <label>
                            <input type="checkbox"
                                   name="user"
                                    <c:out value="${user.roles.contains(Role.USER) ? 'checked' : ''}"/>
                            />
                            USER
                        </label>
                        <br>
                        <label>
                            <input type="checkbox"
                                   name="admin"
                                    <c:out value="${user.roles.contains(Role.ADMIN) ? 'checked' : ''}"/>
                            />
                            ADMIN
                        </label>
                        <button class="btn btn-info" type="submit">Submit</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

</div>
</body>
<%@include file="parts/bottomNavigationMenu.jsp" %>
</html>