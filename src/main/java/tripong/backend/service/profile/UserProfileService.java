package tripong.backend.service.profile;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tripong.backend.dto.profile.UserProfileRequestDto;
import tripong.backend.dto.profile.UserProfileResponseDto;
import tripong.backend.entity.user.User;
import tripong.backend.exception.post.PostErrorMessage;
import tripong.backend.repository.post.PostRepository;
import tripong.backend.repository.user.UserRepository;
import tripong.backend.service.aws.AmazonS3Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserProfileService {

    private final PostRepository postRepository;

    private final UserRepository userRepository;

    private final AmazonS3Service amazonS3Service;

    public UserProfileResponseDto getUserProfile(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException(PostErrorMessage.USER_ID_NOT_MATCH.name()));
        UserProfileResponseDto userProfileResponseDto = new UserProfileResponseDto(user);
        String fileName = user.getPicture();
        if (fileName != null){
            userProfileResponseDto.setPicture(amazonS3Service.getFile(fileName));
        }

        return userProfileResponseDto;
    }

    @Transactional
    public User updateUserProfile(Long userId, UserProfileRequestDto userProfileRequestDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException(PostErrorMessage.USER_ID_NOT_MATCH.name()));

        MultipartFile picture = userProfileRequestDto.getPicture();
        String pictureUrl = null;
        if (picture != null && !picture.isEmpty()){
            pictureUrl = amazonS3Service.uploadFile(userProfileRequestDto.getPicture());
        }

        String fileName = user.getPicture();
        if (fileName != null){
            amazonS3Service.deleteFile(fileName);
        }

        user.update(userProfileRequestDto, pictureUrl);

        return user;
    }

}