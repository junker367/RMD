<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
 
<html>
    <head>
        <title>Create Provider</title>
        <script type="text/javascript" src="<c:url value="/scripts/jquery-1.4.min.js" /> "></script>
        <script type="text/javascript" src="<c:url value="/scripts/json.min.js" /> "></script>
    </head>
    <body>
        <div class="container">
            <h1>
                Create Account
            </h1>
            <div class="span-12 last">    
                <form:form modelAttribute="provider" action="provider/store" method="post">
                    <fieldset>        
                        <legend>Provider Fields</legend>
                        <p>
                            <form:label for="name" path="name" cssErrorClass="error">Name</form:label><br/>
                            <form:input path="name" /><form:errors path="name" />
                        </p>
                        <p>   
                            <input id="create" type="submit" value="Create" />
                        </p>
                    </fieldset>
                </form:form>
            </div>
        </div>
        <div id="mask" style="display: none;"></div>
        <div id="popup" style="display: none;">
            <div class="span-8 last">
                <h3>Account Created Successfully</h3>
                <form>
                    <fieldset>
                        <p>
                            <label for="assignedId">Assigned Id</label><br/>
                            <input id="assignedId" type="text" readonly="readonly" />     
                        </p>
                        <p>
                            <label for="confirmedName">Name</label><br/>
                            <input id="confirmedName" type="text" readonly="readonly" />
                        </p>
                    </fieldset>
                </form>
                <a href="#" onclick="closePopup();">Close</a>           
            </div>            
        </div>        
    </body>
 
    <script type="text/javascript">   
        $(document).ready(function() {
            $("#provider").submit(function() {
                var account = $(this).serializeObject();
                $.postJSON("provider/store", account, function(data) {
                    $("#assignedId").val(data);
                    showPopup();
                });
                return false;               
            });
        });
 
        function showPopup() {
            $.getJSON("provider/" + $("#assignedId").val(), function(provider) {
                $("#confirmedName").val(provider.name);
            });         
            $('#popup').fadeIn('fast');
            $('#mask').fadeIn('fast');
        }
         
        function closePopup() {
            $('#popup').fadeOut('fast');
            $('#mask').fadeOut('fast');
            resetForm();
        }
 
        function resetForm() {
            $('#provider')[0].reset();
        }
         
    </script>
     
</html>
