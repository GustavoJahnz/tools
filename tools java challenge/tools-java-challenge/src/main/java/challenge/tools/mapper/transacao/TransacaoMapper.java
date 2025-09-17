package challenge.tools.mapper.transacao;

import challenge.tools.dto.transacao.TransacaoResponseDTO;
import challenge.tools.entity.transacao.Transacao;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.FIELD)
public interface TransacaoMapper {
    TransacaoResponseDTO toDto(Transacao transacao);
}
