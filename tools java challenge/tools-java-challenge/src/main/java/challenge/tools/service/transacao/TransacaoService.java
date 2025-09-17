package challenge.tools.service.transacao;

import challenge.tools.util.exception.NotFoundException;
import challenge.tools.dto.transacao.TransacaoPagamentoRequisicaoDTO;
import challenge.tools.entity.transacao.Descricao;
import challenge.tools.entity.transacao.FormaPagamento;
import challenge.tools.entity.transacao.Transacao;
import challenge.tools.enumeration.transacao.TransacaoStatusEnum;
import challenge.tools.repository.transacao.TransacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransacaoService {

    private final TransacaoRepository transacaoRepository;

    public Transacao realizarEstorno(Long id) {
        Transacao transacao = this.obterPorId(id);
        transacao.getDescricao().setStatus(TransacaoStatusEnum.NEGADA);
        transacao.setDataAtualizacao(LocalDateTime.now());
        return transacaoRepository.save(transacao);
    }

    public Transacao adicionarTransacao(TransacaoPagamentoRequisicaoDTO dto) {
        Descricao descricao = new Descricao(dto.descricao().valor(), dto.descricao().dataHora(), dto.descricao().estabelecimento());
        FormaPagamento formaPagamento = new FormaPagamento(dto.formaPagamento().tipo(), dto.formaPagamento().parcelas());
        Transacao transacao = new Transacao(dto.cartao(), descricao, formaPagamento);
        return transacaoRepository.save(transacao);
    }

    public Transacao obterPorId(Long id) {
        return transacaoRepository.findById(id).orElseThrow(NotFoundException.from("404", "Transação não encontrada"));
    }

    public Page<Transacao> listarTransacoes(Pageable pageable) {
        return transacaoRepository.findAll(pageable);
    }
}
