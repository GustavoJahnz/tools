package challenge.tools.dto.transacao;

public record TransacaoPagamentoRequisicaoDTO(
        String cartao,
        DescricaoRequisicaoDTO descricao,
        FormaPagamentorRequisicaoDTO formaPagamento
) {
}
