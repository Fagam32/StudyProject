<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">

    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css"
          integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
    <title>Trains on station</title>
</head>
<%@include file="parts/topNavigationMenu.jsp" %>
<body>
<div class="container">

    <table class="table">
        <thead>
        <tr>
            <th scope="col">Id</th>
            <th scope="col">Departure</th>
            <th scope="col">Going to</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${trains}" var="train">
            <tr>
                <td>${train.id}</td>
                <td>${DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy").format(train.departure)}</td>
                <td>${not empty train.toStation? train.toStation.name : "Last station"}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
<%@include file="parts/bottomNavigationMenu.jsp" %>
</html>