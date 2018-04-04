package com.ecm.dao;

import com.ecm.model.EvidenceFactLink;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by deng on 2018/4/3.
 */
public interface EvidenceFactLinkDao extends JpaRepository<EvidenceFactLink, Integer> {
    EvidenceFactLink save(EvidenceFactLink evidenceFactLink);

    EvidenceFactLink findByInitEviNodeIDAndFactNodeID(int initEviNodeID, int factNodeID);

    void deleteByInitEviNodeIDAndFactNodeID(int initEviNodeID, int factNodeID);
}
