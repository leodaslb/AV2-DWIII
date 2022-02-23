package com.autobots.automanager.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager.dto.EnderecoReqDto;
import com.autobots.automanager.dto.EnderecoResDto;
import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.EnderecoRepositorio;

@Service
public class EnderecoServico {
    
    private StringVerificadorNulo verificador = new StringVerificadorNulo();

    @Autowired
    private ClienteRepositorio clienteRepositorio;
    @Autowired
    private EnderecoRepositorio enderecoRepositorio;
    public Endereco converterEndereco(EnderecoReqDto enderecoReqDto) {
        if (enderecoReqDto == null) return null;

        Endereco end = new Endereco();
        end.setEstado(enderecoReqDto.getEstado());
        end.setCidade(enderecoReqDto.getCidade());
        end.setBairro(enderecoReqDto.getBairro());
        end.setRua(enderecoReqDto.getRua());
        end.setNumero(enderecoReqDto.getNumero());
        end.setCodigoPostal(enderecoReqDto.getCodigoPostal());
        end.setInformacoesAdicionais(enderecoReqDto.getInformacoesAdicionais());
        return end;
    }

    public void atualizar(Endereco endereco, Endereco atualizacao) {
        if (atualizacao != null) {
            if (!verificador.verificar(atualizacao.getEstado())) {
                endereco.setEstado(atualizacao.getEstado());
            }
            if (!verificador.verificar(atualizacao.getCidade())) {
                endereco.setCidade(atualizacao.getCidade());
            }
            if (!verificador.verificar(atualizacao.getBairro())) {
                endereco.setBairro(atualizacao.getBairro());
            }
            if (!verificador.verificar(atualizacao.getRua())) {
                endereco.setRua(atualizacao.getRua());
            }
            if (!verificador.verificar(atualizacao.getNumero())) {
                endereco.setNumero(atualizacao.getNumero());
            }
            if (!verificador.verificar(atualizacao.getCodigoPostal())) {
                endereco.setCodigoPostal(atualizacao.getCodigoPostal());
            }
            if (!verificador.verificar(atualizacao.getInformacoesAdicionais())) {
                endereco.setInformacoesAdicionais(atualizacao.getInformacoesAdicionais());
            }
        }
    }

    public Endereco adicionarEndereco(Long clienteId, EnderecoReqDto dto) {
        Cliente cliente = clienteRepositorio.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com ID: " + clienteId));
        
        Endereco novoEndereco = converterEndereco(dto);
        cliente.setEndereco(novoEndereco);
        
        Cliente salvo = clienteRepositorio.save(cliente);
        return salvo.getEndereco();
    }

    public List<Endereco> mostrarTodos() {
	return enderecoRepositorio.findAll();
}

    public Endereco selecionarEndereco(Long clienteId) {
        Cliente cliente = clienteRepositorio.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com ID: " + clienteId));
        
        if (cliente.getEndereco() == null) {
            throw new RuntimeException("Cliente não possui endereço cadastrado");
        }
        
        return cliente.getEndereco();
    }

    public Endereco editarEndereco(Long clienteId, EnderecoReqDto dto) {
        Cliente cliente = clienteRepositorio.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com ID: " + clienteId));
        
        if (cliente.getEndereco() == null) {
            cliente.setEndereco(converterEndereco(dto));
        } else {
            Endereco dadosAtualizados = converterEndereco(dto);
            atualizar(cliente.getEndereco(), dadosAtualizados);
        }
        
        Cliente salvo = clienteRepositorio.save(cliente);
        return salvo.getEndereco();
    }

    public void deletarEndereco(Long clienteId) {
        Cliente cliente = clienteRepositorio.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com ID: " + clienteId));
        
        cliente.setEndereco(null);
        clienteRepositorio.save(cliente);
    }

    public EnderecoResDto toResDto(Endereco endereco) {
        if (endereco == null) return null;
        EnderecoResDto dto = new EnderecoResDto();
        dto.setId(endereco.getId());
        dto.setEstado(endereco.getEstado());
        dto.setCidade(endereco.getCidade());
        dto.setBairro(endereco.getBairro());
        dto.setRua(endereco.getRua());
        dto.setNumero(endereco.getNumero());
        dto.setCodigoPostal(endereco.getCodigoPostal());
        dto.setInformacoesAdicionais(endereco.getInformacoesAdicionais());
        return dto;
    }
    public List<EnderecoResDto> toResDtoList(List<Endereco> enderecos){
        return enderecos.stream().map(this::toResDto).collect(Collectors.toList());
    }
}