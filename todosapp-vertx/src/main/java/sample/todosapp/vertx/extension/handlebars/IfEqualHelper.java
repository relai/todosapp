package sample.todosapp.vertx.extension.handlebars;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import java.io.IOException;

/**
 * Hanlderbars helper function: ifEqual. 
 * 
 * Usage:  
 *    ifEqual arg1 arg2 value
 * When arg1 equals arg2, the value is output.
 *  
 * Example: 
 *    {{ifEqual filter 'all' "class=active"}}
 * 
 * @author relai
 */
public class IfEqualHelper implements Helper{

    @Override
    public CharSequence apply(Object context, Options options) throws IOException {
        Object obj = options.param(0);
        CharSequence result = null;       
        if (context != null && context.equals(obj)) {
            //result = options.fn();
            result = options.param(1);
        }      
        return result;
    }
    
}
