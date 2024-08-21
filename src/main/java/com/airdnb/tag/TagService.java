package com.airdnb.tag;

import com.airdnb.global.exception.NotFoundException;
import com.airdnb.tag.entity.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class TagService {
    private final TagRepository tagRepository;

    @Transactional(readOnly = true)
    public Tag findTagById(Long tagId) {
        return tagRepository.findById(tagId).orElseThrow(() -> new NotFoundException("id와 일치하는 태그를 찾을 수 없습니다."));
    }
}
