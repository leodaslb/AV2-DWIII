package com.autobots.automanager.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;



import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class ClienteResDto extends RepresentationModel<ClienteResDto> {
	
	
	private Long id;
	
	private String nome;
	
	private String nomeSocial;

	private Date dataNascimento;
	
	private Date dataCadastro;
	
	
	private List<DocumentoResDto> documentos = new ArrayList<>();

	private EnderecoResDto endereco;
	
	private List<TelefoneResDto> telefones = new ArrayList<>();

}