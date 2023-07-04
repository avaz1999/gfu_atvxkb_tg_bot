package uz.gfu.gfu_atvxkb_tg_bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.Building;

public interface BuildingRepository extends JpaRepository<Building, Long> {
    Building findByName(String name);
    boolean existsByName(String name);
}
