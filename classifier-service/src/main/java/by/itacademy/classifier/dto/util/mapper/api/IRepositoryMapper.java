package by.itacademy.classifier.dto.util.mapper.api;

public interface IRepositoryMapper<E, D> {
    D entityToDto(E entity);
    E dtoToEntity(D dto);
}
