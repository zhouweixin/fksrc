package com.hnu.fk.service;

import com.hnu.fk.repository.StandingBookDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 台账调度管理表头Service
 */
@Service
public class StandingBookHeadService {
    @Autowired
    private StandingBookDetailRepository standingBookDetailRepository;

}
