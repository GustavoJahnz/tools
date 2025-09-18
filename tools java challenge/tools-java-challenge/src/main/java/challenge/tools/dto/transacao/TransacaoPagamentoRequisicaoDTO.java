package challenge.tools.dto.transacao;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.CreditCardNumber;

public record TransacaoPagamentoRequisicaoDTO(
        @NotNull
        @CreditCardNumber
        String cartao,
        @NotNull
        DescricaoRequisicaoDTO descricao,
        @NotNull
        FormaPagamentorRequisicaoDTO formaPagamento
) {
}
