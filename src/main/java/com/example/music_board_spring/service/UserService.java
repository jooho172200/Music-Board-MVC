package com.example.music_board_spring.service;


import com.example.music_board_spring.exception.UserNotFoundException;
import com.example.music_board_spring.model.dto.UserUpdateDTO;
import com.example.music_board_spring.model.entity.Users;
import com.example.music_board_spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    //spring security에서 제공하는 기능
    @Autowired
    private PasswordEncoder passwordEncoder;

    //회원 등록
    public Users registerUser(Users users){
        // 1. 유저네임 중복 체크
        if (userRepository.findByUsername(users.getUsername()).isPresent()) {
            throw new RuntimeException("이미 사용 중인 유저네임입니다.");
        }

        // 2. 이메일 중복 체크
        if (userRepository.findByEmail(users.getEmail()).isPresent()) {
            throw new RuntimeException("이미 사용 중인 이메일입니다.");
        }

        // 3. 비밀번호 암호화
        users.setPasswordHash(passwordEncoder.encode(users.getPasswordHash()));

        // 4. 사용자 저장
        return userRepository.save(users);
    }

    //로그인 기능
    public Users authenticateUser(String username, String password) throws BadCredentialsException {
        Optional<Users> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent() && passwordEncoder.matches(password, optionalUser.get().getPasswordHash())) {
            return optionalUser.get();
        } else {
            throw new BadCredentialsException("유저네임이나 비밀번호를 틀렸습니다.");
        }
    }

    //아이디로 회원 찾기
    public Optional<Users> findById(Integer id) {
        return userRepository.findById(id);
    }

    //유저네임으로 회원 찾기
    public Optional<Users> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    //전체 회원 목록 반환
    public List<Users> findAll() {
        return userRepository.findAll();
    }

    //회원 정보 수정
    public Users updateUserInfo(Integer id, UserUpdateDTO updateDTO) {
        // 사용자 ID로 사용자 정보를 조회
        return userRepository.findById(id)
                .map(user -> {  //user 객체에 저장된 값을 변환, user=람다 표현식 매개변수
                    boolean isChanged = false;  // 변경 사항이 있는지 확인하는 플래그

                    // 유저네임 업데이트
                    // 유저네임이 제공되고 새 유저네임이 현재 유저 네임과 다를 시
                    if (updateDTO.getUsername() != null && !updateDTO.getUsername().equals(user.getUsername())) {
                        // 새로운 사용자 이름이 이미 존재하는지 확인
                        if (userRepository.findByUsername(updateDTO.getUsername()).isPresent()) {
                            throw new RuntimeException("이미 사용 중인 유저네임입니다."); // 중복일 경우 예외 발생
                        }
                        user.setUsername(updateDTO.getUsername()); // 사용자 이름 업데이트
                        isChanged = true; // 변경 사항이 있음을 표시
                    }

                    // 이메일 업데이트
                    // 이메일이 제공되고 새 이메일이 현재 이메일과 다를 시
                    if (updateDTO.getEmail() != null && !updateDTO.getEmail().equals(user.getEmail())) {
                        // 새로운 이메일이 이미 존재하는지 확인
                        if (userRepository.findByEmail(updateDTO.getEmail()).isPresent()) {
                            throw new RuntimeException("이미 사용 중인 이메일입니다."); // 중복일 경우 예외 발생
                        }
                        // 이메일 형식이 유효한지 체크
                        if (!isValidEmail(updateDTO.getEmail())) {
                            throw new RuntimeException("유효하지 않은 이메일 형식입니다."); // 형식이 잘못된 경우 예외 발생
                        }
                        user.setEmail(updateDTO.getEmail()); // 이메일 업데이트
                        isChanged = true; // 변경 사항이 있음을 표시
                    }

                    // 비밀번호 업데이트
                    // 비밀번호가 제공되고 새 비밀번호가 기존 비밀번호와 다를 시
                    if (updateDTO.getNewPassword() != null && !updateDTO.getNewPassword().isEmpty()) {
                        // 비밀번호 형식이 유효한지 체크
                        if (!isValidPassword(updateDTO.getNewPassword())) {
                            throw new RuntimeException("유효하지 않은 비밀번호 형식입니다."); // 형식이 잘못된 경우 예외 발생
                        }
                        user.setPasswordHash(passwordEncoder.encode(updateDTO.getNewPassword())); // 비밀번호 해시 업데이트
                        isChanged = true; // 변경 사항이 있음을 표시
                    }

                    // 프로필 사진 업데이트
                    // 프로필 사진이 제공되고 새 프로필 사진이 현재 프로필 사진과 다른지
                    if (updateDTO.getProfilePicture() != null) {
                        user.setProfilePicture(updateDTO.getProfilePicture()); // 프로필 사진 업데이트
                        isChanged = true; // 변경 사항이 있음을 표시
                    }

                    // 변경 사항이 있을 경우 사용자 정보를 저장하고 반환, 없을 경우 기존 사용자 반환
                    return isChanged ? userRepository.save(user) : user;
                })
                // 사용자를 찾을 수 없는 경우 예외 발생
                .orElseThrow(() -> new UserNotFoundException("ID가 " + id + "인 사용자를 찾을 수 없습니다."));
    }

    //이메일 유효성 확인
    private boolean isValidEmail(String email) {
        // 간단한 이메일 형식 검사
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    //비밀번호 유효성 확인
    private boolean isValidPassword(String password) {
        boolean passwordValid = false;

        // 비밀번호는 최소 8자 이상이어야 함, 비밀번호의 조건이 추가될 수 있어 if문을 사용함
        if(password.length() >= 8){
            passwordValid = true;
        }

        return passwordValid;
    }

    //회원 삭제
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    //회원 활성화
    public Users activateUser(Integer id) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setActive(true);
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new UserNotFoundException("id: " + id + "인 사용자를 찾을 수 없습니다."));
    }

    //회원 비활성화
    public Users deactivateUser(Integer id) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setActive(false);
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new UserNotFoundException("id: " + id + "인 사용자를 찾을 수 없습니다."));
    }

}
