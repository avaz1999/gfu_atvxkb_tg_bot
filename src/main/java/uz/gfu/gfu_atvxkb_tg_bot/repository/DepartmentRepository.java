package uz.gfu.gfu_atvxkb_tg_bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.Department;

public interface DepartmentRepository extends JpaRepository<Department,Long> {
    Department findByIdAndDeletedFalse(Long departmentId);

}
