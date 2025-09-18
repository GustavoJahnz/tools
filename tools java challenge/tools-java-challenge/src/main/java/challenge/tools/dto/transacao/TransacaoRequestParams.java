package challenge.tools.dto.transacao;

import challenge.tools.enumeration.transacao.TipoFormaPagamentoEnum;
import challenge.tools.enumeration.transacao.TransacaoStatusEnum;

public record TransacaoRequestParams(
        TransacaoStatusEnum status,
        TipoFormaPagamentoEnum formaPagamento
) {
}
