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
    <form method="post">
        <div class="form-group">
            <label for="InputDeleteTrain">Train ID</label>
            <input type="number" class="form-control" id="InputDeleteTrain" name="trainId" maxlength="10">
        </div>
        <button type="submit" class="btn btn-primary">Delete</button>
    </form>
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
</div>
<%@include file="parts/bottomNavigationMenu.jsp" %>
</body>
</html>