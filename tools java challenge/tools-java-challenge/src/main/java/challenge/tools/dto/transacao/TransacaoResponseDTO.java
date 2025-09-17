package challenge.tools.dto.transacao;

public record TransacaoResponseDTO(
        Long id,
        DescricaoResponseDTO descricao,
        FormaPagamentorRequisicaoDTO formaPagamento,
        String cartao
) {
}
