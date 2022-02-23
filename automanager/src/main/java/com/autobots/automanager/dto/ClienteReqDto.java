package com.autobots.automanager.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class ClienteReqDto {

	private String nome;

	private String nomeSocial;

	private Date dataNascimento;

	private List<DocumentoReqDto> documentos = new ArrayList<>();

	private EnderecoReqDto endereco;

	private List<TelefoneReqDto> telefones = new ArrayList<>();

}
