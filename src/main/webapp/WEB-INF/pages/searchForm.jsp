<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">

    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css"
          integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
    <title>Search</title>
</head>
<body>
<%@include file="parts/topNavigationMenu.jsp" %>
<div class="container">
    <div class="form-group">
        <form class="form-inline justify-content-md-center pt-5" method="post">
            <label><input type="text" class="form-control ml-3" name="stationFrom" placeholder="From"></label>
            <label><input type="text" class="form-control ml-3" name="stationTo" placeholder="To"></label>
            <label><input type="date" class="form-control ml-3" name="date"></label>
            <label>
                <button type="submit" class="btn btn-primary ml-3">Search</button>
            </label>
        </form>
    </div>
    <div class="justify-content-md-center align-items-center">
        <table class="table">
            <thead>
            <tr>
                <th scope="col">Id</th>
                <th scope="col">From</th>
                <th scope="col">To</th>
                <th scope="col">Departure</th>
                <th scope="col">Arrival</th>
                <th scope="col">Seats left</th>
                <th scope="col">Action</th>
            </tr>
            </thead>
            <tbody><c:forEach items="${trains}" var="train">
                <tr>
                    <td>${train.id}</td>
                    <td>${train.fromStation.name}</td>
                    <td>${train.toStation.name}</td>
                    <td>${DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy").format(train.departure)}</td>
                    <td>${DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy").format(train.arrival)}</td>
                    <td>${train.seatsNumber}</td>
                    <td>
                        <form method="post" action="/buyTicket">
                            <input type="hidden" name="trainId" value="${train.id}">
                            <input type="hidden" name="fromStation" value="${train.fromStation.name}">
                            <input type="hidden" name="toStation" value="${train.toStation.name}">
                            <button class="btn btn-primary" type="submit">Buy</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>
<%@include file="parts/bottomNavigationMenu.jsp" %>
</body>
</html>