package challenge.tools.entity.transacao;

import challenge.tools.enumeration.transacao.TransacaoStatusEnum;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class Descricao {
    private BigDecimal valor;
    private LocalDateTime dataHora;
    private String estabelecimento;
    private String nsu;
    private String codigoAutorizacao;
    private TransacaoStatusEnum status;



    public Descricao(BigDecimal valor, LocalDateTime dataHora, String estabelecimento) {
        this.valor = valor;
        this.dataHora = dataHora;
        this.estabelecimento = estabelecimento;
        this.status = TransacaoStatusEnum.AUTORIZADA;
        this.nsu = String.format("%06d", new Random().nextInt(999999));
        this.codigoAutorizacao = String.format("%06d", new Random().nextInt(999999));
    }
}
