package challenge.tools.dto.transacao;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DescricaoRequisicaoDTO(
        @NotNull
        String estabelecimento,
        @NotNull
        LocalDateTime dataHora,
        @NotNull
        @Positive
        BigDecimal valor
) {
}
