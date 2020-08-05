<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">

    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css"
          integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
    <title>Train path and tickets</title>
</head>
<%@include file="parts/topNavigationMenu.jsp" %>
<body>
<div class="container">
    <c:out value="${not empty message? message : ''}"/>

    <table class="table" >
        <caption style="caption-side: top">Train path</caption>
        <thead>
        <tr>
            <th scope="col">Station name</th>
            <th scope="col">Arrival</th>
            <th scope="col">Seats left</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td>${path.get(0).stationConnect.from.name}</td>
            <td>${DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy").format(train.departure)}</td>
            <td>${path.get(0).seatsLeft}</td>
        </tr>
        <c:forEach begin="0" end="${path.size() - 1}" var="i">
            <tr>
                <td>${path.get(i).stationConnect.to.name}</td>
                <td>${DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy").format(path.get(i).arrival)}</td>
                <td>${path.get(i).seatsLeft}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <sec:authorize access="hasAuthority('ADMIN')">
        <table class="table">
            <caption style="caption-side: top">Tickets</caption>
            <thead>
            <tr>
                <th scope="col">From</th>
                <th scope="col">To</th>
                <th scope="col">Username</th>
                <th scope="col">Name</th>
                <th scope="col">Surname</th>
                <th scope="col">Birthday</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${tickets}" var="ticket">
                <tr>
                    <td>${ticket.frStation.name}</td>
                    <td>${ticket.toStation.name}</td>
                    <td>${ticket.user.userName}</td>
                    <td>${ticket.user.name}</td>
                    <td>${ticket.user.surname}</td>
                    <td>${ticket.user.birthDate}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </sec:authorize>
</div>
</body>
<%@include file="parts/bottomNavigationMenu.jsp" %>
</html>