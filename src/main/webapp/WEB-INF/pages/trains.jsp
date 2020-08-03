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
    <div class="form-group">
        <form class="form-inline justify-content-md-center pt-5" method="post">
            <label><input type="text" class="form-control ml-3" name="fromStation" placeholder="From"></label>
            <label><input type="text" class="form-control ml-3" name="toStation" placeholder="To"></label>
            <label><input type="datetime-local" class="form-control ml-3" name="date"></label>
            <label><input type="number" class="form-control ml-3" name="seats" placeholder="Seats"></label>
            <label>
                <button type="submit" class="btn btn-primary ml-3">Add</button>
            </label>
        </form>
    </div>
    <table class="table">
        <thead>
        <tr>
            <th scope="col">Id</th>
            <th scope="col">From</th>
            <th scope="col">To</th>
            <th scope="col">Departure</th>
            <th scope="col">Arrival</th>
            <th scope="col">Seats</th>
            <th scope="col">Action</th>
        </tr>
        </thead>
        <tbody>
        <%
            List<Train> edgeList = (List<Train>) request.getAttribute("trains");
            if (edgeList != null) {
                for (Train t : edgeList) {
                    out.println("<tr>"
                            + "<form method=\"post\">\n"
                            + "<input type=\"hidden\" name=\"trainId\" value =\"" + t.getId() + "\">"
                            + "<th scope=\"row\">" + t.getId() + "</th> "
                            + "<td>" + t.getFromStation().getName() + "</td>"
                            + "<td>" + t.getToStation().getName() + "</td>"
                            + "<td>" + t.getDeparture() + "</td>"
                            + "<td>" + t.getArrival() + "</td>"
                            + "<td>" + t.getSeatsNumber() + "</td>"
                            + "<td>" + "<button type=\"submit\" class=\"btn btn-danger\" >Delete</button>" + "</td>"
                            + "</form>"
                            + "</tr>");
                }
            } else {
                out.print("No trains here");
            }
        %>
        </tbody>
    </table>

    </form>
</div>
<%@include file="parts/bottomNavigationMenu.jsp" %>
</body>
</html>