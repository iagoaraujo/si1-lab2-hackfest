package controllers;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Transient;

import models.Hackfest;
import models.Usuario;
import models.dao.DAO;
import models.dao.GenericDAO;
import play.*;
import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.*;
import views.html.*;

public class Application extends Controller {
	static GenericDAO dao = new DAO();
	static Form<Usuario> usuarioForm = Form.form(Usuario.class);
	static Form<Hackfest> hackForm = Form.form(Hackfest.class);
	static Usuario usuarioAtual;
	
    @Transactional
    public static Result index() {
    	if(usuarioAtual==null){
    		return ok(login.render(usuarioForm));
    	}
    	else if(isAdmin(usuarioAtual)){
    		List<Hackfest> hackfests = dao.findAllByClassName("hackfest");
    		return ok(homeAdmin.render(hackForm, hackfests));
    	}
    	else{
    		return ok(home.render(usuarioAtual));
    	}
    }
    
    @Transactional
    public static Result logar(){
    	if(dao.findAllByClassName("hackfest").size()==0){
    		criarExemplos();
    	}
    	Form<Usuario> filledForm = usuarioForm.bindFromRequest();
    	usuarioAtual = filledForm.get();
    	criaUsuario(usuarioAtual);
    	return redirect(routes.Application.index());
    }
    
    private static void criaUsuario(Usuario usuario) {
		dao.merge(usuario);
		dao.flush();
	}
    
    private static boolean isAdmin(Usuario usuario){
    	if(usuario.getNome().toLowerCase().trim().equals("admin")
    			&& usuario.getEmail().trim().equals("admin@admin")){
    		return true;
    	}
    	return false;
    }
    
    @Transactional
    public static Result novoHackfest(){
    	Form<Hackfest> filledForm = hackForm.bindFromRequest();
    	dao.merge(filledForm.get());
    	dao.flush();
    	List<Hackfest> hackfests = dao.findAllByClassName("hackfest");
    	return ok(homeAdmin.render(hackForm, hackfests));
    }
    
    @Transactional
    public static Result mudarListaDeHackfests(String tema){
    	List<Hackfest> hackfestsOnDAO = dao.findAllByClassName("hackfest");
    	List<Hackfest> hackfests = new LinkedList<Hackfest>();
    	for(Hackfest hackfest: hackfestsOnDAO){
    		if(hackfest.getTemas().contains(tema)){
    			hackfests.add(hackfest);
    		}
    	}
		return ok(geral.render(hackfests, tema, usuarioAtual));
    }
    
    public static Result logout(){
    	usuarioAtual = null;
    	return redirect(routes.Application.index());
    }
    
    @Transactional
    public static Result confirmaPresenca(Long id, String tema){
    	Hackfest hackfest = dao.findByEntityId(Hackfest.class, id);
    	hackfest.addUsuario(usuarioAtual);
    	dao.merge(hackfest);
    	dao.flush();
    	return redirect(routes.Application.mudarListaDeHackfests(tema));
    }
    
    @Transactional
    public static Result listarInscritos(Long id){
    	Hackfest hackfest = dao.findByEntityId(Hackfest.class, id);
    	return ok(inscritos.render(hackfest.getUsuarios(), hackfest));
    }
    
