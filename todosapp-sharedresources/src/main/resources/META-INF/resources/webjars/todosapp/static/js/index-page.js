  $(function() {
      	
      	$("div .tile").click(function(event){
      		var dest = this.id;
      		if (dest === "api") dest = "api/todos";
      		window.location.assign("/"+dest);
      	});
      	
   });