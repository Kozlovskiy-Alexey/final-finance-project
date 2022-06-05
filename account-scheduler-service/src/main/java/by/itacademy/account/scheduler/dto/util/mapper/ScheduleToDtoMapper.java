package by.itacademy.account.scheduler.dto.util.mapper;


import by.itacademy.account.scheduler.dto.ScheduleDto;
import by.itacademy.account.scheduler.dto.util.mapper.api.IRepositoryMapper;
import by.itacademy.account.scheduler.repository.entity.Schedule;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class ScheduleToDtoMapper implements IRepositoryMapper<Schedule, ScheduleDto> {


    @Override
    public ScheduleDto entityToDto(Schedule entity) {
        return null;
    }

    @Override
    public Schedule dtoToEntity(ScheduleDto dto) {
        return null;
    }
}
