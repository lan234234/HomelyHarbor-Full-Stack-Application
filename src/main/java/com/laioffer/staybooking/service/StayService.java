package com.laioffer.staybooking.service;

import com.laioffer.staybooking.exception.StayNotExistException;
import com.laioffer.staybooking.model.Stay;
import com.laioffer.staybooking.model.StayImage;
import com.laioffer.staybooking.model.User;
import com.laioffer.staybooking.repository.StayRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class StayService {

    private final ImageStorageService imageStorageService;
    private final StayRepository stayRepository;


    public StayService(ImageStorageService imageStorageService, StayRepository stayRepository) {
        this.imageStorageService = imageStorageService;
        this.stayRepository = stayRepository;
    }

    public List<Stay> listByUser(String username) {
        return stayRepository.findByHost(new User.Builder().setUsername(username).build());
    }

    public Stay findByIdAndHost(Long stayId, String username) throws StayNotExistException {
        Stay stay = stayRepository.findByIdAndHost(stayId, new User.Builder().setUsername(username).build());
        if (stay == null) {
            throw new StayNotExistException("Stay doesn't exist");
        }
        return stay;
    }


    public void add(Stay stay, MultipartFile[] images) {
        List<StayImage> stayImages = Arrays.stream(images)
                .filter(image -> !image.isEmpty())
                .parallel()
                .map(imageStorageService::save)
                .map(mediaLink -> new StayImage(mediaLink, stay))
                .collect(Collectors.toList());
        stay.setImages(stayImages);
        stayRepository.save(stay);
    }

    public void delete(Long stayId, String username) throws StayNotExistException {
        Stay stay = stayRepository.findByIdAndHost(stayId, new User.Builder().setUsername(username).build());
        if (stay == null) {
            throw new StayNotExistException("Stay doesn't exist");
        }
        stayRepository.deleteById(stayId);
    }
}