package uz.gfu.gfu_atvxkb_tg_bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.Building;

import java.util.List;

public interface BuildingRepository extends JpaRepository<Building, Long> {
    Building findByNameAndDeletedFalse(String name);
    Building findByIdAndDeletedFalse(Long id);
    boolean existsByNameAndDeletedFalse(String name);
    List<Building> findAllByDeletedFalse();
}
