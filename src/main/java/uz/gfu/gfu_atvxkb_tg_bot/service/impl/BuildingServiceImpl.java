package uz.gfu.gfu_atvxkb_tg_bot.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import uz.gfu.gfu_atvxkb_tg_bot.constant.BotQuery;
import uz.gfu.gfu_atvxkb_tg_bot.dto.BuildingDto;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.BotUser;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.Building;
import uz.gfu.gfu_atvxkb_tg_bot.enums.UserState;
import uz.gfu.gfu_atvxkb_tg_bot.payload.ResMessageRu;
import uz.gfu.gfu_atvxkb_tg_bot.payload.ResMessageUz;
import uz.gfu.gfu_atvxkb_tg_bot.repository.BuildingRepository;
import uz.gfu.gfu_atvxkb_tg_bot.repository.UserRepository;
import uz.gfu.gfu_atvxkb_tg_bot.service.BuildingService;
import uz.gfu.gfu_atvxkb_tg_bot.service.GeneralService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BuildingServiceImpl implements BuildingService {
    private final BuildingRepository buildingRepository;
    private final UserRepository userRepository;
    @Autowired @Lazy
    GeneralService generalService;

    public BuildingServiceImpl(BuildingRepository buildingRepository, UserRepository userRepository) {
        this.buildingRepository = buildingRepository;
        this.userRepository = userRepository;

    }

    @Override
    public void saveBuilding(String text,Long userId) {
        Optional<BotUser> byId = userRepository.findById(userId);
        if (byId.isPresent()) {
            Building byNameBlock = buildingRepository.findByNameAndDeletedFalse(text);
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
    public String getDtoBuildings(BotUser superAdmin) {
        StringBuilder sb = getBuildingDto(superAdmin);
        return sb.toString();
    }

    private StringBuilder getBuildingDto(BotUser superAdmin) {
        List<Building> allBuildings = buildingRepository.findAllByDeletedFalse();
        List<BuildingDto> shareBuildings = new ArrayList<>();
        for (Building building : allBuildings) {
            BuildingDto dto = new BuildingDto();
            dto.setName(building.getName());
            shareBuildings.add(dto);
        }
        StringBuilder sb = new StringBuilder();
        short i = 1;
        for (BuildingDto building : shareBuildings) {
            sb.append(superAdmin.getLanguage().equals(BotQuery.UZ_SELECT) ? "<b>" + i + " \uD83C\uDFE2 Bino: </b>" : "<b>Здания: </b>").append(building.getName()).append("\n");
            i++;
        }
        return sb;
    }

    @Override
    public boolean  checkBuilding(String text) {
        return buildingRepository.existsByNameAndDeletedFalse(text);
    }

    @Override
    @Transactional
    public void createNewBuilding(String text, BotUser superAdmin, SendMessage sendMessage) {
        if (buildingRepository.existsByNameAndDeletedFalse(text)) {
            if (superAdmin.getLanguage().equals(BotQuery.UZ_SELECT))sendMessage.setText(ResMessageUz.EXIST_BUILDING_NAME);
            else if (superAdmin.getLanguage().equals(BotQuery.RU_SELECT)) sendMessage.setText(ResMessageRu.EXIST_BUILDING_NAME);
        }
        else {
            buildingRepository.save(new Building(text));
            superAdmin.setState(UserState.SUPER_ADMIN_BUILDING);
            userRepository.save(superAdmin);
            if (superAdmin.getLanguage().equals(BotQuery.UZ_SELECT))sendMessage.setText(ResMessageUz.BUILDING_CRUD);
            else if (superAdmin.getLanguage().equals(BotQuery.RU_SELECT)) sendMessage.setText(ResMessageRu.BUILDING_CRUD);
            sendMessage.setReplyMarkup(generalService.crudBuilding());
            StringBuilder sb = getBuildingDto(superAdmin);
            if (superAdmin.getLanguage().equals(BotQuery.UZ_SELECT))sendMessage.setText(ResMessageUz.SUCCESS_ADD_BUILDING + sb);
            else if (superAdmin.getLanguage().equals(BotQuery.RU_SELECT)) sendMessage.setText(ResMessageRu.SUCCESS_ADD_BUILDING +sb);
        }
    }

    @Override
    public List<Building> findAllBuildingsByName(String getBuildings) {
        return buildingRepository.findAll();
    }




}
