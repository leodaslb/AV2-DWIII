package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.dto.ClienteReqDto;
import com.autobots.automanager.dto.ClienteResDto;
import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.modelos.AdicionadorLinkCliente;
import com.autobots.automanager.services.ClienteCadastrador;

@RestController
@RequestMapping("/cliente")
public class ClienteControle {

	@Autowired
	private ClienteCadastrador cadastrador;

	@Autowired
	private AdicionadorLinkCliente adicionadorLink;

	@GetMapping("/{id}")
	public ResponseEntity< ClienteResDto> obterCliente(@PathVariable Long id) {
		Cliente cliente = cadastrador.selecionarPorId(id);
		if (cliente == null) {
            return ResponseEntity.notFound().build(); 
        }
		ClienteResDto dto = cadastrador.toResDto(cliente);
		adicionadorLink.adicionarLink(dto);
		return ResponseEntity.ok(dto);
	}

	@GetMapping("/clientes")
	public ResponseEntity<List<ClienteResDto>> obterClientes() {
		List<Cliente> clientes = cadastrador.selecionarTodos();
		if (clientes.isEmpty()) {
            return ResponseEntity.notFound().build(); 
        }
		List<ClienteResDto> dto = cadastrador.toResDtoList(clientes);
		adicionadorLink.adicionarLink(dto);
		return ResponseEntity.ok(dto);
	}

	@PostMapping("/cadastro")
	public ResponseEntity<ClienteResDto> criarCliente(@RequestBody ClienteReqDto dto) {
		Cliente cliente = cadastrador.criarCliente(dto);
		ClienteResDto cliDto = cadastrador.toResDto(cliente);
		adicionadorLink.adicionarLink(cliDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(cliDto);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ClienteResDto> atualizarCliente(@PathVariable Long id, @RequestBody ClienteReqDto dto) {
		Cliente cliente = cadastrador.atualizar(id, dto);
		if (cliente == null) {
            return ResponseEntity.notFound().build(); // 404 
        }
		ClienteResDto cliDto = cadastrador.toResDto(cliente);
		adicionadorLink.adicionarLink(cliDto);
		return ResponseEntity.ok(cliDto);
	}

	@DeleteMapping("/{id}")
	public void excluirCliente(@PathVariable Long id) {
		cadastrador.excluir(id);
	}
}
