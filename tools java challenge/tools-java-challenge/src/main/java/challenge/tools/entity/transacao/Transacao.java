package challenge.tools.entity.transacao;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "transacao")
@SequenceGenerator(name = "transacao_seq", sequenceName = "transacao_seq", allocationSize = 1)
@Data
@NoArgsConstructor
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transacao_seq")
    @Column(name = "idtransacao", unique = true)
    private Long id;
    @Column(name = "cartao")
    private String cartao;
    @Embedded
    private Descricao descricao;
    @Embedded
    private FormaPagamento formaPagamento;
    @Column(name = "idformapagamento")
    private LocalDateTime dataAtualizacao;

    public Transacao(String cartao, Descricao descricao, FormaPagamento formaPagamento) {
        this.cartao = cartao;
        this.descricao = descricao;
        this.formaPagamento = formaPagamento;
    }

}
