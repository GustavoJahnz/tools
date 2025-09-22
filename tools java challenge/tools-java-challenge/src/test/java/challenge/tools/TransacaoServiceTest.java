package challenge.tools;

import challenge.tools.dto.transacao.DescricaoRequisicaoDTO;
import challenge.tools.dto.transacao.FormaPagamentorRequisicaoDTO;
import challenge.tools.dto.transacao.TransacaoPagamentoRequisicaoDTO;
import challenge.tools.dto.transacao.TransacaoRequestParams;
import challenge.tools.entity.transacao.Descricao;
import challenge.tools.entity.transacao.FormaPagamento;
import challenge.tools.entity.transacao.Transacao;
import challenge.tools.enumeration.transacao.TipoFormaPagamentoEnum;
import challenge.tools.enumeration.transacao.TransacaoStatusEnum;
import challenge.tools.repository.transacao.TransacaoRepository;
import challenge.tools.service.transacao.TransacaoService;
import challenge.tools.util.exception.BusinessException;
import challenge.tools.util.exception.NotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class TransacaoServiceTest {

    @Mock
    private TransacaoRepository transacaoRepository;

    @InjectMocks
    private TransacaoService transacaoService;

    @Test
    void deveCriarTransacaoComSucesso() {
        Descricao descricao = new Descricao(BigDecimal.ONE, LocalDateTime.of(2025, Month.SEPTEMBER, 17, 21, 0), "Estabelecimento");
        FormaPagamento formaPagamento = new FormaPagamento(TipoFormaPagamentoEnum.AVISTA, 1);
        Transacao entidade = new Transacao("111111111",
                descricao,
                formaPagamento
        );
        entidade.setId(1L);

        DescricaoRequisicaoDTO descricaoReqDto = new DescricaoRequisicaoDTO("Estabelecimento", LocalDateTime.of(2025, Month.SEPTEMBER, 17, 21, 0), BigDecimal.ONE);
        FormaPagamentorRequisicaoDTO formaPagamentorRequisicaoDTO = new FormaPagamentorRequisicaoDTO(TipoFormaPagamentoEnum.AVISTA, 1);
        TransacaoPagamentoRequisicaoDTO transacaoRequest = new TransacaoPagamentoRequisicaoDTO("111111111", descricaoReqDto, formaPagamentorRequisicaoDTO);

        Mockito.when(this.transacaoRepository.save(Mockito.any(Transacao.class)))
                .thenReturn(entidade);
        Transacao resultado = this.transacaoService.adicionarTransacao(transacaoRequest);

        Assertions.assertThat(resultado).isNotNull();
        Assertions.assertThat(resultado.getId()).isEqualTo(1L);
        Mockito.verify(this.transacaoRepository).save(Mockito.any(Transacao.class));
        Mockito.verifyNoMoreInteractions(this.transacaoRepository);
    }

    @Test
    void deveLancarExcecaoQuandoTransacaoAvistaComMaisDeUmaParcela() {
        TransacaoPagamentoRequisicaoDTO dto = new TransacaoPagamentoRequisicaoDTO(
                "111111111",
                new DescricaoRequisicaoDTO("Estabelecimento", LocalDateTime.now(), BigDecimal.TEN),
                new FormaPagamentorRequisicaoDTO(TipoFormaPagamentoEnum.AVISTA, 2)
        );

        Assertions.assertThatThrownBy(() -> transacaoService.adicionarTransacao(dto))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Pagamento avista só pode ter uma parcela");

        Mockito.verifyNoInteractions(transacaoRepository);
    }


    @Test
    void obterPorId() {
        Descricao descricao = new Descricao(BigDecimal.ONE, LocalDateTime.of(2025, Month.SEPTEMBER, 17, 21, 0), "Estabelecimento");
        FormaPagamento formaPagamento = new FormaPagamento(TipoFormaPagamentoEnum.AVISTA, 1);
        Transacao entidade = new Transacao("111111111",
                descricao,
                formaPagamento
        );
        entidade.setId(1L);

        Mockito.when(this.transacaoRepository.findById(1L))
                .thenReturn(Optional.of(entidade));
        Transacao resultado = this.transacaoService.obterPorId(1L);

        Assertions.assertThat(resultado).isNotNull();
        Assertions.assertThat(resultado.getId()).isEqualTo(1L);
        Mockito.verify(this.transacaoRepository).findById(1L);
        Mockito.verifyNoMoreInteractions(this.transacaoRepository);
    }

    @Test
    void estornarTransacao() {
        Descricao descricao = new Descricao(
                BigDecimal.ONE,
                LocalDateTime.of(2025, Month.SEPTEMBER, 17, 21, 0),
                "Estabelecimento"
        );
        FormaPagamento formaPagamento = new FormaPagamento(TipoFormaPagamentoEnum.AVISTA, 1);

        Transacao entidade = new Transacao("111111111", descricao, formaPagamento);
        entidade.setId(1L);

        Transacao entidadeAlterada = new Transacao("111111111",
                new Descricao(descricao.getValor(), descricao.getDataHora(), descricao.getEstabelecimento()),
                formaPagamento
        );
        entidadeAlterada.setId(1L);
        entidadeAlterada.getDescricao().setStatus(TransacaoStatusEnum.NEGADA);

        Mockito.when(transacaoRepository.findById(1L))
                .thenReturn(Optional.of(entidade));
        Mockito.when(transacaoRepository.save(Mockito.any(Transacao.class)))
                .thenReturn(entidadeAlterada);

        Transacao resultado = transacaoService.realizarEstorno(1L);

        Assertions.assertThat(resultado).isNotNull();
        Assertions.assertThat(resultado.getId()).isEqualTo(1L);
        Assertions.assertThat(resultado.getDescricao().getStatus())
                .isEqualTo(TransacaoStatusEnum.NEGADA);

        Mockito.verify(transacaoRepository).findById(1L);
        Mockito.verify(transacaoRepository).save(Mockito.any(Transacao.class));
        Mockito.verifyNoMoreInteractions(transacaoRepository);
    }

    @Test
    void deveLancarExcecaoQuandoTransacaoNaoEncontrado() {
        Mockito.when(this.transacaoRepository.findById(999L)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> this.transacaoService.obterPorId(999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Transação não encontrada");

        Mockito.verify(this.transacaoRepository).findById(999L);
        Mockito.verifyNoMoreInteractions(this.transacaoRepository);
    }

    @Test
    void devePesquisarTransacaosSemFiltroComSucesso() {
        Descricao descricao = new Descricao(
                BigDecimal.ONE,
                LocalDateTime.of(2025, Month.SEPTEMBER, 17, 21, 0),
                "Estabelecimento"
        );
        FormaPagamento formaPagamento = new FormaPagamento(TipoFormaPagamentoEnum.AVISTA, 1);
        Pageable pageable = PageRequest.of(0, 10);
        Transacao entidade = new Transacao("111111111", descricao, formaPagamento);
        entidade.setId(1L);

        TransacaoRequestParams transacaoRequestParams = new TransacaoRequestParams(null, null);
        Page<Transacao> transacaoPage = new PageImpl<>(List.of(entidade));
        Mockito.when(this.transacaoRepository.findAll(ArgumentMatchers.any(Specification.class), ArgumentMatchers.eq(pageable)))
                .thenReturn(transacaoPage);

        Page<Transacao> resultado = this.transacaoService.listarTransacoes(transacaoRequestParams, pageable);

        Assertions.assertThat(resultado).isNotEmpty();
        Assertions.assertThat(resultado.getContent()).hasSize(1);
        Assertions.assertThat(resultado.getContent().getFirst().getId()).isEqualTo(1L);
        Assertions.assertThat(resultado.getContent().getFirst().getCartao()).isEqualTo("111111111");

        Mockito.verify(this.transacaoRepository).findAll(ArgumentMatchers.any(Specification.class), ArgumentMatchers.eq(pageable));
        Mockito.verifyNoMoreInteractions(this.transacaoRepository);
    }

    @Test
    void devePesquisarTransacaosComFiltroStatusAutorizadaComSucesso() {
        Descricao descricao = new Descricao(
                BigDecimal.ONE,
                LocalDateTime.of(2025, Month.SEPTEMBER, 17, 21, 0),
                "Estabelecimento"
        );
        FormaPagamento formaPagamento = new FormaPagamento(TipoFormaPagamentoEnum.AVISTA, 1);
        Pageable pageable = PageRequest.of(0, 10);
        Transacao entidade = new Transacao("111111111", descricao, formaPagamento);
        entidade.setId(1L);

        TransacaoRequestParams transacaoRequestParams = new TransacaoRequestParams(TransacaoStatusEnum.AUTORIZADA, null);
        Page<Transacao> transacaoPage = new PageImpl<>(List.of(entidade));
        Mockito.when(this.transacaoRepository.findAll(ArgumentMatchers.any(Specification.class), ArgumentMatchers.eq(pageable)))
                .thenReturn(transacaoPage);

        Page<Transacao> resultado = this.transacaoService.listarTransacoes(transacaoRequestParams, pageable);

        Assertions.assertThat(resultado).isNotEmpty();
        Assertions.assertThat(resultado.getContent()).hasSize(1);
        Assertions.assertThat(resultado.getContent().getFirst().getId()).isEqualTo(1L);
        Assertions.assertThat(resultado.getContent().getFirst().getCartao()).isEqualTo("111111111");

        Mockito.verify(this.transacaoRepository).findAll(ArgumentMatchers.any(Specification.class), ArgumentMatchers.eq(pageable));
        Mockito.verifyNoMoreInteractions(this.transacaoRepository);
    }

    @Test
    void devePesquisarTransacaosComFiltroStatusNegadaComSucesso() {
        Descricao descricao = new Descricao(
                BigDecimal.ONE,
                LocalDateTime.of(2025, Month.SEPTEMBER, 17, 21, 0),
                "Estabelecimento"
        );
        FormaPagamento formaPagamento = new FormaPagamento(TipoFormaPagamentoEnum.AVISTA, 1);
        Pageable pageable = PageRequest.of(0, 10);
        Transacao entidade = new Transacao("111111111", descricao, formaPagamento);
        entidade.setId(1L);
        entidade.getDescricao().setStatus(TransacaoStatusEnum.NEGADA);

        TransacaoRequestParams transacaoRequestParams = new TransacaoRequestParams(TransacaoStatusEnum.NEGADA, null);
        Page<Transacao> transacaoPage = new PageImpl<>(List.of(entidade));
        Mockito.when(this.transacaoRepository.findAll(ArgumentMatchers.any(Specification.class), ArgumentMatchers.eq(pageable)))
                .thenReturn(transacaoPage);

        Page<Transacao> resultado = this.transacaoService.listarTransacoes(transacaoRequestParams, pageable);

        Assertions.assertThat(resultado).isNotEmpty();
        Assertions.assertThat(resultado.getContent()).hasSize(1);
        Assertions.assertThat(resultado.getContent().getFirst().getId()).isEqualTo(1L);
        Assertions.assertThat(resultado.getContent().getFirst().getCartao()).isEqualTo("111111111");

        Mockito.verify(this.transacaoRepository).findAll(ArgumentMatchers.any(Specification.class), ArgumentMatchers.eq(pageable));
        Mockito.verifyNoMoreInteractions(this.transacaoRepository);
    }

    @Test
    void devePesquisarTransacaosComFiltroFormaPagamentoComSucesso() {
        Descricao descricao = new Descricao(
                BigDecimal.ONE,
                LocalDateTime.of(2025, Month.SEPTEMBER, 17, 21, 0),
                "Estabelecimento"
        );
        FormaPagamento formaPagamento = new FormaPagamento(TipoFormaPagamentoEnum.AVISTA, 1);
        Pageable pageable = PageRequest.of(0, 10);
        Transacao entidade = new Transacao("111111111", descricao, formaPagamento);
        entidade.setId(1L);

        TransacaoRequestParams transacaoRequestParams = new TransacaoRequestParams(null, TipoFormaPagamentoEnum.AVISTA);
        Page<Transacao> transacaoPage = new PageImpl<>(List.of(entidade));
        Mockito.when(this.transacaoRepository.findAll(ArgumentMatchers.any(Specification.class), ArgumentMatchers.eq(pageable)))
                .thenReturn(transacaoPage);

        Page<Transacao> resultado = this.transacaoService.listarTransacoes(transacaoRequestParams, pageable);

        Assertions.assertThat(resultado).isNotEmpty();
        Assertions.assertThat(resultado.getContent()).hasSize(1);
        Assertions.assertThat(resultado.getContent().getFirst().getId()).isEqualTo(1L);
        Assertions.assertThat(resultado.getContent().getFirst().getCartao()).isEqualTo("111111111");
        Assertions.assertThat(resultado.getContent().getFirst().getFormaPagamento().getTipo()).isEqualTo(TipoFormaPagamentoEnum.AVISTA);

        Mockito.verify(this.transacaoRepository).findAll(ArgumentMatchers.any(Specification.class), ArgumentMatchers.eq(pageable));
        Mockito.verifyNoMoreInteractions(this.transacaoRepository);
    }

    @Test
    void devePesquisarTransacaosComFiltroCombinadoComSucesso() {
        Descricao descricao = new Descricao(
                BigDecimal.ONE,
                LocalDateTime.of(2025, Month.SEPTEMBER, 17, 21, 0),
                "Estabelecimento"
        );
        FormaPagamento formaPagamento = new FormaPagamento(TipoFormaPagamentoEnum.AVISTA, 1);
        Pageable pageable = PageRequest.of(0, 10);
        Transacao entidade = new Transacao("111111111", descricao, formaPagamento);
        entidade.setId(1L);

        TransacaoRequestParams params = new TransacaoRequestParams(TransacaoStatusEnum.AUTORIZADA, TipoFormaPagamentoEnum.AVISTA);
        Page<Transacao> transacaoPage = new PageImpl<>(List.of(entidade));
        Mockito.when(this.transacaoRepository.findAll(ArgumentMatchers.any(Specification.class), ArgumentMatchers.eq(pageable)))
                .thenReturn(transacaoPage);

        Page<Transacao> resultado = this.transacaoService.listarTransacoes(params, pageable);

        Assertions.assertThat(resultado).isNotEmpty();
        Assertions.assertThat(resultado.getContent()).hasSize(1);
        Assertions.assertThat(resultado.getContent().getFirst().getDescricao().getStatus()).isEqualTo(TransacaoStatusEnum.AUTORIZADA);
        Assertions.assertThat(resultado.getContent().getFirst().getFormaPagamento().getTipo()).isEqualTo(TipoFormaPagamentoEnum.AVISTA);

        Mockito.verify(this.transacaoRepository).findAll(ArgumentMatchers.any(Specification.class), ArgumentMatchers.eq(pageable));
        Mockito.verifyNoMoreInteractions(this.transacaoRepository);
    }

    @Test
    void deveRetornarVazioQuandoNaoExistemTransacoes() {
        Pageable pageable = PageRequest.of(0, 10);
        TransacaoRequestParams params = new TransacaoRequestParams(null, null);

        Mockito.when(this.transacaoRepository.findAll(ArgumentMatchers.any(Specification.class), ArgumentMatchers.eq(pageable)))
                .thenReturn(Page.empty());

        Page<Transacao> resultado = this.transacaoService.listarTransacoes(params, pageable);

        Assertions.assertThat(resultado).isEmpty();

        Mockito.verify(this.transacaoRepository).findAll(ArgumentMatchers.any(Specification.class), ArgumentMatchers.eq(pageable));
        Mockito.verifyNoMoreInteractions(this.transacaoRepository);
    }

}
