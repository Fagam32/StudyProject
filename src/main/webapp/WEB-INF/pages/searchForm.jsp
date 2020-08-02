<%@ page import="com.ivolodin.entities.Train" %>
<%@ page import="java.util.List" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">

    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css"
          integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
    <title>Add Station</title>
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
            <tbody>
            <%
                List<Train> trainList = (List<Train>) request.getAttribute("trains");
                if (trainList != null && !trainList.isEmpty()) {
                    for (Train t : trainList) {
                        out.println("<tr>"
                                + "<th scope=\"row\">" + t.getId() + "</th> "
                                + "<td>" + t.getFromStation().getName() + "</td>"
                                + "<td>" + t.getToStation().getName() + "</td>"
                                + "<td>" + t.getDeparture() + "</td>"
                                + "<td>" + t.getArrival() + "</td>"
                                + "<td>" + t.getSeatsNumber() + "</td>"
                                + "<td>" + "<a href=\"/buyTicket?trainId=" + t.getId()
                                + "&fromStation=" + t.getFromStation().getName()
                                + "&toStation=" + t.getToStation().getName()
                                + "\"><input type=\"button\" class=\"btn btn-primary\" value=\"Buy\"></a>" + "</td>"
                                + "</tr>");
                    }
                } else if (trainList != null) {
                    out.print("No trains found");
                }
            %>
            </tbody>
        </table>
    </div>
</div>
<%@include file="parts/bottomNavigationMenu.jsp" %>
</body>
</html>