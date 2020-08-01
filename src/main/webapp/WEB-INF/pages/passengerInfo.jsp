<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">

    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css"
          integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
    <title>Add Station</title>
</head>
<body class="text-center">
<%@include file="topNavigationMenu.jsp" %>
<div class="container">
    <div class="row justify-content-center align-items-center" style="height:100vh ">
        <form method="post">
            <div class="form-group">
                Name <input type="text" class="form-control" value="<%out.print(request.getAttribute("name")); %>"
                       name="name">
                Surname <input type="text" class="form-control" value="<%out.print(request.getAttribute("surname")); %>"
                       name="surname">
                BirthDate <input type="date" class="form-control" value="<%out.print(request.getAttribute("birthDate")); %>"
                       name="birthDate">
            </div>
            <button type="submit" class="btn-primary">Refresh</button>
        </form>
    </div>
</div>

<%@include file="bottomNavigationMenu.jsp" %>
</body>
</html>