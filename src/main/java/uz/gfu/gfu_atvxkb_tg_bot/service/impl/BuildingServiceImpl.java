package uz.gfu.gfu_atvxkb_tg_bot.service.impl;

import org.springframework.stereotype.Service;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.BotUser;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.Building;
import uz.gfu.gfu_atvxkb_tg_bot.repository.BuildingRepository;
import uz.gfu.gfu_atvxkb_tg_bot.repository.UserRepository;
import uz.gfu.gfu_atvxkb_tg_bot.service.BuildingService;

import java.util.List;
import java.util.Optional;

@Service
public class BuildingServiceImpl implements BuildingService {
    private final BuildingRepository buildingRepository;
    private final UserRepository userRepository;

    public BuildingServiceImpl(BuildingRepository buildingRepository, UserRepository userRepository) {
        this.buildingRepository = buildingRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void saveBuilding(String text,Long userId) {
        Optional<BotUser> byId = userRepository.findById(userId);
        if (byId.isPresent()) {
            Building byNameBlock = buildingRepository.findByName(text);
            BotUser user = byId.get();
            user.setBuilding(byNameBlock);
            userRepository.save(user);
        }
    }

    @Override
    public void saveRoomNumber(String number) {

    }

    @Override
    public List<Building> getAllBuildings() {
        return buildingRepository.findAll();
    }

    @Override
    public boolean  checkBuilding(String text) {
        return buildingRepository.existsByName(text);
    }
}
