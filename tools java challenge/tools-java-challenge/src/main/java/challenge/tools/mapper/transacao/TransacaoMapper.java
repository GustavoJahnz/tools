package challenge.tools.mapper.transacao;

import challenge.tools.dto.transacao.TransacaoResponseDTO;
import challenge.tools.entity.transacao.Transacao;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.FIELD)
public interface TransacaoMapper {
    @Mapping(source = "cartao", target = "cartao", qualifiedByName = "mascararCartao")
    TransacaoResponseDTO toDto(Transacao transacao);

    @Named("mascararCartao")
    default String mascararCartao(String cartao) {
        if (cartao == null || cartao.length() < 16) {
            return cartao;
        }
        String primeiros4 = cartao.substring(0, 4);
        String ultimos4 = cartao.substring(cartao.length() - 4);
        return primeiros4 + "********" + ultimos4;
    }
}
