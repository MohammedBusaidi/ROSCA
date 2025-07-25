package com.example.RoscaApp.service;

import com.example.RoscaApp.dto.RoscaDTO;
import com.example.RoscaApp.repository.RoscaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RoscaService {

    @Autowired
    private RoscaRepository roscaRepo;


    public RoscaDTO createRosca(UUID creatorId, String title, int contributionAmount) {
    }

}
