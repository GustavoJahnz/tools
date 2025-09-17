package challenge.tools.dto.transacao;

import challenge.tools.enumeration.transacao.TransacaoStatusEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DescricaoResponseDTO(
        BigDecimal valor,
        LocalDateTime dataHora,
        String estabelecimento,
        String nsu,
        String codigoAutorizacao,
        TransacaoStatusEnum status
) {
}
