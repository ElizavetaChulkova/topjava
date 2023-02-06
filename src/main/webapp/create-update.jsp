<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: chulk
  Date: 05.02.2023
  Time: 17:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Edit Meal</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Add new meal</h2>
<ul>

    <form action = "meals?action=update&id=${meal.id}" method = "POST">
        <dt>Date and time:</dt>
        <dd><input type = "datetime-local" value="${meal.dateTime}" name = "dateTime"></dd>
        <br />
        Description: <dd><input type = "text" value="${meal.description}" name = "description" /></dd>
        <br/>
        Calories: <dd><input type="text" value="${meal.calories}" name="calories"/></dd>
        <br/>
        <input type = "submit" value = "SAVE" />
        <button onclick="window.history.back()" type="button">Cancel</button>
    </form>

</ul>
</body>
</html>
