<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Edit Meal</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Edit meal</h2>
<ul>

    <form action = "meals?action=edit" method = "POST">
        <input type="hidden" name="id" value="${meal.id}">
        Date and time:<input type = "datetime-local" value="${meal.dateTime}" name = "dateTime" required>
        <br/>
        Description: <input type = "text" value="${meal.description}" name = "description" required/>
        <br/>
        Calories: <input type="number" value="${meal.calories}" name="calories" required/>
        <br/>
        <input type = "submit" value = "SAVE" />
        <button onclick="window.history.back()" type="button">Cancel</button>
    </form>

</ul>
</body>
</html>
