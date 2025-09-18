package challenge.tools.dto.transacao;

import challenge.tools.enumeration.transacao.TipoFormaPagamentoEnum;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record FormaPagamentorRequisicaoDTO(
        @NotNull
        TipoFormaPagamentoEnum tipo,
        @NotNull
        @Positive
        Integer parcelas
) {
}
