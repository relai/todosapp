package sample.todosapp.javaee.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/** 
 * The entity class for to-do items.
 *
 * @author relai
 */
@Entity
@Table(name = "TODOS")
@NamedQueries({
    @NamedQuery(name = "Todo.findAll", query = "SELECT t FROM Todo t"),
    @NamedQuery(name = "Todo.findByCompleted", 
                query = "SELECT t FROM Todo t WHERE t.completed = :done")
})
public class Todo implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @GeneratedValue @Id private Long id;  
    @Version private int version; // Version number for optimistic locking   
    @NotNull @Size(min=1) private String name;
    private String priority;
    private String description;
    private boolean completed;
     
    public Todo() {  }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    
    public int  getVersion() {return version;}
    public void setVersion(int version) {this.version = version;}
    
    public String getName() {return name;}
    public void   setName(String name) {this.name = name;}
    
    public String getDescription() {return description;}
    public void   setDescription(String description) {this.description = description;}

    public String getPriority() {return priority;}
    public void   setPriority(String priority) {this.priority = priority;}
    
    public boolean getCompleted() {return completed;}
    public void    setCompleted(boolean completed) {this.completed = completed;}
}
