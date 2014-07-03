package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity(name="hackfest")
public class Hackfest {

	@Id
	@SequenceGenerator(name = "HACKFEST_SEQUENCE", sequenceName = "HACKFEST_SEQUENCE", allocationSize = 1, initialValue = 0)
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	
	@Column
	private String descricao;
	
	@Column
	private String nome;
	
	@Column
	private String tema;

	@Temporal(value = TemporalType.DATE)
	private Date data;
	
	@OneToMany(cascade=CascadeType.ALL)
	private List<Usuario> usuarios = new ArrayList<Usuario>(); 
	
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
	
	public String getTema() {
		return tema;
	}

	public void setTema(String tema) {
		this.tema = tema;
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
}
