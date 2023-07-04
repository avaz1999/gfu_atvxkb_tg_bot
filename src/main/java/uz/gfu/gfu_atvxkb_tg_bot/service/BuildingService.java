package uz.gfu.gfu_atvxkb_tg_bot.service;

import uz.gfu.gfu_atvxkb_tg_bot.entitiy.Building;

import java.util.List;

public interface BuildingService {
    void saveBuilding(String text,Long userId);
    void saveRoomNumber(String number);

    List<Building> getAllBuildings();

    boolean checkBuilding(String text);

}
