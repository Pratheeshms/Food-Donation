package com.Food_Donation.service;

import com.Food_Donation.dto.AvailableFoodDTO;
import com.Food_Donation.dto.AvailableFoodPatchDTO;
import com.Food_Donation.dto.FoodImageDTO;
import com.Food_Donation.dto.PaginationResponse;
import com.Food_Donation.entity.AvailableFood;
import com.Food_Donation.entity.FoodImage;
import com.Food_Donation.enums.FoodStatus;
import com.Food_Donation.exception.DuplicateResourceException;
import com.Food_Donation.exception.ResourceNotFoundException;
import com.Food_Donation.repository.AvailableFoodRepository;
import com.Food_Donation.repository.FoodImageRepository;
import com.Food_Donation.utils.DataMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
            responseDto.getFoodImageDTOList()
                    .forEach(img -> {
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

    public AvailableFoodPatchDTO stockStatus(Long id, AvailableFoodPatchDTO dto) {

        AvailableFood food = availableFoodRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Food not available"));

        if (dto.getIsActive() != null) {
            food.setActive(dto.isActive());
        }
        if (dto.getQuantity() != null) {
            BigDecimal newQuantity = food.getQuantity().add(dto.getQuantity());

            if (newQuantity.compareTo(BigDecimal.ZERO) < 0) {
                throw new RuntimeException("Quantity cannot be negative");
            }
            food.setQuantity(newQuantity);

        }
        if (dto.getExpiryTime() != null) {
            food.setExpiryTime(dto.getExpiryTime());
        }

        LocalDateTime now = LocalDateTime.now();

        if (food.getExpiryTime() != null && food.getExpiryTime().isBefore(now)) {
            food.setStatus(FoodStatus.EXPIRED);
        }
        else if (food.getQuantity() != null && food.getQuantity().compareTo(BigDecimal.ZERO) == 0) {
            food.setStatus(FoodStatus.OUT_OF_STOCK);
        }
        else {
            food.setStatus(FoodStatus.AVAILABLE);
        }

        food= availableFoodRepository.save(food);
        return dataMapper.ModelsToDto(food);
    }

    public AvailableFoodDTO findById(Long id) {

        AvailableFood availableFood = availableFoodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Food not found with id: " + id));

        List<FoodImageDTO> imageDTOList = new ArrayList<>();

        for (FoodImage image : availableFood.getFoodImageList()) {

            String url = s3Service.getImageUrl(image.getImageKey());

            FoodImageDTO foodImageDTO = new FoodImageDTO();
            foodImageDTO.setId(image.getId());
            foodImageDTO.setImageKey(image.getImageKey());
            foodImageDTO.setImageName(image.getImageName());
            foodImageDTO.setImageType(image.getImageType());
            foodImageDTO.setFoodId(image.getAvailableFood().getId());
            foodImageDTO.setImageUrl(url);
//            foodImageDTO.setImageUrl(s3Service.getImageUrl(image.getImageKey()));

            imageDTOList.add(foodImageDTO); // 🔥 YOU MISSED THIS

        }
        AvailableFoodDTO dto = dataMapper.ModelToDto(availableFood);

        dto.setFoodImageDTOList(imageDTOList); // 🔥 THIS LINE IS MISSING

        return dto;
    }



    public PaginationResponse<AvailableFoodDTO> findAll(int page, int size) {

        Page<AvailableFood> availableFoods = fetchFoods(page, size);

        Map<Long, List<FoodImage>> imageMap = fetchImage(availableFoods);

        List<AvailableFoodDTO> content = mapToDto(availableFoods, imageMap);

        return buildResponses(page, size, availableFoods, content);
    }

    private PaginationResponse<AvailableFoodDTO> buildResponses(
            int page,
            int size,
            Page<AvailableFood> availableFoods,
            List<AvailableFoodDTO> content) {

        return new PaginationResponse<>(
                page,
                size,
                availableFoods.getTotalElements(),
                content
        );
    }

    private List<AvailableFoodDTO> mapToDto(
            Page<AvailableFood> availableFoods,
            Map<Long, List<FoodImage>> imageMap) {

        return availableFoods.getContent()
                .stream()
                .map(food -> {

                    AvailableFoodDTO dto = dataMapper.ModelToDto(food);

                    List<FoodImage> images = imageMap.get(food.getId());

                    if (images != null) {
                        List<FoodImageDTO> imageDTOList = images.stream()
                                .map(img -> {
                                    FoodImageDTO imageDTO = new FoodImageDTO();
                                    imageDTO.setId(img.getId());
                                    imageDTO.setImageKey(img.getImageKey());
                                    imageDTO.setImageName(img.getImageName());
                                    imageDTO.setImageType(img.getImageType());
                                    imageDTO.setImageUrl(s3Service.getImageUrl(img.getImageKey()));
                                    return imageDTO;
                                }).toList();

                        dto.setFoodImageDTOList(imageDTOList);
                    }

                    return dto;
                })
                .toList();
    }

    private Page<AvailableFood> fetchFoods(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size); // 🔥 FIX (0-based)
        return availableFoodRepository.findAllByActive(true, pageable);
    }

    private Map<Long, List<FoodImage>> fetchImage(Page<AvailableFood> availableFoods) {

        List<Long> foodId = extractFoodIds(availableFoods);

        return foodImageRepository.findByAvailableFood_IdIn(foodId)
                .stream()
                .collect(Collectors.groupingBy(v -> v.getAvailableFood().getId()));
    }

    private List<Long> extractFoodIds(Page<AvailableFood> availableFoods) {
        return availableFoods.getContent()
                .stream()
                .map(AvailableFood::getId)
                .toList();
    }
}
