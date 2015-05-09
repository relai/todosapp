var todosapp = todosapp || {};
todosapp.model = todosapp.model || {};

todosapp.model.Todo = Backbone.Model.extend({
	
});


todosapp.model.TodoCollection = Backbone.Collection.extend({
	model: todosapp.model.Todo,
	url: '/api/todos'
});