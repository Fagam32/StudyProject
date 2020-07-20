<%@ page import="com.ivolodin.entities.Station" %>
<%@ page import="java.util.List" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">

    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css"
          integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
    <title>MainPage</title>
</head>
<body>
<div class="container">
    <form method="post">
        <div class="form-group">
            <label for="InputStationName"> Station name</label>
            <input type="text" class="form-control" id="InputStationName" name="stationName">
        </div>
        <button type="submit" class="btn btn-primary">Add</button>
    </form>

    <table class="table">
        <thead>
        <tr>
            <th scope="col">Id</th>
            <th scope="col">Station name</th>
        </tr>
        </thead>
        <tbody>
        <%
            List<Station> stations = (List<Station>) request.getAttribute("stationList");
            if (stations != null) {
                for (Station s : stations) {
                    out.println("<tr>"
                            + "<th scope=\"row\">" + s.getId() + "</th> "
                            + "<td>" + s.getName() + "</td>"
                            + "</tr>");
                }
            } else {
                out.print("WTF no stations here");
            }
        %>
        </tbody>
    </table>
</div>
</body>
</html>