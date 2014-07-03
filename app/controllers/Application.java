package controllers;

import java.util.Date;
import java.util.Iterator;
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
    	if(usuario.getNome().toLowerCase().trim().equals("admin")){
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
    	List<Hackfest> hackfests;
    	hackfests = dao.findByAttributeName("hackfest", "tema", tema);
		return ok(geral.render(hackfests, tema, usuarioAtual));
//    	switch (tema) {
//		case "linguagensDeProgramacao":
//			hackfests = dao.findByAttributeName("hackfest", "tema", "linguagensDeProgramacao");
//			return ok(geral.render(hackfests, "linguagensDeProgramacao", usuarioAtual));
//		case "promovidosPorEmpresas":
//			hackfests = dao.findByAttributeName("hackfest", "tema", "promovidosPorEmpresas");
//			return ok(geral.render(hackfests, "promovidosPorEmpresas", usuarioAtual));
//		case "dispositivosMoveis":
//			hackfests = dao.findByAttributeName("hackfest", "tema", "dispositivosMoveis");
//			return ok(geral.render(hackfests, "dispositivosMoveis", usuarioAtual));
//		default:
//			hackfests = dao.findByAttributeName("hackfest", "tema", "geral");
//			return ok(geral.render(hackfests, "geral", usuarioAtual));
//		}
    }
    
    public static Result logout(){
    	usuarioAtual = null;
    	return redirect(routes.Application.index());
    }
    
    @Transactional
    public static Result confirmaPresenca(Long id){
    	Hackfest hackfest = dao.findByEntityId(Hackfest.class, id);
    	hackfest.addUsuario(usuarioAtual);
    	dao.merge(hackfest);
    	dao.flush();
    	String tema = hackfest.getTema();
    	return redirect(routes.Application.mudarListaDeHackfests(tema));
    }
}
