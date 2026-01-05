package com.addiction.challenge.missionhistory.service;

import com.addiction.challenge.missionhistory.entity.MissionHistory;

import java.util.List;

public interface MissionHistoryService {

    List<MissionHistory> saveAll(List<MissionHistory> missionHistories);

}