    private static void criarExemplos(){
    	String tema1 = "linguagensDeProgramacao";
    	String tema2 = "promovidosPorEmpresas";
    	String tema3 = "dispositivosMoveis";
    	String tema4 = "frameworks";
    	String tema5 = "geral";
    	Usuario joao = new Usuario("João Carlos", "carlos@gmail.com");
    	Usuario lucas = new Usuario("Lucas Ferreira", "lucas.ferreira@gmail.com");
    	Usuario fernando = new Usuario("Fernando Almeida", "almeida@hotmail.com");
    	Usuario jose = new Usuario("José Arthur", "jose.arathur@yahoo.com");
    	Usuario emerson = new Usuario("Emerson Araújo", "emerson.araujo@gmail.com");
    	Usuario antonio = new Usuario("Antonio Carlos", "antonio@gmail.com");
    	Usuario alex = new Usuario("Alex Santana", "alex@hotmail.com");
    	criaUsuario(emerson);
    	criaUsuario(jose);
    	criaUsuario(fernando);
    	criaUsuario(lucas);
    	criaUsuario(joao);
    	criaUsuario(alex);
    	criaUsuario(antonio);
    	Hackfest hackfest1 = new Hackfest("C++", "Resolva problemas com C++", new Date(2014, 10, 1));
    	Hackfest hackfest2 = new Hackfest("Java", "Resolva problemas com Java", new Date(2014, 10, 2));
    	Hackfest hackfest3 = new Hackfest("Python", "Resolva problemas com Python", new Date(2014, 10, 3));
    	Hackfest hackfest4 = new Hackfest("Google Hackfest", "Hackfest promovido pela Google", new Date(2014, 10, 4));
    	Hackfest hackfest5 = new Hackfest("Facebook Hackfest", "Hackfest promovido pelo Facebook", new Date(2014, 10, 5));
    	Hackfest hackfest6 = new Hackfest("Microsoft", "Hackfest promovido pela Microsoft", new Date(2014, 10, 6));
    	Hackfest hackfest7 = new Hackfest("Android", "Mostre suas habilidades com dispositivos Android", new Date(2014, 10, 7));
    	Hackfest hackfest8 = new Hackfest("iOS", "Mostre suas habilidades com essa poderosa ferramenta da Apple", new Date(2014, 10, 8));
    	Hackfest hackfest9 = new Hackfest("Tribunal de Contas", "O Tribunal de Contas da PB pede sua ajuda para melhorar seus softwares", new Date(2014, 10, 9));
    	Hackfest hackfest10 = new Hackfest("TSE Hackfest", "O Tribunal Superior Eleitoral convida a todos a participar do seu Hackfest", new Date(2014, 10, 10));
    	hackfest1.addTema(tema1);
    	hackfest1.addTema(tema5);
    	hackfest2.addTema(tema1);
    	hackfest2.addTema(tema5);
    	hackfest3.addTema(tema1);
    	hackfest3.addTema(tema5);
    	hackfest4.addTema(tema2);
    	hackfest4.addTema(tema4);
    	hackfest5.addTema(tema2);
    	hackfest5.addTema(tema4);
    	hackfest6.addTema(tema2);
    	hackfest6.addTema(tema4);
    	hackfest7.addTema(tema1);
    	hackfest7.addTema(tema3);
    	hackfest7.addTema(tema5);
    	hackfest8.addTema(tema1);
    	hackfest8.addTema(tema3);
    	hackfest8.addTema(tema5);
    	hackfest9.addTema(tema2);
    	hackfest9.addTema(tema4);
    	hackfest10.addTema(tema2);
    	hackfest10.addTema(tema4);
    	hackfest1.addUsuario(emerson);
    	hackfest1.addUsuario(lucas);
    	hackfest1.addUsuario(joao);
    	hackfest1.addUsuario(jose);
    	hackfest1.addUsuario(fernando);
    	hackfest1.addUsuario(alex);
    	hackfest1.addUsuario(antonio);
    	hackfest2.addUsuario(emerson);
    	hackfest2.addUsuario(lucas);
    	hackfest2.addUsuario(joao);
    	hackfest2.addUsuario(jose);
    	hackfest2.addUsuario(fernando);
    	hackfest2.addUsuario(alex);
    	hackfest2.addUsuario(antonio);
    	hackfest3.addUsuario(emerson);
    	hackfest3.addUsuario(lucas);
    	hackfest3.addUsuario(joao);
    	hackfest3.addUsuario(jose);
    	hackfest3.addUsuario(fernando);
    	hackfest3.addUsuario(antonio);
    	criaHackFest(hackfest1);
    	criaHackFest(hackfest2);
    	criaHackFest(hackfest3);
    	criaHackFest(hackfest4);
    	criaHackFest(hackfest5);
    	criaHackFest(hackfest6);
    	criaHackFest(hackfest7);
    	criaHackFest(hackfest8);
    	criaHackFest(hackfest9);
    	criaHackFest(hackfest10);
    }
    
    private static void criaHackFest(Hackfest hackfest){
    	dao.merge(hackfest);
    	dao.flush();
    }
}
