package br.com.food.repository;

import br.com.food.entity.Estabelecimento;
import br.com.food.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Repository
@Transactional(readOnly = true)
public abstract class GenericRepository {

    @PersistenceContext
    private EntityManager em;

    private CriteriaBuilder getCriteriaBuilder() { return em.getCriteriaBuilder(); }

    private <E extends Enum<E>> ParameterExpression<E> createEnumParameterExpression(
        CriteriaBuilder builder, Root<?> root, String conditionalName, E conditional) {
        Class<E> enumType = (Class<E>) conditional.getDeclaringClass();
        return builder.parameter(enumType, conditionalName);
    }

    /**
     *Metodo pode ser chamado: getQuery(Aluno.class).getSingleResult() ou getQuery(Aluno.class).getResultList()
     * @param classe exemplo: Aluno.class
     * @return getSingleResult() ou getResultList()
     * @param <T> tipo generico
     */
    public <T> TypedQuery<T> getQuery(Class<T> classe) {
        CriteriaBuilder builder = this.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = builder.createQuery(classe);
        Root<T> root = criteriaQuery.from(classe);
        criteriaQuery.select(root);

        return this.em.createQuery(criteriaQuery);
    }

    /**
     * Metodo igual ao getQuery a diferença que pode ordenar o retorno se for uma lista
     * @param classe a classe que irá retornar ou irá fazer a busca
     * @param orderByPropertyName nome da propriedade para ordenação
     * @return getSingleResult() ou getResultList()
     * @param <T> generico
     */
    public <T> TypedQuery<T> getQueryAndOrdenation(Class<T> classe, String orderByPropertyName) {
        CriteriaBuilder builder = this.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = builder.createQuery(classe);
        Root<T> root = criteriaQuery.from(classe);
        criteriaQuery.select(root);

        if (orderByPropertyName != null && !orderByPropertyName.isEmpty()) {
            criteriaQuery.orderBy(builder.asc(root.get(orderByPropertyName)));
        }

        return this.em.createQuery(criteriaQuery);
    }

    public <T> T getEntityById(Class<T> entityClass, int id) { return this.em.find(entityClass, id); }

    public <T> T getEntityById(Class<T> entityClass, String id) { return this.em.find(entityClass, id); }

    public <T> T getEntityById(Class<T> entityClass, Long id) { return this.em.find(entityClass, id); }

