
import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.callAction;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.inMemoryDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Hackfest;
import models.Usuario;
import models.dao.DAO;
import models.dao.GenericDAO;

import org.junit.Before;
import org.junit.Test;

import play.db.jpa.JPA;
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
    public void callLogar() {
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
    
    @Test
    public void callNovoHackfest() {
    	Helpers.running(Helpers.fakeApplication(Helpers.inMemoryDatabase()), new Runnable() {
    	    public void run() {
    	        JPA.withTransaction(new play.libs.F.Callback0() {
    	            @Override
    	            public void invoke() throws Throwable {
    	            	GenericDAO dao = new DAO();
    	            	Map<String, String> formData = new HashMap<String, String>();
    	            	formData.put("nome", "Java");
    	            	formData.put("descricao", "Resolver problemas com a linguagem Java");
    	            	formData.put("temas[0]", "geral");
    	            	
    	                result = callAction(controllers.routes.ref.Application.novoHackfest(), 
    	                		fakeRequest().withFormUrlEncodedBody(formData));
    	                
    	                /*Como os casos de exemplos sao criados apenas ao logar, entao so deve haver
    	                o hackfest criado agora no BD
    	                */
    	                List<Hackfest> hackfests = dao.findAllByClassName("hackfest");
    	                assertThat(hackfests.size()).isEqualTo(1);
    	                
    	                assertThat(hackfests.get(0).getNome()).isEqualTo("Java");
    	                assertThat(hackfests.get(0).getDescricao()).isEqualTo("Resolver problemas com a linguagem Java");
    	            }
    	        });
    	    }
    	});
    }
    
    @Test
    public void callConfirmarPresenca(){
    	Helpers.running(Helpers.fakeApplication(Helpers.inMemoryDatabase()), new Runnable() {
    	    public void run() {
    	        JPA.withTransaction(new play.libs.F.Callback0() {
    	            @Override
    	            public void invoke() throws Throwable {
    	            	GenericDAO dao = new DAO();
    	            	Map<String, String> formData = new HashMap<String, String>();
    	            	formData.put("nome", "Java");
    	            	formData.put("descricao", "Resolver problemas com a linguagem Java");
    	            	formData.put("temas[0]", "geral");
    	            	
    	                result = callAction(controllers.routes.ref.Application.novoHackfest(), 
    	                		fakeRequest().withFormUrlEncodedBody(formData));
    	                
    	                List<Hackfest> hackfests = dao.findAllByClassName("hackfest");
    	                Long id = hackfests.get(0).getId();
    	            	
    	                formData = new HashMap<String, String>();
    	            	formData.put("nome", "Arthur");
    	            	formData.put("email", "arthur@gmail.com");
    	            	
    	                result = callAction(controllers.routes.ref.Application.logar(), 
    	                		fakeRequest().withFormUrlEncodedBody(formData));
    	                
    	                result = callAction(controllers.routes.ref.Application.confirmaPresenca(id, "geral"), 
    	                		fakeRequest().withFormUrlEncodedBody(formData));
    	                
    	                Hackfest hackfest = dao.findByEntityId(Hackfest.class, id);
    	                assertThat(hackfest.getUsuarios().size()).isEqualTo(1);
    	            }
    	        });
    	    }
    	});
    }
}
