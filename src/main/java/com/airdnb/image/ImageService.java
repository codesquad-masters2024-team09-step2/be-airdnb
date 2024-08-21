package com.airdnb.image;

import com.airdnb.global.exception.NotFoundException;
import com.airdnb.image.entity.Image;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;

    @Transactional(readOnly = true)
    public Image findImageById(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("id와 일치하는 이미지가 존재하지 않습니다."));
    }
}