    public <T> T getEntityByProperty(
        Class<T> classe,
        String propertySeachName,
        String property
    ) {
        CriteriaBuilder builder = this.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = builder.createQuery(classe);
        Root<T> root = criteriaQuery.from(classe);
        criteriaQuery.select(root);

        criteriaQuery.where(builder.equal(root.get(propertySeachName), property));

        TypedQuery<T> query = this.em.createQuery(criteriaQuery);

        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public <T> T getEntityByProperty(
            Class<T> classe,
            String propertySeachName,
            Long property
    ) {
        CriteriaBuilder builder = this.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = builder.createQuery(classe);
        Root<T> root = criteriaQuery.from(classe);
        criteriaQuery.select(root);

        criteriaQuery.where(builder.equal(root.get(propertySeachName), property));

        TypedQuery<T> query = this.em.createQuery(criteriaQuery);

        return query.getSingleResult();
    }

    /**
     * Metodo busca por exemplo: todos os produtos de um estabelecimento
     * exemplo: this.getEntitiesByForeignKey(Peoduct.class, "estabelecimento", "idestabelecimento",
     *                 "idestabelecimento", idestabelecimento, "descricao");
     * @param entityClass uso Produto.class
     * @param entityName nome da tabela no banco, tabela que irá fazer o join
     * @param entityId nome do id da tabela que irá fazer o join
     * @param foreignKeyPropertyName o nome que irá servir para fazer o join
     * @param foreignKeyId o id de forma primitiva da tabela que irá realizar o join
     * @param orderByPropertyName atributo que irá servir para realizar a ordenação
     * @return getResultList()
     * @param <T> generico
     */
    public <T> List<T> getEntitiesByForeignKey(
            Class<T> entityClass,
            String entityName,
            String entityId,
            String foreignKeyPropertyName,
            int foreignKeyId,
            String orderByPropertyName
    ) {
        CriteriaBuilder builder = this.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = builder.createQuery(entityClass);
        Root<T> root = criteriaQuery.from(entityClass);
        criteriaQuery.select(root);

        List<Predicate> predicates = new ArrayList<>();

        ParameterExpression<Integer> exForeignKeyId = builder.parameter(Integer.class, entityId);
        predicates.add(builder.equal(root.get(entityName).get(foreignKeyPropertyName), exForeignKeyId));
        criteriaQuery.where(predicates.toArray(new Predicate[0]));

        if (orderByPropertyName != null && !orderByPropertyName.isEmpty()) {
            criteriaQuery.orderBy(builder.asc(root.get(orderByPropertyName)));
        }

        TypedQuery<T> query = em.createQuery(criteriaQuery);
        query.setParameter(foreignKeyPropertyName, foreignKeyId);

        return query.getResultList();
    }

    /**
     * Metodo retorna lista de entidades passando duas condicionais booleans para verificação
     * Exemplo: para buscar todos os clientes do estabelecimento
     * @param entityClass User.class => exemplificando o tipo que será buscado no banco
     * @param entityName aqui é a entidade que está relacionada, no caso é: estabelecimento
     * @param foreignKeyIdName idestabelecimento => o nome da chave primária da entidade relacionada
     * @param foreignKeyId idestabelecimento => aqui se referindo ao tipo primário, no caso é um int
     * @param orderByPropertyName => a propriedade que será usada para ordenar a lista, o retorno
     * @param conditionalName => nome do campo no banco para comparação
     * @param conditional => passe a condicional
     * @return => uma lista
     * @param <T> => generico
     *Nesse exemplo, se eu passar firstConditionalName = is_client, firstConditional = true,
     * estarei buscando todos os clientes
     */
    public <T, E extends Enum<E>> List<T> getEntitiesByForeignKeyAndWithConditional(
            Class<T> entityClass,
            String entityName,
            String foreignKeyIdName,
            int foreignKeyId,
            String orderByPropertyName,
            String conditionalName,
            E conditional
    ) {
        CriteriaBuilder builder = this.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = builder.createQuery(entityClass);
        Root<T> root = criteriaQuery.from(entityClass);
        criteriaQuery.select(root);

        List<Predicate> predicates = new ArrayList<>();

        ParameterExpression<Integer> exForeignKeyId = builder.parameter(Integer.class, foreignKeyIdName);
        predicates.add(builder.equal(root.get(entityName).get(foreignKeyIdName), exForeignKeyId));

        ParameterExpression<E> exConditional = this.createEnumParameterExpression(builder,root,conditionalName,conditional);
        predicates.add(builder.equal(root.get(conditionalName), exConditional));

        criteriaQuery.where(predicates.toArray(new Predicate[0]));

        if (orderByPropertyName != null && !orderByPropertyName.isEmpty()) {
            criteriaQuery.orderBy(builder.asc(root.get(orderByPropertyName)));
        }

        TypedQuery<T> query = em.createQuery(criteriaQuery);
        query.setParameter(foreignKeyIdName, foreignKeyId);
        query.setParameter(exConditional, conditional);

        return query.getResultList();
    }

    public <T, E extends Enum<E>> List<T> getTwoEntitiesByForeignKeyAndWithConditional(
            Class<T> entityClass,
            String firstEntityName,
            String secondEntityName,
            String foreignKeyPropertyName,
            int foreignKeyId,
            String orderByPropertyName,
            String conditionalName,
            E conditional
    ) {
        CriteriaBuilder builder = this.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = builder.createQuery(entityClass);
        Root<T> root = criteriaQuery.from(entityClass);
        criteriaQuery.select(root);

        List<Predicate> predicates = new ArrayList<>();

        ParameterExpression<Integer> exForeignKeyId = builder.parameter(Integer.class, foreignKeyPropertyName);
        predicates.add(builder.equal(root.get(firstEntityName).get(secondEntityName).get(foreignKeyPropertyName), exForeignKeyId));

        ParameterExpression<Enum> exConditional = builder.parameter(Enum.class, conditionalName);
        predicates.add(builder.equal(root.get(conditionalName), exConditional));

        criteriaQuery.where(predicates.toArray(new Predicate[0]));

        if (orderByPropertyName != null && !orderByPropertyName.isEmpty()) {
            criteriaQuery.orderBy(builder.asc(root.get(orderByPropertyName)));
        }

        TypedQuery<T> query = em.createQuery(criteriaQuery);
        query.setParameter(foreignKeyPropertyName, foreignKeyId);
        query.setParameter(exConditional, conditional);

        return query.getResultList();
    }

    public <T> List<T> getTwoEntitiesByForeignKey(
            Class<T> entityClass,
            String firstEntityName,
            String secondEntityName,
            String foreignKeyPropertyName,
            int foreignKeyId,
            String orderByPropertyName
    ) {
        CriteriaBuilder builder = this.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = builder.createQuery(entityClass);
        Root<T> root = criteriaQuery.from(entityClass);
        criteriaQuery.select(root);

        List<Predicate> predicates = new ArrayList<>();

        ParameterExpression<Integer> exForeignKeyId = builder.parameter(Integer.class, foreignKeyPropertyName);
        predicates.add(builder.equal(root.get(firstEntityName).get(secondEntityName).get(foreignKeyPropertyName), exForeignKeyId));
        criteriaQuery.where(predicates.toArray(new Predicate[0]));

        if (orderByPropertyName != null && !orderByPropertyName.isEmpty()) {
            criteriaQuery.orderBy(builder.asc(root.get(orderByPropertyName)));
        }

        TypedQuery<T> query = em.createQuery(criteriaQuery);
        query.setParameter(foreignKeyPropertyName, foreignKeyId);

        return query.getResultList();
    }

    /**
     * Metodo irá realizar um JoinColuon em uma tabela de relacionamento para traz
     *
     */
    public <T> List<T> getJoinColumn(
            Class<T> entityClass,
            String entityJoinName,
            String entityId,
            int foreignKeyId,
            String orderByPropertyName
    ) {
        CriteriaBuilder builder = this.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(entityClass);
        Root<T> root = query.from(entityClass);

        Path<Integer> grupoPath = root.join(entityJoinName).get(entityId);

        List<Predicate> predicates = new ArrayList<>();

        Predicate equals = builder.equal(grupoPath,foreignKeyId);
        predicates.add(equals);

        query.where( predicates.toArray(new Predicate[0]));

        if (orderByPropertyName != null && !orderByPropertyName.isEmpty()) {
            query.orderBy(builder.asc(root.get(orderByPropertyName)));
        }

        TypedQuery<T> typedQuery = em.createQuery(query);

        return typedQuery.getResultList();
    }

    public <T> List<T> getJoinColumn(
            Class<T> entityClass,
            String entityJoinName,
            String entityId,
             String foreignKeyId,
            String orderByPropertyName
    ) {
        CriteriaBuilder builder = this.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(entityClass);
        Root<T> root = query.from(entityClass);

        Path<String> grupoPath = root.join(entityJoinName).get(entityId);

        List<Predicate> predicates = new ArrayList<>();

        Predicate equals = builder.equal(grupoPath,foreignKeyId);
        predicates.add(equals);

        query.where( predicates.toArray(new Predicate[0]));

        if (orderByPropertyName != null && !orderByPropertyName.isEmpty()) {
            query.orderBy(builder.asc(root.get(orderByPropertyName)));
        }

        TypedQuery<T> typedQuery = em.createQuery(query);

        return typedQuery.getResultList();
    }

    public <T> List<T> getJoinColumn(
            Class<T> entityClass,
            String entityJoinName,
            String entityId,
            Long foreignKeyId,
            String orderByPropertyName
    ) {
        CriteriaBuilder builder = this.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(entityClass);
        Root<T> root = query.from(entityClass);

        Path<Long> grupoPath = root.join(entityJoinName).get(entityId);

        List<Predicate> predicates = new ArrayList<>();

        Predicate equals = builder.equal(grupoPath,foreignKeyId);
        predicates.add(equals);

        query.where( predicates.toArray(new Predicate[0]));

        if (orderByPropertyName != null && !orderByPropertyName.isEmpty()) {
            query.orderBy(builder.asc(root.get(orderByPropertyName)));
        }

        TypedQuery<T> typedQuery = em.createQuery(query);

        return typedQuery.getResultList();
    }

    public <T> List<T> getJoinColumnWithDateAndPagination(
            Class<T> entityClass,
            String entityJoinName,
            String entityId,
            String foreignKeyId,
            String dateFieldName,
            Date startDate,
            Date endDate,
            String orderByPropertyName,
            int pageNumber,
            int pageSize
    ) {
        CriteriaBuilder builder = this.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(entityClass);
        Root<T> root = query.from(entityClass);

        Path<String> groupPath = root.join(entityJoinName).get(entityId);

        List<Predicate> predicates = new ArrayList<>();

        Predicate equals = builder.equal(groupPath, foreignKeyId);
        predicates.add(equals);

        if (startDate != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get(dateFieldName), startDate));
        }
        if (endDate != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get(dateFieldName), endDate));
        }

        query.where(predicates.toArray(new Predicate[0]));

        if (orderByPropertyName != null && !orderByPropertyName.isEmpty()) {
            query.orderBy(builder.asc(root.get(orderByPropertyName)));
        }

        TypedQuery<T> typedQuery = em.createQuery(query);

        typedQuery.setFirstResult((pageNumber - 1) * pageSize);
        typedQuery.setMaxResults(pageSize);

        return typedQuery.getResultList();

