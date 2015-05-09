<%@ attribute name="title"                required="true" %>
<%@ attribute name="pageScript"           required="false" %>
<%@ attribute name="navigationBarActions" required="true" fragment="true" %>
<%@ attribute name="mainBody"             required="true" fragment="true" %>

<!DOCTYPE html>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
   <link rel="stylesheet" href="/webjars/metroui/2.0.23/min/metro-bootstrap.min.css" >
   <link rel="stylesheet" href="/webjars/metroui/2.0.23/min/metro-responsive.min.css" >

   <script src="/webjars/jquery/2.1.0/jquery.min.js"></script>
   <script src="/webjars/jquery-ui/1.10.4/ui/minified/jquery.ui.widget.min.js"></script>
      
   <script src="/webjars/metroui/2.0.23/min/metro.min.js"></script>
    
   <c:if test="${not empty pageScript}">
       <script src="${pageScript}"></script>
   </c:if>    
   
   <title>${title}</title>
</head>
<body class="metro">
   <header>
      <nav class="navigation-bar">
        <nav class="navigation-bar-content container">        
            <a class="element" href="/">
               <span class="icon-list"></span> <span> todosapp </span>
            </a> 
            
            <jsp:invoke fragment="navigationBarActions" />
            
            <a title="more info" class="element place-right" href="https://github.com/relai/todosapp"> 
                  <span class="icon-info-2"></span>
            </a> 
            <a title="author" class="element place-right" href="http://relai.blogspot.com/"> 
                 <span	class="icon-at"></span>
            </a>              
        </nav>
      </nav>
   </header>

   <div class="container">
       <jsp:invoke fragment="mainBody" />
   </div>

</body>
</html>