package com.autobots.automanager.dto;



import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class DocumentoResDto extends RepresentationModel<DocumentoResDto> {
	
	private Long id;
	
	private String tipo;

	private String numero;
}