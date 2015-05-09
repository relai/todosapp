var todosapp = todosapp || {};
todosapp.view = todosapp.view || {};


todosapp.view.TodoForm = Backbone.View.extend({		
	tagName: 'form',
	
	className: 'span10',
	
	attributes: {
		action: '#'
	},
	
	events: {
		'submit':      'save',
		'blur #name':  '_validateName',
		'keyup #name': '_validateName',
		'click #nameControl button': '_clearName'
	},
	
	render: function() {
        if (!todosapp.templates.form) {
			Handlebars.registerHelper('option', function(option, selected) {
				var content = '<option ' +  (option === selected ? 'selected' : '') + '>' 
						+  option + '</option>';
			    return content;
			});
		    todosapp.templates.form = Handlebars.compile($("#todoform-template").html());	
		}
        
		if (this.model) {
			var html = todosapp.templates.form(this.model.toJSON());
			this.$el.html(html);					
			$("#main").empty().append(this.$el);
		}
		return this;
	},
	
	initialize: function(options) {	  
        var that = this;
        var onFetchError = function() {
            that._raiseError("The todo item cannot be loaded.", null);
        };
        
        // validate id. Intionally use != to compare the model id with its parseInt
		this.modelId = options.modelId;      
        if (this.modelId !== null && 
            (isNaN(this.modelId) || parseInt(this.modelId) != this.modelId)){
            this._raiseError("Incorrect request. The todo id must be an integer.", null);
            return;
        }
        
        if (this.modelId === null) {
            // create a new todo item
            this.model = new todosapp.model.Todo();
            this.render();
        } else if (todosapp.fetched === false) {
            // Ajax fetch of the collection has not returned yet
            this.listenToOnce(todosapp.collection, 'sync', this._initModel);
        } else {
            this._initModel();
            if (this.model) this.model.fetch({error: onFetchError});
        }
              
        // surrounding widgets
		$('#header h1 i').attr('class', 'icon-clipboard smaller');
		$('#header h1 small').text('item');				
		$('#create').hide();
		$('#save').show();
		$('#cancel').show();						
		if (this.model === null || this.model.isNew()) {
			$('#delete').hide();
		} else {
			$('#delete').show();
		}		
		$('#filterSet').hide();
   },
   
    save: function(event) {
        var that = this;
        event.preventDefault();
	    if(!this._validateName()){
            return;
	    }	         
        var attributes = {
            name: $('#name').val(),
            description: $('#description').val(),
            priority: $('#priority option:selected').val(),
            completed: $('#completed').is(':checked')
        };			   
        this.model.set(attributes);			
        if (this.model.isNew()) {
            todosapp.collection.add(this.model); 	
        }        
 
 
        var onSuccess = function() {
            todosapp.router.navigate('', {trigger: true});
        };     
        var onError = function(model, response, options) {
            var message = (response.status === 409) ? 
                "Someone has already modified the record." : 
                "Something went wrong.";     
            that._raiseError(message, model);
        };
        this.model.save(attributes, {success: onSuccess, error: onError});	   
    } ,
   
    deleteItem: function() {
	    this.model.destroy();
    },
   
    _initModel: function() {
	   this.model = todosapp.collection.get(this.modelId);       
       if (this.model) {
           $('#delete').show();
           this.listenTo(this.model, 'sync', this.render);
           this.render();	
       } else {
           this._raiseError("The to-do item does not exist.", null);
       }
    },
   
   _clearName: function(){
	   $('#name').val('');
	   this._validateName();
   },
   
   _validateName: function() {
    	var result;
    	if ($("#name").val() < 1) {
    		$("#nameControl").addClass("error-state");
    		$("#nameMessage").text("(Required)");
    		result = false;
    	} else {
    		$("#nameControl").removeClass("error-state");
    		$("#nameMessage").text("");
    		result = true;
    	}
    	return result;		
    },  
       
    _raiseError : function(message, model) {
        var toGoHome = (model === null);
        var router = todosapp.router;
        
        if (!todosapp.templates.errorPopupContent) {
		    todosapp.templates.errorPopupContent = 
            Handlebars.compile($("#errorPopupContent-template").html());	
		}   
        var errorContent = todosapp.templates.errorPopupContent({
            errorText: message, toGoHome: toGoHome});   
        
        var onRefresh = function() {
            model.fetch();
            $.Dialog.close(); 
        };       
        var onGoHome = function() {
            $.Dialog.close(); 
            router.navigate('', {trigger: true});
        };
                    
        $.Dialog({
            overlay: true,
            shadow: true,
            flat: true,
            icon: '<span class="icon-blocked fg-red"></span>',
            title: '<span class="fg-red">Error</span>',
            overlayClickClose: !toGoHome,
            content: errorContent,
            sysButtons: false,
            padding: 20,
            width:  300,
            onShow: function(_dialog){
                if (toGoHome) {
                    _dialog.find("#popupHome").click(onGoHome);
                } else  {
                    _dialog.find("#popupRefresh").click(onRefresh);
                }
            }
        });

     }
});