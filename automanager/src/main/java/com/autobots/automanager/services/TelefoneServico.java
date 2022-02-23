package com.autobots.automanager.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager.dto.TelefoneReqDto;
import com.autobots.automanager.dto.TelefoneResDto;
import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.repositorios.ClienteRepositorio;

@Service
public class TelefoneServico {
    
    private StringVerificadorNulo verificador = new StringVerificadorNulo();

    @Autowired
    private ClienteRepositorio clienteRepositorio;

   //conversao dto

    public Telefone converterTelefone(TelefoneReqDto dto) {
        Telefone telefone = new Telefone();
        telefone.setDdd(dto.getDdd());
        telefone.setNumero(dto.getNumero());
        return telefone;
    }

    public List<Telefone> converterListaTelefones(List<TelefoneReqDto> dtos) {
        if (dtos == null || dtos.isEmpty()) {
            return new ArrayList<>(); 
        }
        return dtos.stream()
                .map(this::converterTelefone)
                .collect(Collectors.toList());
    }

  

    public void atualizar(Telefone telefone, Telefone atualizacao) {
        if (atualizacao != null) {

           if (!verificador.verificar(atualizacao.getDdd())) {
                telefone.setDdd(atualizacao.getDdd());
            }
            if (!verificador.verificar(atualizacao.getNumero())) {
                telefone.setNumero(atualizacao.getNumero());
            }
        }
    }

    public void atualizar(List<Telefone> telefones, List<Telefone> atualizacoes) {
        for (Telefone atualizacao : atualizacoes) {
            for (Telefone telefone : telefones) {
                if (atualizacao.getId() != null) {
                  
                    if (atualizacao.getId().equals(telefone.getId())) {
                        atualizar(telefone, atualizacao);
                    }
                }
            }
        }
    }


    public Telefone adicionarTelefone(Long clienteId, TelefoneReqDto dto) {
        Cliente cliente = clienteRepositorio.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        
        Telefone novoTelefone = converterTelefone(dto);
        cliente.getTelefones().add(novoTelefone);
        
        Cliente salvo = clienteRepositorio.save(cliente); 
        return salvo.getTelefones().get(salvo.getTelefones().size() - 1);
    }


    public Telefone selecionarTelefone(Long clienteId, Long telefoneId) {
        Cliente cliente = clienteRepositorio.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        
        return cliente.getTelefones().stream()
                .filter(tel -> tel.getId().equals(telefoneId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Telefone não encontrado para este cliente"));
    }


  public Telefone editarTelefone(Long clienteId, Long telefoneId, TelefoneReqDto dto) {
        Cliente cliente = clienteRepositorio.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        
        Telefone telefoneAlvo = cliente.getTelefones().stream()
                .filter(tel -> tel.getId().equals(telefoneId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Telefone não encontrado para este cliente"));
        
        Telefone dadosAtualizados = converterTelefone(dto);
        atualizar(telefoneAlvo, dadosAtualizados);
        
        clienteRepositorio.save(cliente);
        return telefoneAlvo;
    }


    public void deletarTelefone(Long clienteId, Long telefoneId) {
        Cliente cliente = clienteRepositorio.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        
        boolean removido = cliente.getTelefones().removeIf(tel -> tel.getId().equals(telefoneId));
        
        if (!removido) {
            throw new RuntimeException("Telefone não encontrado para este cliente");
        }
        
        clienteRepositorio.save(cliente);
    }

	public TelefoneResDto toResDto(Telefone telefone) {
		if (telefone == null) return null;
		TelefoneResDto dto = new TelefoneResDto();
		dto.setId(telefone.getId());
		dto.setDdd(telefone.getDdd());
		dto.setNumero(telefone.getNumero());
		return dto;
	}
}