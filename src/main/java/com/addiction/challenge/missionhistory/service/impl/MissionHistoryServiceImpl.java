package com.addiction.challenge.missionhistory.service.impl;

import com.addiction.challenge.missionhistory.entity.MissionHistory;
import com.addiction.challenge.missionhistory.repository.MissionHistoryRepository;
import com.addiction.challenge.missionhistory.service.MissionHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MissionHistoryServiceImpl implements MissionHistoryService {

    private final MissionHistoryRepository missionHistoryRepository;

    @Override
    public List<MissionHistory> saveAll(List<MissionHistory> missionHistories) {
        return missionHistoryRepository.saveAll(missionHistories);
    }
}
