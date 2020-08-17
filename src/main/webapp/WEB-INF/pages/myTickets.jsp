<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">

    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css"
          integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
    <title>My tickets</title>
</head>
<body>
<%@include file="parts/topNavigationMenu.jsp" %>
<div class="container">
    <div class="text-center">
        <%if (request.getParameter("message") != null) out.print(request.getParameter("message"));%>
    </div>
    <table class="table">
        <thead>
        <tr>
            <th scope="col">Id</th>
            <th scope="col">From</th>
            <th scope="col">To</th>
            <th scope="col">Departure</th>
            <th scope="col">Arrival</th>
            <th scope="col">Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${tickets}" var="ticket">
            <tr>
                <td>${ticket.id}</td>
                <td>${ticket.frStation.name}</td>
                <td>${ticket.toStation.name}</td>
                <td>${DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy").format(ticket.departure)}</td>
                <td>${DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy").format(ticket.arrival)}</td>
                <td>
                    <form method="post">
                        <input type="hidden" name="ticketId" value="${ticket.id}">
                        <button class="btn btn-danger" type="submit">Cancel</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
<%@include file="parts/bottomNavigationMenu.jsp" %>
</body>
</html>