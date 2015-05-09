$(function(){
	// Filter
	$("#filterButtons button").click(function(event){
        window.location.replace("todos?filter=" + event.target.id);
	});
});