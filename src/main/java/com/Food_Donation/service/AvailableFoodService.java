package com.Food_Donation.service;

import com.Food_Donation.dto.AvailableFoodDTO;
import com.Food_Donation.dto.FoodImageDTO;
import com.Food_Donation.entity.AvailableFood;
import com.Food_Donation.entity.FoodImage;
import com.Food_Donation.enums.FoodStatus;
import com.Food_Donation.exception.DuplicateResourceException;
import com.Food_Donation.exception.ResourceNotFoundException;
import com.Food_Donation.repository.AvailableFoodRepository;
import com.Food_Donation.repository.FoodImageRepository;
import com.Food_Donation.utils.DataMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class AvailableFoodService {

    private final AvailableFoodRepository availableFoodRepository;
    private final FoodImageRepository foodImageRepository;
    private final S3Service s3Service;
    private final DataMapper dataMapper;


    public AvailableFoodDTO create(AvailableFoodDTO availableFoodDTO, List<MultipartFile> imageDto, Long userId) {


        if (availableFoodRepository.existsByFoodName(availableFoodDTO.getFoodName()))
        {
            throw new DuplicateResourceException("Food Name "+ availableFoodDTO.getFoodName()+" already exist");
        }
        AvailableFood availableFood = dataMapper.DtoToModel(availableFoodDTO);
        availableFood.setStatus(FoodStatus.AVAILABLE);
        availableFood.setUserId(userId);
        availableFood.setCreatedAt(LocalDateTime.now());
        availableFood.setActive(true);
        availableFood.setCreatedAt(LocalDateTime.now());

        List<FoodImage> imageList = imageDto.stream()
                .map(file ->{
                    String imageKey = s3Service.uploadImage(file);

                    FoodImage foodImage = new FoodImage();
                    foodImage.setImageKey(imageKey);
                    foodImage.setImageName(file.getOriginalFilename());
                    foodImage.setImageType(file.getContentType());
                    foodImage.setAvailableFood(availableFood);

                    return foodImage;

                }).toList();
        availableFood.setFoodImageList(imageList);
        AvailableFood savedFood = availableFoodRepository.save(availableFood);

        AvailableFoodDTO responseDto = dataMapper.ModelToDto(savedFood);

        if (responseDto.getFoodImageDTOList() != null) {
            responseDto.getFoodImageDTOList().forEach(img -> {
                img.setFoodId(savedFood.getId());
                img.setImageUrl(s3Service.getImageUrl(img.getImageKey()));
            });
        }

        return responseDto;
    }

//    @Transactional
//    public AvailableFoodDTO update(Long id, AvailableFoodDTO dto, List<MultipartFile> imageDto) {
//
//    AvailableFood food = availableFoodRepository.findById(id)
//            .orElseThrow(() -> new ResourceNotFoundException("Food not found"));
//
//    // MapStruct updates existing entity
//    dataMapper.updateFoodFromDto(dto, food);
//
//        List<Long> imageIdsToKeep;
//
//        if (dto.getFoodImageDTOList() != null) {
//            imageIdsToKeep = dto.getFoodImageDTOList().stream()
//                    .map(FoodImageDTO::getId)
//                    .toList();
//        } else {
//            imageIdsToKeep = new ArrayList<>();
//        }
//
//        List<FoodImage> imagesToDelete = food.getFoodImageList().stream()
//                .filter(img -> !imageIdsToKeep.contains(img.getId()))
//                .toList();
//
//        imagesToDelete.forEach(img -> {
//            s3Service.deleteImage(img.getImageKey());
//            food.getFoodImageList().remove(img);
//        });
//    // Add new images
//    if (imageDto != null && !imageDto.isEmpty()) {
//        List<FoodImage> newImageEntities = imageDto.stream()
//                .map(file -> {
//                    String key = s3Service.uploadImage(file);
//                    FoodImage img = new FoodImage();
//
//                    img.setAvailableFood(food);
//                    img.setImageKey(key);
//                    img.setImageName(file.getOriginalFilename());
//                    img.setImageType(file.getContentType());
//                    return img;
//                })
//                .toList();
//
//        food.getFoodImageList().addAll(newImageEntities);
//    }
//
//    AvailableFood updated = availableFoodRepository.save(food);
//
//    AvailableFoodDTO response = dataMapper.ModelToDto(updated);
//
//    if (response.getFoodImageDTOList() != null) {
//        response.getFoodImageDTOList().forEach(img -> {
//            img.setFoodId(updated.getId());
//            img.setImageUrl(s3Service.getImageUrl(img.getImageKey()));
//        });
//    }
//    return response;
//
//}

    //first main method
    @Transactional
    public AvailableFoodDTO update(Long id, AvailableFoodDTO dto, List<MultipartFile> imageDto) {

        AvailableFood food = fetchFoodOrThrow(id);

        updateParent(food, dto);

        syncFoodImages(food, dto, imageDto);

        AvailableFood updatedFood = availableFoodRepository.save(food);

        return buildResponse(updatedFood);
    }

    private AvailableFood fetchFoodOrThrow(Long id) {
        return availableFoodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Food not found with id: " + id));
    }
    private void updateParent(AvailableFood food, AvailableFoodDTO dto) {
        dataMapper.updateFoodFromDto(dto, food);
    }

    //second main method
    private void syncFoodImages(AvailableFood food, AvailableFoodDTO dto, List<MultipartFile> imageDto) {

        List<Long> imageIdsToKeep = getImageIdsToKeep(dto);

        deleteRemovedImages(food, imageIdsToKeep);

        addNewImages(food, imageDto);
    }
    private List<Long> getImageIdsToKeep(AvailableFoodDTO dto) {

        if (dto.getFoodImageDTOList() == null) {
            return new ArrayList<>();
        }

        return dto.getFoodImageDTOList().stream()
                .map(FoodImageDTO::getId)
                .toList();
    }
    private void deleteRemovedImages(AvailableFood food, List<Long> imageIdsToKeep) {

        List<FoodImage> imagesToDelete = food.getFoodImageList().stream()
                .filter(img -> !imageIdsToKeep.contains(img.getId()))
                .toList();

        imagesToDelete.forEach(img -> s3Service.deleteImage(img.getImageKey()));

        food.getFoodImageList().removeAll(imagesToDelete);
    }
    private void addNewImages(AvailableFood food, List<MultipartFile> imageDto) {

        if (imageDto == null || imageDto.isEmpty()) {
            return;
        }

        List<FoodImage> newImageEntities = imageDto.stream()
                .map(file -> {
                    String key = s3Service.uploadImage(file);

                    FoodImage img = new FoodImage();
                    img.setAvailableFood(food);
                    img.setImageKey(key);
                    img.setImageName(file.getOriginalFilename());
                    img.setImageType(file.getContentType());

                    return img;
                })
                .toList();

        food.getFoodImageList().addAll(newImageEntities);
    }
    // final for response
    private AvailableFoodDTO buildResponse(AvailableFood food) {

        food.setUpdatedAt(LocalDateTime.now());
        AvailableFoodDTO response = dataMapper.ModelToDto(food);

        if (response.getFoodImageDTOList() != null) {
            response.getFoodImageDTOList().forEach(img -> {
                img.setFoodId(food.getId());
                img.setImageUrl(s3Service.getImageUrl(img.getImageKey()));
            });
        }
        return response;
    }

    public AvailableFoodDTO stockStatus(Long id, AvailableFoodDTO availableFoodDTO) {

        AvailableFood availableFood = availableFoodRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Food not available"));

        availableFood.setActive(availableFoodDTO.isActive());

        availableFood= availableFoodRepository.save(availableFood);

         AvailableFoodDTO dto = dataMapper.ModelToDto(availableFood);

         return dto;

    }
}
