package challenge.tools.repository.transacao;

import challenge.tools.dto.transacao.TransacaoRequestParams;
import challenge.tools.entity.transacao.Transacao;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class TransacaoSpecification implements Specification<Transacao> {

    private final TransacaoRequestParams transacaoRequestParams;

    @Override
    public Predicate toPredicate(Root<Transacao> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if (Objects.nonNull(this.transacaoRequestParams.status())) {
            predicates.add(criteriaBuilder.equal(root.get("descricao").get("status"), this.transacaoRequestParams.status()));
        }
        if (Objects.nonNull(this.transacaoRequestParams.formaPagamento())) {
            predicates.add(criteriaBuilder.equal(root.get("formaPagamento").get("tipo"), this.transacaoRequestParams.formaPagamento()));
        }
        Predicate[] predicateArray = new Predicate[predicates.size()];
        return criteriaBuilder.and(predicates.toArray(predicateArray));
    }

}