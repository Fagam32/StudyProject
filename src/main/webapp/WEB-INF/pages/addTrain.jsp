<%@ page import="com.ivolodin.entities.Train" %>
<%@ page import="java.util.List" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css"
          integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
    <title>Add Train</title>
</head>
<%@include file="parts/topNavigationMenu.jsp" %>
<body>
<div class="container">
    <form method="post">
        <div class="form-group">
            <label for="InputFromStation"> From</label>
            <input type="text" class="form-control" id="InputFromStation" name="fromStation">
        </div>
        <div class="form-group">
            <label for="InputToStation"> To</label>
            <input type="text" class="form-control" id="InputToStation" name="toStation">
        </div>
        <div class="form-group">
            <label for="InputDate"> Time </label>
            <input type="datetime-local" class="form-control" id="InputDate" name="date">
        </div>
        <div class="form-group">
            <label for="InputSeats"> Total seats </label>
            <input type="number" class="form-control" id="InputSeats" name="seats">
        </div>
        <table class="table">
            <thead>
            <tr>
                <th scope="col">Id</th>
                <th scope="col">From</th>
                <th scope="col">To</th>
                <th scope="col">Departure</th>
                <th scope="col">Arrival</th>
            </tr>
            </thead>
            <tbody>
            <%
                List<Train> edgeList = (List<Train>) request.getAttribute("trainList");
                if (edgeList != null) {
                    for (Train t : edgeList) {
                        out.println("<tr>"
                                + "<th scope=\"row\">" + t.getId() + "</th> "
                                + "<td>" + t.getFromStation().getName() + "</td>"
                                + "<td>" + t.getToStation().getName() + "</td>"
                                + "<td>" + t.getDeparture() + "</td>"
                                + "<td>" + t.getArrival() + "</td>"
                                + "</tr>");
                    }
                } else {
                    out.print("No edgeList here");
                }
            %>
            </tbody>
        </table>
        <button type="submit" class="btn btn-primary">Add</button>
    </form>
</div>
<%@include file="parts/bottomNavigationMenu.jsp" %>
</body>
</html>