package uz.gfu.gfu_atvxkb_tg_bot.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import uz.gfu.gfu_atvxkb_tg_bot.dto.BuildingDto;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.BotUser;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.Building;

import java.util.List;

public interface BuildingService {
    void saveBuilding(String text, Long userId);

    void saveRoomNumber(String number);

    List<Building> getAllBuildings();

    String getDtoBuildings(BotUser superAdmin);

    boolean checkBuilding(String text);

    void createNewBuilding(String text, BotUser superAdmin, SendMessage sendMessage);

    List<Building> findAllBuildingsByName(String getBuildings);
}
