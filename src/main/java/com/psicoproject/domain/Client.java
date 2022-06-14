package com.psicoproject.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;

import org.hibernate.validator.constraints.UniqueElements;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "client")
public class Client implements Serializable{
	private static final long serialVersionUID = 5966684240137063308L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(name = "creation_date", nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;

	@Column(length = 45, nullable = false)
    private String name;
	
	@Column(length = 2, nullable = false)
    private Integer age;

	@Column(length = 45, nullable = false)
    private String occupation;

    
	@Column(length = 100, nullable = false)
    @Email
    private String email;

	@Column(length = 45, nullable = false)
    private String phone;
    
	@Column(length = 45, nullable = false)
    private String birthDate;
    
	@Column(length = 45, nullable = false, unique = true)
    private String cpf;
    
    //@Getter(onMethod = @__({@JsonIgnore}))
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
 
}
