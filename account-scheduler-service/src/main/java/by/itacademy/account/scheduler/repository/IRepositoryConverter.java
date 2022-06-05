package by.itacademy.account.scheduler.repository;

public interface IRepositoryConverter<E, D> {
    D entityToDto(E entity);
    E dtoToEntity(D dto);
}
