<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">

    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css"
          integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
    <title>Stations</title>
</head>
<%@include file="parts/topNavigationMenu.jsp" %>
<body>
<div class="container">
    <div class="form-group">
        <sec:authorize access="hasAuthority('ADMIN')">
            <form class="form-inline justify-content-md-center pt-5" method="post">
                <label><input type="text" class="form-control ml-3" name="stationName"
                              placeholder="Station name"></label>
                <label>
                    <button type="submit" class="btn btn-primary ml-3">Add</button>
                </label>
            </form>
        </sec:authorize>
    </div>
    <table class="table">
        <thead>
        <tr>
            <th scope="col">Id</th>
            <th scope="col">Name</th>
            <th scope="col">Action</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${stations}" var="station">
            <tr>
                <td>${station.id}</td>
                <td>${station.name}</td>
                <td>
                    <div class="form-inline">
                        <sec:authorize access="hasAuthority('ADMIN')">
                            <form method="post">
                                <input type="hidden" name="stationId" value="${station.id}">
                                <button class="btn btn-danger ml-2" type="submit">Delete</button>
                            </form>
                        </sec:authorize>
                        <form method="get" action="/stations/${station.name}/trains/">
                            <button class="btn btn-primary ml-2" type="submit">Show trains</button>
                        </form>
                    </div>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
<%@include file="parts/bottomNavigationMenu.jsp" %>
</html>