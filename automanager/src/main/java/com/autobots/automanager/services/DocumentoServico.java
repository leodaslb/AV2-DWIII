package com.autobots.automanager.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.autobots.automanager.dto.ClienteResDto;
import com.autobots.automanager.dto.DocumentoReqDto;
import com.autobots.automanager.dto.DocumentoResDto;
import com.autobots.automanager.entidades.Cliente;


import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.DocumentoRepositorio;


@Service

public class DocumentoServico {
	private StringVerificadorNulo verificador = new StringVerificadorNulo();

	@Autowired
	private ClienteRepositorio clienteRepositorio;
	@Autowired
	private DocumentoRepositorio documentoRepositorio;

	public Documento converterDocumento(DocumentoReqDto dto) {
        Documento documento = new Documento();
        documento.setTipo(dto.getTipo());
        documento.setNumero(dto.getNumero());
        return documento;
    }

   
    public List<Documento> converterListaDocumentos(List<DocumentoReqDto> dtos) {
       
        if (dtos == null || dtos.isEmpty()) {
            return new ArrayList<>();
        }

        return dtos.stream()
                .map(this::converterDocumento) 
                .collect(Collectors.toList());
    }

	
	public void atualizar(Documento documento, Documento atualizacao) {
		if (atualizacao != null) {
			if (!verificador.verificar(atualizacao.getTipo())) {
				documento.setTipo(atualizacao.getTipo());
			}
			if (!verificador.verificar(atualizacao.getNumero())) {
				documento.setNumero(atualizacao.getNumero());
			}
		}
	}

	public void atualizar(List<Documento> documentos, List<Documento> atualizacoes) {
		for (Documento atualizacao : atualizacoes) {
			for (Documento documento : documentos) {
				if (atualizacao.getId() != null) {
					if (atualizacao.getId().equals(documento.getId())) {
						atualizar(documento, atualizacao);
					}
				}
			}
		}
	}


public Documento adicionaDocumento(Long clienteId, DocumentoReqDto dto) {
	Cliente cliente = clienteRepositorio.findById(clienteId)
	.orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
	Documento novoDocumento = converterDocumento(dto);
	cliente.getDocumentos().add(novoDocumento);
	Cliente clienteSalvo = clienteRepositorio.save(cliente);
	return clienteSalvo.getDocumentos().get(clienteSalvo.getDocumentos().size() - 1);
	
}

public List<Documento> mostrarTodos() {
	return documentoRepositorio.findAll();
}

public Documento selecionarDocumentoPorId(Long id) {
	Documento documento = documentoRepositorio.findById(id)
	.orElseThrow(() -> new RuntimeException("Documento não encontrado"));


	return documento;
}
public List<Documento> selecionarTodosDocumentosPorClienteId(Long clienteId) {
	Cliente cliente = clienteRepositorio.findById(clienteId)
	.orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
	return cliente.getDocumentos();

}


public Documento editarDocumento(Long clienteId, Long documentoId, DocumentoReqDto dto) {
        Cliente cliente = clienteRepositorio.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        
        
        Documento documentoAlvo = cliente.getDocumentos().stream()
                .filter(doc -> doc.getId().equals(documentoId)).findFirst()
                .orElseThrow(() -> new RuntimeException("Documento não encontrado para este cliente"));
        
        Documento dadosAtualizados = converterDocumento(dto);
        atualizar(documentoAlvo, dadosAtualizados);
        
        clienteRepositorio.save(cliente);
		return documentoAlvo;
}

public void deletarDocumento(Long clienteId, Long documentoId) {
        Cliente cliente = clienteRepositorio.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        boolean removido = cliente.getDocumentos().removeIf(doc -> doc.getId().equals(documentoId));
        
        if (!removido) {
            throw new RuntimeException("Documento não encontrado para este cliente");
        }
        
        clienteRepositorio.save(cliente);
    }

	public DocumentoResDto toResDto(Documento documento) {
		if (documento == null) return null;
		DocumentoResDto dto = new DocumentoResDto();
		dto.setId(documento.getId());
		dto.setTipo(documento.getTipo());
		dto.setNumero(documento.getNumero());
		return dto;
	}
	public List<DocumentoResDto> toResDtoList(List<Documento> documentos){
		return documentos.stream().map(this::toResDto).collect(Collectors.toList());
	}

}