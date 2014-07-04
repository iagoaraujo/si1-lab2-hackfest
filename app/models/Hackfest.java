package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity(name="hackfest")
public class Hackfest implements Comparable<Hackfest>{

	@Id
	@SequenceGenerator(name = "HACKFEST_SEQUENCE", sequenceName = "HACKFEST_SEQUENCE", allocationSize = 1, initialValue = 0)
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	
	@Column
	private String descricao;
	
	@Column
	private String nome;
	
	@Temporal(value = TemporalType.DATE)
	private Date data;
	
	@OneToMany(cascade=CascadeType.ALL)
	private List<Usuario> usuarios = new ArrayList<Usuario>(); 
	
	@ElementCollection
	private List<String> temas = new ArrayList<String>();
	
	public Hackfest() {
	}
	
	public Hackfest(String nome, String descricao, Date data){
		this.nome = nome;
		this.descricao = descricao;
		this.data = data;
	}
	
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Long getId() {
		return id;
	}
	
	public void addUsuario(Usuario usuario){
		usuarios.add(usuario);
	}
	
	public boolean hasUsuario(Usuario usuario){
		return usuarios.contains(usuario);
	}
	
	public List<Usuario> getUsuarios(){
		return usuarios;
	}

	@Override
	public int compareTo(Hackfest hackfest) {
		if(hackfest.getQtdInscritos()<getQtdInscritos()){
			return 1;
		}
		else if(hackfest.getQtdInscritos()>getQtdInscritos()){
			return -1;
		}
		return nome.compareToIgnoreCase(hackfest.getNome());
	}
	
	public int getQtdInscritos(){
		return usuarios.size();
	}

	public List<String> getTemas() {
		return temas;
	}
	
	public void addTema(String tema){
		temas.add(tema);
	}
}
