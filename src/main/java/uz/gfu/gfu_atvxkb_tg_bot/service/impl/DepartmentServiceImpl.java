package uz.gfu.gfu_atvxkb_tg_bot.service.impl;

import org.springframework.stereotype.Service;
import uz.gfu.gfu_atvxkb_tg_bot.dto.DepartmentDto;
import uz.gfu.gfu_atvxkb_tg_bot.repository.DepartmentRepository;
import uz.gfu.gfu_atvxkb_tg_bot.service.DepartmentService;
@Service
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public String createDepartment(DepartmentDto dto) {

        return null;
    }
}
