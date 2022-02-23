package com.autobots.automanager.services;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager.dto.ClienteReqDto;
import com.autobots.automanager.dto.ClienteResDto;
import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.repositorios.ClienteRepositorio;


@Service
public class ClienteCadastrador {
	

	@Autowired
	private ClienteRepositorio repositorio;
	private StringVerificadorNulo verificador = new StringVerificadorNulo();
	@Autowired
    private EnderecoServico enderecoServico;

    @Autowired
    private TelefoneServico telefoneServico;

    @Autowired
    private DocumentoServico documentoServico;

	private Cliente converterCliente(ClienteReqDto dto) {
        Cliente cliente = new Cliente();
        cliente.setNome(dto.getNome());
        cliente.setNomeSocial(dto.getNomeSocial());
        cliente.setDataNascimento(dto.getDataNascimento());
		

        if (dto.getEndereco() != null) {
            cliente.setEndereco(enderecoServico.converterEndereco(dto.getEndereco()));
        }

        if (dto.getTelefones() != null) {
            cliente.getTelefones().addAll(telefoneServico.converterListaTelefones(dto.getTelefones()));
        }

        if (dto.getDocumentos() != null) {
            cliente.getDocumentos().addAll(documentoServico.converterListaDocumentos(dto.getDocumentos()));
        }

        return cliente;
    }

	

	public Cliente criarCliente(ClienteReqDto dto) {
		Cliente cliente = converterCliente(dto);
		cliente.setDataCadastro(Calendar.getInstance().getTime());
		
		return repositorio.save(cliente);
		
	}

	public Cliente selecionarPorId(Long id) {
		return repositorio.findById(id).orElse(null);
	}

	public List<Cliente> selecionarTodos() {
		return repositorio.findAll();
	}

	//Cliente atualizador

	private void atualizarDados(Cliente cliente, ClienteReqDto atualizacao) {
		if (!verificador.verificar(atualizacao.getNome())) {
			cliente.setNome(atualizacao.getNome());
		}
		if (!verificador.verificar(atualizacao.getNomeSocial())) {
			cliente.setNomeSocial(atualizacao.getNomeSocial());
		}
		
		if (!(atualizacao.getDataNascimento() == null)) {
			cliente.setDataNascimento(atualizacao.getDataNascimento());
		}
	}

	public Cliente atualizar(Long id, ClienteReqDto atualizacao) {
		Cliente cliente = repositorio.findById(id).orElse(null);
		if (cliente != null) {
			atualizarDados(cliente, atualizacao);
			
			if (atualizacao.getEndereco() != null) {
				enderecoServico.atualizar(cliente.getEndereco(), enderecoServico.converterEndereco(atualizacao.getEndereco()));
			}

			if (atualizacao.getTelefones() != null) {
				telefoneServico.atualizar(cliente.getTelefones(), telefoneServico.converterListaTelefones(atualizacao.getTelefones()));
			}

			if (atualizacao.getDocumentos() != null) {
				documentoServico.atualizar(cliente.getDocumentos(), documentoServico.converterListaDocumentos(atualizacao.getDocumentos()));
			}

			return repositorio.save(cliente);
		}
		return null;
	}

	public void excluir(Long id) {
		Cliente cliente = repositorio.findById(id).orElse(null);
		if (cliente != null) {
			repositorio.delete(cliente);
		}
	}

	public ClienteResDto toResDto(Cliente cliente) {
		if (cliente == null) return null;
		ClienteResDto dto = new ClienteResDto();
		dto.setId(cliente.getId());
		dto.setNome(cliente.getNome());
		dto.setNomeSocial(cliente.getNomeSocial());
		dto.setDataNascimento(cliente.getDataNascimento());
		dto.setDataCadastro(cliente.getDataCadastro());
		if (cliente.getEndereco() != null) {
			dto.setEndereco(enderecoServico.toResDto(cliente.getEndereco()));
		}
		dto.setTelefones(
			cliente.getTelefones().stream()
				.map(telefoneServico::toResDto)
				.collect(Collectors.toList())
		);
		dto.setDocumentos(
			cliente.getDocumentos().stream()
				.map(documentoServico::toResDto)
				.collect(Collectors.toList())
		);
		return dto;
	}

	public List<ClienteResDto> toResDtoList(List<Cliente> clientes) {
		return clientes.stream().map(this::toResDto).collect(Collectors.toList());
	}

}
