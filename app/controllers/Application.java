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

    public static Result index() {
        return ok(login.render(usuarioForm));
    }
    
    @Transactional
    public static Result logar(){
    	Form<Usuario> filledForm = usuarioForm.bindFromRequest();
    	Usuario usuario = filledForm.get();
    	if(isAdmin(usuario)){
    		List<Hackfest> hackfests = dao.findAllByClassName("hackfest");
    		return ok(homeAdmin.render(hackForm, hackfests));
    	}
    	criaUsuario(usuario);
    	return ok(home.render());
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
}
