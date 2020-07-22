<%@ page import="com.ivolodin.entities.StationConnect" %>
<%@ page import="java.util.List" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">

    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css"
          integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
    <title>AddEdge</title>
</head>

<body>
<%@include file="topNavigationMenu.jsp" %>
<div class="container">
    <form method="post">
        <div class="form-row">
            <div class="form-group col-md-5">
                <label for="InputStationName1">First Station</label>
                <input type="text" class="form-control" id="InputStationName1" name="frStat">
            </div>

            <div class="form-group col-md-5">
                <label for="InputStationName2">Second Station</label>
                <input type="text" class="form-control" id="InputStationName2" name="toStat">
            </div>

            <div class="form-group col-md-2">
                <label for="InputDistance">Distance in minutes</label>
                <input type="number" class="form-control" id="InputDistance" name="distance">
            </div>
        </div>
        <button type="submit" class="btn btn-primary">Add</button>
    </form>

    <table class="table">
        <thead>
        <tr>
            <th scope="col">Id</th>
            <th scope="col">From</th>
            <th scope="col">To</th>
            <th scope="col">Distance</th>
        </tr>
        </thead>
        <tbody>
        <%
            List<StationConnect> edgeList = (List<StationConnect>) request.getAttribute("edgeList");
            if (edgeList != null) {
                for (StationConnect s : edgeList) {
                    out.println("<tr>"
                            + "<th scope=\"row\">" + s.getId() + "</th> "
                            + "<td>" + s.getFrom().getName() + "</td>"
                            + "<td>" + s.getTo().getName() + "</td>"
                            + "<td>" + s.getDistanceInMinutes() + "</td>"
                            + "</tr>");
                }
            } else {
                out.print("No edgeList here");
            }
        %>
        </tbody>
    </table>
    <a href="addStation">Stations</a>
</div>
<%@include file="bottomNavigationMenu.jsp" %>
</body>
</html>