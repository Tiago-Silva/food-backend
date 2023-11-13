package br.com.food.repository;

import br.com.food.enuns.UserType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Repository
public abstract class GenericRepository {

    @PersistenceContext
    private EntityManager em;

    private CriteriaBuilder getCriteriaBuilder() {
        return em.getCriteriaBuilder();
    }

    /**
     *Metodo pode ser chamado: getQuery(Aluno.class).getSingleResult() ou getQuery(Aluno.class).getResultList()
     * @param classe exemplo: Aluno.class
     * @return getSingleResult() ou getResultList()
     * @param <T>
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
     * @param classe
     * @param orderByPropertyName
     * @return getSingleResult() ou getResultList()
     * @param <T>
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

    /**
     * Retorna uma entidade pelo id
     * @param entityClass
     * @param id
     * @return
     * @param <T>
     */
    public <T> T getEntityById(Class<T> entityClass, int id) {
        return this.em.find(entityClass, id);
    }

    public <T> T getEntityById(Class<T> entityClass, String id) {
        return this.em.find(entityClass, id);
    }

    public <T> T getEntityById(Class<T> entityClass, Long id) {
        return this.em.find(entityClass, id);
    }

    public <T> T getEntityByProperty(Class<T> entityClass, String property) {
        return this.em.find(entityClass, property);
    }

    public <T> T getEntityByProperty(Class<T> classe, String propertySeachName, String property) {
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
    public <T> List<T> getEntitiesByForeignKey(Class<T> entityClass, String entityName, String entityId,
                                               String foreignKeyPropertyName,
                                               int foreignKeyId, String orderByPropertyName) {
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
    public <T, E extends Enum<E>> List<T> getEntitiesByForeignKeyAndWithConditional(Class<T> entityClass,
                                                    String entityName,
                                                    String foreignKeyIdName,
                                                    int foreignKeyId,
                                                    String orderByPropertyName,
                                                    String conditionalName,
                                                    E conditional) {
        CriteriaBuilder builder = this.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = builder.createQuery(entityClass);
        Root<T> root = criteriaQuery.from(entityClass);
        criteriaQuery.select(root);

        List<Predicate> predicates = new ArrayList<>();

        ParameterExpression<Integer> exForeignKeyId = builder.parameter(Integer.class, foreignKeyIdName);
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

    /**
     * Metodo que irá fazer o join em duas tabelas para traz
     * @param entityClass
     * @param firstEntityName
     * @param secondEntityName
     * @param foreignKeyPropertyName
     * @param foreignKeyId
     * @param orderByPropertyName
     * @return
     * @param <T>
     */
    public <T, E extends Enum<E>> List<T> getTwoEntitiesByForeignKeyAndWithConditional(Class<T> entityClass,
                                                            String firstEntityName,
                                                            String secondEntityName,
                                                            String foreignKeyPropertyName,
                                                            int foreignKeyId,
                                                            String orderByPropertyName,
                                                            String conditionalName,
                                                            E conditional) {
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

    public <T> List<T> getTwoEntitiesByForeignKey(Class<T> entityClass, String firstEntityName,
                                                  String secondEntityName, String foreignKeyPropertyName,
                                                  int foreignKeyId, String orderByPropertyName) {
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
     * @param entityClass
     * @param entityJoinName
     * @param entityId
     * @param foreignKeyId
     * @param orderByPropertyName
     * @return
     * @param <T>
     */
    public <T> List<T> getJoinColumn(Class<T> entityClass, String entityJoinName, String entityId,
                                     int foreignKeyId, String orderByPropertyName) {
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

    public <T> List<T> getJoinColumn(Class<T> entityClass, String entityJoinName, String entityId,
                                     String foreignKeyId, String orderByPropertyName) {
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

    public <T, E extends Enum<E>> List<T> getEntitiesByForeignKeyAndWithConditional(Class<T> entityClass,
                                                                                    String entityName,
                                                                                    String foreignKeyIdName,
                                                                                    String foreignKeyId,
                                                                                    String orderByPropertyName,
                                                                                    String conditionalName,
                                                                                    E conditional) {
        CriteriaBuilder builder = this.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = builder.createQuery(entityClass);
        Root<T> root = criteriaQuery.from(entityClass);
        criteriaQuery.select(root);

        List<Predicate> predicates = new ArrayList<>();

        ParameterExpression<Integer> exForeignKeyId = builder.parameter(Integer.class, foreignKeyIdName);
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

    @Transactional(propagation= Propagation.REQUIRED, isolation= Isolation.SERIALIZABLE)
    public void save(Object objeto) {
        em.persist(objeto);
        em.flush();
    }

    @Transactional(propagation= Propagation.REQUIRED, isolation= Isolation.SERIALIZABLE)
    public Object saveWithReturn(Object objeto) {
        em.persist(objeto);
        em.flush();
        return objeto;
    }
    @Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.SERIALIZABLE)
    public void delete(Object objeto) {
        objeto = em.merge(objeto);
        em.remove(objeto);
    }
    @Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.SERIALIZABLE)
    public void update(Object objeto) {
        em.merge(objeto);
    }

    @Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.SERIALIZABLE)
    public Object updateWithReturn(Object objeto) {
        em.merge(objeto);
        return objeto;
    }

    public void batchUpdate(Collection<?> objects)  {
        for (Object objeto : objects) this.update(objeto);
    }
}
