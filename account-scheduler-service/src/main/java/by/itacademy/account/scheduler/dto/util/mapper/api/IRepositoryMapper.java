package by.itacademy.account.scheduler.dto.util.mapper.api;

public interface IRepositoryMapper<E, D> {
    D entityToDto(E entity);
    E dtoToEntity(D dto);
}
