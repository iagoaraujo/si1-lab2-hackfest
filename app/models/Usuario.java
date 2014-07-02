package models;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

@Entity(name="usuario")
public class Usuario {
	
	@Id
	@SequenceGenerator(name = "USUARIO_SEQUENCE", sequenceName = "USUARIO_SEQUENCE", allocationSize = 1, initialValue = 0)
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	
	@Column
	private String tipo;
	
	@Column
	private String nome;
	
	@Column
	private String email;
	
	@OneToMany(cascade=CascadeType.ALL)
	private List<Hackfest> hackfests;

	public List<Hackfest> getHackfests() {
		if(hackfests==null){
			hackfests = new LinkedList<Hackfest>();
		}
		return hackfests;
	}

	public void setHackfests(List<Hackfest> hackfests) {
		this.hackfests = hackfests;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}