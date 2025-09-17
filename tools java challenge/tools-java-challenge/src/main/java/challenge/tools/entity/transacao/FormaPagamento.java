package challenge.tools.entity.transacao;

import challenge.tools.enumeration.transacao.TipoFormaPagamentoEnum;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@NoArgsConstructor
@Getter
@Setter
public class FormaPagamento {
    private Integer parcelas;
    private TipoFormaPagamentoEnum tipo;

    public FormaPagamento(TipoFormaPagamentoEnum tipo, Integer parcelas) {
        this.tipo = tipo;
        this.parcelas = parcelas;
    }
}
