package co.com.bancolombia.logfunctionalvnt.log.commons;

public interface SimpleMapper<E, D> {
    D toData(E entity);
    E toEntity(D data);
}
