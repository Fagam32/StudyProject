<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">

    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css"
          integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
    <title>Login</title>
</head>
<body class="text-center">
<form>
    <label>
        <input type="text" class="form-control" name="login">
    </label>
    <label>
        <input type="password" class="form-control" name="password">
    </label>
    <button type="submit" class="btn btn-primary">Login</button>
</form>
</body>
</html>