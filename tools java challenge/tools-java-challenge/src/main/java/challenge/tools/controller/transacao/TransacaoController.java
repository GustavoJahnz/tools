package challenge.tools.controller.transacao;

import challenge.tools.dto.transacao.TransacaoPagamentoRequisicaoDTO;
import challenge.tools.dto.transacao.TransacaoRequestParams;
import challenge.tools.dto.transacao.TransacaoResponseDTO;
import challenge.tools.entity.transacao.Transacao;
import challenge.tools.mapper.transacao.TransacaoMapper;
import challenge.tools.service.transacao.TransacaoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Transacoes", description = "Endpoints para gerenciar transações")
@CrossOrigin
@RestController
@RequestMapping("api/v1/transacoes")
@RequiredArgsConstructor
public class TransacaoController {
    private final TransacaoService transacaoService;

    private final TransacaoMapper transacaoMapper;

    @GetMapping
    public ResponseEntity<Page<TransacaoResponseDTO>> listarTransacoes(TransacaoRequestParams params, @PageableDefault(sort="descricao.dataHora") Pageable pageable) {
        Page<Transacao> transacoes = transacaoService.listarTransacoes(params, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(transacoes.map(transacaoMapper::toDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransacaoResponseDTO> obterTransacaoPorId(@PathVariable Long id) {
        Transacao transacao = transacaoService.obterPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(transacaoMapper.toDto(transacao));
    }

    @PostMapping("/pagamento")
    public ResponseEntity<TransacaoResponseDTO> adicionarTransacao(@Valid @RequestBody TransacaoPagamentoRequisicaoDTO dto) {
        Transacao transacao = transacaoService.adicionarTransacao(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(transacaoMapper.toDto(transacao));
    }

    @PutMapping("/{id}/estorno")
    public ResponseEntity<TransacaoResponseDTO> realizarEstorno(@PathVariable Long id) {
        Transacao transacao = transacaoService.realizarEstorno(id);
        return ResponseEntity.status(HttpStatus.OK).body(transacaoMapper.toDto(transacao));
    }
}

