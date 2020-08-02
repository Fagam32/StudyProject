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
<!--TODO: adequate front needed -->
<div class="container justify-content-md-center">
    <div class="text-center">
        <%
            String message = (String) request.getAttribute("message");
            out.print(message);
        %></div>
</div>
<%@include file="parts/bottomNavigationMenu.jsp" %>
</body>
</html>