var todosapp = todosapp || {};
todosapp.view = todosapp.view || {};

todosapp.view.TodoTable = Backbone.View.extend({	
	tagName: 'table',
	className: 'table hovered',
	
	events : {
		
	},

	getFilter: function() {	
		return	this._filter; 
	},
	
	setFilter: function(filter) {
		if (!filter) filter = 'all';
		
		if (this._filter !== filter) {
		   this._filter = filter;
		   this.render();
		} 
	},
	
	render: function() {
		var html = todosapp.templates.table({todos: this._getTodos()});
		this.$el.html(html);
		$("#main").empty().append(this.$el);						
		return this;
	},
	
    initialize: function(options) {
    	if (!todosapp.templates.table) {
    		 todosapp.templates.table = Handlebars.compile($("#todotable-template").html());
    	}
		
	    if (options && options.filter) this._filter = options.filter;	
        
	    this.listenTo(todosapp.collection, 'sync', this.render);	    
	    this.render();
	    
	    // Surrounding widgets
	    $('#header h1 i').attr('class', 'icon-clipboard-2 smaller');
		$('#header h1 small').text('list');
		
		$('#create').show();
		$('#save').hide();
		$('#cancel').hide();
		$('#delete').hide();
		
		$('#filterSet').show();       				
		$("#filterSet button.active").removeClass('active');
		$('#' + this._filter + 'Todos').addClass('active');		
	},
	
	_getTodos: function() {
		var completed = (this._filter === 'done');
		var all = (this._filter === null) || (this._filter.length === 0) || (this._filter === 'all');
		return todosapp.collection
		        .filter(function(it) { return all || (it.get('completed') === completed); })
		        .map(function(it) { return it.toJSON(); });
	},
	
	_filter: 'all'		

});