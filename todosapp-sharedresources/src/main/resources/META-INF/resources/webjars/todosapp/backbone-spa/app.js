var todosapp = todosapp || {};

$(function() {	
	
	todosapp.templates = {};
    todosapp.fetched = false;
    todosapp.collection =  new todosapp.model.TodoCollection();   
    todosapp.router = new todosapp.TodoRouter();
    
    todosapp.collection.once('sync', function() {
        todosapp.fetched = true;
    });
    
    todosapp.collection.fetch();
    Backbone.history.start();
});