//        long total = getTotalCount(entityClass, entityJoinName, entityId, foreignKeyId);

//        return new PageImpl<>(resultList, PageRequest.of(pageNumber - 1, pageSize), total);
    }

    public <T> Page<T> getJoinColumnWithDateAndPagination(
            Class<T> entityClass,
            String entityJoinName,
            String entityId,
            int foreignKeyId,
            String dateFieldName,
            Date startDate,
            Date endDate,
            String orderByPropertyName,
            int pageNumber,
            int pageSize
    ) {
        CriteriaBuilder builder = this.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(entityClass);
        Root<T> root = query.from(entityClass);

        Path<Integer> groupPath = root.join(entityJoinName).get(entityId);

        List<Predicate> predicates = new ArrayList<>();

        Predicate equals = builder.equal(groupPath, foreignKeyId);
        predicates.add(equals);

        if (startDate != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get(dateFieldName), startDate));
        }
        if (endDate != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get(dateFieldName), endDate));
        }

        query.where(predicates.toArray(new Predicate[0]));

        if (orderByPropertyName != null && !orderByPropertyName.isEmpty()) {
            query.orderBy(builder.asc(root.get(orderByPropertyName)));
        }

        TypedQuery<T> typedQuery = em.createQuery(query);

        typedQuery.setFirstResult((pageNumber - 1) * pageSize);
        typedQuery.setMaxResults(pageSize);

        List<T> resultList = typedQuery.getResultList();

        long total = getTotalCount(entityClass, entityJoinName, entityId, foreignKeyId);

        return new PageImpl<>(resultList, PageRequest.of(pageNumber - 1, pageSize), total);
    }

    public <T> Page<T> getByTwoEntitiesAndPropertyNameAndEntityIdPage(
            Class<T> entityClass,
            String firstEntityJoinName,
            String secondEntityJoinName,
            String propertySeachName,
            String entityIdNameToSecondJoinName,
            String orderByPropertyName,
            String stringForSearch,
            int idSecondJoin,
            int pageNumber,
            int pageSize
    ) {

        CriteriaBuilder builder = this.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(entityClass);
        Root<T> root = query.from(entityClass);

        Join<T, User> userJoin = root.join(firstEntityJoinName);
        Join<User, Estabelecimento> estabelecimentoJoin = userJoin.join(secondEntityJoinName);

        Path<String> userNamePath = userJoin.get(propertySeachName);
        Path<Long> idEstabelecimentoPath = estabelecimentoJoin.get(entityIdNameToSecondJoinName);

        List<Predicate> predicates = new ArrayList<>();

        Predicate nomeEquals = builder.equal(userNamePath, stringForSearch);
        predicates.add(nomeEquals);

        Predicate estabelecimentoEquals = builder.equal(idEstabelecimentoPath, idSecondJoin);
        predicates.add(estabelecimentoEquals);

        query.where(predicates.toArray(new Predicate[0]));

        if (orderByPropertyName != null && !orderByPropertyName.isEmpty()) {
            query.orderBy(builder.asc(root.get(orderByPropertyName)));
        }

        TypedQuery<T> typedQuery = em.createQuery(query);
        typedQuery.setFirstResult((pageNumber - 1) * pageSize);
        typedQuery.setMaxResults(pageSize);

        List<T> resultList = typedQuery.getResultList();

        long total = getTotalCountTwoEntityByPropertyName(
                entityClass,
                firstEntityJoinName,
                secondEntityJoinName,
                propertySeachName,
                entityIdNameToSecondJoinName,
                stringForSearch,
                idSecondJoin
        );

        return new PageImpl<>(resultList, PageRequest.of(pageNumber - 1, pageSize), total);
    }

    public <T> Page<T> getTwoEntitiesByForeignKeyWithDateAndPaginationPage(
            Class<T> entityClass,
            String firstEntityName,
            String secondEntityName,
            String foreignKeyPropertyName,
            int foreignKeyId,
            String dateFieldName,
            Date startDate,
            Date endDate,
            String orderByPropertyName,
            int pageNumber,
            int pageSize
    ) {
        CriteriaBuilder builder = this.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = builder.createQuery(entityClass);
        Root<T> root = criteriaQuery.from(entityClass);
        criteriaQuery.select(root);

        List<Predicate> predicates = new ArrayList<>();
        Path<Object> foreignKeyPath = root.get(firstEntityName).get(secondEntityName).get(foreignKeyPropertyName);
        predicates.add(builder.equal(foreignKeyPath, foreignKeyId));

        if (startDate != null && endDate != null) {
            Predicate datePredicate = builder.between(root.get(dateFieldName), startDate, endDate);
            predicates.add(datePredicate);
        }

        Predicate combinedPredicate = builder.and(predicates.toArray(new Predicate[0]));
        criteriaQuery.where(combinedPredicate);

        if (orderByPropertyName != null && !orderByPropertyName.isEmpty()) {
            criteriaQuery.orderBy(builder.asc(root.get(orderByPropertyName)));
        }

        TypedQuery<T> typedQuery = em.createQuery(criteriaQuery);
        typedQuery.setFirstResult((pageNumber - 1) * pageSize);
        typedQuery.setMaxResults(pageSize);

        List<T> resultList = typedQuery.getResultList();

        long total = getTotalCountTwoEntity(
                entityClass,
                firstEntityName,
                secondEntityName,
                foreignKeyPropertyName,
                foreignKeyId,
                dateFieldName,
                startDate,
                endDate
        );

        return new PageImpl<>(resultList, PageRequest.of(pageNumber - 1, pageSize), total);
    }


    public <T> List<T> getTwoEntitiesByForeignKeyWithDateAndPagination(
            Class<T> entityClass,
            String firstEntityName,
            String secondEntityName,
            String foreignKeyPropertyName,
            int foreignKeyId,
            String dateFieldName,
            Date startDate,
            Date endDate,
            String orderByPropertyName,
            int pageNumber,
            int pageSize
    ) {
        CriteriaBuilder builder = this.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = builder.createQuery(entityClass);
        Root<T> root = criteriaQuery.from(entityClass);
        criteriaQuery.select(root);

        List<Predicate> predicates = new ArrayList<>();
        Path<Object> foreignKeyPath = root.get(firstEntityName).get(secondEntityName).get(foreignKeyPropertyName);
        predicates.add(builder.equal(foreignKeyPath, foreignKeyId));

        if (startDate != null && endDate != null) {
            Predicate datePredicate = builder.between(root.get(dateFieldName), startDate, endDate);
            predicates.add(datePredicate);
        }

        Predicate combinedPredicate = builder.and(predicates.toArray(new Predicate[0]));
        criteriaQuery.where(combinedPredicate);

        if (orderByPropertyName != null && !orderByPropertyName.isEmpty()) {
            criteriaQuery.orderBy(builder.asc(root.get(orderByPropertyName)));
        }

        TypedQuery<T> typedQuery = em.createQuery(criteriaQuery);
        typedQuery.setFirstResult((pageNumber - 1) * pageSize);
        typedQuery.setMaxResults(pageSize);

        return typedQuery.getResultList();
    }

    public <T, E extends Enum<E>> List<T> getEntitiesByForeignKeyAndWithConditional(
            Class<T> entityClass,
            String entityName,
            String foreignKeyIdName,
            String foreignKeyId,
            String orderByPropertyName,
            String conditionalName,
            E conditional
    ) {
        CriteriaBuilder builder = this.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = builder.createQuery(entityClass);
        Root<T> root = criteriaQuery.from(entityClass);
        criteriaQuery.select(root);

        List<Predicate> predicates = new ArrayList<>();

        ParameterExpression<String> exForeignKeyId = builder.parameter(String.class, foreignKeyIdName);
        predicates.add(builder.equal(root.get(entityName).get(foreignKeyIdName), exForeignKeyId));

        ParameterExpression<Enum> exConditional = builder.parameter(Enum.class, conditionalName);
        predicates.add(builder.equal(root.get(conditionalName), exConditional));

        criteriaQuery.where(predicates.toArray(new Predicate[0]));

        if (orderByPropertyName != null && !orderByPropertyName.isEmpty()) {
            criteriaQuery.orderBy(builder.asc(root.get(orderByPropertyName)));
        }

        TypedQuery<T> query = em.createQuery(criteriaQuery);
        query.setParameter(foreignKeyIdName, foreignKeyId);
        query.setParameter(exConditional, conditional);

        return query.getResultList();
    }

    public <T, E extends Enum<E>> List<T> getEntitiesByForeignKeyAndWithConditionalWithPagination(
            Class<T> entityClass,
            String entityName,
            String foreignKeyIdName,
            String foreignKeyId,
            String orderByPropertyName,
            String conditionalName,
            E conditional,
            int pageNumber,
            int pageSize) {

        CriteriaBuilder builder = this.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = builder.createQuery(entityClass);
        Root<T> root = criteriaQuery.from(entityClass);
        criteriaQuery.select(root);

        List<Predicate> predicates = new ArrayList<>();

        ParameterExpression<String> exForeignKeyId = builder.parameter(String.class, foreignKeyIdName);
        predicates.add(builder.equal(root.get(entityName).get(foreignKeyIdName), exForeignKeyId));

        ParameterExpression<E> exConditional = this.createEnumParameterExpression(builder, root, conditionalName, conditional);
        predicates.add(builder.equal(root.get(conditionalName), exConditional));

        criteriaQuery.where(predicates.toArray(new Predicate[0]));

        if (orderByPropertyName != null && !orderByPropertyName.isEmpty()) {
            criteriaQuery.orderBy(builder.asc(root.get(orderByPropertyName)));
        }

        TypedQuery<T> query = em.createQuery(criteriaQuery);
        query.setParameter(foreignKeyIdName, foreignKeyId);
        query.setParameter(exConditional, conditional);

        query.setFirstResult((pageNumber - 1) * pageSize);
        query.setMaxResults(pageSize);

        return query.getResultList();

//        long total = getTotalCount(entityClass, entityName, foreignKeyIdName, foreignKeyId, conditionalName, conditional);

//        return new PageImpl<>(resultList, PageRequest.of(pageNumber - 1, pageSize), total);
    }

    public <T, E extends Enum<E>> Map<String, Long> getCountByStatusForEstablishment(
            Class<T> entityClass,
            String statusFieldName,
            String firstEntityName,
            String secondEntityName,
            String foreignKeyIdName,
            int foreygnKeyId
    ) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Object[]> query = builder.createQuery(Object[].class);
        Root<T> root = query.from(entityClass);

        Expression<Long> count = builder.count(root);
        Expression<String> status = root.get(statusFieldName).as(String.class);

        // Adiciona a condição para filtrar pelo ID do estabelecimento
        Predicate establishmentCondition = builder.equal(root.get(firstEntityName).get(secondEntityName).get(foreignKeyIdName), foreygnKeyId);

        query.multiselect(status, count);
        query.where(establishmentCondition);
        query.groupBy(status);

        List<Object[]> results = em.createQuery(query).getResultList();

        Map<String, Long> statusCounts = new HashMap<>();
        for (Object[] result : results) {
            statusCounts.put((String) result[0], (Long) result[1]);
        }

        return statusCounts;
    }

    public <T, E extends Enum<E>> long getTotalCount(
            Class<T> entityClass,
            String entityName,
            String foreignKeyIdName,
            String foreignKeyId,
            String conditionalName,
            E conditional
    ) {
        CriteriaBuilder builder = this.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<T> root = countQuery.from(entityClass);
        countQuery.select(builder.count(root));

        List<Predicate> countPredicates = new ArrayList<>();
        ParameterExpression<String> exForeignKeyId = builder.parameter(String.class, foreignKeyIdName);
        countPredicates.add(builder.equal(root.get(entityName).get(foreignKeyIdName), exForeignKeyId));

        ParameterExpression<E> exConditional = this.createEnumParameterExpression(builder, root, conditionalName, conditional);
        countPredicates.add(builder.equal(root.get(conditionalName), exConditional));

        countQuery.where(countPredicates.toArray(new Predicate[0]));

        TypedQuery<Long> countTypedQuery = em.createQuery(countQuery);
        countTypedQuery.setParameter(foreignKeyIdName, foreignKeyId);
        countTypedQuery.setParameter(exConditional, conditional);

        return countTypedQuery.getSingleResult();
    }

    public <T> long getTotalCount(
            Class<T> entityClass,
            String entityName,
            String foreignKeyIdName,
            String foreignKeyId
    ) {
        CriteriaBuilder builder = this.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<T> root = countQuery.from(entityClass);
        countQuery.select(builder.count(root));

        List<Predicate> countPredicates = new ArrayList<>();
        ParameterExpression<String> exForeignKeyId = builder.parameter(String.class, foreignKeyIdName);
        countPredicates.add(builder.equal(root.get(entityName).get(foreignKeyIdName), exForeignKeyId));


        countQuery.where(countPredicates.toArray(new Predicate[0]));

        TypedQuery<Long> countTypedQuery = em.createQuery(countQuery);
        countTypedQuery.setParameter(foreignKeyIdName, foreignKeyId);

        return countTypedQuery.getSingleResult();
    }

    public <T> long getTotalCount(
            Class<T> entityClass,
            String entityName,
            String foreignKeyIdName,
            int foreignKeyId
    ) {

        CriteriaBuilder builder = this.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<T> root = countQuery.from(entityClass);
        countQuery.select(builder.count(root));

        List<Predicate> countPredicates = new ArrayList<>();
        ParameterExpression<Integer> exForeignKeyId = builder.parameter(Integer.class, foreignKeyIdName);
        countPredicates.add(builder.equal(root.get(entityName).get(foreignKeyIdName), exForeignKeyId));


        countQuery.where(countPredicates.toArray(new Predicate[0]));

        TypedQuery<Long> countTypedQuery = em.createQuery(countQuery);
        countTypedQuery.setParameter(foreignKeyIdName, foreignKeyId);

        return countTypedQuery.getSingleResult();
    }

    public <T> long getTotalCountTwoEntity(
            Class<T> entityClass,
            String firstEntityName,
            String secondEntityName,
            String foreignKeyPropertyName,
            int foreignKeyId,
            String dateFieldName,
            Date startDate,
            Date endDate
    ) {

        CriteriaBuilder builder = this.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<T> root = countQuery.from(entityClass);
        countQuery.select(builder.count(root));

        // Criando expressões para comparação de ID e datas
        Predicate foreignKeyIdPredicate = builder.equal(root.get(firstEntityName).get(secondEntityName).get(foreignKeyPropertyName), foreignKeyId);
        Predicate datePredicate = builder.between(root.get(dateFieldName), startDate, endDate);

        // Combinando as expressões com um 'AND'
        Predicate combinedPredicate = builder.and(foreignKeyIdPredicate, datePredicate);
        countQuery.where(combinedPredicate);

        TypedQuery<Long> countTypedQuery = em.createQuery(countQuery);

        return countTypedQuery.getSingleResult();
    }

    public <T> long getTotalCountTwoEntityByPropertyName(
            Class<T> entityClass,
            String firstEntityJoinName,
            String secondEntityJoinName,
            String propertySeachName,
            String entityIdNameToSecondJoinName,
            String stringForSearch,
            int idSecondJoin
    ) {

        CriteriaBuilder countBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = countBuilder.createQuery(Long.class);
        Root<T> countRoot = countQuery.from(entityClass);

        Join<T, User> countUserJoin = countRoot.join(firstEntityJoinName);
        Join<User, Estabelecimento> countEstabelecimentoJoin = countUserJoin.join(secondEntityJoinName);

        Path<String> countUserNamePath = countUserJoin.get(propertySeachName);
        Path<Long> countIdEstabelecimentoPath = countEstabelecimentoJoin.get(entityIdNameToSecondJoinName);

        List<Predicate> countPredicates = new ArrayList<>();
        Predicate countNomeEquals = countBuilder.equal(countUserNamePath, stringForSearch);
        countPredicates.add(countNomeEquals);

        Predicate countEstabelecimentoEquals = countBuilder.equal(countIdEstabelecimentoPath, idSecondJoin);
        countPredicates.add(countEstabelecimentoEquals);

        countQuery.select(countBuilder.count(countRoot)).where(countPredicates.toArray(new Predicate[0]));

        TypedQuery<Long> countTypedQuery = em.createQuery(countQuery);
        return countTypedQuery.getSingleResult();
    }

    public <T> List<T> getByTwoEntitiesAndPropertyNameAndEntityId(
            Class<T> entityClass,
            String firstEntityJoinName,
            String secondEntityJoinName,
            String propertySeachName,
            String entityIdNameToSecondJoinName,
            String orderByPropertyName,
            String stringForSearch,
            int idSecondJoin) {

        CriteriaBuilder builder = this.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(entityClass);
        Root<T> root = query.from(entityClass);

        Join<T, User> userJoin = root.join(firstEntityJoinName);
        Join<User, Estabelecimento> estabelecimentoJoin = userJoin.join(secondEntityJoinName);

        Path<String> userNamePath = userJoin.get(propertySeachName);
        Path<Long> idEstabelecimentoPath = estabelecimentoJoin.get(entityIdNameToSecondJoinName);

        List<Predicate> predicates = new ArrayList<>();

        Predicate nomeEquals = builder.equal(userNamePath, stringForSearch);
        predicates.add(nomeEquals);

        Predicate estabelecimentoEquals = builder.equal(idEstabelecimentoPath, idSecondJoin);
        predicates.add(estabelecimentoEquals);

        query.where(predicates.toArray(new Predicate[0]));

        if (orderByPropertyName != null && !orderByPropertyName.isEmpty()) {
            query.orderBy(builder.asc(root.get(orderByPropertyName)));
        }

        TypedQuery<T> typedQuery = em.createQuery(query);

        return typedQuery.getResultList();
    }

    @Transactional(propagation= Propagation.REQUIRED, isolation= Isolation.SERIALIZABLE, readOnly = false)
    public void save(Object objeto) {
        em.persist(objeto);
        em.flush();
    }

    @Transactional(propagation= Propagation.REQUIRED, isolation= Isolation.SERIALIZABLE , readOnly = false)
    public Object saveWithReturn(Object objeto) {
        em.persist(objeto);
        em.flush();
        return objeto;
    }
    @Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.SERIALIZABLE, readOnly = false)
    public void delete(Object objeto) {
        objeto = em.merge(objeto);
        em.remove(objeto);
    }
    @Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.SERIALIZABLE, readOnly = false)
    public void update(Object objeto) {
        em.merge(objeto);
    }

    @Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.SERIALIZABLE, readOnly = false)
    public Object updateWithReturn(Object objeto) {
        em.merge(objeto);
        return objeto;
    }

    public void batchUpdate(Collection<?> objects)  {
        for (Object objeto : objects) this.update(objeto);
    }
}
