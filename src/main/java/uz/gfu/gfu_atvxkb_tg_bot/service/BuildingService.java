package uz.gfu.gfu_atvxkb_tg_bot.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;
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

    void getBuildingByName(BotUser superAdmin, SendMessage sendMessage, String data, AbsSender sender);

    void editBuilding(String text, BotUser superAdmin, AbsSender sender, SendMessage sendMessage);

}
