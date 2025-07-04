package com.example.vegetabledragon.service;

import com.example.vegetabledragon.apiPayload.code.status.ErrorStatus;
import com.example.vegetabledragon.apiPayload.exception.GeneralException;
import com.example.vegetabledragon.domain.Comment;
import com.example.vegetabledragon.domain.UserType;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CheckPermissionServiceImpl {

    // 익명 댓글의 경우 비밀번호 검증
    public void checkPermissionForAnonymous(Comment comment, String sessionUsername, String password){
        if (comment.getUserType() == UserType.ANONYMOUS) { // 기존에 writer == "익명" 으로 비교했었는데, 하드코딩으로 비교하는 게 적절하지 않다고 생각해서 수정함.
            if (!comment.getPassword().equals(password)){
                throw new GeneralException(ErrorStatus.COMMENT_NOT_PERMISSION);
            }
        }
    }

    // 일반 댓글의 경우 작성자와 login한 User가 일치하는지 확인
    public void checkPermissionForAuthenticatedUser(Comment comment, String sessionUsername) {
        if (comment.getUser() == null || !comment.getUser().getUsername().equals(sessionUsername)) {
            throw new GeneralException(ErrorStatus.COMMENT_NOT_PERMISSION);
        }
    }


    public void validateCommentPermission(Comment comment, String sessionUsername, String password) {
        if (comment.getUser() == null) {
            checkPermissionForAnonymous(comment, sessionUsername, password);
        } else {
            checkPermissionForAuthenticatedUser(comment, sessionUsername);
        }
    }
}
