package com.tripbook.main.article.service;

import com.tripbook.main.article.dto.ArticleRequestDto;
import com.tripbook.main.article.dto.ArticleResponseDto;
import com.tripbook.main.article.entity.Article;
import com.tripbook.main.article.entity.ArticleImage;
import com.tripbook.main.article.enums.ArticleStatus;
import com.tripbook.main.article.repository.ArticleImageRepository;
import com.tripbook.main.article.repository.ArticleRepository;
import com.tripbook.main.file.service.UploadService;
import com.tripbook.main.global.entity.Image;
import com.tripbook.main.global.enums.ErrorCode;
import com.tripbook.main.global.exception.CustomException;
import com.tripbook.main.global.repository.ImageRepository;
import com.tripbook.main.member.entity.Member;
import com.tripbook.main.member.repository.MemberRepository;
import com.tripbook.main.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService{
    private final MemberService memberService;
    private final UploadService uploadService;
    private final ArticleRepository articleRepository;
    private final ImageRepository imageRepository;
    private final ArticleImageRepository articleImageRepository;
    @Override
    @Transactional
    public ArticleResponseDto.ArticleResponse saveArticle(ArticleRequestDto.ArticleSaveRequest requestDto, OAuth2User principal) {
        String email = principal.getName();
        Member loginMember = memberService.getMemberByEmail(email);

        if (loginMember.isNotEditor()) {
            throw new CustomException.MemberNotPermittedException(ErrorCode.MEMBER_NOT_PERMITTED.getMessage(), ErrorCode.MEMBER_NOT_PERMITTED);
        }

        Article article = articleRepository.save(Article.builder()
                                                        .title(requestDto.getTitle())
                                                        .content(requestDto.getContent())
                                                        .member(loginMember)
                                                        .status(ArticleStatus.ACTIVE)
                                                        .build());

        ArticleResponseDto.ArticleResponse response = article.toDto(loginMember);

        //TODO file path, Image name, article imageList 추가
        List<ArticleImage> imageList = requestDto.getImageList().stream()
                .map(file -> uploadService.imageUpload(file, "article"))
                .map(url -> imageRepository.save(Image.builder().url(url).build()))
                .map(image -> articleImageRepository.save(ArticleImage.builder().image(image).article(article).build()))
                .toList();

        response.setImageList(imageList);
        return response;
    }

}
