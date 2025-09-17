package challenge.tools.dto.transacao;

import challenge.tools.enumeration.transacao.TipoFormaPagamentoEnum;

public record FormaPagamentorRequisicaoDTO(
        TipoFormaPagamentoEnum tipo,
        Integer parcelas
) {
}
