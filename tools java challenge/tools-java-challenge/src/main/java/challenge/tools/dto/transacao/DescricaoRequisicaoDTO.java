package challenge.tools.dto.transacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DescricaoRequisicaoDTO(
        String estabelecimento,
        LocalDateTime dataHora,
        BigDecimal valor
) {
}
