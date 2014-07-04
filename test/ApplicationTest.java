
import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.callAction;
import static play.test.Helpers.charset;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.status;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Usuario;
import models.dao.DAO;
import models.dao.GenericDAO;

import org.junit.Before;
import org.junit.Test;

import play.db.jpa.JPA;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;
import play.test.WithApplication;


/**
*
* Simple (JUnit) tests that can call all parts of a play app.
* If you are interested in mocking a whole application, see the wiki for more details.
*
*/
public class ApplicationTest extends WithApplication{

	Result result;
	
	@Before
	public void setUp() throws Exception{
		start(fakeApplication(inMemoryDatabase()));
	}

    @Test
    public void callCadastrar() {
    	Helpers.running(Helpers.fakeApplication(Helpers.inMemoryDatabase()), new Runnable() {
    	    public void run() {
    	        JPA.withTransaction(new play.libs.F.Callback0() {
    	            @Override
    	            public void invoke() throws Throwable {
    	            	GenericDAO dao = new DAO();
    	            	Map<String, String> formData = new HashMap<String, String>();
    	            	formData.put("nome", "Arthur");
    	            	formData.put("email", "arthur@gmail.com");
    	            	
    	                result = callAction(controllers.routes.ref.Application.logar(), 
    	                		fakeRequest().withFormUrlEncodedBody(formData));
    	                
    	                //27 exemplos criados no BD + 1 usuario de teste
    	                List<Usuario> usuarios = dao.findAllByClassName("usuario");
    	                assertThat(usuarios.size()).isEqualTo(28);
    	            }
    	        });
    	    }
    	});
    }
}
